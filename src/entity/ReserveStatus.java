package entity;

/**
 * 预约状态枚举类.
 * PENDING 待审核, PASS 审核通过, REJECT 审核驳回, CANCEL 预约取消.
 */
public enum ReserveStatus {
  PENDING,
  PASS,
  REJECT,
  CANCEL
}
