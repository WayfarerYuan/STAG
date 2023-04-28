package edu.uob.actions;

import edu.uob.GameAction;

public class Subject extends GameAction implements ActionComponent{
    public Subject(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }
}
