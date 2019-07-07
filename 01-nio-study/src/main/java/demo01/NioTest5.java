package demo01;


import java.nio.ByteBuffer;

/**
 * 类型化的get和put
 */
public class NioTest5 {

    public static void main(String[] args) throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(1);
        buffer.putShort((short)2);
        buffer.putChar('时');
        buffer.putLong(1212312312L);
        buffer.putDouble(123123.123D);
        buffer.putChar('天');

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getChar());

    }

}
