package ui_console;

import entity.CampusResource;
import entity.ReserveRecord;
import entity.User;
import exception.BusinessException;
import result.Result;
import service.ResourceService;
import service.ReserveRecordService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class StudentView {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") ;
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
                    case 2:{
                        scanner.nextLine();
                        System.out.print("请输入要预约的资源ID：");
                        String resId = scanner.nextLine();

                        System.out.println("请输入预约开始时间（格式 yyyy‑MM‑dd HH:mm:ss）");
                        System.out.print("开始时间：");
                        String startStr = scanner.nextLine();

                        System.out.println("请输入预约结束时间（格式 yyyy‑MM‑dd HH:mm:ss）");
                        System.out.print("结束时间：");
                        String endStr = scanner.nextLine();

                        System.out.print("预约备注：");
                        String remark = scanner.nextLine();

                        //解析时间字符串
                        LocalDateTime start;
                        LocalDateTime end;
                        try{
                            start = LocalDateTime.parse(startStr, FORMATTER);
                            end = LocalDateTime.parse(endStr, FORMATTER);
                        }catch (DateTimeParseException e){
                            System.out.println("【时间格式错误】请严格按照 yyyy‑MM‑dd HH:mm:ss 输入！");
                            break;
                        }

                        Result<ReserveRecord> applyResult = reserveRecordService.submitApply(
                                loginStudent.getUsername(),
                                resId,
                                start,
                                end,
                                remark
                        );
                        System.out.println(applyResult.getMsg());
                        break;
                    }
                    case 3:{
                        Result<List<ReserveRecord>> myRes = reserveRecordService.queryMyReserve(loginStudent.getUsername());
                        if(myRes.isSuccess()){
                            List<ReserveRecord> list = myRes.getData();
                            if(list.isEmpty()){
                                System.out.println("你暂无任何预约记录");
                            }else{
                                list.forEach(System.out::println);
                            }
                        }
                        break;
                    }
                    case 4:{
                        scanner.nextLine();
                        System.out.print("请输入需要取消的预约记录ID：");
                        String recordId = scanner.nextLine();
                        Result<Void> cancelRes = reserveRecordService.cancelReserve(loginStudent.getUsername(), recordId);
                        System.out.println(cancelRes.getMsg());
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
