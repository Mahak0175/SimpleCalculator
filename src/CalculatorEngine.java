import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * CalculatorEngine is the core mathematical engine for the SimpleCalculator application.
 * 
 * It parses and evaluates arithmetic expressions adhering to standard mathematical
 * operator precedence (BODMAS / PEMDAS) using a Recursive Descent parsing algorithm.
 * 
 * Features supported:
 * <ul>
 *   <li>Basic operations: Addition (+), Subtraction (-), Multiplication (*, \u00D7), Division (/, \u00F7), Remainder/Modulo (%)</li>
 *   <li>Operator precedence: Multiplicative operations (*, /, %) take precedence over additive (+, -)</li>
 *   <li>Associativity: Left-to-right for binary operations</li>
 *   <li>Unary minus and negation: Negative numbers (e.g., -5, 5 * -3, -(4 + 2))</li>
 *   <li>Parentheses grouping: Sub-expressions inside '(' and ')'</li>
 *   <li>Floating point precision handling: Cleans up IEEE-754 rounding artifacts (e.g. 0.1 + 0.2 = 0.3)</li>
 *   <li>Robust error handling: Explicitly catches and reports division-by-zero, modulo-by-zero, and malformed expressions</li>
 * </ul>
 * 
 * @author Internship Candidate
 * @version 1.0.0
 */
public class CalculatorEngine {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private CalculatorEngine() {
    }

    /**
     * Evaluates a mathematical expression string and returns the computed result.
     *
     * @param expression The mathematical expression (e.g. "5 + 3 * 2", "10 / (2 + 3)")
     * @return The evaluated result as a double
     * @throws ArithmeticException If division by zero or modulo by zero occurs
     * @throws IllegalArgumentException If expression is empty, malformed, or has unmatched parentheses
     */
    public static double evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be empty.");
        }

        // Normalize Unicode arithmetic symbols and strip extra whitespace
        // Using explicit Unicode escape sequences for 100% portable cross-platform compilation:
        // \u00D7 = multiplication sign (×)
        // \u00F7 = division sign (÷)
        // \u2212 = minus sign (−)
        String normalized = expression.replace("\u00D7", "*")
                                      .replace("\u00F7", "/")
                                      .replace("\u2212", "-")
                                      .replace("×", "*")
                                      .replace("÷", "/")
                                      .replace("−", "-")
                                      .replaceAll("\\s+", "");

        Parser parser = new Parser(normalized);
        double result = parser.parse();

        if (parser.hasNext()) {
            throw new IllegalArgumentException("Unexpected character at end: '" + parser.peek() + "'");
        }

        return result;
    }

    /**
     * Formats a floating-point numeric result into a clean, human-readable string.
     * 
     * Handles:
     * - Stripping redundant ".0" for whole numbers (e.g., 5.0 -> "5")
     * - Avoiding IEEE-754 precision artifacts (e.g., 0.1 + 0.2 -> "0.3" instead of "0.30000000000000004")
     * - Handling negative zero (-0.0 -> "0")
     *
     * @param value The double value to format
     * @return A clean formatted string representation
     * @throws ArithmeticException If the value is NaN or Infinite
     */
    public static String formatResult(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new ArithmeticException("Invalid numeric result (NaN or Infinity)");
        }

        // Treat extremely small values close to zero as 0
        if (Math.abs(value) < 1e-12) {
            return "0";
        }

        BigDecimal bd = new BigDecimal(Double.toString(value));
        // Round to 12 significant figures to remove floating-point binary inaccuracies
        bd = bd.round(new MathContext(12, RoundingMode.HALF_UP)).stripTrailingZeros();

        if (bd.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        return bd.toPlainString();
    }

    // =========================================================================
    // Recursive Descent Parser
    // Grammar:
    //   Expression -> Term ( ('+' | '-') Term )*
    //   Term       -> Factor ( ('*' | '/' | '%') Factor )*
    //   Factor     -> ('+' | '-') Factor | Primary
    //   Primary    -> '(' Expression ')' | Number
    // =========================================================================

    private static class Parser {
        private final String input;
        private int pos = 0;

        public Parser(String input) {
            this.input = input;
        }

        public boolean hasNext() {
            return pos < input.length();
        }

        public char peek() {
            return input.charAt(pos);
        }

        public double parse() {
            if (input.isEmpty()) {
                throw new IllegalArgumentException("Expression is empty.");
            }
            return parseExpression();
        }

        /**
         * Parses additive operations (+, -) with lowest precedence.
         */
        private double parseExpression() {
            double left = parseTerm();

            while (hasNext()) {
                char op = peek();
                if (op == '+' || op == '-') {
                    pos++; // consume operator
                    double right = parseTerm();
                    if (op == '+') {
                        left = left + right;
                    } else {
                        left = left - right;
                    }
                } else {
                    break;
                }
            }

            return left;
        }

        /**
         * Parses multiplicative operations (*, /, %) with higher precedence than additive.
         */
        private double parseTerm() {
            double left = parseFactor();

            while (hasNext()) {
                char op = peek();
                if (op == '*' || op == '/' || op == '%') {
                    pos++; // consume operator
                    double right = parseFactor();

                    if (op == '*') {
                        left = left * right;
                    } else if (op == '/') {
                        if (Math.abs(right) < 1e-15) {
                            throw new ArithmeticException("Cannot divide by zero");
                        }
                        left = left / right;
                    } else { // op == '%'
                        if (Math.abs(right) < 1e-15) {
                            throw new ArithmeticException("Cannot divide by zero");
                        }
                        left = left % right;
                    }
                } else {
                    break;
                }
            }

            return left;
        }

        /**
         * Parses unary signs (+, -) or falls through to primary operands.
         */
        private double parseFactor() {
            if (!hasNext()) {
                throw new IllegalArgumentException("Unexpected end of expression");
            }

            char ch = peek();
            if (ch == '+') {
                pos++;
                return parseFactor();
            }
            if (ch == '-') {
                pos++;
                return -parseFactor();
            }

            return parsePrimary();
        }

        /**
         * Parses primary tokens: parenthesized sub-expressions or numeric literals.
         */
        private double parsePrimary() {
            if (!hasNext()) {
                throw new IllegalArgumentException("Unexpected end of expression");
            }

            char ch = peek();

            // Handle parenthesized sub-expression
            if (ch == '(') {
                pos++; // consume '('
                double value = parseExpression();
                if (!hasNext() || peek() != ')') {
                    throw new IllegalArgumentException("Missing closing parenthesis ')'");
                }
                pos++; // consume ')'
                return value;
            }

            // Handle numeric literal
            int start = pos;
            boolean hasDot = false;

            while (hasNext()) {
                char c = peek();
                if (Character.isDigit(c)) {
                    pos++;
                } else if (c == '.') {
                    if (hasDot) {
                        throw new IllegalArgumentException("Invalid number format: multiple decimal points");
                    }
                    hasDot = true;
                    pos++;
                } else {
                    break;
                }
            }

            if (start == pos) {
                throw new IllegalArgumentException("Unexpected character at position " + pos + ": '" + ch + "'");
            }

            String numStr = input.substring(start, pos);
            try {
                return Double.parseDouble(numStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number literal: " + numStr);
            }
        }
    }
}
