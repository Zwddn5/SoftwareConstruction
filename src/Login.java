import java.util.List;
import java.util.Scanner;

/**
 * 登录业务类
 * 负责接收输入、校验用户名密码，完成登录逻辑
 */
public class Login {
    public User doLogin(List<User> userList, Scanner scanner) {
        System.out.println("====用户登录====");
        System.out.println("请输入用户名：");
        String inputName = scanner.next();

        if (inputName.isBlank()) {
            throw new ParamEmptyException("用户名不能为空");
        }
        // 1.先查找用户
        User targetUser = null;
        for (User u : userList) {
            if (u.getUsername().equals(inputName)) {
                targetUser = u;
                break;
            }
        }

        // 情况1：用户名不存在，直接结束登录
        if (targetUser == null) {
            throw new UserNotFoundException("用户名不存在");
        }

        // 情况2：用户名存在，输入密码
        System.out.println("请输入密码：");
        String inputPwd = scanner.next();
        if (inputPwd.isBlank()) {
            throw new ParamEmptyException("密码不能为空");
        }
        if (!targetUser.getPassword().equals(inputPwd)) {
            throw new PasswordWrongException("密码错误！登录失败");
        }
        //登录成功
        if ("admin".equals(targetUser.getRole())) {
            System.out.println("管理员登录成功，欢迎管理员：" + inputName);
        } else {
            System.out.println("学生登录成功，欢迎同学：" + inputName);
        }
        System.out.println("当前用户信息：" + targetUser);
        return targetUser;
    }
}
