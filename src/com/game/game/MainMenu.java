package com.game.game;


//THIS IS THE MAIN FILE. RUN THIS, and it basically just runs everything else. 
//theres not a lot to this file, simply a jframe with a few buttons on it to make a few options before starting the game
//It closes when the actual game starts. If you close the actual game and wish to play again, you must
//run the program again


import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.NoSuchElementException;

import javax.swing.*;

import com.game.engine.GameContainer;

public class MainMenu{
    public static void main(String[] args) throws NoSuchElementException {
        Menu frame = new Menu();
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }
}

class Menu extends JFrame implements Runnable{
    private static final long serialVersionUID = 1L;
    JLabel name;
    JFrame frame;
    JButton level0, level1;

    JButton easy, medium, hard, insane;

    JButton playercontrol, aicontrol;

    JButton back1, back2;

    JLabel display;

    int selectedLevel = 0; int difficulty = 0; int ai = 0;
    public Menu(){
        frame = this;
        setTitle("Main Menu");
        setLayout(null);
        setPreferredSize(new Dimension (960, 720));
        setSize(new Dimension(960, 720));
        setFocusable(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        name = new JLabel("test");
        ImageIcon icon = new ImageIcon("res/Name.png");
        name.setIcon(icon);
        name.setPreferredSize(new Dimension (640, 400));
        name.setBounds(140, 0, 640, 400);
        name.setVisible(true);
        frame.add(name);


        display = new JLabel("Choose a map!");
        display.setPreferredSize(new Dimension(500, 100));
        display.setBounds(420,350,300,150);
        frame.add(display);


        playercontrol = new JButton("I will play");
        playercontrol.setPreferredSize(new Dimension(300, 100));
        playercontrol.setBounds(80,500,300,150);
        playercontrol.setVisible(false);
        playercontrol.addActionListener(evt ->{
            ai = 0;
            startGame(selectedLevel, difficulty, ai);
        });
        frame.add(playercontrol);

        aicontrol = new JButton("The AI will play");
        aicontrol.setPreferredSize(new Dimension(300, 100));
        aicontrol.setBounds(960-80-300,500,300,150);
        
        aicontrol.setVisible(false);
        aicontrol.addActionListener(evt ->{
            ai = 1;
            startGame(selectedLevel, difficulty, ai);
        });
        frame.add(aicontrol);

        back2 = new JButton("Go Back");
        back2.setPreferredSize(new Dimension(100, 50));
        back2.setBounds(20, 20, 100, 50);
        back2.setVisible(false);
        back2.addActionListener(evt -> {
            back2.setVisible(false);
            easy.setVisible(true);
            medium.setVisible(true);
            hard.setVisible(true);
            insane.setVisible(true);
            back1.setVisible(true);
            display.setText("Please choose a difficulty");
            playercontrol.setVisible(false);
            aicontrol.setVisible(false);
        });
        frame.add(back2);


        easy = new JButton("Easy Difficulty");
        easy.setPreferredSize(new Dimension(300, 50));
        easy.setBounds(80,500,300,50);
        easy.setVisible(false);
        easy.addActionListener(evt -> {
            difficulty = 0;
            display.setText("Would you like an AI to play for you? (You will not be playing)");
            easy.setVisible(false);
            medium.setVisible(false);
            hard.setVisible(false);
            insane.setVisible(false);
            back1.setVisible(false);
            back2.setVisible(true);
            playercontrol.setVisible(true);
            aicontrol.setVisible(true);
            System.out.println(difficulty);

        });
        frame.add(easy);

        medium = new JButton("Medium Difficulty");
        medium.setPreferredSize(new Dimension(300, 50));
        medium.setBounds(80,575,300,50);
        medium.setVisible(false);
        medium.addActionListener(evt -> {
            difficulty = 1;
            easy.setVisible(false);
            medium.setVisible(false);
            hard.setVisible(false);
            insane.setVisible(false);
            back1.setVisible(false);
            back2.setVisible(true);
            playercontrol.setVisible(true);
            aicontrol.setVisible(true);
            display.setText("Would you like an AI to play for you? (You will not be playing)");
            System.out.println(difficulty);
        });
        frame.add(medium);

        hard = new JButton("Hard Difficulty");
        hard.setPreferredSize(new Dimension(300, 50));
        hard.setBounds(960-80-300,500,300,50);
        hard.setVisible(false);
        hard.addActionListener(evt -> {
            difficulty = 2;
            easy.setVisible(false);
            medium.setVisible(false);
            hard.setVisible(false);
            insane.setVisible(false);
            back1.setVisible(false);
            playercontrol.setVisible(true);
            aicontrol.setVisible(true);
            display.setText("Would you like an AI to play for you? (You will not be playing)");
            back2.setVisible(true);
            System.out.println(difficulty);
        });
        frame.add(hard);

        insane = new JButton("Insane Difficulty");
        insane.setPreferredSize(new Dimension(350, 50));
        insane.setBounds(960-80-300,575,300,50);
        insane.setVisible(false);
        insane.addActionListener(evt -> {
            difficulty = 3;
            easy.setVisible(false);
            medium.setVisible(false);
            hard.setVisible(false);
            insane.setVisible(false);
            back1.setVisible(false);
            back2.setVisible(true);
            playercontrol.setVisible(true);
            aicontrol.setVisible(true);
            display.setText("Would you like an AI to play for you? (You will not be playing)");
            System.out.println(difficulty);
        });
        frame.add(insane);

        back1 = new JButton("Go Back");
        back1.setPreferredSize(new Dimension(100, 50));
        back1.setBounds(20, 20, 100, 50);
        back1.setVisible(false);
        back1.addActionListener(evt -> {
            back1.setVisible(false);
            level0.setVisible(true);
            level1.setVisible(true);
            display.setText("Choose a map!");
            easy.setVisible(false);
            medium.setVisible(false);
            hard.setVisible(false);
            insane.setVisible(false);
        });
        frame.add(back1);

        level0 = new JButton("Forest");
        level0.setPreferredSize(new Dimension(300, 100));
        level0.setBounds(80,500,300,150);
        level0.addActionListener(evt -> {
            level0.setVisible((false));
            level1.setVisible((false));
            selectedLevel = 0;
            display.setText("Please choose a difficulty");
            back1.setVisible(true);
            easy.setVisible(true);
            medium.setVisible(true);
            hard.setVisible(true);
            insane.setVisible(true);
            System.out.println(selectedLevel);
        });
        frame.add(level0);


        level1 = new JButton("Volcano");
        level1.setPreferredSize(new Dimension(300, 100));
        level1.setBounds(960-80-300,500,300,150);
        level1.addActionListener(evt -> {
            level0.setVisible((false));
            level1.setVisible((false));
            selectedLevel = 1;
            display.setText("Please choose a difficulty");
            back1.setVisible(true);
            easy.setVisible(true);
            medium.setVisible(true);
            hard.setVisible(true);
            insane.setVisible(true);
            System.out.println(selectedLevel);
        });
        frame.add(level1);


        
        
    }
    public void startGame(int a, int b, int c){
        
        GameContainer gc = new GameContainer(new GameManager(a, b, c));
        gc.start();
        frame.dispose();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }
}
