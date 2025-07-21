package jdbc01;

import java.sql.Connection;
import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("PostgreSQLへの接続に成功！！");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("PostgreSQLへの接続に失敗！！");
        }
    }
}