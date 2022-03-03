package com.game.engine;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private GameContainer gc;

    private int NUM_KEYS = 256; // Apparently keyboards generally go up to 256 keys. However, one key I specifically had to catch was the windows key
    //It had a value of 800 something ,and would result in errors whenever it was pressed
    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];

    private int NUM_BUTTONS = 5; // 3 mouse buttons. I originally had support for 2 more mouse buttons since those seem common, but I don't actually have one
    //so I didn't bother using the last two
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    //These keep track of how much the mouse is scrolling, and the location of the cursor
    private int mouseX, mouseY;
    private int scroll;


    public Input(GameContainer gc) {
        this.gc = gc;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        gc.getWindow().getCanvas().addKeyListener(this);
        gc.getWindow().getCanvas().addMouseListener(this);
        gc.getWindow().getCanvas().addMouseMotionListener(this);
        gc.getWindow().getCanvas().addMouseWheelListener(this);
    }

    //In update, i reset the scroll, and set keyslast. When checking if a single button press was made, I just check to see if keys last and keys are different
    public void update(){

        scroll = 0;
        for(int i = 0; i < NUM_KEYS; i++){
            keysLast[i] = keys[i];
        }
        for(int i = 0; i < NUM_BUTTONS; i++){
            buttonsLast[i] = buttons[i];
        }
    }


    //These just make it a little easier to work with inputs. Having a whole array, and simply referring to it simplifies the amount of code required
    //to listen to keys
    public boolean isKey(int keyCode){
        return keys[keyCode];
    }
    public boolean isKeyUp(int keyCode){
        return !keys[keyCode] && keysLast[keyCode];
    }
    public boolean isKeyDown(int keyCode){
        return keys[keyCode] && !keysLast[keyCode];
    }

    //This is the same thing but for buttons on the mouse
    public boolean isButton(int button){
        return buttons[button];
    }
    public boolean isButtonUp(int button){
        return !buttons[button] && buttonsLast[button];
    }
    public boolean isButtonDown(int button){
        return buttons[button] && !buttonsLast[button];
    }


    //Mouse wheel, I dont think i actually used it
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        
        scroll = e.getWheelRotation();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int) (e.getX() / gc.getScale());
        mouseY = (int) (e.getY() / gc.getScale());

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // mouseX = (int) (e.getX() / gc.getScale() + gc.getWindow().getFrame().;
        mouseX = (int) (e.getX() / gc.getScale());
        mouseY = (int) (e.getY() / gc.getScale());
    }

    //There are a bunch of required methods here that java forces you to have. I did not use them though
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;

    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    //I specifically catch the windows key because it seems to result in errors. No idea why the value of windows is so large
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_WINDOWS){
            return;
        }
       keys[e.getKeyCode()] = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_WINDOWS){
            return;
        }
        keys[e.getKeyCode()] = false;

    }

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

    public int getRMouseX() {
		return mouseX + gc.getRenderer().getCX();
	}

	public int getRMouseY() {
		return mouseY + gc.getRenderer().getCY();
	}

	public int getScroll() {
		return scroll;
	}


}
