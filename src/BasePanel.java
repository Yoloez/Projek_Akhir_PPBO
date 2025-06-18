import javax.swing.*;

public abstract class BasePanel extends JPanel {

    // 'protected' agar bisa diakses oleh kelas anak (child class)
    protected MainPanel mainPanel;

    public BasePanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        // Pengaturan umum yang ada di setiap panel, sekarang cukup ditulis sekali di sini.
        setBackground(Theme.BACKGROUND_DARK);
        setLayout(null);
    }
}