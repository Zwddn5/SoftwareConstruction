package result;

/**
 * 用来统一返回的类型.
 *
 * @param <T> 响应数据的类型.
 */
public class Result<T> {
  private boolean success;
  private String msg;
  private T data;

  // 私有构造，只能通过静态工厂创建对象
  private Result() {
  }

  /**
   * 返回成功结果，携带返回数据，默认提示信息：操作成功.
   *
   * @param data 响应数据.
   * @param <T>  响应数据的泛型类型.
   * @return 封装好的成功Result对象.
   */
  // 成功：携带数据
  public static <T> Result<T> success(T data) {
    Result<T> res = new Result<>();
    res.success = true;
    res.msg = "操作成功";
    res.data = data;
    return res;
  }

  /**
   * 返回成功结果，默认提示信息：操作成功.
   *
   * @param msg 提示信息.
   * @param <T> 响应数据的泛型类型.
   * @return 封装好的成功Result对象.
   */
  // 成功：只有提示信息，无数据
  public static <T> Result<T> success(String msg) {
    Result<T> res = new Result<>();
    res.success = true;
    res.msg = msg;
    res.data = null;
    return res;
  }

  /**
   * 返回成功结果，携带返回数据，默认提示信息：操作成功.
   *
   * @param msg  提示信息.
   * @param data 响应数据.
   * @param <T>  响应数据的泛型类型.
   * @return 封装好的成功Result对象.
   */
  public static <T> Result<T> success(String msg, T data) {
    Result<T> res = new Result<>();
    res.success = true;
    res.msg = msg;
    res.data = data;
    return res;
  }

  /**
   * 返回失败结果.
   *
   * @param msg 错误信息.
   * @param <T> 响应数据的泛型类型.
   * @return 封装好的失败Result对象.
   */
  public static <T> Result<T> fail(String msg) {
    Result<T> res = new Result<>();
    res.success = false;
    res.msg = msg;
    res.data = null;
    return res;
  }

  /**
   * 获取响应是否成功.
   *
   * @return 布尔值，表示响应是否成功.
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * 设置响应是否成功.
   *
   * @param success 布尔值，表示响应是否成功.
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }

  /**
   * 获取响应的提示信息.
   *
   * @return 响应的提示信息.
   */
  public String getMsg() {
    return msg;
  }

  /**
   * 设置响应的提示信息.
   *
   * @param msg 提示信息.
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }

  /**
   * 获取响应数据.
   *
   * @return 响应数据.
   */
  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "result.Result{"
        +
        "success=" + success
        +
        ", msg='" + msg + '\''
        +
        ", data=" + data
        +
        '}';
  }
}
