package jdbc02;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        try {
            CashbookDAO cashbookDAO = new CashbookDAO();

            System.out.println("----- CRUD操作前 -----");
            List<CashbookDTO> cashbooksBefore = cashbookDAO.getAllCashbooks();
            for (CashbookDTO cashbook : cashbooksBefore) {
                System.out.println(cashbook);
            }

            // --- 1. 追加 (Create) & 確認 (Read) ---
            System.out.println("\n----- 1. 新しいデータを追加します -----");
            CashbookDTO newCashbook = new CashbookDTO();
            newCashbook.setActOn(Date.valueOf("2025-07-26"));
            newCashbook.setItemId(3); // 教養娯楽費
            newCashbook.setNote("JDBCの学習書");
            newCashbook.setCashIn(0);
            newCashbook.setCashOut(3500);
            
            int newId = cashbookDAO.insertCashbook(newCashbook);
            System.out.println("追加処理が完了しました。 (新しいID: " + newId + ")");

            System.out.println("▼追加されたレコード内容");
            CashbookDTO insertedCashbook = cashbookDAO.findById(newId);
            System.out.println(insertedCashbook);

            // --- 2. 更新 (Update) ---
            System.out.println("\n----- 2. 追加したデータを更新します (ID: " + newId + ") -----");
            CashbookDTO updateTarget = cashbookDAO.findById(newId);
            if (updateTarget != null) {
                System.out.println("更新対象データ: " + updateTarget);
                updateTarget.setNote("新しく追加した本のメモを更新！");
                cashbookDAO.updateCashbook(updateTarget);
                System.out.println("更新完了。");
            } else {
                System.out.println("ID:" + newId + " のデータは見つかりませんでした。");
            }

            // --- 3. 削除 (Delete) ---
            System.out.println("\n----- 3. 更新したデータを削除します (ID: " + newId + ") -----");
            CashbookDTO deleteTarget = cashbookDAO.findById(newId);
            if (deleteTarget != null) {
                System.out.println("削除対象データ: " + deleteTarget);
                cashbookDAO.deleteCashbook(deleteTarget.getCashbookId());
                System.out.println("削除完了。");
            } else {
                System.out.println("ID:" + newId + " のデータは見つかりませんでした。");
            }

            System.out.println("\n----- CRUD操作後（追加したデータが削除され、元の状態に戻っている） -----");
            List<CashbookDTO> cashbooksAfter = cashbookDAO.getAllCashbooks();
            for (CashbookDTO cashbook : cashbooksAfter) {
                System.out.println(cashbook);
            }

        } catch (SQLException e) {
            System.err.println("データベース操作中にエラーが発生しました。");
            e.printStackTrace();
        }
    }
}