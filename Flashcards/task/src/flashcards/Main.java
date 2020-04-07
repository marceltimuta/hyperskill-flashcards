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

        Map<String, String> consoleCommands = new HashMap<>();
        for (int i = 0; i < args.length - 1; i++) {
            consoleCommands.put(args[i], args[++i]);
        }

        if (consoleCommands.containsKey("-import")) {
            dict.importCards(true, consoleCommands.getOrDefault("-import", ""));
        }


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
                    dict.importCards(false, "");
                    break;
                case "ask":
                    dict.ask();
                    break;
                case "export":
                    dict.exportCards(false, "");
                    break;
                case "log" :
                    log.saveLog();
                    break;
                case "hardest card" :
                    dict.getHardestCard();
                    break;
                case "reset stats" :
                    dict.resetStats();
                    break;
                case "exit":
                    log.outMessage("Bye bye!");
                    if (consoleCommands.containsKey("-export")) {
                        dict.exportCards(true, consoleCommands.getOrDefault("-export", ""));
                    }
                    continueLoop = false;
                    break;
            }
        }

    }

}
