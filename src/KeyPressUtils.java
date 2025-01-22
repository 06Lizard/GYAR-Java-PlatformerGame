import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyPressUtils {
    private static volatile boolean keyPressed = false;

    public static void waitForAnyKey() {
        keyPressed = false;

        // Create a dummy frame to capture key events
        Frame dummyFrame = new Frame();
        dummyFrame.setSize(0, 0); // Minimize the frame size
        dummyFrame.setUndecorated(true); // Remove window decorations
        dummyFrame.setVisible(true);
        dummyFrame.setFocusable(true);

        // Add a KeyListener to detect any key press
        dummyFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                keyPressed = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        // Wait for a key press
        while (!keyPressed) {
            try {
                Thread.sleep(50); // Brief sleep to reduce CPU usage
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Dispose of the dummy frame after detecting a key press
        dummyFrame.dispose();
    }
}