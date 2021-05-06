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

}
