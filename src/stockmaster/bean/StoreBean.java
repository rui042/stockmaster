package stockmaster.bean;

import java.time.LocalTime;

public class StoreBean {
	  private int storeId;
	  private String storeName;
	  private String storeAddress;
	  private String phone;
      private LocalTime openTime;
      private LocalTime closeTime;
      private boolean openNow;


	  public StoreBean(int storeId, String storeName, String storeAddress, String phone, LocalTime openTime, LocalTime closeTime) {
	    this.storeId = storeId;
	    this.storeName = storeName;
	    this.storeAddress = storeAddress;
	    this.phone = phone;
	    this.openTime = openTime;
	    this.closeTime = closeTime;
	    updateOpenNow(); // インスタンス生成時に営業中判定
	  }

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	public LocalTime getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}

	public boolean isOpenNow() {
		return openNow;
	}

	public void setOpenNow(boolean openNow) {
		this.openNow = openNow;
	}

	// 営業中かどうかを判定するメソッド
    public void updateOpenNow() {
        LocalTime now = LocalTime.now();
        this.openNow = !now.isBefore(openTime) && now.isBefore(closeTime);
    }
}
