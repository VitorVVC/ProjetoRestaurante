package Application;

import Model.Entities.*;
import Model.Exceptions.DomainException;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class Util {
    // Declarando métodos que usarei no projeto inteiro
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formatação de datas de --> 2000-01-01 para --> 01/01/2000
    public static Scanner sc = new Scanner(System.in); // Scanner para leitura de dados e instruções fornecidas pelo usuário
    public static final Pattern TELEFONE_PATTERN = Pattern.compile("\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}"); // Formatação para leitura de entradas simulando celulares

    public static void principal() {
        Pessoa temp = loginMethod();
        actionMethod(temp);
    }

    // Método para logar o usuário no sistema do restaurante
    // Caso não possua login ele será redirecionado para o método criar conta
    public static Pessoa loginMethod() {
        System.out.println(ConsoleColors.CYAN_BOLD + "Olá, seja bem-vindo ao *La praiana*!!");
        System.out.println(ConsoleColors.PURPLE + "Você já possui cadastro no nosso restaurante? ");
        System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
        String respond;
        do {
            try {
                respond = sc.nextLine();
                if (respond.equalsIgnoreCase("sim")) {
                    return loginExistente();
                } else if (respond.equalsIgnoreCase("não")) {
                    return adicionarAoSistema();
                } else {
                    System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Erro ao identificar sua escrita, tente novamente: ");
                }
            } catch (RuntimeException e) {
                throw new DomainException("Erro inesperado! --> " + e.getMessage());
            }
        } while (!respond.equalsIgnoreCase("sim") && !respond.equalsIgnoreCase("não"));
        return null;
    }

    // Método para realocar o usuário para o login
    public static Pessoa loginExistente() {
        System.out.println(ConsoleColors.CYAN_BOLD + "Que incrível! Seja bem-vindo de volta.");
        System.out.println(ConsoleColors.BLUE_BRIGHT + "Cliente" + ConsoleColors.CYAN_BOLD + " ou " + ConsoleColors.BLUE_BRIGHT + "Funcionário? ");
        System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
        String cOuF;
        do {
            cOuF = sc.nextLine();
            if (cOuF.equalsIgnoreCase("cliente")) {
                return new Cliente().metodoLoginCliente();
            } else if (cOuF.equalsIgnoreCase("funcionário")) {
                return new Funcionario().metodoLoginFuncionario();
            } else {
                System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Erro ao identificar sua escrita, tente novamente: ");
            }
        } while (!cOuF.equalsIgnoreCase("cliente") && !cOuF.equalsIgnoreCase("funcionário"));
        return null;
    }

    // Método para realocar o usuário para a criação de sua conta no sistema
    public static Pessoa adicionarAoSistema() {
        Pessoa p = null;
        System.out.println(ConsoleColors.CYAN_BOLD + "Tudo bem, vamos criar uma conta para você.");
        System.out.println(ConsoleColors.PURPLE + "Você é um " + ConsoleColors.BLUE_BRIGHT + "Cliente" + ConsoleColors.PURPLE + " ou " + ConsoleColors.BLUE_BRIGHT + "Funcionário? ");
        System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
        String resposta;
        do {
            resposta = sc.nextLine();
            try {
                if (resposta.equalsIgnoreCase("funcionário")) {
                    p = new Funcionario().metodoCriarContaFuncionario();
                } else if (resposta.equalsIgnoreCase("cliente")) {
                    p = new Cliente().metodoCriarContaCliente();
                } else {
                    System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Erro ao identificar sua escrita, tente novamente: ");
                }
            } catch (RuntimeException e) {
                throw new DomainException("Erro inesperado! --> " + e.getMessage());
            }
        } while (!resposta.equalsIgnoreCase("funcionário") && !resposta.equalsIgnoreCase("cliente"));
        System.out.println(ConsoleColors.RESET);
        return p;
    }

    // Método de ação. Identifica o objeto que o usuário é e realoca o mesmo para sua respectiva possivel ação
    public static void actionMethod(Pessoa pessoa) {
        if (pessoa instanceof Cliente) {
            actionClienteMethod((Cliente) pessoa);
        } else if (pessoa instanceof Funcionario) {
            if (pessoa instanceof Garcom) {
                actionGarconMethod((Garcom) pessoa);
            } else if (pessoa instanceof Cozinheiro) {
                actionCozinheiroMethod((Cozinheiro) pessoa);
            }
        }
    }

    // Método de ação para clientes
    public static void actionClienteMethod(Cliente pessoa) {
        boolean continuar = true;

        while (continuar) {
            System.out.println(ConsoleColors.CYAN_BOLD + "\nOlá " + pessoa.getNome());
            System.out.println(ConsoleColors.CYAN_BOLD + "Sua lista de possíveis ações: ");
            System.out.printf(ConsoleColors.BLUE_BRIGHT + "Opções:%n[1] --> Visualizar Cardápios%n[2] --> Realizar Pedido%n[3] --> Chamar garçom%n[4] --> Pagamento%n[0] --> Encerrar%n");
            System.out.println(ConsoleColors.CYAN_BOLD + "Qual desses você deseja realizar?");
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
            char resposta = sc.nextLine().charAt(0);

            switch (resposta) {
                case '1':
                    Pedidos.cardapioGeral();
                    break;
                case '2':
                    pessoa.pedido(pessoa);
                    break;
                case '3':
                    pessoa.chamarGarcom();
                    break;
                case '4':
                    pessoa.pagamento(pessoa);
                    break;
                case '0':
                    continuar = false;
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Opção inválida. Por favor, escolha uma opção válida.");
            }
        }
        System.out.println(ConsoleColors.CYAN_BOLD + "Encerrando a interação com o cliente.");
    }

    // Método de ação para garçons
    public static void actionGarconMethod(Garcom pessoa) {
        boolean continuar = true;

        while (continuar) {
            System.out.println(ConsoleColors.CYAN_BOLD + "\nOlá " + pessoa.getNome() + " seja bem vindo(a) ao simulador de funções!");
            System.out.println(ConsoleColors.CYAN_BOLD + "Sua lista de possíveis ações: ");
            System.out.printf(ConsoleColors.BLUE_BRIGHT + "Opções:%n[1] --> Chamado%n[2] --> Anotar Pedido%n[3] --> Mostrar Cardápio%n[0] --> Encerrar%n");
            System.out.println(ConsoleColors.CYAN_BOLD + "Qual desses você deseja realizar?");
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
            char resposta = sc.nextLine().charAt(0);

            switch (resposta) {
                case '1':
                    Random random = new Random();
                    int valorRandom = random.nextInt(2) + 1;
                    pessoa.chamado(valorRandom);
                    break;
                case '2':
                    pessoa.anotarPedidoPersonalizado();
                    break;
                case '3':
                    pessoa.mostrarCardapio();
                    break;
                case '0':
                    continuar = false;
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Opção inválida. Por favor, escolha uma opção válida.");
            }
        }
        System.out.println(ConsoleColors.CYAN_BOLD + "Encerrando a interação teste com garçom.");
    }

    // Método de ação para cozinheiros
    public static void actionCozinheiroMethod(Cozinheiro pessoa) {
        boolean continuar = true;

        while (continuar) {
            System.out.println(ConsoleColors.CYAN_BOLD + "\nOlá " + pessoa.getNome() + " seja bem vindo(a) ao simulador de funções!");
            System.out.println(ConsoleColors.CYAN_BOLD + "Sua lista de possíveis ações: ");
            System.out.printf(ConsoleColors.BLUE_BRIGHT + "Opções:%n[1] --> Receber Pedido%n[2] --> Chamar Garçom%n[0] --> Encerrar%n");
            System.out.println(ConsoleColors.CYAN_BOLD + "Qual desses você deseja realizar?");
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
            char resposta = sc.nextLine().charAt(0);

            switch (resposta) {
                case '1':
                    pessoa.receberPedido();
                    break;
                case '2':
                    pessoa.chamarGarcom();
                    break;
                case '0':
                    continuar = false;
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Opção inválida. Por favor, escolha uma opção válida.");
            }
        }
        System.out.println(ConsoleColors.CYAN_BOLD + "Encerrando a interação teste com cozinheiro.");
    }
}
