package stockmaster.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import stockmaster.bean.Item;
import stockmaster.bean.ShelfBean;
import stockmaster.dao.ItemDao;

@WebServlet("/updateItem")
public class UpdateItemServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String itemId = request.getParameter("itemId");
	    int storeId = (int) request.getSession().getAttribute("storeId");

	    ItemDao dao = new ItemDao();

	    // 商品情報
	    Item item = dao.findItem(itemId, storeId);

	    // STOCK → SHELF_SEQ
	    int shelfSeq = dao.findShelfSeq(itemId, storeId);

	    // SHELF 情報（ShelfBean）
	    ShelfBean shelf = dao.findShelfBySeq(shelfSeq, storeId);

	    // null 安全対策
	    String shelfId = (shelf != null) ? shelf.getShelfId() : "";
	    String note    = (shelf != null) ? shelf.getNote()    : "";

	    request.setAttribute("item", item);
	    request.setAttribute("currentShelfId", shelfId);
	    request.setAttribute("note", note);

	    request.getRequestDispatcher("/views/editItem.jsp").forward(request, response);
	}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String itemId = request.getParameter("itemId");
        String itemName = request.getParameter("itemName");
        int price = Integer.parseInt(request.getParameter("price"));
        String shelfId = request.getParameter("shelfId");
        String category = request.getParameter("category");
        String note = request.getParameter("note");
        int storeId = (int) request.getSession().getAttribute("storeId");

//        System.out.println("=== UpdateItemServlet.doPost Debug ===");
//        System.out.println("POST itemId = " + itemId);
//        System.out.println("POST itemName = " + itemName);
//        System.out.println("POST price = " + price);
//        System.out.println("POST shelfId = " + shelfId);
//        System.out.println("POST storeId = " + storeId);

        ItemDao dao = new ItemDao();

        // 入力された棚ID → SHELF_SEQ（店舗ごと）
        int shelfSeq = dao.findShelfSeqByShelfId(shelfId, storeId);

//        System.out.println("POST shelfSeq = " + shelfSeq);
//        System.out.println("======================================");

        // 棚が存在しない場合は分類と備考含め新規作成
        if (shelfSeq == -1) {
        	shelfSeq = dao.createShelf(shelfId, category, note, storeId);
        } else {
            // 既存棚なら分類と備考更新
        	dao.updateShelfCategory(shelfSeq, category, storeId);
            dao.updateShelfNote(shelfSeq, note, storeId);
        }

        boolean updated = dao.updateItemAndShelf(itemId, itemName, price, shelfSeq, storeId);

        request.setAttribute("message", updated ? "更新しました" : "更新に失敗しました");

        doGet(request, response);
    }
}