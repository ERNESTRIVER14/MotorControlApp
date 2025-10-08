package Serial;

import com.fazecast.jSerialComm.SerialPort;
import java.io.OutputStream;

public class SerialArduino {
    private SerialPort puerto;
    private OutputStream salida;
//Conectar el programa al arduino
    public boolean conectar(String puertoNombre) {
        puerto = SerialPort.getCommPort(puertoNombre);
        puerto.setBaudRate(9600);
        if (puerto.openPort()) {
            salida = puerto.getOutputStream();
            return true;
        }
        return false;
    }
//Enviar datos
    public void enviarDato(char dato) {
        try {
            if (puerto != null && puerto.isOpen()) {
                salida.write(dato);
                salida.flush();
            }
        } catch (Exception e) {
            System.err.println("Error al enviar: " + e.getMessage());
        }
    }
//Cerrar
    public void cerrar() {
        try {
            if (puerto != null && puerto.isOpen()) {
                salida.close();
                puerto.closePort();
            }
        } catch (Exception e) {
            System.err.println("Error al cerrar: " + e.getMessage());
        }
    }
}

