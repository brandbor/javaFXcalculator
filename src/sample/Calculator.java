package sample;

import java.util.Arrays;
import java.util.Collections;

class Calculator {

    private final Display display;
    private boolean lastButtonWasDigit;
    private String operator;

    //змінна, яка зберігає суму по формулі
    private double a;

    private final Formula formula;
   // private String[] operators = new String[]{"+", "−", "x", "÷"};

    Calculator(Display display, Formula formula) {
        this.display = display;
        this.formula = formula;
        }

    boolean isLastButtonWasDigit() {
        return lastButtonWasDigit;
    }

    //БЛОК РОБОТИ З ЧИСЛАМИ
    void digit(String digit) {
        //онулення попередніх результатів, якщо вже було натиснуто =
        if (formula.getFormula().contains("=")) {
            a=0;
            formula.setFormula(digit);
            display.setDisplayNumber(digit);
        } else {
            if (display.getDisplayNumber().equals("0")) {
                display.setDisplayNumber(digit);
                lastButtonWasDigit = true;
            } else {
                if (lastButtonWasDigit) display.setDisplayNumber(display.getDisplayNumber() + digit);
                else display.setDisplayNumber(digit);
                lastButtonWasDigit = true;}
            formula.setFormula(formula.getFormula() + digit);
        }
    }

    void plusMinus() {
        if(!display.getDisplayNumber().contains("-")){
            display.setDisplayNumber("-"+display.getDisplayNumber());

//визначаємо довжину числа на дисплеї, потім віднімаємо 1, так як на дисплеї вже додано "-", а у формулі лише число без мінуса
            int displayLength = display.getDisplayNumber().length()-1;
            formula.setFormula(formula.getFormula().substring(0,formula.getFormula().length()-displayLength)+"("+display.getDisplayNumber()+")");
        }else if (display.getDisplayNumber().contains("-")){
            System.out.println(display.getDisplayNumber());

//визначаємо довжину числа на дисплеї, потім додаємо 2, так як на дисплеї вже немає "-" і немає дужок, а у формулі є
            int displayLength = display.getDisplayNumber().length()+2;
            display.setDisplayNumber(display.getDisplayNumber().replace("-",""));
            formula.setFormula(formula.getFormula().substring(0,formula.getFormula().length()-displayLength)+display.getDisplayNumber());
        }
    }

    void comma() {
        if(!display.getDisplayNumber().contains(".")){
            display.setDisplayNumber(display.getDisplayNumber()+".");
            lastButtonWasDigit = true;
            formula.setFormula(formula.getFormula()+".");
        }
    }

    //БЛОК РОБОТИ З ОПЕРАТОРАМИ
    void operator(String operator) {
        this.operator = operator;
        a = getNumberDouble();
        lastButtonWasDigit = false;
        display.setDisplayNumber("");
        formula.setFormula(formula.getFormula()+operator);
    }

    void operatorOldNew(String operator, String oldOperator) {
            switch (oldOperator){
                case "+": a = a + getNumberDouble(); break;
                case "−": a = a - getNumberDouble(); break;
                case "x": a = a * getNumberDouble(); break;
                case "÷": a = a / getNumberDouble(); break;
            }
        lastButtonWasDigit = false;
        this.operator = operator;
        if(a%1==0) setNumberInt((int)a);
        else setNumberDouble(a);
        formula.setFormula(formula.getFormula()+operator);
    }

    //якщо було вже натиснуто оператор, і натискається знову інший оператор, то оператор міняється
    void operatorChange(String operator) {
        formula.setFormula(formula.getFormula().substring(0, formula.getFormula().length() - 1)+operator);
        this.operator = operator;
    }

    //Натискання С
    void cancel() {
        display.setDisplayNumber("0");
        formula.setFormula("");
        lastButtonWasDigit = false;
        a=0;
    }

