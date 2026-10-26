import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * 注册业务类
 * 校验账号是否重复，合法则新增用户到集合
 */
public class Register {
    public void doRegister(List<User> userList, Scanner scanner) {
        System.out.println("====用户注册====");
        System.out.println("请输入新的用户名：");
        String newUser = scanner.next();
        if (newUser.isBlank()) {
            throw new ParamEmptyException("用户名不能为空");
        }
        if("admin".equals(newUser)) {
            throw new UserExistException("管理员账号不可以注册");
        }
        for (User user : userList)
            if (user.getUsername().equals(newUser)) {
                throw new UserExistException("用户名已存在，请重新注册");
            }
        System.out.println("请输入新的密码：");
        String newPwd = scanner.next();
        if (newPwd == null || newPwd.length() < 6) {
            throw new ParamEmptyException("密码不能为空且长度不能小于6位");
        }
        User user = new User();
        user.setUsername(newUser);
        user.setPassword(newPwd);
        user.setRole("student");
        user.setRegisterTime(new Date());
        userList.add(user);
        FileUtil.saveUsers(userList);
        System.out.println("注册成功!");
        System.out.println("用户信息"+user);
    }
}