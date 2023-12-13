package Model.Entities;

import Model.Enums.Cargos;
import Model.Exceptions.DomainException;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Random;

import static Application.Util.sc;
import static Model.Entities.Pedidos.sortearPrato;

public class Cozinheiro extends Funcionario {

    // Construtor padrão com paramêtros de Funcionario
    public Cozinheiro(String nome, String loginEmail, String loginSenha, int newId, String telefone, LocalDate nascimento, Cargos cargo) {
        super(nome, loginEmail, loginSenha, newId, telefone, nascimento, cargo);
    }
    // Método de receberPedido para que você aleatoriamente receba um prato e mesa
    public void receberPedido() {
        Random random = new Random();
        int mesaRandom = random.nextInt(50) + 1;
        String pratoSorteado = ConsoleColors.CYAN_BOLD + sortearPrato();
        System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + "O garçom informa que o prato: { " + pratoSorteado + " } foi solicitado para a mesa: { " + mesaRandom + " }");
    }
    // Método para chamar o garçom, fornecendo detalhes do pedido que você preparou
    public void chamarGarcom() {
        System.out.println(ConsoleColors.CYAN_BOLD + "Abaixo forneça detalhes para enviar ao garçom: ");
        System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Mesa: ");
        try {
            int mesa = sc.nextInt();
            sc.nextLine();
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Nome do prato: ");
            String nomePrato = sc.nextLine();
            System.out.println(ConsoleColors.CYAN_BOLD + "Algum detalhe extra? (Sim ou não)");
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");
            String decisao;
            String detalhes = "Nenhum detalhe foi informado";
            do {
                decisao = sc.nextLine();
                if (decisao.equalsIgnoreCase("sim")) {
                    System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Detalhes: ");
                    detalhes = sc.nextLine();
                } else if (decisao.equalsIgnoreCase("não")) {
                    System.out.println(ConsoleColors.CYAN_BOLD + "Ok, seu pedido está sendo avaliado");
                } else {
                    System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel identificar sua resposta, tente novamente: ");
                }
            } while (!decisao.equalsIgnoreCase("sim") && !decisao.equalsIgnoreCase("não"));

            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Você confirma esta estrutura de pedido?");
            System.out.println(ConsoleColors.CYAN_BOLD + "Nome: " + nomePrato);
            System.out.println(ConsoleColors.CYAN_BOLD + "Mesa: " + mesa);

            if (decisao.equalsIgnoreCase("sim")) {
                System.out.println(ConsoleColors.CYAN_BOLD + "Detalhes: " + detalhes);
            } else {
                System.out.println(ConsoleColors.CYAN_BOLD + "Sem detalhes");
            }

            System.out.println(ConsoleColors.CYAN_BOLD + "Você confirma?: (Sim ou não)");
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT + "Resposta: ");

            String confirm;
            do {
                confirm = sc.nextLine();
                if (confirm.equalsIgnoreCase("sim")) {
                    System.out.println(ConsoleColors.CYAN_BOLD + "Sua chamada foi enviada para a equipe de garçons, muito obrigado!");
                } else if (confirm.equalsIgnoreCase("não")) {
                    System.out.println(ConsoleColors.CYAN_BOLD + "Portanto, reescreva novamente:");
                    chamarGarcom();
                } else {
                    System.out.print(ConsoleColors.RED_BOLD_BRIGHT + "Não foi possivel identificar sua escrita, tente novamente: ");
                }
            } while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não"));
        } catch (InputMismatchException e) {
            throw new DomainException("Não foi possivel identificar sua escrita " + e.getMessage());
        } catch (RuntimeException e) {
            throw new DomainException("Erro inesperado! " + e.getMessage());
        }
    }
}
