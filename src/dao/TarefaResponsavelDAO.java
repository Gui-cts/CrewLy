package dao;

import connection.Database;
import java.sql.*;

public class TarefaResponsavelDAO {

    public void adicionarResponsavelATarefa(int idTarefa, int idUsuario) throws SQLException {
        String sql = "INSERT INTO tarefa_responsaveis (id_tarefa, id_usuario) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarefa);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        }
    }

    public void removerResponsavelDaTarefa(int idTarefa, int idUsuario) throws SQLException {
        String sql = "DELETE FROM tarefa_responsaveis WHERE id_tarefa = ? AND id_usuario = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarefa);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        }
    }
}
