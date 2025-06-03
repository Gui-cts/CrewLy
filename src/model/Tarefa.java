package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import connection.Database;

public class Tarefa {

    private int idTarefa;
    private String titulo;
    private String descricao;
    private LocalDate dataLimite;
    private boolean concluida;
    private int idEquipe;
    private List<Usuario> responsaveis;
    private List<ArquivoUpload> arquivosEnviados;

    // Getters e Setters
    public int getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        this.idTarefa = idTarefa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public int getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }

    public List<Usuario> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(List<Usuario> responsaveis) {
        this.responsaveis = responsaveis;
    }

    public List<ArquivoUpload> getArquivosEnviados() {
        return arquivosEnviados;
    }

    public void setArquivosEnviados(List<ArquivoUpload> arquivosEnviados) {
        this.arquivosEnviados = arquivosEnviados;
    }

    // Funções de manipulação de responsáveis
    public boolean verificarUsuarioResponsavel(int idUsuario, int idTarefa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM responsaveis_tarefas WHERE id_usuario = ? AND id_tarefa = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idTarefa);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void adicionarResponsavel(int idUsuario, int idTarefa) throws SQLException {
        String sql = "INSERT INTO responsaveis_tarefas (id_tarefa, id_usuario) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTarefa);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        }
    }

    public List<Usuario> buscarResponsaveis() throws SQLException {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT u.id_usuario, u.nome, u.email FROM usuarios u "
                + "JOIN responsaveis_tarefas rt ON u.id_usuario = rt.id_usuario "
                + "WHERE rt.id_tarefa = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.idTarefa);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                lista.add(u);
            }
        }

        return lista;
    }

    public boolean usuarioEstaNaEquipe(int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM membros_equipes WHERE id_equipe = ? AND id_usuario = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.idEquipe);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void concluirTarefa() throws SQLException {
        String sql = "UPDATE tarefas SET concluida = ?, data_conclusao = ? WHERE id_tarefa = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setInt(3, this.getIdTarefa());
            stmt.executeUpdate();
            this.setConcluida(true);
        }
    }

    public List<RelatorioTarefa> gerarRelatorioDoLider(int idLider) throws SQLException {
        List<RelatorioTarefa> relatorio = new ArrayList<>();

        String sql = "SELECT e.nome AS nome_equipe, t.titulo, t.concluida, t.data_limite, t.data_conclusao "
                + "FROM equipes e "
                + "JOIN tarefas t ON e.id_equipe = t.id_equipe "
                + "WHERE e.id_lider = ?";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLider);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RelatorioTarefa item = new RelatorioTarefa();
                item.setNomeEquipe(rs.getString("nome_equipe"));
                item.setTituloTarefa(rs.getString("titulo"));
                item.setConcluida(rs.getBoolean("concluida"));

                Date dataLimite = rs.getDate("data_limite");
                if (dataLimite != null) {
                    item.setDataLimite(dataLimite.toLocalDate());
                }

                Timestamp dataConclusao = rs.getTimestamp("data_conclusao");
                if (dataConclusao != null) {
                    item.setDataConclusao(dataConclusao.toLocalDateTime());
                }

                // Lógica de status do prazo
                LocalDate hoje = LocalDate.now();
                String statusPrazo;
                if (item.isConcluida()) {
                    if (item.getDataConclusao() != null &&
                            !item.getDataConclusao().toLocalDate().isAfter(item.getDataLimite())) {
                        statusPrazo = "No prazo";
                    } else {
                        statusPrazo = "Atrasada";
                    }
                } else {
                    if (item.getDataLimite() != null && !hoje.isAfter(item.getDataLimite())) {
                        statusPrazo = "No prazo";
                    } else {
                        statusPrazo = "Atrasada";
                    }
                }

                item.setStatusPrazo(statusPrazo);
                relatorio.add(item);
            }
        }

        return relatorio;
    }
}
