package org.fbi.ctgproxy;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by zhanrui on 2014/10/26.
 */
public class CtgEciRequest extends CtgRequest {
    public int Call_Type;
    public int Extend_Mode;
    public int Luw_Token;
    public int Message_Qualifier;
    public String Server;
    public String Userid;
    public String Password;
    public String Program;
    public String Transid;
    public int Commarea_Length;
    public byte Commarea[];

    public void readObject(DataInputStream dis) throws IOException {
        Call_Type = dis.readInt();
        switch (Call_Type) {
            default:
                break;

            case 9: // '\t'
                break;

            case 10: // '\n'
            case 11: // '\013'
            case 1: // '\001'
            case 2: // '\002'
            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
            case 7: // '\007'
            case 8: // '\b'
            case 12: // '\f'
            case 13: // '\r'
                readObjectV2(dis);
                break;
        }
    }

    private void readObjectV2(DataInputStream dis) throws IOException {
        Extend_Mode = dis.readInt();
        Luw_Token = dis.readInt();
        Message_Qualifier = dis.readInt();
        boolean bCallbackExists = dis.readBoolean();
        Server = readPaddedString(dis, 8);
        Userid = readPaddedString(dis, 16);
        //Password = readPaddedString(datainputstream, 16, true);
        Password = readPaddedString(dis, 16);
        Program = readPaddedString(dis, 8);
        Transid = readPaddedString(dis, 4);
        Commarea_Length = dis.readInt();
        if (Commarea_Length <= 0) {
            Commarea_Length = 0;
            Commarea = null;
        } else {
            Commarea = new byte[Commarea_Length];
        }
        boolean flag1 = dis.readBoolean();  //Outbound
        boolean flag2 = dis.readBoolean();  //inbound

        if (Commarea_Length > 0) {
            dis.readFully(Commarea);
        }
    }

}
