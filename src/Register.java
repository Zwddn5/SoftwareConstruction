import java.util.Map;
import java.util.Scanner;

public class Register {
    public void doRegister(Map<String, String> userMap, Scanner scanner) {
        System.out.println("====用户注册====");
        System.out.println("请输入新的用户名：");
        String newUser = scanner.next();
        if (userMap.containsKey(newUser))
            System.out.println("用户名已存在，请重新注册");
        else {
            System.out.println("请输入新的密码：");
            String newPwd = scanner.next();
            if (newPwd == null || newPwd.length() < 6)
                System.out.println("密码不能为空且长度不能小于6位");
            else {
                userMap.put(newUser, newPwd);
                System.out.println("注册成功！");
            }
        }
    }
}