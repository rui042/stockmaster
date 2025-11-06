package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import stockmaster.bean.StoreBean;

public class StoreDao extends Dao {

    /** 🔹 全店舗を取得（デバッグログ付き） */
    public List<StoreBean> findAll() throws Exception {
        List<StoreBean> list = new ArrayList<>();
        String sql = "SELECT STORE_ID, STORE_NAME, STORE_ADDRESS FROM STORES ORDER BY STORE_ID";

        System.out.println("[DEBUG] StoreDao.findAll() start");

        try (Connection conn = getConnection()) {

            System.out.println("[DEBUG] Connection success: " + (conn != null));

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("[DEBUG] SQL executed successfully: " + sql);

                while (rs.next()) {
                    StoreBean bean = new StoreBean();
                    bean.setStoreId(rs.getInt("STORE_ID"));
                    bean.setStoreName(rs.getString("STORE_NAME"));
                    bean.setStoreAddress(rs.getString("STORE_ADDRESS"));
                    list.add(bean);
                }

                System.out.println("[DEBUG] Records fetched: " + list.size());
            }

        } catch (Exception e) {
            System.err.println("[ERROR] StoreDao.findAll() failed");
            e.printStackTrace();
            throw e;
        }

        System.out.println("[DEBUG] StoreDao.findAll() end");
        return list;
    }

    /** 🔹 ID指定で1件取得（デバッグログ付き） */
    public StoreBean findById(int storeId) throws Exception {
        String sql = "SELECT STORE_ID, STORE_NAME, STORE_ADDRESS FROM STORES WHERE STORE_ID = ?";
        System.out.println("[DEBUG] StoreDao.findById(" + storeId + ") start");

        try (Connection conn = getConnection()) {
            System.out.println("[DEBUG] Connection success: " + (conn != null));

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, storeId);
                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        StoreBean bean = new StoreBean();
                        bean.setStoreId(rs.getInt("STORE_ID"));
                        bean.setStoreName(rs.getString("STORE_NAME"));
                        bean.setStoreAddress(rs.getString("STORE_ADDRESS"));

                        System.out.println("[DEBUG] Store found: " + bean.getStoreName());
                        return bean;
                    } else {
                        System.out.println("[DEBUG] No store found for ID=" + storeId);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] StoreDao.findById() failed");
            e.printStackTrace();
            throw e;
        }

        System.out.println("[DEBUG] StoreDao.findById() end");
        return null;
    }
}
