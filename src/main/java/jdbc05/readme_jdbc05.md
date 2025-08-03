# jdbc05: N+1問題の理解と対策

## 1. jdbc05の目的

`jdbc04`では、`item`テーブルを対象に、ユーティリティクラスや単一責任の原則を導入し、本格的なCRUDアプリケーションの設計基盤を整えました。

`jdbc05`では、その設計基盤の上に**`cashbook`（家計簿）テーブルのCRUD機能**を実装します。この過程で、多くのデータベースアプリケーションで性能上のボトルネックとなりがちな、非常に重要な問題である「**N+1問題**」に焦点を当て、その原因と解決策について詳しく学びます。

## 2. 今回の最大のテーマ：N+1問題

`CashbookDateRangeSearch`（日付範囲検索）のように、一覧データを表示する際には、関連するテーブルの情報も同時に表示したいケースが頻繁にあります。例えば、家計簿データの一覧に、`item_id`だけでなく、それが何を指すのか（食費、交通費など）という`item_name`も表示したい、というような場合です。

このような要件を素朴に実装すると、N+1問題と呼ばれる深刻なパフォーマンス低下を引き起こす可能性があります。

### 2.1. N+1問題とは？

N+1問題とは、**1回のSQLで一覧データ（親）を取得し、その結果のN件のデータそれぞれに対して、関連するデータ（子）を取得するためにN回のSQLをループ処理で発行してしまう**状況を指します。合計で「1 + N」回のSQLが実行されるため、このように呼ばれます。

**悪い実装の例（N+1問題が発生するコード）:**

```java
// 1. まず、cashbookテーブルから一覧を取得 (これが「1」回のクエリ)
List<CashbookDTO> cashbooks = cashbookDAO.getCashbooksByDateRange(conn, start, end);

// 2. 取得したリストをループで回し、1件ずつitem_nameを取得 (これが「N」回のクエリ)
for (CashbookDTO dto : cashbooks) {
    // ループのたびに、itemDAO経由でSELECT文が実行されてしまう！
    ItemDTO item = itemDAO.getItemById(conn, dto.getItemId()); 
    System.out.println(dto.getNote() + " / " + item.getItemName());
}
```

データが10件なら合計11回、1000件なら合計1001回のSQLがデータベースとの間でやり取りされます。1回あたりのクエリは高速でも、ネットワークの往復遅延やデータベースの処理オーバーヘッドが積み重なり、アプリケーション全体の性能が劇的に悪化します。

### 2.2. jdbc05での解決策：SQLのJOIN

この問題の最も標準的な解決策は、**最初から必要なデータをすべてまとめて取得する**ことです。`jdbc05`の`CashbookDAO`では、`JOIN`句を使ってこのアプローチを実装しています。

**【対策1】 SQLの変更**

`getCashbooksByDateRange`メソッドで実行するSQLを、`cashbook`テーブルと`item`テーブルを`item_id`で結合（JOIN）するものに変更します。

```sql
SELECT 
    cb.cashbook_id, cb.act_on, cb.item_id, cb.note, cb.cash_in, cb.cash_out, 
    i.item_name  -- 欲しい費目名を最初からSELECTに含める
FROM 
    cashbook cb 
JOIN 
    item i ON cb.item_id = i.item_id -- ここで2つのテーブルを結合
WHERE 
    cb.act_on BETWEEN ? AND ? 
ORDER BY 
    cb.act_on ASC, cb.cashbook_id ASC;
```

このSQL文を発行すれば、データベースは**たった1回のクエリ**で、家計簿データとそれに対応する費目名の両方を含んだ結果セットを返してくれます。

**【対策2】 DTOの変更**

`JOIN`で取得した`item_name`をJava側で受け取るために、`CashbookDTO.java`に`itemName`を格納するためのフィールドを追加します。

