package com.example.alwayslike.jihuo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mjh on 2017/5/16.
 */
public class MessageHand {
    static byte headFlag = -1;
    static byte trunFlag = -2;
    static byte headChange = 01;
    static byte trunChange = 00;

    static List<Byte> receiveMessage=new LinkedList<>();

    public byte[] strToBytes(String hexString, byte type) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        //String 转换成char[]
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();

        List<Byte> bytes = new ArrayList<>();
        //添加报文头
        bytes.add(headFlag);
        bytes.add(headFlag);

        //添加类型
        turnAdd(bytes, type);

        //校验与转义
        byte check = 0;
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            byte temp = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            check ^= temp;
            turnAdd(bytes, temp);
        }

        //添加校验
        turnAdd(bytes, check);

        //添加报文尾
        bytes.add(headFlag);
        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }
        return result;
    }

    //转移添加字节
    private void turnAdd(List<Byte> bytes, byte b) {
        if (b == headFlag) {
            bytes.add(trunFlag);
            bytes.add(headChange);
        } else if (b == trunFlag) {
            bytes.add(trunFlag);
            bytes.add(trunChange);
        } else {
            bytes.add(b);
        }
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public Byte byteToStr(StringBuilder result,byte[] blueTooth) {
        for(int i=0;i<blueTooth.length;i++){
            receiveMessage.add(blueTooth[i]);
        }

        for(int i=0;i<receiveMessage.size();i++){
            if(receiveMessage.get(0)!=headFlag){
                receiveMessage.remove(0);
            }
            else if(receiveMessage.get(0)==headFlag&&receiveMessage.get(1)!=headFlag){
                receiveMessage.remove(0);
                receiveMessage.remove(0);
            }
            else {
                break;
            }
        }

        if(receiveMessage.size()<4){
            return null;
        }

        //去掉报文头
        int endIndex=2;
        byte last=0;
        for ( ;endIndex<receiveMessage.size();endIndex++) {
            byte aByte=receiveMessage.get(endIndex);
            last=aByte;
            if(aByte==headFlag){
               break;
            }
        }
        if(last!=headFlag){
            return null;
        }
        List<Byte> resultList=new ArrayList<>();
        //反转译
        for(int i=2;i<endIndex;i++){
            byte temp=receiveMessage.get(i);
            if(temp==trunFlag){
                byte next=receiveMessage.get(0);
                if(i>=endIndex-1&&(next!=0||next!=1)){
                    return byteToStr(result,new byte[] {});
                }
                i++;
                if(next==0){
                    temp=trunFlag;
                }
                else {
                    temp=headFlag;
                }
            }
            resultList.add(temp);
        }
        //校验
        byte type=resultList.get(0);
        byte check=0;
        for (int i = 1; i < resultList.size(); i++) {
            check^=resultList.get(i);
        }
        if(check!=0){
            receiveMessage.remove(0);
            return byteToStr(result,new byte[] {});
        }

        receiveMessage.remove(0);
        receiveMessage.remove(0);
        //转换成字符串
        result.append(bytesToHexString(resultList,1,resultList.size()-2));
        return type;
    }

    private  String bytesToHexString(List<Byte> bytes,int start ,int end) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null ) {
            return null;
        }
        for (int i = start; i <=end; i++) {
            int v = bytes.get(i) & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv.toUpperCase());
        }
        return stringBuilder.toString();
    }

}
