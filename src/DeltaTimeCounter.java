import java.util.Arrays;

public class DeltaTimeCounter {

    private static final int SAMPLE_SIZE = 100;
    private long lastFrameTime;
    private double[] frameTimes;
    private int frameIndex;
    private double runningSum;
    private int validSamples;

    public DeltaTimeCounter() {
        lastFrameTime = System.nanoTime();
        frameTimes = new double[SAMPLE_SIZE];
        frameIndex = 0;
        runningSum = 0.0;
        validSamples = 0;
    }

    public void reset() {
        lastFrameTime = System.nanoTime();
        Arrays.fill(frameTimes, 0.0);
        frameIndex = 0;
        runningSum = 0.0;
        validSamples = 0;
    }

    public void count() {
        long currentFrameTime = System.nanoTime();
        double delta = (currentFrameTime - lastFrameTime) / 1_000_000_000.0; // Convert to seconds
        lastFrameTime = currentFrameTime;

        // remove old value from running sum
        runningSum -= frameTimes[frameIndex];

        // sore new delta time in circular buffer and add to running sum
        frameTimes[frameIndex] = delta;
        runningSum += delta;

        // increment frameIndex, wrapping around using modulo
        frameIndex = (frameIndex + 1) % SAMPLE_SIZE;

        // increment valid samples without exceeding the sample size
        if (validSamples < SAMPLE_SIZE) {
            validSamples++;
        }
    }

    public void countAndDisplay(int x, int y, String whatCounting) {
        long currentFrameTime = System.nanoTime();
        double delta = (currentFrameTime - lastFrameTime) / 1_000_000_000.0; // Convert to seconds

        // remove old value from running sum
        runningSum -= frameTimes[frameIndex];

        // sore new delta time in circular buffer and add to running sum
        frameTimes[frameIndex] = delta;
        runningSum += delta;

        // increment frameIndex, wrapping around using modulo
        frameIndex = (frameIndex + 1) % SAMPLE_SIZE;

        // increment valid samples without exceeding the sample size
        if (validSamples < SAMPLE_SIZE) {
            validSamples++;
        }

        display(x, y, whatCounting);
        lastFrameTime = System.nanoTime();
    }

    public void display(int x, int y, String whatCounting) {
        if (validSamples < SAMPLE_SIZE) {
            // displays how far till valid sampleSize
            System.out.print("\033[" + y + ";" + x + "H\033[2KLoading " + whatCounting + ": " 
                + validSamples + "/" + SAMPLE_SIZE + " samples");
        }
        else {
            // display deltaTime avrage (ms)
            //System.out.print("\033[" + y + ";" + x + "H\033[2KAverage " + whatCounting + " Time: "
            //    + (averageDelta > 0.0 ? averageDelta * 1000.0 : 0.0) + " ms");
            // this version below only displays 3 decimal places
            double averageDelta = getDelta();
            System.out.print("\033[" + y + ";" + x + "H\033[2KAverage " + whatCounting + " Time: " 
                + String.format("%.3f", averageDelta * 1000.0) + " ms");
        }
    }

    public double getDelta() {
        if (validSamples < SAMPLE_SIZE) {
            return Double.NaN; // return NaN when not enough samples
        }
        // return the average delta time
        return runningSum / SAMPLE_SIZE;
    }
}
