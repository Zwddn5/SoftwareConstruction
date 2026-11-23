package exception;

/**
 * 参数为空异常.
 */
public class ParamEmptyException extends BusinessException {
  /**
   * 构造方法.
   *
   * @param message 异常信息.
   */
  public ParamEmptyException(String message) {
    super(message);
  }
}
