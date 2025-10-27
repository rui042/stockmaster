package stockmaster.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import stockmaster.bean.UserBean;

public class UserDao {

    public UserBean findByIdAndPassword(String userId, String password) {
        UserBean user = null;

        try {
            // H2ドライバを明示的にロード
            Class.forName("org.h2.Driver");

            // H2の接続URL（ファイルDBの場合）
            try (Connection conn = DriverManager.getConnection(
                        "jdbc:h2:~/stms", "sa", "");
                 PreparedStatement stmt = conn.prepareStatement(
                        "SELECT USER_ID, PASSWORD, NAME, IS_STAFF FROM USERS WHERE USER_ID = ? AND PASSWORD = ?")) {

                System.out.println("検索条件: userId=" + userId + ", password=" + password);

                stmt.setString(1, userId);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        user = new UserBean(
                            rs.getString("USER_ID"),
                            rs.getString("PASSWORD"),
                            rs.getString("NAME"),
                            rs.getBoolean("IS_STAFF")
                        );
                        System.out.println("DBヒット: " + user.getName());
                    } else {
                        System.out.println("DBヒットなし");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}