package jdbc03;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        // try-with-resourcesで、処理終了時に必ずConnectionがcloseされるようにする
        try (Connection conn = DatabaseConnection.getConnection()) {
            // トランザクション処理の開始
            try {
                // 1. オートコミットを無効にする
                conn.setAutoCommit(false);

                CashbookDAO cashbookDAO = new CashbookDAO();

                System.out.println("----- トランザクション開始前 -----");
                List<CashbookDTO> cashbooksBefore = cashbookDAO.getAllCashbooks(conn);
                for (CashbookDTO cashbook : cashbooksBefore) {
                    System.out.println(cashbook);
                }

                // --- 一連の処理を開始 ---

                // 2. 追加 (Create)
                System.out.println("\n----- 1. 新しいデータを追加します -----");
                CashbookDTO newCashbook = new CashbookDTO();
                newCashbook.setActOn(Date.valueOf("2025-07-26"));
                newCashbook.setItemId(4); // 交際費
                newCashbook.setNote("新人歓迎会");
                newCashbook.setCashIn(0);
                newCashbook.setCashOut(5000);
                int newId = cashbookDAO.insertCashbook(conn, newCashbook);
                System.out.println("追加完了 (ID: " + newId + ")");

                // 3. 更新 (Update)
                System.out.println("\n----- 2. データを更新します (ID: " + newId + ") -----");
                CashbookDTO target = cashbookDAO.findById(conn, newId);
                target.setNote(target.getNote() + "（一次会）");
                cashbookDAO.updateCashbook(conn, target);
                System.out.println("更新完了");

                // 4. 削除 (Delete)
                System.out.println("\n----- 3. データを削除します (ID: 1) -----");
                cashbookDAO.deleteCashbook(conn, 1);
                System.out.println("削除完了");

                // --- 一連の処理がすべて成功 ---

                // 5. コミット
                conn.commit();
                System.out.println("\n----- トランザクションをコミットしました -----");

            } catch (SQLException e) {
                // --- 処理中にエラーが発生した場合 ---
                System.err.println("\n----- エラーが発生したため、トランザクションをロールバックします -----");
                e.printStackTrace();
                try {
                    // 6. ロールバック
                    conn.rollback();
                } catch (SQLException e2) {
                    System.err.println("ロールバックに失敗しました。");
                    e2.printStackTrace();
                }
            }

            // --- 最終結果の確認 ---
            System.out.println("\n----- 最終的なデータ -----");
            // 結果確認のために、新しいトランザクションでデータを再取得する
            // (connは既に閉じてる可能性があるので、新しい接続を取得する)
            try (Connection finalConn = DatabaseConnection.getConnection()) {
                CashbookDAO finalDao = new CashbookDAO();
                List<CashbookDTO> cashbooksAfter = finalDao.getAllCashbooks(finalConn);
                for (CashbookDTO cashbook : cashbooksAfter) {
                    System.out.println(cashbook);
                }
            }

        } catch (SQLException e) {
            System.err.println("データベース接続または最終確認処理でエラーが発生しました。");
            e.printStackTrace();
        }
    }
}
