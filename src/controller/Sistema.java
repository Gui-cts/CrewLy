package controller;

import model.Usuario;
import utils.HashUtil;
import connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import model.Tipo;
import utils.EmailSender;
import utils.TokenUtil;

public class Sistema {

    public Usuario autenticarUsuario(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha_hash = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, HashUtil.gerarHash(senha));  // Gera hash da senha diretamente aqui

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setSenhaHash(rs.getString("senha_hash"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setIdTipo(rs.getInt("id_tipo"));
                // lista de dados, adicionar aqui
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String gerarTokenRecuperacao(String email) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String token = TokenUtil.gerarToken(); // ✅ Gera token UUID
                TokenUtil.salvarTokenNoBanco(email, token); // ✅ Salva no banco
                EmailSender.enviarToken(email, token); // ✅ Envia por e-mail
                return token;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean redefinirSenha(String email, String novaSenha) {
        try (Connection conn = Database.getConnection()) {
            String hashSenha = HashUtil.gerarHash(novaSenha);
            String sql = "UPDATE usuarios SET senha_hash = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, hashSenha);
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean redefinirNome(String nome, Usuario usuario) {
        try (Connection conn = Database.getConnection()) {
            String novoNome = nome;
            String sql = "UPDATE usuarios SET nome = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novoNome);
            stmt.setString(2, usuario.getEmail());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //adicionar metodo para mudar email
    public boolean excluirUsuarioPorEmail(String email) {
        String sql = "DELETE FROM usuarios WHERE email = ?";
        try (Connection con = Database.getConnection(); 
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validarToken(String email, String token) {
        return TokenUtil.validarToken(email, token);
    }

    public void removerToken(String email) {
        TokenUtil.removerToken(email);
    }

}
