package com.game.game;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import com.game.engine.*;
import com.game.game.Objects.*;
import com.game.game.Objects.Units.*;
import com.game.game.Objects.Structures.*;
//The enemy ai has access to the gamemanager and everything inside of it. This allows the ai to understand and be able to process every single gamestate.
//Ideally, the ai should have a great economy, and once the late game arrives, quite powerful, well rounded armies, and brutal attacks.
//However, the truth is I ran into an error with testing, where I foudn that I am running out of space, likely because certian variables are not being reset
//I was unable to fully test the AI
public class AllyAI {
    
    GameManager gm;
    int difficulty;
    PriorityQueue<Commands> commands = new PriorityQueue<Commands>();
    int timer = 0;

    int garbageCollector = 0;
    ArrayList<Barracks> barracks = new ArrayList<Barracks>();
    Castle castle;
    Castle ecastle;

    int numKnights = 0, numMiners = 0, numArchers = 0, numMages = 0, numRogues = 0;
    int EnumKnights= 0, EnumMiners= 0, EnumArchers= 0, EnumMages = 0, EnumRogues= 0;
    
    int numSize = 0;
    int eNumSize = 0;
    int counterTimer = 0;

    int moveTime = 120;


    public AllyAI(GameManager gm){
        this.gm = gm;
        moveTime = 30;



        for(GObject i :gm.getObjects()){
            if(i instanceof Castle){
                if(i.getOwner() == 0){
                    castle = (Castle)i;
                }else{
                    ecastle = (Castle)i;
                }
            }
        }
        
    }
    public void count(){
       
        counterTimer = 0;
        barracks.clear();
            numSize = 0; eNumSize = 0;
            numKnights =0; numArchers = 0; numMages = 0; numRogues = 0; numMiners = 0;
            EnumKnights =0; EnumArchers = 0; EnumMages = 0; EnumRogues = 0; EnumMiners = 0;
            for(GObject i : gm.getObjects()){
                if(i instanceof Knight){
                    if(i.getOwner() == 0) {numKnights++; numSize++;}
                    else {EnumKnights++; eNumSize++;}
                }
                else if(i instanceof Archer){
                    if(i.getOwner() == 0) {numArchers++;numSize++;}
                    else {EnumArchers++;eNumSize++;}
                }
                else if(i instanceof Mage){
                    if(i.getOwner() == 0) {numMages++;numSize++;}
                    else {EnumMages++;eNumSize++;}
                }
                else if(i instanceof Rogue){
                    if(i.getOwner() == 0) {numRogues++;numSize++;}
                    else {EnumRogues++;eNumSize++;}
                }
                else if(i instanceof Miner){
                    if(i.getOwner() == 0) {numMiners++;numSize++;}
                    else {EnumMiners++;eNumSize++;}
                }
                else if(i instanceof Barracks){
                    barracks.add((Barracks) i);
                }
            }
            
    }
    public void update(GameContainer gc, float dt){

        timer++;
        counterTimer++;
        // garbageCollector++;
        // if(garbageCollector >= 600){
        //     System.gc();
        // }
        if(counterTimer > 300){
            count();
            
        }
        if(timer > moveTime){
            timer = 0;
            if(commands.size() > 20){
                commands.removeAll(commands);
                commands.clear();
            }
                addCommand(gc, gm, dt);
                if(commands.size() >= 1)
                    executeCommand(commands.poll());
            
        }
        
    }
    public void render(GameContainer gc, Renderer r) {

    }
    public void executeCommand(Commands command){  
        Barracks spawner = null;
        ArrayList<Barracks> spawnerList = new ArrayList<Barracks>();
        int temp = 0;
        for(Barracks i : barracks){
            if(i.getOwner() == 0){
                spawnerList.add(i);
            }
        }
        if(spawnerList.size() > 0){
            spawner = spawnerList.get((int)(Math.random() * spawnerList.size()));
            temp = (int)(Math.random() * spawnerList.size() + 1);
        }

        System.out.println(command.commandNumber);
        switch(command.commandNumber){
            case(1):
            for(ArrayList<Miner> i : gm.getMinerMap()){
                for(Miner m : i){
                    if(gm.getTiles()[m.getCenterTile()] / 13 != 2 && m.getPath().isEmpty() || (gm.getMinerMap()[m.getCenterTile()].size() > 1 && gm.getTiles()[m.getCenterTile()] / 13 == 2 )){
                        int loc = findClosestGold(gm, m.getCenterTile());
                        m.astar(gm, m.getCenterTile(), loc);
                        return;
                    }
                }
            } break;

            case(2):
            
            if(temp == 0){
                if(castle.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-50);
                    castle.getQueue().add(new Miner(castle.getX() + 32, castle.getY() + 80));
                    castle.getCostQueue().add(10*60);
                }
            }else{
                if(spawner.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-50);
                    spawner.getQueue().add(new Miner(spawner.getX() + 16, spawner.getY() + 48));
                    spawner.getCostQueue().add(10*60);
                }
            }
            break;

