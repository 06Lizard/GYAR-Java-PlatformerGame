import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class PlatformerGame {
// private
    private static final short CAMERA_LEFT_BOUND = (short) 10;
    private static final short CAMERA_RIGHT_BOUND = LvLManager.screenWidth - (short) 10;

	private boolean running = true;

	private Player player;
	private Position cameraPos = new Position();	
	private LvLManager _LvLManager;

// public
    public PlatformerGame(){        
        _LvLManager = new LvLManager(running, cameraPos, null);
        player = new Player((short) 10, (short) 10, _LvLManager);
        _LvLManager.setPlayer(player);
    }

	public void Run(){
        System.out.print("\033[0m" + // reset formating
            "\033[?25l"); // hide cursor

        Menue();        

        // thanks for playing
        System.out.println("\033[2J\033[0m\033[3;5H Thanks for playing");
    }

// private
    public void Menue() {
        class MenuKeyListener implements KeyListener {
            public boolean upPressed = false;
            public boolean downPressed = false;
            public boolean enterPressed = false;
        
            @Override
            public void keyTyped(KeyEvent e) {}
        
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                    upPressed = true;
                }
                if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                    downPressed = true;
                }
                if (keyCode == KeyEvent.VK_ENTER) {
                    enterPressed = true;
                }
            }
        
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                    upPressed = false;
                }
                if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                    downPressed = false;
                }
                if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                    enterPressed = false;
                }
            }
        }
        MenuKeyListener keyListener = new MenuKeyListener();
        boolean menueLoop = true;

        // register the key listener
        java.awt.Frame dummyFrame = new java.awt.Frame(); // Create a dummy frame to attach the KeyListener
        dummyFrame.addKeyListener(keyListener);
        dummyFrame.setVisible(true);
        dummyFrame.setFocusable(true);

        while (menueLoop) {
            boolean optionLoop = true;
            boolean menuOption = true;

            System.out.print("\033[2J\033[3;6HPlatformerGame\033[4;6H StartGame \033[5;6H Quit ");

            // press any key to start the game
            KeyPressUtils.waitForAnyKey();

            while (optionLoop) {
                try {
                    Thread.sleep(100);  // Wait 100 milliseconds before redrawing the menu
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.print("\033[2J\033[3;6HPlatformerGame\033[4;6H"
                    + (menuOption ? "<StartGame>" : " StartGame ")
                    + "\033[5;6H "
                    + (!menuOption ? "<Quit>" : " Quit "));

                // toggle menu option on 'W', 'S', 'UP', or 'DOWN'
                if (keyListener.upPressed || keyListener.downPressed) {
                    menuOption = !menuOption; // toggle menu option
                    keyListener.upPressed = false; // reset key state
                    keyListener.downPressed = false; // reset key state
                }
                // start the game or quit on 'Enter' / 'Space'
                if (keyListener.enterPressed) {
                    optionLoop = false; // exit the option loop
                    keyListener.enterPressed = false; // reset key state
                }
            }

            if (menuOption) {
                GameLoop(); // start the game if StartGame is selected
            } else {
                menueLoop = false;  // exit the menu if Quit is selected
            }
        }

        dummyFrame.dispose(); // dispose of the dummy frame
    }
	
    private void Initzialize(){
        running = true;

        // reset player
        player.Reset();

        // set camra pos
        cameraPos = player.position;
        UpdateCamera();

        // set the mapp
        _LvLManager.Initzialize();
    }
	
    private void GameLoop()
    {
        Initzialize();
        DeltaTimeCounter deltaFrameCounter = new DeltaTimeCounter();

        while (running) {
            //Render(); /*depricated*/
            optimizedRender();
            Update();
            deltaFrameCounter.count();
            deltaFrameCounter.display(1, 1, "Frame");

            // try catch required in java
            try {
                Thread.sleep(25);  // eep for 25 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();  // handle interruption if necessary
            }
        }
    }
	
    private void Update(){
        _LvLManager.Update();

        player.Update();

        UpdateCamera();
    }
    
    public void Render() {
        short worldX;
        short worldY;
        // Loop through the screen height and width
        for (short y = 0; y < LvLManager.screenHight; y++) {
            for (short x = 0; x < LvLManager.screenWidth; x++) {
                // Calculate world position based on camera offset
                worldX = (short) (x + cameraPos.x);
                worldY = (short) (y + cameraPos.y);
            
                // If the player is not at the current world position
                if (player.position.x != worldX || player.position.y != worldY) {
                    // Get the block texture and color at the current position
                    char blockMask = _LvLManager.mapp.get(worldX).get(worldY);
                    System.out.print("\033[" + (y + 1) + ";" + (x + 1) + "H\033[" 
                            + BlockManager.getColour(blockMask) + "m" 
                            + BlockManager.getTexture(blockMask));
                } else {
                    // If the player is at the current position, render the player
                    System.out.print("\033[" + (y + 1) + ";" + (x + 1) + "H\033[" 
                            + player.getColour() + "m" + player.getTexture());
                }
            
                // Temporary area for rendering enemies
                for (Enemy enemy : _LvLManager.enemies) {
                    if (enemy.position.x == worldX && enemy.position.y == worldY) {
                        System.out.print("\033[" + (y + 1) + ";" + (x + 1) + "H\033[" 
                                + enemy.getColour() + "m" + enemy.getTexture());
                    }
                }
            }
        }
    
        // Debugging output (for visualizing camera position and player position)
        // Uncomment to view the camera position and player position
        // System.out.println("\033[25;20H Camera: " + cameraPos.x + "," + cameraPos.y + " Player: " + player.x + "," + player.y);
    }
    
    public void optimizedRender() {
	    // 1: populate/prepare the screenBuffer
        class DisplayElement {
            char texture;
            short color;
    
            DisplayElement(char texture, short color) {
                this.texture = texture;
                this.color = color;
            }
        }
        DisplayElement[][] screenBuffer = new DisplayElement[LvLManager.screenWidth][LvLManager.screenHight];

        for (int y = 0; y < LvLManager.screenHight; y++) {
            for (int x = 0; x < LvLManager.screenWidth; x++) {
                int worldX = x + cameraPos.x;
                int worldY = y + cameraPos.y;

                DisplayElement element;

			    // if not player's pos amend mapp
                if (player.position.x != worldX || player.position.y != worldY) {
                    char blockMask = _LvLManager.mapp.get(worldX).get(worldY);
                    if (blockMask != 0) {
                        char texture = BlockManager.getTexture(blockMask);
                        short color = BlockManager.getColour(blockMask);
                        element = new DisplayElement(texture, color);
                    } else {
                        element = new DisplayElement(' ', (short) 0);
                    }
                } else { // player
                    element = new DisplayElement(player.getTexture(), player.getColour());
                }

                // entity handling
                for (Enemy enemy : _LvLManager.enemies) {
                    if (enemy.position.x == worldX && enemy.position.y == worldY) {
                        element = new DisplayElement(enemy.getTexture(), enemy.getColour());
                        break;
                    }
                }

                // update screenBuffer
                screenBuffer[x][y] = element;
            }
        }

        // 2: construct the screenOutput string from screenBuffer
        StringBuilder screenOutput = new StringBuilder();
        screenOutput.append("\033[2;2H"); // sets initial cursor position

        for (int y = 0; y < LvLManager.screenHight; y++) {
            for (int x = 0; x < LvLManager.screenWidth; x++) {
                DisplayElement element = screenBuffer[x][y];
                screenOutput.append("\033[").append(element.color).append("m");
                screenOutput.append(element.texture);
            }
            screenOutput.append('\n'); // end of row
        }

        // 3: render the screen
        System.out.print(screenOutput.toString());
        System.out.flush();
    }

    private void UpdateCamera(){
        if (player.position.x < cameraPos.x + CAMERA_LEFT_BOUND) {
            cameraPos.x = (short) (player.position.x - CAMERA_LEFT_BOUND);
        } else if (player.position.x > cameraPos.x + CAMERA_RIGHT_BOUND) {
            cameraPos.x = (short) (player.position.x - CAMERA_RIGHT_BOUND);
        }

        // TMP
        cameraPos.y = 1;

        cameraPos.x = (short) Math.max(0, Math.min(cameraPos.x, _LvLManager.width - LvLManager.screenWidth));
    }
}