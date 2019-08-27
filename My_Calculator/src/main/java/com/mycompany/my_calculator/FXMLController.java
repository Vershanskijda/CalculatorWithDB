package com.mycompany.my_calculator;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class FXMLController implements Initializable{
    
    @FXML
    private Text output;
    private double num1 = 0;
    private double num2 = 0;
    
    private boolean start = true;
    
    private String operator = "";
    private Model model = new Model();
    
    String memory = "0";
    String memoryForDB = "";
    
    private DecimalFormat decimalFormat = new DecimalFormat("#.#######################################################");
    
    
    @FXML
    //Ввод чисел
    private void processNum(ActionEvent event) {
        if (start) {
            output.setText("");
            start = false;
        }
        String value = ((Button)event.getSource()).getText();
        output.setText(output.getText()+value);
        memoryForDB = memoryForDB + value;
        System.out.println(memoryForDB);
    }

    @FXML
    //Ввод операторов
    private void processOperator(ActionEvent event) {
        String value = ((Button)event.getSource()).getText();
        memoryForDB = memoryForDB + " " + value + " ";
        System.out.println(memoryForDB);
        if (!"=".equals(value)) {
            if (!operator.isEmpty()) return;
            operator = value;
            num1 = Double.parseDouble(output.getText());
            output.setText("");
        }
        else {
            if (operator.isEmpty()) return;
                System.out.println("Выполнение метода calculation");
                output.setText(String.valueOf(model.calculation(num1, Double.parseDouble(output.getText()), operator)));
                memoryForDB = memoryForDB + output.getText();
                System.out.println(memoryForDB);
                if (value.equals("=")) {
                    insertResultIntoDB(memoryForDB);
                    System.out.println("insertResultIntoDB -> row 68");
                    memoryForDB = "";
                }
            operator = "";
            start = true;
        }
    }
    
    @FXML
    //Удаление числа
    private void buttonBackspace() {
        String value = output.getText();
        if (value.length() > 0) {
            output.setText(value.substring(0, value.length() - 1));
            memoryForDB = memoryForDB.substring(0, value.length() - 1);
            System.out.println(memoryForDB);
        }
    }
    
    @FXML
    // Точка
    private void buttonDot() {
        String value = output.getText();
        if (!value.contains(".") && value.length() > 1) {
            output.setText(value+".");
            memoryForDB = memoryForDB + ".";
        }
    }
    
    @FXML
    // +/-
    private void buttonPlusMinus() {
        double ops = Double.parseDouble(String.valueOf(output.getText()));
        ops = ops * (-1);
        output.setText(String.valueOf(ops));
    }
    
    @FXML
    //Возведение в корень
    private void buttonSquareRoot() {
        double ops = Double.parseDouble(String.valueOf(output.getText()));
        ops = Math.sqrt(ops);
        output.setText(String.valueOf(ops));
    }

    @FXML
    //Проценты
    private void buttonPercent() {
        double temp = Double.parseDouble(output.getText().replace(",", "."));
        num2 = model.calculatePercents(num1, temp);
        output.setText(String.valueOf(num2));
    }
    
    @FXML
    //Сброс последнего введённого числа
    private void buttonCE() {
        memoryForDB = memoryForDB.replace(output.getText(), "");
        output.setText("");
        System.out.println(memoryForDB);
    }
    
    @FXML
    //Полная отмена операции
    private void buttonC() {
        output.setText("");
        memoryForDB = "";
        num1 = 0;
        num2 = 0;
        operator = "";
    }
    
    @FXML
    //Memory Clear
    private void buttonMC() {
        memory = "0";
        System.out.println(memory);
    }

    @FXML
    //Memory Read
    private void buttonMR() {
        output.setText(memory);
        System.out.println(memory);
    }

    @FXML
    //Memory Minus
    private void buttonM_minus() {
        double m = Double.parseDouble(memory);
        double n = Double.parseDouble(output.getText());
        memory = decimalFormat.format(m - n);
        System.out.println(memory);
    }

    @FXML
    //Memory Plus
    private void buttonM_plus() {
        double m = Double.parseDouble(memory);
        double n = Double.parseDouble(output.getText());
        memory = decimalFormat.format(m + n);
        System.out.println(memory);
    }
    
    ObservableList list = FXCollections.observableArrayList();
    
    @FXML
    private ListView<String> listView;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showResultFromDB();
    }
    
    private void showResultFromDB() {
        try {
            Database database = new Database();
            Connection connection = database.getConnectionDB();

            String SELECT = "SELECT * FROM history_sum ORDER BY id DESC LIMIT 10;";
        
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(SELECT);
            list.removeAll();
            while (rs.next()) {
                list.add(rs.getString(2));
            }
                listView.getItems().addAll(list);
        } catch (Exception e) {
            System.out.println("База данных не найдена!");
        }
    }
    
    private void insertResultIntoDB(String row) {
        try {
            Database database = new Database();
            Connection connection = database.getConnectionDB();
            String INSERT = "INSERT INTO history_sum (history_row) VALUE ('" + row + "');";
            
            Statement statement = connection.createStatement();
            statement.executeUpdate(INSERT);
            System.out.println("INSERT " + output.getText());
            
        } catch (Exception e) {
            System.out.println("База данных не найдена!");
        }        
    }
}

    
    
   

