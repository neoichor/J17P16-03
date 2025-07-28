# Gemini との協働ルール (gemini.md)

## 1. プロジェクトの目的

このプロジェクトは、Java の JDBC（Java Database Connectivity）技術を学習・検証することを目的としています。
段階的に `jdbc01` から `jdbc03` へと発展しており、データベース操作の基本から DAO（Data Access Object）パターンの実装までを含みます。

## 2. 技術スタック

- **言語:** Java 17
- **ビルドツール:** Maven
- **データベース:** PostgreSQL 16
- **テストフレームワーク:** JUnit 5
- **実行環境:** Docker

## 3. ローカル開発環境

### 3.1. 起動

1.  リポジトリのルートディレクトリで、`.env.example` をコピーして `.env` ファイルを作成します。
2.  `.env` ファイル内の環境変数を、ご自身の環境に合わせて設定してください。
3.  以下のコマンドを実行して、Docker コンテナを起動します。

    ```bash
    docker-compose up -d --build
    ```

### 3.2. 停止

```bash
docker-compose down
```

## 4. コーディング規約

- **フォーマット:** 基本的に[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)に準拠します。
- **命名規則:**
  - クラス名: `UpperCamelCase`
  - メソッド名・変数名: `lowerCamelCase`
  - 定数: `CONSTANT_CASE`
- **コメント:** 複雑なロジックや、なぜその実装にしたのかという理由を説明する場合にコメントを追加してください。
- **文法:**
  - 使用する文法は Java SE8。
  - 初学者に Java の基礎を理解してもらうために、アロー構文や、::を使ったシュガーシンタックスは使わない。
  - 拡張 for 文と try-catch-with-resources 文はデータベースを扱うので積極的に使う。

## 5. ビルドと実行

### 5.1. ビルド

以下のコマンドで、プロジェクトのビルド（コンパイル、テスト、パッケージング）を実行します。

```bash
mvn package
```

### 5.2. アプリケーションの実行

ビルドが成功すると、`target` ディレクトリに実行可能な JAR ファイルが生成されます。
以下のコマンドで例えば `jdbc03` のアプリケーションを実行します。

```bash
java -jar target/J17P16-01-0.0.1-SNAPSHOT.jar
```

## 6. テスト

- **テストコードの場所:** `src/test/java`
- **テストの実行:**

  ```bash
  mvn test
  ```

- **カバレッジレポート:**

  ```bash
  mvn jacoco:report
  ```

  レポートは `target/site/jacoco/index.html` に生成されます。

## 7. コミットメッセージ

コミットメッセージは [Conventional Commits](https://www.conventionalcommits.org/) の規約に準拠します。

- `feat`: 新機能の追加
- `fix`: バグ修正
- `docs`: ドキュメントの変更
- `style`: コードスタイルの変更（フォーマットなど）
- `refactor`: リファクタリング
- `test`: テストの追加・修正
- `chore`: ビルドプロセスや補助ツールの変更

例: `feat: ユーザー登録機能を追加`

## 8. その他

- データベースの接続情報は `.env` ファイルで管理し、Git の追跡対象から除外します。
- 新しいライブラリを追加する場合は、`pom.xml` に記述し、`mvn dependency-check:check` を実行して脆弱性がないか確認してください。
- プロジェクト内の別フォルダーから、単純に java プログラムをコピーしてくるときには、パッケージ名をコピー先のフォルダー名に合わせて必ず修正してください。
