package demo01;


import java.nio.ByteBuffer;

public class NioTest6 {

    public static void main(String[] args) throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        // 起始位置指向6
        buffer.position(2);
        buffer.limit(6);

        // 创建一个新的buffer，其实是创建了一个引用
        ByteBuffer slice = buffer.slice();

        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 2;
            slice.put(i, b);
        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        // 因为你position已经修改了，所以这里不能用flip
        //buffer.flip();

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

    }

}
