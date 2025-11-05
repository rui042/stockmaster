package stockmaster.bean;

import java.io.Serializable;

public class StockBean implements Serializable {

    private String itemId;   // 商品ID
    private String shelfId;  // 棚ID
    private int storeId;     // 店舗ID
    private int stockNow;    // 現在の在庫数
    private int stockMin;    // 最低在庫数

    // コンストラクタ
    public StockBean() {}

    public StockBean(String itemId, String shelfId, int storeId, int stockNow, int stockMin) {
        this.itemId = itemId;
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

    @Override
    public String toString() {
        return "StockBean [itemId=" + itemId + ", shelfId=" + shelfId +
               ", storeId=" + storeId + ", stockNow=" + stockNow +
               ", stockMin=" + stockMin + "]";
    }
}
