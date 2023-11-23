package Model.Entities;

import Model.Exceptions.DomainException;
import Model.Enums.TiposDePrato;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Application.Util.*;
import static Model.Enums.TiposDePrato.*;

public class Pedidos {
    // Lista de pedidos / lista de pratos

    List<Pedidos> pedidos = new ArrayList<>();
    List<Pedidos> pratosEntrada = new ArrayList<>();
    List<Pedidos> pratosPrincipais = new ArrayList<>();
    List<Pedidos> pratosSobremesa = new ArrayList<>();


    // Métodos que todo prato pedido terá
    private String nome;
    private Double preco;
    private String descricao;
    private TiposDePrato tiposDePrato;

    public Pedidos() {
    }

    public Pedidos(String nome, Double preco, String descricao, TiposDePrato tiposDePrato) {
        if (tiposDePrato != ENTRADA && tiposDePrato != PRINCIPAL && tiposDePrato != SOBREMESA) {
            throw new DomainException("Erro na leitura do tipo do prato.");
        }
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.tiposDePrato = tiposDePrato;
    }

    public void cardapioGeral() {
        System.out.print("Qual cardápio você deseja acessar. Entrada, Principal ou Sobremesa? ");
        try {
            TiposDePrato escolha = TiposDePrato.valueOf(sc.nextLine().toUpperCase());
            if (escolha.equals(TiposDePrato.ENTRADA)) {
                cardapioEntrada();
            }
            if (escolha.equals(TiposDePrato.PRINCIPAL)) {
                cardapioPrincipal();
            }
            if (escolha.equals(TiposDePrato.SOBREMESA)) {
                cardapioSobremesa();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Não identificamos sua escrita: " + e.getMessage());
        }

    }

    // Método cardapioSobremesa() para exibir no método cardapioGeral() o usuario poder visualiza-lo
    private void cardapioSobremesa() {
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
                    pratosPrincipais.add(new Pedidos(nome, preco, descricao, prato));
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
        for (int i = 0; i < pratosPrincipais.size(); i++) {
            System.out.println("Prato numero [" + (i + 1) + "]");
            System.out.println(pratosPrincipais.get(i));
        }
        System.out.println("================================");
        System.out.print("Você deseja ordenar o cardápio do maior para o menor preço ou se encontra satisfeito com o cardápio atual? (Sim ou não)");
        String respond = sc.next();
        if (respond.equalsIgnoreCase("sim")) {

            System.out.println("Cardapio De Sobremesas ( Maior para menor preço ): ");
            Double precoMin = Double.MAX_VALUE;
            for (int i = 0; i < pratosPrincipais.size(); i++) {
                Pedidos p = pratosPrincipais.get(i);
                if (p.getPreco() < precoMin) {
                    precoMin = p.getPreco();
                    System.out.println("Prato numero [" + (i + 1) + "]");
                    System.out.println(p);
                }
            }

        } else {
            System.out.println("Tudo bem.Tenha uma bom dia.");
        }
    }

    // Método cardapioPrincipal() para exibir no método cardapioGeral() o usuario poder visualiza-lo
    private void cardapioPrincipal() {
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
        System.out.println("Cardapio Principal: ");
        for (int i = 0; i < pratosPrincipais.size(); i++) {
            System.out.println("Prato numero [" + (i + 1) + "]");
            System.out.println(pratosPrincipais.get(i));
        }
    }

    // Método cardapioEntrada() para exibir no método CardapioGeral() o usuário poder visualiza-lo
    private void cardapioEntrada() {
        try {
            File file = new File("/Users/vitorvargas/Desktop/Faculdade/Progamação Orientada || Java/SistemaCardapio/src/TxTFiles/Entrada.txt");
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
        System.out.println("Cardapio De Entradas: ");
        for (int i = 0; i < pratosPrincipais.size(); i++) {
            System.out.println("Prato numero [" + (i + 1) + "]");
            System.out.println(pratosPrincipais.get(i));
        }
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
}
