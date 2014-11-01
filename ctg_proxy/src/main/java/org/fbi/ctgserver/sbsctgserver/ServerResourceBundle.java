// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ServerResourceBundle.java

package org.fbi.ctgserver.sbsctgserver;

import java.util.ListResourceBundle;

public class ServerResourceBundle extends ListResourceBundle
{

	public static final String CLASS_VERSION = "@(#) java/com/ibm/ctg/server/ServerResourceBundle.java, client_java, c602, c602-20060418 1.93.3.2 06/02/10 14:54:18";
	private static final String COPYRIGHT_NOTICE = "(c) Copyright IBM Corporation 2003,2006.";
	static final Object contents[][] = {
		{
			"65XX", "CTG65XXW: Unable to find message {0}."
		}, {
			"ctgtitle", "CICS Transaction Gateway, Version 6.0 02. Build Level {0}."
		}, {
			"ctgcopyright", "(C) Copyright IBM Corporation 1999, 2006. All rights reserved."
		}, {
			"6400", "CTG6400I CICS Transaction Gateway is starting"
		}, {
			"6502", "CTG6502I Initial ConnectionManagers = {0}, Maximum ConnectionManagers = {1}"
		}, {
			"6526", "CTG6526I Initial Workers = {0}, Maximum Workers = {1}"
		}, {
			"6505", "CTG6505I Successfully created initial ConnectionManager and Worker threads"
		}, {
			"6506", "CTG6506I Client connected. [{0}] - {1}"
		}, {
			"6507", "CTG6507I Client disconnected. [{0}] - {1}. Reason = {2}"
		}, {
			"6508", "CTG6508I To shut down the Gateway daemon type"
		}, {
			"6509", "CTG6509I Immediate shutdown of Gateway daemon started by {0}"
		}, {
			"6510", "CTG6510I TCP/IP hostnames will not be shown"
		}, {
			"6511", "CTG6511I Gateway daemon has shut down"
		}, {
			"6512", "CTG6512I CICS Transaction Gateway initialization complete"
		}, {
			"6513", "CTG6513E CICS Transaction Gateway failed to initialize"
		}, {
			"6490", "CTG6490I Normal shutdown of Gateway daemon started by {0}"
		}, {
			"6491", "CTG6491I Number of active clients is {0}"
		}, {
			"6492", "CTG6492I Number of active connections at immediate shutdown request was {0}"
		}, {
			"6493", "CTG6493I    Q or - for normal shutdown"
		}, {
			"6494", "CTG6494I    I for immediate shutdown"
		}, {
			"6524", "CTG6524I Successfully started handler for the {0}: protocol on port {1}"
		}, {
			"6525", "CTG6525E Unable to start handler for the {0}: protocol. [{1}]"
		}, {
			"6499", "CTG6499E The CICS Transaction Gateway does not support SystemSSL"
		}, {
			"6533", "CTG6533E Unable to read the configuration file. [{0}]"
		}, {
			"6536", "CTG6536I Generic ECI reply calls will not be allowed"
		}, {
			"6537", "CTG6537I Security classes will required for all connections on all protocols"
		}, {
			"6542", "CTG6542E CICS Transaction Gateway does not support TCPAdmin for remote administration"
		}, {
			"6543", "CTG6543E The value specified for the {0} parameter is invalid."
		}, {
			"6544", "CTG6544E Port {0} is invalid"
		}, {
			"6545", "CTG6545E [{0}] {1}:EXCI error. Function Call = {2}, Response = {3}, Reason = {4}, Subreason field-1 = {5}, subreason field-2 = {6} CTG Rc = {7}"
		}, {
			"6546", "CTG6546E Possible mismatch between EXCI SVC options and CICS SVC options"
		}, {
			"6547", "CTG6547W Gateway daemon will display symbolic TCP/IP hostnames in messages"
		}, {
			"6550", "CTG6550E Unable to listen on requested port"
		}, {
			"6551", "CTG6551E Unable to create requested ConnectionManager and Worker threads"
		}, {
			"6553", "CTG6553E Error reading request. [{0}]"
		}, {
			"6554", "CTG6554E Error in native method. [{0}]"
		}, {
			"6555", "CTG6555E Error writing reply. [{0}]"
		}, {
			"6556", "CTG6556E Error copying request on local Gateway. [{0}]"
		}, {
			"6558", "CTG6558E No protocol handlers started successfully"
		}, {
			"6561", "CTG6561E Unable to use {0} class to provide security to {1}"
		}, {
			"6488", "CTG6488E Request rejected. Gateway daemon is shutting down"
		}, {
			"6563", "CTG6563E {0} protocol handler exited unexpectedly. [{1}]"
		}, {
			"6569", "CTG6569E Unable to open file {0}"
		}, {
			"6570", "CTG6570I Processing file {0}"
		}, {
			"6571", "CTG6571I Creating file {0}"
		}, {
			"6572", "CTG6572I Renaming file {0} to {1}"
		}, {
			"6573", "CTG6573I Processing complete"
		}, {
			"6574", "CTG6574I Connection logging has been disabled"
		}, {
			"6576", "CTG6576I   -truncationsize=<number> - Maximum size of trace data blocks in bytes"
		}, {
			"6577", "CTG6577I Java version is {0}"
		}, {
			"6548", "CTG6548I This command starts the CICS Transaction Gateway."
		}, {
			"6549", "CTG6549I The following command line options can be specified."
		}, {
			"6575", "CTG6575I   -port=<port_number>      - TCP/IP port for tcp protocol"
		}, {
			"6594", "CTG6594I   -adminport=<port_number> - TCP/IP port for local administration"
		}, {
			"6583", "CTG6583I   -initconnect=<number>    - Initial number of ConnectionManager threads"
		}, {
			"6584", "CTG6584I   -maxconnect=<number>     - Maximum number of ConnectionManager threads"
		}, {
			"6585", "CTG6585I   -initworker=<number>     - Initial number of Worker threads"
		}, {
			"6586", "CTG6586I   -maxworker=<number>      - Maximum number of Worker threads"
		}, {
			"6587", "CTG6587I   -trace                   - Enable standard trace"
		}, {
			"6588", "CTG6588I   -noinput                 - Disable reading of input from the console"
		}, {
			"6599", "CTG6599I   -quiet                   - Disable reading/writing to/from console"
		}, {
			"6589", "CTG6589W CICS Transaction Gateway for z/OS does not support the adminport parameter. It has been ignored"
		}, {
			"6590", "CTG6590I   -dnsnames                - Use DNS to display symbolic TCP/IP hostnames"
		}, {
			"6591", "CTG6591I Command line options override those in ctg.ini"
		}, {
			"6595", "CTG6595I   -tfile=<filename>        - Trace file name"
		}, {
			"6596", "CTG6596I   -x                       - Enable full detailed trace"
		}, {
			"6597", "CTG6597I "
		}, {
			"6598", "CTG6598I   -tfilesize=<number>      - Maximum trace file size in kilobytes"
		}, {
			"6578", "CTG6578I   -dumpoffset=<number>     - Byte offset in data to start trace output"
		}, {
			"6579", "CTG6579I   -stack                   - Switch on exception stack trace"
		}, {
			"8400", "CTG8400I Using configuration file {0}."
		}, {
			"6999", "CTG6999E Unable to open configuration file {0}. [{1}]"
		}, {
			"6580", "CTG6580E The parameter {0} in the configuration file is unrecognised or invalid"
		}, {
			"6581", "CTG6581I The Gateway daemon cannot continue."
		}, {
			"6582", "CTG6582E The command line option {0} is unknown."
		}, {
			"8401", "CTG8401I The following ciphers are enabled: "
		}, {
			"8404", "CTG8404I JSSE version unknown. "
		}, {
			"8405", "CTG8405I JSSE provider info: {0} "
		}, {
			"8816", "CTG8816I   -j<argument>             - argument to pass to the JVM"
		}, {
			"6498", "CTG6498W Provided password was not used to access ESM keyring"
		}, {
			"8420", "CTG8420E The CICS Transaction Gateway does not support HTTP protocols"
		}, {
			"6592", "CTG6592W Both 'CTG.INI' and 'ctg.ini' configuration files exist, using default file 'ctg.ini'."
		}, {
			"6593", "CTG6593I ctgstart <options> [<-j>JVMArg1 <-j>JVMArg2...]"
		}, {
			"8410", "CTG8410E {0} log is configured to an unknown destination"
		}, {
			"8411", "CTG8411E Logging to file is not supported on z/OS"
		}, {
			"8412", "CTG8412E {0} log to {1} has missing parameters"
		}, {
			"8413", "CTG8413E {0} log to {1} has invalid parameters"
		}, {
			"8414", "CTG8414E {0} log unable to write to file: {1}"
		}, {
			"8415", "CTG8415W Log to file has mismatched parameter: {0}. Using largest value ({1}) to continue"
		}, {
			"8416", "CTG8416W Unknown log stream: {0}"
		}, {
			"6496", "CTG6496W The use of 128bitonly has been deprecated."
		}, {
			"6495", "CTG6495E No cipher suites available for use."
		}, {
			"6497", "CTG6497W Cipher suite {0} unavailable."
		}, {
			"6489", "CTG6489W 128bitonly cannot be used in conjuction with other cipher suites"
		}, {
			"idle", "idle timeout period exceeded"
		}, {
			"notresp", "Java Client application not responding"
		}, {
			"nohandshk", "JavaGateway security is required but handshaking is turned off"
		}, {
			"cliclose", "Java Client application closed the connection"
		}, {
			"console", "Console"
		}, {
			"ctgadmin", "CTGAdmin"
		}, {
			"zos", "z/OS Operator"
		}, {
			"log_info", "Information"
		}, {
			"log_error", "Error"
		}, {
			"log_file", "file"
		}, {
			"log_console", "console"
		}, {
			"up-level", "Java Client application at a higher version"
		}
	};

	public ServerResourceBundle()
	{
	}

	public Object[][] getContents()
	{
		return contents;
	}

}
