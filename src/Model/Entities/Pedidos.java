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

    public void cardapioGeral() {
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

        String resposta;
        System.out.println("\nEstá satisfeito ou deseja revisitar algum de nossos menus?");
        System.out.print("'Sim desejo' OU 'Não, estou satisfeito' (Sim ou não): ");
        sc.nextLine();
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
                System.out.println("Resposta inválida. Responda 'Sim' ou 'Não'.");
            }
        } while (true);
    }


    // Método cardapioSobremesa() para exibir no método cardapioGeral() o usuario poder visualiza-lo
    private void cardapioSobremesa() {
        List<Pedidos> pratosSobremesa = new ArrayList<>();
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Sobremesas.txt");
            // File file = new File("TxTFiles/Principal.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                try {
                    String nome = sc.nextLine();
                    String precoString = sc.nextLine();
                    String descricao = sc.nextLine();
                    TiposDePrato prato = TiposDePrato.valueOf(sc.nextLine().toUpperCase());

                    Double preco = Double.parseDouble(precoString);
                    pratosSobremesa.add(new Pedidos(nome, preco, descricao, prato));
                } catch (DomainException e) {
                    System.out.println("Tipo de prato inválido" + e.getMessage());
                } catch (RuntimeException e) {
                    System.out.println("Erro inesperado.");
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
        System.out.println("Cardapio De Sobremesas: ");
        for (int i = 0; i < pratosSobremesa.size(); i++) {
            System.out.println("Prato numero [" + (i + 1) + "]");
            System.out.println(pratosSobremesa.get(i));
        }
        System.out.println("================================");
        System.out.print("Você deseja ordenar o cardápio do maior para o menor preço ou menor para maior? Caso não deseje responda não, caso queira então sim: ");
        String respond = sc.next();
        int r = 0;
        do {
            if (respond.equalsIgnoreCase("sim")) {
                System.out.print("Qual ordem deseja usar? Maior para menor (1) ou menor para maior (2): ");
                r = sc.nextInt();
                sc.nextLine();
                if (r == 1) {
                    ordenarMaiorParaMenor(pratosSobremesa);
                    System.out.println("Cardapio De Sobremesas ( Maior para menor preço ): ");
                    for (int i = 0; i < pratosSobremesa.size(); i++) {
                        System.out.println("Prato numero [" + (i + 1) + "]");
                        System.out.println(pratosSobremesa.get(i));
                    }
                } else if (r == 2) {
                    ordenarMenorParaMaior(pratosSobremesa);
                    System.out.println("Cardapio De Sobremesas ( Menor para maior preço ): ");
                    for (int i = 0; i < pratosSobremesa.size(); i++) {
                        System.out.println("Prato numero [" + (i + 1) + "]");
                        System.out.println(pratosSobremesa.get(i));
                    }
                } else {
                    System.out.print("Tente novamente ");
                }
            } else {
                System.out.println("Tudo bem.Tenha uma bom dia.");
                break;
            }
        } while (r != 1 && r != 2);
    }

    // Método cardapioPrincipal() para exibir no método cardapioGeral() o usuario poder visualiza-lo
    private void cardapioPrincipal() {
        List<Pedidos> pratosPrincipais = new ArrayList<>();
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Principal.txt");
            //File file = new File("TxTFiles/Principal.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                try {
                    String nome = sc.nextLine();
                    String precoString = sc.nextLine();
                    String descricao = sc.nextLine();
                    TiposDePrato prato = TiposDePrato.valueOf(sc.nextLine().toUpperCase());

                    Double preco = Double.parseDouble(precoString);
                    pratosPrincipais.add(new Pedidos(nome, preco, descricao, prato));
                } catch (IllegalArgumentException e) {
                    throw new DomainException("Tipo de prato inválido " + e.getMessage());
                } catch (RuntimeException e) {
                    throw new DomainException("Erro inesperado." + e.getMessage());
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
        System.out.println("Cardapio Principal: ");
        for (int i = 0; i < pratosPrincipais.size(); i++) {
            System.out.println("Prato numero [" + (i + 1) + "]");
            System.out.println(pratosPrincipais.get(i));
        }
        System.out.println("================================");
        System.out.print("Você deseja ordenar o cardápio do maior para o menor preço ou menor para maior? Caso não deseje responda não, caso queira então sim: ");
        String respond = sc.next();
        int r = 0;
        do {
            if (respond.equalsIgnoreCase("sim")) {
                System.out.print("Qual ordem deseja usar? Maior para menor (1) ou menor para maior (2): ");
                r = sc.nextInt();
                sc.nextLine();
                if (r == 1) {
                    ordenarMaiorParaMenor(pratosPrincipais);
                    System.out.println("Cardapio De Pratos Principais ( Maior para menor preço ): ");
                    for (int i = 0; i < pratosPrincipais.size(); i++) {
                        System.out.println("Prato numero [" + (i + 1) + "]");
                        System.out.println(pratosPrincipais.get(i));
                    }
                } else if (r == 2) {
                    ordenarMenorParaMaior(pratosPrincipais);
                    System.out.println("Cardapio De Pratos Principais ( Menor para maior preço ): ");
                    for (int i = 0; i < pratosPrincipais.size(); i++) {
                        System.out.println("Prato numero [" + (i + 1) + "]");
                        System.out.println(pratosPrincipais.get(i));
                    }
                } else {
                    System.out.print("Tente novamente ");
                }
            } else {
                System.out.println("Tudo bem.Tenha uma bom dia.");
                break;
            }
        } while (r != 1 && r != 2);
    }

    // Método cardapioEntrada() para exibir no método CardapioGeral() o usuário poder visualiza-lo
    private void cardapioEntrada() {
        List<Pedidos> pratosEntrada = new ArrayList<>();
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Entrada.txt");
            //File file = new File("TxTFiles/Principal.txt");
            Scanner scFile = new Scanner(file);
            while (scFile.hasNext()) {
                try {
                    String nome = scFile.nextLine();
                    String precoString = scFile.nextLine();
                    String descricao = scFile.nextLine();
                    TiposDePrato prato = TiposDePrato.valueOf(scFile.nextLine().toUpperCase());


                    Double preco = Double.parseDouble(precoString);
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
        System.out.println("Cardapio De Entradas: ");
        for (int i = 0; i < pratosEntrada.size(); i++) {
            System.out.println("Prato numero [" + (i + 1) + "]");
            System.out.println(pratosEntrada.get(i));
        }
        System.out.print("\nVocê deseja ordenar o cardápio do maior para o menor preço ou menor para maior? Caso não deseje responda não, caso queira então sim: ");
        String respond = sc.next();
        int r = 0;
        do {
            if (respond.equalsIgnoreCase("sim")) {
                System.out.print("Qual ordem deseja usar? Maior para menor (1) ou menor para maior (2): ");
                r = sc.nextInt();
                sc.nextLine();
                if (r == 1) {
                    List<Pedidos> tempMaiorMenor = ordenarMaiorParaMenor(pratosEntrada);
                    System.out.println("Cardapio De Pratos Principais ( Maior para menor preço ): ");
                    for (int i = 0; i < tempMaiorMenor.size(); i++) {
                        System.out.println("Prato numero [" + (i + 1) + "]");
                        System.out.println(tempMaiorMenor.get(i));
                    }
                } else if (r == 2) {
                    List<Pedidos> tempMenorMaior = ordenarMenorParaMaior(pratosEntrada);
                    System.out.println("Cardapio De Pratos Principais ( Menor para maior preço ): ");
                    for (int i = 0; i < tempMenorMaior.size(); i++) {
                        System.out.println("Prato numero [" + (i + 1) + "]");
                        System.out.println(tempMenorMaior.get(i));
                    }
                } else {
                    System.out.print("Tente novamente ");
                }
            } else {
                System.out.println("Tudo bem.Tenha uma bom dia.");
                break;
            }
        } while (r != 1 && r != 2);
    }

    public List<Pedidos> ordenarMenorParaMaior(List<Pedidos> cardapioX) {
        List<Pedidos> listaOrdenada = new ArrayList<>(cardapioX);
        listaOrdenada.sort(Comparator.comparingDouble(Pedidos::getPreco));
        return listaOrdenada;
    }

    public List<Pedidos> ordenarMaiorParaMenor(List<Pedidos> cardapioX) {
        List<Pedidos> listaOrdenada = new ArrayList<>(cardapioX);
        listaOrdenada.sort(Comparator.comparingDouble(Pedidos::getPreco));
        return listaOrdenada;
    }


    // Sobrescrita do método toString()
    @Override
    public String toString() {
        return String.format("Tipo do pedido: %s%nNome do prato: %s%nDescrição: %s%nValor: %.2f", tiposDePrato, nome, descricao, preco);
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