    //Натискання backspace
    void backspace() {
        //якщо є формула і робиться відкат - стирається лише останнє число

        //якщо формули немає, а в дисплеї стоїть 0, то нічого не робити
        if (formula.getFormula().equals("") & display.getDisplayNumber().equals("0")) System.out.println("Do nothing");
        else {
            //перевірка, якщо у формулі стоїть "=" і робиться відкат - прирівняно до оператора Cancel
            if (formula.getFormula().contains("=")) {
                display.setDisplayNumber("0");
                formula.setFormula("");
            } else {
                //проводиться перевірка на помилку. Виникає помилка, коли довжина числа в display <1
                //помилка обробляється
              //  try {
                    //якщо останній симфол у формулі один із математичних симфолів - нічого не робити
                    if (operatorClicked()) System.out.println("Do nothing");
                    else {
                        display.setDisplayNumber(display.getDisplayNumber().substring(0, display.getDisplayNumber().length() - 1));
                        //обробка відємних чисел
                        if (formula.getFormula().substring(formula.getFormula().length() - 1).equals(")")) {
                            formula.setFormula(formula.getFormula().substring(0, formula.getFormula().length() - 2) + ")");
                        } else formula.setFormula(formula.getFormula().substring(0, formula.getFormula().length() - 1));
                        //обробка остатку від відємних чисел
                        if (display.getDisplayNumber().equals("-")) {
                            display.setDisplayNumber("0");
                            formula.setFormula(formula.getFormula().substring(0, formula.getFormula().length() - 3));
                        }
                    }
//                } catch (StringIndexOutOfBoundsException e) {
//                        e.printStackTrace();
//                }
            }
        }
        //пост-перевірка після видалення останнього числа чи є ще щось на дисплеї і у формулі
        //і якщо немає встановити нуль на дисплеї
        if (formula.getFormula().equals("") & display.getDisplayNumber().equals("")) display.setDisplayNumber("0");
    }

    //Блок унарних операцій
    void sqrt() {
        if(display.getDisplayNumber().contains("Error")) System.out.println("Do nothing");
        else {
            double newNumber;
            try {
                newNumber = getNewNumber();
            } catch (NullPointerException e) {
                newNumber = getNumberDouble();}

            if (newNumber < 0) display.setDisplayNumber("Error -> SQRT could not be find from negative number");
            else {
                double newNumberSQRT = Math.sqrt(newNumber);

                //перевірка вхідного числа на цілісність і запис формули
                if (newNumber % 1 == 0) formula.setFormula("sqrt(" + (int) newNumber + ") =");
                else formula.setFormula("sqrt(" + newNumber + ") =");

                //перевірка результуючого числа на цілісність і запис на екран
                if (newNumberSQRT % 1 == 0) setNumberInt((int) newNumberSQRT);
                else setNumberDouble(newNumberSQRT);
            }
        }
    }

       void fractionOne() throws ArithmeticException {
        if (display.getDisplayNumber().contains("Error")) System.out.println("Do nothing");
        else {
            double newNumber;
            try {
                newNumber = getNewNumber();
            } catch (NullPointerException e) {
                newNumber = getNumberDouble();}

            if (newNumber == 0) {
                display.setDisplayNumber("Error -> Division by zero");
                throw new ArithmeticException("Error -> Division by zero");
            } else {
                double newNumberFract = 1 / newNumber;

                //перевірка вхідного числа на цілісність і запис формули
                if (newNumber % 1 == 0) {
                    //додаткова перевірка чи число відємне
                    if (newNumber < 0) formula.setFormula("1/" + "(" + (int) newNumber + ")" + " =");
                    else formula.setFormula("1/" + (int) newNumber + " =");
                } else {
                    //додаткова перевірка чи число відємне
                    if (newNumber < 0) formula.setFormula("1/" + "(" + newNumber + ")" + " =");
                    else formula.setFormula("1/" + newNumber + " =");
                }

                //перевірка результуючого числа на цілісність і запис на екран
                if (newNumberFract % 1 == 0) setNumberInt((int) newNumberFract);
                else setNumberDouble(newNumberFract);
            }
        }
    }

    void multiplyTwo() {
        if(display.getDisplayNumber().contains("Error")) System.out.println("Do nothing");
        else {
            double newNumber;
            try {
                newNumber = getNewNumber();
            } catch (NullPointerException e) {
                newNumber = getNumberDouble(); }

            double newNumberMult = newNumber * 2;

            //перевірка вхідного числа на цілісність і запис формули
            if (newNumber % 1 == 0) {
                //додаткова перевірка чи число відємне
                if (newNumber < 0) formula.setFormula("(" + (int) newNumber + ")" + "*2 =");
                else formula.setFormula((int) newNumber + "*2 =");
            } else {
                //додаткова перевірка чи число відємне
                if (newNumber < 0) formula.setFormula("(" + newNumber + ")" + "*2 =");
                else formula.setFormula(newNumber + "*2 =");
            }

            //перевірка результуючого числа на цілісність і запис на екран
            if (newNumberMult % 1 == 0) setNumberInt((int) newNumberMult);
            else setNumberDouble(newNumberMult);
        }
    }

