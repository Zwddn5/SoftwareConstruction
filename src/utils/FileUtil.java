package utils;

import entity.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * V1.3 文件工具类，用户数据持久化
 * 职责：自动创建user.txt，读取用户集合，写入用户集合；try‑with‑resources管理流
 */
public class FileUtil {

    /**
     * 存放用户数据的文件，不存在自动创建
     */
    private static final String USER_FILE = "users.txt";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy‑MM‑dd HH:mm:ss");

    //初始化文件，不存在就新建文件
    public static void initUserFile() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 从磁盘user.txt读取全部用户，解析为List<entity.User>
     * @return 用户集合
     */
    public static List<User> loadUsers() {
        initUserFile();//确保文件存在
        List<User> userList = new ArrayList<>();
        //try-with-resources管理流，自动关闭
        try (BufferedReader br = Files.newBufferedReader(Paths.get(USER_FILE))){
            String line;
            //读取一整行数据，按逗号切割，得到字符串数组
            while ((line = br.readLine()) != null){
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                if (parts.length != 4) continue;
                //字符串转为user对象，反序列化
                String username = parts[0];
                String password = parts[1];
                String role = parts[2];
                Date registerTime;
                try {
                    registerTime = SDF.parse(parts[3]);
                } catch (ParseException e) {
                    continue;
                }
                User user = new User(username, password, role, registerTime);
                userList.add(user);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * 将全部用户集合覆盖写入user.txt
     * @param userList 用户列表
     */
    public static void saveUsers(List<User> userList) {
        initUserFile();
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(USER_FILE), StandardCharsets.UTF_8)){
            for(User u : userList){
                String line = String.format("%s,%s,%s,%s",
                        u.getUsername(),
                        u.getPassword(),
                        u.getRole(),
                        SDF.format(u.getRegisterTime()));//format()与parse()是逆操作
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 存放资源的文件，不存在自动创建
     */
    private static final String RESOURCE_FILE = "resources.txt";
    public static void initResourceFile(){
        File file = new File(RESOURCE_FILE);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 读取资源文件，返回List<entity.CampusResource>
     */
    public static List<CampusResource> loadResources(){
        initResourceFile();
        List<CampusResource> resourceList = new ArrayList<>();
        try(BufferedReader br = Files.newBufferedReader(Paths.get(RESOURCE_FILE),StandardCharsets.UTF_8)){
            String line;
            while((line = br.readLine())!=null){
                if(line.isBlank()) continue;
                String[] parts = line.split(",");
                if(parts.length !=6) continue;
                String id = parts[0];
                String name = parts[1];
                String type = parts[2];
                //字符串转为枚举
                ResourceStatus status;
                try{
                    status = ResourceStatus.valueOf(parts[3]);
                }catch (IllegalArgumentException e){
                    continue;
                }
                String location = parts[4];
                String desc = parts[5];
                CampusResource res = new CampusResource(id,name,type,status,location,desc);
                resourceList.add(res);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return resourceList;
    }
    /**
     * 把资源集合覆盖写入resource.txt
     */
    public static void saveResources(List<CampusResource> resourceList){
        initResourceFile();
        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(RESOURCE_FILE),StandardCharsets.UTF_8)){
            for(CampusResource r : resourceList){
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        r.getId(),
                        r.getName(),
                        r.getType(),
                        r.getStatus().name(),
                        r.getLocation(),
                        r.getDescription()
                );
                bw.write(line);
                bw.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * 存放预约记录的文件，不存在自动创建
     */
    private static final String RESERVE_FILE = "reserve.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy‑MM‑dd HH:mm:ss");

    public static void initReserveFile(){
        File file = new File(RESERVE_FILE);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //加载全部预约记录
    public static List<ReserveRecord> loadReserveRecords(){
        initReserveFile();
        List<ReserveRecord> list = new ArrayList<>();
        try(BufferedReader br = Files.newBufferedReader(Paths.get(RESERVE_FILE),StandardCharsets.UTF_8)){
            String line;
            while((line = br.readLine())!=null){
                if(line.isBlank()) continue;
                String[] parts = line.split(",");
                if(parts.length !=8) continue;
                ReserveRecord record = new ReserveRecord();
                record.setRecordId(parts[0]);
                record.setResourceId(parts[1]);
                record.setApplicantUsername(parts[2]);
                record.setApplyTime(LocalDateTime.parse(parts[3],DATE_TIME_FORMATTER));
                record.setReserveStartTime(LocalDateTime.parse(parts[4],DATE_TIME_FORMATTER));
                record.setReserveEndTime(LocalDateTime.parse(parts[5],DATE_TIME_FORMATTER));
                record.setStatus(ReserveStatus.valueOf(parts[6]));
                record.setRemark(parts[7]);
                list.add(record);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //保存预约记录集合，覆盖写入文件
    public static void saveReserveRecords(List<ReserveRecord> list){
        initReserveFile();
        try(BufferedWriter bw = Files.newBufferedWriter(Paths.get(RESERVE_FILE),StandardCharsets.UTF_8)){
            for(ReserveRecord r : list){
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        r.getRecordId(),
                        r.getResourceId(),
                        r.getApplicantUsername(),
                        r.getApplyTime().format(DATE_TIME_FORMATTER),
                        r.getReserveStartTime().format(DATE_TIME_FORMATTER),
                        r.getReserveEndTime().format(DATE_TIME_FORMATTER),
                        r.getStatus().name(),
                        r.getRemark()
                );
                bw.write(line);
                bw.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }





}

