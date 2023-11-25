package Model.Entities;

import Model.Exceptions.DomainException;
import Model.Enums.TiposDePrato;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Random;

import static Application.Util.*;

public class Cliente extends Pessoa implements InterfaceCliente {

    public Cliente(String nome, String email, String senha, Integer id, String telefone, LocalDate nascimento) {
        super(nome, email, senha, id, telefone, nascimento);
    }

    public Cliente() {
    }

    @Override
    public Cliente metodoCriarContaCliente() {
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
        System.out.println("Agora forneca-me a sua data de nascimento (dd/MM/yyyy): ");
        LocalDate nascimento = LocalDate.parse(sc.nextLine(), dateTimeFormatter);

        Random geraID = new Random();
        int newId = 1000000 + geraID.nextInt(9000000);
        System.out.println("Seu ID é: " + newId);

        // Escrever no arquivo antes do return
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(nome + " " + loginEmail + " " + loginSenha + " " + newId);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Erro na escrita do progama.");
        } catch (RuntimeException e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

        return new Cliente(nome, loginEmail, loginSenha, newId, telefone, nascimento);
    }

    @Override
    public void metodoLoginCliente() {

    }

    @Override
    public void Pedido(Pedidos pedido) {
        System.out.println("Por favor leia nosso cardápio através do método cardapio e portanto decida qual será seu pedido.");
        System.out.print("Já leu nosso cardapio? (Sim ou não): ");
        String simOuNao;
        do {
            simOuNao = sc.nextLine();
            if (simOuNao.equalsIgnoreCase("sim")) {
                System.out.println("Pois bem, qual tipo de prato você deseja consumir? (Principal? Sobremesa ou Entrada?) ");
                TiposDePrato escolhaPrato = TiposDePrato.valueOf(sc.nextLine().toUpperCase());

            } else if (simOuNao.equalsIgnoreCase("não")) {
                Pedidos p = new Pedidos();
                p.cardapioGeral();
            } else {
                System.out.print("Não entendi sua resposta, por favor reescreva: ");
            }
        } while (simOuNao.equalsIgnoreCase("sim") && simOuNao.equalsIgnoreCase("não"));
    }
}