    void percent() {
        //якщо натиснуто = то кнопка не працює
        if (formula.getFormula().contains("=")) System.out.println("Do nothing");
        else if (operatorClicked()) System.out.println("Do nothing");
        else {
            //знайти довжину останнього числа
            int lastNumberLengthPlus = formula.getFormula().lastIndexOf("+");
            int lastNumberLengthMinus = formula.getFormula().lastIndexOf("−");
            int lastNumberLengthMultiply = formula.getFormula().lastIndexOf("x");
            int lastNumberLengthDivide = formula.getFormula().lastIndexOf("÷");
            Integer[] lengthArray = {lastNumberLengthPlus, lastNumberLengthMinus, lastNumberLengthMultiply,lastNumberLengthDivide};
            Arrays.sort(lengthArray, Collections.reverseOrder());
            int lastNumberLength = formula.getFormula().length()-(lengthArray[0]+1);


            //отримати частину формули з останнім числом
            String lastNumberStr = formula.getFormula().substring((formula.getFormula().length()-lastNumberLength));
            //обрізати формулу на довжину останнього числа
            formula.setFormula(formula.getFormula().substring(0, formula.getFormula().length() - lastNumberLength));

            //розрахунок % від попереднього чила / набору чисел і операторів
            double percentNumber = a*(getNumberDouble()/100);

            //заміна числа на дисплеї і у формулі
            //якщо вхідне число відємне
            if(lastNumberStr.contains("-")){
                if (percentNumber % 1 == 0) {
                    setNumberInt((int) percentNumber);
                    formula.setFormula(formula.getFormula() + "("+(int) percentNumber+")");
                } else {
                    setNumberDouble(percentNumber);
                    formula.setFormula(formula.getFormula() + "("+percentNumber+")");
                }
            }
            //якщо вхідне число додатнє
            else {
                //якщо результуюче число відємне
                if(percentNumber<0){
                    if (percentNumber % 1 == 0) {
                        setNumberInt((int) percentNumber);
                        formula.setFormula(formula.getFormula() + "("+(int) percentNumber+")");
                    } else {
                        setNumberDouble(percentNumber);
                        formula.setFormula(formula.getFormula() + "("+percentNumber+")");
                    }
                }
                else{
                    if (percentNumber % 1 == 0) {
                        setNumberInt((int) percentNumber);
                        formula.setFormula(formula.getFormula() + (int) percentNumber);
                    } else {
                        setNumberDouble(percentNumber);
                        formula.setFormula(formula.getFormula() + percentNumber);
                    }
                }
            }
        }
    }

    void result() {
        double newNumber = getNewNumber();
        if (newNumber%1==0){
            setNumberInt((int)newNumber);
        }else {
            setNumberDouble(newNumber);
        }
        formula.setFormula(formula.getFormula()+"=");
    }


    //getter and setter for double numbers
    double getNumberDouble(){
        return Double.parseDouble(display.getDisplayNumber());
    }
    void setNumberDouble(double number){
        display.setDisplayNumber(String.valueOf(number));
    }

    //getter and setter for int number
    int getNumberInt(){
        return Integer.parseInt(display.getDisplayNumber());
    }
    void setNumberInt(int number){display.setDisplayNumber(String.valueOf(number));}

    //перевірка останнього символу формули
    private boolean operatorClicked() {
        return formula.getFormula().substring(formula.getFormula().length()-1).contains("+") ||
                formula.getFormula().substring(formula.getFormula().length()-1).contains("−") ||
                formula.getFormula().substring(formula.getFormula().length()-1).contains("x") ||
                formula.getFormula().substring(formula.getFormula().length()-1).contains("÷");
    }

    //підрахунок фінального результату всіх введених чисел та операторів
     double getNewNumber() {
        double newNumber;
        switch (operator){
            case "+": newNumber = a + getNumberDouble(); break;
            case "−": newNumber = a - getNumberDouble(); break;
            case "x": newNumber = a * getNumberDouble(); break;
            case "÷": newNumber = a / getNumberDouble(); break;
            default: throw new IllegalStateException("Unexpected value: " + operator); }
        return newNumber;
    }

}
