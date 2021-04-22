import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private MyChatClient client;

    public ReadThread(Socket socket, Runnable client) {
        this.socket = socket;
        this.client = (MyChatClient) client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            // System.out.println("reader def");
        } catch (IOException ex) {
            //System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                //  System.out.println("wait if responce");
                String response = reader.readLine();
                System.out.println("\n" + response);

                // prints the username after displaying the server's message
                // if (client.getUserName() != null) {
                //     System.out.print("[" + client.getUserName() + "]: ");
                //
            } catch (IOException ex) {
                System.out.println("Du bist weg!");
                break;
            }
        }
    }
}
