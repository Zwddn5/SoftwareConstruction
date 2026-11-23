package exception;

/**
 * 用户未找到异常.
 */
public class UserNotFoundException extends BusinessException {
  /**
   * 构造方法.
   *
   * @param message 异常信息.
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
