package edu.uob;

import edu.uob.action.component.KeyPhrase;
import edu.uob.action.component.Narration;

import java.util.ArrayList;

public class GameAction {
    private String name;
    private ArrayList<KeyPhrase> triggers;
    private ArrayList<GameEntity> subjects;
    private ArrayList<GameEntity> consumed;
    private ArrayList<GameEntity> produced;
    private Narration narration;
    public GameAction(String name) {
        this.name = name;
        this.triggers = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.consumed = new ArrayList<>();
        this.produced = new ArrayList<>();
        this.narration = new Narration("");
    }

    public String getName() {
        return name;
    }
    public ArrayList<KeyPhrase> getTriggers() {
        return triggers;
    }
    public ArrayList<GameEntity> getSubjects() {
        return subjects;
    }
    public ArrayList<GameEntity> getConsumed() {
        return consumed;
    }
    public ArrayList<GameEntity> getProduced() {
        return produced;
    }
    public Narration getNarration() {
        return narration;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTriggers(ArrayList<KeyPhrase> triggers) {
        this.triggers = triggers;
    }
    public void setSubjects(ArrayList<GameEntity> subjects) {
        this.subjects = subjects;
    }
    public void setConsumed(ArrayList<GameEntity> consumed) {
        this.consumed = consumed;
    }
    public void setProduced(ArrayList<GameEntity> produced) {
        this.produced = produced;
    }
    public void setNarration(Narration narration) {
        this.narration = narration;
    }
    public void addTrigger(KeyPhrase trigger) {
        triggers.add(trigger);
    }
    public void addSubject(GameEntity subject) {
        subjects.add(subject);
    }
    public void addConsumed(GameEntity consumed) {
        this.consumed.add(consumed);
    }
    public void addProduced(GameEntity produced) {
        this.produced.add(produced);
    }
}
