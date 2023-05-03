package edu.uob;

import edu.uob.action.component.KeyPhrase;
import edu.uob.action.component.Narration;
import edu.uob.action.component.Trigger;

import java.util.ArrayList;

public class GameAction {
    private String name;
    private Trigger trigger;
    private ArrayList<KeyPhrase> allTriggers;
    private ArrayList<GameEntity> subjects;
    private ArrayList<GameEntity> requiredSubjects;
    private ArrayList<GameEntity> foundRelatedSubjects;
    private ArrayList<GameEntity> consumed;
    private ArrayList<GameEntity> requiredConsumed;
    private ArrayList<GameEntity> foundRelatedConsumed;
    private ArrayList<GameEntity> produced;
    private ArrayList<GameEntity> requiredProduced;
    private ArrayList<GameEntity> foundRelatedProduced;
    private Narration narration;
    public boolean isBuiltIn = false;

    public GameAction(String name) {
        this.name = name;
        this.trigger = new Trigger("");
        this.allTriggers = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.requiredSubjects = new ArrayList<>();
        this.foundRelatedSubjects = new ArrayList<>();
        this.consumed = new ArrayList<>();
        this.requiredConsumed = new ArrayList<>();
        this.foundRelatedConsumed = new ArrayList<>();
        this.produced = new ArrayList<>();
        this.requiredProduced = new ArrayList<>();
        this.foundRelatedProduced = new ArrayList<>();
        this.narration = new Narration("");
        // debug
        System.out.println("[Action] get: " + name);
    }

    public String getName() {
        return name;
    }
    public Trigger getTrigger() {
        return trigger;
    }
    public ArrayList<KeyPhrase> getTriggers() {
        return allTriggers;
    }
    public ArrayList<GameEntity> getSubjects() {
        return subjects;
    }
    public ArrayList<GameEntity> getRequiredSubjects() {
        return requiredSubjects;
    }
    public ArrayList<GameEntity> getFoundRelatedSubjects() {
        return foundRelatedSubjects;
    }
    public ArrayList<GameEntity> getConsumed() {
        return consumed;
    }
    public ArrayList<GameEntity> getRequiredConsumed() {
        return requiredConsumed;
    }
    public ArrayList<GameEntity> getFoundRelatedConsumed() {
        return foundRelatedConsumed;
    }
    public ArrayList<GameEntity> getProduced() {
        return produced;
    }
    public ArrayList<GameEntity> getRequiredProduced() {
        return requiredProduced;
    }
    public ArrayList<GameEntity> getFoundRelatedProduced() {
        return foundRelatedProduced;
    }
    public Narration getNarration() {
        return narration;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
    public void setTriggers(ArrayList<KeyPhrase> triggers) {
        this.allTriggers = triggers;
    }
    public void setSubjects(ArrayList<GameEntity> subjects) {
        this.subjects = subjects;
    }
    public void setRequiredSubjects(ArrayList<GameEntity> requiredSubjects) {
        this.requiredSubjects = requiredSubjects;
    }
    public void setFoundRelatedSubjects(ArrayList<GameEntity> foundRelatedSubjects) {
        this.foundRelatedSubjects = foundRelatedSubjects;
    }
    public void setConsumed(ArrayList<GameEntity> consumed) {
        this.consumed = consumed;
    }
    public void setRequiredConsumed(ArrayList<GameEntity> requiredConsumed) {
        this.requiredConsumed = requiredConsumed;
    }
    public void setFoundRelatedConsumed(ArrayList<GameEntity> foundRelatedConsumed) {
        this.foundRelatedConsumed = foundRelatedConsumed;
    }
    public void setProduced(ArrayList<GameEntity> produced) {
        this.produced = produced;
    }
    public void setRequiredProduced(ArrayList<GameEntity> requiredProduced) {
        this.requiredProduced = requiredProduced;
    }
    public void setFoundRelatedProduced(ArrayList<GameEntity> foundRelatedProduced) {
        this.foundRelatedProduced = foundRelatedProduced;
    }
    public void setNarration(Narration narration) {
        this.narration = narration;
    }
    public void addTrigger(KeyPhrase trigger) {
        allTriggers.add(trigger);
    }
    public void addSubject(GameEntity subject) {
        subjects.add(subject);
    }
    public void addMatchedSubject(GameEntity matchedSubject) {
        requiredSubjects.add(matchedSubject);
    }
    public void addFoundRelatedSubject(GameEntity foundRelatedSubject) {
        foundRelatedSubjects.add(foundRelatedSubject);
    }
    public void addConsumed(GameEntity consumed) {
        this.consumed.add(consumed);
    }
    public void addMatchedConsumed(GameEntity matchedConsumed) {
        this.requiredConsumed.add(matchedConsumed);
    }
    public void addFoundRelatedConsumed(GameEntity foundRelatedConsumed) {
        this.foundRelatedConsumed.add(foundRelatedConsumed);
    }
    public void addProduced(GameEntity produced) {
        this.produced.add(produced);
    }
    public void addMatchedProduced(GameEntity matchedProduced) {
        this.requiredProduced.add(matchedProduced);
    }
    public void addFoundRelatedProduced(GameEntity foundRelatedProduced) {
        this.foundRelatedProduced.add(foundRelatedProduced);
    }
}
