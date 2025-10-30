package stockmaster.bean;

public class ShowMapBean {
	private String shelfId;
    private String itemName;
    private int price;
    private int stockNow;
    private int stockMin;

    // コンストラクタ
    public ShowMapBean(String shelfId, String itemName, int price, int stockNow, int stockMin) {
        this.shelfId = shelfId;
        this.itemName = itemName;
        this.price = price;
        this.stockNow = stockNow;
        this.stockMin = stockMin;
}

	public String getShelfId() {
		return shelfId;
	}

	public void setShelfId(String shelfId) {
		this.shelfId = shelfId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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
}

