class Enemy1 extends Enemy{
// private
	private static final short _health = 1;
	private static final char _texture = 'E';
	private static final short _colour = Text.Formatting.BRIGHT_YELLOW.code; 
	private static final boolean _collision = false;

// public
	public Enemy1(short x, short y, boolean faceRight, LvLManager.LvLManagerHandle _LManagerHandle)
    {
        super(_health, _texture, _colour, _collision, x, y, faceRight, _LManagerHandle);
		if (faceRight) states |= RIGHT;
	}
    @Override
	public void Update() {
        Move();
    }

    @Override
    public void TakeDamage() {
        health--;
        if (health <= 0)
            _LvLManagerHandle.removeEnemy(this);
    }

// protected
    @Override
    protected void Move() {
        // Check if the enemy is grounded
        if (!BlockManager.getCollision(_LvLManagerHandle.mapp.get(position.x).get(position.y + 1))) {
            position.y++; // Fall
            states &= ~DOWN; // Clear Down state
            return;
        } else if ((states & DOWN) == 0) { // If not already in Down state
            states |= DOWN; // Set Down state
            return;
        }

        // Slow down movement speed to half (this part can be adjusted as needed)

        // Handle movement to the right or left
        if ((states & RIGHT) != 0) { // If moving right
            if (position.x < LvLManager.width - 1 && !BlockManager.getCollision(_LvLManagerHandle.mapp.get(position.x + 1).get(position.y))) {
                position.x++; // Move right
            } else if (position.x > 0 && !BlockManager.getCollision(_LvLManagerHandle.mapp.get(position.x - 1).get(position.y))) {
                states &= ~RIGHT; // Stop moving right
                position.x--; // Move left instead
            }
        } else if (position.x > 0 && !BlockManager.getCollision(_LvLManagerHandle.mapp.get(position.x - 1).get(position.y))) {
            position.x--; // Move left
        } else if (position.x < LvLManager.width - 1 && !BlockManager.getCollision(_LvLManagerHandle.mapp.get(position.x + 1).get(position.y))) {
            states |= RIGHT; // Start moving right
            position.x++; // Move right
        }

        // Clear Down state after moving
        states &= ~DOWN;

        // Check if the enemy is out of bounds and remove it
        if (position.x < 0 || position.x >= LvLManager.width || position.y < 0 || position.y >= LvLManager.hight) {
            _LvLManagerHandle.removeEnemy(this); // Remove this enemy from the list
        }
    }
};

