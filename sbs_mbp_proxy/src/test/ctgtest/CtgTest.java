package ctgtest;

import com.ibm.ctg.client.ECIRequest;
import com.ibm.ctg.client.JavaGateway;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
 * CTG���Գ�����SBS���Ի���ͨͨ�����ܹ������շ���
 */
public class CtgTest {

    private static JavaGateway javaGatewayObject;
    public static int iValidationFailed = 0;

    private static boolean bDataConv = true;
    private static String strDataConv = "ASCII";
    private static byte[] buffer = new byte[32000];

    public static void main(String[] args) {

        ECIRequest eciRequestObject = null;
        //String strProgram = "SCLMAIN";
        String strProgram = "SCLMPC";
        String strChosenServer = "haier"; //������������������д��C:\Program Files\IBM\CICS Transaction Gateway\bin\CTG.ini��
        // String strUrl = "local:";
        // String buff = "";
        // int iPort = 1445;
        String strUrl = "10.143.20.130";

        String buff = "";
        int iPort = 2006;

        try {
            int iCommareaSize = 32000;
            byte[] abytCommarea = new byte[iCommareaSize];

            javaGatewayObject = new JavaGateway(strUrl, iPort);
            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);
            System.out.println("Hello!");
            int x = 0;
            String zero = "010100100000000005";
            int no = 1001;

            for (int i = 1; i <= 1; i++) {
                if (eciRequestObject.SystemList.isEmpty() == true) {
                    System.out.println("No CICS servers have been defined.");
                    if (javaGatewayObject.isOpen() == true) {
                        //javaGatewayObject.close();
                    }
                    //System.exit(0);
                }
                //���

                //buff = "TPEIxxxx 010       MPC1SYS1                       0611111103010026004010 140000010201100118000000000000000001";
                buff = "TPEIn020 010        MPC1MPC1SYS213524678"; //��ͷ���ݣ�xxxx���ף�010���㣬MPC1�նˣ�MPC2��Ա����ͷ����51���ַ�
                System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length()); //�����ͷ

                List list = new ArrayList();//�������ݣ����������ݷ���list�У��м����������add����
                //x = x + 1;
                //int length = String.valueOf(x).length();
                //String result = zero.substring(length)+x;
                no = no + 1;
                String result = zero.substring(0, 14) + "" + no;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//n020 CTY ������
                list.add("MPC1000147231452  ");     //MPC��ˮ��
                list.add("010");                    //���׻���
                list.add("20141016");               //ί������
                list.add("010104");                 //�ͻ���
                list.add("CTY");                    //��������
                list.add("001");                    //���׻���
                list.add("+0000000000001.01");      //���׽��
                list.add("T");                      //�������
                list.add("01");                     //����ʻ�����
                list.add("801000017002013001    ");//����ʻ�
                list.add("1");                     //�����ʻ�����
                list.add("                      ");//�����ʻ�
                list.add("11000071209595027012");  //�տ����ʺ�
                list.add("��������ʢ����ά�����޹�˾");//�տ�������
                list.add("������ũ�����ú�����������                                                      ");//�տ�������
                list.add("402511301018                                                                    ");//������
                list.add("                                        ");                                        //������
                list.add("1                                                                               ");//���������150
                list.add("GE����չ̨����                                                                  ");//�����;150
                list.add(" ");    //������
                list.add("402511301018");          //�տ��л����š��к�
                list.add("������    ");            //������
                list.add(" ");                     //������
                list.add("FSPP293062000001");      //FS��ˮ��
                list.add("1300000000000052");      //������ˮ��



/*				
//n022       �Թ�����֧�������ѯ
             list.add("MPC1000147231421  ");                       //  MPCNO         18λ
             list.add("1100000269791273");                         //  ������ˮ��    16λ
             list.add("20141009");                                 //  ��������      8λ
             list.add("317307000198");                             //  �տ����к�   12λ
*/

/*			           
//n040       ��������
           list.add("20140918");                                //  ��������   8
           list.add("1100000049046323  ");                      //  MPCNO       18
           list.add("1100000050851133");                        //  �������к�16
           list.add("+0000000000001.02");                       //  �ܽ��       17
           list.add("0000001");                                 //  �ܱ���        7
           list.add("0000001");                                 //  �����ܱ��� 7
           list.add("0");                                       //  �Ƿ��к�����  0-��1-��
           list.add("801000003012011001    ");                  //  ת���ʻ�  22
           list.add("99999998    ");                            //  ��; 12
           list.add("000000000000000000000000000030");          //  ��ע,   30
           list.add("00000000000000000000000000000032");        //  ��ע1,  32
           list.add("00000000000000000000000000000032");        //  ��ע2,  32
           list.add("105");                                     //  ���д���,  3
           list.add("+0000000000000.00");                       //  ʧ�ܽ��       17
           list.add("0000000");                                 //  ʧ�ܱ���        7
           list.add("BAS");                                     //  ������� BAP-��������,BAS-������������
           list.add("0000001.01||2390049980100048890|name||");  //  ���������ļ�����  29000
*/

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                //������

                //�������
                setValues(list, abytCommarea);
                //buff = "TPEI8118"+ch+ch+"010"+ch+ch+ch+ch+ch+ch+ch+"MPC1SYS2"+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+ch+"0611111103010026004010"+ch+"140000010201100118000000000000000001";
                //buff.replace(' ',ch);
                //System.out.println("test 4.1 + "+buff.length());
                //System.out.println("test 4.2 + "+buff);
                //buff = "TPEI8118000100000000MPC1SYS20000000000000000000000006111111030100260040100140000010201100118000000000000000001";
                //String aa = "TPEI8118  010       MPC1SYS1                       ";
                //System.out.println("aa.size + "+ aa.length());
                //buff = "AAAA";
                //System.arraycopy(buffer, pos, abytCommarea, 0, len);
                //System.arraycopy(buff.getBytes(), 0, abytCommarea, 0, Math.min(abytCommarea.length, buff.length()));
                //System.out.println("test 4.3 + "+abytCommarea.toString());
                //System.out.println("test 4.4 + "+buff.getBytes() +" "+buff);
                //System.arraycopy(buff.getBytes(), 0, abytCommarea, 0, buff.length());
                //System.arraycopy(getBytes(buff), 0, abytCommarea, 0, buff.length());
                //System.out.println("test 4.5 + "+ new String(abytCommarea));

                //���Ͱ�
                eciRequestObject =
                        new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                                strChosenServer, //CICS server
                                "CICSUSER", //CICS userid
                                "", //CICS password
                                strProgram, //CICS program to be run
                                null, //CICS transid to be run
                                abytCommarea, //Byte array containing the
                                // COMMAREA
                                iCommareaSize, //COMMAREA length
                                ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                                0);                       //ECI LUW token


