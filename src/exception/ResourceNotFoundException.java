package exception;

/**
 * 资源未找到异常.
 */
public class ResourceNotFoundException extends BusinessException {
  /**
   * 构造方法.
   *
   * @param message 异常信息.
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
