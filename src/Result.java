public class Result<T> {
    private boolean success;
    private String msg;
    private T data;

    //私有构造，只能通过静态工厂创建对象
    private Result(){}

    //成功：携带数据
    public static <T> Result<T> success(T data){
        Result<T> res = new Result<>();
        res.success = true;
        res.msg = "操作成功";
        res.data = data;
        return res;
    }
    //成功：只有提示信息，无数据
    public static <T> Result<T> success(String msg){
        Result<T> res = new Result<>();
        res.success = true;
        res.msg = msg;
        res.data = null;
        return res;
    }
    //成功：自定义消息 + 携带数据
    public static <T> Result<T> success(String msg, T data){
        Result<T> res = new Result<>();
        res.success = true;
        res.msg = msg;
        res.data = data;
        return res;
    }
    //失败：错误信息
    public static <T> Result<T> fail(String msg){
        Result<T> res = new Result<>();
        res.success = false;
        res.msg = msg;
        res.data = null;
        return res;
    }

    //getter & setter
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
