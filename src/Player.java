import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Player extends Entity {
// private
    private static final short _health = 3;
    private static final char _texture = '#';
    private static final short _colour = Text.Formatting.RED.code;
    private static final boolean _collision = false;
    private LvLManager _LvLManager;

    // track key states, required in java
    private boolean upPressed = false;
    private boolean leftPressed = false;
    private boolean downPressed = false;
    private boolean rightPressed = false;

// public
    public Player(short x, short y, LvLManager _LvLManager)
    {
        super(_health, _texture, _colour, _collision, x, y);
        this._LvLManager = _LvLManager;
    }

    @Override
    public void Update(){
        Input();
        Move();
        Collision();
    }

    public void TakeDamage() { 
        health--; 
        if (health < 0)
            _LvLManager.GameOver();
        else _LvLManager.ResetLvL();
    }
    public void Reset() { health = _health; }

// private
    @Override
    protected void Move() {
        // Ground check
        if (BlockManager.getCollision(_LvLManager.mapp.get(position.x).get(position.y + 1))) {
            states |= IS_GROUNDED;
        } else {
            states &= ~IS_GROUNDED;
        }

        // 1. Stop jumping if UP is not pressed and the player is jumping
        if ((states & UP) == 0 && (states & IS_JUMP) != 0) {
            states &= ~IS_JUMP; // Clears the IS_JUMP flag
            states &= ~JUMP_TIME; // Clears bits representing jumpTime (bits 6-7)
        }

        // 2. If not grounded and not jumping, fall down
        else if ((states & IS_GROUNDED) == 0 && (states & IS_JUMP) == 0) {
            if (BlockManager.getCollision(_LvLManager.mapp.get(position.x).get(position.y + 1))) {
                states |= IS_GROUNDED; // Sets the IS_GROUNDED flag
            } else {
                position.y++; // Fall down
                for (Enemy enemy : _LvLManager.enemies) {
                    if (position.x == enemy.position.x && position.y == enemy.position.y) {
                        enemy.TakeDamage();
                    }
                }
            }
        }

        // 3. Begin jumping if UP is pressed, the player is on the ground, and the space above is clear
        else if ((states & UP) != 0 && (states & IS_GROUNDED) != 0) {
            if (!BlockManager.getCollision(_LvLManager.mapp.get(position.x).get(position.y - 1))) {
                states |= IS_JUMP; // Sets the IS_JUMP flag
                states &= ~IS_GROUNDED; // Clears the IS_GROUNDED flag
                position.y--; // Move up
                states = (byte) ((states & ~JUMP_TIME) | (1 << 6)); // Set jumpTime to 1
            } else {
                states &= ~IS_JUMP; // Blocked, stop jumping
            }
        }

        // 4. Continue jumping if UP is pressed and jumpTime < 3
        else if ((states & UP) != 0 && (states & IS_JUMP) != 0) {
            if (((states >> 6) & 0b11) < 3 && !BlockManager.getCollision(_LvLManager.mapp.get(position.x).get(position.y - 1))) {
                position.y--; // Move up
                states = (byte) ((states & ~JUMP_TIME) | ((((states >> 6) & 0b11) + 1) << 6)); // Increment jumpTime
            } else {
                states &= ~IS_JUMP; // Stop jumping if max height is reached or blocked
            }
        }

        // Horizontal movement (Left and Right)
        if ((states & LEFT) != 0 && (states & RIGHT) == 0) {
            if (position.x > 0 && !BlockManager.getCollision(_LvLManager.mapp.get(position.x - 1).get(position.y))) {
                position.x--; // Move left
            }
        } else if ((states & RIGHT) != 0 && (states & LEFT) == 0) {
            if (position.x < _LvLManager.width - 1 && !BlockManager.getCollision(_LvLManager.mapp.get(position.x + 1).get(position.y))) {
                position.x++; // Move right
            }
        }
    }

    private void Input() {
        states &= ~0b00001111; // resets the movment state

        if (upPressed) {
            states |= UP;
        }
        if (leftPressed) {
            states |= LEFT;
        }
        if (downPressed) {
            states |= DOWN;
        }
        if (rightPressed) {
            states |= RIGHT;
        }
    }

    private void Collision() {
        char block = _LvLManager.mapp.get(position.x).get(position.y);
        if (block == BlockManager.flag || block == BlockManager.flagPole){
            _LvLManager.LvLFinished();
        }

        for (Enemy enemy : _LvLManager.enemies) {
            if (position.x == enemy.position.x && position.y == enemy.position.y) {
                TakeDamage();
            }
        }
        for (Projectile projectile : _LvLManager.projectiles) {
            if (position.x == projectile.position.x && position.y == projectile.position.y) {
                TakeDamage();
            }
        }

        if (health < 0){
            _LvLManager.LvLFinished();
        }
    }

    // key listener
    public class PlayerKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            // set flags on key presses
            if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();

            // reset flags on key release
            if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                upPressed = false;
            }
            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
            if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
        }
    }
    // alternativly make move save the curent state of states to a tmp states and leting key listener continously update states seperately
}
