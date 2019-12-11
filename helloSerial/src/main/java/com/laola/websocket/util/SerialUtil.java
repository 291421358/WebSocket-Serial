package com.laola.websocket.util;

import gnu.io.*;
import java.io.*;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * 第一次学习时从别人处拷贝来的代码，并不能实现功能，存在bug 后期修复
 * 故没有加注解 可delete
 * TODO
 */
public class SerialUtil implements SerialPortEventListener {

    protected static CommPortIdentifier portid = null;  //CommPortIdentifier通讯端口标识符
    protected static SerialPort comPort = null;         //SerialPort串行端口
    protected int BAUD = 115200;  //波特率
    protected int DATABITS = SerialPort.DATABITS_8;
    ;  //数据位
    protected int STOPBITS = SerialPort.STOPBITS_1;  //停止位
    protected int PARITY = SerialPort.PARITY_NONE;  //奇偶检验
    public static OutputStream oStream;    //输出流
    public static InputStream iStream;     //输入流

    /**
     * 读取所有串口名字
     */
    private void listPortChoices() {
        CommPortIdentifier portId;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        // iterate through the ports.
        while (en.hasMoreElements()) {
            portId = (CommPortIdentifier) en.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println(portId.getName());
            }
        }
    }

    /**
     * 设置串口号
     *
     * @param
     * @return
     */
    public void setSerialPortNumber() {

        String osName = null;
        String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.startsWith("windows")) {
            // windows
            osName = "COM3";
        } else if (osname.startsWith("linux")) {
            // linux
            osName = "/dev/ttyS1";
        }
        System.out.println(osName);
        try {
            portid = CommPortIdentifier.getPortIdentifier(osName);
            // portid = CommPortIdentifier.getPortIdentifier(Port);
            if (portid.isCurrentlyOwned()) {
                System.out.println("端口在使用");
            } else {
                comPort = (SerialPort) portid.open(this.getClass().getName(), 1000);
            }
        } catch (PortInUseException e) {
            System.out.println("端口被占用");
            e.printStackTrace();

        } catch (NoSuchPortException e) {
            System.out.println("端口不存在");
            e.printStackTrace();
        }

        try {
            iStream = comPort.getInputStream(); //从COM1获取数据
            oStream = comPort.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            comPort.addEventListener(this);       //给当前串口增加一个监听器
            comPort.notifyOnDataAvailable(true);  //当有数据是通知
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }

        try {
            //设置串口参数依次为(波特率,数据位,停止位,奇偶检验)
            comPort.setSerialPortParams(this.BAUD, this.DATABITS, this.STOPBITS, this.PARITY);
        } catch (UnsupportedCommOperationException e) {
            System.out.println("端口操作命令不支持");
            e.printStackTrace();
        }
    }


    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据时读取数据,并且给串口返回数据
                //进来后 就不是第一次了
                //TODO
                break;
        }
    }
}