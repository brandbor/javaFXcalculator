package sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
//TODO написати тести, щоб покриття було на 100%
    private Calculator calculator;
    private CalculatorController calculatorController;
    private Display display = new DisplayStub();
    private Formula formula = new FormulaStub();
    Main mainClass = new Main();


    @BeforeEach
    void setUp() {
        calculator = new Calculator(display, formula);
    }

    @BeforeEach
    void setUpContr() {
        CalculatorController calculatorController = new CalculatorController();
    }

 //ТЕСТИ на клас Calculator -------------------------------------------------------------------------

    @Test
    void result(){
        calculator.digit("1");
        calculator.operator("+");
        calculator.digit("2");
        calculator.result();
        assertEquals(3,calculator.getNumberInt());
    }

    @Test
    void digit() {
        assertEquals("0",display.getDisplayNumber());
        calculator.digit("1.5");
        assertEquals("1.5",display.getDisplayNumber());
        calculator.digit("58");
        assertEquals("1.558",display.getDisplayNumber());
        //Перевірка чи натиснуто =
        calculator.digit("1");
        calculator.operator("+");
        calculator.digit("1");
        calculator.result();
        assertEquals("=",formula.getFormula().substring(formula.getFormula().length()-1));
        calculator.digit("2");
        assertEquals("2",formula.getFormula());
        assertEquals("2", display.getDisplayNumber());


    }

    @Test
    void operator(){
        calculator.digit("2");
        calculator.operator("+");
        calculator.digit("3");
        calculator.result();
        assertEquals(5,calculator.getNumberInt());
        assertEquals(true, formula.getFormula().contains("+"));
    }

    @Test
    void cancel() {
        //given
        calculator.digit("1");
        assertTrue(calculator.isLastButtonWasDigit());
        //when
        calculator.cancel();
        //then
        assertFalse(calculator.isLastButtonWasDigit());
        assertEquals("0",display.getDisplayNumber());
        assertEquals(0,calculator.getNumberInt());
        assertEquals(0,calculator.getNumberDouble());

    }

    @Test
    void plusMinus() {
        //Test1
        //given
        calculator.digit("10");
        //when
        calculator.plusMinus();
        //then
        assertEquals("-10",display.getDisplayNumber());

        //Test2
        //when
        calculator.plusMinus();
        //then
        assertEquals("10",display.getDisplayNumber());
    }

    @Test
    void comma() {
        //given
        calculator.digit("20");
        //when
        calculator.comma();
        calculator.digit("15");
        //then
        assertEquals("20.15",display.getDisplayNumber());
    }

    @Test
    void commaMayBeUsedOnlyOnce() {
        //given
        calculator.digit("20.15");
        //when
        calculator.comma();
        //then
        assertEquals("20.15",display.getDisplayNumber());
    }

    @Test
    void sqrtDigitWithoutCommaIntResult() {
        //given
        calculator.digit("16");
        //when
        calculator.sqrt();
        //then
        assertEquals("4",display.getDisplayNumber());
    }

    @Test
    void sqrtDigitWithoutCommaDoubleResult() {
        //given
        calculator.digit("9999");
        //when
        calculator.sqrt();
        //then
        assertEquals("99.99499987499375",display.getDisplayNumber());
    }

    @Test
    void sqrtDigitWithComma() {
        //given
        calculator.digit("1.44");
        //when
        calculator.sqrt();
        assertEquals("1.2",display.getDisplayNumber());
    }

    @Test
    void getNumberDouble() {
        //given
        display.setDisplayNumber("0.1");
        //when
        double number = calculator.getNumberDouble();
        assertEquals(0.1,number);
    }

    @Test
    void setNumberDouble() {
        //given
        double number = 42D ;
        //when
        calculator.setNumberDouble(number);
        assertEquals("42.0", display.getDisplayNumber());
    }

    @Test
    void getNumberInt() {
        //given
        display.setDisplayNumber("0");
        //when
        int number = calculator.getNumberInt();
        //then
        assertEquals(0,number);
    }

    @Test
    void setNumberInt() {
        //given
        int number = 56;
        //when
        calculator.setNumberInt(number);
        //then
        assertEquals("56",display.getDisplayNumber());
    }

    @Test
    void operatorOldNewEception() {
        //перевірка обробки Exception, +
        calculator.digit("5");
        calculator.operator("+");
        calculator.digit("10");
        //не коректна -
        calculator.operatorOldNew("-", "+");
        calculator.digit("2");
        assertThrows(IllegalStateException.class, () -> {
            calculator.getNewNumber(); });
    }
    @Test
    void operatorOldNewPlusInt() {
            //перевірка обробки -, int
            calculator.digit("5");
            calculator.operator("−");
            calculator.digit("2");
            calculator.operatorOldNew("+","−");
            calculator.digit("7");
            calculator.result();
            assertEquals("10", display.getDisplayNumber());
     }
    @Test
    void operatorOldNewMultiplyInt() {
        //перевірка оброкби х, int
        calculator.digit("5");
        calculator.operator("x");
        calculator.digit("2");
        calculator.operatorOldNew("x","x");
        calculator.digit("2");
        calculator.result();
        assertEquals("20",display.getDisplayNumber());
    }

    @Test
    void operatorOldNewDivideDouble() {
        //перевірка обробки ÷, double
        calculator.digit("5");
        calculator.operator("÷");
        calculator.digit("2");
        calculator.operatorOldNew("−","÷");
        calculator.digit("1");
        calculator.result();
        assertEquals("1.5",display.getDisplayNumber());
        calculator.cancel();

        //додаткова перевірка ÷, як оператора
        calculator.digit("1");
        calculator.operator("÷");
        calculator.digit("1");
        calculator.operatorOldNew("÷","÷");
        calculator.digit("2");
        calculator.result();
        assertEquals("0.5",display.getDisplayNumber());
    }

    @Test
    void operatorChange() {
        calculator.digit("1");
        calculator.operator("+");
        calculator.operatorChange("÷");
        assertEquals("÷",formula.getFormula().substring(formula.getFormula().length()-1));
    }

    @Test
    void backspace() {
        //базова перевірка
        calculator.digit("1");
        calculator.backspace();
        assertEquals("0",display.getDisplayNumber());
        assertEquals("",formula.getFormula());
        calculator.cancel();

        //перевірка відємних чисел
        calculator.digit("10");
        calculator.plusMinus();
        calculator.backspace();
        assertEquals("-1",display.getDisplayNumber());
        assertEquals("(-1)",formula.getFormula());
        calculator.cancel();

        //перевірка остатку від відємних
        calculator.digit("1");
        calculator.plusMinus();
        calculator.backspace();
        assertEquals("0",display.getDisplayNumber());
        assertEquals("",formula.getFormula());
        calculator.cancel();

        //якщо було натиснуто =
        calculator.digit("1");
        calculator.operator("+");
        calculator.digit("1");
        calculator.result();
        calculator.backspace();
        assertEquals("0",display.getDisplayNumber());
        assertEquals("",formula.getFormula());

        //перевірка Exception - після перевірки виявилося, що помилка ніколи не випадає
//        display.setDisplayNumber("");
//        calculator.backspace();
//        assertThrows(StringIndexOutOfBoundsException.class,()->{calculator.backspace();});
    }

    @Test
    void fractionOne() {
        //додатнє число
        calculator.digit("2");
        calculator.fractionOne();
        assertEquals("0.5",display.getDisplayNumber());

        //відємне вхідне int
        calculator.digit("-1");
        calculator.fractionOne();
        assertEquals("-1",display.getDisplayNumber());

        //відємне вхідне double
        calculator.digit("-0.5");
        calculator.fractionOne();
        assertEquals("-2",display.getDisplayNumber());

        //додатне вхідне double
        calculator.digit("0.5");
        calculator.fractionOne();
        assertEquals("2",display.getDisplayNumber());

        //Exception якщо вхідне число 0
        calculator.digit("0");
        assertThrows(ArithmeticException.class,()->{calculator.fractionOne();});
    }

    @Test
    void multiplyTwo() {
        //int
        calculator.digit("10");
        calculator.multiplyTwo();
        assertEquals("20",display.getDisplayNumber());
        //double
        calculator.digit("10.2");
        calculator.multiplyTwo();
        assertEquals("20.4",display.getDisplayNumber());
    }

    @Test
    void percent() {
        //вхідне число додатне double
        calculator.digit("1");
        calculator.operator("+");
        calculator.digit("20");
        calculator.percent();
        calculator.result();
        assertEquals("1.2",display.getDisplayNumber());

        //вхідне число додатне int
        calculator.digit("100");
        calculator.operator("−");
        calculator.digit("5");
        calculator.percent();
        calculator.result();
        assertEquals("95",display.getDisplayNumber());

        //вхідне число додатне, але результуюче число відємне int
        calculator.digit("-10");
        calculator.operator("÷");
        calculator.digit("50");
        calculator.percent();
        calculator.result();
        assertEquals("2",display.getDisplayNumber());

        //вхідне число додатне, але результуюче число відємне double
        calculator.digit("-20");
        calculator.operator("+");
        calculator.digit("1");
        calculator.percent();
        calculator.result();
        assertEquals("-20.2", display.getDisplayNumber());

        //вхідне число відємне double
        calculator.digit("5");
        calculator.operator("x");
        calculator.digit("50");
        calculator.plusMinus();
        calculator.percent();
        calculator.result();
        assertEquals("-12.5",display.getDisplayNumber());

        //вхідне число відємне int
        calculator.digit("10");
        calculator.operator("+");
        calculator.digit("100");
        calculator.plusMinus();
        calculator.percent();
        calculator.result();
        assertEquals("0",display.getDisplayNumber());


    }
    @Test
    void operatorClicked(){
// перевірено в рамках перевірки backspace
    }

    //ТЕСТИ на клас CalculatorController -------------------------------------------------------------------------
    void digitClic(){
  //      calculatorController.digitClic(num1);
    }


    private static class DisplayStub implements Display{
        private String displayNumber ="0";
        @Override
        public String getDisplayNumber() {
            return displayNumber;
        }

        @Override
        public void setDisplayNumber(String displayNumber) {
            this.displayNumber = displayNumber;
        }
    }

    private static class FormulaStub implements Formula{
        private String formula = "";
        @Override
        public String getFormula() {return formula;}

        @Override
        public void setFormula(String formulaNumber) {this.formula = formulaNumber;}
    }
}

