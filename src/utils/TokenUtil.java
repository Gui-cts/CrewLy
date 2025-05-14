package utils;

import connection.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TokenUtil {

    // Gera um token único
    public static String gerarToken() {
        return UUID.randomUUID().toString();
    }

    // Salva o token no banco junto com o email e a data de expiração
    public static void salvarTokenNoBanco(String email, String token) {
        String sql = "INSERT INTO tokens_redefinicao (email, token, data_expiracao) VALUES (?, ?, ?)";
        
        // Define a expiração para 30 minutos
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(30);
        String dataExpiracaoFormatada = expiracao.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, token);
            stmt.setString(3, dataExpiracaoFormatada);

            stmt.executeUpdate();
            System.out.println("Token salvo no banco com sucesso.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean validarToken(String email, String token) {
    String sql = "SELECT * FROM tokens_redefinicao WHERE email = ? AND token = ? AND data_expiracao > NOW()";

    try (Connection conn = Database.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        stmt.setString(2, token);

        ResultSet rs = stmt.executeQuery();
        boolean valido = rs.next();
        rs.close();

        return valido;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    public static void removerToken(String email) {
    String sql = "DELETE FROM tokens_redefinicao WHERE email = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
