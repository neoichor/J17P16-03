package jdbc02_2;

import java.sql.SQLException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        try {
            ItemDAO itemDAO = new ItemDAO();

            System.out.println("----- CRUD操作前 -----");
            List<ItemDTO> itemsBefore = itemDAO.getAllItems();
            for (ItemDTO item : itemsBefore) {
                System.out.println(item);
            }

            // --- 1. 追加 (Create) & 確認 (Read) ---
            System.out.println("\n----- 1. 新しい項目を追加します -----");
            ItemDTO newItem = new ItemDTO();
            newItem.setItemName("新しい費目");
            
            int newId = itemDAO.insertItem(newItem);
            System.out.println("追加処理が完了しました。 (新しいID: " + newId + ")");

            System.out.println("▼追加されたレコード内容");
            ItemDTO insertedItem = itemDAO.findById(newId);
            System.out.println(insertedItem);

            // --- 2. 更新 (Update) ---
            System.out.println("\n----- 2. 追加した項目を更新します (ID: " + newId + ") -----");
            ItemDTO updateTarget = itemDAO.findById(newId);
            if (updateTarget != null) {
                System.out.println("更新対象データ: " + updateTarget);
                updateTarget.setItemName("更新された費目");
                itemDAO.updateItem(updateTarget);
                System.out.println("更新完了。");
            } else {
                System.out.println("ID:" + newId + " のデータは見つかりませんでした。");
            }
            
            System.out.println("▼更新されたレコード内容");
            ItemDTO updatedItem = itemDAO.findById(newId);
            System.out.println(updatedItem);

            // --- 3. 削除 (Delete) ---
            System.out.println("\n----- 3. 更新した項目を削除します (ID: " + newId + ") -----");
            ItemDTO deleteTarget = itemDAO.findById(newId);
            if (deleteTarget != null) {
                System.out.println("削除対象データ: " + deleteTarget);
                itemDAO.deleteItem(deleteTarget.getItemId());
                System.out.println("削除完了。");
            } else {
                System.out.println("ID:" + newId + " のデータは見つかりませんでした。");
            }

            System.out.println("\n----- CRUD操作後（追加したデータが削除され、元の状態に戻っている） -----");
            List<ItemDTO> itemsAfter = itemDAO.getAllItems();
            for (ItemDTO item : itemsAfter) {
                System.out.println(item);
            }

        } catch (SQLException e) {
            System.err.println("データベース操作中にエラーが発生しました。");
            e.printStackTrace();
        }
    }
}
