import java.util.Scanner;

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
        _LvLManager = new LvLManager(running, cameraPos, player);
        player = new Player((short) 10, (short) 10, _LvLManager);
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
        boolean menueLoop = true;
        while (menueLoop) {
            boolean optionLoop = true;
            boolean menuOption = true;
            System.out.print("\033[2J\033[3;6HPlatformerGame\033[4;6H StartGame \033[5;6H Quit ");
            
            Scanner scanner = new Scanner(System.in);

            while (optionLoop) {
                try {
                    Thread.sleep(100);  // Wait 100 milliseconds before redrawing the menu
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (menuOption) {
                    System.out.println("\033[2J\033[3;6HPlatformerGame\033[4;6H<StartGame>\033[5;6H Quit ");
                } else {
                    System.out.println("\033[2J\033[3;6HPlatformerGame\033[4;6H StartGame \033[5;6H<Quit>");
                }

                // Read user input to change menu option
                if (scanner.hasNext()) {
                    String input = scanner.nextLine().toLowerCase();

                    // Toggle menu option on 'W', 'S', 'UP', or 'DOWN'
                    if (input.equals("w") || input.equals("s") || input.equals("up") || input.equals("down")) {
                        menuOption = !menuOption;
                    }

                    // Start the game or quit on 'Enter' or 'Space'
                    if (input.equals("enter") || input.equals("space")) {
                        optionLoop = false;
                    }
                }
            }

            if (menuOption) {
                GameLoop(); // Start the game if StartGame is selected
            } else {
                scanner.close();
                menueLoop = false;  // Exit the menu if Quit is selected
            }
        }
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
	private void GameLoop(){
        Initzialize();

        while (running) {
            Render();
            Update();

            // try catch required in java
            try {
                Thread.sleep(25);  // Sleep for 25 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();  // Handle interruption if necessary
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