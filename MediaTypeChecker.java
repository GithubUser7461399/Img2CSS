import java.io.File;

public class MediaTypeChecker {
    // List of common photo and video extensions
    String[] photoExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff"};
    String[] videoExtensions = {".mp4", ".avi", ".mov", ".mkv", ".wmv", ".flv"};
    public Boolean isVideo(File directory) {
        //File directory = new File(directoryPath);
        boolean isVideo = false;
        if (directory.isFile()) {
            String fileName = directory.getName().toLowerCase();

            // Check for video extensions
            for (String ext : videoExtensions) {
                if (fileName.endsWith(ext)) {
                    isVideo = true;
                    break;
                }
            }
        }
        return isVideo;
    }
    public Boolean directoryIsOnlyPhotos(String directoryPath) {
        File directory = new File(directoryPath);

        Boolean hasOnlyPhotos = true;
        Boolean fileIsPhoto = false;
        // Check each file in the directory
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String fileName = file.getName().toLowerCase();
                fileIsPhoto = false;
                for (String ext : photoExtensions) {
                    if (fileName.endsWith(ext)) {
                        fileIsPhoto = true;
                        break;
                    }
                }
            }
            if (!fileIsPhoto) {
                hasOnlyPhotos = false;
            }
        }
        return hasOnlyPhotos;
    }
}
