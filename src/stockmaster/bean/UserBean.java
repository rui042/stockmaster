package stockmaster.bean;

public class UserBean {
    private String userId;
    private String password;
    private String name;
    private boolean isStaff;

    public UserBean() {}

    public UserBean(String userId, String password, String name, boolean isStaff) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.isStaff = isStaff;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isStaff() { return isStaff; }
    public void setStaff(boolean isStaff) { this.isStaff = isStaff; }
}