import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

public class Cliente {

    private String nombreServidor;
    private int puertoServidor;
    private static int contador; // el número de conexiones
    private Timer temporizador; // este temporizador es para enviar solicitudes al servidor cada 6 segundos
    private long t0; // el momento en que se envía la solicitud al servidor
    private long t3; // el momento en que se recibe la respuesta del servidor

    // Constructor
    public Cliente(String nombreServidor, int puertoServidor) {
        this.nombreServidor = nombreServidor;
        this.puertoServidor = puertoServidor;
        Cliente.contador = 0;
        this.temporizador = new Timer();
    }

    class Conversacion extends TimerTask {
        // Sobrescribe la función run en TimerTask
        @Override
        public void run() {
            if (contador < 10) {
                try {
                    System.out.println("Conectando a ... " + nombreServidor + " en el puerto " + puertoServidor);

                    // Conectar al servidor
                    Socket cliente = new Socket(nombreServidor, puertoServidor);
                    System.out.println("Conectado a " + cliente.getRemoteSocketAddress());

                    // Enviar mensaje al servidor
                    OutputStream haciaElServidor = cliente.getOutputStream();
                    DataOutputStream salida = new DataOutputStream(haciaElServidor);
                    t0 = System.currentTimeMillis();
                    salida.writeUTF("Hola desde " + cliente.getLocalSocketAddress());

                    // Recibir mensaje del servidor
                    InputStream desdeElServidor = cliente.getInputStream();
                    DataInputStream entrada = new DataInputStream(desdeElServidor);
                    long tiempoServidor = entrada.readLong(); // recibe el tiempo corregido del servidor
                    t3 = System.currentTimeMillis();

                    // Cerrar la conexión
                    cliente.close();

                    // Incrementar contador
                    contador++;

                    // Calcula la hora corregida utilizando el algoritmo de Cristian
                    long tiempoCorregido = tiempoServidor + ((t3 - t0) / 2);

                    // Convertir el tiempo corregido a una fecha y hora legible
                    Date fechaHora = new Date(tiempoCorregido);

                    // Imprimir la hora corregida en un formato legible
                    System.out.println("Hora corregida del servidor (Algoritmo de Cristian): " + fechaHora);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                temporizador.cancel();
                temporizador.purge();
            }
        }
    }

    public static void main(String[] args) {
        // Nombre del servidor
        String serverName = "localhost";

        // Puerto del servidor
        int serverPort = 9092;

        // Crear un cliente que se conectará al servidor
        Cliente client = new Cliente(serverName, serverPort);

        // Tiempo que el objeto Timer realizará las conexiones
        long period = 6000;

        // Instanciar la clase Conversacion
        Cliente.Conversacion conversation = client.new Conversacion();

        // Programar la tarea para ejecutarse periódicamente
        client.temporizador.schedule(conversation, 0, period);
    }
}

