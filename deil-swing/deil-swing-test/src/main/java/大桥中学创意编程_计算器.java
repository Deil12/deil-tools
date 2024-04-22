import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class 大桥中学创意编程_计算器 {

    //静态变量，设置输入框文本。
    static String input = "";

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        JFrame frame = new JFrame("计算器");
        frame.setSize(250, 230);
        frame.setLocationRelativeTo(null);
        //设置界面水平布局
        frame.setLayout(new FlowLayout());

        String[] name = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "=", "+"};

        JButton[] buttons = new JButton[17];
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 30));
        frame.add(textField);

        create_button(name, buttons);
        add_button(buttons, frame);
        add_listener(textField, buttons, name);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //创建按钮，用数组接收。
    public static void create_button(String[] name, JButton[] bts) {
        JButton bt = new JButton("clear");
        bts[0] = bt;
        for (int i = 0; i < name.length; i++) {
            JButton b = new JButton(name[i]);
            //设置按钮大小
            b.setPreferredSize(new Dimension(50, 30));
            bts[i + 1] = b;
        }
    }

    //将按钮添加到面板上。
    public static void add_button(JButton[] bts, JFrame f) {
        for (int i = 0; i < bts.length; i++) {
            f.add(bts[i]);
        }
    }

    //增加按钮监听
    public static void add_listener(JTextField tf, JButton[] bts, String[] name) {
        for (int i = 0; i < bts.length; i++) {
            String text = bts[i].getText();
            bts[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    set_text(tf, text);
                }
            });
        }
    }

    //设置文本框的显示文字以及计算结果
    public static void set_text(JTextField tf, String s) {
        if (s.equals("=")) {
            //进行字符串表达式的运算
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
            try {
                input = String.valueOf(scriptEngine.eval(input));
                tf.setText(input);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        } else if (s.equals("clear")) {
            input = "";
            tf.setText(input);
        } else {
            input = input + s;
            tf.setText(input);
        }
    }

}
