package ImageCompressor.algorithm;

import ImageCompressor.Main;
import ImageCompressor.tools.IOConsole;
import ImageCompressor.tools.IOHandler;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ImageCompressor.tools.OutputImage.generateImage;
import static ImageCompressor.tools.OutputImage.setOutputFileName;

public class DecompressionHandler {
    IOHandler inputOutput = new IOConsole();
    private int width;
    private int height;
    private int[] pixels;
    byte[] rgbValuesByte;
    int[] decompressedPixels;

    private int[] getDecompressedPixels() {
        return decompressedPixels;
    }

    private void setDecompressedPixels(int[] decompressedPixels) {
        this.decompressedPixels = decompressedPixels;
    }

    private byte[] getRgbValuesByte() {
        return rgbValuesByte;
    }

    private void setRgbValuesByte(byte[] rgbValuesByte) {
        this.rgbValuesByte = rgbValuesByte;
    }

    private IOHandler getInputOutput() {
        return inputOutput;
    }

    private void setInputOutput(IOHandler inputOutput) {
        this.inputOutput = inputOutput;
    }

    private int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    private int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    private int[] getPixels() {
        return pixels;
    }

    private void setPixels(int[] pixels) {
        this.pixels = pixels;
    }


    /* @Author: David
     * This method is crucial to decompress every .tsk file compressed earlier
     * it receives the file path with the rgb values and decompress them
     * to convert it in a final bitmap image
     * */
    public void decompress(String route) {
        Path filePath  = extensionCheck(route);
        //Begin to read the file
        try {
            InputStream openedFile = new FileInputStream(filePath.toFile());
            DataInputStream fileInfo = new DataInputStream(openedFile);

            setWidth(fileInfo.readInt());
            setHeight(fileInfo.readInt());

            setPixels(new int[getWidth() * getHeight()]);
            setRgbValuesByte(new byte[getWidth() * getHeight() * 3]);

            fileInfo.readFully(getRgbValuesByte());

        }catch (Exception e){}

        setDecompressedPixels(new int[(getWidth() * getHeight())]);

        for (int i = 0; i < getDecompressedPixels().length/2; i++) {
            int red = (rgbValuesByte[i * 3] & 0xFF) << 16;
            int green = (rgbValuesByte[i * 3 + 1] & 0xFF) << 8;
            int blue = rgbValuesByte[i * 3 + 2] & 0xFF;

            getDecompressedPixels()[i*2] = red | green | blue;
        }

        calculateAvgRGB();

        setPixels(getDecompressedPixels());
        BufferedImage outputImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        int index = 0;
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int intValue = getPixels()[index++];
                outputImage.setRGB(x, y, intValue);
            }
        }

        generateImage(setOutputFileName(), outputImage, "bmp");
    }

    /* @Author: David
     * This method verify if the user is sending a valid file, this be detecting if the
     * file extension matches ".tsk", if not again is called the decompression method
     * */
    private Path extensionCheck(String route){
        Path filePath = Paths.get(route);
        String fileName = filePath.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (!extension.equals("tsk")) { //Manage the error if the provided file has not a .tsk extension
            getInputOutput().showInfo("The file you provided does not have a .tsk extension, try again");
            Main.menu(2);
        }

        return filePath;
    }

    /* @Author: David
     * The most important method in the class to obtain a good quality image
     * it applies again a mask over the previous and next pixel and calculate
     * the average value of every color to join them and create the new pixel
     * */
    private void calculateAvgRGB(){
        for (int i = 1; i < (getDecompressedPixels().length - 1); i += 2) {
            int prevRed = (getDecompressedPixels()[i - 1] >> 16) & 0xFF;
            int prevGreen = (getDecompressedPixels()[i - 1] >> 8) & 0xFF;
            int prevBlue = getDecompressedPixels()[i - 1] & 0xFF;

            int nextRed = (getDecompressedPixels()[i + 1] >> 16) & 0xFF;
            int nextGreen = (getDecompressedPixels()[i + 1] >> 8) & 0xFF;
            int nextBlue = getDecompressedPixels()[i + 1] & 0xFF;

            int avgRed = (prevRed + nextRed) / 2;
            int avgGreen = (prevGreen + nextGreen) / 2;
            int avgBlue = (prevBlue + nextBlue) / 2;

            getDecompressedPixels()[i] = (avgRed << 16) | (avgGreen << 8) | avgBlue;
        }
    }


}
