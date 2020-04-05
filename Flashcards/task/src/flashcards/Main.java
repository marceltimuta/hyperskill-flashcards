package flashcards;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Log log = new Log(scanner);
        Dictionary dict = new Dictionary(log);
        boolean continueLoop = true;

        while (continueLoop) {
            log.outMessage("\nInput the action (add, remove, import, export, ask, exit," +
                    " log, hardest card, reset stats):");
            String action = log.inMessage().toLowerCase();

            switch (action) {
                case "add":
                    dict.addCard();
                    break;
                case "remove":
                    dict.removeCard();
                    break;
                case "import":
                    dict.importCards();
                    break;
                case "ask":
                    dict.ask();
                    break;
                case "export":
                    dict.exportCards();
                    break;
                case "exit":
                    log.outMessage("Bye bye!");
                    log.saveLog();
                    continueLoop = false;
                    break;
            }
        }

    }

}
