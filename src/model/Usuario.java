package model;

import controller.Sistema;
import java.util.List;
import utils.HashUtil;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
    private String senhaHash;
    private Tipo tipo;
    private List<Competencia> competencia;

    public void login(String email, String senhaHash) {
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public void logout() {
        this.email = "";
        this.senhaHash = "";
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

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public List<Competencia> getCompetencia() {
        return competencia;
    }

    public void setCompetencia(List<Competencia> competencia) {
        this.competencia = competencia;
    }
}