```java
// CashbookDTO.java
public class CashbookDTO {
    // ... 既存のフィールド
    private String itemName; // JOINで取得した費目名を格納するフィールドを追加

    // ... itemNameのgetter/setterを追加
}
```

これにより、DAOは取得した`item_name`をDTOに詰め、UIクラスはDTOから直接`getItemName()`を呼び出すだけで、追加のデータベースアクセス無しに費目名を表示できるようになります。

### 2.3. N+1問題への別のアプローチ

`JOIN`は最も強力で一般的な解決策ですが、状況によっては他のアプローチが有効な場合もあります。

-   **アプローチA: IN句による一括取得（アプリケーション側での結合）**
    1.  まず、`cashbook`の一覧データを取得します。（1回目のクエリ）
    2.  Javaのコード上で、取得した`cashbook`リストから必要な`item_id`のリストを作成します。
    3.  次に、`SELECT * FROM item WHERE item_id IN (?, ?, ?, ...)` のように、`IN`句を使って必要な`item`データを**一度に**取得します。（2回目のクエリ）
    4.  取得した`item`リストを`Map<Integer, String>`（IDと名前のマップ）に変換し、最初の`cashbook`リストと突き合わせて表示します。
    *   **特徴:** クエリは合計2回になります。JOINが複雑になりすぎる場合や、複数のデータベースにまたがる場合などに有効なことがあります。

-   **アプローチB: ORMフレームワークの活用**
    *   JPA (Hibernate) や MyBatis といった**ORM (Object-Relational Mapping) フレームワーク**には、N+1問題を解決するための高度な仕組み（例: Eager Fetching, Batch Fetching）が備わっています。
    *   開発者はSQLを直接書く代わりに、オブジェクト間の関連を定義することで、フレームワークが効率的なデータ取得方法を自動的に選択・実行してくれます。
    *   **特徴:** 生産性が非常に高いですが、フレームワークの学習コストや、内部の挙動を理解しないと意図しない性能問題を引き起こす可能性もあります。

## 3. jdbc04の設計原則の継承

`jdbc05`の各クラスは、`jdbc04`で確立した以下の設計原則をすべて継承しています。
-   手動コミットによるトランザクション管理
-   `try-with-resources`による確実なリソース解放
-   `ConsoleUtils`, `StringUtils`の活用によるロジックの共通化とUIの標準化
-   `private`メソッドによる関心事の分離と可読性の向上

これにより、新しい機能（CashbookのCRUD）を追加しつつも、アプリケーション全体の設計思想は一貫しており、保守性の高い状態が保たれています。

---

## 4. 実装上の注意点：日付フォーマットの罠

`CashbookDateRangeSearch.java`のように、ユーザーが入力した文字列を日付として扱う処理には、注意すべき点があります。

### 問題点：`SimpleDateFormat`の書式指定子

例えば、日付フォーマットを以下のように指定したとします。

```java
// 間違った例
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD"); 
```

このコードで`2025-07-28`という日付を検索しようとすると、7月のデータがヒットしない、という問題が発生します。

### 原因：`dd`と`DD`の決定的な違い

`SimpleDateFormat`では、日付の書式指定子の大文字と小文字が厳密に区別されます。

-   `dd`: **月の日 (Day in month)** を表します (例: 28日)。私たちが普段使う日付はこちらです。
-   `DD`: **年の日 (Day in year)** を表します (例: 1月1日から数えて通算で何日目か)。

間違って`DD`を使うと、`2025-07-28`は「2025年の28日目」、つまり「**2025年1月28日**」として解釈されてしまいます。月として入力した`07`は無視されるため、意図した通りの検索ができません。

### 対策：正しい書式指定子を使う

この問題を解決するには、書式指定子を`DD`から`dd`に修正します。

```java
// 正しい例
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
```

この修正により、入力された日付が正しく「年-月-日」として解釈され、期待通りの検索結果が得られるようになります。日付を扱う際は、フォーマット指定子の意味を正確に理解することが非常に重要です。