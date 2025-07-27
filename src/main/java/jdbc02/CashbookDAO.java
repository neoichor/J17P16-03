package jdbc02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CashbookDAO {

    // SQL文を定数として定義しておくと、管理しやすくなる
    private static final String SELECT_ALL_SQL = "SELECT * FROM cashbook ORDER BY cashbook_id ASC";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM cashbook WHERE cashbook_id = ?";
    private static final String INSERT_SQL = "INSERT INTO cashbook (act_on, item_id, note, cash_in, cash_out) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE cashbook SET act_on = ?, item_id = ?, note = ?, cash_in = ?, cash_out = ? WHERE cashbook_id = ?";
    private static final String DELETE_SQL = "DELETE FROM cashbook WHERE cashbook_id = ?";

    public List<CashbookDTO> getAllCashbooks() throws SQLException {
        List<CashbookDTO> cashbooks = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                cashbooks.add(mapToCashbookDTO(rs));
            }
        }
        return cashbooks;
    }

    public CashbookDTO findById(int cashbookId) throws SQLException {
        CashbookDTO cashbook = null;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            pstmt.setInt(1, cashbookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cashbook = mapToCashbookDTO(rs);
                }
            }
        }
        return cashbook;
    }

    public int insertCashbook(CashbookDTO cashbook) throws SQLException {
        int generatedId = -1; // 生成されなかった場合は-1を返す
        // PreparedStatement作成時に、生成キーを返すように指定する
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setDate(1, cashbook.getActOn());
            pstmt.setInt(2, cashbook.getItemId());
            pstmt.setString(3, cashbook.getNote());
            pstmt.setInt(4, cashbook.getCashIn());
            pstmt.setInt(5, cashbook.getCashOut());
            pstmt.executeUpdate();

            // 生成されたキーを取得する
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1); // 1列目のキーを取得
                }
            }
        }
        return generatedId;
    }

    public void updateCashbook(CashbookDTO cashbook) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            pstmt.setDate(1, cashbook.getActOn());
            pstmt.setInt(2, cashbook.getItemId());
            pstmt.setString(3, cashbook.getNote());
            pstmt.setInt(4, cashbook.getCashIn());
            pstmt.setInt(5, cashbook.getCashOut());
            pstmt.setInt(6, cashbook.getCashbookId());
            pstmt.executeUpdate();
        }
    }

    public void deleteCashbook(int cashbookId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            pstmt.setInt(1, cashbookId);
            pstmt.executeUpdate();
        }
    }

    // ResultSetからDTOへの変換処理を共通化するヘルパーメソッド
    private CashbookDTO mapToCashbookDTO(ResultSet rs) throws SQLException {
        CashbookDTO cashbook = new CashbookDTO();
        cashbook.setCashbookId(rs.getInt("cashbook_id"));
        cashbook.setActOn(rs.getDate("act_on"));
        cashbook.setItemId(rs.getInt("item_id"));
        cashbook.setNote(rs.getString("note"));
        cashbook.setCashIn(rs.getInt("cash_in"));
        cashbook.setCashOut(rs.getInt("cash_out"));
        return cashbook;
    }
}