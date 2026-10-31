import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ResourceManager {
    /**
     * 新增资源（管理员权限）
     */
    public Result<Void> addResource(List<CampusResource> resList,
                            String loginRole, Scanner scanner) {
        if (!"admin".equals(loginRole)) {
            throw new PermissionDeniedException("权限不足，只有管理员可以新增资源");
        }
        System.out.println("====新增校园资源====");
        System.out.print("请输入资源名称：");
        String name = scanner.next();
        scanner.nextLine();//吃掉换行
        if (name.isBlank()) {
            throw new ParamEmptyException("资源名称不能为空");
        }
        System.out.print("请输入资源类型(教室/实验室/设备/场地)：");
        String type = scanner.nextLine();
        System.out.print("请输入位置：");
        String location = scanner.nextLine();
        System.out.print("请输入描述：");
        String desc = scanner.nextLine();

        //生成全局唯一ID，R+UUID简化截取
        String resId = "R" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        CampusResource resource = new CampusResource();
        resource.setId(resId);
        resource.setName(name);
        resource.setType(type);
        resource.setStatus(ResourceStatus.AVAILABLE); //默认可预约
        resource.setLocation(location);
        resource.setDescription(desc);

        resList.add(resource);
        FileUtil.saveResources(resList);
        System.out.println("资源新增成功！生成ID=" + resId);
        System.out.println(resource);
        return Result.success("新增资源成功");
    }
    /**
     * 查询全部资源，管理员使用
     */
    public Result<List<CampusResource>> queryAllResource(List<CampusResource> resList, String loginRole) {
        if (!"admin".equals(loginRole)) {
            throw new PermissionDeniedException("权限不足，学生不能查看全部资源");
        }
        System.out.println("\n====全部校园资源列表====");
        if (resList.isEmpty()) {
            System.out.println("暂无资源数据");
        }else{
            resList.forEach(System.out::println);
        }
        return Result.success("查询全部资源成功");
    }
    /**
     * 查询资源，学生使用,只能查到可预约资源
     */
    public Result<List<CampusResource>> queryAvailableResources(List<CampusResource> resList) {
        System.out.println("\n====可预约的校园资源列表====");
        boolean hasAvailable = false;
        for (CampusResource r : resList) {
            if (r.getStatus().equals(ResourceStatus.AVAILABLE)) {
                System.out.println(r);
                hasAvailable = true;
            }
        }
        if (!hasAvailable) {
            System.out.println("暂无可预约资源");
        }
        return Result.success("查询可预约资源成功");
    }

    /**
     * 根据id查找资源，找不到抛异常
     */
    public Result<CampusResource> findById(List<CampusResource> resList,String id){
        for(CampusResource r : resList){
            if(r.getId().equals(id)){
                return Result.success("查询资源成功", r);
            }
        }
        return Result.fail("未找到该ID对应的资源");
    }

    /**
     * 修改资源信息【管理员】
     */
    public Result<Void> updateResource(List<CampusResource> resList,String loginRole,Scanner scanner){
        if(!"admin".equals(loginRole)){
            throw new PermissionDeniedException("权限不足，只有管理员可以修改资源");
        }
        System.out.println("====修改资源信息====");
        System.out.print("请输入待修改资源ID：");
        String editId = scanner.next();
        Result<CampusResource> target = findById(resList,editId);
        if (!target.isSuccess()){
            return Result.fail(target.getMsg());
        }
        CampusResource targetResource = target.getData();
        scanner.nextLine();
        System.out.print("输入新名称(直接回车不修改)：");
        String newName = scanner.nextLine();
        if(!newName.isBlank()) targetResource.setName(newName);

        System.out.print("输入新类型(直接回车不修改)：");
        String newType = scanner.nextLine();
        if(!newType.isBlank()) targetResource.setType(newType);

        System.out.print("输入新位置(直接回车不修改)：");
        String newLoc = scanner.nextLine();
        if(!newLoc.isBlank()) targetResource.setLocation(newLoc);

        System.out.println("设置状态：1-AVAILABLE可预约 2-OCCUPIED占用 3-DISABLED停用，回车不修改");
        System.out.print("请选择：");
        String statusInput = scanner.nextLine();
        if(!statusInput.isBlank()){
            int opt = Integer.parseInt(statusInput);
            switch (opt){
                case 1: targetResource.setStatus(ResourceStatus.AVAILABLE);break;
                case 2: targetResource.setStatus(ResourceStatus.OCCUPIED);break;
                case 3: targetResource.setStatus(ResourceStatus.DISABLED);break;
                default: System.out.println("输入状态无效，状态保持原样");
            }
        }

        System.out.print("输入新描述(回车不修改)：");
        String newDesc = scanner.nextLine();
        if(!newDesc.isBlank()) targetResource.setDescription(newDesc);

        FileUtil.saveResources(resList);
        System.out.println("资源修改完成！");
        System.out.println(target);
        return Result.success("资源修改成功");

    }

    /**
     * 删除资源【管理员】
     */
    public Result<Void> deleteResource(List<CampusResource> resList,String loginRole,Scanner scanner){
        if(!"admin".equals(loginRole)){
            throw new PermissionDeniedException("权限不足，只有管理员可以删除资源");
        }
        System.out.println("====删除资源====");
        System.out.print("输入待删除资源ID：");
        String delId = scanner.next();
        Result<CampusResource> target = findById(resList,delId);
        if (!target.isSuccess()){
            return Result.fail(target.getMsg());
        }
        resList.remove(target.getData());
        FileUtil.saveResources(resList);
        System.out.println("资源ID="+delId+" 删除成功！");
        return Result.success("资源删除成功");
    }
}

