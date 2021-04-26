import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

    /*Hilfsfunktion um Nachrichten auf der Konsole auszugeben --> später evtl. implementierung in FX*/
    public static void writeMessage(String message) {
        System.out.println(message);
        // TODO Konsolenausgabe ersetzen
    }

    /*Hilfsfunktion um Strings einzulesen*/
    public static String readString() {
        while (true) {
            try {
                String buf = bis.readLine();
                if (buf != null)
                    return buf;
            } catch (IOException e) {
                writeMessage("Error");
            }
        }
    }

    /*Hilfsfunktion um Integers einzulesen*/
    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(readString().trim());
            } catch (NumberFormatException e) {
                writeMessage("Keine Zahl");
            }
        }
    }
}
