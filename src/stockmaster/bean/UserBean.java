package stockmaster.bean;

public class UserBean {
    private String userId;
    private String password;
    private String name;
    private String email;   // ★追加
    private boolean isStaff;

    public UserBean() {}

    // コンストラクタ（email あり）
    public UserBean(String userId, String password, String name, String email, boolean isStaff) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.isStaff = isStaff;
    }

    // 既存のコンストラクタ（email なし）も残しておくと互換性が保てます
    public UserBean(String userId, String password, String name, boolean isStaff) {
        this(userId, password, name, null, isStaff);
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }   // ★追加
    public void setEmail(String email) { this.email = email; }

    public boolean isStaff() { return isStaff; }
    public void setStaff(boolean isStaff) { this.isStaff = isStaff; }
}