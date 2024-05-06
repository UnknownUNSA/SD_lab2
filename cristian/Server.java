import java.io.DataInputStream; 
import java.io.DataOutputStream; 
import java.io.IOException; 
import java.net.ServerSocket; 
import java.net.Socket; 
import java.net.SocketTimeoutException; 

public class Server extends Thread { 
    private final ServerSocket serverSocket; 

    public Server(int puerto) throws IOException { 
        serverSocket = new ServerSocket(puerto); 
    } 

    @Override 
    public void run() { 
        while (true) { 
            try { 
                // Aceptar una conexión de los clientes; llamada bloqueante 
                Socket server = serverSocket.accept(); 
                System.out.println("Conectado en: " + server.getRemoteSocketAddress()); 
                
                // Recibir mensaje de los clientes 
                DataInputStream in = new DataInputStream(server.getInputStream()); 
                long timeRecv = System.currentTimeMillis(); 
                System.out.println(in.readUTF()); 
                
                // Calcular la hora corregida utilizando el algoritmo de Cristian
                long timeServer = System.currentTimeMillis();
                long correctedTime = timeRecv + ((timeServer - timeRecv) / 2);
                
                // Enviar la hora corregida al cliente
                DataOutputStream out = new DataOutputStream(server.getOutputStream()); 
                out.writeLong(correctedTime);
                
                // Cerrar la conexión 
                server.close(); 
            } catch (SocketTimeoutException s) { 
                System.out.println("¡Tiempo de espera del socket!"); 
                break; 
            } catch (IOException e) { 
                e.printStackTrace(); 
                break; 
            } 
        } 
    } 

    public static void main(String[] args) { 
        int puerto = 9092; // Puerto por defecto
        try { 
            Thread t = new Server(puerto); 
            t.start(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
}

