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
package org.pandarun.javase;

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
