package ImageCompressor.algorithm;

import ImageCompressor.Main;
import ImageCompressor.tools.IOConsole;
import ImageCompressor.tools.IOHandler;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static ImageCompressor.tools.OutputImage.setOutputFileName;

public class CompressionHandler {

    private int imageWidth;
    private int imageHeight;
    static IOHandler inputOutput = new IOConsole();
    int[] pairValues;

    private int[] getPairValues() {
        return pairValues;
    }

    private void setPairValues(int[] pairValues) {
        this.pairValues = pairValues;
    }

    private int getImageWidth() {
        return imageWidth;
    }

    private void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    private int getImageHeight() {
        return imageHeight;
    }

    private void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    private static IOHandler getInputOutput() {
        return inputOutput;
    }

    private static void setInputOutput(IOHandler inputOutput) {
        CompressionHandler.inputOutput = inputOutput;
    }

    /* @Author: David
     * This is the main algorithm of the class that splits
     * the main data of the original image and save half of
     * the size and save it in a compressed file
     * */
    public void compress(BufferedImage inputImage) {
        try {
            setImageWidth(inputImage.getWidth());
            setImageHeight(inputImage.getHeight());
            if (getImageHeight() * getImageWidth() < 4) {
                throw new IOException();
            }
        } catch (Exception e) {
            getInputOutput().showInfo("The image is too small or its not an accepted file type, please provide a bigger image");
            Main.menu(1);
        }


        int[] pixelsNum = new int[getImageWidth() * getImageHeight()];
        inputImage.getRGB(0, 0, getImageWidth(), getImageHeight(), pixelsNum, 0, getImageWidth());

        setPairValues(new int[pixelsNum.length / 2]);

        for (int i = 0; i < pixelsNum.length; i += 2) {
            getPairValues()[i / 2] = pixelsNum[i];
        }

        byte[] pairValuesByte = new byte[getPairValues().length*3];
        for (int i = 0; i < getPairValues().length; i++) {
            int rgb = getPairValues()[i];
            //Apply binary mask to the rgb value
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            pairValuesByte[i * 3] = (byte) red;
            pairValuesByte[i * 3 + 1] = (byte) green;
            pairValuesByte[i * 3 + 2] = (byte) blue;
        }


        String fileName = setOutputFileName() + ".tsk";

        try {
            DataOutputStream outputFile = new DataOutputStream(new FileOutputStream(fileName));
            outputFile.writeInt(getImageWidth());
            outputFile.writeInt(getImageHeight());
            outputFile.write(pairValuesByte);

        } catch (Exception e) {
        }
    }


}
