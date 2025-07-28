# jdbc01

### 目的/課題：JDBC ドライバーを介して sukkiri データベースに接続する（ハンズオン）

接続に成功したら「PostgreSQL への接続に成功！！」失敗したら「PostgreSQL への接続に失敗！！」と表示されるだけです。
これにより、JDBC 経由で PostgreSQL に接続する基本を学びます。
JDBC 接続の環境設定の良否の確認を兼ねて作成します。

---

### プログラムの機能

- **DatabaseConnection.java**:

  - 環境変数から DB の接続情報（ホスト、ポート、DB 名、ユーザー、パスワード）を読み込みます。
  - 読み込んだ情報に基づき、PostgreSQL の JDBC URL を構築します。
  - `getConnection()` メソッドを通じて、DB への接続オブジェクト (Connection) を提供します。

- **MainApp.java**:
  - `DatabaseConnection` クラスを利用して、データベースへの接続を試みます。
  - 接続が成功したか失敗したかを標準出力に表示します。

### 注意点

- 実行前に、.env ファイルに必要な環境変数を設定し、読み込ませる必要があります。
  - DB_HOST
  - DB_PORT
  - DB_NAME
  - DB_USER
  - DB_PASSWORD
- このプログラムはデータベースへの接続テストのみを行い、データの取得や更新は行いません。
- PostgreSQL の JDBC ドライバがクラスパスに含まれている必要があります。(Maven プロジェクトのため、pom.xml で管理されています)
