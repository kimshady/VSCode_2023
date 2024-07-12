import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<Game> games = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Server <port> <hostname>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String hostname = args[1];

        // Game data
        games.add(new Game("Deep Rock Galactic", 9.99, 6.99));
        games.add(new Game("Darkest Dungeon", 59.99, 39.99));
        games.add(new Game("Street Fighter 6", 60.00, 45.99));

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port + "...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                String gameName = (String) in.readObject();
                Game game = findGame(gameName);
                if (game != null) {
                    out.writeObject(game.getName() + " is on sale for $" + game.getSalePrice() + " Which is " + game.getDiscountPercentage() + " off!");
                } else {
                    out.writeObject("Game not found");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }


    
    private static Game findGame(String gameName) {
        for (Game game : games) {
            if (game.getName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }
        return null;
    }

    
}
