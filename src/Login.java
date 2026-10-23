import java.util.Map;
import java.util.Scanner;

public class Login {
    public void doLogin(Map<String,String> userMap, Scanner scanner){
        System.out.println("====用户登录====");
        System.out.println("请输入用户名：");
        String username = scanner.next();
        System.out.println("请输入密码：");
        String password = scanner.next();
        if(username.isBlank() || password.isBlank())
            System.out.println("用户名或密码不能为空，请重新输入");
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
    }
}
