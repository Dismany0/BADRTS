package com.game.game;

//Commands are simply a number, weight, and a delay that isnt actually used at the moment.
//For the enemy ai, every single command is assigned a weight value. These values can change depending on the state of the game.
//When stored in a priority queue, the ai will always choose to work with what it deems to be more important first, and leave the less important rolls for
//less important times
public class Commands implements Comparable<Commands>{

    public int commandNumber, weight, delay;
    public Commands(int commandNumber, int weight, int delay){
        this.commandNumber = commandNumber;
        this.weight = weight;
        this.delay = delay;
    }
    public Commands(int commandNumber, int weight){
        this.commandNumber = commandNumber;
        this.weight = weight;
        this.delay = 0;
    }

    public int compareTo(Commands o) {
        return this.weight - o.weight;
    }


}
