package javafx.calculator

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

class CalculatorTest {

    Calculator calculator
    Display display = new DisplayStub()
    Formula formula = new FormulaStub()

    @BeforeEach
    void setUp() {
        calculator = new Calculator(display, formula)
    }

 //ТЕСТИ на клас javafx.calculator.Calculator -------------------------------------------------------------------------

    @Test
    void result(){
        calculator.digit("1")
        calculator.operator("+")
        calculator.digit("2")
        calculator.result()
        assert calculator.getNumberInt() == 3
    }

    @Test
    void digit() {
        assert display.getDisplayNumber() =="0"
        calculator.digit("1.5")
        assert display.getDisplayNumber() =="1.5"
        calculator.digit("58")
        assert display.getDisplayNumber() == "1.558"
        //Перевірка чи натиснуто =
        calculator.digit("1")
        calculator.operator("+")
        calculator.digit("1")
        calculator.result()
        assert formula.getFormula().substring(formula.getFormula().length()-1) == "="
        calculator.digit("2")
        assert formula.getFormula() == "2"
        assert display.getDisplayNumber() == "2"
    }

    @Test
    void operator(){
        calculator.digit("2")
        calculator.operator("+")
        calculator.digit("3")
        calculator.result()
        assert calculator.getNumberInt() == 5
        assert formula.getFormula().contains("+")
    }

    @Test
    void cancel() {
        //given
        calculator.digit("1")
        assert calculator.isLastButtonWasDigit()
        //when
        calculator.cancel()
        //then
        assert !calculator.isLastButtonWasDigit()
        assert display.getDisplayNumber() == "0"
        assert calculator.getNumberInt() == 0
        assert calculator.getNumberDouble() == 0

    }

    @Test
    void plusMinus() {
        //Test1
        //given
        calculator.digit("10")
        //when
        calculator.plusMinus()
        //then
        assert display.getDisplayNumber() == "-10"

        //Test2
        //when
        calculator.plusMinus()
        //then
        assert display.getDisplayNumber() == "10"
    }

    @Test
    void comma() {
        //given
        calculator.digit("20")
        //when
        calculator.comma()
        calculator.digit("15")
        //then
        assert display.getDisplayNumber() == "20.15"
    }

    @Test
    void commaMayBeUsedOnlyOnce() {
        //given
        calculator.digit("20.15")
        //when
        calculator.comma()
        //then
        assert display.getDisplayNumber() == "20.15"
    }

    @Test
    void sqrtDigitWithoutCommaIntResult() {
        //given
        calculator.digit("16")
        //when
        calculator.sqrt()
        //then
        assert display.getDisplayNumber() == "4"
    }

    @Test
    void sqrtDigitWithoutCommaDoubleResult() {
        //given
        calculator.digit("9999")
        //when
        calculator.sqrt()
        //then
        assert display.getDisplayNumber() == "99.99499987499375"
    }

    @Test
    void sqrtDigitWithComma() {
        //given
        calculator.digit("1.44")
        //when
        calculator.sqrt()
        assert display.getDisplayNumber() == "1.2"
    }

    @Test
    void getNumberDouble() {
        //given
        display.setDisplayNumber("0.1")
        //when
      //  double number = calculator.getNumberDouble()
       // assertEquals(0.1, number)//виникає warning тому тут написано на java
        assert calculator.getNumberDouble() == 0.1
    }

    @Test
    void setNumberDouble() {
        //given
        double number = 42D
        //when
        calculator.setNumberDouble(number)
        assert display.getDisplayNumber() == "42.0"
    }

    @Test
    void getNumberInt() {
        //given
        display.setDisplayNumber("0")
        //when
        int number = calculator.getNumberInt()
        //then
        assert number == 0
    }

    @Test
    void setNumberInt() {
        //given
        int number = 56
        //when
        calculator.setNumberInt(number)
        //then
        assert display.getDisplayNumber() == "56"
    }

    @Test
    void operatorOldNewEception() {
        //перевірка обробки Exception, +
        calculator.digit("5")
        calculator.operator("+")
        calculator.digit("10")
        //не коректна -
        calculator.operatorOldNew("-", "+")
        calculator.digit("2")
        assertThrows(IllegalStateException.class, () -> calculator.getNewNumber())
    }
    @Test
    void operatorOldNewPlusInt() {
            //перевірка обробки -, int
            calculator.digit("5")
            calculator.operator("−")
            calculator.digit("2")
            calculator.operatorOldNew("+","−")
            calculator.digit("7")
            calculator.result()
            assert display.getDisplayNumber() == "10"
     }
    @Test
    void operatorOldNewMultiplyInt() {
        //перевірка оброкби х, int
        calculator.digit("5")
        calculator.operator("x")
        calculator.digit("2")
        calculator.operatorOldNew("x","x")
        calculator.digit("2")
        calculator.result()
        assert display.getDisplayNumber() == "20"
    }

