package stockmaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import stockmaster.bean.UserBean;

public class UserDao extends Dao {

    // ログイン用
    public UserBean findByIdAndPassword(String userId, String password) {
        UserBean user = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT USER_ID, PASSWORD, NAME, EMAIL, IS_STAFF FROM USERS WHERE USER_ID = ? AND PASSWORD = ?")) {

            stmt.setString(1, userId);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new UserBean(
                        rs.getString("USER_ID"),
                        rs.getString("PASSWORD"),
                        rs.getString("NAME"),
                        rs.getString("EMAIL"),
                        rs.getBoolean("IS_STAFF")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // 新規登録用
    public boolean insert(UserBean user) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO USERS (USER_ID, PASSWORD, NAME, EMAIL, IS_STAFF) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.isStaff());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}