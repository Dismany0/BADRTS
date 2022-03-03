package com.game.engine;

public class GameContainer implements Runnable {
    //Implements a thread so that it can run. This runs the main bit that runs the game. Although it is not the main game loop, update and render is being called
    //from here. Whenever this updates, the entire game, along with everything, updates
    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;
    private AbstractGame game;

    private boolean running = false;
    //cap frames at 60fps
    private double framecap = 1.0 / 60.0;

    //I really wish I could make the scale bigger, but I encountered a problem where the accuracy of mouseListeners really go down once the screen gets larger
    //Not really how it would work if your screen resolution is smaller than the scaled up size
    public int width = 640, height = 480;
    public float scale = 1.5f;
    private String title = "BAAADRTS";

    public GameContainer(AbstractGame game) {
        this.game = game;
    }

    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);


        thread = new Thread(this);
        thread.start();
    }

    public void stop() {

    }

    public void run() {
        running = true;
        //Here i am finding the time between each update cycle. I don't render or update anything unless that time has exceeded a 60th of a second
        //I store this time as a float, delta T for change in time, and pass it along with the update method. I use this to work out how long the game has been running
        // animations, and more
        boolean render = false;
        double time1 = 0;
        double time2 = System.nanoTime() / 1e9; 
        double passedTime = 0;
        //unprocessed time
        double processedTime = 0;

        //Keep track of frames per second
        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        //Keep track of updates per second
        double updateTime = 0;
        int updates = 0;
        int ups = 0;

        
        while (running) {
            //we always set render to false in the beginning, unless it is on that 60fps cycle where we should render
            render = false;

            time1 = System.nanoTime() / 1e9;
            passedTime = time1 - time2;
            time2 = time1;

            //our passed time is added to the frames to the next frame or update
            processedTime += passedTime;

            frameTime += passedTime;
            updateTime += passedTime;

            //if we have processed enough time and it is time for us to update the game, we set render to true and update everything
            while (processedTime >= framecap) {
                processedTime -= framecap;
                render = true;

                //updating along with our frame time as delta t
                game.update(this, (float)framecap);

                //I count the number of updates. When update time hits 1, or 1 second, I print out how many frames I have gotten. This works out to frames per second, 
                //or updates per second
                updates++;
                if(updateTime >= 1.0){
                    updateTime = 0;
                    ups = updates;
                    updates = 0;
                    System.out.println("UPS is " + ups);
                }
                input.update();


                if(frameTime >= 1.0){
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS is " + fps);
                    
                }
                
            }
            //Literally the exact same thing but with rendering instead of updating. I call the renderer here because all the drawing stuff in in there
            if (render) {
                renderer.clear();
                
                game.render(this,renderer);
                renderer.process();
                window.update();
                frames++;

            } else {
                try {
                    //Letting the frame sleep for 1 milisecond really helps with the performance of the program. However, I cannot rely on 
                    //thread.sleep to keep track of time because it is often inaccurate by give or take a few miliseconds.
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

    //I began by making the getters and setters nice and neat, but then eventually I realized there are so many of them and theyre just scattered through the rest
    //of the program
	public int getWidth() {return width;}
	public void setWidth(int width) {this.width = width;}
	public int getHeight() {return height;}
	public void setHeight(int height) {this.height = height;}
	public float getScale() {return scale;}
	public void setScale(float scale) {this.scale = scale;}
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
    public Window getWindow() {return window;}
    public Input getInput() {return input;}

    /**
     * @return the thread
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * @param thread the thread to set
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * @param window the window to set
     */
    public void setWindow(Window window) {
        this.window = window;
    }

    /**
     * @return the renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to set
     */
    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * @param input the input to set
     */
    public void setInput(Input input) {
        this.input = input;
    }

    /**
     * @return the game
     */
    public AbstractGame getGame() {
        return game;
    }

    /**
     * @param game the game to set
     */
    public void setGame(AbstractGame game) {
        this.game = game;
    }

  
 
}
