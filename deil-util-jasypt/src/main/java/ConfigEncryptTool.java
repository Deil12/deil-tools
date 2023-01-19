import org.apache.commons.codec.binary.Base64;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.crypto.Cipher;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @PURPOSE 配置类加密工具
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
public class ConfigEncryptTool {

    /*
     * 主窗口
     */
    private JFrame mainFrame;

    /*
     * 原文
     */
    private JTextArea originalTextArea;

    /*
     * 密文
     */
    private JTextArea ciphertextArea;

    /*
     * 盐
     */
    private JTextArea salttextArea;

    /*
     * 按钮
     */
    private JPanel controlPanel;
    private JPanel modePanel;

    public static String MODE = "byPooledPBE";

    public ConfigEncryptTool() {
        //主窗口
        mainFrame = new JFrame("jasypt加密解密工具");
        mainFrame.setSize(600, 1000);
        mainFrame.setLayout(new GridLayout(5, 1));
        mainFrame.setBackground(Color.BLACK);
        mainFrame.setForeground(Color.WHITE);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 18);

        //盐值文本框
        salttextArea = new JTextArea();
        salttextArea.setBackground(Color.BLACK);
        salttextArea.setForeground(Color.WHITE);
        salttextArea.setFont(font);
        salttextArea.setWrapStyleWord(true);
        salttextArea.setLineWrap(true);
        salttextArea.setBorder(BorderFactory.createTitledBorder( " 盐值： " ));
        JScrollPane saltText = new JScrollPane(salttextArea);
        saltText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //原文文本框
        originalTextArea = new JTextArea();
        originalTextArea.setBackground(Color.BLACK);
        originalTextArea.setForeground(Color.WHITE);
        originalTextArea.setFont(font);
        originalTextArea.setWrapStyleWord(true);
        originalTextArea.setLineWrap(true);
        originalTextArea.setBorder(BorderFactory.createTitledBorder( " 原文： " ));
        JScrollPane originalText = new JScrollPane(originalTextArea);
        originalText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //密文文本框
        ciphertextArea = new JTextArea();
        ciphertextArea.setBackground(Color.BLACK);
        ciphertextArea.setForeground(Color.WHITE);
        ciphertextArea.setFont(font);
        ciphertextArea.setWrapStyleWord(true);
        ciphertextArea.setLineWrap(true);
        ciphertextArea.setBorder(BorderFactory.createTitledBorder( " 密文： " ));
        JScrollPane cipherText = new JScrollPane(ciphertextArea);
        cipherText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //按钮
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder( " 2nd 工具执行 " ));
        //controlPanel.setBackground(Color.BLACK);
        //controlPanel.setForeground(Color.WHITE);
        modePanel = new JPanel();
        modePanel.setLayout(new FlowLayout());
        modePanel.setBorder(BorderFactory.createTitledBorder( " 1st 加密方式 " ));
        //modePanel.setBackground(Color.BLACK);
        //modePanel.setForeground(Color.WHITE);
        //窗口监测
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        //布置窗口
        mainFrame.add(modePanel);
        mainFrame.add(saltText);
        mainFrame.add(originalText);
        mainFrame.add(controlPanel);
        mainFrame.add(cipherText);
        mainFrame.setVisible(true);
        mainFrame.setBackground(Color.BLACK);
        mainFrame.setForeground(Color.WHITE);
        //prepareGUI();
    }

    public static void main(String[] args) {
        ConfigEncryptTool configEncrypt = new ConfigEncryptTool();
        configEncrypt.showEventDemo();
    }

    private void showEventDemo() {
        Font font = new Font(Font.MONOSPACED, Font.ITALIC, 18);
        //按钮栏
        JButton encryptButton = new JButton("加密");
        JButton copyButton = new JButton("复制密文到剪切板");
        JButton decryptButton = new JButton("解密");
        encryptButton.setFont(font);
        decryptButton.setFont(font);
        decryptButton.setBackground(Color.green);
        copyButton.setFont(font);
        //按钮事件
        encryptButton.setActionCommand("encrypt");
        copyButton.setActionCommand("copy");
        decryptButton.setActionCommand("decrypt");
        encryptButton.addActionListener(new ButtonClickListener());
        copyButton.addActionListener(new ButtonClickListener());
        decryptButton.addActionListener(new ButtonClickListener());

        //模式按钮栏
        JToggleButton PooledPBEModeButton = new JToggleButton("PooledPBE方式");
        JToggleButton RSAModeButton = new JToggleButton("RSA方式");
        PooledPBEModeButton.setFont(font);
        PooledPBEModeButton.setEnabled(true);
        RSAModeButton.setFont(font);
        //模式按钮事件
        PooledPBEModeButton.setActionCommand("byPooledPBE");
        RSAModeButton.setActionCommand("byRSA");
        PooledPBEModeButton.addActionListener(new ModeToggleListener());
        RSAModeButton.addActionListener(new ModeToggleListener());

        //布置按钮
        controlPanel.add(encryptButton);
        controlPanel.add(copyButton);
        controlPanel.add(decryptButton);
        modePanel.add(PooledPBEModeButton);
        modePanel.add(RSAModeButton);
        mainFrame.setVisible(true);
    }

    /**
     * 执行逻辑
     *
     * @DATE 2023/01/19
     * @CODE Deil
     * @see ActionListener
     */
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            //默认加密盐
            String saltKey = salttextArea.getText() == null || "".equals(salttextArea.getText())? "password" : salttextArea.getText();
            if (command.equals("encrypt")) {
                String text = originalTextArea.getText();
                try {
                    String encrypt = "byPooledPBE".equals(MODE) ? encryptByPooledPBE(saltKey, text) : encryptByRSA(text);
                    ciphertextArea.setText(encrypt);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    ciphertextArea.setText("加密失败");
                }

            } else if (command.equals("decrypt")) {
                String text = ciphertextArea.getText();
                try {
                    String decrypt = "byPooledPBE".equals(MODE) ? decryptByPooledPBE(saltKey, text) : decryptByRSA(text);
                    originalTextArea.setText(decrypt);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    originalTextArea.setText("解密失败");
                }
            } else if (command.equals("copy")) {
                String text = ciphertextArea.getText();
                // 获取系统剪贴板
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                // 封装文本内容
                Transferable trans = new StringSelection("ENC(" + text + ")");
                // 把文本内容设置到系统剪贴板
                clipboard.setContents(trans, null);
            }
        }
    }

    /**
     * 多线程加密
     *
     * @param saltKey
     * @param text
     * @return
     */
    public String encryptByPooledPBE(String saltKey, String text) {
        PooledPBEStringEncryptor encryptor = getPooledPBEStringEncryptor(saltKey);
        return encryptor.encrypt(text);
    }
    /**
     * 多线程解密
     *
     * @param saltKey
     * @param text
     * @return
     */
    public String decryptByPooledPBE(String saltKey, String text) {
        PooledPBEStringEncryptor encryptor = getPooledPBEStringEncryptor(saltKey);
        return encryptor.decrypt(text);
    }
    /**
     * 多线程初始化
     */
    private PooledPBEStringEncryptor getPooledPBEStringEncryptor(String saltKey) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(saltKey);
