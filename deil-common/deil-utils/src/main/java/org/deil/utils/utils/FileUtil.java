package org.deil.utils.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@UtilityClass
public class FileUtil {

    private Logger log = LoggerFactory.getLogger(FileUtil.class);

    //region 文件操作
    /**
     * 校验文件或目录是否存在
     *
     * @param filePath
     * @return boolean
     * @time 2023/04/21
     * @since 1.0.0
     */
    public static boolean exists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * Writer方式新建文件
     *
     * @param filePath
     * @param fileContent
     * @time 2023/04/21
     * @since 1.0.0
     */
    public static void createByWriter(String filePath, String fileContent) throws IOException {
        //Integer result = -1;
        File file = new File(filePath);
        if (!file.exists()) {
            new File(file.getParent()).mkdirs();
        }
        file.createNewFile();
        Writer out = null;
        //子类实例化，表示可追加
        out = new FileWriter(file, true);
        try {
            out.write(fileContent);
        } catch (IOException e) {
            throw e;
        } finally {
            out.close();
        }
    }

    /**
     * FileInputStream
     *
     * @param path 路径
     * @return {@link String }
     * @throws IOException ioexception
     * @time 2023/03/14
     */
    @Deprecated
    public static String readByFileInputStream(String path) throws IOException {
        String fileStr =
            IOUtils.toString(
                    new FileInputStream(ClassLoader.getSystemResource(path).getPath()),
                    "UTF-8");
        return fileStr;
    }

    /**
     * 使用 getResourceAsStream()，不需要实际路径
     *
     * @param path 路径
     * @return {@link String }
     * @throws IOException ioexception
     * @time 2023/03/14
     */
    @Deprecated
    public static String readByGetClassLoader(String path) throws IOException {
        String fileStr =
                IOUtils.toString(
                        FileUtil.class.getClassLoader().getResourceAsStream(path),
                        "UTF-8");
        return fileStr;
    }

    /**
     * 使用 getResourceAsStream() 直接从resources跟路径下获取，不需要实际路径
     *
     * @param path 路径
     * @return {@link String }
     * @throws IOException ioexception
     * @time 2023/03/14
     */
    @Deprecated
    public static String readByGetResourceAsStream(String path) throws IOException {
        String fileStr =
            IOUtils.toString(
                    FileUtil.class.getResourceAsStream("/" + path),
                    "UTF-8");
        return fileStr;
    }

    /**
     * 通过 ClassPathResource 获取文件流，不需要实际路径
     *
     * @param path 路径
     * @return {@link String }
     * @throws IOException ioexception
     * @time 2023/03/14
     */
    @Deprecated
    public static String readByClassPathResource(String path) throws IOException {
        String fileStr =
            IOUtils.toString(
                    new ClassPathResource(path).getInputStream(),
                    "UTF-8");
        return fileStr;
    }

    /**
     * Reader方式获取文件内容
     *
     * @param filePath
     * @return {@link String }
     * @time 2023/04/21
     * @since 1.0.0
     */
    public static String readByReader(String filePath) throws IOException {
        String xml = null;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            StringBuffer sb = new StringBuffer();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            xml = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xml;
    }

    /**
     * Byte方式获取文件内容
     *
     * @param filePath
     * @return {@link String }
     * @throws Exception
     * @time 2023/07/06
     * @since 1.0.0
     */
    public static String readByByte(String filePath) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        //String result = new String(bytes, Charset.defaultCharset());
        String result = new String(bytes, EncodeUtil.getEncode(new File(filePath), true));
        return result;
    }

    /**
     * 重命名(目录不一致则重命名并移动)
     *
     * @param filePathOld
     * @time 2023/04/21
     * @since 1.2.0
     */
    public static void rename(String filePathOld, String filePathNew) {
        File fileOld = new File(filePathOld);
        File fileNew = new File(filePathNew);
        if (!fileOld.exists()) {
            log.error("文件工具>目标文件不存在{}", filePathOld);
        }
        File pathNew = new File(fileNew.getParent());
        if (!pathNew.exists()) {
            pathNew.mkdirs();
        }
        fileOld.renameTo(fileNew);
        log.info("文件工具>目标文件重命名成功{}", filePathNew);
    }

    /**
     * 删除指定文件或目录
     *
     * @param filePath
     * @time 2023/04/21
     * @since 1.2.0
     */
    public static void remove(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.deleteOnExit();
            log.info("文件工具>目标文件移除成功{}", filePath);
        }
        log.error("文件工具>目标文件不存在{}", filePath);
    }
    //endregion

    //region 唐翼
    /**
     * <IO 创建文件/>
     *
     * @param filePath
     * @param fileName
     * @param fileContent
     * @throws IOException
     * @time 2023/04/12
     * @since 1.0.0
     */
    public void creatWithTimeName(String filePath, String fileName, String fileContent) throws IOException {
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();// 多级目录
        }
        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            fileName = String.format("%s(%s)%s", fileName.substring(0, index), DateTimeUtil.now(DateTimeFormat.LONG_DATETIME_UNSIGNED.value), fileName.substring(index));
        } else {
            fileName = String.format("%s(%s)", fileName, DateTimeUtil.now(DateTimeFormat.LONG_DATETIME_UNSIGNED.value));
        }
        File checkFile = new File(new StringBuilder().append(filePath).append("/").append(fileName).toString());
        FileWriter writer = null;
        try {
            if (!checkFile.exists()) {
                checkFile.createNewFile();// 创建目标文件
            }
            // append为true时为追加模式，false或缺省则为覆盖模式
            writer = new FileWriter(checkFile, true);
            writer.append(fileContent);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    /**
     * 转关运抵报告回执文件校验
     *
     * @param path
     * @param clientSeqNo
     * @return boolean
     * @time 2023/04/21
     * @since 1.2.0
     */
    public static boolean existsEtaIResponse(String path, String clientSeqNo) {
        return new File(path + "/" + clientSeqNo + "/" + clientSeqNo + ".xml").exists();
    }

    /**
     * 转关运抵报告文件校验
     *
     * @param path
     * @param billNo
     * @param packNo
     * @return boolean
     * @time 2023/04/21
     * @since 1.2.0
     */
    public static boolean existsEtaIMessage(String path, String clientSeqNo, String billNo, String packNo) {
        return new File(path + "/" + clientSeqNo + "/" + billNo + "/" + packNo + ".xml").exists();
    }
    //endregion

}
