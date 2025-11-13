package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterDao extends Dao{

	public String registerProduct(
        String itemId, String name, String category, String shelfId,
        int price, int stockNow, int stockMin, int storeId, String note
    ) throws Exception {

		try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // 商品番号の重複チェック
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM ITEMS WHERE ITEM_ID = ?")) {
                checkStmt.setString(1, itemId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return "この商品番号は既に登録されています。";
                }
            }

            // 商品登録
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO ITEMS (ITEM_ID, ITEM_NAME, PRICE) VALUES (?, ?, ?)")) {
                stmt.setString(1, itemId);
                stmt.setString(2, name);
                stmt.setInt(3, price);
                stmt.executeUpdate();
            }

            // 棚チェック・登録・備考処理
            try (PreparedStatement checkShelf = conn.prepareStatement("SELECT CATEGORY, NOTE FROM SHELF WHERE SHELF_ID = ? AND STORE_ID = ?")) {
                checkShelf.setString(1, shelfId);
                checkShelf.setInt(2, storeId);
                ResultSet rs = checkShelf.executeQuery();

                if (rs.next()) {
                    String existingCategory = rs.getString("CATEGORY");
                    String existingNote = rs.getString("NOTE");

                    if (existingCategory != null && !existingCategory.equals(category)) {
                        return String.format("棚番号「%s」は既に存在します。分類は「%s」で指定してください。", shelfId, existingCategory);
                    }

                    if ((existingNote == null || existingNote.trim().isEmpty()) && note != null && !note.trim().isEmpty()) {
                        try (PreparedStatement updateNote = conn.prepareStatement("UPDATE SHELF SET NOTE = ? WHERE SHELF_ID = ? AND STORE_ID = ?")) {
                            updateNote.setString(1, note);
                            updateNote.setString(2, shelfId);
                            updateNote.setInt(3, storeId);
                            updateNote.executeUpdate();
                        }
                    } else if (note != null && !note.trim().isEmpty()) {
                        return "既存の棚には備考を上書きできません。";
                    }

                } else {
                    try (PreparedStatement insertShelf = conn.prepareStatement("INSERT INTO SHELF (SHELF_ID, STORE_ID, CATEGORY, NOTE) VALUES (?, ?, ?, ?)")) {
                        insertShelf.setString(1, shelfId);
                        insertShelf.setInt(2, storeId);
                        insertShelf.setString(3, category);
                        insertShelf.setString(4, note);
                        insertShelf.executeUpdate();
                    }
                }
            }

            // 在庫登録
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO STOCK (ITEM_ID, SHELF_ID, STORE_ID, STOCK_NOW, STOCK_MIN) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, itemId);
                stmt.setString(2, shelfId);
                stmt.setInt(3, storeId);
                stmt.setInt(4, stockNow);
                stmt.setInt(5, stockMin);
                stmt.executeUpdate();
            }

            conn.commit();
            return "success";
        }
    }
}