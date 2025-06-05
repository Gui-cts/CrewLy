package dao;

import model.Equipe;
import connection.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    public void criarEquipe(String nome, int idLider, String descricao) throws SQLException {
        String sql = "INSERT INTO equipes (nome, id_lider, descricao) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, idLider);
            stmt.setString(3, descricao);
            stmt.executeUpdate();
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
