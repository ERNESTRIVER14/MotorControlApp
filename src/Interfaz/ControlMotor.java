package Interfaz;

import Serial.SerialArduino;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControlMotor extends JFrame {

    private SerialArduino conexion;
    private JLabel lblHora;
    private JLabel lblEstado; // NUEVO
    private Timer timer;

    public ControlMotor() {
        setTitle("Panel de Control - Motor de Pasos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(0x1B263B));
        getContentPane().add(principal);

        // ENCABEZADO
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x1B263B));
        header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("CONTROL DEL MOTOR DE PASOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 44));
        lblTitulo.setForeground(Color.WHITE);

        lblHora = new JLabel();
        lblHora.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblHora.setForeground(Color.LIGHT_GRAY);
        lblHora.setHorizontalAlignment(SwingConstants.CENTER);

        lblEstado = new JLabel("Estado: Esperando acción...");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblEstado.setForeground(Color.ORANGE);
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Recursos/Logo.png"));
            Image logo = logoIcon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logo));
            header.add(logoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.out.println("Logo no encontrado");
        }

        JPanel encabezadoCentro = new JPanel(new GridLayout(3, 1));
        encabezadoCentro.setBackground(new Color(0x1B263B));
        encabezadoCentro.add(lblTitulo);
        encabezadoCentro.add(lblEstado);   // NUEVO
        encabezadoCentro.add(lblHora);

        header.add(encabezadoCentro, BorderLayout.CENTER);
        principal.add(header, BorderLayout.NORTH);

        actualizarHora();
        timer = new Timer(1000, e -> actualizarHora());
        timer.start();

        // CENTRO
        JPanel centro = new JPanel(new GridLayout(2, 1));
        centro.setOpaque(false);

        JPanel botonesCirculares = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 50));
        botonesCirculares.setOpaque(false);

        BotonCircular btnInicio = new BotonCircular(">", "Inicio", new Color(0x28A745));
        BotonCircular btnStop = new BotonCircular("■", "Stop", new Color(0x007BFF));
        BotonCircular btnEmergencia = new BotonCircular("!", "Emergencia", new Color(0xDC3545));

        botonesCirculares.add(btnInicio);
        botonesCirculares.add(btnStop);
        botonesCirculares.add(btnEmergencia);

        JPanel botonesDireccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 10));
        botonesDireccion.setOpaque(false);

        JButton btnIzquierda = crearBotonPlano("Girar Izquierda");
        JButton btnDerecha = crearBotonPlano("Girar Derecha");

        botonesDireccion.add(btnIzquierda);
        botonesDireccion.add(btnDerecha);

        centro.add(botonesCirculares);
        centro.add(botonesDireccion);
        principal.add(centro, BorderLayout.CENTER);

        // SLIDER DE VELOCIDAD
        JPanel zonaInferior = new JPanel();
        zonaInferior.setBackground(new Color(0x1B263B));
        zonaInferior.setLayout(new BoxLayout(zonaInferior, BoxLayout.Y_AXIS));

        JLabel lblVelocidadPorcentaje = new JLabel("Velocidad: 50%");
        lblVelocidadPorcentaje.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblVelocidadPorcentaje.setForeground(Color.WHITE);
        lblVelocidadPorcentaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        zonaInferior.add(lblVelocidadPorcentaje);
        zonaInferior.add(Box.createVerticalStrut(10));

        JSlider slider = new JSlider(5, 15, 10);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setForeground(Color.WHITE);
        slider.setBackground(new Color(0x1B263B));
        slider.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setMaximumSize(new Dimension(600, 80));

        zonaInferior.add(slider);
        zonaInferior.setBorder(BorderFactory.createEmptyBorder(30, 0, 50, 0));
        principal.add(zonaInferior, BorderLayout.SOUTH);

        // CONEXIÓN ARDUINO
        conexion = new SerialArduino();
        boolean conectado = conexion.conectar("COM4");

        if (!conectado) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al Arduino", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // EVENTOS CON ACTUALIZACIÓN DE ESTADO
        btnInicio.addActionListener(e -> {
            conexion.enviarDato('I');
            lblEstado.setText("Estado: Motor en marcha");
        });

        btnStop.addActionListener(e -> {
            conexion.enviarDato('S');
            lblEstado.setText("Estado: Motor detenido");
        });

        btnEmergencia.addActionListener(e -> {
            conexion.enviarDato('E');
            lblEstado.setText("Estado: Paro de emergencia ACTIVADO");
        });

        btnDerecha.addActionListener(e -> {
            conexion.enviarDato('R');
            lblEstado.setText("Estado: Girando a la derecha");
        });

        btnIzquierda.addActionListener(e -> {
            conexion.enviarDato('L');
            lblEstado.setText("Estado: Girando a la izquierda");
        });

        slider.addChangeListener(e -> {
            int valor = slider.getValue();
            int porcentaje = 5 + (int)(((valor - slider.getMinimum()) * 95.0) / (slider.getMaximum() - slider.getMinimum()));
            lblVelocidadPorcentaje.setText("Velocidad: " + porcentaje + "%");
            lblEstado.setText("Estado: Velocidad ajustada a " + porcentaje + "%");

            String comando = valor + "V";
            for (char c : comando.toCharArray()) {
                conexion.enviarDato(c);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                conexion.cerrar();
                timer.stop();
            }
        });
    }

    private void actualizarHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lblHora.setText("Fecha y hora actual: " + sdf.format(new Date()));
    }

    private JButton crearBotonPlano(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setBackground(new Color(0x415A77));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(250, 60));
        btn.setFocusPainted(false);
        return btn;
    }

    // BOTÓN CIRCULAR
// BOTÓN CIRCULAR
class BotonCircular extends JPanel {
    private JButton botonInterno;

    public BotonCircular(String icono, String texto, Color color) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(280, 180));

        botonInterno = new JButton(icono) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }

            @Override
            public boolean contains(int x, int y) {
                int radius = getWidth() / 2;
                return (x - radius) * (x - radius) + (y - radius) * (y - radius) <= radius * radius;
            }
        };

        botonInterno.setFont(new Font("Segoe UI", Font.BOLD, 36));
        botonInterno.setForeground(Color.WHITE);
        botonInterno.setBackground(color);
        botonInterno.setFocusPainted(false);
        botonInterno.setPreferredSize(new Dimension(160, 160));
        botonInterno.setMaximumSize(new Dimension(160, 160));
        botonInterno.setBorderPainted(false);
        botonInterno.setContentAreaFilled(false);
        botonInterno.setOpaque(false);

        JLabel lblTexto = new JLabel(texto, SwingConstants.CENTER);
        lblTexto.setForeground(Color.WHITE);
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(botonInterno);
        add(Box.createVerticalStrut(10));
        add(lblTexto);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // 🔧 MÉTODO NECESARIO PARA PODER ASIGNAR ACCIONES
    public void addActionListener(ActionListener listener) {
        botonInterno.addActionListener(listener);
    }
}}
