package ui_console;

import entity.User;
import exception.BusinessException;
import result.Result;
import service.UserService;
import java.util.Scanner;

public class MainView {

    private final UserService userService;
    private final AdminView adminView;
    private final StudentView studentView;
    private final Scanner scanner;

    //构造注入依赖
    public MainView(UserService userService, AdminView adminView, StudentView studentView, Scanner scanner) {
        this.userService = userService;
        this.adminView = adminView;
        this.studentView = studentView;
        this.scanner = scanner;
    }

    /**
     * 启动主菜单循环
     */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n====校园资源预约系统V3.1【三层架构】====");
            System.out.println("1. 用户登录");
            System.out.println("2. 用户注册");
            System.out.println("0. 退出系统");
            System.out.print("请输入你的选择：");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("请输入合法数字！");
                continue;
            }

            switch (choice) {
                case 1:
                    //登录
                    try {
                        scanner.nextLine();
                        System.out.println("==== 用户登录 ====");
                        System.out.print("请输入用户名：");
                        String username = scanner.next();
                        System.out.print("请输入密码：");
                        String password = scanner.next();

                        Result<User> loginResult = userService.doLogin(username, password);
                        User loginUser = loginResult.getData();
                        System.out.println(loginResult.getMsg());

                        // 根据角色进入不同子菜单
                        if ("admin".equals(loginUser.getRole())) {
                            adminView.showMenu(scanner, loginUser);
                        } else {
                            studentView.showMenu(scanner, loginUser);
                        }
                    } catch (BusinessException e) {
                        System.out.println("【业务提示】" + e.getMessage());
                    }
                    break;

                case 2:
                    //注册
                    try {
                        scanner.nextLine();
                        System.out.println("==== 用户注册 ====");
                        System.out.print("请输入用户名：");
                        String username = scanner.next();
                        System.out.print("请输入密码：");
                        String password = scanner.next();

                        Result<Void> regResult = userService.doRegister(username, password);
                        System.out.println(regResult.getMsg());
                    } catch (BusinessException e) {
                        System.out.println("【业务提示】" + e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("退出");
                    running = false;
                    break;

                default:
                    System.out.println("无效选项，请重新选择");
            }
        }
    }
}

