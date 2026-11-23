package exception;

/**
 * 用户已存在异常.
 */
public class UserExistException extends BusinessException {
  /**
   * 构造方法.
   *
   * @param message 异常信息.
   */
  public UserExistException(String message) {
    super(message);
  }
}
