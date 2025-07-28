package jdbc04;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 【品目DAO（itemテーブル用）】
 * トランザクション管理のため、Connectionを外部から受け取るように変更。
 */
public class ItemDAO {

	/**
	 * itemテーブルの全レコードを取得
	 */
	public List<ItemDTO> getAllItems(Connection conn) throws SQLException {
		List<ItemDTO> items = new ArrayList<>();
		String sql = "SELECT * FROM item ORDER BY item_id ASC";
		try (PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				ItemDTO itemDTO = new ItemDTO();
				itemDTO.setItemId(rs.getInt("item_id"));
				itemDTO.setItemName(rs.getString("item_name"));
				items.add(itemDTO);
			}
		}
		return items;
	}

	/**
	 * IDで品目取得
	 */
	public ItemDTO getItemById(Connection conn, int itemId) throws SQLException {
		String sql = "SELECT * FROM item WHERE item_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, itemId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					ItemDTO itemDTO = new ItemDTO();
					itemDTO.setItemId(rs.getInt("item_id"));
					itemDTO.setItemName(rs.getString("item_name"));
					return itemDTO;
				}
			}
		}
		return null;
	}

	/**
	 * 品目追加
	 */
	public void insertItem(Connection conn, ItemDTO item) throws SQLException {
		String sql = "INSERT INTO item (item_name) VALUES (?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, item.getItemName());
			pstmt.executeUpdate();
		}
	}

	/**
	 * 品目更新
	 */
	public void updateItem(Connection conn, ItemDTO item) throws SQLException {
		String sql = "UPDATE item SET item_name=? WHERE item_id=?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, item.getItemName());
			pstmt.setInt(2, item.getItemId());
			pstmt.executeUpdate();
		}
	}

	/**
	 * 品目削除
	 */
	public void deleteItem(Connection conn, int itemId) throws SQLException {
		String sql = "DELETE FROM item WHERE item_id = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, itemId);
			pstmt.executeUpdate();
		}
	}
}
