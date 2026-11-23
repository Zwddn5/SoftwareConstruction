package service;

import dao.ResourceDao;
import entity.CampusResource;
import entity.ResourceStatus;
import exception.ParamEmptyException;
import exception.PermissionDeniedException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import result.Result;

/**
 * 资源管理类.
 */
public class ResourceService {
  private final ResourceDao resourceDao;

  /**
   * 构造函数.
   */

  public ResourceService() {
    this.resourceDao = new ResourceDao();
  }

  /**
   * 新增资源【管理员】.
   *
   * @param loginRole 登录角色.
   * @param name      资源名称.
   * @param type      资源类型.
   * @param location  资源位置.
   * @param desc      资源描述.
   * @return result类包装的新增资源结果.
   */

  public Result<Void> addResource(String loginRole, String name,
                                  String type, String location, String desc) {
    if (!"admin".equals(loginRole)) {
      throw new PermissionDeniedException("权限不足，只有管理员可以新增资源");
    }
    if (name.isBlank()) {
      throw new ParamEmptyException("资源名称不能为空");
    }
    // 生成全局唯一ID，R+UUID简化截取
    String resId = "R" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    CampusResource resource = new CampusResource();
    resource.setId(resId);
    resource.setName(name);
    resource.setType(type);
    resource.setStatus(ResourceStatus.AVAILABLE); // 默认可预约
    resource.setLocation(location);
    resource.setDescription(desc);

    List<CampusResource> resourceList = resourceDao.findAll();
    resourceList.add(resource);
    resourceDao.saveAll(resourceList);
    return Result.success("新增资源成功");
  }

  /**
   * 查询所有资源.
   *
   * @param loginRole 登录角色.
   * @return result类包装的查询全部资源结果.
   */
  public Result<List<CampusResource>> queryAllResource(String loginRole) {
    if (!"admin".equals(loginRole)) {
      throw new PermissionDeniedException("权限不足，学生不能查看全部资源");
    }
    /*修改前的for循环
     *if (resList.isEmpty()) {
     *   System.out.println("暂无资源数据");
     *}else{
     *   resList.forEach(System.out::println);
     * }
     *return result.Result.success("查询全部资源成功");
     */
    List<CampusResource> allSorted = resourceDao.findAll().stream()
        .sorted(Comparator.comparing(CampusResource::getId))
        .collect(Collectors.toList());
    return Result.success("查询全部资源成功", allSorted);
  }

  /**
   * 查询可预约资源【学生】.
   *
   * @return result类包装的查询可预约资源结果.
   */
  // filter过滤状态为AVAILABLE，按id升序排序
  public Result<List<CampusResource>> queryAvailableResources() {
    /*
     *  boolean hasAvailable = false;
     *  for (entity.CampusResource r : resList) {
     *     if (r.getStatus().equals(entity.ResourceStatus.AVAILABLE)) {
     *         System.out.println(r);
     *       hasAvailable = true;
     *    }
     * }
     *  if (!hasAvailable) {
     *     System.out.println("暂无可预约资源");
     *  }
     *  return result.Result.success("查询可预约资源成功");
     */
    List<CampusResource> availableList = resourceDao.findAll().stream()
        .filter(r -> entity.ResourceStatus.AVAILABLE.equals(r.getStatus()))
        .sorted(Comparator.comparing(CampusResource::getId))// 升序排序
        .collect(Collectors.toList());
    return Result.success("查询可预约资源成功", availableList);
  }

  /*
   * 根据id查找资源，找不到抛异常.
   * 修改原因：传统 for 循环找不到时逻辑上等价返回 null，null 容易引发空指针异常。
   * `Optional`是 Java 8 引入的，专门用来表达「对象可能存在，也可能不存在」的容器，强制我们做存在性判断。
   * 好处：显式处理对象缺失场景，降低空指针风险；**但是对外返回依然是 result.Result，上层调用代码完全不用改动**.
   */

