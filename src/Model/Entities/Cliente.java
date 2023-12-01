package Model.Entities;

import Model.Exceptions.DomainException;
import Model.Enums.TiposDePrato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;

import static Application.Util.*;

public class Cliente extends Pessoa implements InterfaceCliente {

    public Cliente(String nome, String email, String senha, Integer id, String telefone, LocalDate nascimento) {
        super(nome, email, senha, id, telefone, nascimento);
    }

    public Cliente() {
    }

    // TODO: 30/11/23 -->  Continuar o tratamento de excecoes no método criar conta && fazer o método de login
    @Override
    public Cliente metodoCriarContaCliente() {
        String nome;
        String loginEmail;
        String loginSenha;
        String loginSenhaConfirm;
        String telefone;
        LocalDate nascimento;
        try {
            System.out.print("Qual seu nome: ");
            nome = sc.nextLine();
            System.out.println("Olá " + nome + ", seja muito bem vindo(a)!");
            System.out.print("Forneça seu email: ");
            loginEmail = sc.nextLine(); // TODO: 30/11/23
            while (!loginEmail.matches("(?=.*[aA])[a-zA-Z0-9]+@gmail\\.com")) {
                System.out.print("Não foi possivel identificar seu email, reescreva: ");
                loginEmail = sc.nextLine();
            }
            System.out.print("Forneça uma senha: ");
            loginSenha = sc.nextLine();
            System.out.print("Confirme a senha escrevendo-a novamente: ");
            loginSenhaConfirm = sc.nextLine();
            while (!loginSenha.equalsIgnoreCase(loginSenhaConfirm)) {
                System.out.print("As senhas não estão iguais, reescreva novamente: ");
                loginSenhaConfirm = sc.nextLine();
            }
            System.out.print("Digite também o seu telefone EX: (85) 1234-5678: ");
            telefone = sc.nextLine();
            while (!validarTelefone(telefone)) {
                System.out.print("Não foi possivel identificar seu numero de celular, reescreva: ");
                telefone = sc.nextLine();
            }
            System.out.print("Agora forneca-me a sua data de nascimento (dd/MM/yyyy): ");
            boolean dataValida = false;
            nascimento = null;

            while (!dataValida) {
                try {
                    String dataInput = sc.nextLine();
                    nascimento = LocalDate.parse(dataInput, dateTimeFormatter);
                    dataValida = true;
                } catch (DateTimeParseException e) {
                    System.out.print("Formato de data inválido. Digite novamente (dd/MM/yyyy): ");
                }
            }
        } catch (InputMismatchException e) {
            throw new DomainException("Valor escrito não foi lido corretamente" + e.getMessage());
        } finally {
            System.out.println("Processo de cadastro finalizado!");
        }

        Random geraID = new Random();
        int newId = 1000000 + geraID.nextInt(9000000);
        System.out.println("Seu ID é: " + newId);

        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(nome + " " + loginEmail + " " + loginSenha + " " + newId + "\n");
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Erro na escrita do progama.");
        } catch (RuntimeException e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

        return new Cliente(nome, loginEmail, loginSenha, newId, telefone, nascimento);
    }

    private boolean validarTelefone(String telefone) {
        Matcher matcher = TELEFONE_PATTERN.matcher(telefone);
        return matcher.matches();
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
            try {
                simOuNao = sc.nextLine();
                if (simOuNao.equalsIgnoreCase("sim")) {
                    System.out.print("Pois bem, qual tipo de prato você deseja consumir? ( Principal, Sobremesa ou Entrada? ) ");
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
            } catch (IllegalArgumentException e) {
                throw new DomainException("Não foi possivel identificar o tipo de prato fornecido!");
            }
        } while (!simOuNao.equalsIgnoreCase("sim") && !simOuNao.equalsIgnoreCase("não"));
        return temp;
    }


    public void realizarPedido(TiposDePrato tiposDePrato) {
        if (tiposDePrato.equals(TiposDePrato.PRINCIPAL)) {
            System.out.println("Do cardapio principal, qual nome do seu pedido?");
            System.out.print("NOME: ");
            String nome = sc.nextLine();
            // Adicionar método para ler o nome comparar pelo TXT inteiro e quando encontrar retornar tal pedido
            try {
                if (compararCardapio("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Principal.txt", nome)) {
                    Random tempoPreparacao = new Random();
                    int random = tempoPreparacao.nextInt(5, 20);
                    System.out.printf("Seu pedido está sendo preparado e em cerca de %d minutos!%n", random);
                }
            } catch (IllegalArgumentException e) {
                throw new DomainException("Não foi possivel identificar o tipo de prato fornecido na realização de seu pedido!");
            }
        }
        if (tiposDePrato.equals(TiposDePrato.SOBREMESA)) {
            System.out.println("Do cardapio sobremesa, qual nome do seu pedido?");
            System.out.print("NOME: ");
            String nome = sc.nextLine();
            // Adicionar método para ler o nome comparar pelo TXT inteiro e quando encontrar retornar tal pedido
            if (compararCardapio("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Sobremesa.txt", nome)) {
                System.out.println("a");
            } else {
                System.out.println("b");
            }
        }
        if (tiposDePrato.equals(TiposDePrato.ENTRADA)) {
            System.out.println("Do cardapio entrada, qual nome do seu pedido?");
            System.out.print("NOME: ");
            String nome = sc.nextLine();
            // Adicionar método para ler o nome comparar pelo TXT inteiro e quando encontrar retornar tal pedido
            if (compararCardapio("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Entrada.txt", nome)) {
                System.out.println("a");
            } else {
                System.out.println("b");
            }
        }
    }

    private static boolean compararCardapio(String arquivo, String nome) {
        try {
            File file = new File(arquivo);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");
                String[] partesNome = nome.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 4) {
                    if (partesNome.length >= 3) {
                        String nomeArquivo1 = partes[0];
                        String nomeArquivo2 = partes[1];
                        String nomeArquivo3 = partes[2];

                        String nomeP1 = partesNome[0];
                        String nomeP2 = partesNome[1];
                        String nomeP3 = partesNome[2];

                        // Comparar, ignorando maiúsculas e minúsculas
                        if (nomeArquivo1.equalsIgnoreCase(nomeP1) && nomeArquivo2.equalsIgnoreCase(nomeP2) && nomeArquivo3.equalsIgnoreCase(nomeP3)) {
                            // Usuário encontrado no arquivo
                            return true;
                        }
                    } else if (partesNome.length == 2) {
                        String nomeArquivo1 = partes[0];
                        String nomeArquivo2 = partes[1];

                        String nomeP1 = partesNome[0];
                        String nomeP2 = partesNome[1];

                        // Comparar, ignorando maiúsculas e minúsculas
                        if (nomeArquivo1.equalsIgnoreCase(nomeP1) && nomeArquivo2.equalsIgnoreCase(nomeP2)) {
                            // Usuário encontrado no arquivo
                            return true;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        }
        // Usuário não encontrado no arquivo
        return false;
    }
}
