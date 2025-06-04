import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

    public class MainPanel extends JPanel {
        CardLayout cardLayout;
        ArrayList<String[]> dataMahasiswa = new ArrayList<>();

        public MainPanel() {
            cardLayout = new CardLayout();
            setLayout(cardLayout);

            LoginPanel loginPanel = new LoginPanel(this);
            Dashboard dashboardPanel = new Dashboard(this, dataMahasiswa);
            PresensiPanel presensiPanel = new PresensiPanel(this, dataMahasiswa);

            add(loginPanel, "login");
            add(dashboardPanel, "dashboard");
            add(presensiPanel, "presensi");

            cardLayout.show(this, "login");
        }

        public void showPage(String name) {
            if (name.equals("presensi")) {
                ((PresensiPanel)getComponent(2)).refreshTable(dataMahasiswa); // index ke-2 = PresensiPanel
            }
            cardLayout.show(this, name);
        }

    }

