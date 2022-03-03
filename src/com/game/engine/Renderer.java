package com.game.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.game.engine.gfx.*;

//The renderer draws the image to the window, which then displays it to the screen
public class Renderer {
    //DepthImage stores an image on the Z axis. When you draw two things on top of each other, the bottom picture gets covered up.
    //However, by using a zBuffer, we can store the Z coordinate of the image, and then draw them in order after sorting them by Z values.
    //This means we can choose which image to have on top regardless of the order rendering them
    private ArrayList<DepthImage> DepthImage = new ArrayList<DepthImage>();
    private int pW, pH;
    //Arrays for each individual pixel on the screen
    private int[] p;
    private int[] zBuffer;

    private int Depth = 0;
    //process is called when we need to work on images that have been placed in the z axis. If they all have the same z value, we dont bother calling it
    private boolean process = false;

    //found some online thing for making fonts. It was pretty cool but I didn't have much use for it beyond debug.
    private Font font = Font.STANDARD;

    //CameraX, CameraY
    //This is brought over from the camera. When you shift your camera around, everything needs to be drawn shifted as well
    private int CX, CY;


    public Renderer(GameContainer gc) {
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[p.length];

    }

    //clear simply sets everything to black. Basically wiping the screen.
    public void clear() {
        for (int i = 0; i < p.length; i++) {
            p[i] = 0xff000000;
            zBuffer[i] = 0;
        }
    }

    //Here, i sort the images, stored as objects called depth images that are just an image and z value, by their depth. That way, we render them in order
    //from the back to the front
public void process(){

    process = true;

    Collections.sort(DepthImage, new Comparator<DepthImage>(){

        public int compare(DepthImage o1, DepthImage o2) {
            if(o1.zDepth < o2.zDepth){
                return -1;
            }
            else if (o1.zDepth > o2.zDepth){
                return 1;
            }
            return 0;
        }

    });

    for(int i = 0; i < DepthImage.size(); i++){
        DepthImage ir = DepthImage.get(i);
        setzDepth(ir.zDepth);
        drawImage(ir.image, ir.offX, ir.offY);
    }
    
    DepthImage.clear();
    process = false;
}

//This sets a single pixel on the screen. I use it to draw minimaps and smaller animations. All the big images are drawn by calling this method as well
    public void setPixel(int x, int y, int value){

        //When stored as a hexadecimal value in java, we can obtain the integer values through bitshifting. A shift right by 24 gives you the
        //alpha, or transparency value of the colour.
        //16 gives you the red colour, 8 gives green, and 0 gives blue.
        int alpha = ((value >> 24) & 0xff);

       //If the picture is outside of bounds, we do not draw it. We also do not draw if it is fully transparent because nothing is there
        if((x<0 || x >= pW || y < 0 || y >= pH) || alpha == 0){
            return;
        }

        //When intially learning how to use transparent colours, i learned from someone who really liked using one dimensional arrays to represent 2 dimensional things
        //It kinda stuck and I think i used it throughout the entire program. It allows me to pass a single variable into many functions that would otherwise require
        //2 or 3
        int index = x + y * pW;
        if(zBuffer[index] > Depth){
            return;
        }
        zBuffer[index] = Depth;

        if(alpha == 255){
            p[index] = value;
        } else{
            //You obtain the values of two colours together by multiplying the colour based on its transparency, after adding them together.
            //Basically, you get a mix on the two colours depending on which one was more opague.
            int pixelColour = p[x+y * pW];
            int newRed = ((pixelColour >> 16) & 0xff) - (int)((((pixelColour >> 16) & 0xff) - ((value >> 16) & 0xff)) * alpha / 255f);
            int newGreen = ((pixelColour >> 8) & 0xff) - (int)((((pixelColour >> 8) & 0xff) - ((value >> 8) & 0xff)) * alpha / 255f);
            int newBlue = ((pixelColour) & 0xff) - (int)((((pixelColour) & 0xff) - ((value) & 0xff)) * alpha / 255f);

            //This is the final new colour, and we set that pixel to it
            p[index] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
        }

        
    }

