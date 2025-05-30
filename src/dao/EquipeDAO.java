package dao;

import model.Equipe;
import connection.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    public void criarEquipe(Equipe equipe) throws SQLException {
        String sql = "INSERT INTO equipes (nome, descricao, id_lider) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, equipe.getNome());
            stmt.setString(2, equipe.getDescricao());
            stmt.setInt(3, equipe.getIdLider());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                equipe.setIdEquipe(rs.getInt(1));
            }
        }
    }

    public List<Equipe> listarEquipesPorLider(int idLider) throws SQLException {
        String sql = "SELECT * FROM equipes WHERE id_lider = ?";
        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLider);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Equipe equipe = new Equipe();
                equipe.setIdEquipe(rs.getInt("id_equipe"));
                equipe.setNome(rs.getString("nome"));
                equipe.setDescricao(rs.getString("descricao"));
                equipe.setIdLider(rs.getInt("id_lider"));

                equipes.add(equipe);
            }
        }
        return equipes;
    }

    public void deletarEquipe(int idEquipe) throws SQLException {
        String sql = "DELETE FROM equipes WHERE id_equipe = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEquipe);
            stmt.executeUpdate();
        }
    }

    public List<Equipe> listarEquipesPorMembro(int idUsuario) throws SQLException {
        String sql = "SELECT e.* FROM equipes e "
                + "JOIN membros_equipes me ON e.id_equipe = me.id_equipe "
                + "WHERE me.id_usuario = ?";

        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Equipe equipe = new Equipe();
                equipe.setIdEquipe(rs.getInt("id_equipe"));
                equipe.setNome(rs.getString("nome"));
                equipe.setDescricao(rs.getString("descricao"));
                equipe.setIdLider(rs.getInt("id_lider"));

                equipes.add(equipe);
            }
        }
        return equipes;
    }
}
