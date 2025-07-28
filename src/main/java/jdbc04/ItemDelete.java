package jdbc04;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * 【品目削除処理】(リファクタリング版)
 * 表示やエラー処理をユーティリティクラスに委譲。
 */
public class ItemDelete {

    private static final String FOREIGN_KEY_VIOLATION = "23503";
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("品目削除処理（Qで終了）");

			while (true) {
				try (Connection conn = DatabaseConnection.getConnection()) {
                    conn.setAutoCommit(false);
					try {
						ItemDAO itemDAO = new ItemDAO();

						List<ItemDTO> items = itemDAO.getAllItems(conn);
                        if (items.isEmpty()) {
                            System.out.println("削除できる品目がありません。");
                            break;
                        }
						ConsoleUtils.printItemList(items);

                        ItemDTO targetItem = selectItemForDeletion(scanner, conn, itemDAO);
                        if (targetItem == null) { // Qが押されたか、エラーがあった
                            break;
                        }

                        if (!confirmDeletion(scanner, targetItem)) {
                            continue;
                        }
						
                        executeDeletion(conn, itemDAO, targetItem.getItemId());
						conn.commit();

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

    private static ItemDTO selectItemForDeletion(Scanner scanner, Connection conn, ItemDAO itemDAO) throws SQLException {
        while (true) {
            System.out.print("削除する品目のIDを入力（Qで終了）: ");
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

    private static boolean confirmDeletion(Scanner scanner, ItemDTO targetItem) {
        System.out.printf(">> 削除対象: %d - %s%n", targetItem.getItemId(), targetItem.getItemName());
        System.out.print(">> 本当に削除しますか？ [Y/N]: ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (confirm.equals("Y")) {
            return true;
        }
        System.out.println(">> 削除をキャンセルしました\n");
        return false;
    }

    private static void executeDeletion(Connection conn, ItemDAO itemDAO, int itemId) throws SQLException {
        try {
            itemDAO.deleteItem(conn, itemId);
            System.out.println(">> 正常に削除されました\n");
        } catch (SQLException e) {
            if (FOREIGN_KEY_VIOLATION.equals(e.getSQLState())) {
                System.out.println("!! エラー: 家計簿データで使用されているため削除できません");
            } else {
                throw e;
            }
        }
    }
}
