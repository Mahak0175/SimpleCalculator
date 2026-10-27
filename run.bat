@echo off
echo ============================================
echo   Launching SimpleCalculator Desktop GUI
echo ============================================
if not exist "bin" mkdir bin
javac -d bin src/CalculatorEngine.java src/SimpleCalculator.java src/CalculatorTest.java
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)
start "SimpleCalculator" javaw -cp bin SimpleCalculator
echo [SUCCESS] SimpleCalculator launched.