            case(3):
            if(temp == 0){
                if(castle.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-100);
                    castle.getQueue().add(new Knight(castle.getX() + 32, castle.getY() + 80));
                    castle.getCostQueue().add(12*60);
                }
            }else{
                if(spawner.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-100);
                    spawner.getQueue().add(new Knight(spawner.getX() + 16, spawner.getY() + 48));
                    spawner.getCostQueue().add(12*60);
                }
            }
            break;
            case(4):
            if(temp == 0){
                if(castle.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-100);
                    castle.getQueue().add(new Archer(castle.getX() + 32, castle.getY() + 80));
                    castle.getCostQueue().add(12*60);
                }
            }else{
                if(spawner.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-100);
                    spawner.getQueue().add(new Archer(spawner.getX() + 16, spawner.getY() + 48));
                    spawner.getCostQueue().add(12*60);
                }
            }
            break;
            case(5):
            if(temp == 0){
                if(castle.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-200);
                    castle.getQueue().add(new Mage(castle.getX() + 32, castle.getY() + 80));
                    castle.getCostQueue().add(15*60);
                }
            }else{
                if(spawner.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-200);
                    spawner.getQueue().add(new Mage(spawner.getX() + 16, spawner.getY() + 48));
                    spawner.getCostQueue().add(15*60);
                }
            }
            break;
            case(6):
            if(temp == 0){
                if(castle.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-200);
                    castle.getQueue().add(new Rogue(castle.getX() + 32, castle.getY() + 80));
                    castle.getCostQueue().add(15*60);
                }
            }else{
                if(spawner.getQueue().size() < 5){

                    gm.setPlayerGold(gm.getPlayerGold()-200);
                    spawner.getQueue().add(new Rogue(spawner.getX() + 16, spawner.getY() + 48));
                    spawner.getCostQueue().add(15*60);
                }
            }
            break;


            case(95):
            for(GObject i : gm.getObjects()){
                if(i instanceof GUnit && i.getOwner() == 0 && !(i instanceof Miner)){
                    
                    if((int)(Math.random() * 10) == 0){
                        
                        int randomTile = (int)((Math.random() * 128) + (Math.random() * 128) * 128);
                        ((GUnit) i).setAttackMoved(true);
                        ((GUnit) i).astar(gm, ((GUnit) i).getCenterTile(), randomTile);
                    }
                }
            }break;
            case(96):
            for(GObject i : gm.getObjects()){
                if(i instanceof GUnit && i.getOwner() == 0){
                   
                    ((GUnit) i).setAttackMoved(true);
                    ((GUnit) i).astar(gm, ((GUnit) i).getCenterTile(), 9 + 8 * 128);
                }
            }
            break;
            case(97):
            for(GObject i : gm.getObjects()){
                if(i instanceof GUnit && i.getOwner() == 0){
               
                    if((int)(Math.random() * 2) == 0){
                        int randomTile = (int)((Math.random() * 128) + (Math.random() * 128) * 128);
                        ((GUnit) i).setAttackMoved(true);
                        ((GUnit) i).astar(gm, ((GUnit) i).getCenterTile(), randomTile);
                    }
                    
                }
            }break;
            case(98):
            int randomTile = (int)((Math.random() * 128) + (Math.random() * 128) * 128);
            for(GObject i : gm.getObjects()){
                if(i instanceof GUnit && i.getOwner() == 0){
                  
                    if((int)(Math.random() * 2) == 0){
                        ((GUnit) i).setAttackMoved(true);
                        ((GUnit) i).astar(gm, ((GUnit) i).getCenterTile(), randomTile);
                    }
                    
                }
            }break;
            case(99):
            for(GObject i : gm.getObjects()){
                if(i instanceof GUnit && i.getOwner() == 0){
                 
                    ((GUnit) i).setAttackMoved(true);
                    ((GUnit) i).astar(gm, ((GUnit) i).getCenterTile(), (128-9) + (128 - 8) * 128);
                }
            }
            break;

        }
    }
    public void addCommand(GameContainer gc, GameManager gm, float dt) {
        for(ArrayList<Miner> i : gm.getMinerMap()){
            for(Miner m : i){
                if(gm.getTiles()[m.getCenterTile()] / 13 != 2 && m.getPath().isEmpty() && m.getTarget() == null && gm.getMinerMap()[m.getCenterTile()].size() > 1){
                    commands.add(new Commands(1, 20));
                    return;
                }
            }
        } 
        for(GObject i : gm.getObjects()){
                if(i instanceof GUnit && i.getOwner() == 0 && ((GUnit) i).getPath() != null){
                  if(((GUnit) i).getPath().isEmpty() && !(i instanceof Miner)){
                    if((int)(Math.random() * 6) == 0){
                        int randomTile = (int)(((Math.random() * 80)) + ((Math.random() * 80)) * 128);
                        ((GUnit) i).setAttackMoved(true);
                        ((GUnit) i).astar(gm, ((GUnit) i).getCenterTile(), randomTile);
                        break;
                    }
                }
                    
                }
            }

            for(GObject i : gm.getObjects()){
                if(i instanceof GUnit && i.getOwner() == 0 && i.getHealth() > i.getMaxHealth() *0.8){
                    if((int)(Math.random() * 20) == 0){
                        for(GObject j : gm.getObjects()){
                            if(j instanceof GUnit && j.getOwner() == 0 && !(j instanceof Miner)){
                                if((int)(Math.random() * 25) == 0){
                                    ((GUnit) j).astar(gm, ((GUnit) j).getCenterTile(), ((GUnit) i).getCenterTile());
                                }
                            }
                    }
                    if(i.getHealth() < 35){
                        int randomTile = (int)(128-((Math.random() * 70)) + (128-(Math.random() * 70)) * 128);
                        ((GUnit) i).astar(gm, ((GUnit) i).getCenterTile(), randomTile);
                    }
                        break;
                    }
                }
                    
                }
            

        if((int)(Math.random() * 80) == 0 && numSize - numMiners > 0){
            commands.add(new Commands(95, 0 +(int)( Math.random() * 40)));
        }
        
       if(numMiners < (gm.getUI().getGameTimer().getCurrentTime() /10f)){
           commands.add(new Commands(1,20));
       }

        if(numSize - numMiners < eNumSize - EnumMiners || (numMiners * 4 > numSize&& numMiners >= 8) || (int)(Math.random() * 8) == 5) {
            if(gm.getPlayerGold() >= 100 && (numKnights <= (numSize - numMiners) / 2 + 5)){
                commands.add(new Commands(3,15 + (int)(Math.random() * 15f)));
            } else if (gm.getPlayerGold() >= 100&& (int)(Math.random() * 5) == 0){
                commands.add(new Commands(3,10 + (int)(Math.random() * 8f)));
            }
            if(gm.getPlayerGold() >= 100 && (numArchers <= (numSize - numMiners) / 4 + 5)){
                commands.add(new Commands(4,15 + (int)(Math.random() * 15f)));
            } else if (gm.getPlayerGold() >= 100&& (int)(Math.random() * 5) == 0){
                commands.add(new Commands(4,15 + (int)(Math.random() * 8f)));
            }
            if(gm.getPlayerGold() >= 200 && (numRogues <= (numSize - numMiners) / 5 + 5)){
                commands.add(new Commands(6,15 + (int)(Math.random() * 15f)));
            } else if (gm.getPlayerGold() >= 200&& (int)(Math.random() * 5) == 0){
                commands.add(new Commands(6,15 + (int)(Math.random() * 8f)));
            }
            if(gm.getPlayerGold() >= 200 && (numMages <= (numSize - numMiners) / 4 + 5)){
                commands.add(new Commands(5,15 + (int)(Math.random() * 15f)));
            } else if (gm.getPlayerGold() >= 200&& (int)(Math.random() * 5) == 0){
                commands.add(new Commands(5,15 + (int)(Math.random() * 8f)));
            }

        }
        else{
            if(gm.getPlayerGold() >= 50){
                if(EnumMiners > numMiners){
                    commands.add(new Commands(2,15 + (int)(Math.random() * 15f)));
                }else{
                if(numMiners <= 8)
                    commands.add(new Commands(2,10+ (int)(Math.random() * 8f)));
                else if(numMiners <= 15)
                    commands.add(new Commands(2,6 + (int)(Math.random() * 4f)));
                else 
                    commands.add(new Commands(2,1+ (int)(Math.random() * 2f)));
                }
            }
        }

        if(eNumSize- EnumMiners < numSize - numMiners && (int)(Math.random() * 150) == 0 && gm.getUI().getGameTimer().getCurrentTime() > 450){
            //ends the game
            commands.add(new Commands(99, 1000));
        }
        if((int)(Math.random() * 500) == 0){
            //grab a bunch of units 50/50 and attack move somewhere
            commands.add(new Commands(98, (int)(25 * (Math.random() * 15f))));
        }
        
        if((int)(Math.random() * 600) == 0){
            //grabs every unit and chooses a random location to attack move
            commands.add(new Commands(97, (int)(25 * (Math.random() * 15f))));
        }
        if((int)(Math.random() * 400) == 0 && castle.getHealth() < 7000){
            //brings every unit back home
            commands.add(new Commands(96, (int)(25 * (Math.random() * 15f))));
        }
        if(castle.getHealth() < 3000 && (int)(Math.random() * 200) == 0){
            commands.add(new Commands(96, (int)(50 * (Math.random() * 15f))));
        }
        
        

        

    }
    public int findClosestGold(GameManager gm, int start) {
        boolean[] Visited = new boolean[gm.getLevelH() * gm.getLevelW()];
        Queue<Integer> toVisit = new LinkedList<Integer>();

        for(int i = 0; i < gm.getTiles().length; i++){
            if((gm.getTiles()[i] >= 13 && gm.getTiles()[i] < 26) || (gm.getTiles()[i] >= 52 && gm.getTiles()[i] < 65)){
                Visited[i] = true;;
            }
            else{
                Visited[i] = false;
            }
        }
        toVisit.add(start);
        Visited[start] = true;
        while(!toVisit.isEmpty()){
            int current = toVisit.poll();
            if(gm.getTiles()[current] / 13 == 2 && gm.getMinerMap()[current].size() < 1){
                return current;
            }
            int ci = current % 128;
            int cj = current / 128;
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    if(ci + i < 0 || cj + j < 0 || ci + i >= 128 || cj + j >= 128){
                        continue;
                    }
                    if(Visited[(ci + i) + (cj + j) * 128]){
                        continue;
                    }
                    toVisit.add((ci + i) + (cj + j) * 128);
                    Visited[(ci + i) + (cj + j) * 128] = true;
                }
            }
            
        }
        toVisit = null;
        Visited = null;
        return start;
    }

}
