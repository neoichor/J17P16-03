package jdbc05;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 【家計簿DAO（cashbookテーブル用）】
 * 手動トランザクション管理に対応し、N+1問題を解決したバージョン。
 */
public class CashbookDAO {

    public void insertCashbook(Connection conn, CashbookDTO cashbook) throws SQLException {
        String sql = "INSERT INTO cashbook (act_on, item_id, note, cash_in, cash_out) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, cashbook.getActOn());
            pstmt.setInt(2, cashbook.getItemId());
            pstmt.setString(3, cashbook.getNote());
            pstmt.setInt(4, cashbook.getCashIn());
            pstmt.setInt(5, cashbook.getCashOut());
            pstmt.executeUpdate();
        }
    }

    public void updateCashbook(Connection conn, CashbookDTO cashbook) throws SQLException {
        String sql = "UPDATE cashbook SET act_on=?, note=?, cash_in=?, cash_out=? WHERE cashbook_id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, cashbook.getActOn());
            pstmt.setString(2, cashbook.getNote());
            pstmt.setInt(3, cashbook.getCashIn());
            pstmt.setInt(4, cashbook.getCashOut());
            pstmt.setInt(5, cashbook.getCashbookId());
            pstmt.executeUpdate();
        }
    }

    public void deleteCashbook(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM cashbook WHERE cashbook_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public CashbookDTO getCashbookById(Connection conn, int cashbookId) throws SQLException {
        String sql = "SELECT * FROM cashbook WHERE cashbook_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cashbookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToCashbookDTO(rs);
                }
            }
        }
        return null;
    }

    /**
     * 【N+1問題解決版】JOINでitem_nameも一度に取得する
     */
    public List<CashbookDTO> getCashbooksByDateRange(Connection conn, Date start, Date end) throws SQLException {
        List<CashbookDTO> cashbooks = new ArrayList<>();
        String sql = "SELECT cb.cashbook_id, cb.act_on, cb.item_id, cb.note, cb.cash_in, cb.cash_out, i.item_name "
                   + "FROM cashbook cb JOIN item i ON cb.item_id = i.item_id "
                   + "WHERE cb.act_on BETWEEN ? AND ? ORDER BY cb.act_on ASC, cb.cashbook_id ASC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, start);
            pstmt.setDate(2, end);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CashbookDTO dto = mapToCashbookDTO(rs);
                    dto.setItemName(rs.getString("item_name")); // JOINした項目をセット
                    cashbooks.add(dto);
                }
            }
        }
        return cashbooks;
    }

    private CashbookDTO mapToCashbookDTO(ResultSet rs) throws SQLException {
        CashbookDTO dto = new CashbookDTO();
        dto.setCashbookId(rs.getInt("cashbook_id"));
        dto.setActOn(rs.getDate("act_on"));
        dto.setItemId(rs.getInt("item_id"));
        dto.setNote(rs.getString("note"));
        dto.setCashIn(rs.getInt("cash_in"));
        dto.setCashOut(rs.getInt("cash_out"));
        return dto;
    }
}
