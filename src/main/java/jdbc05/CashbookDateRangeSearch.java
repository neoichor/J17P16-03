package jdbc05;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

/**
 * 【日付範囲による家計簿データ検索】
 */
public class CashbookDateRangeSearch {

	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.println("\n日付範囲検索（終了するにはQを入力）");

				try {
					System.out.print("開始日（YYYY-MM-DD）: ");
					String startInput = scanner.nextLine();
					if (startInput.equalsIgnoreCase("Q"))
						break;

					System.out.print("終了日（YYYY-MM-DD）: ");
					String endInput = scanner.nextLine();
					if (endInput.equalsIgnoreCase("Q"))
						break;

					if (StringUtils.isNullOrBlank(startInput) || StringUtils.isNullOrBlank(endInput)) {
						System.out.println("エラー: 日付が入力されていません。");
						continue;
					}

					Date startDate = new Date(dateFormat.parse(startInput.trim()).getTime());
					Date endDate = new Date(dateFormat.parse(endInput.trim()).getTime());

					if (endDate.before(startDate)) {
						System.out.println("エラー: 日付の範囲指定が間違っています");
						continue;
					}

					try (Connection conn = DatabaseConnection.getConnection()) {
						conn.setAutoCommit(false);
						try {
							CashbookDAO cashbookDAO = new CashbookDAO();
							List<CashbookDTO> results = cashbookDAO.getCashbooksByDateRange(conn, startDate, endDate);
							ConsoleUtils.printCashbookList(results);
							conn.commit();
						} catch (SQLException e) {
							conn.rollback();
							throw e;
						}
					}

				} catch (ParseException e) {
					System.out.println("エラー: 不正な日付形式です。（例: 2025-07-28）");
				} catch (SQLException e) {
					ConsoleUtils.handleDatabaseError(e);
					break;
				}
			}
			System.out.println("プログラムを終了します");
		}
	}
}
