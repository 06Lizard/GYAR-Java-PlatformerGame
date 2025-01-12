abstract class Entity extends GraphicBase {
// protected
    protected short health;

// public
    public Position position;

    // holds Entity's variables / flags
    public byte states = 0b00000000;

    // define the flags and variable of Entities
    public static final byte UP          = 1 << 0; // 0000 0001
    public static final byte LEFT        = 1 << 1; // 0000 0010
    public static final byte DOWN        = 1 << 2; // 0000 0100
    public static final byte RIGHT       = 1 << 3; // 0000 1000
    public static final byte IS_GROUNDED = 1 << 4; // 0001 0000
    public static final byte IS_JUMP     = 1 << 5; // 0010 0000
    public static final byte JUMP_TIME   = (byte) 0b11000000; // 2-bit variable (bits 6-7)

// protected
    protected abstract void Move();

// public
    public Entity(short health, char texture, short colour, boolean collision, short x, short y) {
        // calls the constructor of the superclass, aka: GraphicBase
        super(texture, colour, collision);

        // and then initializing the Entity fields
        this.health = health;
        position = new Position(x, y);
    }

    public final short getHealth() { return health; }
    public abstract void Update();
    public abstract void TakeDamage();
}
