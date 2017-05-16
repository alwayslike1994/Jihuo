package com.example.alwayslike.jihuo.bean;

/**
 * Created by alwayslike on 2017/5/12.
 */

public class RequestComm {

    public static final int HEARER_LENGHT = 8;

    /**
     * 帧开始结束
     */
    public static final byte FRAME_STARTEND = 0X7E;

    public static final byte REQUEST_BST5 = (byte) 0x11000000;

    public static final byte REQUEST_VST5 = (byte) 0x11010000;


    public static final byte MAC_CTRL_BST5 = (byte) 0x01011000;

    public static final byte MAC_CTRL_VST5 = (byte) 0x11000000;
}

