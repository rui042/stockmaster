package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.HistoryBean;
import stockmaster.bean.UserBean;
import stockmaster.dao.StockDao;

@WebServlet("/stockHistory")
public class StockHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserBean loginUser = (session != null) ? (UserBean) session.getAttribute("loginUser") : null;

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String itemId = request.getParameter("itemId");
        int storeId = Integer.parseInt(request.getParameter("storeId"));

        StockDao dao = new StockDao();
        HistoryBean inbound = dao.findLatestInbound(storeId, itemId);
        HistoryBean outbound = dao.findLatestOutbound(storeId, itemId);

        request.setAttribute("inbound", inbound);
        request.setAttribute("outbound", outbound);
        request.getRequestDispatcher("/views/stockHistory.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}