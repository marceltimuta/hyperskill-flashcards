package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Dictionary {

    private Map<String, String> dictionary = new TreeMap<>();
    private Log log;
    private Random random = new Random();
    private Scanner scanner;

    public Dictionary(Log log) {
        this.log = log;
    }

    private static String getKey(String value, Map<String, String> dictionary) {
        String key = "";
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            if (entry.getValue().equals(value)) {
                key = entry.getKey();
            }
        }
        return key;
    }

    public void addCard() {
        log.outMessage("The card:");
        String card = log.inMessage();

        if (!dictionary.containsKey(card)) {
            log.outMessage("The definition of the card:");
            String definition = log.inMessage();

            if (!dictionary.containsValue(definition)) {
                dictionary.put(card, definition);
                log.outMessage("The pair (\"" + card + "\":\"" + definition + "\") has been added.");
            } else {
                log.outMessage("The definition of \"" + definition + "\" already exists");
            }

        } else {
            log.outMessage("The card \"" + card + "\" already exists.");
        }
    }

    public void removeCard() {
        log.outMessage("The card:");
        String card = log.inMessage();

        if (dictionary.containsKey(card)) {
            dictionary.remove(card);
            log.outMessage("The card has been removed.");
        } else {
            log.outMessage("Can't remove \"" + card + "\": there is no such card.");
        }
    }

    public void importCards() {
        log.outMessage("File name:");
        String inputFile = log.inMessage();

        try {
            Scanner cardFile = new Scanner(new File(inputFile));
            log.outMessage(readCardsFromFile(cardFile) + " cards have been loaded.");

        } catch (FileNotFoundException e) {
            log.outMessage("File not found.");
        }

    }

    public void ask() {
        log.outMessage("How many times to ask?");
        int timesToAsk = Integer.parseInt(log.inMessage());

        for (int i = 0; i < timesToAsk; i++) {
            List<String> keys = new ArrayList<>(dictionary.keySet());
            String key = keys.get(random.nextInt(keys.size()));

            log.outMessage("Print the definition of " + "\"" + key + "\"" + ":");
            String input = log.inMessage();

            if (dictionary.get(key).equals(input)) {
                log.outMessage("Correct answer.");
            } else if (dictionary.containsValue(input)) {
                String definitionOf = getKey(input, dictionary);
                log.outMessage("Wrong answer. The correct one is " + "\"" +
                        dictionary.get(key) + "\"" + ", you've just written the definition of " +
                        "\"" + definitionOf + "\".");
            } else {
                log.outMessage("Wrong answer. The correct one is " + "\"" + dictionary.get(key) + "\".");
            }
        }
    }

    public void exportCards() {
        log.outMessage("File name:");
        String outputFile = log.inMessage();

        try (PrintWriter printWriter = new PrintWriter(new File(outputFile))) {
            for (String card : dictionary.keySet()) {
                printWriter.println(card);
                printWriter.println(dictionary.get(card));
            }
            log.outMessage(dictionary.size() + " cards have been saved.");
        } catch (FileNotFoundException e) {
            log.outMessage("File not found.");
        }
    }

    private int readCardsFromFile(Scanner cardFile) {
        Map<String, String> cardsFromFile = new TreeMap<>();

        while (cardFile.hasNext()) {
            String card = cardFile.nextLine();
            String definition = cardFile.nextLine();
            cardsFromFile.put(card, definition);
        }

        dictionary.putAll(cardsFromFile);

        return cardsFromFile.size();
    }
}
