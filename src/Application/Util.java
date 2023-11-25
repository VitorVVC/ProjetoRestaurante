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


    // Considerar adicionar um while para quando o usuario lembra sim sua senha e ID porém so digitou errado
    // Considerar adicionar uma pessoa Gerente para "gerenciar" o negócio
    public static void loginMethod() {
        System.out.println("Olá, seja bem vindo ao *La praiana*!!");
        System.out.println("Você já possui cadastro no nosso restaurante?");
        String respond = sc.nextLine();
        try {
            if (respond.equalsIgnoreCase("sim")) {
                System.out.println("Que incrivel! Seja bem vindo de volta.");
                System.out.println("Por favor diga-me qual seu ID e senha ");
                System.out.print("ID: ");
                String ID = sc.nextLine();
                System.out.print("Senha: ");
                String senha = sc.nextLine();
                if (verificarCredenciais(ID, senha)) {
                    System.out.println("Processo realizado com sucesso! Seja bem vindo novamente");
                } else {
                    System.out.print("Esqueceu seu ID ou Senha? (Sim ou não) ");
                    String temp = sc.nextLine();
                    if (temp.equalsIgnoreCase("sim")) {
                        System.out.print("Deseja resgatar seu ID ou Senha? ");
                        String idOuSenha = sc.nextLine();
                        if (idOuSenha.equalsIgnoreCase("id")) {
                            recuperarID();
                        }
                        if (idOuSenha.equalsIgnoreCase("senha")) {
                            //recuperarSenha();
                        }
                    } else {
                        System.out.println("Tudo bem, tente novamente.");
                    }
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
        System.out.print("Você é um funcionário ou cliente? ");
        String resposta;
        do {
            resposta = sc.nextLine();
            try {
                if (resposta.equalsIgnoreCase("funcionário")) {
                    Funcionario tempFunc = new Funcionario();
                    tempFunc.metodoCriarContaFuncionario();
                } else if (resposta.equalsIgnoreCase("cliente")) {
                    Cliente tempCliente = new Cliente();
                    tempCliente.metodoCriarContaCliente();
                } else {
                    System.out.print("Não entendi sua resposta, por favor reescreva: ");
                }
            } catch (DomainException e) {
                System.out.println("Erro no progama: " + e.getMessage());
            }
        } while (!resposta.equalsIgnoreCase("funcionário") && !resposta.equalsIgnoreCase("cliente"));
    }

    // Método para LOGAR na conta
    public static boolean verificarCredenciais(String senha, String ID) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 3) {
                    String senhaArquivo = partes[2];
                    String IDArquivo = partes[3];

                    if (senhaArquivo.equalsIgnoreCase(senha) && IDArquivo.equalsIgnoreCase(ID)) {
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

    // Método "recuperar credenciais ID" para auxiliar o método recuperar ID
    public static boolean recuperarCredenciaisID(String nome, String email) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 3) {
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];

                    if (nomeArquivo.equalsIgnoreCase(nome) && emailArquivo.equalsIgnoreCase(emailArquivo)) {
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

    public static String retornaID(String nome, String email) {
        String idArquivo = null;
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 3) {
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];
                    String senhaArquivo = partes[2];

                    if (nome.equalsIgnoreCase(nomeArquivo) && email.equalsIgnoreCase(emailArquivo)) {
                        idArquivo = partes[3];
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        }
        return idArquivo;
    }

    public static void recuperarID() {
        System.out.println("Lamentamos que tenha esquecido seu ID. Para recuperar por favor forneça os dados a seguir: ");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        if (recuperarCredenciaisID(nome, email)) {
            System.out.println("ID Recuperada com sucesso !");
            System.out.println("Sua ID: " + retornaID(nome, email));
        } else {
            System.out.println("Não foi possivel recuperar seu ID");
        }


    }

}
