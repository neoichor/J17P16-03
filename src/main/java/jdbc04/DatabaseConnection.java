package jdbc04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 【DB接続ユーティリティクラス】
 * DBへの接続処理（URL・ユーザ名・パスワードの管理）を集約
 * 各DAOから「DatabaseConnection.getConnection()」で接続を取得する
 */
public class DatabaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        // 環境変数からデータベース接続情報を取得します。
        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String database = System.getenv("DB_NAME");
        USER = System.getenv("DB_USER");
        PASSWORD = System.getenv("DB_PASSWORD");

        // 取得した情報を使って、JDBC URLを構築します。
        URL = "jdbc:postgresql://" + host + ":" + port + "/" + database;
    }

    /**
     * プライベートコンストラクタ。
     * ユーティリティクラスはインスタンス化させないための作法です。
     */
    private DatabaseConnection() {
    }

    /**
     * データベースへの接続(Connection)を取得します。
     * 接続情報はクラスロード時に静的初期化ブロックで設定されます。
     * 
     * @return DB接続(Connection)
     * @throws SQLException 接続失敗時
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}