    //I can draw some text that is stored as an image file. I honestly didn't use it too much because i realized just drawing pictures through normal images
    //worked a lot better
    public void drawText(String text, int offX, int offY, int color){
        offX -= CX; offY -= CY;
        text = text.toUpperCase();
        int offset = 0;

        //I read in pixel data from the image in order to find the locations of letters.
        for(int i = 0; i < text.length(); i++){
            int unicode = text.codePointAt(i) - 32;
            
            for(int y = 0; y < font.getFontImage().getH(); y++){
                for(int x = 0; x < font.getWidths()[unicode]; x++){
                    if(font.getFontImage().getP()[(x + font.getOffsets()[unicode])+y*font.getFontImage().getW()] == 0xffffffff){
                        setPixel(x+offX+offset,y +offY,color);
                    }
                }
            }
            offset += font.getWidths()[unicode];
        }


    }



    //Drawing an image. I take an image, and draw it pixel by pixel. offX and offY are how much the image is offset from the top left corner, or 0,0
    public void drawImage(Image image, int offX, int offY){

        if(image.isAlpha() && !process){
            DepthImage.add(new DepthImage(image, Depth, offX, offY));
            return;
        }
        offX -= CX; offY -= CY;
        if(offX < -image.getW()) return;
        if(offY < -image.getH()) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int newX = 0;int newY = 0;
        int newWidth = image.getW();int newHeight = image.getH();


        if(offX< 0){newX -= offX;}
        if(offY< 0){newY -= offY;}
        if(newWidth + offX> pW){newWidth -= newWidth + offX - pW;}
        if(newHeight + offY> pH){newHeight -= newHeight + offY - pH;}

        for(int y = newY; y < newHeight; y++){
            for(int x = newX; x < newWidth; x++){
                setPixel(x + offX, y + offY, image.getP()[x + y * image.getW()]);
            }
        }
    }

    //An image tile is basically a large image, but instead of rendering the entire image at once, i only render a small portion that i specify. 
    //the cool thing about this is that i dont need to manually choose which tile to render. I can loop through instead. This basically makes all my animations for me
    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY){

        if(image.isAlpha() && !process){
            DepthImage.add(new DepthImage(image.getTileImage(tileX, tileY), Depth, offX, offY));
            return;
        }
        offX -= CX; offY -= CY;
        if(offX < -image.getTileW()) return;
        if(offY < -image.getTileH()) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int newX = 0;int newY = 0;
        int newWidth = image.getTileW();int newHeight = image.getTileH();

        if(offX< 0){newX -= offX;}
        if(offY< 0){newY -= offY;}
        if(newWidth + offX> pW){newWidth -= newWidth + offX - pW;}
        if(newHeight + offY> pH){newHeight -= newHeight + offY - pH;}

        for(int y = newY; y < newHeight; y++){
            for(int x = newX; x < newWidth; x++){
                setPixel(x + offX, y + offY, image.getP()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getW()]);
            }
        }
    }

    //Testing rectangles to help me figure out hitboxes
    public void drawRect(int offX, int offY, int width, int height, int colour){ 
        offX -= CX; offY -= CY;
        for(int y = 0; y <= height; y++){
            setPixel(offX, y + offY, colour);
            setPixel(offX + width, y + offY, colour);
        }
        for(int x = 0; x <= width; x++){
            setPixel(x + offX, offY, colour);
            setPixel(x + offX, offY + height, colour);
        }
    }

    //same thing but filled in instead.
    public void fillRect(int offX, int offY, int width, int height, int colour){
        offX -= CX; offY -= CY;
        if(offX < -width) return;
        if(offY < -height) return;
        if(offX >= pW) return;
        if(offY >= pH) return;

        int newX = 0;int newY = 0;
        int newWidth = width;int newHeight = height;

        if(offX< 0){newX -= offX;}
        if(offY< 0){newY -= offY;}
        if(newWidth + offX> pW){newWidth -= newWidth + offX - pW;}
        if(newHeight + offY> pH){newHeight -= newHeight + offY - pH;}

        for(int y = newY; y < newHeight; y++){
            for(int x = newX; x < newWidth; x++){
                setPixel(x + offX, y+ offY, colour);
            }
        }
        
    }

    public int getzDepth() {
        return Depth;
    }

    public void setzDepth(int zDepth) {
        this.Depth = zDepth;
    }

    public int getCX() {
        return CX;
    }

    public void setCX(int offX) {
        CX = offX;
    }

    public int getCY() {
        return CY;
    }

    public void setCY(int cY) {
        CY = cY;
    }
}
