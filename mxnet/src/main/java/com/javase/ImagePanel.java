package com.javase;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * ClassName: ImagePanel
 * Description: 图层绘画类
 * Author: James Zow
 * Date: 2020/10/6 3:04
 * Version:
 **/
public class ImagePanel extends JPanel {

    int SCALE;
    BufferedImage img;

    public ImagePanel() {
        this.SCALE = 1;
    }
    public ImagePanel(int scale, BufferedImage img) {
        this.SCALE = scale;
        this.img = img;
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.scale(SCALE, SCALE);
        g2d.drawImage(this.img, 0, 0, this);
    }
}
