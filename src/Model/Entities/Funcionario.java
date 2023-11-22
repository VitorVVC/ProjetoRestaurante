package Model.Entities;

import Model.Exceptions.DomainException;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Random;

import static Application.Util.*;
public class Funcionario extends Pessoa{

    public Funcionario(String nome, String email, String senha, Integer id, String telefone, LocalDate nascimento) {
        super(nome, email, senha, id, telefone, nascimento);
    }
    @Override
    public Funcionario metodoLoginFuncionario() {
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
        System.out.println("Agora forneca-me a sua data de nascença (dd/MM/yyyy): ");
        LocalDate nascimento = LocalDate.parse(sc.nextLine(), dateTimeFormatter);

        Random geraID = new Random();
        int newId = 1000000 + geraID.nextInt(9000000);

        return new Funcionario(nome, loginEmail, loginSenha, newId, telefone, nascimento);
    }

}