  /**
   * 根据id查找资源，找不到抛异常.
   */

  public Result<CampusResource> findById(String id) {
    /*
     *for(entity.CampusResource r : resList){
     *   if(r.getId().equals(id)){
     *      return result.Result.success("查询资源成功", r);
     * }
     *  }
     *return result.Result.fail("未找到该ID对应的资源");
     */
    // stream.findFirst 返回 Optional<entity.CampusResource>
    // Optional是一个可以容纳空值的容器，用在这里是为了防止空指针（r.getId()）异常
    Optional<CampusResource> opt = resourceDao.findAll().stream()
        .filter(r -> r.getId().equals(id))
        .findFirst();
    return opt.map(campusResource -> Result.success("查询资源成功", campusResource))
        .orElseGet(() -> Result.fail("资源ID【" + id + "】不存在")); // 懒执行，只有map()为空时才执行
  }

  /**
   * 资源名称模糊查询.
   *
   * @param keyword 查询关键词.
   * @return result类包装的资源名称模糊查询结果.
   */

  public Result<List<CampusResource>> queryResourceByKeyword(String keyword) {
    List<CampusResource> result = resourceDao.findAll().stream()
        .filter(r -> r.getName().toLowerCase().contains(keyword.toLowerCase()))
        .sorted(Comparator.comparing(CampusResource::getId))
        .collect(Collectors.toList());
    return Result.success("模糊查询完成", result);
  }

  /**
   * 修改资源信息【管理员】.
   *
   * @param loginRole 登录角色.
   * @param editId    资源ID.
   * @param newName   新资源名称.
   * @param newType   新资源类型.
   * @param newLoc    新资源位置.
   * @param statusOpt 新资源状态选项.
   * @param newDesc   新资源描述.
   * @return result类包装的资源修改结果.
   */

  public Result<Void> updateResource(String loginRole, String editId,
                                     String newName, String newType, String newLoc,
                                     Integer statusOpt, String newDesc) {
    if (!"admin".equals(loginRole)) {
      throw new PermissionDeniedException("权限不足，只有管理员可以修改资源");
    }
    Result<CampusResource> targetRes = findById(editId);
    if (!targetRes.isSuccess()) {
      return Result.fail(targetRes.getMsg());
    }
    CampusResource targetResource = targetRes.getData();

    if (!newName.isBlank()) {
      targetResource.setName(newName);
    }
    if (!newType.isBlank()) {
      targetResource.setType(newType);
    }
    if (!newLoc.isBlank()) {
      targetResource.setLocation(newLoc);
    }
    if (statusOpt != null) {
      switch (statusOpt) {
        case 1:
          targetResource.setStatus(entity.ResourceStatus.AVAILABLE);
          break;
        case 2:
          targetResource.setStatus(entity.ResourceStatus.OCCUPIED);
          break;
        case 3:
          targetResource.setStatus(entity.ResourceStatus.DISABLED);
          break;
        default:
          // 无效，不修改
          break;
      }
    }
    if (!newDesc.isBlank()) {
      targetResource.setDescription(newDesc);
    }
    List<CampusResource> list = resourceDao.findAll();
    resourceDao.saveAll(list);
    return Result.success("资源修改成功");
  }

  /**
   * 删除资源【管理员】.
   *
   * @param loginRole 登录角色.
   * @param delId     资源ID.
   * @return result类包装的资源删除结果.
   */

  public Result<Void> deleteResource(String loginRole, String delId) {
    if (!"admin".equals(loginRole)) {
      throw new PermissionDeniedException("权限不足，只有管理员可以删除资源");
    }
    Result<CampusResource> targetRes = findById(delId);
    if (!targetRes.isSuccess()) {
      return Result.fail(targetRes.getMsg());
    }
    List<CampusResource> list = resourceDao.findAll();
    list.remove(targetRes.getData());
    resourceDao.saveAll(list);
    return Result.success("资源删除成功");
  }
}

