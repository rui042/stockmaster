package stockmaster.bean;

import java.io.Serializable;

public class ShelfBean implements Serializable {

    private int shelfSeq;     // 棚順序（DBのSHELF_SEQに対応）
    private String shelfId;   // 棚ID
    private String location;  // 位置情報
    private int storeId;      // 店舗ID
    private String category;  // 分類
    private String note;      // 備考

    // 座標情報（フロア図上の位置 %）
    private Double xPct;     // X座標（%）
    private Double yPct;     // Y座標（%）

    public ShelfBean() {}

    public ShelfBean(int shelfSeq, String shelfId, String location, int storeId,
                     String category, String note, Double xPct, Double yPct) {
        this.shelfSeq = shelfSeq;
        this.shelfId = shelfId;
        this.location = location;
        this.storeId = storeId;
        this.category = category;
        this.note = note;
        this.xPct = xPct;
        this.yPct = yPct;
    }

    public ShelfBean(String shelfId, String location, int storeId, String category, String note) {
        this.shelfId = shelfId;
        this.location = location;
        this.storeId = storeId;
               this.category = category;
        this.note = note;
    }

    public int getShelfSeq() { return shelfSeq; }
    public void setShelfSeq(int shelfSeq) { this.shelfSeq = shelfSeq; }

    public String getShelfId() { return shelfId; }
    public void setShelfId(String shelfId) { this.shelfId = shelfId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getStoreId() { return storeId; }
    public void setStoreId(int storeId) { this.storeId = storeId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Double getXPct() { return xPct; }
    public void setXPct(Double xPct) { this.xPct = xPct; }

    public Double getYPct() { return yPct; }
    public void setYPct(Double yPct) { this.yPct = yPct; }

    @Override
    public String toString() {
        return "ShelfBean [shelfSeq=" + shelfSeq +
               ", shelfId=" + shelfId +
               ", location=" + location +
               ", storeId=" + storeId +
               ", category=" + category +
               ", note=" + note +
               ", xPct=" + xPct +
               ", yPct=" + yPct + "]";
    }
}