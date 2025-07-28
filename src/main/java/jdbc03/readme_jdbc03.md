# jdbc03: 手動コミットによるトランザクション管理

## 1. jdbc03の目的とテーマ

`jdbc02`では、DAO/DTOパターンを使い、基本的なCRUD操作を一つずつ実行する方法を学びました。`jdbc03`では、これらの複数の操作を「**一つのまとまった処理**」として扱い、データの整合性を保証するための**トランザクション管理**を導入します。

機能的には`jdbc02`と変わりませんが、ここでは**手動コミットモード**を使い、より堅牢で信頼性の高いデータベースアプリケーションの実装方法を学びます。

## 2. なぜトランザクション管理が必要か？（おさらい）

実際のアプリケーションでは、「商品の在庫を減らし、売上を記録する」「A口座から引き落とし、B口座に入金する」のように、**複数のデータベース更新がすべて成功して初めて、処理全体が成功したと見なしたい**ケースがほとんどです。

`jdbc02`のオートコミットモードでは、一つ目の更新が成功した直後にシステム障害が起きると、データが中途半端な状態（在庫は減ったのに、売上が記録されていない）で残ってしまいます。

トランザクション管理は、一連の処理の**原子性（Atomicity）**、つまり「**すべて成功(All) or すべて失敗(Nothing)**」を保証し、このようなデータの不整合を防ぎます。

## 3. `jdbc02`から`jdbc03`への構造的変化

トランザクションを実現するため、`jdbc02`からクラスの役割分担が少し変わりました。

-   **`jdbc02`の構造**
    -   DAOの各メソッド（`insertCashbook`など）が、**内部で`Connection`の取得と解放を行っていた**。
    -   各メソッドが独立した、完結した一つの処理単位だった。

-   **`jdbc03`の構造**
    -   **DAOは`Connection`を管理しない**。DAOの各メソッドは、引数で`Connection`オブジェクトを**受け取る**だけ。
    -   `MainApp`（呼び出し元）が`Connection`の取得、`commit`/`rollback`、解放の**全責任を持つ**。

このように、トランザクションの境界線をDAOの外（`MainApp`）に移動させることで、複数のDAOメソッドの呼び出しを、単一のトランザクション内で実行できるようになります。

## 4. 手動コミットの実装パターン

`MainApp.java`を見ると、トランザクション管理の典型的な実装パターンが分かります。

```java
// 1. まずConnectionを取得 (try-with-resourcesで自動クローズを保証)
try (Connection conn = DatabaseConnection.getConnection()) {

    // 2. トランザクション処理本体 (try-catchで囲む)
    try {
        // 2a. オートコミットを無効化し、手動コミットモードを開始
        conn.setAutoCommit(false);

        CashbookDAO dao = new CashbookDAO();

        // 2b. 複数のDB操作を、すべて同じConnection(conn)を渡して実行
        dao.insertCashbook(conn, ...);
        dao.updateCashbook(conn, ...);
        dao.deleteCashbook(conn, ...);

        // 2c. すべての処理が例外なく完了したら、変更を確定
        conn.commit();

    } catch (SQLException e) {
        // 2d. 途中でSQLExceptionが発生した場合、すべての変更を取り消す
        System.err.println("エラー発生のため、ロールバックします");
        conn.rollback(); // ※rollback自体も例外を投げる可能性に注意
    }

} catch (SQLException e) {
    // データベース接続や、commit/rollback処理自体の失敗をキャッチ
    e.printStackTrace();
}
```

この`try-catch`と`setAutoCommit(false)`, `commit()`, `rollback()`の組み合わせが、JDBCにおけるトランザクション管理の基本形です。これにより、`insert`, `update`, `delete`のいずれかで問題が発生した場合でも、`rollback()`によってデータベースが処理開始前の状態に安全に戻ることが保証されます。
