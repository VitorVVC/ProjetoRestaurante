package Model.Entities;

import Model.Exceptions.DomainException;
import Model.Enums.TiposDePrato;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static Application.Util.*;
import static Model.Enums.TiposDePrato.*;

public class Pedidos implements Comparable<Pedidos> {

    // Métodos que todos os pratos em pedidos terão
    private String nome;
    private Double preco;
    private String descricao;
    private TiposDePrato tiposDePrato;

    public Pedidos() {
    }

    public Pedidos(String nome, Double preco, String descricao, TiposDePrato tiposDePrato) {
        if (tiposDePrato != ENTRADA && tiposDePrato != PRINCIPAL && tiposDePrato != SOBREMESA) {
            throw new DomainException("Erro na leitura do tipo do prato."); // TODO: 01/12/23 --> Tratar
        }
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.tiposDePrato = tiposDePrato;
    }

    public static void cardapioGeral() {
        System.out.print("Qual cardápio você deseja acessar: Entrada, Principal ou Sobremesa? ");
        try {
            String pratoStr = sc.nextLine();
            TiposDePrato escolha = TiposDePrato.valueOf(pratoStr.toUpperCase());
            switch (escolha) {
                case ENTRADA:
                    cardapioEntrada();
                    break;
                case PRINCIPAL:
                    cardapioPrincipal();
                    break;
                case SOBREMESA:
                    cardapioSobremesa();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }
        } catch (IllegalArgumentException e) {
            throw new DomainException("Não identificamos sua escrita: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado! --> " + e.getMessage());
        }


        System.out.println("Está satisfeito ou deseja revisitar algum de nossos menus?");
        System.out.print("'Sim desejo' OU 'Não, estou satisfeito' (Sim ou não): ");
        String resposta;
        do {
            resposta = sc.nextLine();
            if (resposta.equalsIgnoreCase("sim")) {
                cardapioGeral();
                return;
            } else if (resposta.equalsIgnoreCase("não")) {
                int hora = LocalDateTime.now().getHour();
                if (hora >= 6 && hora <= 12) {
                    System.out.println("Tudo bem, tenha um bom dia!");
                } else if (hora >= 12 && hora < 18) {
                    System.out.println("Tudo bem, tenha uma boa tarde!");
                } else {
                    System.out.println("Tudo bem, tenha uma boa noite!");
                }
                break;
            } else {
                System.out.print("Resposta inválida,tente novamente: ");
            }
        } while (!resposta.equalsIgnoreCase("sim") && !resposta.equalsIgnoreCase("não"));
    }


