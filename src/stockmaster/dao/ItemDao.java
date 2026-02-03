package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import stockmaster.bean.Item;
import stockmaster.bean.ShelfBean;

public class ItemDao extends Dao {

    /**
     * 全件取得（初期表示用）
     */
	public List<Item> findAll(int storeId) {
	    List<Item> list = new ArrayList<>();

	    String sql =
	        "SELECT I.ITEM_ID, I.ITEM_NAME, I.PRICE, " +
	        "       S.SHELF_ID, S.CATEGORY " +
	        "FROM ITEMS I " +
	        "JOIN STOCK ST ON I.ITEM_ID = ST.ITEM_ID " +
	        "JOIN SHELF S ON ST.SHELF_SEQ = S.SHELF_SEQ " +
	        "WHERE ST.STORE_ID = ? " +
	        "ORDER BY I.ITEM_ID";

	    try (Connection con = getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, storeId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            list.add(new Item(
	                rs.getString("ITEM_ID"),
	                rs.getString("ITEM_NAME"),
	                rs.getInt("PRICE"),
	                rs.getString("SHELF_ID"),
	                rs.getString("CATEGORY")
	            ));
	        }

	    } catch (SQLException | NamingException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

    /**
     * キーワード検索（商品名の部分一致）
     */
	public List<Item> searchByKeyword(String keyword, int storeId) {
	    List<Item> list = new ArrayList<>();

	    String sql =
	        "SELECT I.ITEM_ID, I.ITEM_NAME, I.PRICE, " +
	        "       S.SHELF_ID, S.CATEGORY " +
	        "FROM ITEMS I " +
	        "JOIN STOCK ST ON I.ITEM_ID = ST.ITEM_ID " +
	        "JOIN SHELF S ON ST.SHELF_SEQ = S.SHELF_SEQ " +
	        "WHERE ST.STORE_ID = ? " +
	        "  AND I.ITEM_NAME LIKE ? " +
	        "ORDER BY I.ITEM_ID";

	    try (Connection con = getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, storeId);
	        ps.setString(2, "%" + keyword + "%");

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            list.add(new Item(
	                rs.getString("ITEM_ID"),
	                rs.getString("ITEM_NAME"),
	                rs.getInt("PRICE"),
	                rs.getString("SHELF_ID"),
	                rs.getString("CATEGORY")
	            ));
	        }

	    } catch (SQLException | NamingException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

    /**
     * バーコード検索（商品ID完全一致）
     */
	public Item searchById(String id, int storeId) {
	    String sql =
	        "SELECT I.ITEM_ID, I.ITEM_NAME, I.PRICE, " +
	        "       S.SHELF_ID, S.CATEGORY " +
	        "FROM ITEMS I " +
	        "JOIN STOCK ST ON I.ITEM_ID = ST.ITEM_ID " +
	        "JOIN SHELF S ON ST.SHELF_SEQ = S.SHELF_SEQ " +
	        "WHERE I.ITEM_ID = ? AND ST.STORE_ID = ?";

	    try (Connection con = getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, id);
	        ps.setInt(2, storeId);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return new Item(
	                rs.getString("ITEM_ID"),
	                rs.getString("ITEM_NAME"),
	                rs.getInt("PRICE"),
	                rs.getString("SHELF_ID"),
	                rs.getString("CATEGORY")
	            );
	        }

	    } catch (SQLException | NamingException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

    /**
     * editItem用の更新機能
     */
    /**
     * 商品取得
     */
	public Item findItem(String itemId, int storeId) {
	    String sql =
	        "SELECT I.ITEM_ID, I.ITEM_NAME, I.PRICE, S.SHELF_ID, S.CATEGORY " +
	        "FROM ITEMS I " +
	        "JOIN STOCK ST ON I.ITEM_ID = ST.ITEM_ID " +
	        "JOIN SHELF S ON ST.SHELF_SEQ = S.SHELF_SEQ " +
	        "WHERE I.ITEM_ID = ? AND ST.STORE_ID = ?";

	    try (Connection con = getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, itemId);
	        ps.setInt(2, storeId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return new Item(
	                rs.getString("ITEM_ID"),
	                rs.getString("ITEM_NAME"),
	                rs.getInt("PRICE"),
	                rs.getString("SHELF_ID"),
	                rs.getString("CATEGORY")
	            );
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

    /*STOCK から SHELF_SEQ を取得*/
    public int findShelfSeq(String itemId, int storeId) {
        String sql = "SELECT SHELF_SEQ FROM STOCK WHERE ITEM_ID = ? AND STORE_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, itemId);
            ps.setInt(2, storeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("SHELF_SEQ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*SHELF_SEQ → SHELF_ID*/
    public ShelfBean findShelfBySeq(int shelfSeq, int storeId) {
        String sql = "SELECT SHELF_ID, CATEGORY, NOTE FROM SHELF WHERE SHELF_SEQ = ? AND STORE_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, shelfSeq);
            ps.setInt(2, storeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ShelfBean shelf = new ShelfBean();
                shelf.setShelfSeq(shelfSeq);
                shelf.setShelfId(rs.getString("SHELF_ID"));
                shelf.setCategory(rs.getString("CATEGORY"));
                shelf.setNote(rs.getString("NOTE"));
                return shelf;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*SHELF_ID → SHELF_SEQ（店舗ごと）*/
    public int findShelfSeqByShelfId(String shelfId, int storeId) {
        String sql = "SELECT SHELF_SEQ FROM SHELF WHERE SHELF_ID = ? AND STORE_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shelfId);
            ps.setInt(2, storeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("SHELF_SEQ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /*商品情報 + 棚変更*/
    public boolean updateItemAndShelf(String itemId, String itemName, int price, int shelfSeq, int storeId) {

        String updateItemSql = "UPDATE ITEMS SET ITEM_NAME=?, PRICE=? WHERE ITEM_ID=?";
        String updateStockSql = "UPDATE STOCK SET SHELF_SEQ=? WHERE ITEM_ID=? AND STORE_ID=?";

        try (Connection con = getConnection()) {

            /*商品更新*/
            try (PreparedStatement ps = con.prepareStatement(updateItemSql)) {
                ps.setString(1, itemName);
                ps.setInt(2, price);
                ps.setString(3, itemId);
                ps.executeUpdate();
            }

            /*棚更新*/
            try (PreparedStatement ps = con.prepareStatement(updateStockSql)) {
                ps.setInt(1, shelfSeq);
                ps.setString(2, itemId);
                ps.setInt(3, storeId);
                ps.executeUpdate();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createShelf(String shelfId, String category, String note, int storeId) {
        String sql = "INSERT INTO SHELF (SHELF_ID, LOCATION, STORE_ID, CATEGORY, NOTE) " +
                     "VALUES (?, NULL, ?, ?, ?)";

        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, shelfId);
            ps.setInt(2, storeId);
            ps.setString(3, category);
            ps.setString(4, note);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateShelfCategory(int shelfSeq, String category, int storeId) {
        String sql = "UPDATE SHELF SET CATEGORY = ? WHERE SHELF_SEQ = ? AND STORE_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);
            ps.setInt(2, shelfSeq);
            ps.setInt(3, storeId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateShelfNote(int shelfSeq, String note, int storeId) {
        String sql = "UPDATE SHELF SET NOTE = ? WHERE SHELF_SEQ = ? AND STORE_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, note);
            ps.setInt(2, shelfSeq);
            ps.setInt(3, storeId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}