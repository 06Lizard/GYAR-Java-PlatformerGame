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

    static void waitForAnyKey() {
        try {
            // Using System.in.read() waits for one byte of input
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}