package com.qrreader.QRReader.test;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainWin1 {

    private JFrame frame;
    private JButton []jb=new JButton[4];
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	MainWin1 window = new MainWin1();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainWin1() {
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the contents of the frame.
     * @throws IOException 
     */
    private void initialize() throws IOException {
        frame = new JFrame();
        frame.setBounds(100, 100, 612, 519);
        setImage();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(1, 4));
        for(int i=0;i<4;i++){
            frame.add(jb[i]);
        }
    }

    public void setImage() throws IOException{
//        URL img=MainWin.class.getResource("/img/img.jpg");
    	String path="E:\\10.png";
        BufferedImage bimg=ImageIO.read(new File(path));
        int w=bimg.getWidth();
        int h=bimg.getHeight();
        int count=0;
            for(int i=0;i<4;i++){
                BufferedImage wim=bimg.getSubimage(i*w/4,0, w/4, h);
                Image sc=wim.getScaledInstance(frame.getWidth()/4,frame.getHeight(), Image.SCALE_AREA_AVERAGING);
                setupImage(count++,sc);
            }
    }

    private void setupImage(int i,Image wim) {
        jb[i]=new JButton(new ImageIcon(wim));
    }

}