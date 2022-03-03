package com.game.engine.gfx;


public class ImageTile extends Image {
//an image tile is just a big image, but with tile variables to divide up the image into smaller pieces.
//TileW and TileH are the width and height of the tile
    private int tileW, tileH;

    public ImageTile(String path, int tileW, int tileH){
        super(path);
        this.tileW = tileW;
        this.tileH = tileH;

    }

    //You create the image by calling the tiles that should be displayed, instead of the entire thing
    public Image getTileImage(int tileX, int tileY){
        int[] p = new int[tileW * tileH];
        for (int y = 0; y < tileH; y++){
            for (int x = 0; x < tileW; x++){
                p[x + y * tileW] = this.getP()[(x+ tileX * tileW) + (y+tileY * tileH) *this.getW()];
            }
        }

        
        return new Image(p, tileW, tileH);

    }
    public int getTileW() {return tileW;}
    public void setTileW(int tileW) {this.tileW = tileW;}
    public int getTileH() {return tileH;}
    public void setTileH(int tileH) {this.tileH = tileH;}

}
