public class CampusResource {
    private String id;
    private String name;
    private String type;
    private ResourceStatus status;
    private String location;
    private String description;

    public CampusResource() {
    }
    public CampusResource(String id, String name, String type, ResourceStatus status,
                          String location, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.location = location;
        this.description = description;
    }
    // getter & setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public ResourceStatus getStatus() {
        return status;
    }
    public void setStatus(ResourceStatus status) {
        this.status = status;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "CampusResource [id=" + id + ", name=" + name + ", type=" + type + ", status=" + status
                + ", location=" + location + ", description=" + description + "]";
    }

}
