package stockmaster.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import stockmaster.bean.StoreBean;

public class StoreDao {

	public List<StoreBean> searchStores(String name, String area) {
	    List<StoreBean> list = new ArrayList<>();
	    StringBuilder sql = new StringBuilder(
	        "SELECT STORE_ID, STORE_NAME, STORE_ADDRESS, STORE_PHONE, OPEN_TIME, CLOSE_TIME FROM STORES WHERE 1=1"
	    );

	    List<String> values = new ArrayList<>();

	    if (name != null && !name.trim().isEmpty()) {
	    	sql.append(" AND STORE_NAME ILIKE ?");
	        values.add("%" + name.trim().toUpperCase() + "%");
	    }

	    if (area != null && !area.trim().isEmpty()) {
	    	sql.append(" AND STORE_ADDRESS ILIKE ?");
	        values.add("%" + area.trim().toUpperCase() + "%");
	    }

	    try (Connection conn = DriverManager.getConnection(
	            "jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
	         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

	        for (int i = 0; i < values.size(); i++) {
	            stmt.setString(i + 1, values.get(i));
	        }

	        ResultSet rs = stmt.executeQuery();
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

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}
}
