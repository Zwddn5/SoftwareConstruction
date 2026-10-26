import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
public class SystemMain {
    /**
     * 程序入口主类
     * 提供菜单循环，调度登录、注册功能
     */
    public static void main(String[] args) {
        List<User> userList = FileUtil.loadUsers();
        if (userList.isEmpty()) {
            //添加默认的管理员信息
            User admin = new User(); // 调用无参构造
            admin.setUsername("admin");
            admin.setPassword("123456");
            admin.setRole("admin");
            admin.setRegisterTime(new Date());
            userList.add(admin);
            FileUtil.saveUsers(userList);
            System.out.println("默认管理员信息已添加");
        }
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while(running){
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("0. 退出");
            System.out.println("请输入你的选择：");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Login login = new Login();
                    login.doLogin(userList, scanner);
                    break;
                case 2:
                    Register register = new Register();
                    register.doRegister(userList, scanner);
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
