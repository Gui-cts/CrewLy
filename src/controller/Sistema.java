package controller;

import model.Usuario;
import utils.HashUtil;
import connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Tipo;

public class Sistema {
    
    // Método para autenticar o usuário
    public Usuario autenticarUsuario(String email, String senha) {
    String sql = "SELECT * FROM usuarios WHERE email = ? AND senha_hash = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        stmt.setString(2, HashUtil.gerarHash(senha));  // Gera hash da senha diretamente aqui

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("id_usuario"));
            usuario.setNome(rs.getString("nome"));
            usuario.setEmail(rs.getString("email"));
            // Pode carregar mais dados do usuário aqui, como tipo etc.
            return usuario;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

}
