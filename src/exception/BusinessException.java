package exception;

/**
 * 业务异常.
 */
public class BusinessException extends RuntimeException {
  /**
   * 构造方法.
   *
   * @param message 异常信息.
   */
  public BusinessException(String message) {
    super(message);
  }
}
