package com.laola.hello.laola;

import com.laola.hello.laola.utils.DataUtils;
import com.laola.hello.laola.utils.SerialUtil;
import com.laola.hello.laola.utils.SerialUtilCopy;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.OutputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaolaApplication.class)
public class SerialTest {
    static OutputStream outputStream;
    @Test
    public void e59083(){
        String command = "E5 90 82 00 00 00 00 00 00 00 00 00 00 00 00 00";
        SerialUtil serialUtil = new SerialUtil();
        serialUtil.init(command);
         outputStream = serialUtil.getOutputStream();
        byte[] bytes = DataUtils.hexStrToBinaryStr(command);
        System.out.println("发出：" + command);
        try {
            outputStream.write(bytes, 0,
                    bytes.length);
        }catch (Exception e){

        }

        System.out.println(outputStream);
    }
    /**
     * int i = cRead.startComPort();
     *         if (true) {
     *             // 启动线程来处理收到的数据
     *             try {
     *                 String st = "E5 90 83 01 00 00 00 00 00 00 00 00 00 00 00 00";
     *                 byte[] bytes = DataUtils.hexStrToBinaryStr(st);
     *                 System.out.println("发出：" + st);
     *                 outputStream.write(bytes, 0,
     *                         bytes.length);
     *             } catch (IOException e) {
     *                 // TODO Auto-generated catch block
     *                 e.printStackTrace();
     *             }
     *
     *         } else {
     *             return;
     *         }
     */
}
