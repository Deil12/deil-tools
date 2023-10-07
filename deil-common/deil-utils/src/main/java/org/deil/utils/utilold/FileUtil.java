package org.deil.utils.utilold;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Slf4j
@UtilityClass
public class FileUtil {

    public static void str2File(String fullName, String fileContent) {
        //Integer result = -1;
        File file = new File(fullName);
        try {
            //创建文件
            file.createNewFile();
            //声明字符输入流
            Writer out = null;
            //子类实例化，表示可追加
            out = new FileWriter(file, true);
            try {
                //写入数据
                out.write(fileContent);
            } catch (IOException e) {
                log.error(e.getMessage());
            } finally {
                //保存数据
                out.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
