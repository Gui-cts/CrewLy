/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import connection.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entities.Competencia;

public class CompetenciaDAO {
    public List<Competencia> buscarTodas() {
    List<Competencia> competencias = new ArrayList<>();
    String sql = "SELECT * FROM competencia";

    try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            competencias.add(new Competencia(rs.getInt("id_competencia"), rs.getString("nome")));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return competencias;
}

}
