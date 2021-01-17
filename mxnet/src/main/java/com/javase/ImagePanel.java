/*
 * Copyright 2021 Apache All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
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
