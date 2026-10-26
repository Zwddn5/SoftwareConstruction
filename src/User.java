import java.util.Date;

/**
 * 用户实体类
 * 保存用户账号、密码信息，提供get/set方法与账号比对方法
 * 不变量RI：username不为空、password不为空
 */
public class User {
    private String username;
    private String password;
    private String role;
    private Date registerTime;
    //无参构造方法
    public User(){}
    //有参构造方法
    public User(String username, String password, String role, java.util.Date registerTime) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.registerTime = registerTime;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Date getRegisterTime() {
        return registerTime;
    }
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
    //自定义输出字符串
    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + "," +
                " role=" + role + ", registerTime=" + registerTime + "]";
    }
}
