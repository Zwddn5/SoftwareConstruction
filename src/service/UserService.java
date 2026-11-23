package service;

import dao.UserDao;
import entity.User;
import exception.ParamEmptyException;
import exception.PasswordWrongException;
import exception.UserExistException;
import exception.UserNotFoundException;
import java.util.Date;
import java.util.List;
import result.Result;

/**
 * 用户服务层.
 */

public class UserService {
  private final UserDao userDao;

  /**
   * 构造方法.
   */

  public UserService() {
    this.userDao = new UserDao();
  }

  /**
   * 用户登录.
   *
   * @param inputName 用户名.
   * @param inputPwd  密码.
   * @return 返回result包装的用户对象.
   */

  public Result<User> doLogin(String inputName, String inputPwd) {
    if (inputName.isBlank()) {
      throw new ParamEmptyException("用户名不能为空");
    }
    // 1.先查找用户
    List<User> userList = userDao.findAll();
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

    // 情况2：密码为空
    if (inputPwd.isBlank()) {
      throw new ParamEmptyException("密码不能为空");
    }
    if (!targetUser.getPassword().equals(inputPwd)) {
      throw new PasswordWrongException("密码错误！登录失败");
    }
    return Result.success("登录成功", targetUser);
  }

  /**
   * 用户注册.
   *
   * @param newUser 新用户名.
   * @param newPwd  新密码.
   * @return 返回result.
   */
  public Result<Void> doRegister(String newUser, String newPwd) {
    if (newUser.isBlank()) {
      throw new ParamEmptyException("用户名不能为空");
    }
    if ("admin".equals(newUser)) {
      throw new UserExistException("管理员账号不可以注册");
    }
    List<User> userList = userDao.findAll();
    for (User user : userList) {
      if (user.getUsername().equals(newUser)) {
        throw new UserExistException("用户名已存在，请重新注册");
      }
    }
    if (newPwd == null || newPwd.length() < 6) {
      throw new ParamEmptyException("密码不能为空且长度不能小于6位");
    }
    User user = new User();
    user.setUsername(newUser);
    user.setPassword(newPwd);
    user.setRole("student");
    user.setRegisterTime(new Date());
    userList.add(user);
    userDao.saveAll(userList);
    return Result.success("注册成功");
  }
}
