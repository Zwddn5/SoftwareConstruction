package ui_console;

import entity.CampusResource;
import entity.User;
import exception.BusinessException;
import result.Result;
import service.ResourceService;
import service.ReserveRecordService;

import java.util.List;
import java.util.Scanner;

public class StudentView {
    private final ResourceService resourceService;
    private final ReserveRecordService reserveRecordService;

    public StudentView(ResourceService resourceService, ReserveRecordService reserveRecordService) {
        this.resourceService = resourceService;
        this.reserveRecordService = reserveRecordService;
    }

    public void showMenu(Scanner scanner, User loginStudent) {
        boolean run = true;
        while(run){
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
                switch (opt){
                    case 1:{
                        Result<List<entity.CampusResource>> res = resourceService.queryAvailableResources();
                        if(res.isSuccess()){
                            List<CampusResource> list = res.getData();
                            if(list.isEmpty()){
                                System.out.println("暂无可预约资源");
                            }else{
                                list.forEach(System.out::println);
                            }
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
