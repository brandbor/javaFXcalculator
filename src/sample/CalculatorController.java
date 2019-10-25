package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CalculatorController {

    @FXML
    private TextField display;
    private boolean lastButtonWasDigit;


    public void backspaceClic(ActionEvent event) {
        System.out.println("Backspace clicked");
    }

    public void cancelClic(ActionEvent event) {
        setDisplayNumber("0");
        lastButtonWasDigit = false;
        System.out.println(lastButtonWasDigit);
    }

    public void commaClic(ActionEvent event) {
        if(!getDisplayNumber().contains(",")){
            setDisplayNumber(getDisplayNumber()+",");
            lastButtonWasDigit = true;
        }
    }

    public void plusMinusClic(ActionEvent event) {
        if(!getDisplayNumber().contains("-")){
            setDisplayNumber("-"+getDisplayNumber());
        }else if (getDisplayNumber().contains("-")){
            System.out.println(getDisplayNumber());
            setDisplayNumber(getDisplayNumber().replace("-",""));
        }
    }

    public void digitClic(ActionEvent event) {
        Button button = (Button) event.getSource();
        String digit = button.getText();
        System.out.println("Digit clicked"+ digit);

        if (lastButtonWasDigit){
            setDisplayNumber(getDisplayNumber()+digit);
        }else {setDisplayNumber(digit);}
        lastButtonWasDigit = true;
        System.out.println("display number = "+getDisplayNumber());
    }

    public void sqrtClic(ActionEvent event) {
        if (getDisplayNumber().contains(",")) {
            double newNumber = Math.sqrt(getNumberDouble());
            setNumberDouble(newNumber);
        } else if (!getDisplayNumber().contains(",")){
            if (Math.sqrt(getNumberDouble())%1==0){
                int newNumber = (int) Math.sqrt(getNumberDouble());
                setNumberInt(newNumber);
            } else if (Math.sqrt(getNumberDouble())%1!=0){
                double newNumber = Math.sqrt(getNumberDouble());
                setNumberDouble(newNumber);
            }
        }
    }

    public String getDisplayNumber() {
        return display.getText();
    }

    public void setDisplayNumber(String displayNumber) {
        display.setText(displayNumber);
    }

//getter and setter for double numbers
    public double getNumberDouble(){
        return Double.parseDouble(getDisplayNumber());
    }

    public void setNumberDouble(double number){
        setDisplayNumber(String.valueOf(number));
    }

//getter and setter for int number
    public int getNumberInt(){
        return Integer.parseInt(getDisplayNumber());
    }

    public void setNumberInt(int number){
        setDisplayNumber(String.valueOf(number));
    }



}
