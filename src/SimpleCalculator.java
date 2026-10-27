import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * SimpleCalculator is a modern, professional desktop calculator application
 * built using Java Swing and AWT.
 * 
 * Key Features:
 * - Basic arithmetic: Addition (+), Subtraction (-), Multiplication (\u00D7), Division (\u00F7), Modulo/Remainder (%)
 * - Full operator precedence (BODMAS / PEMDAS) evaluated via CalculatorEngine
 * - Decimals and negative numbers (including \u00B1 negation toggle)
 * - Parentheses support for explicit operation grouping
 * - Division-by-zero and invalid-expression error handling
 * - Interactive buttons for digits, operators, equals, clear (C), and delete (DEL)
 * - Complete keyboard support (0-9, +, -, *, /, %, Enter, Backspace, Escape, parentheses)
 * - Clipboard copying of the current calculation result
 * - Modern dark theme UI with smooth rounded buttons and responsive hover effects
 * 
 * @author Internship Candidate
 * @version 1.0.0
 */
public class SimpleCalculator extends JFrame {

    // --- Unicode Constants for Clean Cross-Platform Portability ---
    public static final String OP_DIV = "\u00F7"; // Division symbol (÷)
    public static final String OP_MUL = "\u00D7"; // Multiplication symbol (×)
    public static final String OP_SUB = "-";      // Subtraction symbol (-)
    public static final String OP_ADD = "+";      // Addition symbol (+)
    public static final String OP_MOD = "%";      // Modulo symbol (%)
    public static final String BTN_NEG = "\u00B1"; // Negation symbol (±)

    // --- Color Palette (Modern Slate Dark Theme) ---
    private static final Color COLOR_WINDOW_BG       = new Color(24, 24, 27);    // Zinc 900
    private static final Color COLOR_DISPLAY_BG      = new Color(39, 39, 42);    // Zinc 800
    private static final Color COLOR_DISPLAY_BORDER  = new Color(63, 63, 70);    // Zinc 700
    private static final Color COLOR_HISTORY_TEXT    = new Color(161, 161, 170); // Zinc 400
    private static final Color COLOR_DISPLAY_TEXT    = new Color(255, 255, 255); // Crisp White
    private static final Color COLOR_ERROR_TEXT      = new Color(248, 113, 113); // Coral Red

    // Button Colors
    private static final Color COLOR_DIGIT_BG        = new Color(39, 39, 42);    // Zinc 800
    private static final Color COLOR_DIGIT_HOVER     = new Color(63, 63, 70);    // Zinc 700
    private static final Color COLOR_DIGIT_PRESS     = new Color(82, 82, 91);    // Zinc 600
    private static final Color COLOR_DIGIT_TEXT      = new Color(244, 244, 245); // Zinc 100

    private static final Color COLOR_OP_BG           = new Color(59, 130, 246);  // Accent Blue
    private static final Color COLOR_OP_HOVER        = new Color(37, 99, 235);   // Blue 600
    private static final Color COLOR_OP_PRESS        = new Color(29, 78, 216);   // Blue 700
    private static final Color COLOR_OP_TEXT         = Color.WHITE;

    private static final Color COLOR_CLEAR_BG        = new Color(239, 68, 68);   // Accent Red
    private static final Color COLOR_CLEAR_HOVER     = new Color(220, 38, 38);   // Red 600
    private static final Color COLOR_CLEAR_PRESS     = new Color(185, 28, 28);   // Red 700

    private static final Color COLOR_FN_BG           = new Color(63, 63, 70);    // Slate 700
    private static final Color COLOR_FN_HOVER        = new Color(82, 82, 91);    // Slate 600
    private static final Color COLOR_FN_PRESS        = new Color(113, 113, 122); // Slate 500

    private static final Color COLOR_EQUALS_BG       = new Color(16, 185, 129);  // Emerald Green
    private static final Color COLOR_EQUALS_HOVER    = new Color(5, 150, 105);   // Emerald 600
    private static final Color COLOR_EQUALS_PRESS    = new Color(4, 120, 87);    // Emerald 700

    // --- GUI Components ---
    private JLabel historyLabel;
    private JLabel displayLabel;

    // --- Calculator State ---
    private final StringBuilder currentInput = new StringBuilder();
    private final StringBuilder expression = new StringBuilder();
    private boolean isCalculated = false;
    private boolean isError = false;

