import com.sun.jna.Library;
import com.sun.jna.Native;

public interface User32 extends Library {
    // Load the user32 DLL from Windows
    User32 INSTANCE = Native.load("user32", User32.class);
    
    /**
     * Retrieves the status of the specified virtual key.
     * @param vKey The virtual-key code.
     * @return a short with key state information.
     */
    short GetAsyncKeyState(int vKey);


    /**
     * Helper function to wrap the GetAsyncKeyState call.
     * @param keyCode the virtual key code.
     * @return true if the key is currently pressed.
     */
    static boolean isKeyPressed(int keyCode) {
        // The 0x8000 mask checks the high-order bit (the key is down)
        return (User32.INSTANCE.GetAsyncKeyState(keyCode) & 0x8000) != 0;
    }

    static void waitForAnyKey() {
        while (true) {
            for (int i = 8; i <= 255; i++) { // Scan for key presses
                if (User32.isKeyPressed(i)) {
                    return; // Exit when a key is pressed
                }
            }
            try {
                Thread.sleep(5); // Reduce CPU usage
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper instances for the keyCodes:
    static final int VK_UP = 0x26;
    static final int VK_DOWN = 0x28;
    static final int VK_LEFT = 0x25;
    static final int VK_RIGHT = 0x27;
    static final int VK_W = 0x57;
    static final int VK_A = 0x41;
    static final int VK_S = 0x53;
    static final int VK_D = 0x44;
    static final int VK_SPACE = 0x20;
    static final int VK_RETURN = 0x0D;
}