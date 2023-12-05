package Model.Entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static Application.Util.dateTimeFormatter;

public class Pessoa {

    private String nome;
    private String email;
    private String senha;
    private Integer id;
    private String telefone;
    private LocalDate nascimento;

    public Pessoa(String nome, String email, String senha, Integer id, String telefone, LocalDate nascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.id = id;
        this.telefone = telefone;
        this.nascimento = nascimento;
    }


    @Override
    public String toString() {
        return "Pessoa{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", id=" + id +
                ", telefone='" + telefone + '\'' +
                ", nascimento=" + nascimento +
                '}';
    }

    public Pessoa() {
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }
}
