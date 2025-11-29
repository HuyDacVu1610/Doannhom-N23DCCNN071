/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectdb;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Admin
 */
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
class BaseDAO {

    // Cấu hình MySQL
    private static String hostName = "localhost";
    private static String port = "3306"; // MySQL mặc định là 3306
    private static String database = "QLSV"; // Tên database của bạn
    private static String username = "root";
    private static String password = "123456"; // <-- ĐIỀN PASS CỦA BẠN VÀO ĐÂY

    public Connection getConnection() {
        Connection conn = null;
        try {
            // Nạp Driver MySQL (Khác với jtds của SQL Server)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Chuỗi kết nối MySQL
            String connectionURL = "jdbc:mysql://" + hostName + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true";

            conn = DriverManager.getConnection(connectionURL, username, password);
            System.out.println("Connected to MySQL!");
        } catch (ClassNotFoundException e) {
            System.out.println("Thiếu thư viện mysql-connector-j!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối MySQL!");
            e.printStackTrace();
        }
        return conn;
    }

    public void closeConnection(Connection conn, PreparedStatement ps,
            ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
