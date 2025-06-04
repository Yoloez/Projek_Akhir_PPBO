import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class Theme extends JPanel{
    // Colors
    public static final Color BACKGROUND_DARK = new Color(0x1E1E1E); // Very dark grey, almost black
    public static final Color BACKGROUND_LIGHT = new Color(0x2D2D2D); // Dark grey
    public static final Color FOREGROUND_LIGHT = Color.WHITE;
    public static final Color ACCENT_BLUE = new Color(0x007ACC); // Bright blue
    public static final Color TEXT_FIELD_BACKGROUND = new Color(0x3C3C3C); // Grey for text fields
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    public static final Color TABLE_HEADER_BG = new Color(0x3C3C3C);
    public static final Color TABLE_GRID_COLOR = new Color(0x4A4A4A);
    public static final Color TABLE_SELECTION_BG = ACCENT_BLUE;
    public static final Color TABLE_SELECTION_FG = Color.WHITE;


    // Fonts
    public static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 14);
    public static final Font FONT_TEXT_FIELD = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);
    public static final Font FONT_TABLE_HEADER = new Font("Arial", Font.BOLD, 12);
    public static final Font FONT_TABLE_CELL = new Font("Arial", Font.PLAIN, 12);

    // General styling method (optional, can be applied directly)
    public static void applyButtonStyles(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(ACCENT_BLUE);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false); // Removes the focus border
        button.setBorderPainted(false); // Optional: removes border for a flatter look
    }

    public static void applyTextFieldStyles(JTextField textField) {
        textField.setFont(FONT_TEXT_FIELD);
        textField.setBackground(TEXT_FIELD_BACKGROUND);
        textField.setForeground(FOREGROUND_LIGHT);
        textField.setCaretColor(FOREGROUND_LIGHT); // Cursor color
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1), // Blue border
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding
        ));
    }
    public static void applyPasswordFieldStyles(JPasswordField passwordField) {
        passwordField.setFont(FONT_TEXT_FIELD);
        passwordField.setBackground(TEXT_FIELD_BACKGROUND);
        passwordField.setForeground(FOREGROUND_LIGHT);
        passwordField.setCaretColor(FOREGROUND_LIGHT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    public static void applyLabelStyles(JLabel label) {
        label.setFont(FONT_LABEL);
        label.setForeground(FOREGROUND_LIGHT);
    }

    public static void applyTableStyles(JTable table, JScrollPane scrollPane) {
        table.setBackground(BACKGROUND_LIGHT);
        table.setForeground(FOREGROUND_LIGHT);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setFont(FONT_TABLE_CELL);
        table.setRowHeight(25);
        table.setSelectionBackground(TABLE_SELECTION_BG);
        table.setSelectionForeground(TABLE_SELECTION_FG);

        table.getTableHeader().setFont(FONT_TABLE_HEADER);
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
        table.getTableHeader().setForeground(FOREGROUND_LIGHT);

        scrollPane.getViewport().setBackground(BACKGROUND_LIGHT);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE));
    }
}