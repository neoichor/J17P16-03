package jdbc02_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private static final String SELECT_ALL_SQL = "SELECT * FROM item ORDER BY item_id ASC";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM item WHERE item_id = ?";
    private static final String INSERT_SQL = "INSERT INTO item (item_name) VALUES (?)";
    private static final String UPDATE_SQL = "UPDATE item SET item_name = ? WHERE item_id = ?";
    private static final String DELETE_SQL = "DELETE FROM item WHERE item_id = ?";

    public List<ItemDTO> getAllItems() throws SQLException {
        List<ItemDTO> items = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                items.add(mapToItemDTO(rs));
            }
        }
        return items;
    }

    public ItemDTO findById(int itemId) throws SQLException {
        ItemDTO item = null;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    item = mapToItemDTO(rs);
                }
            }
        }
        return item;
    }

    public int insertItem(ItemDTO item) throws SQLException {
        int generatedId = -1;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, item.getItemName());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        }
        return generatedId;
    }

    public void updateItem(ItemDTO item) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setInt(2, item.getItemId());
            pstmt.executeUpdate();
        }
    }

    public void deleteItem(int itemId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
        }
    }

    /**
     * 【ヘルパーメソッド】
     * ResultSetのカーソルが現在指している行のデータをItemDTOにマッピング（詰め替え）します。
     * 
     * このメソッドは、DAOクラス内部の複数の場所で同じ処理（ResultSetからDTOへのデータ詰め替え）が
     * 必要になるために作成されました。処理を一つにまとめることで、コードの重複をなくし、
     * 可読性とメンテナンス性を向上させる目的があります（このような定型処理をまとめたメソッドを
     * ヘルパーメソッドやユーティリティメソッドと呼びます）。
     * 
     * @param rs データが格納されているResultSetオブジェクト。カーソルは有効な行を指している必要があります。
     * @return マッピングされたItemDTOオブジェクト
     * @throws SQLException ResultSetからのデータ取得に失敗した場合
     */
    private ItemDTO mapToItemDTO(ResultSet rs) throws SQLException {
        ItemDTO item = new ItemDTO();
        item.setItemId(rs.getInt("item_id"));
        item.setItemName(rs.getString("item_name"));
        return item;
    }
}
