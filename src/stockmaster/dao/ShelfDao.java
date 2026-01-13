package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import stockmaster.bean.ShelfBean;

public class ShelfDao extends Dao {

    // 全件取得
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
                shelf.setShelfSeq(rs.getInt("SHELF_SEQ"));
                shelf.setXPct(rs.getDouble("X_PCT"));
                shelf.setYPct(rs.getDouble("Y_PCT"));

                list.add(shelf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 店舗IDで棚取得
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
                    shelf.setShelfSeq(rs.getInt("SHELF_SEQ"));
                    shelf.setXPct(rs.getDouble("X_PCT"));
                    shelf.setYPct(rs.getDouble("Y_PCT"));

                    list.add(shelf);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 最寄り棚（storeId 限定）
    public ShelfBean findNearestShelf(int storeId, Double xPct, Double yPct) {

        if (xPct == null || yPct == null) return null;

        List<ShelfBean> shelves = findByStore(storeId);
        ShelfBean nearest = null;
        double minDist = Double.MAX_VALUE;

        for (ShelfBean shelf : shelves) {
            if (shelf.getXPct() != null && shelf.getYPct() != null) {

                double dx = shelf.getXPct() - xPct;
                double dy = shelf.getYPct() - yPct;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < minDist) {
                    minDist = dist;
                    nearest = shelf;
                }
            }
        }
        return nearest;
    }
}