//        config.setAlgorithm("PBEWithMD5AndDES");//默认配置
//        config.setKeyObtentionIterations("1000");//默认配置
        config.setPoolSize("4");
//        config.setProviderName("SunJCE");//默认配置
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");//默认配置
//        config.setStringOutputType("base64");//默认配置
        encryptor.setConfig(config);
        return encryptor;
    }

    /**
     * 公钥加密
     *
     * @param text
     * @return
     * @throws Exception
     */
    public String encryptByRSA(String text) throws Exception {
        //转换公钥对象
        byte[] buffer = new Base64().decode(getResource("public.pem"));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        //加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        byte[] output = cipher.doFinal(text.getBytes());
        return new String(new Base64().encode(output));
    }
    /**
     * 私钥解密
     *
     * @param text
     * @return
     * @throws Exception
     */
    private String decryptByRSA(String text) throws Exception {
        //转换私钥对象
        byte[] buffer = new Base64().decode(getResource("privatepkcs8.pem"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        // 使用默认RSA
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
        byte[] output = cipher.doFinal(Base64.decodeBase64(text));
        return new String(output);
    }
    /**
     * 获取密钥
     * @throws IOException
     */
    public static String getResource(String resourceFile) throws IOException {
        Resource resource = new ClassPathResource(resourceFile);
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String data = "";
        StringBuilder stringBuilder = new StringBuilder();
        String result = "";
        while((data = br.readLine()) != null) {
            result = stringBuilder.append(data).append("\n").toString();
        }

        br.close();
        isr.close();
        is.close();
        return result;
    }

    /**
     * 切换
     */
    private class ModeToggleListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String saltKey = salttextArea.getText();
            if ("byPooledPBE".equals(command)) {
                salttextArea.setText("");
                ciphertextArea.setText("");
                originalTextArea.setText("");
                MODE = command;
            } else if ("byRSA".equals(command)) {
                salttextArea.setText("RSA加密模式，本处省略。。。");
                ciphertextArea.setText("");
                originalTextArea.setText("");
                MODE = command;
            }
        }
    }

}
