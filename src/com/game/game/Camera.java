package com.game.game;

import java.awt.event.KeyEvent;

import com.game.engine.GameContainer;
import com.game.engine.Renderer;

//The camera, despite playing such a large role, is actually quite simple. It stores 2 coordinates that change as you move the camera around.
//These two coordinates are sent to the renderer, which actually draws the pictures. when moving the camera around, the renderer draws the pictures
//shifted by the movement of the camera. It gives the visual change of a moving camera, but it is actually everything else moving.
public class Camera {
    private float offX, offY;
    private float CX, CY;
    
    private int[] CameraLocations = {-1,-1,-1,-1};
    

    public Camera() {
        CX = 0;
        CY = 0;
    }

    //By moving the mouse to the side of the screen in any direction, you can being scrolling the camera. There are two speeds, one at a tenth of the screen,
    //and the very fast one at a fourtieth of the screen
    public void update(GameContainer gc, GameManager gm, float dt) {

        if(gc.getInput().isKey(KeyEvent.VK_CONTROL)){
            if(gc.getInput().isKeyDown(KeyEvent.VK_F5)){
                CameraLocations[0] = (int)CX + (int)CY * gm.getLevelW() * 16;
            }
            if(gc.getInput().isKeyDown(KeyEvent.VK_F6)){
                CameraLocations[1] = (int)CX + (int)CY * gm.getLevelW() * 16;
            }
            if(gc.getInput().isKeyDown(KeyEvent.VK_F7)){
                CameraLocations[2] = (int)CX + (int)CY * gm.getLevelW() * 16;
            }
            if(gc.getInput().isKeyDown(KeyEvent.VK_F8)){
                CameraLocations[3] = (int)CX + (int)CY * gm.getLevelW() * 16;
            }
        }
        
        if(gc.getInput().isKeyDown(KeyEvent.VK_F5)){
            if(CameraLocations[0] != -1){
                CX = CameraLocations[0] % (gm.getLevelW() * 16);
                CY = CameraLocations[0] / (gm.getLevelW() * 16);
            }
            
        }else
        if(gc.getInput().isKeyDown(KeyEvent.VK_F6)){
            if(CameraLocations[1] != -1){
                CX = CameraLocations[1] % (gm.getLevelW() * 16);
                CY = CameraLocations[1] / (gm.getLevelW() * 16);
            }
        }else
        if(gc.getInput().isKeyDown(KeyEvent.VK_F7)){
            if(CameraLocations[2] != -1){
                CX = CameraLocations[2] % (gm.getLevelW() * 16);
                CY = CameraLocations[2] / (gm.getLevelW() * 16);
            }
        }else
        if(gc.getInput().isKeyDown(KeyEvent.VK_F8)){
            if(CameraLocations[3] != -1){
                CX = CameraLocations[3] % (gm.getLevelW() * 16);
                CY = CameraLocations[3] / (gm.getLevelW() * 16);
            }
        }
        



        if(!gm.getUI().intersects(gc.getInput().getMouseX(), gc.getInput().getMouseY())){

        
            if (gc.getInput().getMouseX() < (gc.getWidth() / 10)) {
                CX -= 4;
            }
            if (gc.getInput().getMouseX() > (gc.getWidth() / 10 * 9)) {
                CX += 4;
            }
            if (gc.getInput().getMouseY() < (gc.getHeight() / 10)) {
                CY -= 4;
            }
            if (gc.getInput().getMouseY() > (gc.getHeight() / 10 * 9)) {
                CY += 4;
            }

            if (gc.getInput().getMouseX() < (gc.getWidth() / 40)) {
                CX -= 6;
            }
            if (gc.getInput().getMouseX() > (gc.getWidth() / 40 * 39)) {
                CX += 6;
            }
            if (gc.getInput().getMouseY() < (gc.getHeight() / 40)) {
                CY -= 6;
            }
            if (gc.getInput().getMouseY() > (gc.getHeight() / 40 * 39)) {
                CY += 6;
            }
        }

        if(CX - gc.getWidth() / 2 < 0){
            CX = gc.getWidth() / 2;
        }
        if(CY - gc.getHeight() / 2 < 0){
            CY = gc.getHeight() / 2;
        }
        if(CX + gc.getWidth() / 2 > gm.getLevelW() * GameManager.TILE_SIZE){
            CX = (gm.getLevelW() * GameManager.TILE_SIZE) - (gc.getWidth() / 2);
        }
        if(CY + gc.getHeight() / 2 > gm.getLevelH() * GameManager.TILE_SIZE){
            CY = (gm.getLevelH() * GameManager.TILE_SIZE) - (gc.getHeight() / 2);
        }

        offX = CX - gc.getWidth() / 2;
        offY = CY - gc.getHeight() / 2;

        
    }

