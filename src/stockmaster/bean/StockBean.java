package stockmaster.bean;

import java.io.Serializable;

public class StockBean implements Serializable {

    private String itemId;    // 商品ID
    private String itemName;  // 商品名（ITEMSテーブルから取得）
    private String genre;     // 商品分類（ITEMSテーブルから取得）
    private String shelfId;   // 棚ID
    private int storeId;      // 店舗ID
    private int stockNow;     // 現在の在庫数
    private int stockMin;     // 最低在庫数

    // 🔽 追加：価格（ITEMSテーブルから取得）
    private int price;

    // コンストラクタ
    public StockBean() {}

    public StockBean(String itemId, String itemName, String genre,
                     String shelfId, int storeId, int stockNow, int stockMin) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.genre = genre;
        this.shelfId = shelfId;
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

    public String getShelfId() {
        return shelfId;
    }
    public void setShelfId(String shelfId) {
        this.shelfId = shelfId;
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

    // 🔽 追加：価格のGetter / Setter
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
               ", shelfId=" + shelfId +
               ", storeId=" + storeId +
               ", stockNow=" + stockNow +
               ", stockMin=" + stockMin +
               ", price=" + price + "]";
    }
}