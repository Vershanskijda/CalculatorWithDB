package com.mycompany.my_calculator;

/**
 *
 * @author Дмитрий
 */
public class Model {
    public double calculation(double a, double b, String operator) {
        
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) return 0;
                return a / b;
                
                
        }
        System.out.println("Неизвестный оператор " + operator);
        
        return 0;
    }
    
    public double calculatePercents(double wholeValue, double percents) {
      return wholeValue / 100 * percents;
   }
}
