package jdbc05;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 【品目一覧表示】(リファクタリング版)
 * 表示処理をConsoleUtilsに委譲。
 */
public class ItemList {

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // 読み取り専用処理だが、作法としてトランザクションを管理
            conn.setAutoCommit(false);

            try {
                ItemDAO itemDAO = new ItemDAO();
                List<ItemDTO> items = itemDAO.getAllItems(conn);

                // ConsoleUtilsの共通メソッドを使用して一覧を表示
                ConsoleUtils.printItemList(items);

                conn.commit();
            } catch (SQLException e) {
                // selectでもエラーは起こりうるのでrollback
                conn.rollback();
                ConsoleUtils.handleDatabaseError(e);
            }
        } catch (SQLException e) {
            // Connection取得やcommit/rollback自体のエラー
            ConsoleUtils.handleDatabaseError(e);
        }
    }
}