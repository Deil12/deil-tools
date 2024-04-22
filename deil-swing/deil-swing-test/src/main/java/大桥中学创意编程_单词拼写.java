import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class 大桥中学创意编程_单词拼写 extends JFrame {

    TextField tf;
    BufferedReader in = null;
    JPanel pane1, pane2, pane3;
    JLabel lab1, lab2;
    JButton bu,bu1;
    JLabel[] bx = new JLabel[15];
    String str, st;
    int n = 0;
    char c;

    大桥中学创意编程_单词拼写() {
        tf = new TextField(10);
        lab1 = new JLabel("从这里输入单词");
        lab2 = new JLabel("");
        bu = new JButton("随机获取一个四级单词");
        bu1 = new JButton("确定");
        bu1.addActionListener(new MyActionListener());
        bu.addActionListener(new MyActionListener());
        pane1 = new JPanel();
        pane1.add(lab1);
        pane1.add(tf);
        pane1.add(bu);
        pane1.add(bu1);
        add(pane1, BorderLayout.NORTH);
        pane2 = new JPanel();
        pane2.add(lab2);
        add(pane2, BorderLayout.SOUTH);
    }

    class MyActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("随机获取一个四级单词")){
                try {
                    in=new BufferedReader(new FileReader("/Users/d/Documents/Deil/BAKSPACE/CSAIR/java/tangbackground.java/commonUtil/src/main/test/Words.txt"));
                    // in=new BufferedReader(new FileReader("./Words.txt"));
                }catch (IOException a){
                    a.printStackTrace();
                }
                int  t= (int) (Math.random()*4502);
                int flag=0;
                String line=null;
                try {
                    while ((line=in.readLine())!= null) {
                        if(flag==t)break;
                        flag++;
                    }
                }catch (IOException a){
                    a.printStackTrace();
                }
                String[] comput = line.split(" ");
                str =comput[0];
                n = str.length();
                st=line.substring(n);
                pane3 = new JPanel();
                Font font = new Font(str, Font.PLAIN, 46);
                for (int i = 0; i < n; i++) {
                    char c = str.charAt(i);
                    bx[i] = new JLabel("" + c);
                    bx[i].setPreferredSize(new Dimension(80, 80));
                    bx[i].setFont(font);
                    bx[i].setForeground(Color.black);
                    pane3.add(bx[i]);
                    add(pane3, BorderLayout.CENTER);
                }
                tf.setText(null);
                setVisible(true);
            }
            if (e.getActionCommand().equals("确定")) {
                if(tf.getText().equals(str)){
                    lab2.setText("拼写正确，翻译为: "+st);
                }else {
                    lab2.setText("拼写错误");
                }
            }
        }
    }

    public static void main(String[] args) {
        大桥中学创意编程_单词拼写 wordTool = new 大桥中学创意编程_单词拼写();
        wordTool.setBounds(300, 200, 900, 300);
        wordTool.setResizable(false);
        wordTool.setVisible(true);
        wordTool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
