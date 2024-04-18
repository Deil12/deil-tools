import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/** 一：启动逻辑 */
public class SnakeTool {
    public static void main(String[] args) {
        new GameFrame();
    }
}

/** 二：界面定义 */
class GameFrame extends JFrame {
    GameFrame() {
        this.add(new GamePanel());
        this.setTitle("大桥中学创意编程");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();//调整此窗口的大小，以适合其子组件的首选大小和布局
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

/** 三：动作逻辑 */
class GamePanel extends JPanel implements ActionListener {
    // 定义英语单词显示
    static java.util.List<String> months = new ArrayList<>();
    static {
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
    }

    // 设置项
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 300; //DELAY为贪吃蛇移动速度 数字越小 贪吃蛇移动速度越快
    final int[] x = new int[GAME_UNITS];//
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 1;
    int eggsEaten;//纪录蛇吃了多少蛋
    int eggX;
    int eggY;
    char direction = 'R';//默认初始化窗口贪吃蛇的运动方向为向右
    boolean running = false;
    Timer timer;//引入计时器参数timer
    Random random;//随机数random

    // 执行动作
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));//组件高度长度确定为刚才设定的SCREEN_WIDTH和SCREEN_HEIGHT
        this.setBackground(Color.BLACK);//背景面板颜色设置为黑色
        this.setFocusable(true);//让窗口可以被操作 改为false 那么无法用键盘操纵蛇
        this.addKeyListener(new MyKeyAdapter());//确保窗口内的东西可以被键盘指定键操控
        startGame();
    }
    // 启动游戏
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();//窗口运行时，让蛇开始运动
            checkEgg();//检测蛇是否吃到蛋
            checkCollisions();//检测蛇头是否撞到了边界或者蛇身
        }
        repaint();//停止运行就改变画面
    }
    // 游戏开始
    public void startGame() {
        newEgg();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    // 游戏控制
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);//绘制地图 根据窗口大小来画出每次蛇移动的格子
            }
            g.setColor(Color.red);
            g.fillOval(eggX, eggY, UNIT_SIZE, UNIT_SIZE);//蛋占一个格子
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }//画出初始化绿色的蛇头位置，前面设定的x[],y[]用来确认蛇头
                else {
                    g.setColor(new Color(45, 180, 0));//绿色
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);//确认蛇身
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            // g.drawString("Score:" + eggsEaten, (SCREEN_WIDTH - getFontMetrics(g.getFont()).stringWidth("Score:" + eggsEaten)) / 2, g.getFont().getSize());//画出分数 分数的位置在窗口最上方中间
            g.drawString(months.get(eggsEaten % 10), 1, g.getFont().getSize());//画出分数 分数的位置在窗口最上方中间
        } else {
            gameOver(g);
        }
    }
    // 蛇动作
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;//当向上移动时，蛇头的纵坐标变为原纵坐标-单位长度
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;//当向下移动时，蛇头的纵坐标变为原纵坐标+单位长度
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;//当向左移动时，蛇头的横坐标变为原横坐标-单位长度
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;//当向右移动时，蛇头的横坐标变为原横坐标+单位长度
        }
    }

    // 蛋动作
    public void newEgg() {
        eggX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        eggY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }
    public void checkEgg() {
        if ((x[0] == eggX) && (y[0] == eggY)) {
            bodyParts++;
            eggsEaten++;
            newEgg();//当蛇头坐标与蛋坐标相同时，蛇身变长，分数加一，然后再生成新蛋
        }
    }
    // 发生碰撞
    public void checkCollisions() {
        //如果蛇头和蛇身相撞 停止运行
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //如果蛇头撞到窗口左边界 停止运行
        if (x[0] < 0) {
            running = false;
        }
        //如果蛇头撞到窗口右边界 停止运行
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //如果蛇头撞到窗口上边界 停止运行
        if (y[0] < 0) {
            running = false;
        }
        //如果蛇头撞到窗口下边界 停止运行
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        //如果停止运行，则计时器也停止
        if (!running) {
            timer.stop();
        }
    }
    // 游戏结束
    public void gameOver(Graphics g) {
        //当游戏失败时，也显示分数
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        // g.drawString("Score:" + eggsEaten, (SCREEN_WIDTH - getFontMetrics(g.getFont()).stringWidth("Score:" + eggsEaten)) / 2, g.getFont().getSize());
        g.drawString("Score:" + eggsEaten, 1, g.getFont().getSize());

        //游戏失败时，显示gameover
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        g.drawString("Game Over", (SCREEN_WIDTH - getFontMetrics(g.getFont()).stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }
    // 键盘监控
    public class MyKeyAdapter extends KeyAdapter {
        //首先我们要弄清楚一件事便是，当蛇前进时，是不能后退的，只能向左或向右，同理，蛇向左运动时，是没有办法向右的。
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {//键盘侦听，接受到的键为←时，如果这时蛇没有向右运动，则方向可变为←
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {//接受到的键为→时，如果这时蛇没有向左运动，则方向可变为→
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {//接受到的键为↑时，如果这时蛇没有向下运动，则方向可变为↑
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {//接受到的键为↓时，如果这时蛇没有向右运动，则方向可变为↓
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
