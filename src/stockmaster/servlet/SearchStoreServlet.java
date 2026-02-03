package stockmaster.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import stockmaster.bean.StoreBean;
import stockmaster.dao.StoreDao;

@WebServlet("/searchStore")
public class SearchStoreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	HttpSession session = req.getSession(true);

        // ゲストフラグを確認
        String guestFlag = req.getParameter("guest");
        String username = null;

        if ("true".equals(guestFlag)) {
            // ログイン情報をリセットしてゲスト扱いにする
            session.removeAttribute("loginUser");
            session.removeAttribute("username");
            session.removeAttribute("isStaff");
            session.removeAttribute("chatHistory");
            session.removeAttribute("currentStepKey");

            session.setAttribute("isGuest", true);
            username = "ゲスト";
            // System.out.println("ゲストユーザーとして店舗検索にアクセス");
        } else {
            // 通常ログインユーザーを確認
            username = (String) session.getAttribute("username");
            if (username != null) {
                System.out.println("ログイン中ユーザー: " + username);
            } else {
                System.out.println("未ログインユーザーが店舗検索にアクセス");
            }
        }

        /** // デバッグ表示
        if (username != null) {
            System.out.println("ログイン中ユーザー: " + username);
        } else {
            System.out.println("未ログインユーザーが店舗検索にアクセス");
        } */

        String areaIdStr = req.getParameter("areaId");

        StoreDao dao = new StoreDao();
        List<StoreBean> storeList = new ArrayList<>();

        try {
            if (areaIdStr != null && !areaIdStr.isEmpty()) {
                // 地域IDで検索（本来は areaId → address などに変換するのが自然）
                storeList = dao.searchStores(null, areaIdStr);
            } else {
                // 全件取得
                storeList = dao.findAll();
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.setAttribute("storeList", storeList);
        req.setAttribute("searched", true);
        req.setAttribute("areaId", areaIdStr); // 選択状態保持用
        // ログインしていれば名前を渡してヘッダーに表示
        req.setAttribute("username", username);

        // JSPにフォワード
        req.getRequestDispatcher("/views/searchStore.jsp").forward(req, resp);
    }
}
