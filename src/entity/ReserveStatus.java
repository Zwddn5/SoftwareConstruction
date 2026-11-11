package entity;

public enum ReserveStatus {
    PENDING,    //待审核，刚提交申请
    PASS,      //已通过，审核成功
    REJECT,    //已驳回，管理员拒绝
    CANCEL     //已取消，学生撤销
}
