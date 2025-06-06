package model.entities;

import connection.Database;
import java.sql.*;
import controller.Sistema;
import java.util.ArrayList;
import java.util.List;
import utils.HashUtil;
import model.entities.Competencia;

public class Usuario {

    private int idUsuario;
    private String nome;
    private String email;
    private String senhaHash;
    private int idTipo;
    private List<Competencia> competencia;

    public void login(String email, String senhaHash) {
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public void logout() {
        this.idUsuario = 0;
        this.nome = "";
        this.email = "";
        this.senhaHash = "";
        this.idTipo = 0;
        if (this.competencia != null) {
            this.competencia.clear();
        }
    }

    public void adicionarCompetencia(List<Competencia> competencias) {
        this.competencia.addAll(competencias);
    }

    public void removerCompetencia(List<Competencia> competencias) {
        this.competencia.removeAll(competencias);
    }

    public void listarCompetencia() {

    }

    public void isLider() {

    }

    public void recuperarSenha(String senha) {
        this.senhaHash = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public List<Competencia> getCompetencia() {
        return competencia;
    }

    public void setCompetencia(List<Competencia> competencia) {
        this.competencia = competencia;
    }

    public List<Usuario> buscarUsuariosPorNomeOuEmail(String filtro) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE LOWER(nome) LIKE ? OR LOWER(email) LIKE ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String filtroLike = "%" + filtro.toLowerCase() + "%";
            stmt.setString(1, filtroLike);
            stmt.setString(2, filtroLike);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                // ... outros campos
                usuarios.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public boolean verificarUsuarioNaEquipe(int idUsuario, int idEquipe) throws SQLException {
        String sql = "SELECT COUNT(*) FROM membros_equipes WHERE id_usuario = ? AND id_equipe = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idEquipe);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void adicionarUsuarioNaEquipe(int idUsuario, int idEquipe) throws SQLException {
        String sql = "INSERT INTO membros_equipes (id_usuario, id_equipe) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idEquipe);
            stmt.executeUpdate();
        }
    }

    public void tornarLider(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET id_tipo = 2 WHERE id_usuario = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }
    }

    public List<Usuario> buscarUsuariosPorNomeEmailECompetencia(String texto, Integer idCompetencia) throws SQLException {
        List<Usuario> lista = new ArrayList<>();

        String query = """
        SELECT DISTINCT u.* FROM usuarios u
        LEFT JOIN usuario_competencia uc ON u.id_usuario = uc.id_usuario
        WHERE (u.nome LIKE ? OR u.email LIKE ?)
    """;

        if (idCompetencia != null) {
            query += " AND uc.id_competencia = ?";
        }

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + texto + "%");
            stmt.setString(2, "%" + texto + "%");

            if (idCompetencia != null) {
                stmt.setInt(3, idCompetencia);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                // preencha outros campos se necessário
                lista.add(u);
            }
        }

        return lista;
    }

}
