package com.common.staticvoidmainTest;

import ai.djl.basicdataset.FashionMnist;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.dataset.Dataset;
import ai.djl.translate.TranslateException;
import com.javase.Container;
import com.javase.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ClassName: ImageClassification
 * Description: 图像分类 获取MNIST 数据集 并用JavaSE渲染
 * Author: James Zow
 * Date: 2020/10/7 0007 1:19
 * Version: 1.0
 **/
public class ImageClassification {

    /**
     * main方法入口
     *
     * @param args
     */
    public static void main(String[] args) {
        runExample();
    }

    public static void runExample(){
        int batchSize = 216;
        // 随机
        boolean randomShuffle = true;
        // 获取数据集
        ArrayDataset mnistTrain = getDataset(Dataset.Usage.TRAIN, batchSize, randomShuffle);
        ArrayDataset mnistTest = getDataset(Dataset.Usage.TEST, batchSize, randomShuffle);
        NDManager manager = NDManager.newBaseManager();
        System.out.println(mnistTrain.size());
        System.out.println(mnistTest.size());
        try {
            showImages(mnistTrain,18,28,28,4,manager);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据集
     *
     * @param batchSize     批次大小
     * @param randomShuffle 是否分别随机地对数据进行随机排序
     * @return
     */
    public static ArrayDataset getDataset(Dataset.Usage usage, int batchSize, boolean randomShuffle) {
        try {
            FashionMnist fashionMnist = FashionMnist.builder().optUsage(usage)
                    .setSampling(batchSize, randomShuffle)
                    .build();
            fashionMnist.prepare();
            return fashionMnist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取多个标签
     * @param labelIndices
     * @return
     */
    public static String[] getFashionMnistLabels(int[] labelIndices) {
        String[] textLabels = {"t恤衫", "裤子", "套头衫", "连衣裙", "外套",
                "凉鞋", "衬衫", "运动鞋", "包", "短靴"};
        String[] convertedLabels = new String[labelIndices.length];
        for (int i = 0; i < labelIndices.length; i++) {
            convertedLabels[i] = textLabels[labelIndices[i]];
        }
        return convertedLabels;
    }

    /**
     * 获取单个标签
     * @param labelIndice
     * @return
     */
    public static String getFashionMnistLabel(int labelIndice) {
        String[] textLabels = {"t恤衫", "裤子", "套头衫", "连衣裙", "外套",
                "凉鞋", "衬衫", "运动鞋", "包", "短靴"};
        return textLabels[labelIndice];
    }

    /**
     * 绘图
     * @param dataset 数据集
     * @param number 标签
     * @param WIDTH 宽度
     * @param HEIGHT 高度
     * @param SCALE
     * @param manager
     * @throws IOException
     * @throws TranslateException
     */
    public static void showImages(ArrayDataset dataset,
                           int number, int WIDTH, int HEIGHT, int SCALE,
                           NDManager manager)
            throws IOException, TranslateException {
        JFrame frame = new JFrame("服装数据标签");
        for (int record = 0; record < number; record++) {
            NDArray X = dataset.get(manager, record).getData().get(0).squeeze(-1);
            int y = (int)dataset.get(manager, record).getLabels().get(0).getFloat();
            BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = (Graphics2D) img.getGraphics();
            for(int i = 0; i < WIDTH; i++) {
                for(int j = 0; j < HEIGHT; j++) {
                    float c = X.getFloat(j, i) / 255;  // 缩小到0到1之间
                    g.setColor(new Color(c, c, c)); // 设置灰色
                    g.fillRect(i, j, 1, 1);
                }
            }
            JPanel panel = new ImagePanel(SCALE, img);
            panel.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
            JPanel container = new Container(getFashionMnistLabel(y));
            container.add(panel);
            frame.getContentPane().add(container);
        }
        frame.getContentPane().setLayout(new FlowLayout());
        frame.pack();
        frame.setVisible(true);
    }

}
