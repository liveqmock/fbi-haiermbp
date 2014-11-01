// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ServerResourceBundle_zh.java

package org.fbi.ctgserver.sbsctgserver;

import java.util.ListResourceBundle;

public class ServerResourceBundle_zh extends ListResourceBundle
{

	public static final String CLASS_VERSION = "@(#) java/com/ibm/ctg/server/ServerResourceBundle.java, client_java, c000 1.92 04/09/14 01:10:59";
	private static final String COPYRIGHT_NOTICE = "(c) Copyright IBM Corporation 2003,2004.";
	static final Object contents[][] = {
		{
			"65XX", "CTG65XXW: 无法找到消息 {0}。"
		}, {
			"ctgtitle", "CICS Transaction Gateway V6.0 02. 构建级别 {0}。"
		}, {
			"ctgcopyright", "(C) Copyright IBM Corporation 1999, 2004. All rights reserved."
		}, {
			"6400", "CTG6400I CICS Transaction Gateway 正在启动"
		}, {
			"6502", "CTG6502I 初始 ConnectionManager 数 = {0}，最大 ConnectionManager 数 = {1}"
		}, {
			"6526", "CTG6526I 初始 Worker 数 = {0}，最大 Worker 数 = {1}"
		}, {
			"6505", "CTG6505I 成功创建了初始 ConnectionManager 和 Worker 线程"
		}, {
			"6506", "CTG6506I 客户机已连接。[{0}] - {1}"
		}, {
			"6507", "CTG6507I 客户机已断开连接。[{0}] - {1}。原因 = {2}"
		}, {
			"6508", "CTG6508I 关闭 Gateway 守护程序类型"
		}, {
			"6509", "CTG6509I 立即关闭 {0} 启动的 Gateway 守护程序"
		}, {
			"6510", "CTG6510I 不会显示 TCP/IP 主机名"
		}, {
			"6511", "CTG6511I Gateway 守护程序已关闭"
		}, {
			"6512", "CTG6512I CICS Transaction Gateway 初始化已完成"
		}, {
			"6513", "CTG6513E CICS Transaction Gateway 无法初始化"
		}, {
			"6490", "CTG6490I 正常关闭{0}启动的 Gateway 守护程序"
		}, {
			"6491", "CTG6491I 活动的客户机数为 {0}"
		}, {
			"6492", "CTG6492I 请求立即关闭的活动的连接数为 {0}"
		}, {
			"6493", "CTG6493I    Q 或 - 用于正常关闭"
		}, {
			"6494", "CTG6494I    I 用于立即关闭"
		}, {
			"6524", "CTG6524I 为 {0}（端口 {1} 上的协议）成功启动了处理程序"
		}, {
			"6525", "CTG6525E 无法为 {0}（协议）启动处理程序。[{1}]"
		}, {
			"6499", "CTG6499E CICS Transaction Gateway 不支持 SystemSSL"
		}, {
			"6533", "CTG6533E 无法读取配置文件。[{0}]"
		}, {
			"6536", "CTG6536I 不会允许一般 ECI 应答调用"
		}, {
			"6537", "CTG6537I 所有协议上的所有连接都将需要安全类"
		}, {
			"6542", "CTG6542E CICS Transaction Gateway 不支持用于远程管理的 TCPAdmin"
		}, {
			"6543", "CTG6543E 为 {0} 参数指定的值无效。"
		}, {
			"6544", "CTG6544E 端口 {0} 无效"
		}, {
			"6545", "CTG6545E [{0}] {1}:EXCI 出错。函数调用 = {2}，响应 = {3}，原因 = {4}，次因字段-1 = {5}，次因字段-2 = {6}，CTG Rc = {7}"
		}, {
			"6546", "CTG6546E EXCI SVC 选项和 CICS SVC 选项之间可能不匹配"
		}, {
			"6547", "CTG6547W Gateway 守护程序将显示消息中用符号表示的 TCP/IP 主机名"
		}, {
			"6550", "CTG6550E 无法侦听请求的端口"
		}, {
			"6551", "CTG6551E 无法创建请求的 ConnectionManager 和 Worker 线程"
		}, {
			"6553", "CTG6553E 读取请求时出错。[{0}]"
		}, {
			"6554", "CTG6554E 本机方法出错。[{0}]"
		}, {
			"6555", "CTG6555E 写应答时出错。[{0}]"
		}, {
			"6556", "CTG6556E 在本地 Gateway 上复制请求时出错。[{0}]"
		}, {
			"6558", "CTG6558E 协议处理程序未成功启动"
		}, {
			"6561", "CTG6561E 无法使用 {0} 类来为 {1} 提供安全性"
		}, {
			"6488", "CTG6488E 请求被拒绝。Gateway 守护程序正在关闭"
		}, {
			"6563", "CTG6563E {0} 协议处理程序意外退出。[{1}]"
		}, {
			"6564", "CTG6564E CICSCONV － CICS Transaction Gateway 初始化文件转换实用程序"
		}, {
			"6565", "CTG6565E 命令选项为："
		}, {
			"6566", "CTG6566E     /g=filename   － Gateway 属性文件"
		}, {
			"6567", "CTG6567E     /o=filename   － 转换的 INI 文件"
		}, {
			"6568", "CTG6568E 发出 cicsconv /? 或 /h 以列出命令选项"
		}, {
			"6569", "CTG6569E 无法打开文件 {0}"
		}, {
			"6570", "CTG6570I 正在处理文件 {0}"
		}, {
			"6571", "CTG6571I 正在创建文件 {0}"
		}, {
			"6572", "CTG6572I 正在将文件 {0} 重命名为 {1}"
		}, {
			"6573", "CTG6573I 处理已完成"
		}, {
			"6574", "CTG6574I 连接记录已禁用"
		}, {
			"6576", "CTG6576I   -truncationsize=<number> － 跟踪数据块的最大大小（以字节为单位）"
		}, {
			"6577", "CTG6577I Java 版本是 {0}"
		}, {
			"6548", "CTG6548I 此命令将启动 CICS Transaction Gateway。"
		}, {
			"6549", "CTG6549I 可以指定以下命令行选项。"
		}, {
			"6575", "CTG6575I   -port=<port_number>      － tcp 协议的 TCP/IP 端口"
		}, {
			"6594", "CTG6594I   -adminport=<port_number> － 用于本地管理的 TCP/IP 端口"
		}, {
			"6583", "CTG6583I   -initconnect=<number>    － 初始 ConnectionManager 线程数"
		}, {
			"6584", "CTG6584I   -maxconnect=<number>     － 最大 ConnectionManager 线程数"
		}, {
			"6585", "CTG6585I   -initworker=<number>     － 初始 Worker 线程数"
		}, {
			"6586", "CTG6586I   -maxworker=<number>      － 最大 Worker 线程数"
		}, {
			"6587", "CTG6587I   -trace                   － 启用标准跟踪"
		}, {
			"6588", "CTG6588I   -noinput                 － 禁止从控制台读取输入"
		}, {
			"6599", "CTG6599I   -quiet                   － 禁止从控制台读取或写入控制台"
		}, {
			"6589", "CTG6589W CICS Transaction Gateway for z/OS 不支持 adminport 参数。它已被忽略"
		}, {
			"6590", "CTG6590I   -dnsnames                － 使用 DNS 来显示用符号表示的 TCP/IP 主机名"
		}, {
			"6591", "CTG6591I 命令行选项覆盖 ctg.ini 中的那些选项"
		}, {
			"6595", "CTG6595I   -tfile=<filename>        － 跟踪文件名"
		}, {
			"6596", "CTG6596I   -x                       － 启用全部详细跟踪"
		}, {
			"6597", "CTG6597I "
		}, {
			"6598", "CTG6598I   -tfilesize=<number>      － 最大跟踪文件大小（以千字节为单位）"
		}, {
			"6578", "CTG6578I   -dumpoffset=<number>     － 启动跟踪输出的数据中的字节偏移量"
		}, {
			"6579", "CTG6579I   -stack                   － 打开异常堆栈跟踪"
		}, {
			"8400", "CTG8400I 正在使用配置文件 {0}。"
		}, {
			"6999", "CTG6999E 无法打开配置文件 {0}。[{1}]"
		}, {
			"6580", "CTG6580E 配置文件中的参数 {0} 不可识别或无效"
		}, {
			"6581", "CTG6581I Gateway 守护程序无法继续执行。"
		}, {
			"6582", "CTG6582E 命令行选项 {0} 未知。"
		}, {
			"8401", "CTG8401I 已启用以下加密器："
		}, {
			"8404", "CTG8404I JSSE 版本未知。"
		}, {
			"8405", "CTG8405I JSSE 提供者信息：{0}"
		}, {
			"8816", "CTG8816I   -j<argument>             － 要传递给 JVM 的参数"
		}, {
			"6498", "CTG6498W 未使用提供的密码来访问 ESM 密钥环"
		}, {
			"8420", "CTG8420E CICS Transaction Gateway 不支持 HTTP 协议"
		}, {
			"6592", "CTG6592W “CTG.INI”和“ctg.ini”配置文件都存在，正在使用缺省文件“ctg.ini”。"
		}, {
			"6593", "CTG6593I ctgstart <options> [<-j>JVMArg1 <-j>JVMArg2...]"
		}, {
			"8410", "CTG8410E {0} 日志被配置给未知目标"
		}, {
			"8411", "CTG8411E z/OS 不支持记录到文件"
		}, {
			"8412", "CTG8412E {1} 的 {0} 日志缺少参数"
		}, {
			"8413", "CTG8413E {1} 的 {0} 日志具有无效参数"
		}, {
			"8414", "CTG8414E {0} 日志无法写入文件：{1}"
		}, {
			"8415", "CTG8415W 文件的日志有不匹配的参数：{0}。正在使用最大值（{1}）继续执行"
		}, {
			"8416", "CTG8416W 日志流 {0} 未知"
		}, {
			"6496", "CTG6496W 不推荐使用 128bitonly。"
		}, {
			"6495", "CTG6495E 没有可供使用的密码套件。"
		}, {
			"6497", "CTG6497W 密码套件 {0} 不可用。"
		}, {
			"6489", "CTG6489W 128bitonly 无法与其它密码套件联合使用"
		}, {
			"idle", "超过了空闲超时期"
		}, {
			"notresp", "Java 客户机应用程序未响应"
		}, {
			"nohandshk", "JavaGateway 安全性是必需的，但握手已关闭"
		}, {
			"cliclose", "Java 客户机应用程序关闭了此连接"
		}, {
			"console", "控制台"
		}, {
			"ctgadmin", "CTGAdmin"
		}, {
			"zos", "z/OS 操作员"
		}, {
			"log_info", "信息"
		}, {
			"log_error", "错误"
		}, {
			"log_file", "文件"
		}, {
			"log_console", "控制台"
		}
	};

	public ServerResourceBundle_zh()
	{
	}

	public Object[][] getContents()
	{
		return contents;
	}

}
