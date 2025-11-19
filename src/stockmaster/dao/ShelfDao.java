package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import stockmaster.bean.ShelfBean;

public class ShelfDao extends Dao {

    // 🔹 棚テーブルの全件取得
    public List<ShelfBean> findAll() {
        List<ShelfBean> list = new ArrayList<>();
        String sql = "SELECT SHELF_SEQ, SHELF_ID, LOCATION, STORE_ID, CATEGORY, NOTE, X_PCT, Y_PCT FROM SHELF";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ShelfBean shelf = new ShelfBean(
                    rs.getString("SHELF_ID"),
                    rs.getString("LOCATION"),
                    rs.getInt("STORE_ID"),
                    rs.getString("CATEGORY"),
                    rs.getString("NOTE")
                );
                shelf.setShelfSeq(rs.getInt("SHELF_SEQ")); // 🔹 追加
                shelf.setXPct(rs.getInt("X_PCT"));
                shelf.setYPct(rs.getInt("Y_PCT"));

                list.add(shelf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔹 店舗IDで棚を検索
    public List<ShelfBean> findByStore(int storeId) {
        List<ShelfBean> list = new ArrayList<>();
        String sql = "SELECT SHELF_SEQ, SHELF_ID, LOCATION, STORE_ID, CATEGORY, NOTE, X_PCT, Y_PCT FROM SHELF WHERE STORE_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, storeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ShelfBean shelf = new ShelfBean(
                        rs.getString("SHELF_ID"),
                        rs.getString("LOCATION"),
                        rs.getInt("STORE_ID"),
                        rs.getString("CATEGORY"),
                        rs.getString("NOTE")
                    );
                    shelf.setShelfSeq(rs.getInt("SHELF_SEQ")); // 🔹 追加
                    shelf.setXPct(rs.getInt("X_PCT"));
                    shelf.setYPct(rs.getInt("Y_PCT"));

                    list.add(shelf);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔹 棚IDで1件取得（検索結果から棚座標を使う場合に便利）
    public ShelfBean findById(String shelfId) {
        ShelfBean shelf = null;
        String sql = "SELECT SHELF_SEQ, SHELF_ID, LOCATION, STORE_ID, CATEGORY, NOTE, X_PCT, Y_PCT FROM SHELF WHERE SHELF_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, shelfId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    shelf = new ShelfBean(
                        rs.getString("SHELF_ID"),
                        rs.getString("LOCATION"),
                        rs.getInt("STORE_ID"),
                        rs.getString("CATEGORY"),
                        rs.getString("NOTE")
                    );
                    shelf.setShelfSeq(rs.getInt("SHELF_SEQ")); // 🔹 追加
                    shelf.setXPct(rs.getInt("X_PCT"));
                    shelf.setYPct(rs.getInt("Y_PCT"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return shelf;
    }
}