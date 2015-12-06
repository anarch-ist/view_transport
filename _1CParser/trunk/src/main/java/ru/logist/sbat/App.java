package ru.logist.sbat;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Controller controller = new Controller();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String nextLine = scanner.nextLine();
            if (nextLine.equals("exit")) {
                controller.close();
                System.exit(0);
            } else if (nextLine.equals("start")) {
                controller.startGeneration();
            }
        }
    }
}
