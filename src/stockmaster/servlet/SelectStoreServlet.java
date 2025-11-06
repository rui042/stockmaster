package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.bean.StoreBean;

@WebServlet("/selectStore")
public class SelectStoreServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {

    List<StoreBean> storeList = new ArrayList<>();

    // ドライバのロード
    try {
      Class.forName("org.h2.Driver");

      try (Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
           PreparedStatement stmt = conn.prepareStatement("SELECT STORE_ID, STORE_NAME, STORE_ADDRESS, STORE_PHONE, OPEN_TIME, CLOSE_TIME FROM STORES");
           ResultSet rs = stmt.executeQuery()) {

    	  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        while (rs.next()) {
          int id = rs.getInt("STORE_ID");
          String name = rs.getString("STORE_NAME");
          String address = rs.getString("STORE_ADDRESS");
          String phone = rs.getString("STORE_PHONE");
          String openTimeStr = rs.getString("OPEN_TIME");
          String closeTimeStr = rs.getString("CLOSE_TIME");
          // 時刻を LocalTime に変換
          LocalTime openTime = LocalTime.parse(openTimeStr, timeFormatter);
          LocalTime closeTime = LocalTime.parse(closeTimeStr, timeFormatter);

          StoreBean store = new StoreBean(id, name, address, phone, openTime, closeTime);
          storeList.add(store);
        }
      }

      // 正常時にJSPへ遷移
      req.setAttribute("storeList", storeList);
      req.getRequestDispatcher("/views/selectStore.jsp").forward(req, resp);

    } catch (ClassNotFoundException | SQLException e) {
      throw new ServletException(e);
    }
  }
}