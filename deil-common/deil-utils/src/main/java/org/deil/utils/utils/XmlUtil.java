package org.deil.utils.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.experimental.UtilityClass;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.*;

@UtilityClass
public class XmlUtil {

    private static Logger log = LoggerFactory.getLogger(XmlUtil.class);

    //region xml <-> file
    /**
     * <DOM stream 创建xml/>
     *
     * @param fullFileName 完整文件名称
     * @param document     文档
     * @time 2023/04/12
     * @since 1.0.0
     */
    public synchronized static void createXml(String fullFileName, org.w3c.dom.Document document) {
        try {
            // 判断文件是否存在，如存在就删掉它
            File file = new File(fullFileName);
            if (file.exists()) {
                file.delete();
            }
            //多级目录
            new File(fullFileName.substring(0, fullFileName.lastIndexOf("/"))).mkdirs();
            //重建目标文件
            file.createNewFile();
            /** 将document中的内容写入文件中 */
            TransformerFactory tFactory = TransformerFactory.newInstance();
            //XXE漏洞：禁用外部实体
            tFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            tFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            tFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(fullFileName));
            transformer.transform(source, result);
        } catch (final Exception exception) {
            log.info("更新{}出错：{}", fullFileName, exception);
        }
    }
    //endregion

    //region xml -> json
    /**
     * xml转json
     *
     * @param xml xml格式字串
     * @return JSONObject
     */
    public static JSONObject xml2Json(String xml) {
        JSONObject parent = new JSONObject();
        JSONObject json = new JSONObject();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            dom4j2Json(doc.getRootElement(), null, json);
        }
        parent.put(doc.getRootElement().getName(), json);
        return parent;
    }

    /**
     * xml转json
     *
     * @param element 解析的元素
     * @param json    待填充的json
     */
    private static void dom4j2Json(Element element, JSONObject parent, JSONObject json) {
        if (parent == null) {
            parent = new JSONObject();
        }
        log.debug("name5:" + element.getName() + ",value:" + json);
        parent.put(element.getName(), json);
        // 如果是属性
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute) o;
            if (isEmpty(attr.getValue())) {
                parent.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();
        if (chdEl.isEmpty() && isEmpty(element.getText())) {// 如果没有子元素,只有一个值
            log.debug("name1:" + element.getName() + ",value:" + element.getText());
            parent.put(element.getName(), element.getText());
        } else {
            for (Element e : chdEl) {// 有子元素
                if (!e.elements().isEmpty()) {// 子元素也有子元素
                    JSONObject chdJson = new JSONObject();
                    dom4j2Json(e, json, chdJson);
                    Object o = parent.get(e.getName());
                    if (o != null) {
                        JSONArray jsona = null;
                        if (o instanceof JSONObject) {// 如果此元素已存在,则转为jsonArray
                            JSONObject jsono = (JSONObject) o;
                            json.remove(e.getName());
                            jsona = new JSONArray();
                            jsona.add(jsono);
                            jsona.add(chdJson);
                        } else if (o instanceof JSONArray) {
                            jsona = (JSONArray) o;
                            jsona.add(chdJson);
                        }
                        log.debug("name2:" + e.getName() + ",value:" +jsona);
                        json.put(e.getName(), jsona);
                    } else {
                        if(!chdJson.isEmpty()){
                            log.debug("name3:" + e.getName() + ",value:" + chdJson);
                            json.put(e.getName(), chdJson);
                        }
                    }
                } else {// 子元素没有子元素
                    for (Object o : element.attributes()) {
                        Attribute attr = (Attribute) o;
                        if (isEmpty(attr.getValue())) {
                            json.put("@" + attr.getName(), attr.getValue());
                        }
                    }
                    if (!e.getText().isEmpty()) {
                        log.debug("name4:" + e.getName() + ",value:" +e.getText());
                        json.put(e.getName(), e.getText());
                    }
                }
            }
        }
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return false;
        }
        return true;
    }

    /*
    public static JSONObject xml2Json(String xmlStr) throws JDOMException, IOException {
        if (StringUtils.isEmpty(xmlStr))
            return null;
        xmlStr = xmlStr.replaceAll("\\\n", "");
        byte[] xml = xmlStr.getBytes("UTF-8");
        JSONObject json = new JSONObject();
        InputStream is = new ByteArrayInputStream(xml);
        SAXBuilder sb = new SAXBuilder();
        org.jdom2.Document doc = sb.build(is);
        org.jdom2.Element root = doc.getRootElement();
        json.put(root.getName(), iterate2Json(root));
        return json;
    }

    private static JSONObject iterate2Json(org.jdom2.Element element) {
        List<org.jdom2.Element> node = element.getChildren();
        JSONObject obj = new JSONObject();
        List list = null;
        for (Element child : node) {
            list = new LinkedList();
            String text = child.getTextTrim();
            if (StringUtil.isBlank(text)) {
                if (child.getChildren().size() == 0) {
                    continue;
                }
                if (obj.containsKey(child.getName())) {
                    list = (List) obj.get(child.getName());
                }
                list.add(iterate2Json(child)); //遍历child的子节点
                obj.put(child.getName(), list);
            } else {
                if (obj.containsKey(child.getName())) {
                    Object value = obj.get(child.getName());
                    try {
                        list = (List) value;
                    } catch (ClassCastException e) {
                        list.add(value);
                    }
                }
                if (child.getChildren().size() == 0) { //child无子节点时直接设置text
                    obj.put(child.getName(), text);
                } else {
                    list.add(text);
                    obj.put(child.getName(), list);
                }
            }
        }
        return obj;
    }
    */
    //endregion

    //region xml -> form
    /**
     * @date 2022/7/12, 下午2:41 : TODO 普通字符串转 xml字符串
     * @param xmlStr
     * @return java.lang.String
     */
    public static String xmlStr2Form(String xmlStr) {
        try {
            String s = "";
            /*Guid guid = new Guid();
            String path = @".\" + guid.ToString(); //当前目录下
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(strXml);
            doc.Save(path);
            using (StreamReader sr = new StreamReader(path, System.Text.Encoding.UTF8)) {
                s = sr.ReadToEnd();
            }
            File.Delete(path);*/
            return s;
        } catch (Exception ex) {
            return xmlStr;
            //ErrLogManager elog = new ErrLogManager();
            //elog.WriteInfoLine("ConvertToXmlForm error " + ex.Message);
        }
    }
    //endregion

    //region xml -> bean
    /**
     * xml转换bean
     *
     * @param clazz
     * @param xml
     * @return {@link Object }
     * @time 2023/04/11
     * @since 1.2.0
     */
    public static <T> T generateBean(String xml, Class<?> clazz) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshal = context.createUnmarshaller();
            StringReader reader = new StringReader(xml);

            //方案一
            //xmlObject = unmarshal.unmarshal(reader);

            //方案二 (忽略命名空间)
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            Source source = new SAXSource(xmlReader, new InputSource(reader));
            xmlObject = unmarshal.unmarshal(source);

            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return (T) xmlObject;
    }

    /**
     * @param xml
     * @param type
     * @return {@link T }
     * @time 2023/04/20
     * @since 1.2.0
     */
    @Deprecated
    public static <T> T xmlStr2BeanWithoutAttribute(String xml, Class<T> type) {
        return JAXB.unmarshal(new StringReader(xml), type);
    }

    /**
     * @param xml
     * @param type
     * @return {@link T }
     * @time 2023/04/20
     * @since 1.2.0
     */
    public static <T> T xmlStr2BeanIgnoreAttribute(String xml, Class<T> type) throws Exception {
        Unmarshaller unmarshaller = null;
        Source source = null;
        try {
            JAXBContext context = JAXBContext.newInstance(type);
            unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            SAXParserFactory sax = SAXParserFactory.newInstance();
            sax.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            XMLReader xmlReader = sax.newSAXParser().getXMLReader();
            source = new SAXSource(xmlReader, new InputSource(reader));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return (T) unmarshaller.unmarshal(source);
    }

    /**
     * @param clazz
     * @param filePath
     * @return {@link Object }
     * @time 2023/04/22
     * @since 1.0.0
     */
    @Deprecated
    public static Object filePath2Obj(Class<?> clazz, String filePath) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            //方案1
            /*//InputStreamReader isr=new InputStreamReader(new FileInputStream(filePath),"UTF-8");*/

            //方案2
            DocumentBuilderFactory instance = DocumentBuilderFactory.newInstance();
            // 1。禁用DTD（doctypes），这样可以阻止几乎所有的XML实体攻击
            instance.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
            /*//FIXME 2。1无法完全禁用时设置：禁用外部通用实体和参数实体（根据实际使用的jar包）
            instance.setFeature("http://xml.org/sax/features/external-general-entities",false);
            instance.setFeature("http://xml.org/sax/features/external-parameter-entities", false);*/
            /*//FIXME 2。2禁用外部DTD
            instance.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",false);*/
            /*//FIXME 2。3设置 XInclude 处理的状态为false,禁止实体扩展引用
            instance.setXIncludeAware(false);
            instance.setExpandEntityReferences(false);*/
            DocumentBuilder builder = instance.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(FileUtil.readByByte(filePath))));

            xmlObject = unmarshaller.unmarshal(document);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return xmlObject;
    }
    //endregion

    //region xml -> map
    /**
     * @date 2022/6/30, 上午11:26 : 自定义转 Map 迭代器 + 策略
     */
    /*
    @SuppressWarnings("unchecked")
    public static <T> T interaterToMap(String filePath, Class<?> clazz) {
        //迭代器，保存 content对象
        Map elementMap = new LinkedHashMap();
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(filePath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //根结点
        Element rootElement = document.getRootElement();
        elementMap.put("ROOTELEMENT", rootElement.getName());
        //命名空间
        Iterator<Attribute> attributeIterator = rootElement.attributeIterator();
        //while (attributeIterator.hasNext()) {
        //    Attribute next = attributeIterator.next();
        //    elementMap.put(next.getName() != null ? next.getName() : "NAMESPACE", next.getValue());
        //}
        Namespace namespace = rootElement.getNamespace();
        elementMap.put(namespace.getName() != null ? namespace.getName() : "NAMESPACE", namespace.getText());
        //子节点
        Iterator<Element> iterator = rootElement.elementIterator();
        while (iterator.hasNext()) {
            Element childElement = (Element) iterator.next();
            if (!childElement.isRootElement()) {
                if (elementMap.get("HEADELEMENT") == null) {
                    elementMap.put("HEADELEMENT", childElement.getName());
                } else {
                    if (elementMap.get("BODYELEMENT")==null)
                        elementMap.put("BODYELEMENT", childElement.getName());
                }
            }
            elementMap.put(childElement.getName(), childElement.getStringValue());
            Iterator<Element> childIterator = childElement.elementIterator();
            while (childIterator.hasNext()) {
                Element element = (Element) childIterator.next();
                elementMap.put(element.getName(), element.getStringValue());
            }
        }
        if (clazz != null) {
            XStream xstream = new XStream(new MyXppDriver(false));
            xstream.autodetectAnnotations(true);
            xstream.processAnnotations(clazz);
        }
        return (T) elementMap;
    }

    public static class MyXppDriver extends XppDriver {
        boolean useCDATA = false;

        MyXppDriver(boolean useCDATA) {
            super(new XmlFriendlyNameCoder("__", "_"));
            this.useCDATA = useCDATA;
        }

        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            if (!useCDATA) {
                return super.createWriter(out);
            }
            return new PrettyPrintWriter(out) {
                boolean cdata = true;

                @Override
                public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write(cDATA(text));
                    } else {
                        writer.write(text);
                    }
                }

                private String cDATA(String text) {
                    return "<![CDATA[" + text + "]]>";
                }
            };
        }
    }
    */

    /**
     * @date 2022/7/6, 上午11:23 : filePath 获取 Map
     * @param filePath
     * @return java.util.Map
     */
    /*public static Map filePath2Map(String filePath) throws Exception {
        String xmlStr = FileUtil.readByByte(filePath);
        return xmlStr2Map(xmlStr);
    }*/

    /**
     * @date 2022/7/6, 上午11:29 : xmlStr 获取 Map
     * @param xmlStr
     * @return java.util.Map
     */
    /*public static Map xmlStr2Map(String xmlStr) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        Map xmlMap = xmlMapper.readValue(xmlStr, Map.class);
        return xmlMap;
    }*/
    //endregion

    //region xml -> doc
    /**
     * @date 2022/7/11, 下午4:08 : filePath 获取 Document
     * @param filePath
     * @return org.dom4j.Document
     */
    public static Document filePath2Dom4jDoc(String filePath) throws Exception {
        String xmlStr = FileUtil.readByByte(filePath);
        return xmlStr2Dom4jDoc(xmlStr);
    }

    /**
     * Dom4j方式
     *
     * @param xmlStr
     * @return {@link Document }
     * @throws DocumentException
     * @time 2023/07/11
     * @since 1.0.0
     */
    public static Document xmlStr2Dom4jDoc(String xmlStr) throws DocumentException {
        return DocumentHelper.parseText(xmlStr)/*.setXMLEncoding(EncodeUtil.CODE_UTF8_BOM)*/;
    }

    /**
     * w3c方式
     *
     * @param xmlStr
     * @return {@link org.w3c.dom.Document }
     * @throws ParserConfigurationException
     * @time 2023/04/22
     * @since 1.0.0
     */
    public static org.w3c.dom.Document xmlStr2W3cDoc(String xmlStr) throws ParserConfigurationException {
        org.w3c.dom.Document doc = null;
        StringReader sr = new StringReader(xmlStr);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //XXE漏洞：禁用外部实体
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (ParserConfigurationException e) {
            log.info("ParserConfiguration错误"+e);
        } catch (SAXException e) {
            log.info("SAX错误"+e);
        } catch (IOException e) {
            log.info("IO错误"+e);
        }
        return doc;
    }
    //endregion

    //region 解析XML方案
    /************************************************** dom4j **************************************************/
    public static List<?> parserXmlToEntity(String returnXml, Class cls, Map<String, String> fieldXmlPaths) {
        Document document;
        try {
            //将给定文本解析为XML文档并返回新创建的文档
            document = DocumentHelper.parseText(returnXml);
            //存储属性的名称
            List<String> filedNames = new ArrayList<>();
            //存放<属性,values>
            Map<String, List<Node>> map = new HashMap<>();
            if (null != fieldXmlPaths && !fieldXmlPaths.isEmpty()) {
                Iterator iterator = fieldXmlPaths.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    List<Node> nodes = document.selectNodes(value.toString());
                    List<Node> list = new ArrayList<>();
                    for (Node node : nodes) {
                        list.add(node);
                    }
                    //循环放入属性-> 对应的节点
                    map.put(key.toString(), list);
                    filedNames.add(key.toString());
                }
            }

            List list = new ArrayList();
            //循环将节点的值放入到对象中，然后放入到list
            for (int i = 0; i < map.get(filedNames.get(0)).size(); i++) {
                Object instance = cls.newInstance();
                for (String fieldName : filedNames) {
                    //拼接方法名称
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    //通过反射获取setter方法
                    Method method = cls.getMethod(methodName, String.class);
                    //通过invoke调用实体类对应的setter方法
                    method.invoke(instance, map.get(fieldName).get(i).getText());
                }
                list.add(instance);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /************************************************** jsoup **************************************************/
    /*public static <T> T parseFromXml(Class<T> clazz, String xml) {
        XStream xStream = new XStream(new DomDriver());
        //反序列化风险(230116最新依赖1.4.20)
        XStream.setupDefaultSecurity(xStream);
        xStream.processAnnotations(clazz);
        @SuppressWarnings("unchecked")
        T t = (T) xStream.fromXML(xml);
        return t;
    }*/
    /*public static <T> T parseElementObj(String xml, String elementId, Class<T> clazz) {
        if (StringUtils.isBlank(xml) || StringUtils.isBlank(elementId)) {
            return null;
        }
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(clazz);
        if (StringUtils.isBlank(xml) || StringUtils.isBlank(elementId)) {
            return null;
        }
        // 获取document对象
        org.jsoup.nodes.Document document = Jsoup.parse(xml, "", new Parser(new XmlTreeBuilder()));
        // 获取对应的document片段
        String ele = document.select(elementId).toString().replaceAll("\\s*", "");
        @SuppressWarnings("unchecked")
        T t = (T) xStream.fromXML(ele);
        return t;
    }*/

    /**************************************************  **************************************************/
    public static Object convertXmlStrToObject(Class<?> clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshal = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshal.unmarshal(sr);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return xmlObject;
    }
    //endregion

}
