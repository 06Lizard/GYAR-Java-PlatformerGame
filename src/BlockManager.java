class BlockManager
{
// public
	// blockMasks
	public static final char ground = 'G';
	public static final char flag = 'F';
	public static final char flagPole = 'f';

	public static boolean getCollision(final char block){
        switch (block) {
            case 'G':
                return true;        
            default:
                return false;
        }
    }
	public static char getTexture(final char block){
        switch (block) {
            case 'G':
                return '=';
            case 'F':
                return 'F';
            case 'f':
                return '|';        
            default:
                return ' ';
        }
    }
	public static short getColour(final char block){
        switch (block) {
            case 'G':
                return 0; //Text::Green;
            case 'F':
                return 0; //Text::BrightRed;
            case 'f':
                return 0; //Text::Gray;
            default:
                return 0; //Text::ResetAll;
        }
    }
};