import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReserveManager {
    /**
     * 学生提交预约申请
     * @param reserveList 全部预约记录
     * @param resourceList 全部校园资源
     * @param applicantUsername 当前登录学生用户名
     * @param resId 要预约的资源ID
     * @param start 预约开始时间
     * @param end 预约结束时间
     * @param remark 预约备注
     * @return Result<ReserveRecord>
     */
    public Result<ReserveRecord> submitApply(List<ReserveRecord> reserveList,
                                             List<CampusResource> resourceList,
                                             String applicantUsername,
                                             String resId,
                                             LocalDateTime start,
                                             LocalDateTime end,
                                             String remark){
        //1.校验资源是否存在
        Optional<CampusResource> optRes = resourceList.stream()
                .filter(r -> r.getId().equals(resId))
                .findFirst();
        if(optRes.isEmpty()){
            return Result.fail("预约资源不存在");
        }
        CampusResource resource = optRes.get();
        //2.只能预约状态AVAILABLE可预约的资源
        if(!ResourceStatus.AVAILABLE.equals(resource.getStatus())){
            return Result.fail("该资源不可预约（占用/停用）");
        }
        //3.时间基础校验：开始不能晚于结束；预约时间不能早于当前时刻
        if(start.isAfter(end)){
            return Result.fail("预约开始时间不能晚于结束时间");
        }
        if(start.isBefore(LocalDateTime.now())){
            return Result.fail("预约时间不能早于当前时间");
        }
        //4.时间冲突校验：该资源已经存在状态为PASS的预约，时间段重叠，禁止提交
        List<ReserveRecord> passReserveList = reserveList.stream()
                .filter(r -> resId.equals(r.getResourceId()))
                .filter(r -> ReserveStatus.PASS.equals(r.getStatus()))
                .collect(Collectors.toList());

        for(ReserveRecord passRec : passReserveList){
            //时间段重叠判断：!(结束早于对方开始 || 开始晚于对方结束) → 代表冲突
            boolean conflict = !(end.isBefore(passRec.getReserveStartTime()) || start.isAfter(passRec.getReserveEndTime()));
            if(conflict){
                return Result.fail("预约时间段与已通过预约发生冲突，无法提交申请");
            }
        }
        //5.构造预约记录，状态默认PENDING待审核
        String recordId = "RE" + UUID.randomUUID().toString().substring(0,6).toUpperCase();
        ReserveRecord record = new ReserveRecord();
        record.setRecordId(recordId);
        record.setResourceId(resId);
        record.setApplicantUsername(applicantUsername);
        record.setApplyTime(LocalDateTime.now());
        record.setReserveStartTime(start);
        record.setReserveEndTime(end);
        record.setStatus(ReserveStatus.PENDING);
        record.setRemark(remark);

        reserveList.add(record);
        FileUtil.saveReserveRecords(reserveList);
        return Result.success("预约申请提交成功，等待管理员审核", record);
    }

    /**
     * 学生：查询我的全部预约记录
     */
    public Result<List<ReserveRecord>> queryMyReserve(List<ReserveRecord> reserveList, String loginUsername){
        List<ReserveRecord> myList = reserveList.stream()
                .filter(r -> loginUsername.equals(r.getApplicantUsername()))
                .collect(Collectors.toList());
        return Result.success("查询我的预约成功",myList);
    }

    /**
     * 学生：取消预约
     * 允许取消：PENDING待审核、PASS已通过；REJECT、CANCEL不可取消；只能取消自己的预约
     */
    public Result<Void> cancelReserve(List<ReserveRecord> reserveList,
                                      List<CampusResource> resourceList,
                                      String loginUsername,
                                      String recordId){
        Optional<ReserveRecord> optRec = reserveList.stream()
                .filter(r -> recordId.equals(r.getRecordId()))
                .findFirst();
        if(optRec.isEmpty()){
            return Result.fail("预约记录不存在");
        }
        ReserveRecord rec = optRec.get();
        //权限校验：不能取消别人的预约
        if(!rec.getApplicantUsername().equals(loginUsername)){
            throw new PermissionDeniedException("无权取消他人预约记录");
        }
        ReserveStatus status = rec.getStatus();
        //只有PENDING、PASS可以取消
        if(status == ReserveStatus.REJECT || status == ReserveStatus.CANCEL){
            return Result.fail("当前预约状态不支持取消");
        }
        //状态改为CANCEL已取消
        rec.setStatus(ReserveStatus.CANCEL);
        //业务联动：如果原来状态是PASS已通过 → 资源恢复AVAILABLE可预约
        if(ReserveStatus.PASS.equals(status)){
            Optional<CampusResource> optRes = resourceList.stream()
                    .filter(r -> r.getId().equals(rec.getResourceId()))
                    .findFirst();
            if(optRes.isPresent()){
                optRes.get().setStatus(ResourceStatus.AVAILABLE);
                FileUtil.saveResources(resourceList);
            }
        }
        FileUtil.saveReserveRecords(reserveList);
        return Result.success("预约取消成功");
    }

    // ==========管理员功能==========
    /**
     * 管理员查询全部预约记录
     */
    public Result<List<ReserveRecord>> queryAllReserve(List<ReserveRecord> reserveList, String loginRole){
        if(!"admin".equals(loginRole)){
            throw new PermissionDeniedException("权限不足，只有管理员可以查看全部预约");
        }
        return Result.success("查询全部预约记录成功",reserveList);
    }

    /**
     * 管理员审核通过预约
     * 仅PENDING待审核可以通过；审核通过资源置OCCUPIED占用
     */
    public Result<Void> auditPass(List<ReserveRecord> reserveList,
                                  List<CampusResource> resourceList,
                                  String loginRole,
                                  String recordId){
        if(!"admin".equals(loginRole)){
            throw new PermissionDeniedException("权限不足，只有管理员可以执行审核");
        }
        Optional<ReserveRecord> optRec = reserveList.stream()
                .filter(r -> recordId.equals(r.getRecordId()))
                .findFirst();
        if(optRec.isEmpty()){
            return Result.fail("预约记录不存在");
        }
        ReserveRecord rec = optRec.get();
        if(!ReserveStatus.PENDING.equals(rec.getStatus())){
            return Result.fail("仅待审核(PENDING)状态预约可以审核通过");
        }
        //修改预约状态为PASS已通过
        rec.setStatus(ReserveStatus.PASS);
        //联动：资源修改为OCCUPIED占用
        Optional<CampusResource> optRes = resourceList.stream()
                .filter(r -> r.getId().equals(rec.getResourceId()))
                .findFirst();
        if(optRes.isPresent()){
            optRes.get().setStatus(ResourceStatus.OCCUPIED);
            FileUtil.saveResources(resourceList);
        }
        FileUtil.saveReserveRecords(reserveList);
        return Result.success("审核通过成功，资源已设置为占用");
    }

    /**
     * 管理员审核驳回预约
     * 仅PENDING待审核可以驳回；资源状态不变
     */
    public Result<Void> auditReject(List<ReserveRecord> reserveList, String loginRole, String recordId){
        if(!"admin".equals(loginRole)){
            throw new PermissionDeniedException("权限不足，只有管理员可以执行审核");
        }
        Optional<ReserveRecord> optRec = reserveList.stream()
                .filter(r -> recordId.equals(r.getRecordId()))
                .findFirst();
        if(optRec.isEmpty()){
            return Result.fail("预约记录不存在");
        }
        ReserveRecord rec = optRec.get();
        if(!ReserveStatus.PENDING.equals(rec.getStatus())){
            return Result.fail("仅待审核(PENDING)状态预约可以驳回");
        }
        rec.setStatus(ReserveStatus.REJECT);
        FileUtil.saveReserveRecords(reserveList);
        return Result.success("预约已驳回");
    }
}

