import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI extends JFrame {
    private JTextField gameField;
    private JTextArea resultArea;

    public ClientGUI() {
        super("Steam Game Sales Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new BorderLayout());

        JLabel gameLabel = new JLabel("Enter a game to track:");
        gameField = new JTextField(20);
        JButton trackButton = new JButton("Track");
        trackButton.addActionListener(new TrackButtonListener());

        JPanel inputPanel = new JPanel();
        inputPanel.add(gameLabel);
        inputPanel.add(gameField);
        inputPanel.add(trackButton);

        resultArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private class TrackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String game = gameField.getText();
            if (game.equals("")) {
                resultArea.append("Please enter a game name.\n");
                return;
            }
            String serverIP = JOptionPane.showInputDialog("Enter the server IP address:");
            if (serverIP == null || serverIP.equals("")) {
                System.exit(1);
            }
            String portStr = JOptionPane.showInputDialog("Enter the server port number:");
            if (portStr == null || portStr.equals("")) {
                System.exit(1);
            }
            int port = Integer.parseInt(portStr);
            try (
                Socket socket = new Socket(serverIP, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ) {
                out.writeObject(game);
                String result = (String) in.readObject();
                resultArea.append(result + "\n");
            } catch (UnknownHostException ex) {
                resultArea.append("Error: Unknown host.\n");
            } catch (IOException ex) {
                resultArea.append("Error: Could not connect to server.\n");
            } catch (ClassNotFoundException ex) {
                resultArea.append("Error: Server sent an invalid response.\n");
            }
        }
    }

    public static void main(String[] args) {
        ClientGUI gui = new ClientGUI();
        gui.setVisible(true);
    }
}
