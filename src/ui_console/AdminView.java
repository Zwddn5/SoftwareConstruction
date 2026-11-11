package ui_console;

import entity.User;
import exception.BusinessException;
import result.Result;
import service.ResourceService;
import service.ReserveRecordService;
import java.util.List;
import java.util.Scanner;

public class AdminView {
    private final ResourceService resourceService;
    private final ReserveRecordService reserveRecordService;

    //构造注入service
    public AdminView(ResourceService resourceService, ReserveRecordService reserveRecordService) {
        this.resourceService = resourceService;
        this.reserveRecordService = reserveRecordService;
    }

    //传入scanner和当前登录用户，开启管理员菜单循环
    public void showMenu(Scanner scanner, User loginAdmin) {
        boolean run = true;
        while(run){
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
                switch (opt){
                    case 1:{
                        scanner.nextLine();
                        System.out.print("请输入资源名称：");
                        String name = scanner.next();
                        scanner.nextLine();
                        System.out.print("请输入资源类型(教室/实验室/设备/场地)：");
                        String type = scanner.nextLine();
                        System.out.print("请输入位置：");
                        String location = scanner.nextLine();
                        System.out.print("请输入描述：");
                        String desc = scanner.nextLine();
                        Result<Void> res = resourceService.addResource("admin", name, type, location, desc);
                        System.out.println(res.getMsg());
                        break;
                    }
                    case 2:{
                        Result<List<entity.CampusResource>> res = resourceService.queryAllResource("admin");
                        if(res.isSuccess()){
                            List<entity.CampusResource> list = res.getData();
                            if(list.isEmpty()){
                                System.out.println("暂无资源数据");
                            }else{
                                list.forEach(System.out::println);
                            }
                        }else{
                            System.out.println(res.getMsg());
                        }
                        break;
                    }
                    case 0:
                        run = false;
                        System.out.println("返回主菜单");
                        break;
                    default:
                        System.out.println("暂未实现该选项");
                }
            }catch (BusinessException e){
                System.out.println("【业务提示】"+e.getMessage());
            }
        }
    }
}
