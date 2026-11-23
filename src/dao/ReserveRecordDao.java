package dao;

import entity.ReserveRecord;
import java.util.List;
import utils.FileUtil;

/**
 * 预约资源记录数据访问对象.
 */

public class ReserveRecordDao {
  private final List<ReserveRecord> reserveRecordList;

  /**
   * 构造方法.
   */
  public ReserveRecordDao() {
    reserveRecordList = FileUtil.loadReserveRecords();
  }

  /**
   * 获取所有预约资源记录.
   *
   * @return 预约资源记录列表.
   */

  public List<ReserveRecord> findAll() {
    return reserveRecordList;
  }

  /**
   * 保存所有预约资源记录.
   *
   * @param list 预约资源记录列表.
   */

  public void saveAll(List<ReserveRecord> list) {
    FileUtil.saveReserveRecords(list);
  }
}

