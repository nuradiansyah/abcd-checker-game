package com.game.checker.resources;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CheckerImageLoader {
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();
    
    public static ImageIcon loadImage(String imageName) {
        if (imageCache.containsKey(imageName)) {
            return imageCache.get(imageName);
        }
        
        try {
            String path = "/images/checker/" + imageName;
            URL imageURL = CheckerImageLoader.class.getResource(path);
            
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                imageCache.put(imageName, scaledIcon);
                return scaledIcon;
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + imageName);
            e.printStackTrace();
        }
        
        return createPlaceholderIcon(imageName);
    }
    
    private static ImageIcon createPlaceholderIcon(String imageName) {
        Image placeholder = new java.awt.image.BufferedImage(80, 80, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) placeholder.getGraphics();
        
        if (imageName.contains("red")) {
            g.setColor(new Color(200, 50, 50));
        } else {
            g.setColor(new Color(50, 50, 50));
        }
        
        g.fillOval(10, 10, 60, 60);
        
        if (imageName.contains("king")) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("K", 30, 50);
        }
        
        g.dispose();
        return new ImageIcon(placeholder);
    }
}
