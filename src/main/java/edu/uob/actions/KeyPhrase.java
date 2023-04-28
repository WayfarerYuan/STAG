package edu.uob.actions;

public class KeyPhrase implements ActionComponent{
    String name;

    public KeyPhrase(String name) {
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
