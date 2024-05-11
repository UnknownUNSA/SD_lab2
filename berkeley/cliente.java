import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class cliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            System.out.println("Conexión establecida con el servidor...");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Inicia un hilo para enviar la hora al servidor
            Thread sendTimeThread = new Thread(() -> {
                while (true) {
                    try {
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                        out.println(currentTime);
                        System.out.println("Hora enviada exitosamente");
                        Thread.sleep(5000); // Espera 5 segundos antes de enviar nuevamente
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            sendTimeThread.start();

            // Lee la hora sincronizada del servidor
            while (true) {
                String synchronizedTimeStr = in.readLine();
                Date synchronizedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(synchronizedTimeStr);

                System.out.println("Hora sincronizada con el servidor: " + synchronizedTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
