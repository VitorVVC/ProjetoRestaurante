package Application;

import Model.Entities.Cliente;
import Model.Entities.Funcionario;
import Model.Exceptions.DomainException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Util {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static Scanner sc = new Scanner(System.in);

    public static void loginMethod() {
        System.out.println("Olá, seja bem vindo ao *Restaurante ficticio*!!");
        System.out.println("Você já possui cadastro no nosso restaurante?");
        String respond = sc.nextLine();
        try {
            if (respond.equalsIgnoreCase("sim")) {
                System.out.println("Que incrivel! Seja bem vindo de volta.");
                System.out.println("Por favor diga-me qual seu nome, email e senha ");
                System.out.print("Nome: ");
                String nome = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Senha: ");
                String senha = sc.nextLine();
                if (verificarCredenciais(nome, email, senha)) {
                    System.out.println("Seja bem vindo novamente " + nome);
                } else {
                    System.out.println("Falha ao encontrar as credenciais");
                }
            }
            if (respond.equalsIgnoreCase("não")) {
                System.out.println("Tudo bem. Vamos realizar o seu cadastro.");
                adicionarAoSistema();
            } else {
                while (!respond.equalsIgnoreCase("sim") && !respond.equalsIgnoreCase("não")) {
                    System.out.print("Não foi possivel entender a sua resposta por favor digita novamente ( sim ou não ): ");
                    respond = sc.nextLine();
                }
            }
        } catch (InputMismatchException e) {
            throw new DomainException("Não foi possivel identificar sua escrita por favor recarregue o progama.");
        }
    }

    public static void adicionarAoSistema() {
        System.out.println("Você é um funcionário ou cliente? ");
        String resposta = sc.nextLine();
        try {
            if (resposta.equalsIgnoreCase("funcionário")) {
                Funcionario tempFunc = new Funcionario();
                tempFunc.metodoCriarContaFuncionario();
            } else {
                Cliente tempCliente = new Cliente();
                tempCliente.metodoCriarContaCliente();
            }
        } catch (DomainException e) {
            System.out.println("Erro no progama: " + e.getMessage());
        }
    }

    public static boolean verificarCredenciais(String nome, String email, String senha) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/IdLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 3) {
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];
                    String senhaArquivo = partes[2];

                    if (nomeArquivo.equalsIgnoreCase(nome) && emailArquivo.equalsIgnoreCase(email) && senhaArquivo.equalsIgnoreCase(senha)) {
                        // Usuário encontrado no arquivo
                        return true;
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
