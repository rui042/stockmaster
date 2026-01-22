package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegisterDao extends Dao {

    public String registerProduct(
        String itemId, String name, String category, String shelfId,
        int price, int stockNow, int stockMin, int storeId, String note,
        boolean forceRegister
    ) throws Exception {

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // 商品番号の重複チェック
            try (PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM ITEMS WHERE ITEM_ID = ?")) {
                checkStmt.setString(1, itemId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    return "この商品番号は既に登録されています。";
                }
            }

            // 商品登録
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO ITEMS (ITEM_ID, ITEM_NAME, PRICE) VALUES (?, ?, ?)")) {
                stmt.setString(1, itemId);
                stmt.setString(2, name);
                stmt.setInt(3, price);
                stmt.executeUpdate();
            }

            int shelfSeq = -1;

            // 棚チェック
            try (PreparedStatement checkShelf = conn.prepareStatement(
                    "SELECT SHELF_SEQ, CATEGORY, NOTE FROM SHELF WHERE SHELF_ID = ? AND STORE_ID = ? ORDER BY SHELF_SEQ DESC")) {
                checkShelf.setString(1, shelfId);
                checkShelf.setInt(2, storeId);
                ResultSet rs = checkShelf.executeQuery();

                // 初期化
                boolean foundMatchingCategory = false;
                Integer matchingShelfSeq = null;
                String firstExistingCategory = null;
                String existingNote = null;

                while (rs.next()) {
                    String existingCat = rs.getString("CATEGORY");

                    // 取得データを保存、比較
                    if (firstExistingCategory == null) {
                        firstExistingCategory = existingCat;
                    }

                    // 一致したデータの情報を取得し1つのみを使用
                    if (existingCat != null && existingCat.equals(category)) {
                        foundMatchingCategory = true;
                        matchingShelfSeq = rs.getInt("SHELF_SEQ");
                        existingNote = rs.getString("NOTE");
                        break;
                    }
                }

                if (foundMatchingCategory) {
                    // 分類一致 → 既存棚を利用
                    shelfSeq = matchingShelfSeq;

                    // 既存の棚に備考がnullまたは空文字の場合上書き可能
                    if ((existingNote == null || existingNote.trim().isEmpty())
                            && note != null && !note.trim().isEmpty()) {
                        try (PreparedStatement updateNote = conn.prepareStatement(
                        		"UPDATE SHELF SET NOTE = ? WHERE SHELF_SEQ = ?")) {
                            updateNote.setString(1, note);
                            updateNote.setInt(2, shelfSeq); // 主キーで更新
                            updateNote.executeUpdate();
                        }
                    }else if (!existingNote.equals(note) && forceRegister) {
                        // 備考不一致だが承認済み → 上書き
                        try (PreparedStatement updateNote = conn.prepareStatement(
                                "UPDATE SHELF SET NOTE = ? WHERE SHELF_SEQ = ?")) {
                            updateNote.setString(1, note);
                            updateNote.setInt(2, shelfSeq); // 主キーで更新
                            updateNote.executeUpdate();
                        }
                    }
                } else {
                    // 分類不一致 or 棚未登録
                    if (firstExistingCategory != null && !forceRegister) {
                        conn.rollback();
                        return String.format("棚「%s」は現在「%s」分類です。異なる分類「%s」で登録するには承認が必要です。",
                                shelfId, firstExistingCategory, category);
                    }

                    // 棚番号が存在しない場合新規登録
                    try (PreparedStatement insertShelf = conn.prepareStatement(
                            "INSERT INTO SHELF (SHELF_ID, STORE_ID, CATEGORY, NOTE) VALUES (?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        insertShelf.setString(1, shelfId);
                        insertShelf.setInt(2, storeId);
                        insertShelf.setString(3, category);
                        insertShelf.setString(4, note);
                        insertShelf.executeUpdate();

                        try (ResultSet keys = insertShelf.getGeneratedKeys()) {
                            if (keys.next()) {
                                shelfSeq = keys.getInt(1);
                            } else {
                            	// （主キー取得不可）
                                conn.rollback();
                                return "棚の登録に失敗しました。";
                            }
                        }
                    }

                    // 整合性チェック：新規棚が本当に STORE_ID と紐づいているか確認
                    try (PreparedStatement verify = conn.prepareStatement(
                            "SELECT COUNT(*) FROM SHELF WHERE SHELF_SEQ = ? AND STORE_ID = ?")) {

                        verify.setInt(1, shelfSeq);
                        verify.setInt(2, storeId);

                        ResultSet vrs = verify.executeQuery();
                        if (!vrs.next() || vrs.getInt(1) == 0) {
                            conn.rollback();
                            return "棚の作成に失敗しました（店舗IDが一致しません）。";
                        }
                    }

                }
            }

            // 在庫登録（SHELF_SEQ を利用）
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO STOCK (ITEM_ID, SHELF_SEQ, STORE_ID, STOCK_NOW, STOCK_MIN) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, itemId);
                stmt.setInt(2, shelfSeq);
                stmt.setInt(3, storeId);
                stmt.setInt(4, stockNow);
                stmt.setInt(5, stockMin);
                stmt.executeUpdate();
            }

            conn.commit();
            return "success";
        }
    }

    // 分類不一致チェック
    public String checkShelfCategory(String shelfId, int storeId, String category) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT CATEGORY FROM SHELF WHERE SHELF_ID = ? AND STORE_ID = ? ORDER BY SHELF_SEQ DESC")) {
            stmt.setString(1, shelfId);
            stmt.setInt(2, storeId);
            ResultSet rs = stmt.executeQuery();

            String existingCategory = null;
            while (rs.next()) {
                String cat = rs.getString("CATEGORY");

                if (cat != null) {
                    existingCategory = cat;
                    if (cat.equals(category)) {
                        return null; // 一致 → 警告不要
                    }
                }
            }

            if (existingCategory != null && !existingCategory.equals(category)) {
                return String.format(
                    "棚「%s」は現在「%s」分類の棚です。異なる分類「%s」で登録してよろしいですか？",
                    shelfId, existingCategory, category
                );
            }
        }
        return null;
    }

    // 備考不一致チェック
    public String checkShelfNote(String shelfId, int storeId, String newNote) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT CATEGORY, NOTE FROM SHELF WHERE SHELF_ID = ? AND STORE_ID = ? ORDER BY SHELF_SEQ DESC")) {
            stmt.setString(1, shelfId);
            stmt.setInt(2, storeId);
            ResultSet rs = stmt.executeQuery();

            String existingCategory = null;
            String existingNote = null;

            if (rs.next()) {
            	existingCategory = rs.getString("CATEGORY");
                existingNote = rs.getString("NOTE");
            }

            if (existingNote != null && !existingNote.trim().isEmpty()
                    && newNote != null && !newNote.trim().isEmpty()
                    && !existingNote.equals(newNote)) {
                return String.format(
                    "棚「%s」（分類: %s）には既に備考「%s」が登録されています。新しい備考「%s」で上書きしてよろしいですか？",
                    shelfId,
                    (existingCategory != null ? existingCategory : "未分類"),
                    existingNote,
                    newNote
                );
            }
        }
        return null; // 警告不要
    }
}