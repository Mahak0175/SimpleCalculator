@echo off
echo ============================================
echo   Running SimpleCalculator Test Suite
echo ============================================
if not exist "bin" mkdir bin
javac -d bin src/CalculatorEngine.java src/SimpleCalculator.java src/CalculatorTest.java
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)
java -cp bin CalculatorTest
pause
