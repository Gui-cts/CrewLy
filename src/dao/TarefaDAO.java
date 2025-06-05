package dao;

import connection.Database;
import model.Tarefa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;

public class TarefaDAO {

    public void criarTarefa(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO tarefas (id_equipe, titulo, descricao, data_limite, concluida) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, tarefa.getIdEquipe());
            stmt.setString(2, tarefa.getTitulo());
            stmt.setString(3, tarefa.getDescricao());
            stmt.setDate(4, Date.valueOf(tarefa.getDataLimite()));  // LocalDate para Date
            stmt.setBoolean(5, tarefa.isConcluida());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                tarefa.setIdTarefa(rs.getInt(1));
            }
        }
    }

    public List<Tarefa> listarTarefasPorEquipe(int idEquipe) throws SQLException {
        String sql = "SELECT * FROM tarefas WHERE id_equipe = ?";
        List<Tarefa> tarefas = new ArrayList<>();

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEquipe);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setIdTarefa(rs.getInt("id_tarefa"));
                tarefa.setTitulo(rs.getString("titulo"));
                tarefa.setDescricao(rs.getString("descricao"));
                tarefa.setDataLimite(rs.getDate("data_limite").toLocalDate());
                tarefa.setConcluida(rs.getBoolean("concluida"));
                tarefa.setIdEquipe(rs.getInt("id_equipe"));

                // Aqui carrega os responsáveis
                List<Usuario> responsaveis = listarResponsaveisDaTarefa(tarefa.getIdTarefa());
                tarefa.setResponsaveis(responsaveis);

                tarefas.add(tarefa);
            }
        }
        return tarefas;
    }

    public List<Usuario> listarResponsaveisDaTarefa(int idTarefa) throws SQLException {
        String sql = "SELECT u.* FROM usuarios u "
                + "INNER JOIN responsaveis_tarefas tr ON u.id_usuario = tr.id_usuario "
                + "WHERE tr.id_tarefa = ?";
        List<Usuario> responsaveis = new ArrayList<>();

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarefa);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNome(rs.getString("nome"));
                // preencha mais campos se tiver
                responsaveis.add(u);
            }
        }
        return responsaveis;
    }

    public void marcarComoConcluida(int idTarefa) throws SQLException {
        String sql = "UPDATE tarefas SET concluida = ? WHERE id_tarefa = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, true);
            stmt.setInt(2, idTarefa);
            stmt.executeUpdate();
        }
    }

    public void deletarTarefa(int idTarefa) throws SQLException {
        String sql = "DELETE FROM tarefas WHERE id_tarefa = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarefa);
            stmt.executeUpdate();
        }
    }
}
