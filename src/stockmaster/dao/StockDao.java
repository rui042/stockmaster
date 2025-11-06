package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import stockmaster.bean.StockBean;

public class StockDao extends Dao {

    // 🔹 全在庫一覧を取得
    public List<StockBean> findAll() {
        List<StockBean> list = new ArrayList<>();
        String sql = "SELECT ITEM_ID, SHELF_ID, STORE_ID, STOCK_NOW, STOCK_MIN FROM STOCK";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StockBean stock = new StockBean(
                    rs.getString("ITEM_ID"),
                    rs.getString("SHELF_ID"),
                    rs.getInt("STORE_ID"),
                    rs.getInt("STOCK_NOW"),
                    rs.getInt("STOCK_MIN")
                );
                list.add(stock);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 🔹 商品ID＋店舗IDで在庫を検索
    public StockBean findByItemAndStore(String itemId, int storeId) {
        StockBean stock = null;
        String sql = "SELECT ITEM_ID, SHELF_ID, STORE_ID, STOCK_NOW, STOCK_MIN FROM STOCK WHERE ITEM_ID = ? AND STORE_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, itemId);
            stmt.setInt(2, storeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stock = new StockBean(
                        rs.getString("ITEM_ID"),
                        rs.getString("SHELF_ID"),
                        rs.getInt("STORE_ID"),
                        rs.getInt("STOCK_NOW"),
                        rs.getInt("STOCK_MIN")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stock;
    }

    // 🔹 在庫数を加算更新（入荷処理用）
    public boolean updateStock(int storeId, String itemId, int quantityToAdd) {
        String sql = "UPDATE STOCK SET STOCK_NOW = STOCK_NOW + ? WHERE STORE_ID = ? AND ITEM_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantityToAdd);
            stmt.setInt(2, storeId);
            stmt.setString(3, itemId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("[INFO] 在庫を加算更新しました: STORE_ID=" + storeId + ", ITEM_ID=" + itemId + ", +" + quantityToAdd);
            } else {
                System.out.println("[WARN] 該当する在庫レコードがありません: STORE_ID=" + storeId + ", ITEM_ID=" + itemId);
            }

            return rows > 0;

        } catch (Exception e) {
            System.err.println("[ERROR] updateStock() でエラー発生");
            e.printStackTrace();
            return false;
        }
    }
}
