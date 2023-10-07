package org.deil.utils.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

public class CaptchaUtil {
    private static CaptchaUtil captchaUtil;

    //private static final Random ran = new Random();
    private static final SecureRandom ran = new SecureRandom();

    private String code = "0123456789abcdefghijklmnopqrstDEFGHIJKLMNOPQRSTUVWXYZ";

    private int num = 4;

    private int height;

    private int width;

    public static CaptchaUtil getInstance() {
        if (captchaUtil == null)
            captchaUtil = new CaptchaUtil();
        return captchaUtil;
    }

    public void set(int width, int height, int num, String code) {
        this.width = width;
        this.height = height;
        this.num = num;
        this.code = code;
    }

    public void set(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void set(int width, int height, int num) {
        this.width = width;
        this.height = height;
        this.num = num;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String generateCheckcode() {
        StringBuilder cc = new StringBuilder();
        for (int i = 0; i < this.num; i++)
            cc.append(this.code.charAt(ran.nextInt(this.code.length())));
        return cc.toString();
    }

    public BufferedImage generateCheckImg(String checkcode) {
        BufferedImage img = new BufferedImage(this.width, this.height, 1);
        Graphics2D graphic = img.createGraphics();
        graphic.setColor(Color.WHITE);
        graphic.fillRect(0, 0, this.width, this.height);
        graphic.setColor(Color.BLACK);
        graphic.drawRect(0, 0, this.width - 1, this.height - 1);
        Font font = new Font("宋体", 3, (int)(this.height * 0.8D));
        graphic.setFont(font);
        int i;
        for (i = 0; i < this.num; i++) {
            graphic.setColor(new Color(ran.nextInt(180), ran.nextInt(180), ran.nextInt(180)));
            graphic.drawString(String.valueOf(checkcode.charAt(i)), i * this.width / this.num + 4, (int)(this.height * 0.8D));
        }
        for (i = 0; i < this.width + this.height; i++) {
            graphic.setColor(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
            graphic.drawOval(ran.nextInt(this.width), ran.nextInt(this.height), 1, 1);
        }
        for (i = 0; i < 4; i++) {
            graphic.setColor(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
            graphic.drawLine(0, ran.nextInt(this.height), this.width, ran.nextInt(this.height));
        }
        return img;
    }
}

