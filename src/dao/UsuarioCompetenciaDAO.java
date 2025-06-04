/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import connection.Database;
import java.sql.*;

/**
 *
 * @author guico
 */
public class UsuarioCompetenciaDAO {

    public List<Integer> buscarIdsPorUsuario(int idUsuario) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT id_competencia FROM usuario_competencia WHERE id_usuario = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getInt("id_competencia"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