    public void render(GameContainer gc, GameManager gm, Renderer r){
        r.setCX((int)offX);
        r.setCY((int)offY);
        r.setzDepth(65599);
        if(CameraLocations[0] != -1){
            r.drawRect((((CameraLocations[0] % (gm.getLevelW() * 16)) - gc.getWidth() / 2)/ 16 + r.getCX()),((((CameraLocations[0] / (gm.getLevelW() * 16)) - gc.getHeight() / 2)/ 16+ r.getCY())) + (480 - 128), 640 / 16, 480 / 16, 0xffdddddd);
            r.drawText("F5", (((CameraLocations[0] % (gm.getLevelW() * 16)) - gc.getWidth() / 2)/ 16 + r.getCX()) + (640 / 32) -5,((((CameraLocations[0] / (gm.getLevelW() * 16)) - gc.getHeight() / 2)/ 16+ r.getCY())) + (480 - 128) + (480 / 32) - 4, 0xffffffff);
        }
        if(CameraLocations[1] != -1){
            r.drawRect((((CameraLocations[1] % (gm.getLevelW() * 16)) - gc.getWidth() / 2)/ 16 + r.getCX()),((((CameraLocations[1] / (gm.getLevelW() * 16)) - gc.getHeight() / 2)/ 16+ r.getCY())) + (480 - 128), 640 / 16, 480 / 16, 0xffcdcddf);
            r.drawText("F6", (((CameraLocations[1] % (gm.getLevelW() * 16)) - gc.getWidth() / 2)/ 16 + r.getCX()) + (640 / 32) -5,((((CameraLocations[1] / (gm.getLevelW() * 16)) - gc.getHeight() / 2)/ 16+ r.getCY())) + (480 - 128) + (480 / 32) - 4, 0xffffffff);
        }
        if(CameraLocations[2] != -1){
            r.drawRect((((CameraLocations[2] % (gm.getLevelW() * 16)) - gc.getWidth() / 2)/ 16 + r.getCX()),((((CameraLocations[2] / (gm.getLevelW() * 16)) - gc.getHeight() / 2)/ 16+ r.getCY())) + (480 - 128), 640 / 16, 480 / 16, 0xffcacaee);
            r.drawText("F7", (((CameraLocations[2] % (gm.getLevelW() * 16)) - gc.getWidth() / 2)/ 16 + r.getCX()) + (640 / 32) -5,((((CameraLocations[2] / (gm.getLevelW() * 16)) - gc.getHeight() / 2)/ 16+ r.getCY())) + (480 - 128) + (480 / 32) - 4, 0xffffffff);
        }
        if(CameraLocations[3] != -1){
            r.drawRect((((CameraLocations[3] % (gm.getLevelW() * 16)) - gc.getWidth() / 2)/ 16 + r.getCX()),((((CameraLocations[3] / (gm.getLevelW() * 16)) - gc.getHeight() / 2)/ 16+ r.getCY())) + (480 - 128), 640 / 16, 480 / 16, 0xffbdbdff);
            r.drawText("F8", (((CameraLocations[3] % (gm.getLevelW() * 16)) - gc.getWidth() / 2)/ 16 + r.getCX()) + (640 / 32) -5,((((CameraLocations[3] / (gm.getLevelW() * 16)) - gc.getHeight() / 2)/ 16+ r.getCY())) + (480 - 128) + (480 / 32) - 4, 0xffffffff);
        }
        r.setzDepth(0);
            
        
    }
        
    

    /**
     * @return the cX
     */
    public float getCX() {
        return CX;
    }

    /**
     * @param cX the cX to set
     */
    public void setCX(float cX) {
        CX = cX;
    }

    /**
     * @return the cY
     */
    public float getCY() {
        return CY;
    }

    /**
     * @param cY the cY to set
     */
    public void setCY(float cY) {
        CY = cY;
    }
}
