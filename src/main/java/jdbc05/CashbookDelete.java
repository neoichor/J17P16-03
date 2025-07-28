package jdbc05;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class CashbookDelete {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("家計簿データ削除（Qで終了）");

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

                        if (confirmDeletion(scanner)) {
                            cashbookDAO.deleteCashbook(conn, target.getCashbookId());
                            System.out.println(">> 正常に削除されました");
                        } else {
                            System.out.println(">> 削除をキャンセルしました");
                        }
                        conn.commit();

                    } catch (SQLException e) {
                        conn.rollback();
                        ConsoleUtils.handleDatabaseError(e);
                    }
                } catch (SQLException e) {
                    ConsoleUtils.handleDatabaseError(e);
                    break;
                }
                System.out.println();
            }
        }
        System.out.println("\nプログラムを終了します");
    }

    private static CashbookDTO selectCashbook(Scanner scanner, Connection conn, CashbookDAO cashbookDAO) throws SQLException {
        while (true) {
            System.out.print("\n削除するcashbook_idを入力（Qで終了）: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Q")) return null;
            if (StringUtils.isNullOrBlank(input)) continue;

            try {
                int cashbookId = Integer.parseInt(input.trim());
                CashbookDTO target = cashbookDAO.getCashbookById(conn, cashbookId);
                if (target == null) {
                    System.out.println("!! エラー: 指定されたIDは存在しません");
                    continue;
                }
                return target;
            } catch (NumberFormatException e) {
                System.out.println("!! エラー: 数値でIDを入力してください");
            }
        }
    }

    private static boolean confirmDeletion(Scanner scanner) {
        System.out.print("\n本当に削除しますか？ [Y/N]: ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        return "Y".equals(confirm);
    }
}