    @Test
    void operatorOldNewDivideDouble() {
        //перевірка обробки ÷, double
        calculator.digit("5")
        calculator.operator("÷")
        calculator.digit("2")
        calculator.operatorOldNew("−","÷")
        calculator.digit("1")
        calculator.result()
        assert display.getDisplayNumber() == "1.5"
        calculator.cancel()

        //додаткова перевірка ÷, як оператора
        calculator.digit("1")
        calculator.operator("÷")
        calculator.digit("1")
        calculator.operatorOldNew("÷","÷")
        calculator.digit("2")
        calculator.result()
        assert display.getDisplayNumber() == "0.5"
    }

    @Test
    void operatorChange() {
        calculator.digit("1")
        calculator.operator("+")
        calculator.operatorChange("÷")
        assert formula.getFormula().substring(formula.getFormula().length()-1) == "÷"
    }

    @Test
    void backspace() {
        //базова перевірка
        calculator.digit("1")
        calculator.backspace()
        assert display.getDisplayNumber() == "0"
        assert formula.getFormula() == ""
        calculator.cancel()

        //перевірка відємних чисел
        calculator.digit("10")
        calculator.plusMinus()
        calculator.backspace()
        assert display.getDisplayNumber() == "-1"
        assert formula.getFormula() == "(-1)"
        calculator.cancel()

        //перевірка остатку від відємних
        calculator.digit("1")
        calculator.plusMinus()
        calculator.backspace()
        assert display.getDisplayNumber() == "0"
        assert formula.getFormula() == ""
        calculator.cancel()

        //якщо було натиснуто =
        calculator.digit("1")
        calculator.operator("+")
        calculator.digit("1")
        calculator.result()
        calculator.backspace()
        assert display.getDisplayNumber() == "0"
        assert formula.getFormula() == ""

        //перевірка Exception - після перевірки виявилося, що помилка ніколи не випадає
//        display.setDisplayNumber("");
//        calculator.backspace();
//        assertThrows(StringIndexOutOfBoundsException.class,()->{calculator.backspace();});
    }

    @Test
    void fractionOne() {
        //додатнє число
        calculator.digit("2")
        calculator.fractionOne()
        assert display.getDisplayNumber() == "0.5"

        //відємне вхідне int
        calculator.digit("-1")
        calculator.fractionOne()
        assert display.getDisplayNumber() == "-1"

        //відємне вхідне double
        calculator.digit("-0.5")
        calculator.fractionOne()
        assert display.getDisplayNumber() == "-2"

        //додатне вхідне double
        calculator.digit("0.5")
        calculator.fractionOne()
        assert display.getDisplayNumber() == "2"

        //Exception якщо вхідне число 0
        calculator.digit("0")
        assertThrows(ArithmeticException.class,()-> calculator.fractionOne())
    }

    @Test
    void multiplyTwo() {
        //int
        calculator.digit("10")
        calculator.multiplyTwo()
        assert display.getDisplayNumber() == "20"
        //double
        calculator.digit("10.2")
        calculator.multiplyTwo()
        assert display.getDisplayNumber() == "20.4"
    }

    @Test
    void percent() {
        //вхідне число додатне double
        calculator.digit("1")
        calculator.operator("+")
        calculator.digit("20")
        calculator.percent()
        calculator.result()
        assert display.getDisplayNumber() == "1.2"

        //вхідне число додатне int
        calculator.digit("100")
        calculator.operator("−")
        calculator.digit("5")
        calculator.percent()
        calculator.result()
        assert display.getDisplayNumber() == "95"

        //вхідне число додатне, але результуюче число відємне int
        calculator.digit("-10")
        calculator.operator("÷")
        calculator.digit("50")
        calculator.percent()
        calculator.result()
        assert display.getDisplayNumber() == "2"

        //вхідне число додатне, але результуюче число відємне double
        calculator.digit("-20")
        calculator.operator("+")
        calculator.digit("1")
        calculator.percent()
        calculator.result()
        assert display.getDisplayNumber() == "-20.2"

        //вхідне число відємне double
        calculator.digit("5")
        calculator.operator("x")
        calculator.digit("50")
        calculator.plusMinus()
        calculator.percent()
        calculator.result()
        assert display.getDisplayNumber() == "-12.5"

        //вхідне число відємне int
        calculator.digit("10")
        calculator.operator("+")
        calculator.digit("100")
        calculator.plusMinus()
        calculator.percent()
        calculator.result()
        assert display.getDisplayNumber() == "0"

    }
    @Test
    void operatorClicked(){
// перевірено в рамках перевірки backspace
    }

    //ТЕСТИ на клас javafx.calculator.CalculatorController -------------------------------------------------------------------------
    //не пишемо, оскільки не проводяться математичні операції


    class DisplayStub implements Display{
        String displayNumber ="0"
    }

    class FormulaStub implements Formula {
        String formula = ""
    }
}

