package ccmstest;

/**
 * Created by zhanrui on 2014/12/12.
 */
public class T9009002Response {
    private String formid;//T531:已成功接收，SBS记账成功，银行处理结果待查询   T999：重复记账  	X(4)


    private String nbkmsg;//(不用)	X(4)
    private String vchnum;//凭证号(银行返回，左对齐，右补空格)	X(39)
    private String secnum;//外围系统流水号	X(18)		
    private String txncde;//交易代号	X(4)		
    private String tlrnum;//柜员号	X(4)		
    private String vchset;//传票套号(左补零右对齐)	X(4)		
    private String setseq;//传票套内顺序号(左补零右对齐)	X(2)		
    private String txndat;//交易日期(YYYYMMDD)	X(8)		
    private String intcur;//币别代号	X(3)		
    private String txnamt;//交易金额(左补零右对齐)	S9(13).99		
    private String actty1;//付款账号帐户类型(01)	X(2)		
    private String iptac1;//付款账号	X(22)		
    private String cusid1;//付款账号客户号	X(7)		
    private String actnm1;//付款账号户名	X(30)		
    private String shtseq;//(不用)	X(2)		
    private String actty2;//帐户类型2(01)	X(2)		
    private String iptac2;//帐号2	X(22)		
    private String cusid2;//客户号2	X(7)		
    private String actnm2;//帐户名称2	X(30)		
    private String dptprd;//(不用)	X(2)		
    private String valdat;//起息日(YYYYMMDD)	X(8)		
    private String auttlr;//授权柜员	X(4)		
    private String acttyp1;//是否落地处理(00-不落地)	X(2)		
    private String acttyp2;//帐户类型2(01)	X(2)		

}
