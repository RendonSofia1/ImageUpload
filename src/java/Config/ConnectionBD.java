/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author rendo
 */
public class ConnectionBD {

    Connection conection;

    public ConnectionBD() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conection = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "");
        } catch (Exception e) {
            System.err.println("El error está en la conexión: " + e);
        }
    }

    public Connection getConnectionBD() {
        return conection;
    }
}
