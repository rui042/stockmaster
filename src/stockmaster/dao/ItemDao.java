package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import stockmaster.bean.Item;

public class ItemDao extends Dao {

    /**
     * 全件取得（初期表示用）
     */
    public List<Item> findAll() {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT ITEM_ID, ITEM_NAME, PRICE FROM ITEMS";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Item(
                    rs.getString("ITEM_ID"),
                    rs.getString("ITEM_NAME"),
                    rs.getInt("PRICE")
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
    public List<Item> searchByKeyword(String keyword) {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT ITEM_ID, ITEM_NAME, PRICE FROM ITEMS WHERE ITEM_NAME LIKE ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Item(
                    rs.getString("ITEM_ID"),
                    rs.getString("ITEM_NAME"),
                    rs.getInt("PRICE")
                ));
            }

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean updateItem(String itemId, String itemName, int price) {
        String sql = "UPDATE ITEMS SET ITEM_NAME = ?, PRICE = ? WHERE ITEM_ID = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, itemName);
            ps.setInt(2, price);
            ps.setString(3, itemId);

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * バーコード検索（商品ID完全一致）
     */
    public Item searchById(String id) {
        String sql = "SELECT ITEM_ID, ITEM_NAME, PRICE FROM ITEMS WHERE ITEM_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Item(
                    rs.getString("ITEM_ID"),
                    rs.getString("ITEM_NAME"),
                    rs.getInt("PRICE")
                );
            }

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
