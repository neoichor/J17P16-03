package jdbc05;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class CashbookInsert {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("家計簿データ追加（Qで終了）");

            while (true) {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    conn.setAutoCommit(false);
                    try {
                        ItemDAO itemDAO = new ItemDAO();
                        CashbookDAO cashbookDAO = new CashbookDAO();

                        ConsoleUtils.printItemList(itemDAO.getAllItems(conn));

                        Date actOn = inputDate(scanner, dateFormat);
                        if (actOn == null) break;

                        int itemId = inputItemId(scanner, conn, itemDAO);
                        if (itemId == -1) break;

                        String note = inputNote(scanner);
                        if (note == null) break;

                        int cashIn = inputAmount(scanner, "入金額");
                        if (cashIn == -1) break;

                        int cashOut = inputAmount(scanner, "出金額");
                        if (cashOut == -1) break;

                        if (cashIn == 0 && cashOut == 0) {
                            System.out.println("!! エラー: 入金と出金が両方0です");
                            continue;
                        }

                        CashbookDTO dto = new CashbookDTO();
                        dto.setActOn(actOn);
                        dto.setItemId(itemId);
                        dto.setNote(note);
                        dto.setCashIn(cashIn);
                        dto.setCashOut(cashOut);

                        cashbookDAO.insertCashbook(conn, dto);
                        conn.commit();
                        System.out.println(">> データを登録しました");

                    } catch (SQLException e) {
                        conn.rollback();
                        ConsoleUtils.handleDatabaseError(e);
                    } catch (ParseException e) {
                        System.out.println("!! エラー: 正しい日付形式で入力してください（例: 2025-07-28）");
                    } catch (NumberFormatException e) {
                        System.out.println("!! エラー: 数値のみ入力してください");
                    }
                } catch (SQLException e) {
                    ConsoleUtils.handleDatabaseError(e);
                    break;
                }
                if (!confirmContinue(scanner)) break;
            }
            System.out.println("プログラムを終了します");
        }
    }

    private static Date inputDate(Scanner scanner, SimpleDateFormat dateFormat) throws ParseException {
        System.out.print("日付を入力（形式: YYYY-MM-DD／Qで終了）: ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Q")) return null;
        return new Date(dateFormat.parse(input.trim()).getTime());
    }

    private static int inputItemId(Scanner scanner, Connection conn, ItemDAO itemDAO) throws SQLException {
        while (true) {
            System.out.print("費目IDを入力（Qで終了）: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Q")) return -1;
            int id = Integer.parseInt(input.trim());
            if (itemDAO.getItemById(conn, id) != null) {
                return id;
            }
            System.out.println("!! エラー: 存在しないIDです。一覧から選択してください");
        }
    }

    private static String inputNote(Scanner scanner) {
        System.out.print("メモを入力（100文字以内／Qで終了）: ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Q")) return null;
        String trimmed = input.trim();
        if (trimmed.length() > 100) {
            System.out.println("!! エラー: メモは100文字以内で入力してください");
            return inputNote(scanner); // 再帰的に再入力
        }
        return trimmed;
    }

    private static int inputAmount(Scanner scanner, String label) {
        System.out.print(label + "を入力（0以上の整数／Qで終了）: ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Q")) return -1;
        int value = Integer.parseInt(input.trim());
        if (value < 0) {
            System.out.println("!! エラー: 0以上の値を入力してください");
            return inputAmount(scanner, label); // 再帰的に再入力
        }
        return value;
    }

    private static boolean confirmContinue(Scanner scanner) {
        System.out.print("\n続けて入力しますか？ [Y/N]: ");
        String input = scanner.nextLine().trim().toUpperCase();
        return input.equals("Y");
    }
}
