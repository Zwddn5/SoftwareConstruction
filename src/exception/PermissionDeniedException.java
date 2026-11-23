package exception;

/**
 * 权限拒绝异常.
 */
public class PermissionDeniedException extends BusinessException {
  /**
   * 构造方法.
   *
   * @param message 异常信息.
   */
  public PermissionDeniedException(String message) {
    super(message);
  }
}
