package ImageCompressor;

import ImageCompressor.algorithm.CompressionHandler;
import ImageCompressor.algorithm.DecompressionHandler;
import ImageCompressor.tools.IOConsole;
import ImageCompressor.tools.IOHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    static IOHandler inputOutput = new IOConsole();
    static CompressionHandler compression = new CompressionHandler();
    static DecompressionHandler decompression = new DecompressionHandler();

    public static void main(String[] args) {
        IOHandler inputOutput = new IOConsole();

        int option = inputOutput.getInt("""
                        Welcome to the TSK image compressor, please choose an option:\s
                        1: I want to compress my image\s
                        2: I want to decompress an image""",
                "Not valid input, please choose 1 or 2");
        menu(option);
    }

    public static void menu(int option){

        String route;
        String imageRouteQuery= "Please provide the route of the image: ";
        String notValidMessage="Not valid input";

        switch (option) {
            case 1:
                route = inputOutput.getString(imageRouteQuery, notValidMessage);
                try {
                    File inputFile = new File(route);
                    BufferedImage inputImage = ImageIO.read(inputFile);
                    compression.compress(inputImage);
                } catch (IOException e) {
                    inputOutput.showInfo("There was an error while reading the file, please provide a valid bitmap image");
                }
                break;

            case 2:
                route = inputOutput.getString(imageRouteQuery, notValidMessage);
                try {
                    decompression.decompress(route);
                } catch (Exception e) {
                    inputOutput.showInfo("There was an error while reading the file, please provide a valid tsk File");
                }
                break;
        }

    }

}
