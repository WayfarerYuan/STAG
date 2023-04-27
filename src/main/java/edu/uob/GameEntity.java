package edu.uob;

public abstract class GameEntity
{
    private String name;
    private String description;

    public GameEntity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String newName) {
        name = newName;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String newDescription) {
        description = newDescription;
    }
}
