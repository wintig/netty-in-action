package demo01;

import java.nio.ByteBuffer;

/**
 * 只读BUFFER
 */
public class NioTest7 {

    public static void main(String[] args) throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        // 创建只读buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(readOnlyBuffer);


    }

}
