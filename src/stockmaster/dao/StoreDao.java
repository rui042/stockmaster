package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import stockmaster.bean.StoreBean;

public class StoreDao extends Dao {

    /** 🔹 名前・住所で店舗を検索 */
    public List<StoreBean> searchStores(String name, String area) {
        List<StoreBean> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT STORE_ID, STORE_NAME, STORE_ADDRESS, STORE_PHONE, OPEN_TIME, CLOSE_TIME FROM STORES WHERE 1=1"
        );

        List<String> values = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND STORE_NAME LIKE ?");
            values.add("%" + name.trim() + "%");
        }

        if (area != null && !area.trim().isEmpty()) {
            sql.append(" AND STORE_ADDRESS LIKE ?");
            values.add("%" + area.trim() + "%");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < values.size(); i++) {
                stmt.setString(i + 1, values.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Time open = rs.getTime("OPEN_TIME");
                    Time close = rs.getTime("CLOSE_TIME");

                    StoreBean store = new StoreBean(
                        rs.getInt("STORE_ID"),
                        rs.getString("STORE_NAME"),
                        rs.getString("STORE_ADDRESS"),
                        rs.getString("STORE_PHONE"),
                        open != null ? open.toLocalTime() : LocalTime.MIN,
                        close != null ? close.toLocalTime() : LocalTime.MAX
                    );
                    store.updateOpenNow();
                    list.add(store);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /** 🔹 全店舗取得（ドロップダウン用） */
    public List<StoreBean> findAll() {
        List<StoreBean> list = new ArrayList<>();
        String sql = "SELECT STORE_ID, STORE_NAME, STORE_ADDRESS, STORE_PHONE, OPEN_TIME, CLOSE_TIME FROM STORES ORDER BY STORE_ID";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Time open = rs.getTime("OPEN_TIME");
                Time close = rs.getTime("CLOSE_TIME");

                StoreBean store = new StoreBean(
                    rs.getInt("STORE_ID"),
                    rs.getString("STORE_NAME"),
                    rs.getString("STORE_ADDRESS"),
                    rs.getString("STORE_PHONE"),
                    open != null ? open.toLocalTime() : LocalTime.MIN,
                    close != null ? close.toLocalTime() : LocalTime.MAX
                );
                store.updateOpenNow();
                list.add(store);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
