package ccmstest;

/**
 * Created by zhanrui on 2014/12/12.
 * 单笔对外支付
 */
public class T9009002Request {
    private String FBTIDX;//外围系统流水号 X(18)
    private String ORGIDT;//交易机构(010)  X(3)
    private String ORDDAT;//委托日期(交易日期)  X(8)
    private String FBTACT;//客户号 X(7)
    private String ORDTYP;//交易类型(CTY－汇款)    X(3)
    private String TXNCUR;//交易货币(001:人民币)   X(3)
    private String TXNAMT;//交易金额
    private String RMTTYP;//汇款类型(M－信汇 T－电汇)X(1)
    private String CUSTYP;//汇款帐户类型(01)  X(2)
    private String CUSACT;//汇款帐户 (18位帐户号)   X(22)
    private String FEETYP;//是否见单（0-不见单，1-见单，2-维修费,3-个人付款）   X(1)
    private String FEEACT;//费用帐户 (18位帐户号)   X(22)
    private String BENACT;//收款人帐号        X(35)
    private String BENNAM;//收款人名称       X(150)
    private String BENBNK;//收款行行名       X(150)
    private String AGENBK;//代理行行名       X(80)
    private String PBKACT;//人行账号        X(40)
    private String RETNAM;//汇款人名称       X(150)
    private String RETAUX;//汇款用途(备注)    x(150)
    private String CHQTYP;//支票类型        X(1)
    private String CHQNUM;//收款行行号(12位大额支付行号)    X(12)
    private String CHQPWD;//支票密码        X(10)
    private String FUNCDE;//保留项         X(1)
    private String ADVNUM;//FS流水号       X(16)
    private String REQNUM;//交易流水号       X(16)
}
