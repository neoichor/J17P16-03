package jdbc02;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class MainApp {
	public static void main(String[] args) {
		try {
			CashbookDAO cashbookDAO = new CashbookDAO();

			// 全データを取得して表示
			System.out.println("----- CRUD操作前 -----");
			List<CashbookDTO> cashbooks = cashbookDAO.getAllCashbooks();
			for (CashbookDTO cashbook : cashbooks) {
				System.out.println(cashbook.getCashbookId() + ", "
						+ cashbook.getActOn() + ", "
						+ cashbook.getItemId() + ", "
						+ cashbook.getNote() + ", "
						+ cashbook.getCashIn() + ", "
						+ cashbook.getCashOut());
			}

			// 新しいデータを挿入
			CashbookDTO newCashbook = new CashbookDTO();
			newCashbook.setActOn(Date.valueOf("2024-02-20"));
			newCashbook.setItemId(1);
			newCashbook.setNote("新しいメモを追加");
			newCashbook.setCashIn(0);
			newCashbook.setCashOut(1000);
			cashbookDAO.insertCashbook(newCashbook);

			// データを更新（２番目のレコードを更新）
			if (!cashbooks.isEmpty()) {
				CashbookDTO updateCashbook = cashbooks.get(1); // 2レコード目を取得
				updateCashbook.setNote("2行目のメモを更新");
				cashbookDAO.updateCashbook(updateCashbook);
			} else {
				System.out.println("更新するデータがありません。");
			}

			// データを削除（三番目のレコードを削除）
			if (cashbooks.size() > 1) {
				CashbookDTO deleteCashbook = cashbooks.get(2); // 3番レコード目を取得
				cashbookDAO.deleteCashbook(deleteCashbook.getCashbookId());
			} else {
				System.out.println("削除するデータがありません。");
			}

			System.out.println("----- CRUD操作後 -----");
			System.out.print("①新しいメモを追加");
			System.out.print("②2行目のメモを更新");
			System.out.println("③3行目を削除");
			cashbooks = cashbookDAO.getAllCashbooks();
			for (CashbookDTO cashbook : cashbooks) {
				System.out.println(cashbook.getCashbookId() + ", "
						+ cashbook.getActOn() + ", "
						+ cashbook.getItemId() + ", "
						+ cashbook.getNote() + ", "
						+ cashbook.getCashIn() + ", "
						+ cashbook.getCashOut());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}