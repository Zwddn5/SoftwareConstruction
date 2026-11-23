package appstarter;

import dao.ReserveRecordDao;
import dao.ResourceDao;
import dao.UserDao;
import java.util.Scanner;
import service.ReserveRecordService;
import service.ResourceService;
import service.UserService;
import uiconsole.AdminView;
import uiconsole.MainView;
import uiconsole.StudentView;

/**
 * 启动类.
 */

public class SystemMain {
  /**
   * 程序入口主类.
   */

  public static void main(String[] args) {

    // new之后会自动加载文件里面的数据
    UserDao userDao = new UserDao();
    ResourceDao resourceDao = new ResourceDao();
    ReserveRecordDao reserveRecordDao = new ReserveRecordDao();

    // 创建用户对象
    UserService userService = new UserService();
    // 创建资源对象
    ResourceService resourceService = new ResourceService();
    // 创建预约记录对象
    ReserveRecordService reserveRecordService = new ReserveRecordService();

    // 创建Scanner对象，用于用户输入
    Scanner scanner = new Scanner(System.in);

    // 管理员视图
    AdminView adminView = new AdminView(resourceService, reserveRecordService);

    // 学生视图
    StudentView studentView = new StudentView(resourceService, reserveRecordService);

    // 主视图
    MainView mainView = new MainView(userService, adminView, studentView, scanner);

    mainView.start();

    // 程序结束关闭扫描器
    scanner.close();
  }
}
