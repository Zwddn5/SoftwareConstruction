import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SystemMain {
    public static void main(String[] args) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("admin","123456");
        userMap.put("student","123456");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = scanner.next();
        System.out.println("请输入密码：");
        String password = scanner.next();
        if (userMap.containsKey(username)){
            String realPwd = userMap.get(username);
            if(realPwd.equals(password)){
                if (username.equals("admin")) {
                    System.out.println("管理员登录成功，欢迎管理员：" + username);
                } else {
                    System.out.println("学生登录成功，欢迎同学：" + username);
                }
            }else{
                System.out.println("密码错误，请重新登录");
            }
        }else{
            System.out.println("用户名不存在，请重新登录");
        }
        scanner.close();
    }
}