    /**
     * Constructor initializes the calculator UI, registers components, and sets up key bindings.
     */
    public SimpleCalculator() {
        super("Simple Calculator");
        initUI();
        setupKeyBindings();
    }

    /**
     * Configures the main application window, panels, and layouts.
     */
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 580);
        setMinimumSize(new Dimension(330, 520));
        setLocationRelativeTo(null); // Center on screen
        getContentPane().setBackground(COLOR_WINDOW_BG);
        setLayout(new BorderLayout(10, 10));

        // Root wrapper with padding
        JPanel rootPanel = new JPanel(new BorderLayout(0, 12));
        rootPanel.setBackground(COLOR_WINDOW_BG);
        rootPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // 1. Display Panel (History + Main Result)
        JPanel displayContainer = createDisplayPanel();
        rootPanel.add(displayContainer, BorderLayout.NORTH);

        // 2. Middle Utility Toolbar (Parentheses + Copy Result)
        JPanel utilityPanel = createUtilityPanel();

        // 3. Main Keypad Grid (5 rows x 4 columns)
        JPanel keypadPanel = createKeypadPanel();

        // Combine utility panel and keypad into center
        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setBackground(COLOR_WINDOW_BG);
        centerPanel.add(utilityPanel, BorderLayout.NORTH);
        centerPanel.add(keypadPanel, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }

    /**
     * Creates the rounded display panel containing history and main input/result labels.
     */
    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_DISPLAY_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(COLOR_DISPLAY_BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(0, 4));
        panel.setBorder(new EmptyBorder(14, 16, 14, 16));
        panel.setPreferredSize(new Dimension(320, 100));

        // Top line: Expression history
        historyLabel = new JLabel("", SwingConstants.RIGHT);
        historyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        historyLabel.setForeground(COLOR_HISTORY_TEXT);
        historyLabel.setPreferredSize(new Dimension(300, 22));

        // Bottom line: Main display number / result / error
        displayLabel = new JLabel("0", SwingConstants.RIGHT);
        displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        displayLabel.setForeground(COLOR_DISPLAY_TEXT);
        displayLabel.setPreferredSize(new Dimension(300, 46));

        panel.add(historyLabel, BorderLayout.NORTH);
        panel.add(displayLabel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a secondary toolbar for parentheses and clipboard copy.
     */
    private JPanel createUtilityPanel() {
        JPanel utilityPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        utilityPanel.setBackground(COLOR_WINDOW_BG);
        utilityPanel.setPreferredSize(new Dimension(320, 36));

        ModernButton btnOpenParen = new ModernButton("(", COLOR_FN_BG, COLOR_FN_HOVER, COLOR_FN_PRESS, COLOR_DIGIT_TEXT, new Font("Segoe UI", Font.BOLD, 16));
        ModernButton btnCloseParen = new ModernButton(")", COLOR_FN_BG, COLOR_FN_HOVER, COLOR_FN_PRESS, COLOR_DIGIT_TEXT, new Font("Segoe UI", Font.BOLD, 16));
        ModernButton btnCopy = new ModernButton("Copy", COLOR_FN_BG, COLOR_FN_HOVER, COLOR_FN_PRESS, COLOR_DIGIT_TEXT, new Font("Segoe UI", Font.PLAIN, 14));

        btnOpenParen.addActionListener(e -> onParenthesisClicked("("));
        btnCloseParen.addActionListener(e -> onParenthesisClicked(")"));
        btnCopy.addActionListener(e -> onCopyClicked());

        utilityPanel.add(btnOpenParen);
        utilityPanel.add(btnCloseParen);
        utilityPanel.add(btnCopy);

        return utilityPanel;
    }

    /**
     * Creates the primary 5-row by 4-column keypad.
     */
    private JPanel createKeypadPanel() {
        JPanel grid = new JPanel(new GridLayout(5, 4, 8, 8));
        grid.setBackground(COLOR_WINDOW_BG);

        // Key definitions: text, type
        String[][] buttonLayout = {
            {"C", "DEL", OP_MOD, OP_DIV},
            {"7", "8",   "9",    OP_MUL},
            {"4", "5",   "6",    OP_SUB},
            {"1", "2",   "3",    OP_ADD},
            {BTN_NEG, "0", ".",  "="}
        };

        for (String[] row : buttonLayout) {
            for (String label : row) {
                ModernButton btn = createButtonForLabel(label);
                grid.add(btn);
            }
        }

        return grid;
    }

    /**
     * Factory method creating a styled ModernButton based on its operational role.
     */
    private ModernButton createButtonForLabel(String label) {
        ModernButton btn;
        Font fontRegular = new Font("Segoe UI", Font.PLAIN, 22);
        Font fontBold = new Font("Segoe UI", Font.BOLD, 22);

        if (label.equals("C")) {
            btn = new ModernButton(label, COLOR_CLEAR_BG, COLOR_CLEAR_HOVER, COLOR_CLEAR_PRESS, Color.WHITE, fontBold);
            btn.addActionListener(e -> onClearClicked());
        } else if (label.equals("DEL")) {
            btn = new ModernButton(label, COLOR_FN_BG, COLOR_FN_HOVER, COLOR_FN_PRESS, COLOR_DIGIT_TEXT, new Font("Segoe UI", Font.BOLD, 17));
            btn.addActionListener(e -> onDeleteClicked());
        } else if (label.equals(BTN_NEG)) {
            btn = new ModernButton(label, COLOR_FN_BG, COLOR_FN_HOVER, COLOR_FN_PRESS, COLOR_DIGIT_TEXT, fontRegular);
            btn.addActionListener(e -> onNegateClicked());
        } else if (label.equals("=")) {
            btn = new ModernButton(label, COLOR_EQUALS_BG, COLOR_EQUALS_HOVER, COLOR_EQUALS_PRESS, Color.WHITE, new Font("Segoe UI", Font.BOLD, 26));
            btn.addActionListener(e -> onEqualsClicked());
        } else if (label.equals(OP_ADD) || label.equals(OP_SUB) || label.equals(OP_MUL) || label.equals(OP_DIV) || label.equals(OP_MOD)) {
            btn = new ModernButton(label, COLOR_OP_BG, COLOR_OP_HOVER, COLOR_OP_PRESS, COLOR_OP_TEXT, fontBold);
            btn.addActionListener(e -> onOperatorClicked(label));
        } else if (label.equals(".")) {
            btn = new ModernButton(label, COLOR_DIGIT_BG, COLOR_DIGIT_HOVER, COLOR_DIGIT_PRESS, COLOR_DIGIT_TEXT, fontRegular);
            btn.addActionListener(e -> onDecimalClicked());
        } else { // Digits 0-9
            btn = new ModernButton(label, COLOR_DIGIT_BG, COLOR_DIGIT_HOVER, COLOR_DIGIT_PRESS, COLOR_DIGIT_TEXT, fontRegular);
            btn.addActionListener(e -> onDigitClicked(label));
        }

        return btn;
    }

    // =========================================================================
    // Event Handlers & State Management
    // =========================================================================

    private void onDigitClicked(String digit) {
        if (isError || isCalculated) {
            resetAllState();
        }

        if (currentInput.toString().equals("0")) {
            currentInput.setLength(0);
        }

        currentInput.append(digit);
        updateMainDisplay(currentInput.toString(), false);
    }

    private void onDecimalClicked() {
        if (isError || isCalculated) {
            resetAllState();
        }

        if (currentInput.length() == 0) {
            currentInput.append("0.");
        } else if (currentInput.indexOf(".") == -1) {
            currentInput.append(".");
        }

        updateMainDisplay(currentInput.toString(), false);
    }

    private void onOperatorClicked(String op) {
        if (isError) {
            return;
        }

        // If an operation was just evaluated with '=', chain from the result
        if (isCalculated) {
            isCalculated = false;
            expression.setLength(0);
            expression.append(displayLabel.getText()).append(" ").append(op).append(" ");
            historyLabel.setText(expression.toString());
            currentInput.setLength(0);
            return;
        }

        // If user has typed a number, append it with the operator
        if (currentInput.length() > 0) {
            expression.append(currentInput).append(" ").append(op).append(" ");
            historyLabel.setText(expression.toString());
            currentInput.setLength(0);
        } else if (expression.length() > 0) {
            // User pressed an operator right after another operator: replace trailing operator
            String trimmed = expression.toString().trim();
            int lastSpace = trimmed.lastIndexOf(" ");
            if (lastSpace != -1) {
                expression.setLength(lastSpace);
                expression.append(" ").append(op).append(" ");
            } else {
                expression.setLength(0);
                expression.append(trimmed).append(" ").append(op).append(" ");
            }
            historyLabel.setText(expression.toString());
        } else if (op.equals(OP_SUB)) {
            // Allow starting a negative number directly with '-'
            currentInput.append("-");
            updateMainDisplay(currentInput.toString(), false);
        }
    }

    private void onParenthesisClicked(String paren) {
        if (isError) {
            return;
        }
        if (isCalculated) {
            resetAllState();
        }

        if (paren.equals("(")) {
            if (currentInput.length() > 0) {
                // If number is already typed, treat as multiplication: e.g. 5( -> 5 × (
                expression.append(currentInput).append(" ").append(OP_MUL).append(" ( ");
                currentInput.setLength(0);
            } else {
                expression.append("( ");
            }
        } else { // ")"
            if (currentInput.length() > 0) {
                expression.append(currentInput).append(" ) ");
                currentInput.setLength(0);
            } else {
                expression.append(") ");
            }
        }

        historyLabel.setText(expression.toString());
    }

    private void onNegateClicked() {
        if (isError) {
            return;
        }

        if (isCalculated) {
            try {
                double val = -Double.parseDouble(displayLabel.getText());
                String formatted = CalculatorEngine.formatResult(val);
                updateMainDisplay(formatted, false);
            } catch (Exception ignored) {
            }
            return;
        }

        if (currentInput.length() > 0) {
            if (currentInput.charAt(0) == '-') {
                currentInput.deleteCharAt(0);
            } else {
                currentInput.insert(0, '-');
            }
            updateMainDisplay(currentInput.toString(), false);
        }
    }

    private void onDeleteClicked() {
        if (isError || isCalculated) {
            resetAllState();
            return;
        }

        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            if (currentInput.length() == 0 || currentInput.toString().equals("-")) {
                currentInput.setLength(0);
                updateMainDisplay("0", false);
            } else {
                updateMainDisplay(currentInput.toString(), false);
            }
        }
    }

    private void onClearClicked() {
        resetAllState();
    }

    private void onEqualsClicked() {
        if (isError) {
            return;
        }

        // Assemble full expression to evaluate
        String fullExpr;
        if (currentInput.length() > 0) {
            fullExpr = expression.toString() + currentInput.toString();
        } else if (expression.length() > 0) {
            fullExpr = expression.toString().trim();
        } else {
            fullExpr = displayLabel.getText();
        }

        if (fullExpr.trim().isEmpty()) {
            return;
        }

        try {
            double result = CalculatorEngine.evaluate(fullExpr);
            String formatted = CalculatorEngine.formatResult(result);

            historyLabel.setText(fullExpr + " =");
            updateMainDisplay(formatted, false);

            isCalculated = true;
            currentInput.setLength(0);
            expression.setLength(0);
        } catch (ArithmeticException ex) {
            historyLabel.setText(fullExpr + " =");
            String msg = ex.getMessage();
            if (msg != null && msg.toLowerCase().contains("zero")) {
                updateMainDisplay("Cannot divide by zero", true);
            } else {
                updateMainDisplay("Math Error", true);
            }
            isError = true;
        } catch (IllegalArgumentException ex) {
            historyLabel.setText(fullExpr + " =");
            updateMainDisplay("Invalid Expression", true);
            isError = true;
        } catch (Exception ex) {
            historyLabel.setText(fullExpr + " =");
            updateMainDisplay("Error", true);
            isError = true;
        }
    }

    private void onCopyClicked() {
        String text = displayLabel.getText();
        if (text != null && !text.isEmpty() && !isError) {
            StringSelection selection = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            // Subtle visual feedback in history label
            String originalHistory = historyLabel.getText();
            historyLabel.setText("Copied to clipboard!");
            Timer timer = new Timer(1200, evt -> historyLabel.setText(originalHistory));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void resetAllState() {
        currentInput.setLength(0);
        expression.setLength(0);
        historyLabel.setText("");
        updateMainDisplay("0", false);
        isError = false;
        isCalculated = false;
    }

    /**
     * Updates the main display label and dynamically adjusts font size to prevent clipping.
     */
    private void updateMainDisplay(String text, boolean error) {
        displayLabel.setText(text);
        if (error) {
            displayLabel.setForeground(COLOR_ERROR_TEXT);
            displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        } else {
            displayLabel.setForeground(COLOR_DISPLAY_TEXT);
            // Adjust font size based on length
            if (text.length() > 16) {
                displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            } else if (text.length() > 11) {
                displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            } else {
                displayLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
            }
        }
    }

    // =========================================================================
    // Keyboard Binding Support
    // =========================================================================

    private void setupKeyBindings() {
        JComponent root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        // Digits 0-9
        for (int i = 0; i <= 9; i++) {
            final String digit = String.valueOf(i);
            bindKey(im, am, "NUM_" + digit, KeyStroke.getKeyStroke(digit), e -> onDigitClicked(digit));
            bindKey(im, am, "NUMPAD_" + digit, KeyStroke.getKeyStroke("NUMPAD" + digit), e -> onDigitClicked(digit));
        }

        // Decimal point
        bindKey(im, am, "PERIOD", KeyStroke.getKeyStroke('.'), e -> onDecimalClicked());
        bindKey(im, am, "DECIMAL", KeyStroke.getKeyStroke("DECIMAL"), e -> onDecimalClicked());

        // Operators
        bindKey(im, am, "ADD", KeyStroke.getKeyStroke('+'), e -> onOperatorClicked(OP_ADD));
        bindKey(im, am, "SUB", KeyStroke.getKeyStroke('-'), e -> onOperatorClicked(OP_SUB));
        bindKey(im, am, "MUL", KeyStroke.getKeyStroke('*'), e -> onOperatorClicked(OP_MUL));
        bindKey(im, am, "DIV", KeyStroke.getKeyStroke('/'), e -> onOperatorClicked(OP_DIV));
        bindKey(im, am, "MOD", KeyStroke.getKeyStroke('%'), e -> onOperatorClicked(OP_MOD));

        // Equals & Enter
        bindKey(im, am, "EQUALS", KeyStroke.getKeyStroke('='), e -> onEqualsClicked());
        bindKey(im, am, "ENTER", KeyStroke.getKeyStroke("ENTER"), e -> onEqualsClicked());

        // Clear & Delete
        bindKey(im, am, "BACKSPACE", KeyStroke.getKeyStroke("BACK_SPACE"), e -> onDeleteClicked());
        bindKey(im, am, "ESCAPE", KeyStroke.getKeyStroke("ESCAPE"), e -> onClearClicked());
        bindKey(im, am, "KEY_C", KeyStroke.getKeyStroke('c'), e -> onClearClicked());
        bindKey(im, am, "KEY_CAP_C", KeyStroke.getKeyStroke('C'), e -> onClearClicked());

        // Parentheses
        bindKey(im, am, "OPEN_PAREN", KeyStroke.getKeyStroke('('), e -> onParenthesisClicked("("));
        bindKey(im, am, "CLOSE_PAREN", KeyStroke.getKeyStroke(')'), e -> onParenthesisClicked(")"));
    }

    private void bindKey(InputMap im, ActionMap am, String key, KeyStroke stroke, ActionListener action) {
        im.put(stroke, key);
        am.put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
    }

    // =========================================================================
    // Custom Modern Button Component
    // =========================================================================

    /**
     * ModernButton is a custom JButton with rounded corners, smooth hover feedback,
     * and high-contrast typography.
     */
    private static class ModernButton extends JButton {
        private final Color normalColor;
        private final Color hoverColor;
        private final Color pressColor;
        private boolean isHovered = false;

        public ModernButton(String text, Color normalColor, Color hoverColor, Color pressColor, Color textColor, Font font) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
            this.pressColor = pressColor;

            setFont(font);
            setForeground(textColor);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Color bgColor = normalColor;
            if (getModel().isPressed()) {
                bgColor = pressColor;
            } else if (isHovered) {
                bgColor = hoverColor;
            }

            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            // Draw centered text
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            Rectangle bounds = fm.getStringBounds(getText(), g2).getBounds();
            int x = (getWidth() - bounds.width) / 2;
            int y = (getHeight() - bounds.height) / 2 + fm.getAscent() - 2;
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    // =========================================================================
    // Application Entry Point
    // =========================================================================

    public static void main(String[] args) {
        // Ensure UI components are created on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Apply system look and feel for native title bar styling
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            SimpleCalculator app = new SimpleCalculator();
            app.setVisible(true);
        });
    }
}
