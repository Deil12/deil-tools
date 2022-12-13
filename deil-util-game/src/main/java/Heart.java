import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * @PURPOSE 心
 * @DATE 2022/12/06
 * @CODE Deil
 * @see JFrame
 */
public class Heart extends JFrame {

    private static final long serialVersionUID = -1284128891908775645L;

    // 定义加载窗口大小

    public static final int GAME_WIDTH = 500;

    public static final int GAME_HEIGHT = 500;
    // 获取屏幕窗口大小
    public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public Heart() {
        // 设置窗口标题
        this.setTitle("");
        // 设置窗口初始位置
        this.setLocation((WIDTH - GAME_WIDTH) / 2, (HEIGHT - GAME_HEIGHT) / 2);
        // 设置窗口大小
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        // 设置背景色
        this.setBackground(Color.PINK);
        // 设置窗口关闭方式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口显示
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        double x, y, r;

        Image OffScreen =
                createImage(GAME_WIDTH, GAME_HEIGHT);
                //createImage(new FilteredImageSource(this.getClass().getResource("/images/bg.png"), new RGBGrayFilter()));
                //Toolkit.getDefaultToolkit().getImage("images/bg.jpg");

        Graphics drawOffScreen = OffScreen.getGraphics();
        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 90; j++) {
                r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j)) * 18;
                x = r * Math.cos(Math.PI / 45 * j) * Math.sin(Math.PI / 45 * i) + GAME_WIDTH / 2;
                y = -r * Math.sin(Math.PI / 45 * j) + GAME_HEIGHT / 4;
                //设置画笔颜色
                drawOffScreen.setColor(Color.RED);
                // 绘制椭圆
                drawOffScreen.fillOval((int) x, (int) y, 2, 2);
            }
            // 生成图片
            g.drawImage(OffScreen, 0, 0, this);
            try{ Thread.sleep(50); }catch(Exception e){ System.exit(0); }
        }
    }
    private static class RGBGrayFilter extends RGBImageFilter {
        public RGBGrayFilter() {
            canFilterIndexColorModel = true;
        }
        public int filterRGB(int x, int y, int rgb) {
            // find the average of red, green, and blue
            float avg = (((rgb >> 16) & 0xff) / 255f +
                    ((rgb >>  8) & 0xff) / 255f +
                    (rgb        & 0xff) / 255f) / 3;
            // pull out the alpha channel
            float alpha = (((rgb>>24)&0xff)/255f);
            // calc the average
            avg = Math.min(1.0f, (1f-avg)/(100.0f/35.0f) + avg);
            // turn back into rgb
            int rgbval = (int)(alpha * 255f) << 24 |
                    (int)(avg   * 255f) << 16 |
                    (int)(avg   * 255f) <<  8 |
                    (int)(avg   * 255f);
            return rgbval;
        }
    }

    /**
     * @TIME 2022/12/06 : 主要
     * @param args
     */
    public static void main(String[] args) {
        Heart demo = new Heart();
        demo.setVisible(true);
    }

}