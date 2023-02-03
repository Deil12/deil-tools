import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
import java.io.*;
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
    private JTextArea cipherTextArea;

    /*
     * 盐
     */
    private JTextArea saltTextArea;

    /*
     * 按钮
     */
    private JPanel controlPanel;
    private JPanel modePanel;

    public static String MODE = "byPooledPBE";

    public ConfigEncryptTool() {
        //主窗口
        mainFrame = new JFrame("加密解密工具");
        mainFrame.setSize(600, 1000);
        mainFrame.setLayout(new GridLayout(5, 1));
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 18);

        //盐值文本框
        saltTextArea = new JTextArea();
        saltTextArea.setForeground(Color.GREEN);
        saltTextArea.setFont(font);
        saltTextArea.setWrapStyleWord(true);
        saltTextArea.setLineWrap(true);
        saltTextArea.setBorder(BorderFactory.createTitledBorder( " 盐值： " ));
        JScrollPane saltText = new JScrollPane(saltTextArea);
        saltText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //原文文本框
        originalTextArea = new JTextArea();
        originalTextArea.setFont(font);
        originalTextArea.setWrapStyleWord(true);
        originalTextArea.setLineWrap(true);
        originalTextArea.setBorder(BorderFactory.createTitledBorder( " 原文： " ));
        JScrollPane originalText = new JScrollPane(originalTextArea);
        originalText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //密文文本框
        cipherTextArea = new JTextArea();
        cipherTextArea.setFont(font);
        cipherTextArea.setWrapStyleWord(true);
        cipherTextArea.setLineWrap(true);
        cipherTextArea.setBorder(BorderFactory.createTitledBorder( " 密文： " ));
        JScrollPane cipherText = new JScrollPane(cipherTextArea);
        cipherText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //按钮框
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder( " 2nd 工具执行 " ));
        modePanel = new JPanel();
        modePanel.setLayout(new FlowLayout());
        modePanel.setBorder(BorderFactory.createTitledBorder( " 1st 加密方式 " ));
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
        //mainFrame.setBackground(Color.BLACK);
        //mainFrame.getContentPane().setBackground(Color.BLACK);
        //mainFrame.setForeground(Color.WHITE);
    }

    /**
     * 逻辑执行主入口
     *
     * @param args
     * @TIME 2023/01/26
     */
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
        copyButton.setFont(font);
        //按钮事件
        encryptButton.setActionCommand("encrypt");
        copyButton.setActionCommand("copy");
        decryptButton.setActionCommand("decrypt");
        encryptButton.addActionListener(new ButtonClickListener());
        copyButton.addActionListener(new ButtonClickListener());
        decryptButton.addActionListener(new ButtonClickListener());

        //模式按钮栏
        JButton/*JToggleButton*/ PooledPBEModeButton = new JButton/*JToggleButton*/("PooledPBE方式");
        JButton/*JToggleButton*/ RSAModeButton = new JButton/*JToggleButton*/("RSA方式");
        JButton/*JToggleButton*/ Base64ModeButton = new JButton/*JToggleButton*/("Base64方式");
        PooledPBEModeButton.setFont(font);
        PooledPBEModeButton.setEnabled(true);
        RSAModeButton.setFont(font);
        Base64ModeButton.setFont(font);
        //模式按钮事件
        PooledPBEModeButton.setActionCommand("byPooledPBE");
        RSAModeButton.setActionCommand("byRSA");
        Base64ModeButton.setActionCommand("byBase64");
        PooledPBEModeButton.addActionListener(new ModeToggleListener());
        RSAModeButton.addActionListener(new ModeToggleListener());
        Base64ModeButton.addActionListener(new ModeToggleListener());

        //布置按钮
        controlPanel.add(encryptButton);
        controlPanel.add(copyButton);
        controlPanel.add(decryptButton);
        modePanel.add(PooledPBEModeButton);
        modePanel.add(RSAModeButton);
        modePanel.add(Base64ModeButton);
        mainFrame.setVisible(true);
    }

    /**
     * 动作监听执行逻辑
     *
     * @DATE 2023/01/19
     * @CODE Deil
     * @see ActionListener
     */
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            //base64
            if (MODE.equals("byBase64")) {

            }
            //jasypt 默认加密盐、回显
            String saltKey = saltTextArea.getText() == null || "".equals(saltTextArea.getText()) ?
                    "password" : saltTextArea.getText();
            saltTextArea.setText(saltKey);
            if (command.equals("encrypt")) {
                String text = originalTextArea.getText();
                try {
                    String encrypt = MODE.equals("byPooledPBE") ?
                            encryptByPooledPBE(saltKey, text) : MODE.equals("byRSA") ? encryptByRSA(text) : encryptByBase64(text);
                    cipherTextArea.setText(encrypt);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    cipherTextArea.setText("加密失败");
                }
            } else if (command.equals("decrypt")) {
                String text = cipherTextArea.getText();
                try {
                    String decrypt = MODE.equals("byPooledPBE") ?
                            decryptByPooledPBE(saltKey, text) : MODE.equals("byRSA") ? decryptByRSA(text) : decryptByBase64(text);
                    originalTextArea.setText(decrypt);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    originalTextArea.setText("解密失败");
                }
            } else if (command.equals("copy")) {
                String text = cipherTextArea.getText();
                // 获取系统剪贴板
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                // 封装文本内容
                Transferable trans = MODE.equals("byPooledPBE") || MODE.equals("byPooledPBE") ?
                        new StringSelection("ENC(" + text + ")") : new StringSelection(text);
                // 把文本内容设置到系统剪贴板
                clipboard.setContents(trans, null);
            }
        }
    }

    /**
     * <内部类>执行切换监听</内部类>
     *
     * @DATE 2023/01/20
     * @CODE Deil
     * @see ActionListener
     */
    private class ModeToggleListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if ("byPooledPBE".equals(command)) {
                saltTextArea.setEnabled(true);
                saltTextArea.setText("");
                saltTextArea.setForeground(Color.GREEN);
                saltTextArea.setBackground(Color.WHITE);
                cipherTextArea.setText("");
                originalTextArea.setText("");
                MODE = command;
            } else if ("byRSA".equals(command)) {
                saltTextArea.setEnabled(false);
                saltTextArea.setText("RSA方式仅维护密钥文件，加密盐忽略。。。");
                saltTextArea.setBackground(Color.BLACK);
                cipherTextArea.setText("");
                originalTextArea.setText("");
                MODE = command;
            } else if ("byBase64".equals(command)) {
                saltTextArea.setEnabled(false);
                saltTextArea.setText("BASE64方式无需加密盐。。。");
                saltTextArea.setBackground(Color.BLACK);
                cipherTextArea.setText("");
                originalTextArea.setText("");
                MODE = command;
            }
        }
    }

    /*************************************************************************************************************
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
        //config.setAlgorithm("PBEWithMD5AndDES");//默认配置
        //config.setKeyObtentionIterations("1000");//默认配置
        config.setPoolSize("4");
        //config.setProviderName("SunJCE");//默认配置
        //config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");//默认配置
        //config.setStringOutputType("base64");//默认配置
        encryptor.setConfig(config);
        return encryptor;
    }

    /*************************************************************************************************************
     * 公钥加密
     *
     * @param text
     * @return
     * @throws Exception
     */
    public String encryptByRSA(String text) throws Exception {
        //转换公钥对象
        byte[] buffer = new org.apache.commons.codec.binary.Base64().decode(getResource("public.pem"));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        //加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        byte[] output = cipher.doFinal(text.getBytes());
        return new String(new org.apache.commons.codec.binary.Base64().encode(output));
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
        byte[] buffer = new org.apache.commons.codec.binary.Base64().decode(getResource("privatepkcs8.pem"));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        // 使用默认RSA
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
        byte[] output = cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(text));
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

    /*************************************************************************************************************
     * BASE64加密
     *
     * @param encryptText 加密文本
     * @return {@link String }
     * @TIME 2023/01/31
     */
    public static String encryptByBase64(String encryptText) {
        return new BASE64Encoder().encode(encryptText.getBytes());
    }

    /**
     * BASE64解密
     *
     * @param decryptText 解密文本
     * @return {@link String }
     * @TIME 2023/01/31
     */
    @SneakyThrows
    public static String decryptByBase64(String decryptText) {
        return new String(new BASE64Decoder().decodeBuffer(decryptText));
    }

    /**
     * BASE64 工具类
     * {@link BASE64Encoder}< encode() >/< decodeBuffer() >或将启用
     *
     * @DATE 2023/01/31
     * @CODE Deil
     */
    class Base64 {

        //region init
        private char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', '+', '/' };

        private byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1,
                -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1,
                -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1,
                -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
                37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
                -1, -1, -1, -1, -1 };

        private Base64() {}
        //endregion

        //region forByte
        public String encode(byte[] data) {
            int len = data.length;
            int i = 0;
            int b1, b2, b3;
            StringBuilder sb = new StringBuilder(len);

            while (i < len) {
                b1 = data[i++] & 0xff;
                if (i == len) {
                    sb.append(base64EncodeChars[b1 >>> 2]);
                    sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                    sb.append("==");
                    break;
                }
                b2 = data[i++] & 0xff;
                if (i == len) {
                    sb.append(base64EncodeChars[b1 >>> 2]);
                    sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                    sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                    sb.append("=");
                    break;
                }
                b3 = data[i++] & 0xff;
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
                sb.append(base64EncodeChars[b3 & 0x3f]);
            }
            return sb.toString();
        }

        public byte[] decode(String str) {
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            byte[] data = str.getBytes();
            int len = data.length;
            ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
            int i = 0;
            int b1, b2, b3, b4;

            while (i < len) {

                /* b1 */
                do {
                    b1 = base64DecodeChars[data[i++]];
                } while (i < len && b1 == -1);
                if (b1 == -1) {
                    break;
                }

                /* b2 */
                do {
                    b2 = base64DecodeChars[data[i++]];
                } while (i < len && b2 == -1);
                if (b2 == -1) {
                    break;
                }
                buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

                /* b3 */
                do {
                    b3 = data[i++];
                    if (b3 == 61) {
                        return buf.toByteArray();
                    }
                    b3 = base64DecodeChars[b3];
                } while (i < len && b3 == -1);
                if (b3 == -1) {
                    break;
                }
                buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

                /* b4 */
                do {
                    b4 = data[i++];
                    if (b4 == 61) {
                        return buf.toByteArray();
                    }
                    b4 = base64DecodeChars[b4];
                } while (i < len && b4 == -1);
                if (b4 == -1) {
                    break;
                }
                buf.write((int) (((b3 & 0x03) << 6) | b4));
            }
            return buf.toByteArray();
        }
        //endregion

        //region forString
        public String encodeStr(String str, String charset) throws UnsupportedEncodingException {
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            return encode(str.getBytes(charset));
        }

        public String decodeStr(String str, String charset) throws UnsupportedEncodingException {
            if (StringUtils.isEmpty(str)) {
                return null;
            }
            byte[] res = decode(str);
            return new String(res);
        }
        //endregion

    }
}
