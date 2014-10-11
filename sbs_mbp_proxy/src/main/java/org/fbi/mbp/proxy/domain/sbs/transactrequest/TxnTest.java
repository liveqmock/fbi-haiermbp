package org.fbi.mbp.proxy.domain.sbs.transactrequest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by zhanrui on 2014/10/11.
 */
public class TxnTest <T>{
    public static void main(String... argv) throws JAXBException {
         String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                 "<root>\n" +
                 "<Head>\n" +
                 "\t<OpBankCode>105</OpBankCode>\n" +
                 "\t<OpName>Transact</OpName>\n" +
                 "\t<OpEntID>SBS</OpEntID>\n" +
                 "\t<OpDate>20141010</OpDate>\n" +
                 "\t<OpID>1300000000000037</OpID>\n" +
                 "</Head>\n" +
                 "<Param>\n" +
                 "\t<ToAccount>11000071209595027012</ToAccount>\n" +
                 "\t<ToName>泌阳县新盛电器维修有限公司</ToName>\n" +
                 "\t<ToBank>泌阳县农村信用合作社联合社</ToBank>\n" +
                 "\t<ToReserved1>402511301018</ToReserved1>\n" +
                 "\t<ToReserved2>402511301018</ToReserved2>\n" +
                 "\t<ToReserved3></ToReserved3>\n" +
                 "\t<ToReserved4></ToReserved4>\n" +
                 "\t<FromAccount>37101985510050404002</FromAccount>\n" +
                 "\t<FromName>海尔财务公司</FromName>\n" +
                 "\t<FromBank>青岛建行</FromBank>\n" +
                 "\t<FromReserved1>105452004038</FromReserved1>\n" +
                 "\t<FromReserved2>105452004038</FromReserved2>\n" +
                 "\t<FromReserved3></FromReserved3>\n" +
                 "\t<FromReserved4></FromReserved4>\n" +
                 "\t<EnterpriseSerial>1300000000000037</EnterpriseSerial>\n" +
                 "\t<VoucherNum></VoucherNum>\n" +
                 "\t<Amount>101</Amount>\n" +
                 "\t<Currency>CNY</Currency>\n" +
                 "\t<Usage>GE冰箱展台制作</Usage>\n" +
                 "\t<TransDate>20141010</TransDate>\n" +
                 "\t<System>1</System>\n" +
                 "\t<Bank>1</Bank>\n" +
                 "\t<Local>1</Local>\n" +
                 "\t<Internal>0</Internal>\n" +
                 "\t<Public>0</Public>\n" +
                 "\t<Speed>1</Speed>\n" +
                 "\t<Reserved1>801000017002013001,重庆海尔投资发展有限公司</Reserved1>\n" +
                 "\t<Reserved2></Reserved2>\n" +
                 "\t<Reserved3></Reserved3>\n" +
                 "\t<Reserved4></Reserved4>\n" +
                 "</Param>\n" +
                 "</root>";

        TxnTest<TransactRoot> test = new TxnTest<TransactRoot>();
        TransactRoot msg = (TransactRoot)test.xmlToBean(xml.getBytes(), TransactRoot.class);
        test.beanToXml(msg, TransactRoot.class);
    }

    private  T xmlToBean(byte[] buffer, Class clazz) throws JAXBException {
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        T msg = (T)um.unmarshal(is);

        System.out.println(msg);
        return msg;
    }

    private byte[]  beanToXml(T msgBean, Class clazz) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazz);
        Marshaller ms = jc.createMarshaller();
        ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ms.setProperty(Marshaller.JAXB_ENCODING, "GBK");
        ms.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ms.marshal(msgBean, os);
        byte[] buffer = os.toByteArray();

        System.out.println(new String(buffer));
        return buffer;
    }
/*
    private TransactRoot xmlToBean(byte[] buffer) throws JAXBException {
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        JAXBContext jaxbContext = JAXBContext.newInstance(TransactRoot.class);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        TransactRoot msg = (TransactRoot)um.unmarshal(is);

        System.out.println(msg);
        return msg;
    }

    private void beanToXml(TransactRoot msg) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(TransactRoot.class);
        Marshaller ms = jc.createMarshaller();
        ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ms.setProperty(Marshaller.JAXB_ENCODING, "GBK");
        ms.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ms.marshal(msg, os);
        byte[] buffer = os.toByteArray();

        System.out.println(new String(buffer));
    }
*/
}
