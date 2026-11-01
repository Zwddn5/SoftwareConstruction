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
        List<CampusResource> resourceList = FileUtil.loadResources();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while(running){
            System.out.println("\n====校园资源预约系统V2.0====");
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("0. 退出");
            System.out.println("请输入你的选择：");
            int choice;
            try{
                choice = scanner.nextInt();
            }catch (Exception e){
                scanner.nextLine();
                System.out.println("输入数字！");
                continue;
            }
            switch (choice) {
                case 1:{
                    try {
                        Login login = new Login();
                        Result<User> loginResult = login.doLogin(userList, scanner);
                        //登录Result一定成功，业务异常已经被catch捕获
                        User loginUser = loginResult.getData();
                        if("admin".equals(loginUser.getRole())){
                            //管理员菜单
                            adminSubMenu(resourceList,scanner);
                        }else{
                            //学生菜单
                            studentSubMenu(resourceList,scanner);
                        }
                    }catch (BusinessException e){
                        Result<?> result = Result.fail(e.getMessage());
                        System.out.println("【业务提示】"+e.getMessage());
                    }
                    break;
                }
                case 2:{
                    try {
                        Register register = new Register();
                        Result<Void> regResult = register.doRegister(userList, scanner);
                        if(regResult.isSuccess()) {
                            System.out.println(regResult.getMsg());
                        }
                    }catch (BusinessException e){
                        Result<?> result = Result.fail(e.getMessage());
                        System.out.println("【业务提示】"+e.getMessage());
                    }
                    break;
                }
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
    /**
     * 管理员二级子菜单
     */
    private static void adminSubMenu(List<CampusResource> resList, Scanner scanner) {
        ResourceManager resourceManager = new ResourceManager();
        boolean adminRun = true;
        while(adminRun){
            System.out.println("\n--------管理员操作菜单--------");
            System.out.println("1 新增校园资源");
            System.out.println("2 查询全部资源");
            System.out.println("3 修改资源");
            System.out.println("4 删除资源");
            System.out.println("5 根据名称模糊查询资源");
            System.out.println("0 返回上层主菜单");
            System.out.print("请输入选择：");
            int opt;
            try{
                opt = scanner.nextInt();
            }catch (Exception e){
                scanner.nextLine();
                System.out.println("请输入数字");
                continue;
            }
            try {
                Result<?> result;
                switch (opt){
                    case 1:
                        result = resourceManager.addResource(resList,"admin",scanner);
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 2:
                        result = resourceManager.queryAllResource(resList,"admin");
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 3:
                        result = resourceManager.updateResource(resList,"admin",scanner);
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 4:
                        result = resourceManager.deleteResource(resList,"admin",scanner);
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 5:{
                        System.out.print("输入查询关键词：");
                        String key = scanner.next();
                        Result<List<CampusResource>> keyResult = resourceManager.queryResourceByKeyword(resList,key);
                        if(keyResult.isSuccess()){
                            List<CampusResource> list = keyResult.getData();
                            if(list.isEmpty()){
                                System.out.println("没有匹配资源");
                            }else{
                                list.forEach(System.out::println);
                            }
                        }else{
                            System.out.println(keyResult.getMsg());
                        }
                        break;
                    }
                    case 0:
                        adminRun = false;
                        System.out.println("返回主菜单");
                        break;
                    default:
                        System.out.println("无效选项");
                }
            }catch (BusinessException e){
                Result<?> failResult = Result.fail(e.getMessage());
                System.out.println("【业务提示】"+failResult.getMsg());
            }
        }
    }

    /**
     * 学生二级子菜单
     */
    private static void studentSubMenu(List<CampusResource> resList, Scanner scanner) {
        ResourceManager resourceManager = new ResourceManager();
        boolean stuRun = true;
        while(stuRun){
            System.out.println("\n--------学生操作菜单--------");
            System.out.println("1 查询可预约资源");
            System.out.println("0 返回上层主菜单");
            System.out.print("请输入选择：");
            int opt;
            try{
                opt = scanner.nextInt();
            }catch (Exception e){
                scanner.nextLine();
                System.out.println("请输入数字");
                continue;
            }
            try {
                Result<?> result;
                switch (opt){
                    case 1:
                        result = resourceManager.queryAvailableResources(resList);
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 0:
                        stuRun = false;
                        System.out.println("返回主菜单");
                        break;
                    default:
                        System.out.println("无效选项");
                }
            }catch (BusinessException e){
                Result<?> failResult = Result.fail(e.getMessage());
                System.out.println("【业务提示】"+failResult.getMsg());
            }
        }
    }
}
