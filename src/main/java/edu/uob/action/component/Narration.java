package edu.uob.action.component;

public class Narration implements ActionComponent{
    String name;

    public Narration(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
