package Model.Entities;

import Model.Enums.Cargos;
import Model.Exceptions.DomainException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;

import static Application.Util.*;

public class Funcionario extends Pessoa implements InterfaceFuncionario {
    List<Funcionario> funcionarios = new ArrayList<>();

    Cargos cargo;

    public Funcionario(String nome, String email, String senha, Integer id, String telefone, LocalDate nascimento, Cargos cargo) {
        super(nome, email, senha, id, telefone, nascimento);
        if (!cargo.equals(Cargos.GERENTE) && !cargo.equals(Cargos.COZINHEIRO) && !cargo.equals(Cargos.GARCOM)) {
            throw new DomainException("Cargo inexistente");
        }
        this.cargo = cargo;
    }

    public Funcionario() {
    }

    @Override
    public Pessoa metodoCriarContaFuncionario() {
        String nome;
        String loginEmail;
        String loginSenha;
        String loginSenhaConfirm;
        String telefone;
        LocalDate nascimento;
        try {
            // Pede o nome do usuário
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Qual seu nome: ");
            nome = sc.nextLine();
            System.out.println(ConsoleColors.CYAN_BOLD + "Olá " + nome + ", seja muito bem vindo(a)!");
            // Pede o email do usuário
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Forneça seu email: ");
            loginEmail = sc.nextLine();
            // Enquanto o email não atender as exigências -->
            // Possuir ao menos um digito em formato string
            // e colocar @gmail.com
            // Ele ficará em um loop, forçando o usuário a ter de atender tais exigências
            while (!loginEmail.matches("(?=.*[aA])[a-zA-Z0-9]+@gmail\\.com")) {
                System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel identificar seu email, reescreva: ");
                loginEmail = sc.nextLine();
            }
            // Pede a senha duas vezes e as compara, enquanto não forem iguais repete o processo
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Forneça uma senha: ");
            loginSenha = sc.nextLine();
            System.out.print("Confirme a senha escrevendo-a novamente: ");
            loginSenhaConfirm = sc.nextLine();
            while (!loginSenha.equalsIgnoreCase(loginSenhaConfirm)) {
                System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "As senhas não estão iguais, reescreva novamente: ");
                loginSenhaConfirm = sc.nextLine();
            }
            // Pede o telefone e o valida com a função validarTelefone();
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Digite também o seu telefone EX: (85) 1234-5678: ");
            telefone = sc.nextLine();
            while (!validarTelefone(telefone)) {
                System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel identificar seu numero de celular, reescreva: ");
                telefone = sc.nextLine();
            }
            // Pede a data de nascimento do cliente e fica em um loop caso ela não atenda o formato esperado ( dd/ mm / yyyy )
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Agora forneca-me a sua data de nascimento (dd/MM/yyyy): ");
            boolean dataValida = false;
            nascimento = null;

            while (!dataValida) {
                try {
                    String dataInput = sc.nextLine();
                    nascimento = LocalDate.parse(dataInput, dateTimeFormatter);
                    dataValida = true;
                } catch (DateTimeParseException e) {
                    System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Formato de data inválido. Digite novamente (dd/MM/yyyy): ");
                }
            }
        } catch (InputMismatchException e) {
            throw new DomainException("Valor escrito não foi lido corretamente" + e.getMessage());
        }

        System.out.println(ConsoleColors.PURPLE + "Qual sua função no restaurante? ");
        System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
        String pratoStr = sc.nextLine();
        Cargos cargo = Cargos.valueOf(pratoStr.toUpperCase());
        String senha;
        switch (cargo) {
            case GARCOM, COZINHEIRO -> System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Seja bem vindo");
            case GERENTE -> {
                do {
                    System.out.println(ConsoleColors.PURPLE + "Qual a senha?");
                    System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
                    senha = sc.nextLine();
                    if (!senha.equalsIgnoreCase("so eu sei")) {
                        System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Senha errada.");
                    } else {
                        System.out.println(ConsoleColors.CYAN_BOLD + "Seja bem vindo gerente");
                    }
                } while (!senha.equalsIgnoreCase("so eu sei"));
            }
        }


        Random geraID = new Random();
        int newId = 1000000 + geraID.nextInt(9000000);
        System.out.println("Seu ID é: " + newId);

        File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");

