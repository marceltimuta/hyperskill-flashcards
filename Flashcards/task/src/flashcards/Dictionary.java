package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Dictionary {

    private Map<String, String> dictionary = new TreeMap<>();
    private Map<String, Integer> mistakes = new HashMap<>();
    private Log log;
    private Random random = new Random();

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
            mistakes.remove(card);
            log.outMessage("The card has been removed.");
        } else {
            log.outMessage("Can't remove \"" + card + "\": there is no such card.");
        }
    }

    public void importCards(boolean isConsoleArg, String fileName) {
        String inputFile;

        if (isConsoleArg) {
            inputFile = fileName;
        } else {
            log.outMessage("File name:");
            inputFile = log.inMessage();
        }

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
                mistakes.put(key, mistakes.getOrDefault(key, 0) + 1);
                log.outMessage("Wrong answer. The correct one is " + "\"" +
                        dictionary.get(key) + "\"" + ", you've just written the definition of " +
                        "\"" + definitionOf + "\".");
            } else {
                log.outMessage("Wrong answer. The correct one is " + "\"" + dictionary.get(key) + "\".");
                mistakes.put(key, mistakes.getOrDefault(key, 0) + 1);
            }
        }
    }

    public void resetStats() {
        log.outMessage("Card statistics has been reset.");
        mistakes.clear();
    }

    public void getHardestCard() {
        int maxErrors = mistakes.isEmpty() ? 0 : Collections.max(mistakes.values());
        if (mistakes.isEmpty() || maxErrors == 0) {
            log.outMessage("There are no cards with errors.");
        } else {
            List<String> cardsWithMostErrors = new ArrayList<>();
            for (String card : mistakes.keySet()) {
                if (mistakes.get(card).equals(maxErrors)) {
                    cardsWithMostErrors.add(card);
                }
            }
            log.outMessage(listToString(cardsWithMostErrors, maxErrors));
        }
    }

    public void exportCards(boolean isConsoleArg, String fileName) {
        String outputFile;

        if (isConsoleArg) {
            outputFile = fileName;
        } else {
            log.outMessage("File name:");
            outputFile = log.inMessage();
        }

        try (PrintWriter printWriter = new PrintWriter(new File(outputFile))) {
            for (String card : dictionary.keySet()) {
                printWriter.println(card);
                printWriter.println(dictionary.get(card));
                printWriter.println(mistakes.getOrDefault(card, 0));
            }
            log.outMessage(dictionary.size() + " cards have been saved.");
        } catch (FileNotFoundException e) {
            log.outMessage("File not found.");
        }
    }

    private int readCardsFromFile(Scanner cardFile) {
        Map<String, String> cardsFromFile = new TreeMap<>();
        Map<String, Integer> errorCount = new HashMap<>();

        while (cardFile.hasNext()) {
            String card = cardFile.nextLine();
            String definition = cardFile.nextLine();
            String errorStr = cardFile.nextLine();
            int mistakes = errorStr.isEmpty() ? 0 : Integer.parseInt(errorStr);
            cardsFromFile.put(card, definition);
            errorCount.put(card, mistakes);
        }
        dictionary.putAll(cardsFromFile);
        mistakes.putAll(errorCount);
        return cardsFromFile.size();
    }

    private String listToString(List<String> list, int maxErrors) {
        String output = "";
        if (list.size() == 1) {
            return "The hardest card is \"" + list.get(0) + "\"." +
                    " You have " + maxErrors + " errors answering it.";
        } else {
            output = "The hardest cards are ";
            for (int i = 0; i < list.size(); i++) {
                output += "\"" + list.get(i) + "\"";
                if (i < list.size() - 1) {
                    output += ",";
                }
            }
            output += ". You have " + maxErrors + " errors answering them.";
        }
        return output;
    }
}
