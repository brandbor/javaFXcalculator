package sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
//TODO написати тести, щоб покриття було на 100%
    private Calculator calculator;
    private Display display = new DisplayStub();
    private Formula formula = new FormulaStub();

    @BeforeEach
    void setUp() {
        calculator = new Calculator(display, formula);
    }

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

