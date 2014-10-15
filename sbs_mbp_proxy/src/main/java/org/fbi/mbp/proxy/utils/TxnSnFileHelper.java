package org.fbi.mbp.proxy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanrui on 2014/10/8.
 * ��ˮ���ļ�����
 */
public class TxnSnFileHelper {
    private static Logger logger = LoggerFactory.getLogger(TxnSnFileHelper.class);

    public synchronized static boolean isRepeatSn(String fileName, String sn) {
        if (fileName == null || "".equals(fileName)) {
            throw new IllegalArgumentException("�ļ�������Ϊ��.");
        }

        boolean isFound = false;
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(":");
                if (sn.equals(fields[0])) {
                    isFound = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
            //TODO
//            logger.error("�ļ�������", e);
//            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("�ļ��������", e);
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("�ļ��������", e);
                }
            }
        }
        return isFound;
    }

    public synchronized static List<String> readFileByLines(String fileName) {
        List<String> lines = new ArrayList<>();
        if (fileName == null || "".equals(fileName)) {
            throw new IllegalArgumentException("�ļ�������Ϊ��.");
        }
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            logger.error("�ļ�������", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("�ļ��������", e);
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("�ļ��������", e);
                }
            }
        }
        return lines;
    }

    public synchronized static boolean writeFileIsAppend(String fileName, String content) {
        if (fileName == null || "".equals(fileName)) {
            throw new IllegalArgumentException("�ļ�������Ϊ��.");
        }

        //�������кŰ�ʱ��ת��
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            logger.error("Sleep error", e);
        }

        boolean flag = false;
        OutputStreamWriter osw = null;

        try {
            osw = new OutputStreamWriter(new FileOutputStream(fileName, true));
        } catch (Exception e) {
            flag = false;
            logger.error("�ļ��������", e);
            throw new RuntimeException(e);
        }

        if (osw != null) {
            BufferedWriter bw = new BufferedWriter(osw);
            try {
                if (content != null && !"".equals(content)) {
                    bw.write(content);
                    bw.newLine();
                    flag = true;
                }
            } catch (IOException e) {
                flag = false;
                logger.error("�ļ��������", e);
                throw new RuntimeException(e);
            } finally {
                try {
                    bw.close();
                    osw.close();
                } catch (IOException e) {
                    flag = false;
                    logger.error("�ļ��������", e);
                }
            }
        }
        return flag;
    }
}
