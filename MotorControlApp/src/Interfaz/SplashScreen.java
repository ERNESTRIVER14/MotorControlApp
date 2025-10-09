package Interfaz;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    public SplashScreen() {
        // Pantalla completa 
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // hace que se vea en toda la pantalla

        // Fondo
        getContentPane().setBackground(new Color(0x1B263B));//color del fondo
        setLayout(new BorderLayout());

        // Panel central para mensaje
        JLabel mensaje = new JLabel("Cargando MotorControlApp...", SwingConstants.CENTER);
        mensaje.setForeground(Color.WHITE);
        mensaje.setFont(new Font("Segoe UI", Font.BOLD, 64)); // tamaño de la fuente
        add(mensaje, BorderLayout.CENTER);

        // Panel superior para logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(0x1B263B));
        logoPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 30, 30)); 

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Recursos/Logo.png"));//Seleccionar la imagen que tenemos en recursos
            Image logoEscalado = logoIcon.getImage().getScaledInstance(250, 160, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoEscalado));
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            JLabel error = new JLabel("Logo no encontrado");// En el caso que no se haya importado la imagen
            error.setForeground(Color.RED);
            logoPanel.add(error);
        }

        add(logoPanel, BorderLayout.NORTH);

        // Timer para cerrar splash y abrir login
        Timer timer = new Timer(3000, e -> {//el tiempo(3segundos)
            ((Timer) e.getSource()).stop();
            dispose();
            new Login().setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
