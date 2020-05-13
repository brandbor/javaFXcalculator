package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class CalculatorController implements Display, Formula {
    private boolean operatorIsEmpty;
    private String oldOperator;

    @FXML
    private TextField display;

    @FXML
    private Label formula;

    public CalculatorController() {
        calculator = new Calculator(this, this);
    }

    private Calculator calculator;

    public void digitClic(ActionEvent event) {
        Button button = (Button) event.getSource();
        String digit = button.getText();
        calculator.digit(digit);
    }

    public void operatorClic(ActionEvent event) {
        Button button = (Button) event.getSource();
        String operator = button.getText();
        try {
            if (operatorClicked()) {calculator.operatorChange(operator); this.oldOperator = operator;}
            else if (resultClicked()) {//нічого не робити
                System.out.println("do nothing");}
            else {
                if (!operatorIsEmpty) {
                    calculator.operator(operator);
                    operatorIsEmpty = true;
                    this.oldOperator = operator;}
                else {
                    calculator.operatorOldNew(operator, oldOperator);
                    operatorIsEmpty = true;
                    this.oldOperator = operator;}
            }
        } catch (Exception e) {//нічого не робити
            System.out.println("do nothing");
        }
    }


    public void backspaceClic() {
        calculator.backspace();
    }

    public void cancelClic() {calculator.cancel(); operatorIsEmpty=false;}

    public void commaClic() {
        if(operatorClicked() || resultClicked()) System.out.println("do nothing");
        else calculator.comma();
    }

    public void plusMinusClic() {
        if(operatorClicked() || resultClicked()) System.out.println("do nothing");
        else calculator.plusMinus();
    }

    public void sqrtClic() {
        if(operatorClicked()) System.out.println("do nothing");
        else calculator.sqrt();}

    public void fractionOneClic() {
        if(operatorClicked()) System.out.println("do nothing");
        else calculator.fractionOne();}

    public void multiplyTwoClic() {
        if(operatorClicked()) System.out.println("do nothing");
        else calculator.multiplyTwo();}

    public void percentClic() {
        calculator.percent();
    }

    public void resultClic() {
        if(operatorClicked() || resultClicked()){System.out.println("do nothing");}
        else {calculator.result(); operatorIsEmpty=false;}
    }

    @Override
    public String getDisplayNumber() {return display.getText();}

    @Override
    public void setDisplayNumber(String displayNumber) {display.setText(displayNumber);}


    @Override
    public String getFormula() {return formula.getText();}

    @Override
    public void setFormula(String formulaLabel) {formula.setText(formulaLabel);}

    // Блок методів для перевірки натискання клавіш
    private boolean operatorClicked() {
        return getFormula().substring(getFormula().length()-1).contains("+") ||
                getFormula().substring(getFormula().length()-1).contains("−") ||
                getFormula().substring(getFormula().length()-1).contains("x") ||
                getFormula().substring(getFormula().length()-1).contains("÷");
    }

    private boolean resultClicked() {
        return getFormula().substring(getFormula().length()-1).contains("=");
    }


}
