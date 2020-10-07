package com.fastdjl.javase;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

/**
 * ClassName: Container
 * Description: 容器类
 * Author: James Zow
 * Date: 2020/10/6 3:05
 * Version:
 **/
public class Container extends JPanel {

    public Container(String label) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(label, JLabel.CENTER);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(l);
    }
    public Container(String trueLabel, String predLabel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel l = new JLabel(trueLabel, JLabel.CENTER);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(l);
        JLabel l2 = new JLabel(predLabel, JLabel.CENTER);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(l2);
    }

}
