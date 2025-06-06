package model;

import java.util.List;

public class Equipe {
    private int idEquipe;
    private String nome;
    private String descricao;  // ✅ Adicionado
    private int idLider;  // id do usuário líder
    private List<Usuario> membros;  // opcional
    private List<Tarefa> tarefas;   // opcional

    // Getters e Setters
    public int getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {  // ✅ Getter adicionado
        return descricao;
    }

    public void setDescricao(String descricao) {  // ✅ Setter adicionado
        this.descricao = descricao;
    }

    public int getIdLider() {
        return idLider;
    }

    public void setIdLider(int idLider) {
        this.idLider = idLider;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
}
