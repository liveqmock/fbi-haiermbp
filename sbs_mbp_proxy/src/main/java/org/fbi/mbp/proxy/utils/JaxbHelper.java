package org.fbi.mbp.proxy.utils;

import org.fbi.mbp.proxy.domain.ccbvip.t2719response.CcbvipT2719ResponseRoot;
import org.fbi.mbp.proxy.domain.sbs.transactrequest.TransactRequestRoot;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by zhanrui on 2014/10/11.
 * jaxb工具
 */
public class JaxbHelper {

    public <T> T xmlToBean(Class msgBeanClazz, byte[] buffer) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            JAXBContext jaxbContext = JAXBContext.newInstance(msgBeanClazz);
            Unmarshaller um = jaxbContext.createUnmarshaller();
            T msg = (T) um.unmarshal(is);
            return msg;
        } catch (JAXBException e) {
            throw new RuntimeException("XML转Bean时出错.", e);
        }
    }

    public <T> String beanToXml(Class msgBeanClazz, T msgBean) {
        try {
            JAXBContext jc = JAXBContext.newInstance(msgBeanClazz);
            Marshaller ms = jc.createMarshaller();
            ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            ms.setProperty(Marshaller.JAXB_ENCODING, "GBK");
            ms.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ms.marshal(msgBean, os);
            byte[] buffer = os.toByteArray();

            return new String(buffer, "GBK");
        } catch (JAXBException | UnsupportedEncodingException e) {
            throw new RuntimeException("转换XML时出错.", e);
        }
    }

    private void txn_transact() throws JAXBException {
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

        xml= "<?xml version=\"1.0\" encoding=\"GBK\"?><root><Head><OpBankCode>105</OpBankCode><OpName>Transact</OpName><OpEntID>SBS</OpEntID><OpDate>20141017</OpDate><OpID>1100000276253631</OpID></Head><Param><ToAccount>44201542000052511031</ToAccount><ToName>深圳市富意达电器有限公司</ToName><ToBank>中国建设银行深圳市龙岗支行</ToBank><ToReserved1>105584000030</ToReserved1><ToReserved2>105584000030</ToReserved2><ToReserved3></ToReserved3><ToReserved4></ToReserved4><FromAccount>37101985510050404002</FromAccount><FromName>海尔财务公司</FromName><FromBank>青岛建行</FromBank><FromReserved1>105452004038</FromReserved1><FromReserved2>105452004038</FromReserved2><FromReserved3></FromReserved3><FromReserved4></FromReserved4><EnterpriseSerial>1100000276253631</EnterpriseSerial><VoucherNum></VoucherNum><Amount>43800</Amount><Currency>CNY</Currency><Usage>材料</Usage><TransDate>20141017</TransDate><System>1</System><Bank>0</Bank><Local>1</Local><Internal>0</Internal><Public>0</Public><Speed>1</Speed><Reserved1>801000243602012001,重庆新日日顺家电销售有限公司深圳分公司</Reserved1><Reserved2></Reserved2><Reserved3></Reserved3><Reserved4></Reserved4></Param></root>";

        JaxbHelper test = new JaxbHelper();
        TransactRequestRoot msg =  test.xmlToBean(TransactRequestRoot.class, xml.getBytes());
        System.out.println(msg);
        System.out.println(test.beanToXml(TransactRequestRoot.class, msg));
    }

    private void txn_2719() throws JAXBException {
//        String xml = "<?xml version=\"1.0\"?>\n" +
        String xml = "<?xml version=\"1.0\" ?>\n" +
                "<Root>\n" +
                "<Head>\n" +
                "    <Version>01</Version>\n" +
                "    <TxCode>2719</TxCode>\n" +
                "    <FuncCode>100</FuncCode>\n" +
                "    <Channel>0002</Channel>\n" +
                "    <SubCenterId>0371</SubCenterId>\n" +
                "    <NodeId>0371000000000261</NodeId>\n" +
                "    <TellerId>060830</TellerId>\n" +
                "    <TxSeqId>11443400</TxSeqId>\n" +
                "    <TxDate>20141016</TxDate>\n" +
                "    <TxTime>114434</TxTime>\n" +
                "    <UserId>0371000000000261</UserId>\n" +
                "  </Head>\n" +
                "  <Body>\n" +
                "    <RespMsg>转帐成功!</RespMsg>\n" +
                "    <RespCode>M0001</RespCode>\n" +
                "  </Body>\n" +
                "</Root>";

        xml = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
               "<Root>\n" +
                "<Head>\n" +
                "<Version>01</Version>\n" +
                "<TxCode>2719</TxCode>\n" +
                "<FuncCode>000</FuncCode>\n" +
                "<Channel>0005</Channel>\n" +
                "<SubCenterId>0371</SubCenterId>\n" +
                "<UserId>0371000000000261</UserId>\n" +
                "<TellerId>060830</TellerId>\n" +
                "<TxSeqId>09270898</TxSeqId>\n" +
                "<TxDate>20141017</TxDate>\n" +
                "<TxTime>092706</TxTime>\n" +
                "<NodeId>0371000000000261</NodeId>\n" +
                "</Head>\n" +
                "<Body>\n" +
                "<RespCode>M0001</RespCode>\n" +
                "<RespMsg>交易受理成功，转银行落地处理</RespMsg>\n" +
                "<TxnEndFlag>0</TxnEndFlag>\n" +
                "<TxnEndFlag2>0</TxnEndFlag2>\n" +
                "</Body>\n" +
                "</Root>";

        JaxbHelper test = new JaxbHelper();
        CcbvipT2719ResponseRoot msg =  test.xmlToBean(CcbvipT2719ResponseRoot.class, xml.getBytes());
        System.out.println(msg);
        System.out.println(test.beanToXml(CcbvipT2719ResponseRoot.class, msg));
    }

    public static void main(String... argv) throws JAXBException {
        JaxbHelper test = new JaxbHelper();
        test.txn_transact();
//        test.txn_2719();

/*
        TransactResponseRoot clientRespBean = new TransactResponseRoot();
        ClientResponseHead clientResponseHead = new ClientResponseHead();
        TransactResponseParam clientResponseParam = new TransactResponseParam();
        clientRespBean.setHead(clientResponseHead);
        clientRespBean.setParam(clientResponseParam);

        clientResponseParam.setReserved1("");
        System.out.println(test.beanToXml(TransactResponseRoot.class, clientRespBean));
*/
    }
}
