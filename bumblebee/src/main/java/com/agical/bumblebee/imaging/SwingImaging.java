package com.agical.bumblebee.imaging;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Utility methods to get and manipulate screenshots from Swing GUIs or web pages (e.g. 
 * using Selenium)
 */
public class SwingImaging extends Imaging {
    private final ShotStrategy shotStrategy;
    private static final Robot ROBOT = createRobot();

    private SwingImaging(ShotStrategy shotStrategy) {
        this.shotStrategy = shotStrategy;
    }
    private static Robot createRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new RuntimeException( "Stupid exception", e );
        }
    }
    public static SwingImaging usingScreenShot() {
        return new SwingImaging(new ScreenShotStrategy());
    }
    public static SwingImaging usingSwing() {
        return new SwingImaging(new ComponentShotStrategy());
    }
    private static interface ShotStrategy {
        BufferedImage createImageForComponent(Component component, int imageType);
    }
    private static class ScreenShotStrategy implements ShotStrategy {
        public BufferedImage createImageForComponent(Component component, int imageType) {
            try {
                Point location = component.getLocationOnScreen();
                Dimension size = component.getSize();
                Rectangle componentRectangle = new Rectangle(location.x, location.y, size.width, size.height);
                BufferedImage image = ROBOT.createScreenCapture(componentRectangle);
                return image;
            } catch (HeadlessException e) {
                throw new RuntimeException("Couldn't create shot of component " + component, e);
            }
        }
    }
    private static class ComponentShotStrategy implements ShotStrategy {
        public BufferedImage createImageForComponent(Component component, int imageType) {
            Dimension componentSize = component.getSize();
            BufferedImage img = new BufferedImage(componentSize.width, componentSize.height, imageType);
            Graphics2D grap = img.createGraphics();
            grap.fillRect(0, 0, img.getWidth(), img.getHeight());
            component.paint(grap);
            return img;
        }
    }
    
    public JFrame showComponentInFrame(Component component) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(component);
        frame.setSize(new Dimension(200, 150));
        frame.setLocation(100, 100);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Write the component as a PNG image to the provided output stream target.
     * @param object
     * @param target
     * @throws IOException
     */
    public void writePng(Component object, OutputStream target) throws IOException {
        Component component = (Component) object;
        BufferedImage bufferedImage = create(component);
        ImageIO.write(bufferedImage, "png", target);
    }
    /**
     * Creates a buffered image of type TYPE_INT_RGB from the supplied
     * component. This method will use the preferred size of the component as
     * the image's size.
     * 
     * @param component
     *            the component to draw
     * @return an image of the component
     */
    public BufferedImage create(Component component) {
        return createImage(component, BufferedImage.TYPE_INT_RGB);
    }
    
    /**
     * Creates a buffered image (of the specified type) from the supplied
     * component. This method will use the preferred size of the component as
     * the image's size.
     * 
     * @param component
     *            the component to draw
     * @param imageType
     *            the type of buffered image to draw, e.g. BufferedImage.TYPE_XXX_XXX
     * 
     * @return an image of the component
     */
    public BufferedImage createImage(Component component, int imageType) {
        return shotStrategy.createImageForComponent(component, imageType);
    }
    
    
}
