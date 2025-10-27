package stockmaster.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/showMap")
public class ShowMapServlet extends HttpServlet {

    public static class Hotspot {
        public String id;
        public String title;
        public double xPct;
        public double yPct;
        public double wPct;
        public double hPct;
        public String desc;
        public Hotspot(String id, String title, double xPct, double yPct, double wPct, double hPct, String desc){
            this.id=id; this.title=title; this.xPct=xPct; this.yPct=yPct; this.wPct=wPct; this.hPct=hPct; this.desc=desc;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Hotspot> hotspots = new ArrayList<>();
        hotspots.add(new Hotspot("s1","倉庫A", 12.5, 18.0, 8.0, 6.0, "在庫:120"));
        hotspots.add(new Hotspot("s2","店舗B", 40.0, 20.0, 9.0, 7.0, "営業中"));
        hotspots.add(new Hotspot("s3","配送C", 70.0, 55.0, 10.0, 8.0, "入荷予定"));

        req.setAttribute("hotspots", hotspots);
        req.setAttribute("floorImage", req.getContextPath() + "/resources/floorplan.png");

        // あなたの構成に一致（WebContent/views/showmap.jsp）
        req.getRequestDispatcher("/views/showmap.jsp").forward(req, resp);
    }
}