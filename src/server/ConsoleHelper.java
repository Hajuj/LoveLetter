package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The type Server.Console Helper.
 */
public class ConsoleHelper {
    private static final BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Write message.
     *
     * @param message the message
     */
    /*Hilfsfunktion um Nachrichten auf der Konsole auszugeben */
    public static void writeMessage(String message) {
        System.out.println(message);
    }

    /**
     * Read string string.
     *
     * @return the string
     */
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

    /**
     * Read int int.
     *
     * @return the int
     */
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
