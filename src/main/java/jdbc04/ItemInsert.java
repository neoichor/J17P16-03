package jdbc04;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * 【品目追加処理】(リファクタリング版)
 * 入力チェックをStringUtilsに、エラー表示をConsoleUtilsに委譲。
 */
public class ItemInsert {

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("品目追加処理（Qで終了）");

			while (true) {
				System.out.print("品名を入力: ");
				String input = scanner.nextLine();

				if (input.equalsIgnoreCase("Q")) {
					break;
				}
				
				if (StringUtils.isNullOrBlank(input) || input.trim().length() > 20) {
					System.out.println("エラー: 品名は1～20文字で入力してください。");
					continue;
				}
				String itemName = input.trim();

				try (Connection conn = DatabaseConnection.getConnection()) {
					conn.setAutoCommit(false);
					try {
						ItemDAO itemDAO = new ItemDAO();
						ItemDTO newItem = new ItemDTO();
						newItem.setItemName(itemName);

						itemDAO.insertItem(conn, newItem);
						conn.commit();
						System.out.println(">> 品目を正常に追加しました。");

					} catch (SQLException e) {
						conn.rollback();
						System.err.println("!! データ登録に失敗したため、ロールバックしました。");
						ConsoleUtils.handleDatabaseError(e);
					}
				} catch (SQLException e) {
					System.err.println("!! データベース接続に失敗しました。");
					ConsoleUtils.handleDatabaseError(e);
				}
				System.out.println("--------------------");
			}
			System.out.println("プログラムを終了します。");
		}
	}
}