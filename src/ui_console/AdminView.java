package ui_console;

import entity.CampusResource;
import entity.ReserveRecord;
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
                        Result<Void> res = resourceService.addResource(loginAdmin.getRole(), name, type, location, desc);
                        System.out.println(res.getMsg());
                        break;
                    }
                    case 2:{
                        Result<List<entity.CampusResource>> res = resourceService.queryAllResource(loginAdmin.getRole());
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
                    //3 修改资源
                    case 3:{
                        scanner.nextLine();
                        System.out.print("请输入要修改的资源ID：");
                        String editId = scanner.nextLine();

                        System.out.print("新资源名称(留空不修改)：");
                        String newName = scanner.nextLine();
                        System.out.print("新资源类型(留空不修改)：");
                        String newType = scanner.nextLine();
                        System.out.print("新位置(留空不修改)：");
                        String newLoc = scanner.nextLine();
                        System.out.println("状态：1-可预约  2-占用  3-停用；直接回车不修改");
                        System.out.print("请输入状态选项：");
                        String statusInput = scanner.nextLine();
                        Integer statusOpt = null;
                        if(!statusInput.isBlank()){
                            statusOpt = Integer.parseInt(statusInput);
                        }
                        System.out.print("新描述(留空不修改)：");
                        String newDesc = scanner.nextLine();

                        Result<Void> updateResult = resourceService.updateResource(
                                loginAdmin.getRole(),editId,newName,newType,newLoc,statusOpt,newDesc
                        );
                        System.out.println(updateResult.getMsg());
                        break;
                    }
                    //4 删除资源
                    case 4:{
                        scanner.nextLine();
                        System.out.print("请输入要删除的资源ID：");
                        String delId = scanner.nextLine();
                        Result<Void> delRes = resourceService.deleteResource(loginAdmin.getRole(), delId);
                        System.out.println(delRes.getMsg());
                        break;
                    }
                    //5 模糊查询资源
                    case 5:{
                        scanner.nextLine();
                        System.out.print("请输入查询关键词：");
                        String keyword = scanner.nextLine();
                        Result<List<CampusResource>> queryRes = resourceService.queryResourceByKeyword(keyword);
                        if(queryRes.isSuccess()){
                            List<CampusResource> list = queryRes.getData();
                            if(list.isEmpty()){
                                System.out.println("没有匹配的资源");
                            }else{
                                list.forEach(System.out::println);
                            }
                        }
                        break;
                    }
                    //6 查询全部预约记录
                    case 6:{
                        Result<List<ReserveRecord>> allReserve = reserveRecordService.queryAllReserve(loginAdmin.getRole());
                        if(allReserve.isSuccess()){
                            List<ReserveRecord> list = allReserve.getData();
                            if(list.isEmpty()){
                                System.out.println("暂无预约记录");
                            }else{
                                list.forEach(System.out::println);
                            }
                        }else{
                            System.out.println(allReserve.getMsg());
                        }
                        break;
                    }
                    //7 审核通过
                    case 7:{
                        scanner.nextLine();
                        System.out.print("输入待审核预约记录ID：");
                        String recordId = scanner.nextLine();
                        Result<Void> passRes = reserveRecordService.auditPass(loginAdmin.getRole(), recordId);
                        System.out.println(passRes.getMsg());
                        break;
                    }
                    //8 审核驳回
                    case 8:{
                        scanner.nextLine();
                        System.out.print("输入待审核预约记录ID：");
                        String recordId = scanner.nextLine();
                        Result<Void> rejectRes = reserveRecordService.auditReject(loginAdmin.getRole(), recordId);
                        System.out.println(rejectRes.getMsg());
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
