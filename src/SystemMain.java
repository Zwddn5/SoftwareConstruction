import dao.ReserveRecordDao;
import dao.ResourceDao;
import dao.UserDao;
import service.ReserveRecordService;
import service.ResourceService;
import service.UserService;
import ui_console.AdminView;
import ui_console.MainView;
import ui_console.StudentView;
import java.util.Scanner;
public class SystemMain {
    /**
     * 程序入口主类
     */
    public static void main(String[] args) {

       //new之后会自动加载文件里面的数据
        UserDao userDao = new UserDao();
        ResourceDao resourceDao = new ResourceDao();
        ReserveRecordDao reserveRecordDao = new ReserveRecordDao();
        //创建业务对象，调用业务方法

        UserService userService = new UserService();
        ResourceService resourceService = new ResourceService();
        ReserveRecordService reserveRecordService = new ReserveRecordService();

        //创建Scanner对象，用于用户输入
        Scanner scanner = new Scanner(System.in);

        //管理员视图
        AdminView adminView = new AdminView(resourceService, reserveRecordService);

        //学生视图
        StudentView studentView = new StudentView(resourceService, reserveRecordService);

        //主视图
        MainView mainView = new MainView(userService, adminView, studentView, scanner);

        mainView.start();

        //程序结束关闭扫描器
        scanner.close();
    }
}
