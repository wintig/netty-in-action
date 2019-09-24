package demo01;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class NioTest13_编码解码 {


    public static void main(String[] args) throws IOException {

        String inputFile = "NioTest13_In.txt";
        String outputFile = "NioTest13_Out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile, "rw");

        // 获取输入文件的长度
        long inputLengt = new File(inputFile).length();

        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputFileChannel = outputRandomAccessFile.getChannel();

        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLengt);

        // 这里就算携程ios-8859-1也没有问题，因为解码和编码用的同一种格式。相当于A->B 然后B->A最后是什么也没有改变
        Charset charset = Charset.forName("utf-8");

        // 解码 byte -> string
        CharsetDecoder decoder = charset.newDecoder();
        // 编码 string -> byte
        CharsetEncoder encoder = charset.newEncoder();

        CharBuffer charBuffer = decoder.decode(inputData);
        ByteBuffer outputData = encoder.encode(charBuffer);

        outputFileChannel.write(outputData);

        inputRandomAccessFile.close();
        outputRandomAccessFile.close();

    }


}
