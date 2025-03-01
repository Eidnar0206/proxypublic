package proxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommandInterface {
    public static void main(String[] args) {
        try {
            int managementPort = Integer.parseInt(ProxyCache.getConfig("managementPort"));
            String host = ProxyCache.getConfig("host");

            System.out.println("Command Interface connected to " + host + " on port " + managementPort);
            System.out.println("Available commands:");
            System.out.println("  - list_cache : List all cache entries");
            System.out.println("  - remove_cache <key> : Remove a specific cache entry");
            System.out.println("  - clear_cache : Clear all cache entries");
            System.out.println("  - exit : Close the interface");

            try (Socket socket = new Socket(host, managementPort);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

                String command;

                while (true) {
                    System.out.print("> ");
                    command = userInput.readLine().trim();

                    if (command.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting Command Interface.");
                        break;
                    }

                    out.println(command);

                    String response = in.readLine();
                    if (response == null || response.isEmpty()) {
                        System.out.println("No response from the server. Verify the command or the server state.");
                    } else {
                        System.out.println(response);
                        if (command.startsWith("list_cache")) {
                            System.out.println("End of cache list.");
                        } else if (command.startsWith("remove_cache")) {
                            System.out.println("Cache entry removed if it existed.");
                        } else if (command.equalsIgnoreCase("clear_cache")) {
                            System.out.println("All cache entries cleared.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
