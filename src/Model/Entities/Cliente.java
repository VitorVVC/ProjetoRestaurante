package Model.Entities;

import Model.Exceptions.DomainException;
import Model.Enums.TiposDePrato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;

import static Application.Util.*;
import static Model.Entities.Pedidos.cardapioGeral;

public class Cliente extends Pessoa implements InterfaceCliente {
    // Parametro extra que todo cliente tem sobre pessoa
    Double valorPagar;

    // Construtor padrão
    public Cliente(String nome, String email, String senha, Integer id, String telefone, LocalDate nascimento) {
        super(nome, email, senha, id, telefone, nascimento);
        valorPagar = 0.0;
    }

    // Construtor para testes
    public Cliente() {
    }

    // Método sobrescrito por interface
    @Override
    public Cliente metodoCriarContaCliente() {
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
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Confirme a senha escrevendo-a novamente: ");
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
                } catch (
                        DateTimeParseException e) { // Neste catch usamos syso pois é um loop até validar a data escrita
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Formato de data inválido!");
                    System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Digite novamente (dd/MM/yyyy): ");
                }
            }
        } catch (InputMismatchException e) {
            throw new DomainException("Valor escrito não foi lido corretamente" + e.getMessage());
        }
        try {
            // Geração do ID
            Random geraID = new Random();
            int newId = 1000000 + geraID.nextInt(9000000);
            System.out.println(ConsoleColors.CYAN_BOLD + "Seu ID é: " + newId);

            // Construção da linha para escrever no arquivo
            String linha = nome + "|" + loginEmail + "|" + loginSenha + "|" + newId + "|" + telefone + "|" + nascimento.format(dateTimeFormatter) + "\n";

            // Escrita no arquivo
            try {
                File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");
                FileWriter fileWriter = new FileWriter(file, true);
                fileWriter.write(linha);
                fileWriter.close();
            } catch (IOException e) {
                throw new DomainException("Erro na escrita do progama.");
            } catch (RuntimeException e) {
                throw new DomainException("Erro inesperado: " + e.getMessage());
            }

            return new Cliente(nome, loginEmail, loginSenha, newId, telefone, nascimento);
        } catch (InputMismatchException e) {
            throw new DomainException("Valor escrito não foi lido corretamente" + e.getMessage());
        } finally {
            System.out.println(ConsoleColors.CYAN_BOLD + "Processo de cadastro finalizado!");
        }
    }

    // Método auxiliar para criação da conta
    private boolean validarTelefone(String telefone) {
        Matcher matcher = TELEFONE_PATTERN.matcher(telefone);
        return matcher.matches();
    }

    // Método para logar o cliente
    @Override
    public Pessoa metodoLoginCliente() {
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
                return lerInformacoesCliente(ID);
            } else {
                System.out.println(ConsoleColors.PURPLE + "Esqueceu seu ID ou Senha? (Sim ou não) ");
                System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
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
    private Cliente lerInformacoesCliente(String ID) {
        File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] partes = linha.split("\\|"); // Usando o pipe como delimitador

                if (partes.length == 6 && partes[3].equals(ID)) {
                    String nome = partes[0];
                    String email = partes[1];
                    String senha = partes[2];
                    int id = Integer.parseInt(partes[3]);
                    String telefone = partes[4];
                    LocalDate nascimento = LocalDate.parse(partes[5],dateTimeFormatter);

                    return new Cliente(nome, email, senha, id, telefone, nascimento);
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Erro na leitura de arquivos");
        }

        return null; // Retorna null se não encontrar o cliente com o ID fornecido
    }


    // Método para conferir as credenciais para assim realizar o login
    private boolean verificarCredenciaisFuncionarios(String senha, String ID) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNext()) {
                    String linha = sc.nextLine();
                    String[] partes = linha.split("\\|"); // Usando o pipe como delimitador

                    try {
                        String senhaArquivo = partes[2];
                        String IDArquivo = partes[3];

                        if (senhaArquivo.equals(senha) && IDArquivo.equals(ID)) {
                            // Usuário encontrado no arquivo
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new DomainException("Não foi possível identificar ID, SENHA ou outros campos necessários.");
                    } catch (RuntimeException e) {
                        throw new DomainException("Erro inesperado! --> " + e.getMessage());
                    }
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
    private static boolean recuperarCredenciaisID(String nome, String email) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String linha = sc.nextLine();
                    String[] partes = linha.split("\\|"); // Usando o pipe como delimitador

                    // Verificar se a linha contém informações suficientes
                    if (partes.length >= 6) {
                        String nomeArquivo = partes[0];
                        String emailArquivo = partes[1];

                        if (nomeArquivo.equalsIgnoreCase(nome) && emailArquivo.equalsIgnoreCase(email)) {
                            // Usuário encontrado no arquivo
                            return true;
                        }
                    }
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


    // Método auxiliar de recuperarID(). Que no método abaixo retorna o ID do usuário em String para podermos exibi-lo
    private String retornaID(String nome, String email) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String linha = sc.nextLine();
                    String[] partes = linha.split("\\|"); // Usando o pipe como delimitador

                    // Verificar se a linha contém informações suficientes
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];

                    if (nome.equalsIgnoreCase(nomeArquivo) && email.equalsIgnoreCase(emailArquivo)) {
                        return partes[3];
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado! --> " + e.getMessage());
        }
        return null;
    }

    // Método para recuperar a sua senha. Confere se o usuario existe
    private boolean recuperarCredenciaisSenha(String nome, String email) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String linha = sc.nextLine();
                    String[] partes = linha.split("\\|"); // Usando o pipe como delimitador

                    // Verificar se a linha contém informações suficientes
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
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado! --> " + e.getMessage());
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
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String linha = sc.nextLine();
                    String[] partes = linha.split("\\|"); // Usando o pipe como delimitador

                    // Verificar se a linha contém informações suficientes
                    String nomeArquivo = partes[0];
                    String emailArquivo = partes[1];

                    // Comparar nome e email sem diferenciar maiúsculas de minúsculas
                    if (nome.equalsIgnoreCase(nomeArquivo) && email.equalsIgnoreCase(emailArquivo)) {
                        return partes[2];
                    }
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
    private void reescreverSenha(String nome, String email, String ID) {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/ClientesLogin.txt");

            try (Scanner scFile = new Scanner(file)) {
                List<String> linhas = new ArrayList<>();

                while (scFile.hasNextLine()) {
                    String linha = scFile.nextLine();
                    String[] partes = linha.split("\\|"); // Usando o pipe como delimitador

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
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Não foi possível encontrar o arquivo fornecido: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado! --> " + e.getMessage());
        }
    }

    @Override
    public void pedido(Cliente c) {
        System.out.println(ConsoleColors.CYAN_BOLD + "Por favor leia nosso cardápio através do método cardapio e portanto decida qual será seu pedido.");
        String simOuNao;
        String resp;
        do {
            try {
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Já leu nosso cardapio? (Sim ou não): ");
                System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
                simOuNao = sc.nextLine();
                if (simOuNao.equalsIgnoreCase("sim")) {
                    System.out.print(ConsoleColors.BLUE_BOLD_BRIGHT + "Pois bem, qual tipo de prato você deseja consumir? ( Principal, Sobremesa ou Entrada? ) ");
                    String escolhaStr = sc.nextLine();
                    TiposDePrato escolhaPrato = TiposDePrato.valueOf(escolhaStr.toUpperCase());
                    realizarPedido(escolhaPrato);
                } else if (simOuNao.equalsIgnoreCase("não")) {
                    cardapioGeral();
                    System.out.print(ConsoleColors.BLUE_BOLD_BRIGHT + "Pois bem, qual tipo de prato você deseja consumir? ( Principal, Sobremesa ou Entrada? ) ");
                    String escolhaStr = sc.nextLine();
                    TiposDePrato escolhaPrato = TiposDePrato.valueOf(escolhaStr.toUpperCase());
                    realizarPedido(escolhaPrato);
                } else {
                    System.out.print("Não entendi sua resposta, por favor reescreva: ");
                }
            } catch (IllegalArgumentException e) {
                throw new DomainException("Não foi possivel identificar o tipo de prato fornecido!" + e.getMessage());
            }
        } while (!simOuNao.equalsIgnoreCase("sim") && !simOuNao.equalsIgnoreCase("não"));
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Deseja realizar outro pedido? ");
        do {
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
            resp = sc.nextLine();
            if (resp.equalsIgnoreCase("sim")) {
                pedido(c);
            } else if (resp.equalsIgnoreCase("não")) {
                System.out.println(ConsoleColors.CYAN_BOLD + "Caso deseje realizar outro pedido estaremos a disposição");
            } else {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel identificar sua resposta, por favor tente novamente");
            }
        } while (!resp.equalsIgnoreCase("sim") && !resp.equalsIgnoreCase("não"));
    }

    // Uma das 3 ações possiveis para o usuario
    public void chamarGarcom() {
        char resposta;
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Qual o motivo de sua chamada?");
        System.out.println(ConsoleColors.CYAN_BOLD + "1 --> Quero ajuda com cardápio");
        System.out.println(ConsoleColors.CYAN_BOLD + "2 --> Pedido personalizado");
        System.out.println(ConsoleColors.CYAN_BOLD + "3 --> Comemorações especiais");
        System.out.println(ConsoleColors.CYAN_BOLD + "4 --> Pagamento da conta");
        System.out.println(ConsoleColors.CYAN_BOLD + "5 --> Outros");
        do {
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
            resposta = sc.nextLine().charAt(0);
            switch (resposta) {
                case '1', '2', '3', '5':
                    System.out.println(ConsoleColors.CYAN_BOLD + "Garçom está a caminho");
                    break;
                case '4':

                    break;
                default:
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Opção inválida. Tente novamente.");
            }
        } while (resposta != '1' && resposta != '2' && resposta != '3' && resposta != '4' && resposta != '5');
    }

    // Uma das 3 ações possiveis para o usuario
    public void realizarPedido(TiposDePrato tiposDePrato) {
        String cardapioPrincipal = "/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Principal.txt";
        String cardapioSobremesa = "/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Sobremesas.txt";
        String cardapioEntrada = "/Users/vitorvargas/Desktop/Faculdade/Semestre DOIS/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Entrada.txt";
        String nome;

        if (tiposDePrato.equals(TiposDePrato.PRINCIPAL)) {
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Do cardápio principal, qual nome do seu pedido?");
            do {
                System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "NOME: ");
                nome = sc.nextLine();
                if (compararCardapio(cardapioPrincipal, nome) && buscarPreco(cardapioPrincipal, nome) != null) {
                    valorPagar += buscarPreco(cardapioPrincipal, nome);
                    Random tempoPreparacao = new Random();
                    int random = tempoPreparacao.nextInt(5, 20);
                    System.out.printf(ConsoleColors.BLUE_BOLD_BRIGHT + "Seu pedido está sendo preparado e em cerca de %d minutos você o receberá!%n", random);
                    System.out.printf(ConsoleColors.BLUE_BOLD_BRIGHT + "Até então sua conta totaliza: %.2f%n", valorPagar); // TODO: 04/12/23 --> TESTAR !
                }
            } while (!compararCardapio(cardapioPrincipal, nome));
        }

        if (tiposDePrato.equals(TiposDePrato.SOBREMESA)) {
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Do cardápio de sobremesas, qual nome do seu pedido?");
            do {
                System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "NOME: ");
                nome = sc.nextLine();
                if (compararCardapio(cardapioSobremesa, nome) && buscarPreco(cardapioSobremesa, nome) != null) {
                    valorPagar += buscarPreco(cardapioSobremesa, nome);
                    Random tempoPreparacao = new Random();
                    int random = tempoPreparacao.nextInt(5, 20);
                    System.out.printf(ConsoleColors.BLUE_BOLD_BRIGHT + "Seu pedido está sendo preparado e em cerca de %d minutos você o receberá!%n", random);
                    System.out.printf(ConsoleColors.BLUE_BOLD_BRIGHT + "Até então sua conta totaliza: %.2f%n", valorPagar);
                }
            } while (!compararCardapio(cardapioSobremesa, nome));
        }

        if (tiposDePrato.equals(TiposDePrato.ENTRADA)) {
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Do cardápio de entrada, qual nome do seu pedido?");
            do {
                System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "NOME: ");
                nome = sc.nextLine();
                if (compararCardapio(cardapioEntrada, nome) && buscarPreco(cardapioEntrada, nome) != null) {
                    valorPagar += buscarPreco(cardapioEntrada, nome);
                    Random tempoPreparacao = new Random();
                    int random = tempoPreparacao.nextInt(5, 20);
                    System.out.printf(ConsoleColors.BLUE_BOLD_BRIGHT + "Seu pedido está sendo preparado e em cerca de %d minutos você o receberá!%n", random);
                    System.out.printf(ConsoleColors.BLUE_BOLD_BRIGHT + "Até então sua conta totaliza: %.2f%n", valorPagar);
                }
            } while (!compararCardapio(cardapioEntrada, nome));
        }
    }

    // Método para receber um arquivo / nome e procurar se ele realmente existe
    private static boolean compararCardapio(String arquivo, String nome) {
        try (Scanner scFile = new Scanner(new File(arquivo))) {
            while (scFile.hasNextLine()) {
                String[] partes = scFile.nextLine().split("\\|");
                String nomeArquivo = partes[0];
                if (nomeArquivo.equalsIgnoreCase(nome)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Erro na leitura de arquivo: " + e.getMessage());
        } catch (Exception e) {
            throw new DomainException("Erro inesperado: " + e.getMessage());
        }
        return false;
    }

    // Método para receber um arquivo / nome e retornar seu preço
    private Double buscarPreco(String arquivo, String nome) {
        Double precoPrato = null;

        try (Scanner scFile = new Scanner(new File(arquivo))) {
            while (scFile.hasNextLine()) {
                String[] partes = scFile.nextLine().split("\\|");
                String nomeTemp = partes[0];
                if (nomeTemp.equalsIgnoreCase(nome)) {
                    if (scFile.hasNext()) {
                        String precoString = partes[1];
                        precoPrato = Double.parseDouble(precoString);
                        break; // Interromper o loop após encontrar o nome
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new DomainException("Erro na leitura de arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new DomainException("Formato de preço inválido no arquivo: " + e.getMessage());
        } catch (Exception e) {
            throw new DomainException("Erro inesperado: " + e.getMessage());
        }
        return precoPrato;
    }

    // Método pagamento para o cliente poder pagar sua conta total
    public void pagamento(Cliente p) {
        char formaPagamento;
        System.out.println("Valor final: " + p.getValorPagar());
        System.out.println("Qual a forma de pagamento?");
        System.out.printf("Opções:%n(1) --> Cartão%n(2) --> Pix%n(3) --> Dinheiro%n");
        do {
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
            formaPagamento = sc.nextLine().charAt(0);
            if (formaPagamento == '1') {
                System.out.println(ConsoleColors.CYAN_BOLD + "Garçom está a caminho com a máquina");
            } else if (formaPagamento == '2') {
                System.out.println(ConsoleColors.CYAN_BOLD + "Garçom está a caminho com o QR Code de nosso restaurante");
            } else if (formaPagamento == '3') {
                System.out.println(ConsoleColors.CYAN_BOLD + "Garçom está a caminho para o acerto de contas");
            } else {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Não pude identificar sua resposta, tente novamente:");
            }
        } while (formaPagamento != '1' && formaPagamento != '2' && formaPagamento != '3');
        p.setValorPagar(0.0);
    }

    // toString() de cliente. Que não inclue todos os seus dados. Excluindo: Senha & totalPagar
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Nome: " + getNome() + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Email: " + getEmail() + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "ID: " + getId() + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Telefone: " + getTelefone() + "\n");
        sb.append(ConsoleColors.PURPLE_BRIGHT + "Nascimento: " + getNascimento().format(dateTimeFormatter) + "\n");

        return sb.toString();
    }

    public Double getValorPagar() {
        return valorPagar;
    }

    public void setValorPagar(Double valorPagar) {
        this.valorPagar = valorPagar;
    }
}
