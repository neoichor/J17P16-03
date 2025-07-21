-- このスクリプトは docker-compose.yml で作成された 'sukkiri' データベース上で実行されます。
-- データベースの作成や接続コマンドは不要です。

-- 開発演習用のitemテーブルを作成する
CREATE TABLE item (
    item_id SERIAL PRIMARY KEY,
    item_name VARCHAR(20) NOT NULL
);

-- item テストデータを挿入する
INSERT INTO item (item_name) VALUES ('食費'), ('給料'), ('教養娯楽費'), ('交際費'), ('水道光熱費');

-- 開発演習用のcashbookテーブルを作成する
CREATE TABLE cashbook (
    cashbook_id SERIAL PRIMARY KEY,
    act_on DATE NOT NULL,
    item_id INTEGER NOT NULL,
    note VARCHAR(100),
    cash_in INTEGER,
    cash_out INTEGER,
    CONSTRAINT fk_item_id
        FOREIGN KEY (item_id)
        REFERENCES item (item_id)
        ON DELETE RESTRICT
);

-- cashbook テストデータを挿入する
INSERT INTO cashbook (act_on, item_id, note, cash_in, cash_out) VALUES
('2024-02-03', 1, 'コーヒーを購入', 0, 380),
('2024-02-10', 2, '1月の給料', 280000, 0),
('2024-02-11', 3, '書籍を購入', 0, 2800),
('2024-02-14', 4, '同期会の会費', 0, 5000),
('2024-02-18', 5, '1月の電気代', 0, 7560);

-- cashbookのViewを作成する
CREATE VIEW cashbook_view AS
SELECT
    cb.cashbook_id AS "ID",
    cb.act_on      AS "日付",
    cb.item_id     AS "itemID",
    i.item_name    AS "費目",
    cb.note        AS "メモ",
    cb.cash_in     AS "入金額",
    cb.cash_out    AS "出金額"
FROM
    cashbook cb
JOIN
    item i ON cb.item_id = i.item_id;