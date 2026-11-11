package dao;

import entity.CampusResource;
import utils.FileUtil;
import java.util.List;

public class ResourceDao {
    private final List<CampusResource> resourceList;

    public ResourceDao(){
        resourceList = FileUtil.loadResources();
    }

    public List<CampusResource> findAll(){
        return resourceList;
    }

    public void saveAll(List<CampusResource> list){
        FileUtil.saveResources(list);
    }
}

