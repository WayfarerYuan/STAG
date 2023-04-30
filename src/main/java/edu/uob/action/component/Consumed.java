package edu.uob.action.component;

public class Consumed implements ActionComponent{
    String name;

    public Consumed(String name) {
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
