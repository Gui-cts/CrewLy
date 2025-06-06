package controller;

import model.Usuario;
import model.Equipe;
import model.Tarefa;
import utils.HashUtil;
import utils.EmailSender;
import utils.TokenUtil;
import connection.Database;

import dao.EquipeDAO;
import dao.EquipeUsuarioDAO;
import dao.TarefaDAO;
import dao.TarefaResponsavelDAO;
import dao.CompetenciaDAO;
import dao.UsuarioCompetenciaDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.MessagingException;
import model.Competencia;

public class Sistema {

    private CompetenciaDAO competenciaDAO;
    private UsuarioCompetenciaDAO usuarioCompetenciaDAO;
    private EquipeDAO equipeDAO = new EquipeDAO();
    private EquipeUsuarioDAO equipeUsuarioDAO = new EquipeUsuarioDAO();
    private TarefaDAO tarefaDAO = new TarefaDAO();
    private TarefaResponsavelDAO tarefaResponsavelDAO = new TarefaResponsavelDAO();

    public Sistema() {
        this.competenciaDAO = new CompetenciaDAO();
        this.usuarioCompetenciaDAO = new UsuarioCompetenciaDAO();
    }

    // =================== EXISTENTES ====================
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

    public String gerarTokenRecuperacao(String email) throws MessagingException {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT nome FROM usuarios WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String token = TokenUtil.gerarToken();
                TokenUtil.salvarTokenNoBanco(email, token);

                // Mensagem personalizada para recuperação de senha
                String assunto = "Recuperação de Senha - Crewly";
                String corpo = "Olá " + nome + ",\n\n"
                        + "Recebemos uma solicitação para redefinir sua senha no sistema Crewly.\n"
                        + "Use o código abaixo para concluir o processo de recuperação:\n\n"
                        + "Código: " + token + "\n\n"
                        + "Se você não solicitou isso, ignore este e-mail.\n\n"
                        + "Atenciosamente,\nEquipe Crewly";

                EmailSender.enviarToken(email, token, assunto, corpo);
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
            String sql = "UPDATE usuarios SET nome = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, usuario.getEmail());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirUsuarioPorEmail(String email) {
        String sql = "DELETE FROM usuarios WHERE email = ?";
        try (Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
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

    // =================== NOVOS MÉTODOS ====================
    // ===== EQUIPES =====
    public void criarEquipe(String nome, String descricao, int idLider) throws SQLException {
        equipeDAO.criarEquipe(nome, idLider, descricao);
    }

    public List<Equipe> listarEquipesPorLider(int idLider) throws SQLException {
        return equipeDAO.listarEquipesPorLider(idLider);
    }

    public void adicionarUsuarioNaEquipe(int idEquipe, int idUsuario) throws SQLException {
        equipeUsuarioDAO.adicionarUsuarioAEquipe(idEquipe, idUsuario);
    }

    public void removerUsuarioDaEquipe(int idEquipe, int idUsuario) throws SQLException {
        equipeUsuarioDAO.removerUsuarioDaEquipe(idEquipe, idUsuario);
    }

    // ===== TAREFAS =====
    public void criarTarefa(Tarefa tarefa) throws SQLException {
        tarefaDAO.criarTarefa(tarefa);
    }

    public List<Tarefa> listarTarefasDaEquipe(int idEquipe) throws SQLException {
        return tarefaDAO.listarTarefasPorEquipe(idEquipe);
    }

    public void marcarTarefaComoConcluida(int idTarefa) throws SQLException {
        tarefaDAO.marcarComoConcluida(idTarefa);
    }

    public void deletarTarefa(int idTarefa) throws SQLException {
        tarefaDAO.deletarTarefa(idTarefa);
    }

    // ===== RESPONSÁVEIS =====
    public void adicionarResponsavelATarefa(int idTarefa, int idUsuario) throws SQLException {
        tarefaResponsavelDAO.adicionarResponsavelATarefa(idTarefa, idUsuario);
    }

    public void removerResponsavelDaTarefa(int idTarefa, int idUsuario) throws SQLException {
        tarefaResponsavelDAO.removerResponsavelDaTarefa(idTarefa, idUsuario);
    }

    public List<String> buscarCompetencias(int idUsuario) {
        List<String> competencias = new ArrayList<>();

        String sql = "SELECT c.nome_competencia FROM competencia c "
                + "JOIN usuario_competencia uc ON c.id_competencia = uc.id_competencia "
                + "WHERE uc.id_usuario = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    competencias.add(rs.getString("nome_competencia"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return competencias;
    }

    public List<Competencia> buscarTodasCompetencias() {
        return competenciaDAO.buscarTodas();
    }

    public List<Integer> buscarCompetenciasPorUsuario(int idUsuario) {
        return usuarioCompetenciaDAO.buscarIdsPorUsuario(idUsuario);
    }

}
