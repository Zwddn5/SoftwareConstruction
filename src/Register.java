import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Register {
    public void doRegister(List<User> userList, Scanner scanner) {
        System.out.println("====用户注册====");
        System.out.println("请输入新的用户名：");
        String newUser = scanner.next();
        if (newUser.isBlank()) {
            System.out.println("用户名不能为空");
            return;
        }
        if("admin".equals(newUser)) {
            System.out.println("管理员账号不可以注册");
            return;
        }
        for (User user : userList)
            if (user.getUsername().equals(newUser)) {
                System.out.println("用户名已存在，请重新注册");
                return;
            }
        System.out.println("请输入新的密码：");
        String newPwd = scanner.next();
        if (newPwd == null || newPwd.length() < 6) {
            System.out.println("密码不能为空且长度不能小于6位");
            return;
        }
        User user = new User();
        user.setUsername(newUser);
        user.setPassword(newPwd);
        user.setRole("student");
        user.setRegisterTime(new Date());
        userList.add(user);
        System.out.println("注册成功!");
        System.out.println("用户信息"+user);
    }
}