package edu.uob.action.component;

public class Trigger implements ActionComponent{
    String name;

    public Trigger(String name) {
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
