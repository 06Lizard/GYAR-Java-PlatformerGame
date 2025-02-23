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
            case ground:
                return '=';
            case flag:
                return 'F';
            case flagPole:
                return '|';        
            default:
                return ' ';
        }
    }
	public static short getColour(final char block){
        switch (block) {
            case ground:
                return Text.Formatting.GREEN.code;
            case flag:
                return Text.Formatting.BRIGHT_RED.code;
            case flagPole:
                return Text.Formatting.GRAY.code;
            default:
                return Text.Formatting.RESET_ALL.code;
        }
    }
};