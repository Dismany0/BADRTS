package com.game.game;

import java.awt.event.*;
import java.util.*;
import java.util.ResourceBundle.Control;
import java.awt.Rectangle;

import com.game.engine.*;
import com.game.engine.gfx.*;
import com.game.engine.audio.*;
import com.game.game.Objects.*;
import com.game.game.Objects.Units.*;
import com.game.game.Objects.Structures.*;



//The game manager is the core of the game. It contains all the game variables, all the audio files, all the image files, and more. Essentially, 
//The entire game is run on the game amanager, while everything else is just pieces of the larger game

public class GameManager extends AbstractGame {
    public static final int TILE_SIZE = 16;
    //0-9 are different rotations of a grassy wall. 0 is the top left corner, 1 is the top, 2 is top right, etc.
    //9 is left
    //This was originally named grasssheet because it was only going to contain the grassy textures. It was the first imageTile i made for this project
    //I severely underestimated how large imageTiles could be, and the actual image tile is like 70% empty space.
    //I couldve done a lot more than just grass
    private ImageTile GrassWallTile = new ImageTile("/GrassSheet.png", 16, 16);

    //each player begins with 50 gold
    private int PlayerGold = 50;
    private int EnemyGold = 50;

    //CHANGE THIS DIFFICULTY IN ORDER TO CHANGE THE BOT DIFFICULTY 
    EnemyAI bot;
    //0 for afk bot, 1 for really slow bot, 2 for regular, 3 for hard bot
    int difficulty = 2; 
    int map = 0; 

    AllyAI Abot;


    private Image level;
    private int[] tiles;
    private int[] FlowField;
    private int[] goldTiles;

    //Creating the image tiles here. This way, i can just reference the image instead of creating a whole new image, saving time
    public static ImageTile guy = new ImageTile("/guyTile1.png", 16, 16);
    public static ImageTile Knight = new ImageTile("/Knight.png", 24, 16);
    public static ImageTile Archer = new ImageTile("/Archer.png", 16, 16);
    public static ImageTile Rogue = new ImageTile("/Rogue.png", 16, 16);
    public static ImageTile RogueSlash = new ImageTile("/RogueSlash.png", 16, 16);

    public static ImageTile Mage = new ImageTile("/Mage.png", 16, 24);
    public static ImageTile Fire = new ImageTile("/Fire.png", 16, 16);

    public static ImageTile Smoke = new ImageTile("/Smoke.png", 16, 16);

    public static ImageTile Miner = new ImageTile("/Miner.png", 24, 20);

    public static ImageTile Castle = new ImageTile("/Castle.png", 80, 80);
    public static ImageTile Barracks = new ImageTile("/Barracks.png", 48, 48);

    public static ImageTile SelectedUnit = new ImageTile("/SelectedUnit.png", 128, 128);

    public static Image paused = new Image("/Pause.png");
    public static Image selectRing = new Image("/SelectRing.png");

    
//The same idea applies to the soundclips. If i have arrays containing all the sound clips, I would not need to create them again to hear them
//It saves on the processing and the memeory
    public static ArrayList<SoundClip> KnightSounds = new ArrayList<SoundClip>();
    public static ArrayList<SoundClip> ArcherSounds = new ArrayList<SoundClip>();
    public static ArrayList<SoundClip> MageSounds = new ArrayList<SoundClip>();
    public static ArrayList<SoundClip> RogueSounds = new ArrayList<SoundClip>();
    public static ArrayList<SoundClip> MinerSounds = new ArrayList<SoundClip>();
    public static ArrayList<SoundClip> AttackSounds = new ArrayList<SoundClip>();
    public static ArrayList<SoundClip> Music1 = new ArrayList<SoundClip>();
    public static ArrayList<SoundClip> Music2 = new ArrayList<SoundClip>();


//Temporary searching when without a target
    float tempsearch = 0;

    private ArrayList<GUnit>[] UnitMap;
    private ArrayList<GUnit>[] EUnitMap;

    private ArrayList<Miner>[] MinerMap;
    private ArrayList<Miner>[] EMinerMap;
    
    private ArrayList<Integer> damageMap;
    private ArrayList<Integer> edamageMap;
    
    private GStructure[] StructureMap;
    private GStructure[] EStructureMap;

    private ArrayList<GObject>[] ControlGroups;
    //It would be inefficient to loop through the object array that contains every single object over and over again when you know the likely location of a unit
    //You can access arraylists of objects that are presently standing on a certain tile through these maps. These 4 maps all serve different purposes

    private int levelW, levelH;
    private ArrayList<GObject> objects = new ArrayList<GObject>();
    private Camera camera;
    private UI UI;

    private Rectangle mouseBox;
    private int rx,ry;

    //These variables are varibles that are kind of cheating, but I used them to debug and test my code. You can turn them on if you like through the numpad
    //More detail will be in the read me document
    //Test Numpad Variables
    Boolean showgrid = false;
    Boolean showFlowField = false;
    Boolean showHitbox = false;
    Boolean showPath = false;
    Boolean showUnitMap = false;
    Boolean showUnitHealth = true;
    Boolean godSelect = false;
    Boolean showMinerMap = false;
    Boolean MuteMusic = false;
    Boolean Pause = false;
    Boolean aiControl = false;



