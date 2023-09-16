package org.deil.utils.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * 注入防御工具
 *
 * @DATE 2023/03/10
 * @CODE Deil
 */
@Slf4j
@UtilityClass
public class InjectDefenseUtil {

    //region XSS注入防御
    public static String stripXSSParam(String param) {
        if (param != null) {
            // By ESAPPI
            // param = ESAPI.encoder().canonicalize(param);

            // Avoid null characters
            param = param.replaceAll("", "");

            // Avoid anything between script tags
            param = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE).matcher(param).replaceAll("");

            // Avoid anything in a src="..." type of e­xpression
            param = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");
            param = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");

            // Remove any lonesome </script> tag
            param = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE).matcher(param).replaceAll("");

            // Remove any lonesome <script ...> tag
            param = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");

            // Avoid eval(...) e­xpressions
            param = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");

            // Avoid e­xpression(...) e­xpressions
            param = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");

            // Avoid javascript:... e­xpressions
            param = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE).matcher(param).replaceAll("");

            // Avoid vbscript:... e­xpressions
            param = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE).matcher(param).replaceAll("");

            // Avoid onload= e­xpressions
            param = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");
        }
        return param;
    }
    //endregion

    //region 路径注入防御
    public static String stripPathParam(String param) {
        if (param != null) {
            param = param.replaceAll("", "");

            param = Pattern.compile("[\\\\/| :*?\"<>]", Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");

            param = Pattern.compile("../", Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");

            param = Pattern.compile("./", Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");

            param = Pattern.compile("~/", Pattern.MULTILINE | Pattern.DOTALL).matcher(param).replaceAll("");
        }
        return param;
    }
    //endregion

    //region XML注入防御
    public static String stripXmlParam(String param) {

        return param;
    }
    //endregion

}
