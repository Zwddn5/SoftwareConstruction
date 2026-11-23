package dao;

import entity.CampusResource;
import java.util.List;
import utils.FileUtil;

/**
 * 校园资源数据访问对象.
 */

public class ResourceDao {
  private final List<CampusResource> resourceList;

  /**
   * 构造方法.
   */

  public ResourceDao() {
    resourceList = FileUtil.loadResources();
  }

  /**
   * 获取所有资源.
   *
   * @return 校园资源列表.
   */

  public List<CampusResource> findAll() {
    return resourceList;
  }

  /**
   * 保存所有资源.
   *
   * @param list 校园资源列表.
   */

  public void saveAll(List<CampusResource> list) {
    FileUtil.saveResources(list);
  }
}