    public GameManager(int map, int difficulty, int allyAI){
        // loadLevel("/WallTest.png");
        
        this.map = map;
        this.difficulty = difficulty;
        if(allyAI == 1){
            aiControl = true;
        }else{
            aiControl = false;
        }

        if(map == 0){
            loadLevel("/Map.png");
        }else
        {
            loadLevel("/LavaMap.png");
        }
        paused.setAlpha(true);
        selectRing.setAlpha(true);


        
        


        camera = new Camera();
        UI = new UI(this);
        
        //Pain. I had to initialize all the sound files, and i realized how much there really was
        //1-4 spawn, 5-8 select, 9-11 move, 12-14 AMove, 15-18 attack, 18-20 death
        KnightSounds.add(new SoundClip("/audio/Knight/KnightSpawn1.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightSpawn2.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightSpawn1.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightSpawn4.wav"));
        KnightSounds.add(new SoundClip("/audio/Knight/KnightSelect1.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightSelect2.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightSelect3.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightSelect4.wav"));
        KnightSounds.add(new SoundClip("/audio/Knight/KnightMove1.wav"));KnightSounds.add(new SoundClip("/audio/Knight/KnightMove2.wav"));KnightSounds.add(new SoundClip("/audio/Knight/KnightMove3.wav"));
        KnightSounds.add(new SoundClip("/audio/Knight/KnightAMove1.wav"));KnightSounds.add(new SoundClip("/audio/Knight/KnightAMove2.wav"));KnightSounds.add(new SoundClip("/audio/Knight/KnightAMove3.wav"));
        KnightSounds.add(new SoundClip("/audio/Knight/KnightAttack1.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightAttack2.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightAttack3.wav")); KnightSounds.add(new SoundClip("/audio/Knight/KnightAttack4.wav"));
        KnightSounds.add(new SoundClip("/audio/Knight/KnightDeath1.wav"));KnightSounds.add(new SoundClip("/audio/Knight/KnightDeath2.wav"));KnightSounds.add(new SoundClip("/audio/Knight/KnightDeath3.wav"));
        
        for(SoundClip i : KnightSounds){
            i.setVolume(-19);
        }
        //1.wav-3.wav spawn, 4.wav-6.wav select,7.wav-1.wav0 move, 1.wav1.wav-1.wav5.wav, amove, 1.wav6.wav-1.wav7.wav attack, 1.wav7.wav-1.wav8 death
        ArcherSounds.add(new SoundClip("/audio/Archer/ArcherSpawn3.wav")); ArcherSounds.add(new SoundClip("/audio/Archer/ArcherSpawn3.wav")); ArcherSounds.add(new SoundClip("/audio/Archer/ArcherSpawn3.wav")); 
        ArcherSounds.add(new SoundClip("/audio/Archer/ArcherSelect1.wav")); ArcherSounds.add(new SoundClip("/audio/Archer/ArcherSelect2.wav")); ArcherSounds.add(new SoundClip("/audio/Archer/ArcherSelect3.wav")); 
        ArcherSounds.add(new SoundClip("/audio/Archer/ArcherMove1.wav"));ArcherSounds.add(new SoundClip("/audio/Archer/ArcherMove2.wav"));ArcherSounds.add(new SoundClip("/audio/Archer/ArcherMove3.wav"));ArcherSounds.add(new SoundClip("/audio/Archer/ArcherMove4.wav"));
        ArcherSounds.add(new SoundClip("/audio/Archer/ArcherAMove1.wav"));ArcherSounds.add(new SoundClip("/audio/Archer/ArcherAMove2.wav"));ArcherSounds.add(new SoundClip("/audio/Archer/ArcherAMove3.wav"));ArcherSounds.add(new SoundClip("/audio/Archer/ArcherAMove4.wav"));ArcherSounds.add(new SoundClip("/audio/Archer/ArcherAMove5.wav"));
        ArcherSounds.add(new SoundClip("/audio/Archer/ArcherAttack1.wav")); ArcherSounds.add(new SoundClip("/audio/Archer/ArcherAttack2.wav")); 
        ArcherSounds.add(new SoundClip("/audio/Archer/ArcherDeath1.wav"));ArcherSounds.add(new SoundClip("/audio/Archer/ArcherDeath2.wav"));
        for(SoundClip i : ArcherSounds){
            i.setVolume(-19);
        }
        //1.wav-3.wav spawn, 4.wav-7.wav select, 8-1.wav0 move, 1.wav1.wav-1.wav4.wav amove, 1.wav5.wav-2.wav0 attack, 2.wav1.wav-2.wav3.wav death
        MageSounds.add(new SoundClip("/audio/Mage/MageSpawn1.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageSpawn2.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageSpawn3.wav")); 
        MageSounds.add(new SoundClip("/audio/Mage/MageSelect1.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageSelect2.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageSelect3.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageSelect4.wav"));
        MageSounds.add(new SoundClip("/audio/Mage/MageMove1.wav"));MageSounds.add(new SoundClip("/audio/Mage/MageMove2.wav"));MageSounds.add(new SoundClip("/audio/Mage/MageMove3.wav"));
        MageSounds.add(new SoundClip("/audio/Mage/MageAMove1.wav"));MageSounds.add(new SoundClip("/audio/Mage/MageAMove2.wav"));MageSounds.add(new SoundClip("/audio/Mage/MageAMove3.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageAMove4.wav"));
        MageSounds.add(new SoundClip("/audio/Mage/MageAttack1.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageAttack2.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageAttack3.wav")); MageSounds.add(new SoundClip("/audio/Mage/MageAttack4.wav"));MageSounds.add(new SoundClip("/audio/Mage/MageAttack5.wav"));MageSounds.add(new SoundClip("/audio/Mage/MageAttack6.wav"));
        MageSounds.add(new SoundClip("/audio/Mage/MageDeath1.wav"));MageSounds.add(new SoundClip("/audio/Mage/MageDeath2.wav"));MageSounds.add(new SoundClip("/audio/Mage/MageDeath3.wav"));
        for(SoundClip i : MageSounds){
            i.setVolume(-19);
        }
        
        //1.wav-3.wav spawn, 4.wav-7.wav select, 8-1.wav0 move, 1.wav1.wav-1.wav3.wav amove, 1.wav4.wav-1.wav6.wav attack, 1.wav7.wav-1.wav9 death
        RogueSounds.add(new SoundClip("/audio/Rogue/RogueSpawn1.wav")); RogueSounds.add(new SoundClip("/audio/Rogue/RogueSpawn2.wav")); RogueSounds.add(new SoundClip("/audio/Rogue/RogueSpawn3.wav")); 
        RogueSounds.add(new SoundClip("/audio/Rogue/RogueSelect1.wav")); RogueSounds.add(new SoundClip("/audio/Rogue/RogueSelect2.wav")); RogueSounds.add(new SoundClip("/audio/Rogue/RogueSelect3.wav")); RogueSounds.add(new SoundClip("/audio/Rogue/RogueSelect4.wav"));
        RogueSounds.add(new SoundClip("/audio/Rogue/RogueMove1.wav"));RogueSounds.add(new SoundClip("/audio/Rogue/RogueMove2.wav"));RogueSounds.add(new SoundClip("/audio/Rogue/RogueMove3.wav"));
        RogueSounds.add(new SoundClip("/audio/Rogue/RogueAMove1.wav"));RogueSounds.add(new SoundClip("/audio/Rogue/RogueAMove2.wav"));RogueSounds.add(new SoundClip("/audio/Rogue/RogueAMove3.wav"));
        RogueSounds.add(new SoundClip("/audio/Rogue/RogueAttack1.wav")); RogueSounds.add(new SoundClip("/audio/Rogue/RogueAttack2.wav")); RogueSounds.add(new SoundClip("/audio/Rogue/RogueAttack3.wav"));
        RogueSounds.add(new SoundClip("/audio/Rogue/RogueDeath1.wav"));RogueSounds.add(new SoundClip("/audio/Rogue/RogueDeath2.wav"));
        for(SoundClip i : RogueSounds){
            i.setVolume(-19);
        }
        
        //1-3 spawn 4-6 select, 7-10 move, 11-13 amove 14-16 attack, 17-21 death
        MinerSounds.add(new SoundClip("/audio/Miner/MinerSpawn1.wav")); MinerSounds.add(new SoundClip("/audio/Miner/MinerSpawn2.wav")); MinerSounds.add(new SoundClip("/audio/Miner/MinerSpawn3.wav")); 
        MinerSounds.add(new SoundClip("/audio/Miner/MinerSelect1.wav")); MinerSounds.add(new SoundClip("/audio/Miner/MinerSelect2.wav")); MinerSounds.add(new SoundClip("/audio/Miner/MinerSelect3.wav"));
        MinerSounds.add(new SoundClip("/audio/Miner/MinerMove1.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerMove2.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerMove3.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerMove4.wav"));
        MinerSounds.add(new SoundClip("/audio/Miner/MinerAMove1.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerAMove2.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerAMove3.wav"));
        MinerSounds.add(new SoundClip("/audio/Miner/MinerAttack1.wav")); MinerSounds.add(new SoundClip("/audio/Miner/MinerAttack2.wav")); MinerSounds.add(new SoundClip("/audio/Miner/MinerAttack3.wav")); 
        MinerSounds.add(new SoundClip("/audio/Miner/MinerDeath1.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerDeath2.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerDeath3.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerDeath4.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerDeath5.wav"));
        // MinerSounds.add(new SoundClip("/audio/Miner/MinerBuild1.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerBuild2.wav"));MinerSounds.add(new SoundClip("/audio/Miner/MinerBuild3.wav"));
        for(SoundClip i : MinerSounds){
            i.setVolume(-19);
        }
        //1-3 archer, 4 fire, 5-8knight, 9-11 mage, 12-13 miner, 14-15 rogue
        AttackSounds.add(new SoundClip("/audio/AttackSounds/A1.wav"));AttackSounds.add(new SoundClip("/audio/AttackSounds/A2.wav"));AttackSounds.add(new SoundClip("/audio/AttackSounds/A3.wav"));
        AttackSounds.add(new SoundClip("/audio/AttackSounds/F1.wav"));
        AttackSounds.add(new SoundClip("/audio/AttackSounds/K1.wav"));AttackSounds.add(new SoundClip("/audio/AttackSounds/K2.wav"));AttackSounds.add(new SoundClip("/audio/AttackSounds/K3.wav"));AttackSounds.add(new SoundClip("/audio/AttackSounds/K4.wav"));
        AttackSounds.add(new SoundClip("/audio/AttackSounds/M1.wav"));;AttackSounds.add(new SoundClip("/audio/AttackSounds/M3.wav"));
        AttackSounds.add(new SoundClip("/audio/AttackSounds/Mi2.aiff"));
        AttackSounds.add(new SoundClip("/audio/AttackSounds/R1.wav"));AttackSounds.add(new SoundClip("/audio/AttackSounds/R2.wav"));
        
        for(SoundClip i : AttackSounds){
            i.setVolume(-19);
        }

        
        Music1.add(new SoundClip("/audio/Music/Muse1.wav"));Music1.add(new SoundClip("/audio/Music/Muse2.wav"));Music1.add(new SoundClip("/audio/Music/Muse3.wav"));
        Music2.add(new SoundClip("/audio/Music/Muse4.wav"));Music2.add(new SoundClip("/audio/Music/Muse5.wav"));Music2.add(new SoundClip("/audio/Music/Muse6.wav"));
        for(SoundClip i : Music1){
            i.setVolume(-12);
        }
        for(SoundClip i : Music2){
            i.setVolume(-20);
        }
}
    

//I load the level in from an image provided by the string. I used an image over a 2d array because an image is essentially just a 
//bigger 2d array but with colours. I thought it would make it easier to create and form levels with a drawing application rather than a
//application such as excel
//It was also nice to be able to design both maps and arts in the same application. I used gimp
    public void loadLevel(String path){
        level = new Image(path);
        levelW = level.getW();
        levelH = level.getH();
        damageMap = new ArrayList<Integer>();
        edamageMap = new ArrayList<Integer>();

        tiles = new int[levelW * levelH];
        goldTiles = new int[levelW * levelH];
        UnitMap = new ArrayList[levelW * levelH];
        EUnitMap = new ArrayList[levelW * levelH]; 
        MinerMap = new ArrayList[levelW * levelH];
        EMinerMap = new ArrayList[levelW * levelH]; 
        StructureMap = new GStructure[levelW * levelH];
        EStructureMap = new GStructure[levelW * levelH];
        ControlGroups = new ArrayList[12];
        for(int i = 0; i < UnitMap.length; i++){
            UnitMap[i] = new ArrayList<GUnit>();
            EUnitMap[i] = new ArrayList<GUnit>();
            MinerMap[i] = new ArrayList<Miner>();
            EMinerMap[i] = new ArrayList<Miner>();
        }
        for(int i = 0; i < 12; i++){
            ControlGroups[i] = new ArrayList<GObject>();
        }
        

        //The way that I drew the ground was that each set of 13, have the exact same properties, but with a different art.
        //meaning that from 0 to 12, the tiles are functionally identical, differing only in design. 
        //with this, I can build walls by saying anything / 4 is 5, and that gives me a range of 4 units due to the properties of integer division.
        for(int y = 0; y < levelH; y++){
            for(int x = 0; x < levelW; x++){
                int pixel = x + y * levelW;
                
                //I have labeled which tile is assigned to which colour and labeled them below.These are the same colours that represent the minimap
                switch(level.getP()[pixel]){
                    //Grass Tiles
                    case(0xff33f133):
                    tiles[pixel] = 0; break;
                    case(0xff33f233):
                    tiles[pixel] = 1; break;
                    case(0xff33f333):
                    tiles[pixel] = 2; break;
                    case(0xff33f433):
                    tiles[pixel] = 3; break;

                    //Road Tiles
                    case(0xff919191):
                    tiles[pixel] = 4; break;
                    case(0xff929292):
                    tiles[pixel] = 5; break;
                    case(0xff939393):
                    tiles[pixel] = 6; break;

                    //sand tiles
                    case(0xffffff91):
                    tiles[pixel] = 7; break;
                    case(0xffffff92):
                    tiles[pixel] = 8; break;
                    case(0xffffff93):
                    tiles[pixel] = 9; break;
                    case(0xffffff94):
                    tiles[pixel] = 10; break;

                    //water tiles
                    case(0xff2555f1):
                    tiles[pixel] = 22; break;

                    //gold tiles
                    case(0xffffd700):
                    tiles[pixel] = 29; break;
                    
                    //------------------------------------
                    //Map 2
                    //Purple Stone
                    case(0xff00187a):
                    tiles[pixel] = 39; break;
                    case(0xff01187a):
                    tiles[pixel] = 40; break;
                    case(0xff02187a):
                    tiles[pixel] = 41; break;
                    case(0xff03187a):
                    tiles[pixel] = 42; break;

                    //Red Stone
                    case(0xffa10e2e):
                    tiles[pixel] = 43; break;
                    case(0xffa20e2e):
                    tiles[pixel] = 44; break;

                    //Magma
                    case(0xff850004):
                    tiles[pixel] = 45; break;
                    case(0xff850104):
                    tiles[pixel] = 46; break;

                    //Marble
                    case(0xffc3c3c3):
                    tiles[pixel] = 47; break;
                    case(0xffc4c4c4):
                    tiles[pixel] = 48; break;

                    //Ash
                    case(0xff707070):
                    tiles[pixel] = 49; break;
                    case(0xff717171):
                    tiles[pixel] = 50; break;
                    case(0xff727272):
                    tiles[pixel] = 51; break;

                    //Purple Wall & Crystals
                    case(0xff32003b):
                    tiles[pixel] = 52; break;
                    case(0xff32013b):
                    tiles[pixel] = 53; break;
                    case(0xff32023b):
                    tiles[pixel] = 54; break;
                    case(0xff32033b):
                    tiles[pixel] = 55; break;

                    //Red Wall & skull
                    case(0xff1c0006):
                    tiles[pixel] = 56; break;
                    case(0xff1c0106):
                    tiles[pixel] = 57; break;
                    case(0xff1c0206):
                    tiles[pixel] = 58; break;

                    //Marble Wal;
                    case(0xff5e5e5e):
                    tiles[pixel] = 59; break;
                    case(0xff5f5f5f):
                    tiles[pixel] = 60; break;

                    //lava step down
                    case(0xffff4d01):
                    tiles[pixel] = 61; break;
                    //Lava;
                    
                    case(0xffff4d00):
                    tiles[pixel] = 62; break;
                    
                    


                    //grass walls
                    default:
                    tiles[pixel] = (level.getP()[pixel] >> 8 & 0xff) + 13;
                }
                
            }
        }

        //To set up the level, the structures, which are incapable of movement, are placed down.
        //the map is perfectly symmetrical to ensure an even playing field.
        Castle castle = new Castle(9,8, this,0);
        Castle Ecastle = new Castle(128 - 9 - 5,128 - 8 - 5, this,1);
        objects.add(castle);
        objects.add(Ecastle);

        
        Miner m1 = new Miner(10*16, 15*16, 0); m1.setOwner(0); objects.add(m1);
        Miner m2 = new Miner(11*16, 15*16,0); m2.setOwner(0); objects.add(m2);
        Miner m3 = new Miner(12*16, 15*16,0); m3.setOwner(0); objects.add(m3);

        Miner e1 = new Miner((128-10)*16, (128-15)*16,0); e1.setOwner(1); objects.add(e1);
        Miner e2 = new Miner((128-11)*16, (128-15)*16,0); e2.setOwner(1); objects.add(e2);
        Miner e3 = new Miner((128-12)*16, (128-15)*16,0); e3.setOwner(1); objects.add(e3);

        if(map == 0){
            Barracks barracks1 = new Barracks(38,18, this,-1);
            Barracks Ebarracks1 = new Barracks(128-38-3,128-18-3, this,-1);
            objects.add(barracks1);
            objects.add(Ebarracks1);
            Barracks barracks2 = new Barracks(82,12, this,-1);
            Barracks Ebarracks2 = new Barracks(128-82-3,128-12-3, this,-1);
            objects.add(barracks2);
            objects.add(Ebarracks2);
            Barracks barracks3 = new Barracks(104,22, this,-1);
            Barracks Ebarracks3 = new Barracks(128-104-3,128-22-3, this,-1);
            objects.add(barracks3);
            objects.add(Ebarracks3);
            Barracks barracks4 = new Barracks(42,58, this,-1);
            Barracks Ebarracks4 = new Barracks(128-42-3,128-58-3, this,-1);
            objects.add(barracks4);
            objects.add(Ebarracks4);
        }
        else{
            Barracks barracks1 = new Barracks(27,14, this,-1);
            Barracks Ebarracks1 = new Barracks(128-27-3,128-14-3, this,-1);
            objects.add(barracks1);
            objects.add(Ebarracks1);
            Barracks barracks2 = new Barracks(10,45, this,-1);
            Barracks Ebarracks2 = new Barracks(128-10-3,128-45-3, this,-1);
            objects.add(barracks2);
            objects.add(Ebarracks2);
            Barracks barracks3 = new Barracks(42,45, this,-1);
            Barracks Ebarracks3 = new Barracks(128-42-3,128-45-3, this,-1);
            objects.add(barracks3);
            objects.add(Ebarracks3);
            Barracks barracks4 = new Barracks(83,33, this,-1);
            Barracks Ebarracks4 = new Barracks(128-83-3,128-33-3, this,-1);
            objects.add(barracks4);
            objects.add(Ebarracks4);
            Barracks barracks5 = new Barracks(112,12, this,-1);
            Barracks Ebarracks5 = new Barracks(128-112-3,128-12-3, this,-1);
            objects.add(barracks5);
            objects.add(Ebarracks5);
        }

        
        
        //After everything is established, the bot is created. It is created here so that it can read the data and deicde what moves due to its access to gm

        bot = new EnemyAI(this, difficulty);
        
        Abot = new AllyAI(this);
    }

//While the game is running, as long as the music isnt muted, play music
    public void update(GameContainer gc, float dt) {
        if(gc.getInput().isKeyDown(KeyEvent.VK_ESCAPE)){
            Pause ^= true;
        }
        if(Pause){
            return;
        }
        camera.update(gc, this, dt);
        UI.update(gc, this, dt);
        
        if(map == 0){
            if(!MuteMusic){
                if(!(Music1.get(0).isRunning() || Music1.get(1).isRunning() || Music1.get(2).isRunning())){
                    Music1.get((int)(Math.random() * 3)).play();
                }
            }else{
                for(SoundClip i : Music1) {
                    i.stop();
                }
            }
        }else{
            if(!MuteMusic){
                if(!(Music2.get(0).isRunning() || Music2.get(1).isRunning() || Music2.get(2).isRunning())){
                    Music2.get((int)(Math.random() * 3)).play();
                }
            }else{
                for(SoundClip i : Music2) {
                    i.stop();
                }
            }
        }
        
        

        if(!aiControl){

        
        //If you have godselect on, you are able to create units, both enemy ones and yours, just by tapping buttons. You can also control and select enemy units.
        //-----------------------------------------------
        int owner = 0;
        if(godSelect){
        if(gc.getInput().isKey(KeyEvent.VK_CONTROL)){
             owner = 1;
        }
        if(gc.getInput().isButtonDown(MouseEvent.BUTTON2)){
            guy guy = new guy(gc.getInput().getRMouseX(), gc.getInput().getRMouseY());
            guy.setOwner(owner);
            objects.add(guy);
            System.out.println("guy");
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_Z)){
            Knight knight = new Knight(gc.getInput().getRMouseX(), gc.getInput().getRMouseY());
            knight.setOwner(owner);
            objects.add(knight);
            System.out.println("Knight");
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_X)){
            Archer archer = new Archer(gc.getInput().getRMouseX(), gc.getInput().getRMouseY());
            archer.setOwner(owner);
            objects.add(archer);
            System.out.println("Archer");
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_C)){
            Rogue rogue = new Rogue(gc.getInput().getRMouseX(), gc.getInput().getRMouseY());
            rogue.setOwner(owner);
            objects.add(rogue);
            System.out.println("Rogue");
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_V)){
            Mage mage = new Mage(gc.getInput().getRMouseX(), gc.getInput().getRMouseY());
            mage.setOwner(owner);
            objects.add(mage);
            System.out.println("Mage");
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_B)){
            Miner miner = new Miner(gc.getInput().getRMouseX(), gc.getInput().getRMouseY());
            miner.setOwner(owner);
            objects.add(miner);
            System.out.println("Miner");
        }
    }

        //-----------------------------------------------
        //Setting the control group
        if(gc.getInput().isKey(KeyEvent.VK_CONTROL)){
            if(gc.getInput().isKeyDown(KeyEvent.VK_1)){
                ControlGroups[1].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[1].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_2)){
                ControlGroups[2].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[2].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_3)){
                ControlGroups[3].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[3].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_4)){
                ControlGroups[4].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[4].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_5)){
                ControlGroups[5].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[5].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_6)){
                ControlGroups[6].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[6].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_7)){
                ControlGroups[7].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[7].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_8)){
                ControlGroups[8].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[8].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_9)){
                ControlGroups[9].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[9].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_0)){
                ControlGroups[0].clear();
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[0].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_F1)){
                for(GObject i : objects){
                    if(i instanceof Miner && tiles[((GUnit) i).getCenterTile()] / 13 != 2 && owner == 0 && ((GUnit) i).getPath().isEmpty()){
                        i.setSelectStatus(1);
                    }
                }
            }
        }
        //-------------------------------------------------------------------------------------------
        //Adding to control groups
        if(gc.getInput().isKey(KeyEvent.VK_SHIFT)){
            if(gc.getInput().isKeyDown(KeyEvent.VK_A)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ((GUnit) i).searchTarget(this, gc.getInput().getMouseX() / 16, gc.getInput().getMouseY() / 16);
                    }
                }
            }
            if(gc.getInput().isKeyDown(KeyEvent.VK_1)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[1].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_2)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[2].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_3)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[3].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_4)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[4].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_5)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[5].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_6)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[6].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_7)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[7].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_8)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[8].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_9)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[9].add(i);
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_0)){
                for(GObject i : objects){
                    if(i.getSelectStatus() == 1){
                        ControlGroups[0].add(i);
                    }
                }
            }else if(gc.getInput().isKeyDown(KeyEvent.VK_F1)){
                for(GObject i : objects){
                    if(i instanceof Miner && tiles[((GUnit) i).getCenterTile()] / 13 != 2 && owner == 0 && i.getSelectStatus() == 0 && ((GUnit) i).getPath().isEmpty()){
                        i.setSelectStatus(1);
                        break;
                    }
                }
            }
        }
    }
        //--------------------------------------------------------------------------------------
        //Selecting
        if(!aiControl){

        
            if(gc.getInput().isKeyDown(KeyEvent.VK_1)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[1]){
                    if(i.isRemove()){
                        ControlGroups[1].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_2)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[2]){
                    if(i.isRemove()){
                        ControlGroups[2].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_3)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[3]){
                    if(i.isRemove()){
                        ControlGroups[3].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_4)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[4]){
                    if(i.isRemove()){
                        ControlGroups[4].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_5)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[5]){
                    if(i.isRemove()){
                        ControlGroups[5].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_6)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[6]){
                    if(i.isRemove()){
                        ControlGroups[6].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_7)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[7]){
                    if(i.isRemove()){
                        ControlGroups[7].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_8)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[8]){
                    if(i.isRemove()){
                        ControlGroups[8].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_9)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[9]){
                    if(i.isRemove()){
                        ControlGroups[9].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_0)){
                for(GObject i : objects){
                    i.setSelectStatus(0);
                }
                for(GObject i : ControlGroups[0]){
                    if(i.isRemove()){
                        ControlGroups[0].remove(i);
                        continue;
                    }
                    i.setSelectStatus(1);
                    UI.getObjectBox().setRenderOb(i);
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_F1)){
                
                for(GObject i : objects){
                    if(i instanceof Miner && tiles[((GUnit) i).getCenterTile()] / 13 != 2 && i.getOwner()== 0 && i.getSelectStatus() == 0 && ((GUnit) i).getPath().isEmpty()) {
                        for(GObject j : objects){
                            j.setSelectStatus(0);
                        }
                        i.setSelectStatus(1);
                        break;
                    }
                }
            }
            else if(gc.getInput().isKeyDown(KeyEvent.VK_F2)){
                for(GObject i : objects){
                    if(i instanceof GUnit && !(i instanceof Miner) && i.getOwner() == 0){
                        i.setSelectStatus(1);
                    }else{
                        i.setSelectStatus(0);
                    }
                }
            }
        }
        



        //Here are some checks with the mouse and the minimap, when clicking on the minimap, the click is reflected onto the actual location of the click
        //shown on the minimap instead

        if(!aiControl){

        if(gc.getInput().isButtonDown(MouseEvent.BUTTON3) || (gc.getInput().isKeyDown(KeyEvent.VK_A) && !(gc.getInput().isKey(KeyEvent.VK_SHIFT)))){
            if(UI.getMinimap().intersects(gc.getInput().getMouseX(), gc.getInput().getMouseY())){
                GFlowField((int) gc.getInput().getMouseX(), (gc.getInput().getMouseY()-(480-128)));
            }else{
            GFlowField(gc.getInput().getRMouseX() / 16, gc.getInput().getRMouseY() / 16);
            }

        }
        if(gc.getInput().isButtonDown(MouseEvent.BUTTON1)){
            if(!UI.intersects(gc.getInput().getMouseX(), gc.getInput().getMouseY())){
                rx = gc.getInput().getRMouseX(); ry = gc.getInput().getRMouseY();
                for(int i = 0; i < objects.size(); i++){
                    objects.get(i).setSelectStatus(0);
                }
                mouseBox = new Rectangle(Math.min(rx, gc.getInput().getRMouseX()), Math.min(ry, gc.getInput().getRMouseY()),1,1);
                UI.getObjectBox().setRenderOb(null);
            }
           
            //create a box that checks for interesctions. When they intersect wiht another unit, they are selected.
        }
        if(gc.getInput().isButton(MouseEvent.BUTTON1)){
            if(mouseBox != null)
                mouseBox = new Rectangle(Math.min(rx, gc.getInput().getRMouseX()), Math.min(ry, gc.getInput().getRMouseY()),
                Math.max(Math.abs(gc.getInput().getRMouseX() - rx), 1), Math.max(Math.abs(gc.getInput().getRMouseY() - ry),1));
            
        }
        if(gc.getInput().isButtonUp(MouseEvent.BUTTON1)){
            for(int i = 0; i < objects.size(); i++){
                if(mouseBox!= null){
                    if(new Rectangle(objects.get(i).getX(),objects.get(i).getY(),objects.get(i).getWidth(),objects.get(i).getHeight()).intersects(mouseBox)){
                        if(!godSelect && objects.get(i).getOwner() != 0){
                            
                        }else{
                            objects.get(i).setSelectStatus(1);
                            UI.getObjectBox().setRenderOb(objects.get(i));
                        }
                        
                    }
                }
            }
            mouseBox = null;
        }
    }
        //Everytick, the maps are updated. All the units from the previous tick are moved to different, new tiles on the map
        for(int i = 0; i < UnitMap.length; i++){
            UnitMap[i].clear();
            EUnitMap[i].clear();
            MinerMap[i].clear();
            EMinerMap[i].clear();
            
        }
        damageMap.clear();
        edamageMap.clear();
        //Whenever a unit's health reaches zero, it is no longer alive and is removed from the game. When something is removed, it is coupled with a bit of
        //smoke, showing a death animation
        for(int i = 0; i < objects.size(); i++){
            objects.get(i).update(gc, this, dt);

            if(objects.get(i).isRemove()){
                if(objects.get(i) instanceof GUnit){
                    objects.add(new DSmoke(Smoke,30, objects.get(i).getX(), objects.get(i).getY()));
                }
                objects.remove(i);
                i--;
            }
        }
        //Mage Damage marks the tiles, then the tiles deal damage afterward
        for(int i : damageMap){
            for(GObject unit : EUnitMap[i]){
                ((GUnit) unit).setHealth(((GUnit) unit).getHealth() - 55);
                // System.out.println("Attacked " + unit.getTag() + " " + owner+"|| "+ ((GUnit) unit).getHealth() + "HP left");
            }
        }//the same thing but for enemies
        for(int i : edamageMap){
            for(GObject unit : UnitMap[i]){
                ((GUnit) unit).setHealth(((GUnit) unit).getHealth() - 30);
                // System.out.println("Attacked " + unit.getTag() + " " + owner+"|| "+ ((GUnit) unit).getHealth() + "HP left");
            }
        }

        //gaining gold, if a player has had a miner stand on top of a gold tile for 75 ticks, they earn one gold
        for(int y = 0; y < levelH; y++){
            for(int x = 0; x < levelW; x++){
                int loc = x+y*levelW;
                //if not gold tile, continue
                if(tiles[loc] / 13 != 2){
                    continue;
                }
                //it either increases or decreases dpending on who is standing on the gold tile. Hitting negative 75 grants the enemy one gold.
                if(goldTiles[loc] >= 75){
                    PlayerGold++;
                    goldTiles[loc] = 0;
                }
                if(goldTiles[loc] <= -75){
                    EnemyGold++;
                    goldTiles[loc] = 0;
                }
                //If two miners from the same team stand on the tile, the speed does not double. In fact, nothing changes
                if(MinerMap[loc].size() >= 1 && EMinerMap[loc].size() == 0){
                    goldTiles[loc]++;
                } else if(MinerMap[loc].size() == 0 && EMinerMap[loc].size() >= 1){
                    goldTiles[loc]--;
                } else if(MinerMap[loc].size() ==0 && EMinerMap[loc].size() ==0 && goldTiles[loc] != 0){
                    goldTiles[loc] -= 1 * (goldTiles[loc] / Math.abs(goldTiles[loc]));
                }

                //the tiles at the location recieve an art based on how far along they are in granting gold
                tiles[loc] = 29 + (goldTiles[loc] / 20);
                
            }
        }


        //Because constantly searching aroudn every single unit is a intensive process, this is only done 8 times a second instead of the expected 60.
        //The program runs smoother, and the loss in speed in almost irrelevant because it only affects how fast a unit is able to choose a new target after
        //losing its previous one. None of the units can attack faster than 8 times a second anyway.
        tempsearch += dt;
        if(tempsearch > 0.125){
            for(int i = 0; i < objects.size(); i++){
                if(objects.get(i) instanceof GUnit){
                    GUnit current = (GUnit) objects.get(i);
                    if(!current.isAttackMoved()){
                        current.setAttacking(false);
                        continue;
                    } 
                    if(current.getTarget() == null){
                        current.setAttacking(false);
                        current.searchTarget(this);
                    }else{
                        if(((GUnit) current.getTarget()).getHealth() <= 0) {
                            current.setTarget(null);
                            continue;
                        }
                        if(current.distance(((GUnit)(current.getTarget())).getCenterTile(), this) > current.getLeashRange()){
                            current.setTarget(null);
                            current.setAttacking(false);
                        } else if (current.distance(((GUnit)(current.getTarget())).getCenterTile(), this) > current.getAttackRange()){
                            current.astar(this, current.getCenterTile(), ((GUnit)(current.getTarget())).getCenterTile());
                            current.setAttacking(false);
                            // System.out.println(current.getTarget().getTag() + " " + current.getTarget().getX() + " " + current.getTarget().getY());
                        } else{
                            if(current.getTarget().getX() > current.getX()){
                                current.setDirection(0);
                            }else{
                                current.setDirection(1);
                            }
                            
                            current.setAttacking(true);
                            if(current.getPath() != null)
                                current.getPath().clear();
                        }
                    }
                    
                }
            }
            tempsearch = 0;
        }

        

        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD1)){
            showgrid ^= true;
            
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD2)){
            showFlowField ^= true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD3)){
            showHitbox ^= true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD4)){
            showPath ^= true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD5)){
            showUnitMap ^= true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD6)){
            showUnitHealth ^= true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD7)){
            godSelect ^= true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD8)){
            showMinerMap ^= true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_NUMPAD9)){
            MuteMusic ^= true;
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_END)){
            aiControl ^= true;
        }

        //------------------------------------------------------------------------------------------------------
        //This is the scary line to uncover. I believe that I am creating too many objects and not enough are being removed by java's garbage
        //collector, so the game fizzes out after aroudn a minute. However, uncomment line 570 to see the ai in its full glory of rng.
        bot.update(gc,dt);
        if(aiControl)
        Abot.update(gc,dt);
        //----------------------------------------------------------------------------------------------------
    }




    public void render(GameContainer gc, Renderer r) {

        if(Pause){
            r.setzDepth(9999999);
            r.drawImage(paused,0 + r.getCX(),0 + r.getCY());
            r.setzDepth(0);
        }
        
        camera.render(gc, this, r);
        //This flips through the image tile and draws a picture for each tile, depending on the value of the tile
        for(int y = 0; y < levelH; y++){
            for(int x = 0; x < levelW; x++){
                if(tiles[x + y * levelW] >= 22 && tiles[x+y*levelW] <= 25 && (int)(Math.random()*90) == 0){
                    tiles[x+y*levelW] = (int)(Math.random()*4) + 22;
                }
                if(tiles[x+y*levelW] >= 62 && tiles[x+y*levelW] <= 64 && (int)(Math.random()*90) == 0){
                    tiles[x+y*levelW] = (int)(Math.random()*3) + 62;
                    
                }

                r.drawImageTile(GrassWallTile, x*TILE_SIZE, y*TILE_SIZE, tiles[(x + y * levelW)] % 13, tiles[(x + y * levelW)] / 13);
                
            }
        }
        //debug stuff. It allows me to see the flowfield units are pathfinding with
        if(showFlowField){
            r.drawText("2", r.getCX() + 620, r.getCY() +40, 0xffffffff);
        
        if(FlowField != null){
            for(int y = 0; y < levelH; y++){
                for(int x = 0; x < levelW; x++){
                    if(FlowField[x + y * levelW] == 65535){
                        r.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, (0xff000000));
                    }else{
                        int red = FlowField[x + y * levelW];
                        int blue = 0;
                        int green = 255 - FlowField[x + y * levelW] * 5;
                        r.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, (255 << 24 | red << 16 | green << 8 | blue));
                    }
                    

    
                }
            }
        }
    }

    //shows the path and tile map. I placed a small number in the top right corner showing which "Cheats" are enabled
        if(showPath)
        r.drawText("4", r.getCX() + 610, r.getCY() +30, 0xffffffff);
        if(showUnitMap)
        r.drawText("5", r.getCX() + 620, r.getCY() +30, 0xffffffff);
        if(showUnitHealth){
            r.drawText("6", r.getCX() + 630, r.getCY() +30, 0xffffffff);
        }
        if(godSelect){
            r.drawText("7", r.getCX() + 610, r.getCY() +20, 0xffffffff);
        }
        if(MuteMusic){
            r.drawText("9", r.getCX() + 630, r.getCY() +20, 0xffffffff);
        }

        if(showgrid){
        for(int y = 0; y < levelH; y++){
            for(int x = 0; x < levelW; x++){
                r.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE ,TILE_SIZE, 0xa229af79);
                r.drawText(""+tiles[x+y*levelW], x * TILE_SIZE, y * TILE_SIZE, 0xff000000);
            }
        }
        r.drawText("1", r.getCX() + 610, r.getCY() +40, 0xffffffff);
    }
        if(showMinerMap){
            r.drawText("8", r.getCX() + 620, r.getCY() +20, 0xffffffff);
        for(int y = 0; y < levelH; y++){
            for(int x = 0; x < levelW; x++){
                //show only gold tiles
                if(tiles[x + y * levelW] / 13 == 2){
                    r.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE ,TILE_SIZE, 0xa229af79);
                    r.drawText(""+goldTiles[x+y*levelW], x * TILE_SIZE, y * TILE_SIZE, 0xffd3d3d3);
                    r.drawText(""+MinerMap[x+y*levelW].size(), x * TILE_SIZE + 10, y * TILE_SIZE, 0xffd3d3d3);
                    r.drawText(""+EMinerMap[x+y*levelW].size(), x * TILE_SIZE + 10, y * TILE_SIZE + 10, 0xffd3d3d3);
                }
            }
        }
    }
        

        for(int i = 0; i < objects.size(); i++){
            objects.get(i).render(gc,this, r);
        }
        if(mouseBox != null){
            r.fillRect(mouseBox.x - r.getCX() + r.getCX(), mouseBox.y - r.getCY() + r.getCY(), (int)mouseBox.getWidth(), (int)mouseBox.getHeight(), 0x47eb34d8);
            r.drawRect(mouseBox.x - r.getCX() + r.getCX() , mouseBox.y - r.getCY() + r.getCY(), (int)mouseBox.getWidth(), (int)mouseBox.getHeight(), 0xff0048d9);
        }
        if(showUnitMap){
            for(int y = 0; y < levelH; y++){
                for(int x = 0; x < levelW; x++){
                    r.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE ,TILE_SIZE, 0xa229af79);
                    r.drawText(""+UnitMap[x+y*levelW].size(), x*TILE_SIZE, y*TILE_SIZE, 0xff000000);
                    r.drawText(""+EUnitMap[x+y*levelW].size(), x*TILE_SIZE, y*TILE_SIZE+8, 0xffffffff);
                }
            }
        }
        if(showHitbox){
            r.drawRect(gc.getInput().getRMouseX() / 16 * 16, gc.getInput().getRMouseY() / 16 * 16,16,16, 0xffff00ff);
            r.drawText("3", r.getCX() + 630, r.getCY() +40, 0xffffffff);
        }
        UI.render(this, r);
        
    }
    


    public void GFlowField(int GoalX, int GoalY){
        //GFlowField, or generate flow field, creates the flowfield for pathfinding the units. It works on every single right click or a button click
        //It is just straight up djikstras on the grid, and creates a path from every single tile to the goal tile, or the one you clicked on. This allows every
        //single unit to be given a path to the location of your mouse.

        FlowField = new int[levelH * levelW];
        int[] Costs = new int[levelH * levelW];
        Queue<Integer> toVisit = new LinkedList<Integer>();
        for(int i = 0; i < tiles.length; i++){
            if((tiles[i] >= 13 && tiles[i] < 26) || (tiles[i] >= 52 && tiles[i] < 65)){
                Costs[i] = 65535;
            }
            else{
                Costs[i] = 1;
            }
            FlowField[i] = 65535;
        }
        if(Costs[GoalX + GoalY * levelW] != 65535){

            FlowField[GoalX + GoalY * levelW] = 0;
            
        }
        toVisit.add(GoalX + GoalY * levelW);
        while(!toVisit.isEmpty()){
            int current = toVisit.poll();
            int ci = current % levelW;
            int cj = current / levelW;
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    if(ci + i < 0 || cj + j < 0 || ci + i >= levelW || cj + j >= levelH){
                        continue;
                    }
                    if(FlowField[current] + Costs[(ci + i) + (cj + j) * levelW] < FlowField[(ci + i) + (cj + j) * levelW]){
                        FlowField[(ci + i) + (cj + j) * levelW] = FlowField[current] + Costs[(ci + i) + (cj + j) * levelW];
                        toVisit.add((ci + i) + (cj + j) * levelW);
                    }
                }
            }
        }
        // for(int y = 0; y < levelH; y++){
        //     for(int x = 0; x < levelW; x++){
        //         System.out.print(FlowField[x + y * levelW] + " ");
        //     }
        //     System.out.println();
        // }
        
    }

    //Start the game
    // public static void main(String args[]){
    //     //Int Difficulty, int Map, int AllyAI
    //     //difficulty is the difficulty of the enemy, 0-3;
    //     //0 for forest map, 1 for cursed volcano map
    //     //0 for no ally ai, 1 for ally ai
    //     GameContainer gc = new GameContainer(new GameManager(0, 1, 0));
    //     gc.start();
    // }

    /**
     * @return the levelW
     */
    public int getLevelW() {
        return levelW;
    }

    /**
     * @param levelW the levelW to set
     */
    public void setLevelW(int levelW) {
        this.levelW = levelW;
    }

    /**
     * @return the levelH
     */
    public int getLevelH() {
        return levelH;
    }

    /**
     * @param levelH the levelH to set
     */
    public void setLevelH(int levelH) {
        this.levelH = levelH;
    }

    /**
     * @return the flowField
     */
    public int[] getFlowField() {
        return FlowField;
    }

    /**
     * @param flowField the flowField to set
     */
    public void setFlowField(int[] flowField) {
        FlowField = flowField;
    }

    /**
     * @return the showgrid
     */
    public Boolean getShowgrid() {
        return showgrid;
    }

    /**
     * @param showgrid the showgrid to set
     */
    public void setShowgrid(Boolean showgrid) {
        this.showgrid = showgrid;
    }

    /**
     * @return the showFlowField
     */
    public Boolean getShowFlowField() {
        return showFlowField;
    }

    /**
     * @param showFlowField the showFlowField to set
     */
    public void setShowFlowField(Boolean showFlowField) {
        this.showFlowField = showFlowField;
    }

    /**
     * @return the showHitbox
     */
    public Boolean getShowHitbox() {
        return showHitbox;
    }

    /**
     * @param showHitbox the showHitbox to set
     */
    public void setShowHitbox(Boolean showHitbox) {
        this.showHitbox = showHitbox;
    }

    /**
     * @return the showPath
     */
    public Boolean getShowPath() {
        return showPath;
    }

    /**
     * @param showPath the showPath to set
     */
    public void setShowPath(Boolean showPath) {
        this.showPath = showPath;
    }

    /**
     * @return the tileSize
     */
    public static int getTileSize() {
        return TILE_SIZE;
    }

    /**
     * @return the tiles
     */
    public int[] getTiles() {
        return tiles;
    }

    /**
     * @param tiles the tiles to set
     */
    public void setTiles(int[] tiles) {
        this.tiles = tiles;
    }

    /**
     * @return the unitMap
     */
    public ArrayList<GUnit>[] getUnitMap() {
        return UnitMap;
    }

    /**
     * @param unitMap the unitMap to set
     */
    public void setUnitMap(ArrayList<GUnit>[] unitMap) {
        UnitMap = unitMap;
    }

    /**
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * @param camera the camera to set
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * @return the uI
     */
    public UI getUI() {
        return UI;
    }

    /**
     * @param uI the uI to set
     */
    public void setUI(UI uI) {
        UI = uI;
    }

    /**
     * @return the mouseBox
     */
    public Rectangle getMouseBox() {
        return mouseBox;
    }

    /**
     * @param mouseBox the mouseBox to set
     */
    public void setMouseBox(Rectangle mouseBox) {
        this.mouseBox = mouseBox;
    }

    /**
     * @return the rx
     */
    public int getRx() {
        return rx;
    }

    /**
     * @param rx the rx to set
     */
    public void setRx(int rx) {
        this.rx = rx;
    }

    /**
     * @return the ry
     */
    public int getRy() {
        return ry;
    }

    /**
     * @param ry the ry to set
     */
    public void setRy(int ry) {
        this.ry = ry;
    }

    /**
     * @return the showUnitMap
     */
    public Boolean getShowUnitMap() {
        return showUnitMap;
    }

    /**
     * @param showUnitMap the showUnitMap to set
     */
    public void setShowUnitMap(Boolean showUnitMap) {
        this.showUnitMap = showUnitMap;
    }

    /**
     * @return the structureMap
     */
    public GStructure[] getStructureMap() {
        return StructureMap;
    }

    /**
     * @param structureMap the structureMap to set
     */


    /**
     * @return the objects
     */
    public ArrayList<GObject> getObjects() {
        return objects;
    }

    /**
     * @param objects the objects to set
     */
    public void setObjects(ArrayList<GObject> objects) {
        this.objects = objects;
    }

    /**
     * @param structureMap the structureMap to set
     */
    public void setStructureMap(GStructure[] structureMap) {
        StructureMap = structureMap;
    }

    /**
     * @return the eUnitMap
     */
    public ArrayList<GUnit>[] getEUnitMap() {
        return EUnitMap;
    }

    /**
     * @param eUnitMap the eUnitMap to set
     */
    public void setEUnitMap(ArrayList<GUnit>[] eUnitMap) {
        EUnitMap = eUnitMap;
    }

    /**
     * @return the eStructureMap
     */
    public GStructure[] getEStructureMap() {
        return EStructureMap;
    }

    /**
     * @param eStructureMap the eStructureMap to set
     */
    public void setEStructureMap(GStructure[] eStructureMap) {
        EStructureMap = eStructureMap;
    }

    /**
     * @return the damageMap
     */
    public ArrayList<Integer> getDamageMap() {
        return damageMap;
    }

    /**
     * @param damageMap the damageMap to set
     */
    public void setDamageMap(ArrayList<Integer> damageMap) {
        this.damageMap = damageMap;
    }

    /**
     * @return the level
     */
    public Image getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Image level) {
        this.level = level;
    }

    /**
     * @return the showUnitHealth
     */
    public Boolean getShowUnitHealth() {
        return showUnitHealth;
    }

    /**
     * @param showUnitHealth the showUnitHealth to set
     */
    public void setShowUnitHealth(Boolean showUnitHealth) {
        this.showUnitHealth = showUnitHealth;
    }

    /**
     * @return the edamageMap
     */
    public ArrayList<Integer> getEdamageMap() {
        return edamageMap;
    }

    /**
     * @param edamageMap the edamageMap to set
     */
    public void setEdamageMap(ArrayList<Integer> edamageMap) {
        this.edamageMap = edamageMap;
    }

    /**
     * @return the playerGold
     */
    public int getPlayerGold() {
        return PlayerGold;
    }

    /**
     * @param playerGold the playerGold to set
     */
    public void setPlayerGold(int playerGold) {
        PlayerGold = playerGold;
    }

    /**
     * @return the enemyGold
     */
    public int getEnemyGold() {
        return EnemyGold;
    }

    /**
     * @param enemyGold the enemyGold to set
     */
    public void setEnemyGold(int enemyGold) {
        EnemyGold = enemyGold;
    }

    /**
     * @return the goldTiles
     */
    public int[] getGoldTiles() {
        return goldTiles;
    }

    /**
     * @param goldTiles the goldTiles to set
     */
    public void setGoldTiles(int[] goldTiles) {
        this.goldTiles = goldTiles;
    }

    /**
     * @return the minerMap
     */
    public ArrayList<Miner>[] getMinerMap() {
        return MinerMap;
    }

    /**
     * @param minerMap the minerMap to set
     */
    public void setMinerMap(ArrayList<Miner>[] minerMap) {
        MinerMap = minerMap;
    }

    /**
     * @return the eMinerMap
     */
    public ArrayList<Miner>[] getEMinerMap() {
        return EMinerMap;
    }

    /**
     * @param eMinerMap the eMinerMap to set
     */
    public void setEMinerMap(ArrayList<Miner>[] eMinerMap) {
        EMinerMap = eMinerMap;
    }

    /**
     * @return the godSelect
     */
    public Boolean getGodSelect() {
        return godSelect;
    }

    /**
     * @param godSelect the godSelect to set
     */
    public void setGodSelect(Boolean godSelect) {
        this.godSelect = godSelect;
    }

    /**
     * @return the aiControl
     */
    public Boolean getAiControl() {
        return aiControl;
    }

    /**
     * @param aiControl the aiControl to set
     */
    public void setAiControl(Boolean aiControl) {
        this.aiControl = aiControl;
    }

    /**
     * @return the difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * @param difficulty the difficulty to set
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
