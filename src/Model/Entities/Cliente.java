package Model.Entities;

import Model.Exceptions.DomainException;
import Model.Enums.TiposDePrato;
import com.sun.source.doctree.EntityTree;

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
    public Pedidos pedido() {

        Pedidos temp = new Pedidos();
        System.out.println("Por favor leia nosso cardápio através do método cardapio e portanto decida qual será seu pedido.");
        System.out.print("Já leu nosso cardapio? (Sim ou não): ");
        String simOuNao;
        do {
            simOuNao = sc.nextLine();
            if (simOuNao.equalsIgnoreCase("sim")) {
                System.out.print("Pois bem, qual tipo de prato você deseja consumir? (Principal? Sobremesa ou Entrada?) ");
                TiposDePrato escolhaPrato = TiposDePrato.valueOf(sc.nextLine().toUpperCase());
                realizarPedido(escolhaPrato);
            } else if (simOuNao.equalsIgnoreCase("não")) {
                Pedidos p = new Pedidos();
                p.cardapioGeral();
                System.out.println("Agora que já viu o cardápio, nos diga..");
                System.out.print("Pois bem, qual tipo de prato você deseja consumir? (Principal? Sobremesa ou Entrada?) ");
                TiposDePrato escolhaPrato = TiposDePrato.valueOf(sc.nextLine().toUpperCase());
                realizarPedido(escolhaPrato);
            } else {
                System.out.print("Não entendi sua resposta, por favor reescreva: ");
            }
        } while (simOuNao.equalsIgnoreCase("sim") && simOuNao.equalsIgnoreCase("não"));
        return temp;
    }


    private void realizarPedido(TiposDePrato tiposDePrato) {
        if (tiposDePrato.equals(TiposDePrato.PRINCIPAL)) {
            System.out.println("Do cardapio principal, qual nome do seu pedido?");
            System.out.print("NOME: ");
            String nome = sc.nextLine();
            // Adicionar método para ler o nome comparar pelo TXT inteiro e quando encontrar retornar tal pedido
        }
        if (tiposDePrato.equals(TiposDePrato.SOBREMESA)) {
            System.out.println("Do cardapio sobremesa, qual nome do seu pedido?");
            System.out.print("NOME: ");
            String nome = sc.nextLine();
            // Adicionar método para ler o nome comparar pelo TXT inteiro e quando encontrar retornar tal pedido
        }
        if (tiposDePrato.equals(TiposDePrato.ENTRADA)) {
            System.out.println("Do cardapio entrada, qual nome do seu pedido?");
            System.out.print("NOME: ");
            String nome = sc.nextLine();
            // Adicionar método para ler o nome comparar pelo TXT inteiro e quando encontrar retornar tal pedido
        }
    }
}
