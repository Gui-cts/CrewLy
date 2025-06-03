package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RelatorioTarefa {
    private String nomeEquipe;
    private String tituloTarefa;
    private boolean concluida;
    private LocalDate dataLimite;
    private LocalDateTime dataConclusao;
    private String statusPrazo;

    // Getters e Setters
    public String getNomeEquipe() {
        return nomeEquipe;
    }

    public void setNomeEquipe(String nomeEquipe) {
        this.nomeEquipe = nomeEquipe;
    }

    public String getTituloTarefa() {
        return tituloTarefa;
    }

    public void setTituloTarefa(String tituloTarefa) {
        this.tituloTarefa = tituloTarefa;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getStatusPrazo() {
        return statusPrazo;
    }

    public void setStatusPrazo(String statusPrazo) {
        this.statusPrazo = statusPrazo;
    }
}
