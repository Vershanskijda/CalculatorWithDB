package com.mycompany.my_calculator;

import java.sql.Connection;
import java.sql.DriverManager;


public class Database {
    final String user = "root";
    final String password = "";
    final String url = "jdbc:mysql://localhost:3306/history"
            + "?zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    
    public Connection connection = null;

    public Connection getConnectionDB() {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            return connection;

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("База данных не найдена!");
        }
            return connection;
    }
}
