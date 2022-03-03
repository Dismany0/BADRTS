package com.game.engine.gfx;
//It literally is just an image but with a depth value on the z axis.
//Its only here so i can sort them with a comparator
public class DepthImage {
    public Image image;
    public int zDepth;
    public int offX, offY;

    public DepthImage(Image image, int zDepth, int offX, int offY){
        this.image = image;
        this.zDepth = zDepth;
        this.offX = offX;
        this.offY = offY;
    }
}
