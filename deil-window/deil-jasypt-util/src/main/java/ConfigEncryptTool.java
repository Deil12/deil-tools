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
import java.io.*;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @PURPOSE 配置类加密工具
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
public class ConfigEncryptTool {

    /*主窗口*/
    private JFrame mainFrame;
    /*原文*/
    private JTextArea originalTextArea;
    /*密文*/
    private JTextArea cipherTextArea;
    /*盐*/
    private JTextArea saltTextArea;
    /*按钮*/
    private JPanel controlPanel;
    private JPanel modePanel;

    public static String MODE = "byPooledPBE";
    public static String RESOURCE = "";

    public ConfigEncryptTool() {
        //主窗口
        mainFrame = new JFrame("加密工具");
        mainFrame.setSize(400, 1000);
        mainFrame.setLayout(new GridLayout(5, 1));
        mainFrame.setAlwaysOnTop(true);
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
        JButton copyButton = new JButton("复制密文");
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
        JButton PooledPBEModeButton = new JButton("启动参数方式");
        JButton RSAModeButton = new JButton("密钥文件方式");
        JButton Base64ModeButton = new JButton("Base64加密");
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
                            "ENC(" + encryptByPooledPBE(saltKey, text) + ")" : MODE.equals("byRSA") ? "ENC(" + encryptByRSA(text) + ")" : encryptByBase64(text);
                    cipherTextArea.setText(encrypt);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    cipherTextArea.setText("加密失败");
                }
            } else if (command.equals("decrypt")) {
                String text = cipherTextArea.getText().startsWith("ENC(") && cipherTextArea.getText().endsWith(")") ?
                        cipherTextArea.getText().substring(4, cipherTextArea.getText().length() - 1) :
                        cipherTextArea.getText();
                try {
                    String decrypt = MODE.equals("byPooledPBE") ?
                            decryptByPooledPBE(saltKey, text) : MODE.equals("byRSA") ? decryptByRSA(text) : decryptByBase64(text);
                    RESOURCE = "";
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
                Transferable trans = new StringSelection(text);
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
                saltTextArea.setText("RSA方式请选择公钥加密、私钥解密，否则默认");
                saltTextArea.setBackground(Color.BLACK);
                cipherTextArea.setText("");
                originalTextArea.setText("");
                MODE = command;
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showOpenDialog(jFileChooser);
                if (jFileChooser.getSelectedFile() != null) {
                    try {
                        File file = new File(jFileChooser.getSelectedFile().getPath());
                        FileInputStream fileInputStream = new FileInputStream(file);
                        byte[] bytes = new byte[(int) file.length()];
                        fileInputStream.read(bytes);
                        RESOURCE
                                = new String(bytes, "UTF-8");
                        saltTextArea.setText(RESOURCE);
                        fileInputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            } else if ("byBase64".equals(command)) {
                saltTextArea.setEnabled(false);
                saltTextArea.setText("无需加密盐或密钥。。。");
                saltTextArea.setBackground(Color.BLACK);
                cipherTextArea.setText("");
                originalTextArea.setText("");
                MODE = command;
            }
        }
    }

    //region PooledPBE线程加密
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
        //config.setAlgorithm("PBEWithMD5AndDES");//默认配置
        //config.setKeyObtentionIterations("1000");//默认配置
        config.setPoolSize("4");
        //config.setProviderName("SunJCE");//默认配置
        //config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");//默认配置
        //config.setStringOutputType("base64");//默认配置
        encryptor.setConfig(config);
        return encryptor;
    }
    //endregion

    //region 非对称密钥加密
    /**
     * 公钥加密
     *
     * @param text
     * @return
     * @throws Exception
     */
    public String encryptByRSA(String text) throws Exception {
        //转换公钥对象
        byte[] buffer = new org.apache.commons.codec.binary.Base64().decode(
                RESOURCE.equals("") ? getResource("public.pem") : RESOURCE
        );
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
        byte[] buffer = new org.apache.commons.codec.binary.Base64().decode(
                RESOURCE.equals("") ? getResource("privatepkcs8.pem") : RESOURCE
        );
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
    //endregion

    //region Base64对称加密
    /**
     * BASE64加密
     *
     * @param encryptText 加密文本
     * @return {@link String }
     * @TIME 2023/01/31
     */
    public static String encryptByBase64(String encryptText) {
        //return new BASE64Encoder().encode(encryptText.getBytes());
        return new String(Base64.getEncoder().encode(encryptText.getBytes()));
    }

    /**
     * BASE64解密
     *
     * @param decryptText 解密文本
     * @return {@link String }
     * @TIME 2023/01/31
     */
    public static String decryptByBase64(String decryptText) throws IOException {
        //return new String(new BASE64Decoder().decodeBuffer(decryptText));
        return new String(Base64.getDecoder().decode(decryptText));
    }
    //endregion
}
