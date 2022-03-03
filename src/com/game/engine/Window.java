package com.game.engine;

import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.BorderLayout;

import com.game.game.*;
public class Window {
    private JFrame frame;
    private BufferedImage image;
    private Canvas canvas;
    private BufferStrategy bs;
    private Graphics g;

    MainMenu mainMenu;

//the normal java window. I put it in a separate file to make everything more neat
    public Window(GameContainer gc){
        image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
        canvas = new Canvas();
        Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()),(int)( gc.getHeight() * gc.getScale()));

        canvas.setPreferredSize(s);
        canvas.setMaximumSize(s);
        canvas.setMinimumSize(s);

        // mainMenu = new MainMenu();

        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(canvas, BorderLayout.CENTER);
        // frame.add(mainMenu, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        
        
        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        g = bs.getDrawGraphics();

        
    }

    public void update(){
        g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(),null);
        bs.show();
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

  
  
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * @return the frame
     */
    public JFrame getFrame() {
        return frame;
    }

    

 
}
