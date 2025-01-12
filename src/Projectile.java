class Projectile extends Entity
{
// private
	private final LvLManager.LvLManagerHandle _LvLManagerHandle;

	private static final short _health = 8;
	private static final char _texture = '*';
	private static final short _colour = Text.Formatting.BRIGHT_MAGENTA.code;
	private static final boolean _collision = false;
	
	private final boolean isRight;

// public
	public Projectile(short x, short y, boolean isRight, LvLManager.LvLManagerHandle _LvLManagerHandle)
    {
        super(_health, _texture, _colour, _collision, x, y);
		this._LvLManagerHandle = _LvLManagerHandle;
        this.isRight = isRight;
    }
    @Override
	public void Update(){
        health--;
        Move();
        if (health < 0)
        _LvLManagerHandle.removeProjectile(this);

    }

    @Override
    public void TakeDamage(){
        _LvLManagerHandle.removeProjectile(this);
    }

// protected
    @Override
	protected void Move(){
        if(isRight)
            position.x++;
        else
            position.x--;
        if (BlockManager.getCollision(_LvLManagerHandle.mapp.get(position.x).get(position.y)))
            _LvLManagerHandle.removeProjectile(this);
    }
};

