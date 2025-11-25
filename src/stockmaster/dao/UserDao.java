package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import stockmaster.bean.UserBean;

public class UserDao extends Dao {

    // ログイン用（店舗情報も取得）
    public UserBean findByIdAndPassword(String userId, String password) {
        UserBean user = null;
        String sql = "SELECT u.USER_ID, u.PASSWORD, u.NAME, u.EMAIL, u.IS_STAFF, u.IS_ADMIN, " +
                "u.STORE_ID, s.STORE_NAME " +
                "FROM USERS u " +
                "LEFT JOIN STORES s ON u.STORE_ID = s.STORE_ID " +
                "WHERE u.USER_ID = ? AND u.PASSWORD = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new UserBean(
                        rs.getString("USER_ID"),
                        rs.getString("PASSWORD"),
                        rs.getString("NAME"),
                        rs.getString("EMAIL"),
                        rs.getBoolean("IS_STAFF"),
                        rs.getBoolean("IS_ADMIN")
                    );

                    // 店舗情報をセット
                    int storeId = rs.getInt("STORE_ID");
                    if (!rs.wasNull()) {
                        user.setStoreId(storeId);
                        user.setStoreName(rs.getString("STORE_NAME"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // 新規登録用（店舗IDも登録可能）
    public boolean insert(UserBean user) {
    	String sql = "INSERT INTO USERS (USER_ID, PASSWORD, NAME, EMAIL, IS_STAFF, IS_ADMIN, STORE_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.isStaff());
            stmt.setBoolean(6, user.isAdmin());

            if (user.getStoreId() != null) {
                stmt.setInt(7, user.getStoreId());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}