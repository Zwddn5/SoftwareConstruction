import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
public class SystemMain {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        List<ReserveRecord> reserveList = FileUtil.loadReserveRecords();

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
                            adminSubMenu(resourceList, reserveList, scanner);
                        }else{
                            //学生菜单
                            studentSubMenu(resourceList, reserveList, loginUser, scanner);
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
    private static void adminSubMenu(List<CampusResource> resourcesList,
                                     List<ReserveRecord> reserveList, Scanner scanner) {
        ResourceManager resourceManager = new ResourceManager();
        ReserveManager reserveManager = new ReserveManager();
        boolean adminRun = true;
        while(adminRun){
            System.out.println("\n--------管理员操作菜单--------");
            System.out.println("1 新增校园资源");
            System.out.println("2 查询全部资源");
            System.out.println("3 修改资源");
            System.out.println("4 删除资源");
            System.out.println("5 根据名称模糊查询资源");
            System.out.println("6 查询全部预约记录");
            System.out.println("7 预约审核【通过】");
            System.out.println("8 预约审核【驳回】");
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
                        result = resourceManager.addResource(resourcesList,"admin",scanner);
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 2:
                        result = resourceManager.queryAllResource(resourcesList,"admin");
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 3:
                        result = resourceManager.updateResource(resourcesList,"admin",scanner);
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 4:
                        result = resourceManager.deleteResource(resourcesList,"admin",scanner);
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 5:{
                        System.out.print("输入查询关键词：");
                        String key = scanner.next();
                        Result<List<CampusResource>> keyResult = resourceManager.queryResourceByKeyword(resourcesList,key);
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
                    case 6:
                        result = reserveManager.queryAllReserve(reserveList,"admin");
                        if(result.isSuccess()){
                            List<ReserveRecord> allRes = (List<ReserveRecord>) result.getData();
                            if(allRes.isEmpty()){
                                System.out.println("暂无预约记录");
                            }else {
                                allRes.forEach(System.out::println);
                            }
                        }else{
                            System.out.println(result.getMsg());
                        }
                        break;
                    case 7:
                        scanner.nextLine();
                        System.out.print("输入要审核通过的预约recordId：");
                        String passId = scanner.nextLine();
                        result = reserveManager.auditPass(reserveList, resourcesList, "admin", passId);
                        System.out.println(result.getMsg());
                        break;
                    case 8:
                        scanner.nextLine();
                        System.out.print("输入要审核驳回的预约recordId：");
                        String rejectId = scanner.nextLine();
                        result = reserveManager.auditReject(reserveList,"admin",rejectId);
                        System.out.println(result.getMsg());
                        break;
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
     * 学生二级子菜单 V3.0更新
     * @param resourcesList 资源集合
     * @param reserveList 预约集合
     * @param loginUser 当前登录学生用户对象
     * @param scanner 输入
     */
    private static void studentSubMenu(List<CampusResource> resourcesList,
                                       List<ReserveRecord> reserveList, User loginUser,
                                       Scanner scanner) {
        ResourceManager resourceManager = new ResourceManager();
        ReserveManager reserveManager = new ReserveManager();
        boolean stuRun = true;
        while(stuRun){
            System.out.println("\n--------学生操作菜单--------");
            System.out.println("1 查询可预约资源");
            System.out.println("2 提交资源预约申请");
            System.out.println("3 查询我的预约列表");
            System.out.println("4 取消我的预约");
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
                        result = resourceManager.queryAvailableResources(resourcesList);
                        if (!result.isSuccess()) System.out.println(result.getMsg());
                        break;
                    case 2:{
                        //业务方法**绝不接收 Scanner**，不做任何`System.out`打印。
                        //登录和注册逻辑需要修改
                        scanner.nextLine();
                        System.out.print("输入要预约的资源ID：");
                        String resId = scanner.nextLine();
                        System.out.print("输入预约开始时间(格式 yyyy‑MM‑dd HH:mm:ss)：");
                        String startStr = scanner.nextLine();
                        System.out.print("输入预约结束时间(格式 yyyy‑MM‑dd HH:mm:ss)：");
                        String endStr = scanner.nextLine();
                        System.out.print("输入预约备注：");
                        String remark = scanner.nextLine();

                        LocalDateTime start;
                        LocalDateTime end;
                        try {
                            start = LocalDateTime.parse(startStr,FORMATTER);
                            end = LocalDateTime.parse(endStr,FORMATTER);
                        }catch (Exception ex){
                            System.out.println("【业务提示】时间格式错误！");
                            break;
                        }
                        Result<ReserveRecord> applyResult = reserveManager.submitApply(
                                reserveList,
                                resourcesList,
                                loginUser.getUsername(),
                                resId,
                                start,
                                end,
                                remark
                        );
                        System.out.println(applyResult.getMsg());
                        break;
                    }
                    case 3:{
                        result = reserveManager.queryMyReserve(reserveList,loginUser.getUsername());
                        if(result.isSuccess()){
                            List<ReserveRecord> myRes = (List<ReserveRecord>) result.getData();
                            if(myRes.isEmpty()){
                                System.out.println("你暂无预约记录");
                            }else{
                                myRes.forEach(System.out::println);
                            }
                        }else{
                            System.out.println(result.getMsg());
                        }
                        break;
                    }
                    case 4:{
                        scanner.nextLine();
                        System.out.print("输入要取消的预约recordId：");
                        String cancelRecordId = scanner.nextLine();
                        Result<Void> cancelResult = reserveManager.cancelReserve(
                                reserveList,
                                resourcesList,
                                loginUser.getUsername(),
                                cancelRecordId
                        );
                        System.out.println(cancelResult.getMsg());
                        break;
                    }
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
