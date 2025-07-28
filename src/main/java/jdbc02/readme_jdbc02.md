# jdbc02: 基本的なCRUD操作とDAO/DTOパターン

## 1. jdbc02の目的

`jdbc01`でデータベースへの接続を確認できたので、`jdbc02`では実際にデータベースを操作する基本的なコーディング方法を学びます。

この章では、アプリケーションの基本的な機能である**CRUD**（Create, Read, Update, Delete）を実装し、その過程で初学者が覚えるべき重要な設計パターンやセキュリティの基礎を解説します。

**この章のポイント:**
-   データベース操作の基本であるCRUDの実装方法
-   DAO/DTOパターンによる関心の分離
-   `PreparedStatement`を使った安全なSQLの実行
-   ヘルパーメソッドによるコードの共通化

**前提:**
この章では、まずデータベース操作の基本構造を理解することに焦点を当てるため、JDBCの**オートコミットモード**で処理を行います。SQLが実行されると、その結果は即座にデータベースに反映されます。

## 2. DAO/DTOパターン：役割分担でコードを綺麗に保つ

`jdbc02`では、`CashbookDAO.java`と`CashbookDTO.java`という2つのクラスが登場します。これは**DAO/DTOパターン**と呼ばれる、データベースアプリケーションで広く使われる設計パターンです。

-   **DTO (Data Transfer Object): `CashbookDTO.java`**
    -   **役割:** データを運ぶための箱。
    -   データベースの`cashbook`テーブルの**1行分のデータ**を格納するためのオブジェクトです。
    -   `cashbook_id`や`note`といった、テーブルのカラムに対応するフィールド（変数）と、その値を取得・設定するための`getter/setter`メソッドを持ちます。
    -   ロジック（計算など）は持たず、純粋にデータを保持することに専念します。

-   **DAO (Data Access Object): `CashbookDAO.java`**
    -   **役割:** データベースとのやり取りを担当する職人。
    -   `INSERT`や`SELECT`といった、データベースへの**具体的なアクセス処理（SQLの実行）**をすべてこのクラスにまとめます。
    -   `MainApp.java`などの他のクラスは、「データを取得して」や「このデータを保存して」とDAOに依頼するだけで済み、SQLの具体的な内容を知る必要がなくなります。

**なぜ分けるのか？**
このように役割を分けることで、「データ」と「データベース操作のロジック」が分離され、コードの見通しが良くなります。将来的にデータベースの種類が変わったり、SQLの記述を修正したりする場合も、**DAOクラスだけを変更すればよくなる**ため、メンテナンス性が大幅に向上します。

## 3. `PreparedStatement`: 安全なSQL実行の必須テクニック

`CashbookDAO.java`では、SQLを実行するために`PreparedStatement`を利用しています。

```java
// 例: IDを指定してデータを取得するSQL
private static final String SELECT_BY_ID_SQL = "SELECT * FROM cashbook WHERE cashbook_id = ?";
// ...
PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL);
pstmt.setInt(1, cashbookId); // 1番目の?にcashbookIdをセット
ResultSet rs = pstmt.executeQuery();
```

`PreparedStatement`は、SQL文の中の可変部分を`?`（プレースホルダ）としておき、後から`pstmt.setInt()`などのメソッドで値をセットする方式です。

**なぜ`PreparedStatement`を使うのか？**
最大の理由は**SQLインジェクション攻撃を防ぐため**です。もし、SQL文を文字列連結で作成すると（例: `"SELECT * FROM ... WHERE id = " + userInput`）、悪意のあるユーザーが`userInput`にSQL文を破壊するような文字列（例: `1 OR 1=1`）を入力した場合、意図しないデータが漏洩・改ざんされる危険性があります。

`PreparedStatement`は、`?`にセットされた値を**単なる値**として扱い、SQL文の構造の一部として解釈しないため、このような攻撃を根本的に防ぐことができます。データベースを扱う上で、**セキュリティの基本中の基本**となる非常に重要なクラスです。

## 4. ヘルパーメソッドと`toString()`

### ヘルパーメソッドでコードの重複を避ける

`CashbookDAO.java`には`mapToCashbookDTO()`という`private`なメソッドがあります。

```java
private CashbookDTO mapToCashbookDTO(ResultSet rs) throws SQLException {
    // ResultSetからDTOへデータを詰め替える処理
}
```

`getAllCashbooks()`や`findById()`など、`ResultSet`から`CashbookDTO`にデータを詰め替える処理は何度も登場します。この共通処理を`mapToCashbookDTO`という**ヘルパーメソッド**に切り出すことで、同じコードを何度も書く必要がなくなり、コードがスッキリします。もし将来詰め替え処理のロジックに変更が必要になった場合も、この一箇所を修正するだけで済みます。

### `toString()`でデバッグを効率化

`CashbookDTO.java`では、`Object`クラスの`toString()`メソッドをオーバーライドしています（実装されていなければ、ぜひ実装してください）。

```java
@Override
public String toString() {
    return "CashbookDTO{" +
           "cashbookId=" + cashbookId +
           ", note='" + note + '\'' +
           // ...
           '}';
}
```

これにより、`System.out.println(someCashbookDto);`を実行したときに、オブジェクトのメモリアドレスではなく、**中身のデータが分かりやすく表示**されるようになります。これは、プログラムが正しく動作しているかを確認する際の、非常に強力な助けとなります。
