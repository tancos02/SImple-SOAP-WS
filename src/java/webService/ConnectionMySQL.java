/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webService;
import java.sql.*;

/**
 *
 * @author paulus
 */
public class ConnectionMySQL {
    Connection conn = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    public Connection getConnect() {
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String JDBC_URL = "jdbc:mysql://localhost:3306/bank";
        String USER = "root";
        String PASS = "";
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(JDBC_URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Gagal Koneksi" + e.toString());
        }
        return conn;
    }
}
