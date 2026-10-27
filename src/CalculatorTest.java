/**
 * CalculatorTest is an automated verification suite for the SimpleCalculator engine.
 * 
 * It tests arithmetic operations, operator precedence, decimals, negative numbers,
 * division-by-zero detection, modulo-by-zero detection, and invalid expression handling.
 * 
 * To run:
 *   javac -d bin src/*.java
 *   java -cp bin CalculatorTest
 */
public class CalculatorTest {

    private static int passedCount = 0;
    private static int failedCount = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   SimpleCalculator - Automated Test Suite       ");
        System.out.println("=================================================\n");

        // 1. Basic Arithmetic Operations
        testCategory("1. Basic Arithmetic Operations");
        assertResult("Addition: 15 + 27", "15 + 27", "42");
        assertResult("Subtraction: 100 - 37", "100 - 37", "63");
        assertResult("Multiplication: 8 * 9", "8 * 9", "72");
        assertResult("Unicode Multiplication: 8 × 9", "8 × 9", "72");
        assertResult("Division: 84 / 7", "84 / 7", "12");
        assertResult("Unicode Division: 84 ÷ 7", "84 ÷ 7", "12");
        assertResult("Remainder/Modulo: 19 % 4", "19 % 4", "3");

        // 2. Arithmetic Operator Precedence (BODMAS / PEMDAS)
        testCategory("2. Operator Precedence (BODMAS / PEMDAS)");
        assertResult("Precedence * over +: 2 + 3 * 4", "2 + 3 * 4", "14");
        assertResult("Precedence / over -: 20 - 12 / 3", "20 - 12 / 3", "16");
        assertResult("Precedence % over +: 10 + 14 % 4", "10 + 14 % 4", "12");
        assertResult("Left-to-right + and -: 10 - 4 + 2", "10 - 4 + 2", "8");
        assertResult("Left-to-right * and /: 20 / 4 * 2", "20 / 4 * 2", "10");
        assertResult("Combined: 5 + 3 * 4 - 8 / 2 + 10 % 3", "5 + 3 * 4 - 8 / 2 + 10 % 3", "14");

        // 3. Parentheses Support
        testCategory("3. Parentheses & Grouping");
        assertResult("Parentheses override precedence: (2 + 3) * 4", "(2 + 3) * 4", "20");
        assertResult("Nested parentheses: ((10 + 5) * 2) / 6", "((10 + 5) * 2) / 6", "5");
        assertResult("Complex parenthesized: 100 / (2 * (3 + 2))", "100 / (2 * (3 + 2))", "10");

        // 4. Decimal Operations and Precision
        testCategory("4. Decimal Operations & Precision");
        assertResult("Decimal addition: 0.1 + 0.2", "0.1 + 0.2", "0.3");
        assertResult("Decimal multiplication: 5.5 * 2", "5.5 * 2", "11");
        assertResult("Decimal division: 7.5 / 2.5", "7.5 / 2.5", "3");
        assertResult("Decimal modulo: 10.5 % 3", "10.5 % 3", "1.5");
        assertResult("Small decimals: 0.005 * 0.2", "0.005 * 0.2", "0.001");

        // 5. Negative Numbers and Unary Negation
        testCategory("5. Negative Numbers & Unary Negation");
        assertResult("Leading negative: -5 + 8", "-5 + 8", "3");
        assertResult("Negative operand: 10 + -3", "10 + -3", "7");
        assertResult("Negative multiplication: 5 * -4", "5 * -4", "-20");
        assertResult("Double negative: -10 / -2", "-10 / -2", "5");
        assertResult("Negative modulo: -14 % 4", "-14 % 4", "-2");
        assertResult("Negated subexpression: -(5 + 3)", "-(5 + 3)", "-8");

