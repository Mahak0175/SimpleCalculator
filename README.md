# SimpleCalculator - Desktop Java Application

A complete, fully functional, modern desktop calculator application built in **Java** utilizing **Java Swing** and **AWT**. Developed as a clean, production-ready project for an internship submission.

---

## 📋 Table of Contents
1. [Project Overview](#-project-overview)
2. [Key Features](#-key-features)
3. [Architecture & Design](#-architecture--design)
4. [Technologies Used](#-technologies-used)
5. [Prerequisites](#-prerequisites)
6. [Project Structure](#-project-structure)
7. [Compilation & Execution Guide (Windows)](#-compilation--execution-guide-windows)
8. [Running in Visual Studio Code](#-running-in-visual-studio-code)
9. [Automated Testing & Verification](#-automated-testing--verification)
10. [Keyboard Shortcuts](#-keyboard-shortcuts)
11. [Manual GitHub Upload Guide](#-manual-github-upload-guide)

---

## 🌟 Project Overview

`SimpleCalculator` is a graphical desktop application designed to provide intuitive, accurate, and responsive arithmetic calculations. Unlike standard calculators that evaluate operations in simple sequential order without precedence, `SimpleCalculator` incorporates an arithmetic evaluation engine based on a **Recursive Descent Parser**, guaranteeing strict mathematical precedence (**BODMAS / PEMDAS**).

The application features a sleek dark-slate user interface, antialiased custom controls, animated hover states, dynamic font scaling, clipboard integration, and comprehensive keyboard support.

---

## ✨ Key Features

- **Four Core Arithmetic Operations:** Addition (`+`), Subtraction (`-`), Multiplication (`×`), and Division (`÷`).
- **Remainder / Modulo Operation:** Remainder calculations (`%`) fully integrated with standard operator precedence.
- **Strict Operator Precedence:** Evaluates expressions mathematically (`*`, `/`, `%` bind tighter than `+`, `-`), so `2 + 3 * 4` evaluates accurately to `14` (not `20`).
- **Parentheses Grouping:** Full support for `(` and `)` sub-expressions to explicitly define evaluation order (e.g. `(2 + 3) * 4 = 20`).
- **Decimal & Negative Numbers:** Support for arbitrary decimal values (`.`) and instant number sign toggling via the `±` button.
- **Division-by-Zero Handling:** Explicitly guards against zero denominators and zero modulo divisors, presenting clear error alerts (`Cannot divide by zero`) instead of crashing or showing `Infinity`.
- **Invalid-Expression Handling:** Safely catches malformed syntax (e.g. dangling operators, mismatched parentheses) and displays `Invalid Expression`.
- **Precision Correction:** Eliminates binary floating-point rounding quirks (e.g., `0.1 + 0.2` formats strictly as `0.3` rather than `0.30000000000000004`).
- **Dual-Line Display:** 
  - **Upper Label:** Displays ongoing expression and calculation history.
  - **Lower Label:** Displays current numeric entry or formatted result with dynamic font autoscaling to prevent text clipping.
- **Clipboard Integration:** One-click `Copy` button to copy the evaluated result straight to the system clipboard.
- **Complete Keyboard Navigation:** Supports numpad and standard keyboard keys for all digits, operators, equals, delete, clear, and parentheses.

---

## 🏗 Architecture & Design

The application follows the **Separation of Concerns (MVC-inspired)** architectural pattern:

```
+-----------------------------------------------------------+
|                   SimpleCalculator (View/UI)              |
|  - Modern Dark UI Theme & Antialiased Custom Buttons      |
|  - Event Dispatch Thread (EDT) Management                 |
|  - Keyboard Bindings & Clipboard Integration              |
+-----------------------------+-----------------------------+
                              |
                     Delegates Expression
                              |
+-----------------------------v-----------------------------+
|                CalculatorEngine (Model/Core)              |
|  - Recursive Descent Parser Grammar:                      |
|      Expression -> Term ( ('+' | '-') Term )*             |
|      Term       -> Factor ( ('*' | '/' | '%') Factor )*   |
|      Factor     -> ('+' | '-') Factor | Primary           |
|      Primary    -> '(' Expression ')' | Number            |
|  - Precedence Enforcement & Zero-Divisor Guarding         |
|  - IEEE-754 Precision Cleanup (BigDecimal / MathContext)  |
+-----------------------------------------------------------+
```

---

## 💻 Technologies Used

- **Programming Language:** Java (JDK 8 or higher, tested on JDK 17 OpenJDK)
- **GUI Toolkit:** Java Swing (`javax.swing.*`) & Java AWT (`java.awt.*`)
- **Build System:** Standard Java Compiler (`javac`), zero third-party dependencies required.
- **Testing:** Standalone custom verification suite (`CalculatorTest.java`).

---

## ⚙ Prerequisites

Before compiling or running, ensure that a **Java Development Kit (JDK)** is installed and available in your environment `PATH`.

To verify your Java installation, open PowerShell or Command Prompt:

```powershell
javac -version
java -version
```

If version numbers (e.g., `javac 17.0.x` or higher) appear, your environment is ready.

---

## 📁 Project Structure

```
SimpleCalculator/
├── .vscode/
│   ├── launch.json          # VS Code launch and debug configuration
│   └── tasks.json           # VS Code compilation and test build tasks
├── src/
│   ├── CalculatorEngine.java # Pure arithmetic evaluator and recursive descent parser
│   ├── SimpleCalculator.java # Swing desktop GUI application and event handling
│   └── CalculatorTest.java   # Automated unit test suite (38 test cases)
├── .gitignore               # Ignores compiled bytecode, IDE, and OS artifacts
├── build.bat                # Windows batch script to compile the project
├── run.bat                  # Windows batch script to compile and launch GUI
├── test.bat                 # Windows batch script to run automated test suite
├── LICENSE                  # MIT Open Source License
└── README.md                # Project documentation and user guide
```

---

## 🚀 Compilation & Execution Guide (Windows)

All commands can be executed from the `SimpleCalculator` root directory.

### Method 1: Using the Included Windows Batch Scripts
- **Launch GUI directly:** Double-click `run.bat` or run:
  ```cmd
  run.bat
  ```
- **Run Automated Tests:** Double-click `test.bat` or run:
  ```cmd
  test.bat
  ```
- **Build Only:**
  ```cmd
  build.bat
  ```

---

### Method 2: Using Standard Windows PowerShell / Command Prompt

1. Open PowerShell and navigate to the project directory:
   ```powershell
   cd "e:\java web apps for internship\SimpleCalculator"
   ```

2. Compile all source files into the `bin` directory:
   ```powershell
   if (!(Test-Path bin)) { New-Item -ItemType Directory -Path bin }
   javac -d bin src/*.java
   ```

3. Launch the graphical desktop application:
   ```powershell
   java -cp bin SimpleCalculator
   ```

4. Run the automated test suite in terminal:
   ```powershell
   java -cp bin CalculatorTest
   ```

---

## 💻 Running in Visual Studio Code

1. Open Visual Studio Code.
2. Select **File > Open Folder...** and choose `SimpleCalculator` (`e:\java web apps for internship\SimpleCalculator`).
3. If prompted, install the official **Extension Pack for Java** by Microsoft.
4. **To Build & Run:**
   - Press **F5** or go to **Run > Start Debugging**.
   - Select **Launch SimpleCalculator (GUI)** to open the desktop application.
   - Select **Run Automated Tests** to run the unit tests in the integrated terminal.
5. **Using Tasks:**
   - Press `Ctrl + Shift + B` to trigger the default build task (`javac -d bin ...`).

---

## 🧪 Automated Testing & Verification

The included automated test suite (`CalculatorTest.java`) systematically validates 38 distinct test cases across 7 operational categories:

| Test Category | Example Test Cases | Status |
| :--- | :--- | :---: |
| **1. Basic Arithmetic** | `15 + 27 = 42`, `100 - 37 = 63`, `8 * 9 = 72`, `84 / 7 = 12`, `19 % 4 = 3` | ✅ PASS |
| **2. Operator Precedence** | `2 + 3 * 4 = 14`, `20 - 12 / 3 = 16`, `10 + 14 % 4 = 12`, `20 / 4 * 2 = 10` | ✅ PASS |
| **3. Parentheses & Grouping** | `(2 + 3) * 4 = 20`, `((10 + 5) * 2) / 6 = 5`, `100 / (2 * (3 + 2)) = 10` | ✅ PASS |
| **4. Decimals & Precision** | `0.1 + 0.2 = 0.3`, `5.5 * 2 = 11`, `7.5 / 2.5 = 3`, `10.5 % 3 = 1.5` | ✅ PASS |
| **5. Negatives & Negation** | `-5 + 8 = 3`, `10 + -3 = 7`, `5 * -4 = -20`, `-10 / -2 = 5`, `-(5 + 3) = -8` | ✅ PASS |
| **6. Division by Zero** | `10 / 0`, `10 / 0.0`, `15 % 0`, `25 / (5 - 5)` (Throws clean ArithmeticException) | ✅ PASS |
| **7. Malformed Syntax** | `5 +`, `5 + * 3`, `(5 + 3`, `5 + abc`, `5.2.1 + 3` (Caught as syntax error) | ✅ PASS |

**Test Result:** `38 / 38 Tests Passed (100% Pass Rate)`.

---

## ⌨ Keyboard Shortcuts

| Key | Action |
| :--- | :--- |
| `0` - `9` / Numpad `0` - `9` | Input Digits |
| `.` / Numpad `.` | Input Decimal Point |
| `+` | Addition Operator |
| `-` | Subtraction Operator |
| `*` | Multiplication Operator (`×`) |
| `/` | Division Operator (`÷`) |
| `%` | Remainder / Modulo Operator |
| `(` and `)` | Open and Close Parentheses |
| `Enter` or `=` | Calculate Result (Equals) |
| `Backspace` | Delete Last Input Character (DEL) |
| `Escape` or `C` / `c` | Clear All Inputs and Reset (C) |

---

## 📤 Manual GitHub Upload Guide

Follow these steps when you are ready to upload this project to your GitHub repository:

1. **Log in to GitHub:** Open [https://github.com](https://github.com) in your web browser.
2. **Create New Repository:**
   - Click the **`+`** icon in the upper-right corner and select **New repository**.
   - Name the repository: `SimpleCalculator`.
   - Set description: `A complete Java Swing desktop calculator application with BODMAS precedence for internship submission.`
   - Keep the repository **Public** (or **Private** based on submission requirements).
   - **Do NOT** initialize with a README, .gitignore, or license (these are already included locally).
   - Click **Create repository**.
3. **Push Local Files via Terminal:**
   Open PowerShell in the `SimpleCalculator` directory:
   ```powershell
   cd "e:\java web apps for internship\SimpleCalculator"
   git init
   git add .
   git commit -m "Initial commit: Complete SimpleCalculator application with tests and documentation"
   git branch -M main
   git remote add origin https://github.com/<YOUR-USERNAME>/SimpleCalculator.git
   git push -u origin main
   ```
4. **Alternative (Web Upload):**
   - On the new repository page on GitHub, click **uploading an existing file**.
   - Drag and drop all files and folders from `e:\java web apps for internship\SimpleCalculator`.
   - Click **Commit changes**.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.
