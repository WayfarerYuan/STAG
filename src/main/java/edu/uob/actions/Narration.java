package edu.uob.actions;

public class Narration implements ActionComponent{
    String name;

    public Narration(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
