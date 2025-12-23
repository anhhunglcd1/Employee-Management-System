package model;

public class Department {
    private int id;
    private String code;
    private String name;
    private String description;
    private int managerId;
    private String status;
    public Department() {}
    
    public Department(int id, String code, String name, String description, int managerId,String status) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.managerId = managerId;
        this.status = status;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return name != null ? name : "";
    }
}