                //��ȡ���ر���
                if (flowRequest(eciRequestObject) == true) {
                    //��sof
                    System.out.println("����ֵ11Ϊ\n" + new String(abytCommarea));
                }
                System.out.println("����ֵ22Ϊ\n" + new String(abytCommarea));

                if (javaGatewayObject.isOpen() == true) {
                    //javaGatewayObject.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static byte[] getBytes(String source) throws UnsupportedEncodingException {
        if (bDataConv) {
            return source.getBytes(strDataConv);
        } else {
            return source.getBytes();
        }
    }

    public static void setValues(List list, byte[] bb) {
        int start = 51;
        for (int i = 1; i <= list.size(); i++) {
            String value = list.get(i - 1).toString();

            String size = "";
            try {
                size = new String(value.getBytes("GBK"), "ISO-8859-1");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  System.out.println(i+" "+start+" "+value.length()+" "+value);

            System.out.println(i + " " + start + " " + size.length() + " " + value);
            setVarData(start, value, bb); //����list�������������������������������ѭ���������
            start = start + size.length() + 2;
        }
    }

    public static void setVarData(int pos, String data, byte[] aa) {

        //˵���������Է��֣��������ݵ����ݳ��ȣ�����2�ַ���������ʮ��������ʽ����
        //���磬��һ�����������������Ϊ��111111��������Ϊ6���ַ�������ڰ�����д��"06111111"����CTG Server���޷�������ȡ�ַ����ȣ�
        //"06"������ʮ��������ʽ���ͣ���0x00 0x60
        //����������������ַ�������һ�²�����ȷȡ�������ַ��ĳ��ȣ�ʹ��UTF-8�ַ�������ȡ����ȷ�������ַ��ĳ���
        String size = "";
        try {
            size = new String(data.getBytes("GBK"), "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        short len = (short) size.length();

//		short len = (short)data.length();
        byte[] slen = new byte[2];
        slen[0] = (byte) (len >> 8);
        slen[1] = (byte) (len >> 0);
        System.arraycopy(slen, 0, aa, pos, 2);
        System.arraycopy(data.getBytes(), 0, aa, pos + 2, len);
    }

    private static boolean

    flowRequest(ECIRequest requestObject) {
        try {
            int iRc = javaGatewayObject.flow(requestObject);
            switch (requestObject.getCicsRc()) {
                case ECIRequest.ECI_NO_ERROR:
                    if (iRc == 0) {
                        return false;
                    } else {
                        System.out.println("\nError from Gateway ("
                                + requestObject.getRcString()
                                + "), correct and rerun this sample");
                        if (javaGatewayObject.isOpen() == true) {
                            javaGatewayObject.close();
                        }
                        System.exit(0);
                    }
                case ECIRequest.ECI_ERR_SECURITY_ERROR:
                    if (iValidationFailed == 0) {
                        return true;
                    }
                    System.out.print("\n\nValidation failed. ");
                    if (iValidationFailed < 3) {
                        System.out.println("Try entering your details again.");
                        return true;
                    }
                    break;
                case ECIRequest.ECI_ERR_TRANSACTION_ABEND:
                    System.out.println("\nYou are not authorised to run this "
                            + "transaction.");
            }
            System.out.println("\nECI returned: "
                    + requestObject.getCicsRcString());
            System.out.println("Abend code was "
                    + requestObject.Abend_Code + "\n");
            if (javaGatewayObject.isOpen() == true) {
                javaGatewayObject.close();
            }
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return true;
    }
}