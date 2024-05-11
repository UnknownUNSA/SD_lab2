import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class server {
    private static HashMap<String, HashMap<String, Object>> clientData = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Servidor de reloj iniciado...");

            // Inicia el hilo para aceptar conexiones de clientes
            Thread connectionThread = new Thread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                        // Inicia un hilo para manejar la conexión del cliente
                        Thread clientHandlerThread = new Thread(() -> handleClient(clientSocket));
                        clientHandlerThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            connectionThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            while (true) {
                String clientTimeStr = in.readLine();
                Date clientTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(clientTimeStr);

                long timeDiff = System.currentTimeMillis() - clientTime.getTime();

                String clientAddress = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                HashMap<String, Object> clientInfo = new HashMap<>();
                clientInfo.put("time", clientTime);
                clientInfo.put("diff", timeDiff);

                clientData.put(clientAddress, clientInfo);

                System.out.println("Datos del cliente actualizados: " + clientAddress);

                Thread.sleep(5000); // Espera 5 segundos antes de volver a leer
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
