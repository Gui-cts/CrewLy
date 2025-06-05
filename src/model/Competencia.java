package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connection.Database;

public class Competencia {

    private int id;
    private String nome;

    public Competencia(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Competencia() {
        // Construtor vazio
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    public static List<Competencia> buscarTodas() {
        List<Competencia> competencias = new ArrayList<>();
        String sql = "SELECT id_competencia, nome FROM competencia";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_competencia");
                String nome = rs.getString("nome");
                competencias.add(new Competencia(id, nome));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Substitua por logger se desejar
        }

        return competencias;
    }
}
