package com.agical.bumblebee.imaging;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.agical.bumblebee.Storage;

public class Shot {
    private static final Imaging IMAGING = new Imaging();
    private static final Color LINE_GREEN = new Color(0x88, 0xaa, 0x88);
    private static final Color BB_GREEN = new Color(0xbb, 0xee, 0xbb);
    private static final Color TEXT_GREEN = new Color(0x00, 0x55, 0x00);
    private static int x = 0;
    private static int y = 1;
    protected final BufferedImage image;
    protected final List<Callout> callouts;
    protected final List<Rectangle> highlights;
    
    public Shot(BufferedImage image, List<Callout> callouts, List<Rectangle> highlights) {
        this.image = image;
        this.callouts = callouts;
        this.highlights = highlights;
    }

    public Shot crop(Rectangle rectangle) {
        compensateCalloutsForCropping(rectangle, callouts, 0);
        compensateHighlightsForCropping(rectangle, highlights, 0);
        return new Shot(getImaging().crop(image, rectangle), callouts, highlights);
    }

    protected static Imaging getImaging() {
        return IMAGING;
    }
    
    protected static BufferedImage addHighlights(BufferedImage image, float alpha, Rectangle... locations) {
        if(locations.length==0) return image;
        BufferedImage buffImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = buffImg.createGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.5F));
        graphics2D.drawImage(image, 0, 0, null);
    
        for (Rectangle location : locations) {
            BufferedImage highlightedPart = getImaging().crop(image, location);
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1F));
            graphics2D.drawImage(highlightedPart, location.x, location.y, null);
        }
        return buffImg;
    }

    public void writeAs(String linkReference) {
        File file = getAvailableImageFile(linkReference);
        file.getParentFile().mkdirs();
        BufferedImage withHighlights = addHighlights(image, 0.5F, highlights.toArray(new Rectangle[] {}));
        BufferedImage withCallouts = addCallouts(withHighlights, callouts);
        getImaging().writePng(file, withCallouts);
        Storage.store(linkReference, "[[" + file.getPath().substring(12) + "][]]");
    }

    private static BufferedImage addCallouts(BufferedImage rawImage, List<Callout> callouts) {
        if(callouts.isEmpty()) return rawImage;
        int margin = 30;
        compensateCalloutsForCropping(new Rectangle(0, 0, rawImage.getWidth(), rawImage.getHeight()), callouts, margin);
        BufferedImage marginedImage = new BufferedImage(rawImage.getWidth()+margin*2, rawImage.getHeight()+margin*2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = marginedImage.createGraphics();
        graphics2D.drawImage(rawImage, margin, margin, null);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int[] balloon = new int[] {15, 45};
        float[] direction = new float[2];
        int count = 1;
        int[] delta = new int[] {0,30};
        for (Callout annotation : callouts) {
            Rectangle location = annotation.getLocation();
            annotation.setNr(count);
            
            direction[x] = (location.x + location.width/2) - balloon[x];
            direction[y] = (location.y + location.height/2) - balloon[y];
    
            int[] target = new int[] {0,0};
            if(direction[x]!=0 && 
                    Math.abs(direction[y]/direction[x])<(((float)location.height)/location.width)) {
                if(direction[x]>0) {
                    // left
                    target = new int[] {location.x, location.y + location.height/2};
                } else {
                    // right
                    target = new int[] {location.x+location.width, location.y + location.height/2};
                }
            } else { 
                if(direction[y]>0) {
                    // top
                    target = new int[] {location.x+location.width/2, location.y};
                } else {
                    // bottom
                    target = new int[] {location.x+location.width/2, location.y+location.height};
                }
            }
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
            graphics2D.setColor(LINE_GREEN);
            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.drawLine(balloon[x], balloon[y], target[x], target[y]);
            //graphics2D.draw(location);
    
            writeBall(graphics2D, balloon, count);
            createBallImage(count, annotation);
            balloon[x] = balloon[x]+delta[x];
            balloon[y] = balloon[y]+delta[y];
            if(balloon[y]>rawImage.getHeight()+margin*1.5&&delta[y]==margin) {
                balloon[x] = (int) (margin*1.5);
                balloon[y] = (int) (rawImage.getHeight()+margin*1.5);
                delta[y] = 0;
                delta[x] = margin;
            }
            count++;
        }
        return marginedImage;
    }

    private static void compensateCalloutsForCropping(Rectangle cropArea, List<Callout> callouts, int margin) {
        for (Callout annotation : callouts) {
            Rectangle location = annotation.getLocation();
            location.x = location.x + margin-cropArea.x;
            location.y = location.y + margin-cropArea.y;
        }
    }

    private static void compensateHighlightsForCropping(Rectangle cropArea, List<Rectangle> callouts, int margin) {
        for (Rectangle location : callouts) {
            location.x = location.x + margin-cropArea.x;
            location.y = location.y + margin-cropArea.y;
        }
    }

    private static void createBallImage(int count, Callout annotation) {
        BufferedImage ballImage = new BufferedImage(22,22, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ballGraphics2D = ballImage.createGraphics();
        writeBall(ballGraphics2D, new int[] {10,10}, count);
        String imageUrl = "images/balls/ball_" + count + ".png";
        annotation.setIcon("[[" + imageUrl + "][]]");
        File file = new File("target/site/" + imageUrl );
        file.getParentFile().mkdirs();
        getImaging().writePng(file, ballImage);
    }

    private static void writeBall(Graphics2D graphics2D, int[] center, int count) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
        graphics2D.setColor(BB_GREEN);
        graphics2D.fillOval(center[x]-10, center[y]-10, 20, 20);
    
        graphics2D.setColor(TEXT_GREEN);
        graphics2D.drawString(count+"", center[x]-4, center[y]+5);
    
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5F));
        graphics2D.setColor(LINE_GREEN);
        graphics2D.drawOval(center[x]-10, center[y]-10, 20, 20);
    }

    private File getAvailableImageFile(String linkReference) {
        File file = new File("target/site/images/" + linkReference + ".png");
        int imageCounter = 1;
        while(file.exists())  {
            file = new File("target/site/images/" + linkReference + "_" + imageCounter + ".png");
            imageCounter++;
        } 
        return file;
    }

    public Shot callout(Rectangle location, String text, String annotationReference) {
        Callout callout = new Callout();
        callout.setText(text);
        callout.setLocation(location);
        Storage.store(annotationReference, callout);
        callouts.add(callout);
        return this;
    }

    public Shot highlight(Rectangle... rectanglesForElements) {
        highlights.addAll(Arrays.asList(rectanglesForElements));
        return this;
    }
    
}
