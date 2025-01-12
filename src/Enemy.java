abstract class Enemy extends Entity{
// protected
    protected LvLManager.LvLManagerHandle _LvLManagerHandle;    

// public
    public Enemy(short health, char texture, short colour, boolean collision, short x, short y, boolean faceRight, LvLManager.LvLManagerHandle _LvLManagerHandle) {
        super(health, texture, colour, collision, x, y);
        this._LvLManagerHandle = _LvLManagerHandle;
        if (faceRight) states |= RIGHT;
    }

    public abstract void Update();

// protected
    protected abstract void Move();
}
