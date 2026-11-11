package dao;

import entity.User;
import utils.FileUtil;
import java.util.Date;
import java.util.List;

public class UserDao {
    //内存集合
    private final List<User> userList;

    //构造方法：程序启动加载磁盘数据
    public UserDao(){
        userList = FileUtil.loadUsers();
        //和原来SystemMain逻辑保持一致：没有用户就插入默认admin
        if(userList.isEmpty()){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123456");
            admin.setRole("admin");
            admin.setRegisterTime(new Date());
            userList.add(admin);
            FileUtil.saveUsers(userList);
        }
    }

    //查询全部用户
    public List<User> findAll(){
        return userList;
    }

    //覆盖保存全部用户集合
    public void saveAll(List<User> list){
        FileUtil.saveUsers(list);
    }
}

