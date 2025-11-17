package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.dao.ItemDao;

@WebServlet("/updateItem")
public class UpdateItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /** 編集画面表示（GET） */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // パラメータをそのままJSPに渡す（itemId, itemName, price）
        request.setAttribute("itemId", request.getParameter("itemId"));
        request.setAttribute("itemName", request.getParameter("itemName"));
        request.setAttribute("price", request.getParameter("price"));

        // 編集画面へフォワード
        request.getRequestDispatcher("/views/editItem.jsp").forward(request, response);
    }

    /** 商品情報更新（POST） */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String itemId = request.getParameter("itemId");
        String itemName = request.getParameter("itemName");
        String priceStr = request.getParameter("price");

        int price = 0;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "価格が不正です");
            request.setAttribute("itemId", itemId);
            request.setAttribute("itemName", itemName);
            request.setAttribute("price", priceStr);
            request.getRequestDispatcher("/views/editItem.jsp").forward(request, response);
            return;
        }

        ItemDao dao = new ItemDao();
        boolean updated = dao.updateItem(itemId, itemName, price);

        if (updated) {
            request.setAttribute("message", "商品情報を更新しました");
        } else {
            request.setAttribute("message", "更新に失敗しました");
        }

        request.setAttribute("itemId", itemId);
        request.setAttribute("itemName", itemName);
        request.setAttribute("price", price);
        request.getRequestDispatcher("/views/editItem.jsp").forward(request, response);
    }
}