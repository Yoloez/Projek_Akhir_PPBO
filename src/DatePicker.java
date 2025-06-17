import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Kelas sederhana untuk menampilkan dialog pemilih tanggal.
 */
public class DatePicker extends JDialog {
    private int month;
    private int year;
    private JLabel lblMonthYear = new JLabel();
    private JButton[] buttons = new JButton[42]; // 7 kolom * 6 baris
    private String selectedDate;

    public DatePicker(JFrame parent) {
        super(parent, "Pilih Tangga", true); // true for modal dialog

        // Panel utama
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        p1.setPreferredSize(new Dimension(430, 120));

        // Buat tombol untuk hari (Sun, Mon, etc.)
        String[] header = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
        for (int x = 0; x < 7; x++) {
            p1.add(new JLabel(header[x], SwingConstants.CENTER));
        }

        // Buat tombol untuk tanggal
        for (int x = 0; x < buttons.length; x++) {
            final int selection = x;
            buttons[x] = new JButton();
            buttons[x].setFocusPainted(false);
            buttons[x].setBackground(Color.WHITE);
            buttons[x].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    // Jangan lakukan apa pun jika tombol kosong
                    if(buttons[selection].getText().isEmpty()) return;

                    // Format tanggal YYYY-MM-DD
                    String day = buttons[selection].getActionCommand();
                    LocalDate date = LocalDate.of(year, month + 1, Integer.parseInt(day));
                    selectedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

                    // Tutup dialog
                    dispose();
                }
            });
            p1.add(buttons[x]);
        }

        // Panel untuk navigasi bulan
        JPanel p2 = new JPanel(new BorderLayout());
        JButton btnPrev = new JButton("<<");
        btnPrev.addActionListener(e -> {
            month--;
            if (month < 0) {
                month = 11;
                year--;
            }
            displayDate();
        });
        p2.add(btnPrev, BorderLayout.WEST);

        p2.add(lblMonthYear, BorderLayout.CENTER);
        lblMonthYear.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnNext = new JButton(">>");
        btnNext.addActionListener(e -> {
            month++;
            if (month > 11) {
                month = 0;
                year++;
            }
            displayDate();
        });
        p2.add(btnNext, BorderLayout.EAST);


        // Atur layout dialog
        Container pane = getContentPane();
        pane.add(p1, BorderLayout.CENTER);
        pane.add(p2, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(parent);

        // Tampilkan tanggal hari ini saat pertama kali dibuka
        LocalDate today = LocalDate.now();
        this.month = today.getMonthValue() - 1;
        this.year = today.getYear();
        displayDate();
    }

    private void displayDate() {
        // Hapus teks dari semua tombol
        for (int x = 0; x < buttons.length; x++) {
            buttons[x].setText("");
        }

        Calendar cal = new GregorianCalendar(year, month, 1);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1=Sunday, 2=Monday, ...
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Atur teks tombol dengan angka tanggal
        for (int i = 1, day = 1; day <= daysInMonth; i++) {
            // dayOfWeek-1 karena array header kita mulai dari 0=Minggu
            if (i >= dayOfWeek) {
                buttons[i-1].setText(String.valueOf(day));
                buttons[i-1].setActionCommand(String.valueOf(day));
                day++;
            }
        }

        lblMonthYear.setText(new java.text.SimpleDateFormat("MMMM yyyy").format(cal.getTime()));
        setTitle("Pilih Tanggal");
    }

    /**
     * Mengembalikan tanggal yang dipilih dalam format YYYY-MM-DD.
     * @return String tanggal atau null jika tidak ada yang dipilih.
     */
    public String getSelectedDate() {
        return selectedDate;
    }
}
