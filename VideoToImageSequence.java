import java.io.File;
import java.io.IOException;

public class VideoToImageSequence {
    public File generateImageSequence(File videoFilePath, File dir) {
        // Ensure the output directory exists
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // FFmpeg command to extract frames
        String command = String.format("ffmpeg -i %s %s", videoFilePath, dir);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true); // Redirect error stream to output stream
            Process process = processBuilder.start();

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Image sequence saved to " + dir);
            } else {
                System.out.println("Error occurred during conversion. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }   
        return dir;
    }
}