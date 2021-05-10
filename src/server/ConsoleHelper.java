package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The type Server.Console Helper.
 *
 * @author Altug, Mohamad
 */
public class ConsoleHelper {
    private static final BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Write message.
     *
     * @param message the message
     */

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    /**
     * Read method for a string.
     *
     * @return the string
     */

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
