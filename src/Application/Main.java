package Application;

import Model.Entities.Cliente;
import Model.Entities.Pedidos;

import java.util.Locale;

import static Application.Util.*;

public class Main {
    public static void main(String[] args){
        Locale.setDefault(Locale.US);

        // Apresentação !!!
        // ============= \\
            principal();
        // ============= \\

        // TESTES ABAIXO !!!
        // ============= \\
        //adicionarAoSistema();
        Pedidos p = new Pedidos();
        //p.cardapioGeral();
        Cliente c = new Cliente();
        //c.pedido();
        //c.metodoCriarContaCliente();
        // ============= \\
    }
}