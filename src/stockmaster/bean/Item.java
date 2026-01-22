package stockmaster.bean;

public class Item {
    private String id;
    private String name;
    private int price;
    private String shelfId;
    private String category;

    public Item() {}

    public Item(String id, String name, int price,String shelfId, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.shelfId = shelfId;
        this.category = category;
    }

    public String getId() {
    	return id;
    }

    public void setId(String id) {
    	this.id = id;
    }

    public String getName() {
    	return name;
    }
    public void setName(String name) {
    	this.name = name;
    }

    public int getPrice() {
    	return price;
    }

    public void setPrice(int price) {
    	this.price = price;
    }

	public String getCategory() {
		return category;
	}

	public String getShelfId() {
		return shelfId;
	}

	public void setShelfId(String shelfId) {
		this.shelfId = shelfId;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}