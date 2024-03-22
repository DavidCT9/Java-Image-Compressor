package ImageCompressor.tools;

import ImageCompressor.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OutputImage {

    static IOHandler inputOutput = new IOConsole();

    private static IOHandler getInputOutput() {
        return inputOutput;
    }

    private static void setInputOutput(IOHandler inputOutput) {
        OutputImage.inputOutput = inputOutput;
    }

    /* @Author: David
     * This method is essential once the decompression algorithm is applied
     * the values of name the name, the processed image and the extension
     * will be passed to finally create the decompress and image result
     * */
    public static void generateImage(String imageName, BufferedImage image, String fileExtension) {
        File outputImage = new File(imageName + "." + fileExtension);
        try {
            ImageIO.write(image, fileExtension, outputImage);
        } catch (IOException e) {
            getInputOutput().showInfo("File extension or the file configuration was corrupted");
            Main.menu(2);
        }
    }

    /* @Author: David
     * This method is called when needed to assign a valid name
     * for the output file
     * */
    public static String setOutputFileName() {
        String fileName = "";
        try {
            fileName = getInputOutput().getString("Please provide the desired name for the output file", "Not valid input for a name");
        } catch (Exception e) {
            getInputOutput().showInfo("Not valid input to name the file");
            setOutputFileName();
        }

        return fileName;
    }

}
