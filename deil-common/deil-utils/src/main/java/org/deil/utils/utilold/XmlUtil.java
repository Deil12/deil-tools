//package org.deil.utils.util;
//
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//import lombok.extern.slf4j.Slf4j;
//import org.dom4j.*;
//import org.dom4j.io.SAXReader;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Unmarshaller;
//import java.beans.XMLDecoder;
//import java.beans.XMLEncoder;
//import java.io.*;
//import java.lang.reflect.Method;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Slf4j
//public class XmlUtil {
//
//    /**
//     * @date 2022/6/28, 上午9:21 : 反序列化
//     * @param xml
//     * @return T
//     */
//    @SuppressWarnings("unchecked")
//    public static <T> T parserXML(String xml) {
//        ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
//        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
//        decoder.close();
//        return (T) decoder.readObject();
//    }
//
//    /**
//     * @date 2022/6/28, 上午9:21 : 序列化
//     * @param entity
//     * @return java.lang.String
//     */
//    @Deprecated
//    public static <T> String formatXML(T entity) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(out));
//        encoder.writeObject(entity);
//        encoder.close();
//        return out.toString();
//    }
//
//    public static <T> T parse(String filePath) throws DocumentException {
//        SAXReader reader = new SAXReader();
//        Map nodeMap = new HashMap<>();
//        Document document = reader.read(new File(filePath));
//        Element rootElement = document.getRootElement();
//        String qualifiedName = rootElement.getQualifiedName();
//        Iterator<Element> iterator = rootElement.elementIterator();
//        while (iterator.hasNext()) {
//            Element childElement = (Element) iterator.next();
//            List<Attribute> attributes = childElement.attributes();
//            Iterator<Element> childIterator = childElement.elementIterator();
//            while (childIterator.hasNext()) {
//                Element element = (Element) childIterator.next();
//                nodeMap.put(element.getName(), element.getStringValue());
//            }
//        }
//        return (T) nodeMap;
//    }
//
//    /*
//    public static JSONObject xml2Json(String xmlStr) throws JDOMException, IOException {
//        if (StringUtils.isEmpty(xmlStr))
//            return null;
//        xmlStr = xmlStr.replaceAll("\\\n", "");
//        byte[] xml = xmlStr.getBytes("UTF-8");
//        JSONObject json = new JSONObject();
//        InputStream is = new ByteArrayInputStream(xml);
//        SAXBuilder sb = new SAXBuilder();
//        org.jdom2.Document doc = sb.build(is);
//        org.jdom2.Element root = doc.getRootElement();
//        json.put(root.getName(), iterate2Json(root));
//        return json;
//    }
//
//    private static JSONObject iterate2Json(org.jdom2.Element element) {
//        List<org.jdom2.Element> node = element.getChildren();
//        JSONObject obj = new JSONObject();
//        List list = null;
//        for (Element child : node) {
//            list = new LinkedList();
//            String text = child.getTextTrim();
//            if (StringUtil.isBlank(text)) {
//                if (child.getChildren().size() == 0) {
//                    continue;
//                }
//                if (obj.containsKey(child.getName())) {
//                    list = (List) obj.get(child.getName());
//                }
//                list.add(iterate2Json(child)); //遍历child的子节点
//                obj.put(child.getName(), list);
//            } else {
//                if (obj.containsKey(child.getName())) {
//                    Object value = obj.get(child.getName());
//                    try {
//                        list = (List) value;
//                    } catch (ClassCastException e) {
//                        list.add(value);
//                    }
//                }
//                if (child.getChildren().size() == 0) { //child无子节点时直接设置text
//                    obj.put(child.getName(), text);
//                } else {
//                    list.add(text);
//                    obj.put(child.getName(), list);
//                }
//            }
//        }
//        return obj;
//    }
//    */
//
//    /**
//     * @date 2022/7/12, 下午2:41 : TODO 普通字符串转 xml字符串
//     * @param xmlStr
//     * @return java.lang.String
//     */
//    public static String xmlStr2Form(String xmlStr) {
//        try {
//            String s = "";
//            /*Guid guid = new Guid();
//            String path = @".\" + guid.ToString(); //当前目录下
//            XmlDocument doc = new XmlDocument();
//            doc.LoadXml(strXml);
//            doc.Save(path);
//            using (StreamReader sr = new StreamReader(path, System.Text.Encoding.UTF8)) {
//                s = sr.ReadToEnd();
//            }
//            File.Delete(path);*/
//            return s;
//        } catch (Exception ex) {
//            return xmlStr;
//            //ErrLogManager elog = new ErrLogManager();
//            //elog.WriteInfoLine("ConvertToXmlForm error " + ex.Message);
//        }
//    }
//    /**
//     * 将String类型的xml转换成对象
//     */
//    public static Object xmlStr2Obj(Class<?> clazz, String xmlStr) {
//        Object xmlObject = null;
//        try {
//            JAXBContext context = JAXBContext.newInstance(clazz);
//            // 进行将Xml转成对象的核心接口
//            Unmarshaller unmarshal = context.createUnmarshaller();
//            StringReader sr = new StringReader(xmlStr);
//            xmlObject = unmarshal.unmarshal(sr);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return xmlObject;
//    }
//
//    /**
//     * 将file类型的xml转换成对象
//     */
//    public static Object filePath2Obj(Class<?> clazz, String filePath) {
//        Object xmlObject = null;
//        try {
//            JAXBContext context = JAXBContext.newInstance(clazz);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            InputStreamReader isr=new InputStreamReader(new FileInputStream(filePath),"UTF-8");
//            xmlObject = unmarshaller.unmarshal(isr);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return xmlObject;
//    }
//
//    /**
//     * @date 2022/6/30, 上午11:26 : 自定义转 Map 迭代器 + 策略
//     */
//    /*
//    @SuppressWarnings("unchecked")
//    public static <T> T interaterToMap(String filePath, Class<?> clazz) {
//        //迭代器，保存 content对象
//        Map elementMap = new LinkedHashMap();
//        SAXReader reader = new SAXReader();
//        Document document = null;
//        try {
//            document = reader.read(new File(filePath));
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//        //根结点
//        Element rootElement = document.getRootElement();
//        elementMap.put("ROOTELEMENT", rootElement.getName());
//        //命名空间
//        Iterator<Attribute> attributeIterator = rootElement.attributeIterator();
//        //while (attributeIterator.hasNext()) {
//        //    Attribute next = attributeIterator.next();
//        //    elementMap.put(next.getName() != null ? next.getName() : "NAMESPACE", next.getValue());
//        //}
//        Namespace namespace = rootElement.getNamespace();
//        elementMap.put(namespace.getName() != null ? namespace.getName() : "NAMESPACE", namespace.getText());
//        //子节点
//        Iterator<Element> iterator = rootElement.elementIterator();
//        while (iterator.hasNext()) {
//            Element childElement = (Element) iterator.next();
//            if (!childElement.isRootElement()) {
//                if (elementMap.get("HEADELEMENT") == null) {
//                    elementMap.put("HEADELEMENT", childElement.getName());
//                } else {
//                    if (elementMap.get("BODYELEMENT")==null)
//                        elementMap.put("BODYELEMENT", childElement.getName());
//                }
//            }
//            elementMap.put(childElement.getName(), childElement.getStringValue());
//            Iterator<Element> childIterator = childElement.elementIterator();
//            while (childIterator.hasNext()) {
//                Element element = (Element) childIterator.next();
//                elementMap.put(element.getName(), element.getStringValue());
//            }
//        }
//        if (clazz != null) {
//            XStream xstream = new XStream(new MyXppDriver(false));
//            xstream.autodetectAnnotations(true);
//            xstream.processAnnotations(clazz);
//        }
//        return (T) elementMap;
//    }
//
//    public static class MyXppDriver extends XppDriver {
//        boolean useCDATA = false;
//
//        MyXppDriver(boolean useCDATA) {
//            super(new XmlFriendlyNameCoder("__", "_"));
//            this.useCDATA = useCDATA;
//        }
//
//        @Override
//        public HierarchicalStreamWriter createWriter(Writer out) {
//            if (!useCDATA) {
//                return super.createWriter(out);
//            }
//            return new PrettyPrintWriter(out) {
//                boolean cdata = true;
//
//                @Override
//                public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
//                    super.startNode(name, clazz);
//                }
//
//                @Override
//                protected void writeText(QuickWriter writer, String text) {
//                    if (cdata) {
//                        writer.write(cDATA(text));
//                    } else {
//                        writer.write(text);
//                    }
//                }
//
//                private String cDATA(String text) {
//                    return "<![CDATA[" + text + "]]>";
//                }
//            };
//        }
//    }
//    */
//
//    /**
//     * @date 2022/7/6, 下午12:24 : filePath 获取 String
//     * @param filePath
//     * @return java.lang.String
//     */
//    public static String filePath2Str(String filePath) throws IOException,Exception {
//        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
//        String result = new String(bytes, EncodeUtil.getEncode(new File(filePath), true));
//        return result;
//    }
//
//    /**
//     * @date 2022/7/6, 上午11:23 : filePath 获取 Map
//     * @param filePath
//     * @return java.util.Map
//     */
//    public static Map filePath2Map(String filePath) throws Exception {
//        String xmlStr = filePath2Str(filePath);
//        return xmlStr2Map(xmlStr);
//    }
//
//    /**
//     * @date 2022/7/6, 上午11:29 : xmlStr 获取 Map
//     * @param xmlStr
//     * @return java.util.Map
//     */
//    public static Map xmlStr2Map(String xmlStr) throws Exception {
//        XmlMapper xmlMapper = new XmlMapper();
//        Map xmlMap = xmlMapper.readValue(xmlStr, Map.class);
//        return xmlMap;
//    }
//
//    /**
//     * @date 2022/7/11, 下午4:08 : filePath 获取 Document
//     * @param filePath
//     * @return org.dom4j.Document
//     */
//    public static Document filePath2Doc(String filePath) throws Exception {
//        String xmlStr = filePath2Str(filePath);
//        return xmlStr2Doc(xmlStr);
//    }
//
//    /**
//     * @date 2022/7/11, 下午4:08 : xmlStr 获取 Document
//     * @param xmlStr
//     * @return org.dom4j.Document
//     */
//    public static Document xmlStr2Doc(String xmlStr) throws DocumentException {
//        return DocumentHelper.parseText(xmlStr)/*.setXMLEncoding(EncodeUtil.CODE_UTF8_BOM)*/;
//    }
//
//    /************************************************** dom4j **************************************************/
//    public static List<?> parserXmlToEntity(String returnXml, Class cls, Map<String, String> fieldXmlPaths) {
//        Document document;
//        try {
//            //将给定文本解析为XML文档并返回新创建的文档
//            document = DocumentHelper.parseText(returnXml);
//            //存储属性的名称
//            List<String> filedNames = new ArrayList<>();
//            //存放<属性,values>
//            Map<String, List<Node>> map = new HashMap<>();
//            if (null != fieldXmlPaths && !fieldXmlPaths.isEmpty()) {
//                Iterator iterator = fieldXmlPaths.entrySet().iterator();
//                while (iterator.hasNext()) {
//                    Map.Entry entry = (Map.Entry) iterator.next();
//                    Object key = entry.getKey();
//                    Object value = entry.getValue();
//                    List<Node> nodes = document.selectNodes(value.toString());
//                    List<Node> list = new ArrayList<>();
//                    for (Node node : nodes) {
//                        list.add(node);
//                    }
//                    //循环放入属性-> 对应的节点
//                    map.put(key.toString(), list);
//                    filedNames.add(key.toString());
//                }
//            }
//
//            List list = new ArrayList();
//            //循环将节点的值放入到对象中，然后放入到list
//            for (int i = 0; i < map.get(filedNames.get(0)).size(); i++) {
//                Object instance = cls.newInstance();
//                for (String fieldName : filedNames) {
//                    //拼接方法名称
//                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//                    //通过反射获取setter方法
//                    Method method = cls.getMethod(methodName, String.class);
//                    //通过invoke调用实体类对应的setter方法
//                    method.invoke(instance, map.get(fieldName).get(i).getText());
//                }
//                list.add(instance);
//            }
//            return list;
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return null;
//    }
//
//    /************************************************** jsoup **************************************************/
//    public static <T> T parseFromXml(Class<T> clazz, String xml) {
//        XStream xStream = new XStream(new DomDriver());
//        xStream.processAnnotations(clazz);
//        @SuppressWarnings("unchecked")
//        T t = (T) xStream.fromXML(xml);
//        return t;
//    }
//    /*public static <T> T parseElementObj(String xml, String elementId, Class<T> clazz) {
//        if (StringUtils.isBlank(xml) || StringUtils.isBlank(elementId)) {
//            return null;
//        }
//        XStream xStream = new XStream(new DomDriver());
//        xStream.processAnnotations(clazz);
//        if (StringUtils.isBlank(xml) || StringUtils.isBlank(elementId)) {
//            return null;
//        }
//        // 获取document对象
//        org.jsoup.nodes.Document document = Jsoup.parse(xml, "", new Parser(new XmlTreeBuilder()));
//        // 获取对应的document片段
//        String ele = document.select(elementId).toString().replaceAll("\\s*", "");
//        @SuppressWarnings("unchecked")
//        T t = (T) xStream.fromXML(ele);
//        return t;
//    }*/
//
//    /**************************************************  **************************************************/
//    public static Object convertXmlStrToObject(Class<?> clazz, String xmlStr) {
//        Object xmlObject = null;
//        try {
//            JAXBContext context = JAXBContext.newInstance(clazz);
//            // 进行将Xml转成对象的核心接口
//            Unmarshaller unmarshal = context.createUnmarshaller();
//            StringReader sr = new StringReader(xmlStr);
//            xmlObject = unmarshal.unmarshal(sr);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return xmlObject;
//    }
//
//    /************************************************** 大兴通知报文 DxpMsg 解析 **************************************************/
//
//    /**
//     * @time Time() : 获取报文外层BizCollaborationId
//     * @param strXml
//     * @return {@link String }
//     */
//    public static String getXMLContentBizCollaborationId(String strXml) {
//        String result = "";
//
//        String leftPattern = "<dxp:Key name=\"BizCollaborationId\">";
//        String rightPattern = "</dxp:Key>";
//        Pattern regex = Pattern.compile(String.format("%s([\\s\\S]*?)%s", leftPattern, rightPattern), Pattern.CASE_INSENSITIVE);
//
//        Matcher matcher = regex.matcher(strXml);
//        while (matcher.find()) {
//            int patternLength = (leftPattern + rightPattern).length();
//            if (matcher.end() - matcher.start() + 1 <= patternLength) {
//                continue;
//            }
//            result = matcher.group().substring(leftPattern.length(), leftPattern.length() + (matcher.end() - matcher.start() - patternLength));
//        }
//
//        return result;
//    }
//
//    /**
//     * @time Time() : 获取报文外层BPTNo
//     * @param strXml
//     * @return {@link String }
//     */
//    public static String getXMLContentBPTNo(String strXml) {
//        String result = "";
//
//        String leftPattern = "<dxp:Key name=\"BPTNo\">";
//        String rightPattern = "</dxp:Key>";
//        Pattern regex = Pattern.compile(String.format("%s([\\s\\S]*?)%s", leftPattern, rightPattern), Pattern.CASE_INSENSITIVE);
//
//        Matcher matcher = regex.matcher(strXml);
//        while (matcher.find()) {
//            int patternLength = (leftPattern + rightPattern).length();
//            if (matcher.end() - matcher.start() + 1 <= patternLength) {
//                continue;
//            }
//            result = matcher.group().substring(leftPattern.length(), leftPattern.length() + (matcher.end() - matcher.start() - patternLength));
//        }
//
//        return result;
//    }
//
//    /**
//     * 处理XML内容
//     * @param strXml xml字符串
//     * @return {@link String }
//     * @time Time() : 解析xmlcontent>
//     */
//    public static String getXMLContentDecodeBase64(String strXml) {
//        if (StringUtil.isBlank(strXml)) {
//            return strXml;
//        }
//        String data = "";
//
//        String leftPattern = "<dxp:Data>";
//        String rightPattern = "</dxp:Data>";
//        Pattern regex = Pattern.compile(String.format("%s([\\s\\S]*?)%s", leftPattern, rightPattern), Pattern.CASE_INSENSITIVE);
//
//        Matcher matcher = regex.matcher(strXml);
//        while (matcher.find()) {
//            int patternLength = (leftPattern + rightPattern).length();
//            if (matcher.end() - matcher.start() + 1 <= patternLength) {
//                continue;
//            }
//            data = matcher.group().substring(leftPattern.length(), leftPattern.length() + (matcher.end() - matcher.start() - patternLength));
//        }
//
//        //Base64解码
//        String result = new String(Base64.getDecoder().decode(data));
//        return result;
//    }
//
//}
