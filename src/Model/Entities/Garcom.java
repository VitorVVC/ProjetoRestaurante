package Model.Entities;

import Model.Enums.Cargos;
import Model.Enums.TiposDePrato;

import java.time.LocalDate;
import java.util.Random;

import static Application.Util.*;
import static Model.Entities.Pedidos.*;

public class Garcom extends Funcionario {

    public Garcom(String nome, String loginEmail, String loginSenha, int newId, String telefone, LocalDate nascimento, Cargos cargo) {
        super(nome,loginEmail,loginSenha,newId,telefone,nascimento,cargo);
    }

    public void chamado(Integer validar) {
        if (validar == 1) {
            Random random = new Random();
            int mesaRandom = random.nextInt(50) + 1;
            int randomSituacaoSorteado = random.nextInt(5) + 1;
            String situacao = null;
            switch (randomSituacaoSorteado) {
                case 1:
                    situacao = ConsoleColors.CYAN_BOLD + "Ajuda com cardápio";
                    break;
                case 2:
                    situacao = ConsoleColors.CYAN_BOLD + "Pedido personalizado";
                    break;
                case 3:
                    situacao = ConsoleColors.CYAN_BOLD + "Comemorações especiais";
                    break;
                case 4:
                    situacao = ConsoleColors.CYAN_BOLD + "Pagamento da conta";
                    break;
                case 5:
                    situacao = ConsoleColors.CYAN_BOLD + "Outros";
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel identificar o numero");
                    break;
            }
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + "Mesa: { " + mesaRandom + " } está lhe chamando para: [ " + situacao + " ]");
        } else if (validar == 2) {
            Random random = new Random();
            int mesaRandom = random.nextInt(50) + 1;
            String pratoSorteado = ConsoleColors.CYAN_BOLD + sortearPrato();
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + "A cozinha informa que o prato: { " + pratoSorteado + " } da mesa { " + mesaRandom + " } está pronto!");
        } else {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Erro na leitura da chamada fornecida!");
        }
    }

    public void anotarPedidoPersonalizado() {
        System.out.println(ConsoleColors.CYAN_BOLD + "Anote o pedido no bloco abaixo: ");
        System.out.println(ConsoleColors.CYAN_BOLD + "Quantas pessoas? ");
        System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
        int pessoas = sc.nextInt();
        System.out.println(ConsoleColors.CYAN_BOLD + "Anotações: ");
        for (int i = 0; i < pessoas; i++) {
            System.out.printf("Pedido numero: {[%d}]%n", (i + 1));
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Nome do cliente: ");
            String nomeCliente = sc.nextLine();
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Nome do pedido: ");
            String nomePedido = sc.nextLine();
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Bebida(s): ");
            String bebida = sc.nextLine();
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Especificações: ");
            String especificacoes = sc.nextLine();
            System.out.printf(ConsoleColors.CYAN_BOLD + "A cozinha recebeu o pedido de {%s}%n", nomeCliente);
        }
    }

    public void mostrarCardapio() {
        System.out.println(ConsoleColors.CYAN_BOLD + "Qual cardápio o cliente deseja acessar? ");
        System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
        String cardapio = sc.nextLine();
        TiposDePrato prato = TiposDePrato.valueOf(cardapio.toUpperCase());
        switch (prato) {
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
                break;
        }
    }

}
