package stockmaster.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.bean.StoreBean;

@WebServlet("/storeDetail")
public class StoreDetailServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String idStr = req.getParameter("id");
    if (idStr == null || idStr.isEmpty()) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "店舗IDが指定されていません");
      return;
    }

    try {
      Class.forName("org.h2.Driver");

      try (Connection conn = DriverManager.getConnection(
              "jdbc:h2:tcp://localhost/~/stockmaster;MODE=MySQL", "sa", "");
           PreparedStatement stmt = conn.prepareStatement(
              "SELECT STORE_ID, STORE_NAME, STORE_ADDRESS, STORE_PHONE, OPEN_TIME, CLOSE_TIME FROM STORES WHERE STORE_ID = ?")) {

        stmt.setInt(1, Integer.parseInt(idStr));
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
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

          req.setAttribute("store", store);
          req.getRequestDispatcher("/views/storeDetail.jsp").forward(req, resp);
        } else {
          resp.sendError(HttpServletResponse.SC_NOT_FOUND, "店舗が見つかりません");
        }
      }
    } catch (ClassNotFoundException | SQLException e) {
      throw new ServletException(e);
    }
  }
}
