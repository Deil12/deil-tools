package org.deil.utils.utils;

import lombok.experimental.UtilityClass;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@UtilityClass
public class XMLdom4jUtil {

    private static final String FIELD = "";
    private static final String FIELDNAME = "";
    private static final String ROW = "";

    /** * 获取xml对象的根节点 * @param document * @return root */
    public static Element getRoot(Document document){

        Element root = null;
        root = document.getRootElement();
        return root;
    }
    /** * 获取fieldName的属性值 * @param document * @return fields */
    public static List<String> getFields(Document document){

        List<String> fields = new ArrayList<>();
        Element root = getRoot(document);
        if (null != root){

            // 存储遍历节点
            for (Iterator i = root.elementIterator(); i.hasNext();) {

                Element el = (Element) i.next();
                for (Iterator j = el.elementIterator(FIELD); j.hasNext();){

                    Element e2 = (Element) j.next();
                    Attribute attribute = e2.attribute(FIELDNAME);
                    fields.add(attribute.getValue());
                }
            }
        }
        return fields;
    }

    /** * 根获取每行数据形成一个列表 * @param document * @return rows */
    public static List<Element> getRows(Document document){

        List<Element> rows = new ArrayList<>();
        Element root = getRoot(document);
        List<String> fields = getFields(document);
        if (!fields.isEmpty()){

            for (Iterator i = root.elementIterator(); i.hasNext();) {

                Element el = (Element) i.next();
                for (Iterator k = el.elementIterator(ROW);k.hasNext();){

                    // 一行数据
                    Element e2 = (Element) k.next();
                    rows.add(e2);
                }
            }
        }
        return rows;
    }

    /** * 根据url获取xml对象 * @param url * @return document */
    public static Document getXmlFromUrl(String url) throws DocumentException, SAXException {

        Document document=null;
        SAXReader saxReader = new SAXReader();
        saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        document = saxReader.read(url);
        return document;
    }

    /** * 根据xml字符串获取xml对象 * @param xmlString * @return document */
    public static Document getXmlFromString(String xmlString) throws DocumentException {

        Document document = DocumentHelper.parseText(xmlString);
        return document;
    }

    /** * xml一行转对象 * @param document * @param clazz * @param row * @return * row ---> pojo */
    public static Object getObject(Document document, Class<?> clazz, Element row) {

        Object obj=null;
        try {

            obj=clazz.newInstance();//创建对象
            List<String> fields = getFields(document);
            // 获取属性名
            for(int i=0;i<fields.size();i++){

                String propertyname = fields.get(i);
                String propertyvalue = row.attribute(fields.get(i)).getValue();
                Method method = obj.getClass().getMethod("set"+propertyname,String.class);
                method.invoke(obj,propertyvalue);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return obj;
    }

}
