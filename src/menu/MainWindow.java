package menu;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle ("Sistema Rentacar - Equipo 7");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 15));


        //eze vehiculo , new Flotapanel

        //ernesto , cliente,reserva

        //Keensey , contrartos

        // Cristofer , pagos, daños, mantenimiento
        tabs.addTab("Pagos", new PagosPanel());
        tabs.addTab("Daños", new DanosPanel());
        tabs.addTab("Mantenimiento", new MantenimientoPanel());

        add(tabs, BorderLayout.CENTER);

        // Barra de estado abajo
        JLabel statusBar = new JLabel("  Sistema RentaCar - Equipo 7  |  Listo");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setFont(new Font("Arial", Font.PLAIN, 11));
        add(statusBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}