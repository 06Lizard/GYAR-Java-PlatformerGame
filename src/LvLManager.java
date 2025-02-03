import java.util.List;
import java.util.ArrayList;
import java.awt.Toolkit;

class LvLManager
{	
// public
    // handle to only allow necessary data from the LvLManager
    public class LvLManagerHandle {
    // public
        // refrences to the actual mapp
        public List<List<Character>> mapp = new ArrayList<>(); // 2D vector of Block identifiers

        // constructor
        public LvLManagerHandle(List<List<Character>> mapp) {
            this.mapp = mapp;
        }

        // spawn a projectile
        public void createProjectile(short x, short y, boolean facingRight) {
            projectiles.add(new Projectile(x, y, facingRight, getHandle()));
        }
        public void removeEnemy(Enemy enemy){ enemies.remove(enemy); }
        public void removeProjectile(Projectile projectile){ projectiles.remove(projectile); }
    }

// private
	private LvLManagerHandle handle;

	private int TMP = 0;
	private BoolWrapper runningRef;
	private Position cameraPos;
	private Player player;
	private short lvl = 0;
    private short score = 0;

// public
	public static final short screenWidth = 32;
	public static final short screenHight = 16;
	public static final short width = 64;
	public static final short hight = 32;		

	// no need for forward decleration
    public List<Enemy> enemies = new ArrayList<>();
    public List<Projectile> projectiles = new ArrayList<>();    
    public List<List<Character>> mapp = new ArrayList<>(); // 2D vector of Block identifiers

	public LvLManager(BoolWrapper running, Position cameraPos, Player player){
        this.runningRef = running;
        this.cameraPos = cameraPos;
        this.player = player;        
        handle = new LvLManagerHandle(mapp);
    }	

	public void Initzialize(){
        score = 0;
        lvl = 0;
        LoadLvL();
    }
	public void ResetLvL(){
        LoadLvL();
    }
	public void LvLFinished(){
        lvl++;
        ResetLvL();
    }
	public void GameOver(){
        runningRef.state = false;
        System.out.print("\033[2J\033[0m\033[3;5H Game Over\033[4;5H Score: " + score + "\033[5;5H Level: " + lvl); 
        // Make a beep sound (default system beep)
        Toolkit.getDefaultToolkit().beep(); 
        // Sleep for 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }   
        // Get user input (waiting for key press to continue)
        User32.waitForAnyKey();
    }
    public void Update(){
        // only update enemies on the screen
        for (Enemy enemy : enemies) {
            short x = enemy.position.x;
            short y = enemy.position.y;
        
            // Check if the enemy is within the camera's view bounds with some offset
            if (cameraPos.x <= x + 2 && x - 2 < cameraPos.x + screenWidth &&
                cameraPos.y <= y + 2 && y - 2 < cameraPos.y + screenHight) {
                enemy.Update();  // Polymorphic call to the enemy's update method
            }
        }

        // Always update projectiles, they have their own lifetime logic
        for (Projectile projectile : projectiles) {
            projectile.Update();  // Update projectiles
        }
    }

	public void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }
    public void addProjectile(short x, short y, boolean isRight){
        projectiles.add(new Projectile(x, y, isRight, getHandle()));
    }
    public void setPlayer(Player player){
        this.player = player;
    }

	LvLManagerHandle getHandle() {
		return handle;
	}

// private
	private void LoadLvL(){
        // Clear up before writing to avoid memory leaks
        mapp.clear();
        projectiles.clear();
        enemies.clear();

        switch (lvl) {
            case 0: LvL0(); break;
            case 1: LvL1(); break;
            case 2: LvL2(); break;
            default: GameWon(); break;
        }
    }
	private void GameWon(){
        runningRef.state = false;
        System.out.print("\033[2J\033[0m\033[3;5H Game Won!\033[4;5H Score: " + score + "\033[5;5H Level: " + lvl); 
        // Make a beep sound (default system beep)
        Toolkit.getDefaultToolkit().beep(); 
        // Sleep for 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }   
        // Get user input (waiting for key press to continue)
        User32.waitForAnyKey();
    }

	public void LvL0() {
        // Set size of mapp (a List of Lists of Blocks)
        for (int i = 0; i < width; i++) {
            mapp.add(new ArrayList<>());
            for (int j = 0; j < hight; j++) {
                mapp.get(i).add(' ');  // Initialize with null, indicating empty space
            }
        }

        // Set the map (adding blocks)

        // Floor
        for (int x = 0; x < width; x++) {
            mapp.get(x).set(16, BlockManager.ground);  // Setting ground block at y=16
        }

        // Platform
        for (int x = 8; x < 12; x++) {
            mapp.get(x).set(13, BlockManager.ground);  // Setting ground block at y=13
        }

        // Lone block
        mapp.get(3).set(15, BlockManager.ground);  // Setting ground block at x=3, y=15

        // Flag pole (from y=11 to y=15)
        for (short y = 11; y < 16; y++) {
            mapp.get(width - 1).set(y, BlockManager.flagPole);  // Setting flag pole block at x=width-1
        }
        mapp.get(width - 1).set(10, BlockManager.flag);  // Setting flag block at x=width-1, y=10

        // Set the enemies (example for adding an enemy)
        addEnemy(new Enemy1((short) 5, (short) 5, false, getHandle()));

        // Set player position
        player.position = new Position((short) 10, (short) 10);
    }
	private void LvL1(){ LvLFinished(); }
	private void LvL2(){ LvLFinished(); }     
};