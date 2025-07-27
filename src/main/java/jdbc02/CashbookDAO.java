package jdbc02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CashbookDAO {

    public List<CashbookDTO> getAllCashbooks() throws SQLException {
        List<CashbookDTO> cashbooks = new ArrayList<>();
        
        String sql = "SELECT * FROM cashbook ORDER BY cashbook_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CashbookDTO cashbook = new CashbookDTO();
                cashbook.setCashbookId(rs.getInt("cashbook_id"));
                cashbook.setActOn(rs.getDate("act_on"));
                cashbook.setItemId(rs.getInt("item_id"));
                cashbook.setNote(rs.getString("note"));
                cashbook.setCashIn(rs.getInt("cash_in"));
                cashbook.setCashOut(rs.getInt("cash_out"));
                cashbooks.add(cashbook);
            }
        }
        return cashbooks;
    }

    public void insertCashbook(CashbookDTO cashbook) throws SQLException {
        String sql = "INSERT INTO cashbook "
        		+ "(act_on, item_id, note, cash_in, cash_out) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, cashbook.getActOn());
            pstmt.setInt(2, cashbook.getItemId());
            pstmt.setString(3, cashbook.getNote());
            pstmt.setInt(4, cashbook.getCashIn());
            pstmt.setInt(5, cashbook.getCashOut());
            pstmt.executeUpdate();
        }
    }

    public void updateCashbook(CashbookDTO cashbook) throws SQLException {
        String sql = "UPDATE cashbook SET act_on = ?, item_id = ?, note = ?, cash_in = ?, cash_out = ? WHERE cashbook_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        String sql = "DELETE FROM cashbook WHERE cashbook_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cashbookId);
            pstmt.executeUpdate();
        }
    }
} 