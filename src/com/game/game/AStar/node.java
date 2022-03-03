package com.game.game.AStar;

//This is a simple node that stores a location and the cost of moving to that location from any other location. It is only used for pathfinding
//my units using a*
public class node implements Comparable<node>{
    public int location;
    public double cost;
    public node(int location, double cost){
        this.location = location;
        this.cost = cost;
    }

    @Override
    public int compareTo(node o) {
        if (this.cost - o.cost > 1){
            return 1;
        }
        else if(this.cost - o.cost < 1){
            return -1;
        }
        return 0;
    }
}
