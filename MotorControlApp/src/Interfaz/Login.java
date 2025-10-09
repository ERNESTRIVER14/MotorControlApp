package Interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public Login() {
        setTitle("Acceso - MotorControlApp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);//Pantalla Completa
        setUndecorated(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x1B263B));
        getContentPane().add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25); // Márgenes grandes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // LOGO (esquina superior derecha)
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Recursos/Logo.png"));// Logo cargado desde recursos
            Image logoEscalado = logoIcon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoEscalado));
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            panel.add(logoLabel, gbc);
        } catch (Exception e) {
            System.out.println("Error cargando logo: " + e);// Error en el caso que no este
        }

        // TÍTULO
        JLabel lblTitulo = new JLabel("BIENVENIDO A MOTORCONTROLAPP");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitulo, gbc);

        // USUARIO
        JLabel lblUsuario = new JLabel("USUARIO:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        lblUsuario.setForeground(Color.WHITE);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(lblUsuario, gbc);

        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtUsuario, gbc);

        // CONTRASEÑA
        JLabel lblPassword = new JLabel("CONTRASEÑA:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        lblPassword.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtPassword, gbc);

        // BOTÓN INGRESAR
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setBackground(new Color(0x415A77));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 32));
        btnIngresar.setFocusPainted(false);
        gbc.gridy = 4;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnIngresar, gbc);

        // ACCIÓN BOTÓN
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String pass = new String(txtPassword.getPassword());

                if (usuario.equals("admin") && pass.equals("1234")) {// Condicion para poner las credenciales correctas
                    dispose();
                    new ControlMotor().setVisible(true);// Para cambiar a la interfaz de control del motor
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);// Si no se ingresan bien
                }
            }
        });
    }
}

