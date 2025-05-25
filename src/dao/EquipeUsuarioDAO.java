package dao;

import connection.Database;
import java.sql.*;

public class EquipeUsuarioDAO {

    public void adicionarUsuarioAEquipe(int idEquipe, int idUsuario) throws SQLException {
        String sql = "INSERT INTO equipe_usuarios (id_equipe, id_usuario) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEquipe);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        }
    }

    public void removerUsuarioDaEquipe(int idEquipe, int idUsuario) throws SQLException {
        String sql = "DELETE FROM equipe_usuarios WHERE id_equipe = ? AND id_usuario = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEquipe);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        }
    }
}
