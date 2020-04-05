package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Log {

    private Scanner scanner;
    private List <String> logs = new ArrayList<>();

    public Log(Scanner scanner) {
        this.scanner = scanner;
    }

    public void outMessage(String out) {
        System.out.println(out);
        logs.add(out);
    }

    public String inMessage() {
        String input = scanner.nextLine();
        logs.add(input);
        return input;
    }

    public void saveLog() {
        try (PrintWriter printWriter = new PrintWriter(new File("todayLog.txt"))) {
            for (String line : logs) {
                printWriter.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
