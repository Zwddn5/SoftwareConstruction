package exception;

/**
 * 密码错误异常.
 */
public class PasswordWrongException extends BusinessException {
  /**
   * 构造方法.
   *
   * @param message 异常信息.
   */
  public PasswordWrongException(String message) {
    super(message);
  }
}
