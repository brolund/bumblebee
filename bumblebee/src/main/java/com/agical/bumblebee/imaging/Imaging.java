package com.agical.bumblebee.imaging;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Utility methods to get and manipulate screenshots from Swing GUIs or web pages (e.g. 
 * using Selenium)
 */
public class Imaging {
    /**
     * Write a buffered image as a PNG to a file
     * @param file
     * @param image
     * @throws IOException
     */
    public void writePng(File file, BufferedImage image) {
        try {
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            throw new RuntimeException( "", e );
        }
    }
    /**
     * Returns a new image with a horizontal slice removed,
     * @param image
     * @param sliceStart
     * @param sliceEnd
     * @return
     */
    public BufferedImage removeHorizontalSlice(BufferedImage image, int sliceStart, int sliceEnd) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage top = image.getSubimage(0, 0, width, sliceStart);
        BufferedImage bottom = image.getSubimage(0, sliceEnd, width, height - sliceEnd);
        
        BufferedImage mergedImage = new BufferedImage(width, height - (sliceEnd - sliceStart),
                BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = mergedImage.getRaster();
        raster.setRect(0, 0, top.getRaster());
        raster.setRect(0, sliceStart, bottom.getRaster());
        mergedImage.setData(raster);
        return mergedImage;
    }
    
    /**
     * Crop a png file on disk, i.e. the old file will be overwritten with the new cropped version.
     * @param filename
     * @param x
     * @param y
     * @param width
     * @param height
     * @throws IOException
     */
    public void cropPngOnDisk(String filename, int x, int y, int width, int height) {
        File file = new File(filename);
        BufferedImage bufferedImage = read(file);
        BufferedImage image = crop(bufferedImage, x, y, width, height);
        writePng(file, image);
    }
    
    /**
     * Read a file on disk and return an image
     * @param file
     * @return
     * @throws IOException
     */
    public BufferedImage read(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            return bufferedImage;
        } catch (Exception e) {
            throw new RuntimeException( "", e );
        }
    }
    
    /**
     * Crop an image
     * @param bufferedImage
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public BufferedImage crop(BufferedImage bufferedImage, int x, int y, int width, int height) {
        return bufferedImage.getSubimage(x, y, width, height);
    }
    public BufferedImage crop(BufferedImage image, Rectangle rectangle) {
        return crop(image, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    public static BufferedImage getScreenPart(Rectangle rectangle) {
        try {
            BufferedImage image = (new Robot()).createScreenCapture(rectangle);
            return image;
        } catch (Exception e) {
            throw new RuntimeException( "", e );
        }
    }
    public static BufferedImage getScreenShot() {
        return getScreenPart(getScreenArea());
    }
    public static Rectangle getScreenArea() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        return screenRect;
    }
    
}
