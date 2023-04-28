package edu.uob.actions;

import edu.uob.GameEntity;

public class ActionEntity extends GameEntity implements ActionComponent {
    public ActionEntity(String name, String description) {
        super(name, description);
    }
}