        // 6. Division and Modulo by Zero Handling
        testCategory("6. Division & Modulo by Zero Handling");
        assertThrowsArithmetic("Division by integer 0: 10 / 0", "10 / 0", "Cannot divide by zero");
        assertThrowsArithmetic("Division by decimal 0.0: 10 / 0.0", "10 / 0.0", "Cannot divide by zero");
        assertThrowsArithmetic("Modulo by integer 0: 15 % 0", "15 % 0", "Cannot divide by zero");
        assertThrowsArithmetic("Complex division by zero: 25 / (5 - 5)", "25 / (5 - 5)", "Cannot divide by zero");

        // 7. Invalid Expression Handling
        testCategory("7. Invalid Expression & Syntax Error Handling");
        assertThrowsSyntax("Dangling operator: 5 +", "5 +");
        assertThrowsSyntax("Consecutive operators: 5 + * 3", "5 + * 3");
        assertThrowsSyntax("Unmatched opening paren: (5 + 3", "(5 + 3");
        assertThrowsSyntax("Unmatched closing paren: 5 + 3)", "5 + 3)");
        assertThrowsSyntax("Empty expression: ''", "");
        assertThrowsSyntax("Multiple decimals: 5.2.1 + 3", "5.2.1 + 3");
        assertThrowsSyntax("Invalid characters: 5 + abc", "5 + abc");

        // Final Summary
        System.out.println("\n=================================================");
        System.out.println("   Test Results Summary");
        System.out.println("=================================================");
        System.out.println("Total Tests Executed : " + (passedCount + failedCount));
        System.out.println("Passed               : " + passedCount);
        System.out.println("Failed               : " + failedCount);

        if (failedCount == 0) {
            System.out.println("\n>>> ALL TESTS PASSED SUCCESSFULLY! <<<");
            System.exit(0);
        } else {
            System.err.println("\n>>> SOME TESTS FAILED! <<<");
            System.exit(1);
        }
    }

    private static void testCategory(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    private static void assertResult(String testName, String expression, String expected) {
        try {
            double rawResult = CalculatorEngine.evaluate(expression);
            String actual = CalculatorEngine.formatResult(rawResult);
            if (actual.equals(expected)) {
                System.out.println("  [PASS] " + testName + " => " + actual);
                passedCount++;
            } else {
                System.err.println("  [FAIL] " + testName + " => Expected '" + expected + "', got '" + actual + "'");
                failedCount++;
            }
        } catch (Exception ex) {
            System.err.println("  [FAIL] " + testName + " => Unexpected exception: " + ex.getMessage());
            failedCount++;
        }
    }

    private static void assertThrowsArithmetic(String testName, String expression, String expectedMessageSubstring) {
        try {
            CalculatorEngine.evaluate(expression);
            System.err.println("  [FAIL] " + testName + " => Expected ArithmeticException, but evaluated successfully!");
            failedCount++;
        } catch (ArithmeticException ex) {
            if (expectedMessageSubstring == null || ex.getMessage().toLowerCase().contains(expectedMessageSubstring.toLowerCase())) {
                System.out.println("  [PASS] " + testName + " => Threw ArithmeticException: " + ex.getMessage());
                passedCount++;
            } else {
                System.err.println("  [FAIL] " + testName + " => Exception message mismatch: " + ex.getMessage());
                failedCount++;
            }
        } catch (Exception ex) {
            System.err.println("  [FAIL] " + testName + " => Expected ArithmeticException, got: " + ex.getClass().getSimpleName());
            failedCount++;
        }
    }

    private static void assertThrowsSyntax(String testName, String expression) {
        try {
            CalculatorEngine.evaluate(expression);
            System.err.println("  [FAIL] " + testName + " => Expected IllegalArgumentException, but evaluated successfully!");
            failedCount++;
        } catch (IllegalArgumentException ex) {
            System.out.println("  [PASS] " + testName + " => Caught Syntax Error: " + ex.getMessage());
            passedCount++;
        } catch (Exception ex) {
            System.err.println("  [FAIL] " + testName + " => Expected IllegalArgumentException, got: " + ex.getClass().getSimpleName());
            failedCount++;
        }
    }
}
