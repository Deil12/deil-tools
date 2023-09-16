package org.deil.gatewaynode.filter;

import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * XSS 请求包装
 *
 * @DATE 2023/01/28
 * @CODE Deil
 * @see HttpServletRequestWrapper
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {
    private static Logger log = LoggerFactory.getLogger(XssRequestWrapper.class);

    private static final String ENCODING = "UTF-8";

    /**
     * 策略文件：需要将要使用的策略文件放到项目资源文件路径
     * 1、<p>antisamy.xml</p>
     * 默认规则，允许大部分 HTML 标记，不允许 JavaScript 标记出现。
     * <p>
     * 2、<p>antisamy-anythinggoes.xml</p>
     * 如果想允许每一个有效的 HTML 和 CSS 元素(但是不允许 JavaScript 或明显的 CSS 相关的钓鱼攻击)，你可以使用这个策略文件。
     * 它包含每个元素的基本规则，所以在使用定制其他策略文件时，可以将它用作知识库。
     * <p>
     * 3、<p>antisamy-ebay.xml</p>
     * eBay 是世界上最受欢迎的在线拍卖网站，它是一个公共站点，因此任何人都可以发布包含丰富 HTML 内容的清单。
     * 考虑到 eBay 作为一个有吸引力的目标，它受到一些复杂的 XSS 攻击并不奇怪。清单被允许包含比 Slashdot 更丰富的内容——所以它的攻击面相当大。
     * 因此，antisamy-ebay.xml 策略文件提供的策略是支持丰富的 HTML 标记，但是不支持 CSS 标记 和 JavaScript 标记。
     * <p>
     * 4、<p>antisamy-myspace.xml</p>
     * MySpace 是一个曾经非常受欢迎的社交网站，用户可以提交几乎所有他们想要的 HTML 和CSS ——只要不包含 JavaScript.
     * MySpace 使用一个单词黑名单来验证用户的 HTML，这就是为什么他们会受到臭名昭著的 Samy 蠕虫的攻击。
     * Samy 蠕虫使用碎片攻击和一个应该被列入黑名单的词(eval)——是这个项目的灵感来源。
     * 因此，antisamy-myspace.xml 策略文件提供的策略是支持非常丰富的 HTML 和 CSS 标记，但是不支持 JavaScript 标记。
     * <p>
     * 5、<p>antisamy-slashdot.xml</p>
     * Slashdot 是一个技术新闻网站，它允许用户匿名回复非常有限的 HTML 标记的新闻帖子。
     * 现在，Slashdot 不仅是最酷的网站之一，它也是一个受到许多不同成功攻击的网站。
     * Slashdot 的规则相当严格：用户只能提交以下 <b>, <u>, <i>, <a>, <blockquote> 这些 HTML 标记，不能提交 CSS.
     * 因此，antisamy-slashdot.xml 文件支持类似的功能，所有直接对字体、颜色或重点进行操作的文本格式标记都是允许的，但是不允许 CSS 和 JavaScript 标记出现。
     * <p>
     * 6、<p>antisamy-tinymce.xml</p>
     * 只允许文本格式通过，相对比较安全。
     *
     * @see org.owasp.validator
     */
    public static String ANTISAMY_XML =
            //"antisamy.xml"
            //"antisamy-anythinggoes.xml"
            "antisamy-ebay.xml"
            //"antisamy-myspace.xml"
            //"antisamy-slashdot.xml"
            //"antisamy-tinymce.xml"
    ;

    public static Policy POLICY = null;

    public static final ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

    static {
        try {
            POLICY = Policy.getInstance(
                    Objects.requireNonNull(patternResolver.getResource(ANTISAMY_XML).getInputStream())
            );
        } catch (PolicyException | IOException e) {
            //e.printStackTrace();
            log.error("XssRequestWrapper.static:{}", e.getMessage());
        }
    }

    public XssRequestWrapper(
            HttpServletRequest request
    ) {
        super(request);
    }

    //region 按提交数据清理
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int len = values.length;
        for (int j = 0; j < len; j++) {
            String temp = values[j];
            //过滤清理
            values[j] = xssClean(values[j]);
            if (!temp.equals(values[j])) {
                log.info("Antisamy 过滤清理, 过滤前:{}, 过滤后:{}", temp, values[j]);
            }
        }
        return values;
    }

    @Override
    public String getParameter(String paramString) {
        String str = super.getParameter(paramString);
        if (str == null) {
            return null;
        }
        //过滤清理
        String newStr = xssClean(str);
        if (!str.equals(newStr)) {
            log.info("Antisamy 过滤清理, 过滤前:{}, 过滤后:{}", str, newStr);
        }
        return newStr;
    }

    @Override
    public String getHeader(String paramString) {
        String str = super.getHeader(paramString);
        if (str == null) {
            return null;
        }
        //过滤清理
        String newStr = xssClean(str);
        if (!str.equals(newStr)) {
            log.info("Antisamy 过滤清理, 过滤前:{}, 过滤后:{}", str, newStr);
        }
        return newStr;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> requestMap = super.getParameterMap();
        for (Map.Entry<String, String[]> me : requestMap.entrySet()) {
            String[] values = me.getValue();
            for (int i = 0; i < values.length; i++) {
                String temp = values[i];
                //过滤清理
                values[i] = xssClean(temp);
                if (!temp.equals(values[i])) {
                    log.info("Antisamy 过滤清理, 过滤前:{}, 过滤后:{}", temp, values[i]);
                }
            }
        }
        return requestMap;
    }
    //endregion

    /**
     * Antisamy 过滤数据
     *
     * @param taintedHTML 需要进行过滤的数据
     * @return {@link String }
     * @TIME 2023/01/28
     */
    private String xssClean(String taintedHTML) {
        try {
            //使用AntiSamy 进行过滤
            AntiSamy antiSamy = new AntiSamy();
            CleanResults cr = antiSamy.scan(taintedHTML, POLICY);
            taintedHTML = cr.getCleanHTML();
        } catch (ScanException e) {
            //e.printStackTrace();
            log.error("未扫描到AntiSamy策略");
        } catch (PolicyException e) {
            //e.printStackTrace();
            log.error("未加载AntiSamy策略");
        }
        return taintedHTML;
    }

}
