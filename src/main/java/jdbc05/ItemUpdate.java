package jdbc05;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * 【品目修正処理】(リファクタリング版)
 * 表示やエラー処理をユーティリティクラスに委譲。
 */
public class ItemUpdate {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("品目修正処理（Qで終了）");

            while (true) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    conn.setAutoCommit(false);
                    try {
                        ItemDAO itemDAO = new ItemDAO();

                        List<ItemDTO> items = itemDAO.getAllItems(conn);
                        if (items.isEmpty()) {
                            System.out.println("修正できる品目がありません。");
                            break;
                        }
                        ConsoleUtils.printItemList(items);

                        ItemDTO targetItem = selectItemById(scanner, conn, itemDAO);
                        if (targetItem == null) { // Qが押されたか、エラーがあった
                            if (scanner.hasNextLine()) { // Qが押された場合
                                break;
                            }
                            continue; // ID入力エラーの場合
                        }

                        String newName = inputNewItemName(scanner, targetItem.getItemName());
                        if (newName == null) { // Qが押された
                            System.out.println(">> 修正をキャンセルしました\n");
                            continue;
                        }

                        targetItem.setItemName(newName);
                        itemDAO.updateItem(conn, targetItem);

                        conn.commit();
                        System.out.println(">> 品目を更新しました\n");

                    } catch (SQLException e) {
                        conn.rollback();
                        ConsoleUtils.handleDatabaseError(e);
                    }
                } catch (SQLException e) {
                    ConsoleUtils.handleDatabaseError(e);
                }
                System.out.println("--------------------");
            }
        }
        System.out.println("プログラムを終了します。");
    }

    private static ItemDTO selectItemById(Scanner scanner, Connection conn, ItemDAO itemDAO) throws SQLException {
        while (true) {
            System.out.print("修正する品目のIDを入力（Qで終了）: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Q")) {
                return null;
            }
            if (StringUtils.isNullOrBlank(input)) {
                System.out.println("!! エラー: IDが入力されていません。");
                continue;
            }
            try {
                int itemId = Integer.parseInt(input.trim());
                ItemDTO item = itemDAO.getItemById(conn, itemId);
                if (item == null) {
                    System.out.printf("!! エラー: ID %d は存在しません\n\n", itemId);
                    continue;
                }
                return item;
            } catch (NumberFormatException e) {
                System.out.println("!! エラー: 半角数字でIDを入力してください");
            }
        }
    }

    private static String inputNewItemName(Scanner scanner, String currentName) {
        while (true) {
            System.out.printf("新しい品目名を入力（現: %s／Qで終了）: ", currentName);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Q")) {
                return null;
            }
            if (StringUtils.isNullOrBlank(input)) {
                System.out.println("!! エラー: 品目名が入力されていません");
                continue;
            }
            String trimmedInput = input.trim();
            if (trimmedInput.length() > 20) {
                System.out.println("!! エラー: 品目名は20文字以内で入力してください");
                continue;
            }
            return trimmedInput;
        }
    }
}
