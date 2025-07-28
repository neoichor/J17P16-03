package jdbc04;

import java.sql.SQLException;
import java.util.List;

/**
 * コンソールへの出力に関する共通処理をまとめたユーティリティクラスです。
 */
public class ConsoleUtils {

    /**
     * 品目リストを標準的なフォーマットでコンソールに出力します。
     * 
     * @param items 表示する品目のリスト
     */
    public static void printItemList(List<ItemDTO> items) {
        System.out.println("\n【現在登録されている品目】");
        System.out.println("ID | 品目名");
        System.out.println("---+-------------------");
        if (items == null || items.isEmpty()) {
            System.out.println("登録されている品目はありません");
        } else {
            for (ItemDTO item : items) {
                System.out.printf("%3d | %s%n", item.getItemId(), item.getItemName());
            }
        }
        System.out.println();
    }

    /**
     * データベースエラーに関する情報を標準エラー出力に表示します。
     * 
     * @param e 発生したSQLException
     */
    public static void handleDatabaseError(SQLException e) {
        System.err.println("\n!! データベースエラーが発生しました");
        if (e.getSQLState() == null) {
            System.err.println("!! エラー: データベース接続に問題がある可能性があります。");
        } else {
            System.err.printf("!! SQLSTATE: %s%n", e.getSQLState());
            System.err.printf("!! Message: %s%n", e.getMessage());
        }
        e.printStackTrace(System.err);
        System.err.println();
    }

    // /**
    // * 家計簿データのリストを表形式でコンソールに出力します。
    // * @param cashbooks 表示する家計簿データのリスト
    // */
    // public static void printCashbookList(List<CashbookDTO> cashbooks) {
    // if (cashbooks == null || cashbooks.isEmpty()) {
    // System.out.println(">> 該当するデータがありませんでした。");
    // return;
    // }

    // System.out.println("\n【検索結果】");
    // System.out.println("----------------------------------------------------------------------------------------");
    // System.out.printf("%-4s | %-10s | %-12s | %-20s | %10s | %10s%n", "ID", "日付",
    // "費目名", "メモ", "入金額", "出金額");
    // System.out.println("----------------------------------------------------------------------------------------");

    // for (CashbookDTO dto : cashbooks) {
    // System.out.printf("%-4d | %-10s | %-12s | %-20.20s | %,10d | %,10d%n",
    // dto.getCashbookId(),
    // dto.getActOn(),
    // StringUtils.getSafeString(dto.getItemName()),
    // StringUtils.getSafeString(dto.getNote()),
    // dto.getCashIn(),
    // dto.getCashOut());
    // }
    // System.out.println("----------------------------------------------------------------------------------------");
    // }

    // /**
    // * 家計簿データ1件の詳細をコンソールに出力します。
    // * @param cashbook 表示する家計簿データ
    // * @param itemName 費目名（別途取得して渡す）
    // */
    // public static void printCashbookDetails(CashbookDTO cashbook, String
    // itemName) {
    // if (cashbook == null) {
    // return;
    // }

    // System.out.println("\n【操作対象データ】");
    // System.out.println("--------------------");
    // System.out.println("ID : " + cashbook.getCashbookId());
    // System.out.println("日付 : " + cashbook.getActOn());
    // System.out.println("費目 : " + StringUtils.getSafeString(itemName));
    // System.out.println("メモ : " + StringUtils.getSafeString(cashbook.getNote()));
    // System.out.println("入金額 : " + String.format("%,d 円", cashbook.getCashIn()));
    // System.out.println("出金額 : " + String.format("%,d 円", cashbook.getCashOut()));
    // System.out.println("--------------------");
    // }
}