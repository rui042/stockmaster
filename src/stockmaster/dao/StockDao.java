package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import stockmaster.bean.HistoryBean;
import stockmaster.bean.StockBean;

public class StockDao extends Dao {

	// 商品表示一覧
    /** 店舗IDとキーワードで商品を検索し、在庫情報＋価格を返す */
    public List<StockBean> findByStoreAndKeyword(int storeId, String keyword) {
        List<StockBean> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.SHELF_SEQ, s.ITEM_ID, s.STOCK_NOW, s.STOCK_MIN, ");
        sql.append("i.ITEM_NAME, i.PRICE, ");
        sql.append("sh.SHELF_ID, sh.CATEGORY ");
        sql.append("FROM STOCK s ");
        sql.append("JOIN ITEMS i ON s.ITEM_ID = i.ITEM_ID ");
        sql.append("JOIN SHELF sh ON s.SHELF_SEQ = sh.SHELF_SEQ ");
        sql.append("WHERE s.STORE_ID = ? ");

        if (keyword != null && !keyword.isEmpty()) {
            sql.append("AND i.ITEM_NAME LIKE ? ");
        }

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, storeId);

            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(idx++, "%" + keyword + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockBean bean = new StockBean();
                    bean.setShelfSeq(rs.getInt("SHELF_SEQ"));
                    bean.setItemId(rs.getString("ITEM_ID"));
                    bean.setItemName(rs.getString("ITEM_NAME"));
                    bean.setPrice(rs.getInt("PRICE"));
                    bean.setStockNow(rs.getInt("STOCK_NOW"));
                    bean.setStockMin(rs.getInt("STOCK_MIN"));
                    bean.setStoreId(storeId);
                    bean.setShelfId(rs.getString("SHELF_ID"));
                    bean.setCategory(rs.getString("CATEGORY"));

                    list.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 商品IDで検索（バーコード検索） */
    public StockBean findByItemId(int storeId, String itemId) {
        String sql = "SELECT s.SHELF_SEQ, s.ITEM_ID, s.STOCK_NOW, s.STOCK_MIN, " +
                     "i.ITEM_NAME, i.PRICE, " +
                     "sh.SHELF_ID, sh.CATEGORY " +
                     "FROM STOCK s JOIN ITEMS i ON s.ITEM_ID = i.ITEM_ID " +
                     "JOIN SHELF sh ON s.SHELF_SEQ = sh.SHELF_SEQ " +
                     "WHERE s.STORE_ID = ? AND s.ITEM_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, storeId);
            ps.setString(2, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StockBean bean = new StockBean();
                    bean.setShelfSeq(rs.getInt("SHELF_SEQ"));
                    bean.setItemId(rs.getString("ITEM_ID"));
                    bean.setItemName(rs.getString("ITEM_NAME"));
                    bean.setPrice(rs.getInt("PRICE"));
                    bean.setStockNow(rs.getInt("STOCK_NOW"));
                    bean.setStockMin(rs.getInt("STOCK_MIN"));
                    bean.setStoreId(storeId);
                    bean.setShelfId(rs.getString("SHELF_ID"));
                    bean.setCategory(rs.getString("CATEGORY"));

                    return bean;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 店舗IDで全在庫を取得（価格付き） */
    public List<StockBean> findByStore(int storeId) {
        List<StockBean> list = new ArrayList<>();
        String sql = "SELECT s.SHELF_SEQ, s.ITEM_ID, s.STOCK_NOW, s.STOCK_MIN, " +
                     "i.ITEM_NAME, i.PRICE, " +
                     "sh.SHELF_ID, sh.CATEGORY " +
                     "FROM STOCK s JOIN ITEMS i ON s.ITEM_ID = i.ITEM_ID " +
                     "JOIN SHELF sh ON s.SHELF_SEQ = sh.SHELF_SEQ " +
                     "WHERE s.STORE_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockBean bean = new StockBean();
                    bean.setShelfSeq(rs.getInt("SHELF_SEQ"));
                    bean.setItemId(rs.getString("ITEM_ID"));
                    bean.setItemName(rs.getString("ITEM_NAME"));
                    bean.setPrice(rs.getInt("PRICE"));
                    bean.setStockNow(rs.getInt("STOCK_NOW"));
                    bean.setStockMin(rs.getInt("STOCK_MIN"));
                    bean.setStoreId(storeId);
                    bean.setShelfId(rs.getString("SHELF_ID"));
                    bean.setCategory(rs.getString("CATEGORY"));

                    list.add(bean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    // 入荷処理
    /** 在庫更新（入荷数を加算） */
    public boolean updateStock(int storeId, String itemId, int quantity) {
        String sql = "UPDATE STOCK SET STOCK_NOW = STOCK_NOW + ? WHERE STORE_ID = ? AND ITEM_ID = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, storeId);
            ps.setString(3, itemId);

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 入荷処理（在庫更新＋最新状態をSTOCK_STATUSに記録） */
    public boolean receiveStock(int storeId, String itemId, int quantity, String userId) {
        boolean result = false;
        String updateStockSql = "UPDATE STOCK SET STOCK_NOW = STOCK_NOW + ? WHERE STORE_ID = ? AND ITEM_ID = ?";
        String upsertStatusSql = "MERGE INTO STOCK_STATUS (ITEM_ID, STORE_ID, LAST_ACTION_TYPE, QUANTITY, ACTION_AT, USER_ID) "
                				+ "KEY (ITEM_ID, STORE_ID, LAST_ACTION_TYPE) "
                				+ "VALUES (?, ?, 'RECEIVE', ?, CURRENT_TIMESTAMP, ?)";

        try (Connection con = getConnection()) {
            con.setAutoCommit(false); // トランザクション開始

            /** 在庫数を加算 */
            try (PreparedStatement ps = con.prepareStatement(updateStockSql)) {
                ps.setInt(1, quantity);
                ps.setInt(2, storeId);
                ps.setString(3, itemId);
                int updated = ps.executeUpdate();
                if (updated == 0) {
                    con.rollback();
                    return false;
                }
            }

            /** 最新状態をSTOCK_STATUSに記録（MERGEでINSERT or UPDATE） */
            try (PreparedStatement ps = con.prepareStatement(upsertStatusSql)) {
                ps.setString(1, itemId);
                ps.setInt(2, storeId);
                ps.setInt(3, quantity);
                ps.setString(4, userId);
                ps.executeUpdate();
            }

            con.commit();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    // 在庫の入出荷履歴
    /** 最新の入荷履歴を1件取得 */
    public HistoryBean findLatestInbound(int storeId, String itemId) {
        String sql = "SELECT ss.ITEM_ID, ss.STORE_ID, ss.USER_ID, u.NAME, " +
                     "ss.LAST_ACTION_TYPE, ss.QUANTITY, ss.ACTION_AT, i.ITEM_NAME " +
                     "FROM STOCK_STATUS ss " +
                     "JOIN USERS u ON ss.USER_ID = u.USER_ID " +
                     "JOIN ITEMS i ON ss.ITEM_ID = i.ITEM_ID " +
                     "WHERE ss.STORE_ID = ? AND ss.ITEM_ID = ? AND ss.LAST_ACTION_TYPE = 'RECEIVE' " +
                     "ORDER BY ss.ACTION_AT DESC LIMIT 1";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setString(2, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HistoryBean(
                        rs.getString("ITEM_ID"),
                        rs.getString("ITEM_NAME"),
                        rs.getInt("STORE_ID"),
                        rs.getString("USER_ID"),
                        rs.getString("NAME"),
                        rs.getString("LAST_ACTION_TYPE"),
                        rs.getInt("QUANTITY"),
                        rs.getTimestamp("ACTION_AT")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 最新の出荷履歴を1件取得 */
    public HistoryBean findLatestOutbound(int storeId, String itemId) {
        String sql = "SELECT ss.ITEM_ID, ss.STORE_ID, ss.USER_ID, u.NAME, " +
                     "ss.LAST_ACTION_TYPE, ss.QUANTITY, ss.ACTION_AT, i.ITEM_NAME " +
                     "FROM STOCK_STATUS ss " +
                     "JOIN USERS u ON ss.USER_ID = u.USER_ID " +
                     "JOIN ITEMS i ON ss.ITEM_ID = i.ITEM_ID " +
                     "WHERE ss.STORE_ID = ? AND ss.ITEM_ID = ? AND ss.LAST_ACTION_TYPE = 'SHIP' " +
                     "ORDER BY ss.ACTION_AT DESC LIMIT 1";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setString(2, itemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HistoryBean(
                        rs.getString("ITEM_ID"),
                        rs.getString("ITEM_NAME"),
                        rs.getInt("STORE_ID"),
                        rs.getString("USER_ID"),
                        rs.getString("NAME"),
                        rs.getString("LAST_ACTION_TYPE"),
                        rs.getInt("QUANTITY"),
                        rs.getTimestamp("ACTION_AT")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}