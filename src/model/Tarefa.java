package model;

import java.time.LocalDate;
import java.util.List;

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
}
