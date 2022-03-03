package com.game.engine.gfx;

public class Font {
    //a font has 59 characters. I didn't think i would use the fonts, so i never bothered with lower case. I only ended up using it for debug
    public static final Font STANDARD = new Font("/fonts/standard.png");
    
    private Image fontImage;
    private int[] offsets;
    private int[] widths;

    public Font(String path){
        fontImage = new Image(path);
        offsets = new int[59];
        widths = new int[59];

        int unicode = 0;

        for(int i = 0; i < fontImage.getW(); i++){
            //pure blue colour
            if(fontImage.getP()[i] == 0xff0000ff){
                offsets[unicode] = i;
            }
            //no blue, it is bright yellow
            if(fontImage.getP()[i] == 0xffffff00){
                widths[unicode] = i - offsets[unicode];
                ++unicode;
            }
        }

    }

    public Image getFontImage() {
        return fontImage;
    }

    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public int[] getWidths() {
        return widths;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }
}
