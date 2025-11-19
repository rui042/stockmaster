package stockmaster.bean;

import java.io.Serializable;

public class StockBean implements Serializable {

    private String itemId;     // 商品ID
    private String itemName;   // 商品名（ITEMSテーブルから取得）
    private String genre;      // 商品分類（ITEMSテーブルから取得）
    private int shelfSeq;      // 棚順序（SHELFテーブルの主キー）
    private int storeId;       // 店舗ID
    private int stockNow;      // 現在の在庫数
    private int stockMin;      // 最低在庫数
    private int price;         // 価格（ITEMSテーブルから取得）

    // コンストラクタ
    public StockBean() {}

    public StockBean(String itemId, String itemName, String genre,
                     int shelfSeq, int storeId, int stockNow, int stockMin) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.genre = genre;
        this.shelfSeq = shelfSeq;
        this.storeId = storeId;
        this.stockNow = stockNow;
        this.stockMin = stockMin;
    }

    // Getter / Setter
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getShelfSeq() {
        return shelfSeq;
    }
    public void setShelfSeq(int shelfSeq) {
        this.shelfSeq = shelfSeq;
    }

    public int getStoreId() {
        return storeId;
    }
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getStockNow() {
        return stockNow;
    }
    public void setStockNow(int stockNow) {
        this.stockNow = stockNow;
    }

    public int getStockMin() {
        return stockMin;
    }
    public void setStockMin(int stockMin) {
        this.stockMin = stockMin;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockBean [itemId=" + itemId +
               ", itemName=" + itemName +
               ", genre=" + genre +
               ", shelfSeq=" + shelfSeq +
               ", storeId=" + storeId +
               ", stockNow=" + stockNow +
               ", stockMin=" + stockMin +
               ", price=" + price + "]";
    }
}