package jdbc05;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class CashbookUpdate {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("家計簿データ修正処理（Qで終了）");

            while (true) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    conn.setAutoCommit(false);
                    try {
                        CashbookDAO cashbookDAO = new CashbookDAO();
                        ItemDAO itemDAO = new ItemDAO();

                        CashbookDTO target = selectCashbook(scanner, conn, cashbookDAO);
                        if (target == null) break;

                        String itemName = itemDAO.getItemById(conn, target.getItemId()).getItemName();
                        ConsoleUtils.printCashbookDetails(target, itemName);

                        System.out.println("\n新しい情報を入力してください（変更しない場合はEnter）:");
                        updateCashbookDTO(scanner, target);

                        cashbookDAO.updateCashbook(conn, target);
                        conn.commit();
                        System.out.println(">> 家計簿データを更新しました。");

                    } catch (SQLException e) {
                        conn.rollback();
                        ConsoleUtils.handleDatabaseError(e);
                    } catch (NumberFormatException e) {
                        System.out.println("!! 数値項目には数字を入力してください。");
                    } catch (IllegalArgumentException e) {
                        System.out.println("!! 日付はyyyy-mm-dd形式で入力してください。");
                    }
                } catch (SQLException e) {
                    ConsoleUtils.handleDatabaseError(e);
                    break;
                }
                System.out.println();
            }
            System.out.println("プログラムを終了します");
        }
    }

    private static CashbookDTO selectCashbook(Scanner scanner, Connection conn, CashbookDAO cashbookDAO) throws SQLException {
        while (true) {
            System.out.print("\n修正対象のcashbook_idを入力してください（Qで終了）: ");
            String idStr = scanner.nextLine();
            if (idStr.equalsIgnoreCase("Q")) return null;
            if (StringUtils.isNullOrBlank(idStr)) continue;

            int cashbookId = Integer.parseInt(idStr.trim());
            CashbookDTO cashbook = cashbookDAO.getCashbookById(conn, cashbookId);
            if (cashbook == null) {
                System.out.println("!! 指定されたIDは存在しません。");
                continue;
            }
            return cashbook;
        }
    }

    private static void updateCashbookDTO(Scanner scanner, CashbookDTO cashbook) {
        System.out.printf("日付（現: %s）: ", cashbook.getActOn());
        String dateInput = scanner.nextLine();
        if (!StringUtils.isNullOrBlank(dateInput)) {
            cashbook.setActOn(Date.valueOf(dateInput.trim()));
        }

        System.out.printf("メモ（現: %s）: ", cashbook.getNote());
        String noteInput = scanner.nextLine();
        if (!StringUtils.isNullOrBlank(noteInput)) {
            cashbook.setNote(noteInput.trim());
        }

        System.out.printf("入金額（現: %,d）: ", cashbook.getCashIn());
        String cashInInput = scanner.nextLine();
        if (!StringUtils.isNullOrBlank(cashInInput)) {
            cashbook.setCashIn(Integer.parseInt(cashInInput.trim()));
        }

        System.out.printf("出金額（現: %,d）: ", cashbook.getCashOut());
        String cashOutInput = scanner.nextLine();
        if (!StringUtils.isNullOrBlank(cashOutInput)) {
            cashbook.setCashOut(Integer.parseInt(cashOutInput.trim()));
        }
    }
}
