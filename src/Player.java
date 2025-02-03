class Player extends Entity {
// private
    private static final short _health = 3;
    private static final char _texture = '#';
    private static final short _colour = Text.Formatting.RED.code;
    private static final boolean _collision = false;
    private LvLManager _LvLManager;

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

    /**
     * Poll input asynchronously using the Windows API.
     */
    private void Input() {
        // Clear movement-related bits (lower nibble)
        states &= ~0xF;  // Clear bits 0-3
        
        // Poll key states using our JNA wrapper:
        // 0x57 = 'W', 0x26 = Up arrow, 0x41 = 'A', 0x25 = Left arrow,
        // 0x53 = 'S', 0x28 = Down arrow, 0x44 = 'D', 0x27 = Right arrow
        
        if (isKeyPressed(0x57) || isKeyPressed(0x26)) {
            states |= UP;
        }
        if (isKeyPressed(0x41) || isKeyPressed(0x25)) {
            states |= LEFT;
        }
        if (isKeyPressed(0x53) || isKeyPressed(0x28)) {
            states |= DOWN;
        }
        if (isKeyPressed(0x44) || isKeyPressed(0x27)) {
            states |= RIGHT;
        }
    }
    
    /**
     * Helper function to wrap the GetAsyncKeyState call.
     * @param keyCode the virtual key code.
     * @return true if the key is currently pressed.
     */
    private boolean isKeyPressed(int keyCode) {
        // The 0x8000 mask checks the high-order bit (the key is down)
        return (User32.INSTANCE.GetAsyncKeyState(keyCode) & 0x8000) != 0;
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
}
