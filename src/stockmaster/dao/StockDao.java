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

    // 🔹 商品IDで在庫を検索
    public StockBean findByItemId(String itemId) {
        StockBean stock = null;

        String sql = "SELECT ITEM_ID, SHELF_ID, STORE_ID, STOCK_NOW, STOCK_MIN FROM STOCK WHERE ITEM_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, itemId);

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

    // 🔹 在庫数を更新
    public boolean updateStock(String itemId, int newStock) {
        String sql = "UPDATE STOCK SET STOCK_NOW = ? WHERE ITEM_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newStock);
            stmt.setString(2, itemId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