        String cargoComParenteses = "(" + cargo + ")";
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(nome + " " + loginEmail + " " + loginSenha + " " + newId + " " + telefone + " " + nascimento + " " + cargoComParenteses + "\n");
        } catch (IOException e) {
            throw new DomainException("Erro na escrita do programa.");
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado: " + e.getMessage());
        } finally {
            System.out.println(ConsoleColors.CYAN_BOLD + "Processo de cadastro finalizado!");
        }
        Funcionario funcAdicionado = new Funcionario(nome, loginEmail, loginSenha, newId, telefone, nascimento, cargo);
        funcionarios.add(funcAdicionado);
        return funcAdicionado;
    }

    // Método para dar suporte a escrita de um novo funcionário
    private boolean validarTelefone(String telefone) {
        Matcher matcher = TELEFONE_PATTERN.matcher(telefone);
        return matcher.matches();
    }

    @Override
    public Pessoa metodoLoginFuncionario() {
        String ID;
        String senha;

        System.out.println(ConsoleColors.CYAN_BOLD + "Por favor, diga-me qual seu ID e senha ");
        do {
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "ID: ");
            ID = sc.nextLine();
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Senha: ");
            senha = sc.nextLine();

            if (verificarCredenciaisFuncionarios(senha, ID)) {
                System.out.println(ConsoleColors.CYAN_BOLD + "Processo realizado com sucesso! Seja bem-vindo novamente ");
                Funcionario retorno = lerInformacoesFuncionario(ID);
                return retorno;
            } else {
                System.out.print(ConsoleColors.PURPLE + "Esqueceu seu ID ou Senha? (Sim ou não) ");
                String temp = sc.nextLine();

                if (temp.equalsIgnoreCase("sim")) {
                    System.out.print(ConsoleColors.PURPLE + "Deseja resgatar seu ID ou Senha? ");
                    String idOuSenha = sc.nextLine();

                    if (idOuSenha.equalsIgnoreCase("id")) {
                        recuperarID();
                    } else if (idOuSenha.equalsIgnoreCase("senha")) {
                        System.out.print(ConsoleColors.PURPLE + "Você deseja recuperar sua senha (1) ou reescrevê-la? (2): ");
                        char resp = sc.nextLine().charAt(0);

                        if (resp == '1') {
                            recuperarSenha();
                        } else if (resp == '2') {
                            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Forneça-me seu nome: ");
                            String nome = sc.nextLine();
                            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Forneça-me seu email atrelado à nossa instituição: ");
                            String email = sc.nextLine();
                            reescreverSenha(nome, email, ID);
                        } else {
                            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possível identificar sua resposta, digite novamente: ");
                        }
                    }
                } else if (temp.equalsIgnoreCase("não")) {
                    System.out.println(ConsoleColors.CYAN_BOLD + "Reescreva seus dados");
                } else {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possível identificar sua resposta, digite novamente: ");
                }
            }
        } while (!verificarCredenciaisFuncionarios(senha, ID));

        return null;
    }

    // Método para retornar o funcionario
    private Funcionario lerInformacoesFuncionario(String email) {
        File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split(" ");

                // Verifica se a linha contém as informações do funcionário com o e-mail fornecido
                if (partes.length >= 3 && partes[1].equals(email)) {
                    // Crie uma instância de Funcionario com base nas informações do arquivo
                    String nome = partes[0];
                    String senha = partes[2];
                    Integer id = Integer.valueOf(partes[3]);
                    String telefone = partes[4];
                    LocalDate nascimento = LocalDate.parse(partes[5]);
                    Cargos cargo = Cargos.valueOf(partes[6]);

                    // Retorne a instância criada
                    return new Funcionario(nome, email, senha, id, telefone, nascimento, cargo);
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Erro na leitura de arquivos");
        }

        // Retorna null se não encontrar o funcionário com o e-mail fornecido
        return null;
    }


    // Método para conferir as credenciais para assim realizar o login
    private boolean verificarCredenciaisFuncionarios(String senha, String ID) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                try {
                    if (partes.length >= 7) {
                        String senhaArquivo = partes[2];
                        String IDArquivo = partes[3];

                        if (senhaArquivo.equalsIgnoreCase(senha) && IDArquivo.equalsIgnoreCase(ID)) {
                            // Usuário encontrado no arquivo
                            return true;
                        }
                    }
                } catch (NoSuchElementException e) {
                    throw new DomainException("Não foi possivel identificar ID ou SENHA.");
                } catch (RuntimeException e) {
                    throw new DomainException("Erro inesperado! --> " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado! --> " + e.getMessage());
        }
        // Usuário não encontrado no arquivo
        return false;
    }

    // Método para recuperar a ID do usuário
    // Caso o tonto do usuário esqueça o email ai ele que entre no email pra procurar
    private void recuperarID() {
        System.out.println(ConsoleColors.CYAN_BOLD + "Para recuperar por favor forneça os dados a seguir: ");
        String nome;
        String email;
        do {
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Nome: ");
            nome = sc.nextLine();
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Email: ");
            email = sc.nextLine();
            if (recuperarCredenciaisID(nome, email)) {
                System.out.println(ConsoleColors.CYAN_BOLD + "ID Recuperada com sucesso !");
                System.out.println(ConsoleColors.CYAN_BOLD + "Sua ID: " + retornaID(nome, email));
            } else {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel recuperar seu ID, tente novamente.");
            }
            // Loop para dar tempo de tentar varias combinações e caso esquecer realmente o email atrelado
            // Poder ir ao celular e conferir sem o progama ter de resetar
        } while (!recuperarCredenciaisID(nome, email));
    }

    // Método "recuperar credenciais ID" para auxiliar o método recuperar ID
    private boolean recuperarCredenciaisID(String nome, String email) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 7) {
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];

                    if (nomeArquivo.equalsIgnoreCase(nome) && emailArquivo.equalsIgnoreCase(email)) {
                        // Usuário encontrado no arquivo
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        }
        // Usuário não encontrado no arquivo
        return false;
    }

    // Método auxiliar de recuperarID(). Que no método abaixo retorna o ID do usuário em String para podermos exibi-lo
    private String retornaID(String nome, String email) {
        String idArquivo = null;
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 7) {
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];

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


    private boolean recuperarCredenciaisSenha(String nome, String email) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 7) {
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];

                    if (nomeArquivo.equalsIgnoreCase(nome) && emailArquivo.equalsIgnoreCase(email)) {
                        // Usuário encontrado no arquivo
                        return true;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        }
        // Usuário não encontrado no arquivo
        return false;
    }

    // Método para recuperar a senha do usuário
    private void recuperarSenha() {
        System.out.println(ConsoleColors.CYAN_BOLD + "Para recuperar por favor forneça os dados a seguir: ");
        String nome;
        String email;
        do {
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Nome: ");
            nome = sc.nextLine();
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Email: ");
            email = sc.nextLine();
            if (recuperarCredenciaisSenha(nome, email)) {
                System.out.println(ConsoleColors.CYAN_BOLD + "Senha Recuperada com sucesso !");
                System.out.println(ConsoleColors.CYAN_BOLD + "Sua Senha: " + retornaSenha(nome, email));
            } else {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel recuperar seu ID, tente novamente.");
            }
            // Loop para dar tempo de tentar varias combinações e caso esquecer realmente o email atrelado
            // Poder ir ao celular e conferir sem o progama ter de resetar
        } while (!recuperarCredenciaisID(nome, email));
    }


    // Método para auxiliar o retorno de senha do usuario. Retornando a senha já "resgatada"
    private String retornaSenha(String nome, String email) {
        String senhaArquivo = null;
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split(" ");

                // Verificar se a linha contém informações suficientes
                if (partes.length >= 7) {
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];

                    // Comparar nome e email sem diferenciar maiúsculas de minúsculas
                    if (nome.equalsIgnoreCase(nomeArquivo) && email.equalsIgnoreCase(emailArquivo)) {
                        senhaArquivo = partes[2];
                        break;  // Sair do loop quando encontrar a correspondência
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        }
        return senhaArquivo;
    }


    // Método para "reescrever a senha"
    private static void reescreverSenha(String nome, String email, String ID) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner scFile = new Scanner(file);

            List<String> linhas = new ArrayList<>();

            while (scFile.hasNextLine()) {
                String linha = scFile.nextLine();
                String[] partes = linha.split(" ");

                if (partes.length >= 7) {
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];
                    String idArquivo = partes[3];

                    if (nome.equalsIgnoreCase(nomeArquivo) && email.equalsIgnoreCase(emailArquivo) && ID.equalsIgnoreCase(idArquivo)) {
                        System.out.print("Escreva sua nova senha: ");
                        String newSenha = sc.nextLine();

                        StringBuilder novaLinha = new StringBuilder();
                        for (int i = 0; i < partes.length; i++) {
                            if (i == 2) {
                                novaLinha.append(newSenha);
                            } else {
                                novaLinha.append(partes[i]);
                            }

                            if (i < partes.length - 1) {
                                novaLinha.append(" ");
                            }
                        }

                        linha = novaLinha.toString();
                    }
                }
                linhas.add(linha);
            }

            try (FileWriter fileWriter = new FileWriter(file)) {
                for (String linha : linhas) {
                    fileWriter.write(linha + "\n");
                }
            } catch (IOException e) {
                throw new DomainException("Erro na escrita do programa.");
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        }
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
    }
}
