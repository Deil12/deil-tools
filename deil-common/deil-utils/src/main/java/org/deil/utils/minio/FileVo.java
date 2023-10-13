package org.deil.utils.minio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileVo {

    /**
     * 原文件名
     */
    private String oldFileName;

    /**
     * 新文件名
     */
    private String newFileName;

    /**
     * 文件路径
     */
    private String fileUrl;

}
