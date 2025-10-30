package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
           PreparedStatement stmt = conn.prepareStatement("SELECT STORE_ID, STORE_NAME, STORE_ADDRESS FROM STORES");
           ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
          int id = rs.getInt("STORE_ID");
          String name = rs.getString("STORE_NAME");
          String address = rs.getString("STORE_ADDRESS");
          storeList.add(new StoreBean(id, name, address));
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