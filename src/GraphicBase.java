class GraphicBase {
// protected
    protected boolean isCollision;
    protected char texture;
    protected short colour;

// public
    public GraphicBase(char texture, short colour, boolean isCollision) {
        this.texture = texture;
        this.colour = colour;
        this.isCollision = isCollision;
    }
    // no constructor needed as it's garbage collected

    public boolean getCollision() {
        return isCollision;
    }
    public char getTexture(){
        return texture;
    }
    public short getColour(){
        return colour;
    }
}
