package org.deil.utils.utils;

import lombok.experimental.UtilityClass;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * 
 *
 * @DATE 2023/03/28
 * @CODE Deil
 */
@UtilityClass
public class XMLjdkUtil {

    public static Object xmlStrToObject(Class clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlObject;
    }

    public static String objectToXmlStr(Object obj,Class<?> load){
        String result = "";
        try {
            JAXBContext context = JAXBContext.newInstance(load);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj,writer);
            result = writer.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
