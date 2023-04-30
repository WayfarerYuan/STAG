package edu.uob.action.component;

public class Produced implements ActionComponent{
    String name;

    public Produced(String name) {
        this.name=name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }
}
