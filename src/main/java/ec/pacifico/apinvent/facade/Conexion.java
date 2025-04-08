/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.pacifico.apinvent.facade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.TimeZone;

/**
 *
 * @author Carolina
 */
public class Conexion {
    private Connection connection;
    private String servidor = "jdbc:sqlserver://localhost:1433;databaseName=apinven;integratedSecurity=true";  //BD SERVIDOR BDP
    //private String servidor = "jdbc:sqlserver://127.0.0.1;encrypt=false;trustServerCertificate=true;databaseName=apinven;user=gustavoc;password=Tetian02";  //BD LOCAL 
    public Conexion() {
        this.createConnection();
    }
    
    private void createConnection() {
        try {
            TimeZone timeZone = TimeZone.getTimeZone("America/Bogota");
            TimeZone.setDefault(timeZone);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = null;
            this.connection = DriverManager.getConnection(servidor);
            //this.connection = DriverManager.getConnection(servidor, username, password);
            //System.out.println("CONEXION ESTABLECIDA CON EL SERVIDOR!!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error CreateConnection(): " + e.getMessage());
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("closeConnection: " + e.getMessage());
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
}
