package stockmaster.bean;

import java.sql.Timestamp;

public class HistoryBean {
    private String itemId;
    private String itemName;
    private int storeId;
    private String userId;
    private String userName;
    private String lastActionType;
    private int quantity;
    private Timestamp actionAt;

    public HistoryBean() {}

    // コンストラクタ
    public HistoryBean(String itemId, String itemName, int storeId,
                       String userId, String userName,
                       String lastActionType, int quantity, Timestamp actionAt) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.storeId = storeId;
        this.userId = userId;
        this.userName = userName;
        this.lastActionType = lastActionType;
        this.quantity = quantity;
        this.actionAt = actionAt;
    }

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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastActionType() {
		return lastActionType;
	}

	public void setLastActionType(String lastActionType) {
		this.lastActionType = lastActionType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Timestamp getActionAt() {
		return actionAt;
	}

	public void setActionAt(Timestamp actionAt) {
		this.actionAt = actionAt;
	}

}