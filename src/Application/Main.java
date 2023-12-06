package Application;

import Model.Entities.Cliente;
import Model.Entities.ConsoleColors;
import Model.Entities.Pedidos;

import java.time.LocalDate;
import java.util.Locale;

import static Application.Util.*;
import static Model.Entities.Pedidos.*;

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
        //c.chamarGarcom();
        //c.metodoCriarContaCliente();
        // ============= \\
    }
}