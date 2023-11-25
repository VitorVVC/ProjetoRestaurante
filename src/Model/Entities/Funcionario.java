package Model.Entities;

import Model.Exceptions.DomainException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;

import static Application.Util.*;

public class Funcionario extends Pessoa implements InterfaceFuncionario {
    List<Funcionario> funcionarios = new ArrayList<>();

    public Funcionario(String nome, String email, String senha, Integer id, String telefone, LocalDate nascimento) {
        super(nome, email, senha, id, telefone, nascimento);
    }

    public Funcionario() {
    }

    @Override
    public Funcionario metodoCriarContaFuncionario() {
        String nome;
        String loginEmail;
        String loginSenha;
        String loginSenhaConfirm;
        String telefone;
        try {
            System.out.print("Qual seu nome: ");
            nome = sc.nextLine();
            System.out.println("Olá " + nome + ", seja muito bem vindo!");
            System.out.print("Forneça seu email: ");
            loginEmail = sc.nextLine();
            System.out.print("Forneça uma senha: ");
            loginSenha = sc.nextLine();
            System.out.print("Confirme a senha escrevendo-a novamente: ");
            loginSenhaConfirm = sc.nextLine();
            System.out.print("Digite também o seu telefone: ");
            telefone = sc.nextLine();
        } catch (InputMismatchException e) {
            throw new DomainException("Valor escrito não foi lido corretamente");
        }
        while (!loginSenha.equalsIgnoreCase(loginSenhaConfirm)) {
            System.out.print("As senhas não estão iguais, reescreva novamente: ");
            loginSenhaConfirm = sc.nextLine();
        }
        System.out.print("Agora forneca-me a sua data de nascimento (dd/MM/yyyy): ");
        LocalDate nascimento = LocalDate.parse(sc.nextLine(), dateTimeFormatter);

        Random geraID = new Random();
        int newId = 1000000 + geraID.nextInt(9000000);
        System.out.println("Seu ID é: " + newId);

        // Escrever no arquivo antes do return
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            FileWriter fileWriter = new FileWriter(file,true);
            fileWriter.write(nome + " " + loginEmail + " " + loginSenha + " " + newId);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Erro na escrita do progama.");
        } catch (RuntimeException e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
        // Retornar o novo funcionário
        return new Funcionario(nome, loginEmail, loginSenha, newId, telefone, nascimento);
    }


    @Override
    public void metodoLoginFuncionario() {
        System.out.println("Forneça seu ID, email e senha nas casas abaixo: ");
        System.out.println("ID: ");
        String id = sc.nextLine();
    }
}