    // Método cardapioSobremesa() para exibir no método cardapioGeral() o usuario poder visualiza-lo
    private static void cardapioSobremesa() {
        List<Pedidos> pratosSobremesa = new ArrayList<>();

        try {
            // Altere o caminho do arquivo conforme necessário
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Sobremesas.txt");
            Scanner scFile = new Scanner(file);

            while (scFile.hasNext()) {
                try {
                    // Lê cada linha do arquivo
                    String[] partes = scFile.nextLine().split("\\|");

                    // Extrai informações da linha
                    String nome = partes[0];
                    double preco = Double.parseDouble(partes[1]);
                    String descricao = partes[2];
                    TiposDePrato prato = TiposDePrato.valueOf(partes[3].toUpperCase());

                    // Adiciona o prato à lista
                    pratosSobremesa.add(new Pedidos(nome, preco, descricao, prato));

                } catch (IllegalArgumentException e) {
                    throw new DomainException("Tipo de prato inválido " + e.getMessage());
                } catch (RuntimeException e) {
                    throw new DomainException("Erro inesperado." + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }

        // Exibe o cardápio de entradas
        System.out.println("Cardápio de Sobremesas: ");
        for (int i = 0; i < pratosSobremesa.size(); i++) {
            System.out.println("Prato número [" + (i + 1) + "]");
            System.out.println(pratosSobremesa.get(i));
        }

        // Ordena o cardápio, se o usuário desejar
        System.out.print("\nDeseja ordenar o cardápio do maior para o menor preço ou menor para maior? Caso não deseje, responda não. Caso contrário, responda sim: ");
        String respond = sc.nextLine();

        if (respond.equalsIgnoreCase("sim")) {
            System.out.print("Qual ordem deseja usar? Maior para menor (1) ou menor para maior (2): ");
            int r = sc.nextInt();
            sc.nextLine();

            // Ordena e exibe o cardápio conforme a escolha do usuário
            if (r == 1) {
                List<Pedidos> tempMaiorMenor = ordenarMaiorParaMenor(pratosSobremesa);
                exibirCardapioOrdenado(tempMaiorMenor, "maior para menor");
            } else if (r == 2) {
                List<Pedidos> tempMenorMaior = ordenarMenorParaMaior(pratosSobremesa);
                exibirCardapioOrdenado(tempMenorMaior, "menor para maior");
            } else {
                System.out.print("Opção inválida. Tente novamente.");
            }
        } else {
            System.out.println("Tudo bem. Tenha um bom dia.");
        }
    }


    // Método cardapioEntrada() para exibir no método CardapioGeral() o usuário poder visualiza-lo
    private static void cardapioEntrada() {
        List<Pedidos> pratosEntrada = new ArrayList<>();

        try {
            // Altere o caminho do arquivo conforme necessário
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Entrada.txt");
            Scanner scFile = new Scanner(file);

            while (scFile.hasNext()) {
                try {
                    // Lê cada linha do arquivo
                    String[] partes = scFile.nextLine().split("\\|");

                    // Extrai informações da linha
                    String nome = partes[0];
                    double preco = Double.parseDouble(partes[1]);
                    String descricao = partes[2];
                    TiposDePrato prato = TiposDePrato.valueOf(partes[3].toUpperCase());

                    // Adiciona o prato à lista
                    pratosEntrada.add(new Pedidos(nome, preco, descricao, prato));

                } catch (IllegalArgumentException e) {
                    throw new DomainException("Tipo de prato inválido " + e.getMessage());
                } catch (RuntimeException e) {
                    throw new DomainException("Erro inesperado." + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }

        // Exibe o cardápio de entradas
        System.out.println("Cardápio de Entradas: ");
        for (int i = 0; i < pratosEntrada.size(); i++) {
            System.out.println("Prato número [" + (i + 1) + "]");
            System.out.println(pratosEntrada.get(i));
        }

        // Ordena o cardápio, se o usuário desejar
        System.out.print("\nDeseja ordenar o cardápio do maior para o menor preço ou menor para maior? Caso não deseje, responda não. Caso contrário, responda sim: ");
        String respond = sc.nextLine();

        if (respond.equalsIgnoreCase("sim")) {
            System.out.print("Qual ordem deseja usar? Maior para menor (1) ou menor para maior (2): ");
            int r = sc.nextInt();
            sc.nextLine();

            // Ordena e exibe o cardápio conforme a escolha do usuário
            if (r == 1) {
                List<Pedidos> tempMaiorMenor = ordenarMaiorParaMenor(pratosEntrada);
                exibirCardapioOrdenado(tempMaiorMenor, "maior para menor");
            } else if (r == 2) {
                List<Pedidos> tempMenorMaior = ordenarMenorParaMaior(pratosEntrada);
                exibirCardapioOrdenado(tempMenorMaior, "menor para maior");
            } else {
                System.out.print("Opção inválida. Tente novamente.");
            }
        } else {
            System.out.println("Tudo bem. Tenha um bom dia.");
        }
    }

    // Método cardapioPrincipal() para exibir no método cardapioGeral() o usuario poder visualiza-lo
    private static void cardapioPrincipal() {
        List<Pedidos> cardapioPrincipal = new ArrayList<>();

        try {
            // Altere o caminho do arquivo conforme necessário
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Principal.txt");
            Scanner scFile = new Scanner(file);

            while (scFile.hasNext()) {
                try {
                    // Lê cada linha do arquivo
                    String[] partes = scFile.nextLine().split("\\|");

                    // Extrai informações da linha
                    String nome = partes[0];
                    double preco = Double.parseDouble(partes[1]);
                    String descricao = partes[2];
                    TiposDePrato prato = TiposDePrato.valueOf(partes[3].toUpperCase());

                    // Adiciona o prato à lista
                    cardapioPrincipal.add(new Pedidos(nome, preco, descricao, prato));

                } catch (IllegalArgumentException e) {
                    throw new DomainException("Tipo de prato inválido " + e.getMessage());
                } catch (RuntimeException e) {
                    throw new DomainException("Erro inesperado." + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }

        // Exibe o cardápio de entradas
        System.out.println("Cardápio principal: ");
        for (int i = 0; i < cardapioPrincipal.size(); i++) {
            System.out.println("Prato número [" + (i + 1) + "]");
            System.out.println(cardapioPrincipal.get(i));
        }

        // Ordena o cardápio, se o usuário desejar
        System.out.print("\nDeseja ordenar o cardápio do maior para o menor preço ou menor para maior? Caso não deseje, responda não. Caso contrário, responda sim: ");
        String respond = sc.nextLine();

        if (respond.equalsIgnoreCase("sim")) {
            System.out.print("Qual ordem deseja usar? Maior para menor (1) ou menor para maior (2): ");
            int r = sc.nextInt();
            sc.nextLine();

            // Ordena e exibe o cardápio conforme a escolha do usuário
            if (r == 1) {
                List<Pedidos> tempMaiorMenor = ordenarMaiorParaMenor(cardapioPrincipal);
                exibirCardapioOrdenado(tempMaiorMenor, "maior para menor");
            } else if (r == 2) {
                List<Pedidos> tempMenorMaior = ordenarMenorParaMaior(cardapioPrincipal);
                exibirCardapioOrdenado(tempMenorMaior, "menor para maior");
            } else {
                System.out.print("Opção inválida. Tente novamente.");
            }
        } else {
            System.out.println("Tudo bem. Tenha um bom dia.");
        }
    }

    private static void exibirCardapioOrdenado(List<Pedidos> cardapioOrdenado, String ordem) {
        System.out.println("Cardápio de Entradas (Ordenado " + ordem + " preço): ");
        for (int i = 0; i < cardapioOrdenado.size(); i++) {
            System.out.println("Prato número [" + (i + 1) + "]");
            System.out.println(cardapioOrdenado.get(i));
        }
    }

    public static List<Pedidos> ordenarMenorParaMaior(List<Pedidos> cardapioX) {
        List<Pedidos> listaOrdenada = new ArrayList<>(cardapioX);
        listaOrdenada.sort(Comparator.comparingDouble(Pedidos::getPreco));
        return listaOrdenada;
    }

    public static List<Pedidos> ordenarMaiorParaMenor(List<Pedidos> cardapioX) {
        List<Pedidos> listaOrdenada = new ArrayList<>(cardapioX);
        listaOrdenada.sort(Comparator.comparingDouble(Pedidos::getPreco).reversed()); // Adiciona reversed() para inverter a ordem
        return listaOrdenada;
    }


    // Sobrescrita do método toString()
    @Override
    public String toString() {
        return String.format("Tipo do pedido: %s%nNome do prato: %s%nDescrição: %s%nValor: %.2f%n", tiposDePrato, nome, descricao, preco);
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TiposDePrato getTiposDePrato() {
        return tiposDePrato;
    }

    public void setTiposDePrato(TiposDePrato tiposDePrato) {
        this.tiposDePrato = tiposDePrato;
    }

    @Override
    public int compareTo(Pedidos outroPedido) {
        return Double.compare(this.getPreco(), outroPedido.getPreco());
    }
}
