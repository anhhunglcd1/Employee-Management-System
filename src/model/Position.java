package model;

public class Position {
    private int id;
    private String code;
    private String name;
    private String level; // STAFF, TEAM_LEADER, MANAGER, DIRECTOR
    private String description;
    public Position() {}
    
    public Position(int id, String code, String name, String level,String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.level = level;
        this.description = description;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getDescription (){return description; }
    public void setDescription(String description){ this.description = description;}

    @Override
    public String toString() {
        return name != null ? name : "";
    }
}