import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SystemMain {
    public static void main(String[] args) {
        Map<String, String> userMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        Boolean running = true;
        while(running){
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("0. 退出");
            System.out.println("请输入你的选择：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Login login = new Login();
                    login.doLogin(userMap, scanner);
                    break;
                case 2:
                    Register register = new Register();
                    register.doRegister(userMap, scanner);
                    break;
                case 0:
                    System.out.println("退出系统");
                    running = false;
                    break;
                default:
                    System.out.println("无效的选择，请重新输入");
            }
        }
        scanner.close();
    }
}
