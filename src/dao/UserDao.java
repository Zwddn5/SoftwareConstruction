package dao;

import entity.User;
import java.util.Date;
import java.util.List;
import utils.FileUtil;

/**
 * 用户数据访问对象.
 */

public class UserDao {
  // 内存集合
  private final List<User> userList;

  /**
   * 构造方法.
   */

  public UserDao() {
    userList = FileUtil.loadUsers();
    // 和原来SystemMain逻辑保持一致：没有用户就插入默认admin
    if (userList.isEmpty()) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword("123456");
      admin.setRole("admin");
      admin.setRegisterTime(new Date());
      userList.add(admin);
      FileUtil.saveUsers(userList);
    }
  }

  /**
   * 获取所有用户.
   *
   * @return 用户列表.
   */

  public List<User> findAll() {
    return userList;
  }

  /**
   * 保存全部用户.
   *
   * @param list 用户列表.
   */

  public void saveAll(List<User> list) {
    FileUtil.saveUsers(list);
  }
}

