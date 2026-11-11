package dao;

import entity.ReserveRecord;
import utils.FileUtil;
import java.util.List;

public class ReserveRecordDao {
    private final List<ReserveRecord> reserveRecordList;

    public ReserveRecordDao(){
        reserveRecordList = FileUtil.loadReserveRecords();
    }

    public List<ReserveRecord> findAll(){
        return reserveRecordList;
    }

    public void saveAll(List<ReserveRecord> list){
        FileUtil.saveReserveRecords(list);
    }
}

