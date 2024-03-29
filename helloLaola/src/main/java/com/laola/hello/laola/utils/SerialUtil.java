package com.laola.hello.laola.utils;

import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import gnu.io.*;

public class SerialUtil extends Thread implements SerialPortEventListener { // SerialPortEventListener
    // 监听器,我的理解是独立开辟一个线程监听串口数据
    static CommPortIdentifier portId; // 串口通信管理类
    static Enumeration<?> portList; // 有效连接上的端口的枚举
    InputStream inputStream; // 从串口来的输入流
    static OutputStream outputStream;// 向串口输出的流
    static SerialPort serialPort; // 串口的引用
    // 堵塞队列用来存放读到的数据
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

    private volatile boolean shutdownRequested = false;

    @Override
    /**
     * SerialPort EventListene 的方法,持续监听端口上是否有数据流
     */
    public void serialEvent(SerialPortEvent event) {//

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
            case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据时读取数据
                byte[] readBuffer = new byte[8];
                try {
                    int numBytes = -1;
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(readBuffer);

                        if (numBytes > 0) {
                            msgQueue.add(new Date() + "真实收到的数据为：-----"
                                    + DataUtils.bytes2hexStr(readBuffer));
                            System.out.println(readBuffer.length);
                            System.out.println(DataUtils.bytes2hexStr(readBuffer) + "///");
                            serialPort.notifyOnDataAvailable(false);
                            serialPort.removeEventListener();
                            serialPort.close();
                            readBuffer = new byte[16];// 重新构造缓冲对象，否则有可能会影响接下来接收的数据
                        } else {
                            msgQueue.add("额------没有读到数据");
                        }
                    }
                } catch (IOException e) {
                }
                break;
        }
    }

    /**
     * 通过程序打开COM4串口，设置监听器以及相关的参数
     *
     * @return 返回1 表示端口打开成功，返回 0表示端口打开失败
     */
    public int startComPort() {
        // 通过串口通信管理类获得当前连接上的串口列表
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {

            // 获取相应串口对象
            portId = (CommPortIdentifier) portList.nextElement();

            System.out.println("设备类型：--->" + portId.getPortType());
            System.out.println("设备名称：---->" + portId.getName());
            // 判断端口类型是否为串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 判断如果COM4串口存在，就打开该串口
                if (portId.getName().equals("COM2")) {
                    try {
                        // 打开串口名字为COM_4(名字任意),延迟为2毫秒
                        serialPort = (SerialPort) portId.open("COM_3", 2000);
                    } catch (PortInUseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    // 设置当前串口的输入输出流
                    try {
                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    // 给当前串口添加一个监听器
                    try {
                        serialPort.addEventListener(this);
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    // 设置监听器生效，即：当有数据时通知
                    serialPort.notifyOnDataAvailable(true);

                    // 设置串口的一些读写参数
                    try {
                        // 比特率、数据位、停止位、奇偶校验位
                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        e.printStackTrace();
                        return 0;
                    }

                    return 1;
                }
            }
        }
        return 0;
    }

    void doShutDown() throws InterruptedException {
        shutdownRequested = true;
        interrupt();
    }

    @Override
    public void run() {
        //判断是否中断

        try {
            doShutDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                System.out.println("--------------任务处理线程运行了--------------");
                while (true) {
                    // 如果堵塞队列中存在数据就将其输出
                    if (msgQueue.size() > 0) {
                        System.out.println(msgQueue.take()
                        );
                        serialPort.close();
                    }

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public String init(String command) {
        SerialUtil cRead = new SerialUtil();
        int i = cRead.startComPort();
        if (true) {
            // 启动线程来处理收到的数据
            try {
                byte[] bytes = DataUtils.hexStrToBinaryStr(command);
                System.out.println("发出字节数：" + command.getBytes("gbk").length);
                outputStream.write(bytes, 0,
                        bytes.length);
                doShutDown();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "100";
        } else {
            return "200";
        }
    }


    public static void main(String[] args) {
        SerialUtil cRead = new SerialUtil();
        int i = cRead.startComPort();
        if (true) {
            // 启动线程来处理收到的数据
            try {
                String st = "E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00";
                byte[] bytes = DataUtils.hexStrToBinaryStr(st);
                System.out.println("发出字节数：" + st.getBytes("gbk").length);
                outputStream.write(bytes, 0,
                        bytes.length);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            return;
        }
    }


    public OutputStream getOutputStream() {
        return outputStream;
    }
}