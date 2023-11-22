package Application;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Util {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("'Date: 'dd/MM/yyyy");
    public static Scanner sc = new Scanner(System.in);

    public static void adicionarAoSistema() {
        System.out.println("Você é um funcionário ou cliente? ");
        String resposta = sc.nextLine();

    }
}
