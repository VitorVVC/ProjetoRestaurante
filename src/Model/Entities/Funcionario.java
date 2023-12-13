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
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Qual seu nome: ");
            nome = sc.nextLine();
            System.out.println(ConsoleColors.CYAN_BOLD + "Olá " + nome + ", seja muito bem vindo(a)!");
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Forneça seu email: ");
            loginEmail = sc.nextLine();
            while (!loginEmail.matches("(?=.*[aA])[a-zA-Z0-9]+@gmail\\.com")) {
                System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel identificar seu email, reescreva: ");
                loginEmail = sc.nextLine();
            }
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Forneça uma senha: ");
            loginSenha = sc.nextLine();
            System.out.print("Confirme a senha escrevendo-a novamente: ");
            loginSenhaConfirm = sc.nextLine();
            while (!loginSenha.equalsIgnoreCase(loginSenhaConfirm)) {
                System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "As senhas não estão iguais, reescreva novamente: ");
                loginSenhaConfirm = sc.nextLine();
            }
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Digite também o seu telefone EX: (85) 1234-5678: ");
            telefone = sc.nextLine();
            while (!validarTelefone(telefone)) {
                System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel identificar seu numero de celular, reescreva: ");
                telefone = sc.nextLine();
            }
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
        String cargoStr = sc.nextLine();
        Cargos cargo = Cargos.valueOf(cargoStr.toUpperCase());
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

        File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");

        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(nome + "|" + loginEmail + "|" + loginSenha + "|" + newId + "|" + telefone + "|" + nascimento.format(dateTimeFormatter) + "|" + cargo + "\n");
        } catch (IOException e) {
            throw new DomainException("Erro na escrita do programa.");
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado: " + e.getMessage());
        } finally {
            System.out.println(ConsoleColors.CYAN_BOLD + "Processo de cadastro finalizado!");
        }
        if (cargo.equals(Cargos.GARCOM)) {
            Funcionario funcAdicionado = new Garcom(nome, loginEmail, loginSenha, newId, telefone, nascimento, cargo);
            funcionarios.add(funcAdicionado);
            return funcAdicionado;
        } else if (cargo.equals(Cargos.COZINHEIRO)) {
            Funcionario funcAdicionado = new Cozinheiro(nome, loginEmail, loginSenha, newId, telefone, nascimento, cargo);
            funcionarios.add(funcAdicionado);
            return funcAdicionado;
        } else {
            Funcionario funcAdicionado = new Funcionario(nome, loginEmail, loginSenha, newId, telefone, nascimento, cargo);
            funcionarios.add(funcAdicionado);
            return funcAdicionado;
        }

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
                return lerInformacoesFuncionario(ID);
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
    private Funcionario lerInformacoesFuncionario(String ID) {
        File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split("\\|"); //Usando pipe como delimitador

                // Verifica se a linha contém as informações do funcionário com o e-mail fornecido
                if (partes.length == 7 && partes[3].equals(ID)) {
                    // Crie uma instância de Funcionario com base nas informações do arquivo
                    String nome = partes[0];
                    String email = partes[1];
                    String senha = partes[2];
                    int id = Integer.parseInt(partes[3]);
                    String telefone = partes[4];
                    LocalDate nascimento = LocalDate.parse(partes[5], dateTimeFormatter);
                    Cargos cargo = Cargos.valueOf(partes[6]);

                    // Retorne a instância criada
                    Funcionario temp = new Funcionario(nome, email, senha, id, telefone, nascimento, cargo);
                    if (temp.getCargo().equals(Cargos.GARCOM)) {
                        return new Garcom(nome, email, senha, id, telefone, nascimento, cargo);
                    } else if (temp.getCargo().equals(Cargos.COZINHEIRO)) {
                        return new Cozinheiro(nome, email, senha, id, telefone, nascimento, cargo);
                    } else {
                        return new Funcionario(nome, email, senha, id, telefone, nascimento, cargo);
                    }
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
        File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split("\\|"); // Usando pipe como delimitador

                // Verificar se a linha contém informações suficientes
                try {
                    String senhaArquivo = partes[2];
                    String idArquivo = partes[3];
                    if (senhaArquivo.equals(senha) && idArquivo.equals(ID)) {
                        // Usuário encontrado no arquivo
                        return true;
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
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split("\\|");

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
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split("\\|"); // Usando pipe como delimitador

                // Verificar se a linha contém informações suficientes
                String nomeArquivo = partes[0];
                String emailArquivo = partes[1];

                if (nome.equalsIgnoreCase(nomeArquivo) && email.equalsIgnoreCase(emailArquivo)) {
                    return partes[3];
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        }
        return null;
    }


    private boolean recuperarCredenciaisSenha(String nome, String email) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split("\\|");

                // Verificar se a linha contém informações suficientes
                String nomeArquivo = partes[0];
                String emailArquivo = partes[1];

                if (nomeArquivo.equalsIgnoreCase(nome) && emailArquivo.equalsIgnoreCase(email)) {
                    // Usuário encontrado no arquivo
                    return true;
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
        File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");
        try (Scanner sc = new Scanner(file);) {
            while (sc.hasNextLine()) {
                String linha = sc.nextLine();
                String[] partes = linha.split("\\|");

                // Verificar se a linha contém informações suficientes
                String nomeArquivo = partes[0];
                String emailArquivo = partes[1];

                // Comparar nome e email sem diferenciar maiúsculas de minúsculas
                if (nome.equalsIgnoreCase(nomeArquivo) && email.equalsIgnoreCase(emailArquivo)) {
                    return partes[2];
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado! --> " + e.getMessage());
        }
        return null;
    }


    // Método para "reescrever a senha"
    private static void reescreverSenha(String nome, String email, String ID) {
        File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/FuncionariosLogins.txt");

        try (Scanner scFile = new Scanner(file)) {
            List<String> linhas = new ArrayList<>();

            while (scFile.hasNextLine()) {
                String linha = scFile.nextLine();
                String[] partes = linha.split("\\|");

                String nomeArquivo = partes[0];
                String emailArquivo = partes[1];
                String idArquivo = partes[3];

                if (nome.equalsIgnoreCase(nomeArquivo) && email.equalsIgnoreCase(emailArquivo) && ID.equalsIgnoreCase(idArquivo)) {
                    System.out.print("Escreva sua nova senha: ");
                    String newSenha = sc.nextLine();

                    partes[2] = newSenha; // Atualiza a senha
                    linha = String.join("|", partes);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Nome: " + getNome() + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Email: " + getEmail() + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "ID: " + getId() + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Telefone: " + getTelefone() + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Nascimento: " + getNascimento().format(dateTimeFormatter) + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Cargo: " + getCargo() + "\n");

        return sb.toString();
    }

    public Cargos getCargo() {
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
    }
}
