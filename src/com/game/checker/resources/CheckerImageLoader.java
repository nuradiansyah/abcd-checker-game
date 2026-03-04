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
    
    public static ImageIcon loadScaledImage(String imageName, int size) {
        String cacheKey = imageName + "_" + size;
        if (imageCache.containsKey(cacheKey)) {
            return imageCache.get(cacheKey);
        }
        
        try {
            String path = "/images/checker/" + imageName;
            URL imageURL = CheckerImageLoader.class.getResource(path);
            
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                imageCache.put(cacheKey, scaledIcon);
                return scaledIcon;
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + imageName);
            e.printStackTrace();
        }
        
        return createPlaceholderIcon(imageName, size);
    }
    
    private static ImageIcon createPlaceholderIcon(String imageName) {
        return createPlaceholderIcon(imageName, 80);
    }
    
    private static ImageIcon createPlaceholderIcon(String imageName, int size) {
        Image placeholder = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) placeholder.getGraphics();
        
        if (imageName.contains("red")) {
            g.setColor(new Color(200, 50, 50));
        } else {
            g.setColor(new Color(50, 50, 50));
        }
        
        int margin = size / 8;
        int ovalSize = size - (2 * margin);
        g.fillOval(margin, margin, ovalSize, ovalSize);
        
        if (imageName.contains("king")) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, size * 3 / 8));
            g.drawString("K", size * 3 / 8, size * 5 / 8);
        }
        
        g.dispose();
        return new ImageIcon(placeholder);
    }
}
