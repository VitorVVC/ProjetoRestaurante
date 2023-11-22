package Model.Entities;

import Model.TiposDePrato.TiposDePrato;

import java.util.ArrayList;
import java.util.List;

import static Application.Util.*;
import static Model.TiposDePrato.TiposDePrato.*;

public class Pedidos {
    private String nome;
    private Double preco;
    private String descricao;
    private TiposDePrato tiposDePrato;

    public Pedidos(String nome, Double preco, String descricao, TiposDePrato tiposDePrato) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.tiposDePrato = tiposDePrato;
    }

    public void cardapioGeral() {
        System.out.println("Qual cardápio você deseja acessar?");
        System.out.print("Entrada, Principal ou Sobremesas? ");
        TiposDePrato escolha = TiposDePrato.valueOf(sc.nextLine().toUpperCase());
        if (escolha.equals(ENTRADA)) {
            cardapioEntrada();
        }
        if (escolha.equals(TiposDePrato.PRINCIPAL)) {
            cardapioPrincipal();
        }
        if (escolha.equals(TiposDePrato.SOBREMESA)) {
            cardapioSobremesa();
        } else {
            while (!escolha.equals("ENTRADA") && !escolha.equals("PRINCIPAL") && !escolha.equals("SOBREMESA")) {
                System.out.print("Não identificamos sua resposta, por favor repita: ");
                escolha = TiposDePrato.valueOf(sc.nextLine().toUpperCase());
            }
        }
    }

    // Método cardapioPrincipal() para exibir no método cardapioGeral() o usuario poder visualiza-lo
    private List<Pedidos> cardapioPrincipal() {
        List<Pedidos> cardapioPrincipal = new ArrayList<>();
        cardapioPrincipal.add(new Pedidos("Filé Alfredo",249.00,"400g de medalhões de Filé Mignon envolvidos com bacon com molho funghi acompanhado de fettuccine com molho Alfredo preparado com creme de leite fresco.",PRINCIPAL));
        cardapioPrincipal.add(new Pedidos("Meio Filé Paulista (2 pessoas)",113.00,"250g de filé mignon cobertos com alhos fritos. Acompanha batatas recheadas e delicioso arroz \"biro-biro\" com batata palha, ovos, bacon, cebola picada, salsinha, cheiro verde e farofa.",PRINCIPAL));
        cardapioPrincipal.add(new Pedidos("Paella Caldosa Coco Bambu (2 pessoas)",201.00,"Arroz caldoso com camarão, lula, polvo, peixe e mexilhão, refogado com pimentões, ervilha, especiarias e um leve toque de açafrão. Servidos na paellera. Rico em sabor e apresentação.",PRINCIPAL));
        cardapioPrincipal.add(new Pedidos("Camarão Capri (2 pessoas)",118.00,"Camarões grelhados envolvidos em fettuccine, berinjela, abobrinha, tomate seco, creme de leite fresco e molho pesto de manjericão.",PRINCIPAL));
        cardapioPrincipal.add(new Pedidos("Camarão Ibiza (2 pessoas)",127.00,"Camarões empanados com gergelim, acompanhados de arroz com leite de coco, molho de tomates frescos, nata, mix de pimentões, cebola, coentro e cebolinha.",PRINCIPAL));

        return cardapioPrincipal;
    }

    // Método cardapioEntrada() para exibir no método CardapioGeral() o usuário poder visualiza-lo
    private List<Pedidos> cardapioEntrada() {
        List<Pedidos> cardapioEntrada = new ArrayList<>();
        cardapioEntrada.add(new Pedidos("Batata Frita Especial", 30.00, "Batatas fritas cobertas de queijo muçarela derretido e bacon crocante", ENTRADA));
        cardapioEntrada.add(new Pedidos("Camarão à Milanesa", 69.00, "Camarão à milanesa com gergelim. Acompanha molho tártaro.", ENTRADA));
        cardapioEntrada.add(new Pedidos("Camarão Recife", 50.00, "Camarões inteiros (com casca e cabeça) marinados na cerveja, levemente empanados,fritos e refogados com cebola e alho.", ENTRADA));
        cardapioEntrada.add(new Pedidos("Couvert Especial", 60.00, "Camarões marinados, tomate seco com queijo minas frescal temperado, tomatesgrelhados com alho frito, caponata de berinjela e ceviche de salmão. Acompanha torradastemperadas e pães de queijo.", ENTRADA));
        cardapioEntrada.add(new Pedidos("Croquete de Bacalhau (4 unidades)", 35.00, "Servido com molho tártaro.", ENTRADA));
        cardapioEntrada.add(new Pedidos("Filé com Fritas", 73.00, "300g de cubos de filé, refogado com cebola roxa e molho barbecue. Acompanha batata frita.", ENTRADA));

        return cardapioEntrada;
    }

    // Sobrescrita do método toString()
    @Override
    public String toString() {
        return String.format("Tipo do pedido:%s%nNome do prato: %s%nDescrição:%s%nValor: %.2f", tiposDePrato, nome, descricao, preco);
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
