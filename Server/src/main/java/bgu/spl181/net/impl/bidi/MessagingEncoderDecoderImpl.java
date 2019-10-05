package bgu.spl181.net.impl.bidi;

import bgu.spl181.net.api.MessageEncoderDecoder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessagingEncoderDecoderImpl implements MessageEncoderDecoder<String> {

    private byte[] buffer = new byte[1024];
    private int length = 0;

    /**
     * add the next byte to the decoding process
     *
     * @param nextByte the next byte to consider for the currently decoded
     * message
     * @return a message if this byte completes one or null if it doesnt.
     */
    @Override
    public String decodeNextByte(byte nextByte){
        if(nextByte=='\n')
        {
            String msg = new String(buffer,0,length, StandardCharsets.UTF_8);
            length=0;
            return msg;
        }
        else
        {
            if(length>=buffer.length){
                buffer = Arrays.copyOf(buffer,length*2);
            }

            buffer[length]= nextByte;
            length++;
            return null;


        }
    }

    /**
     * encodes the given message to bytes array
     *
     * @param message the message to encode
     * @return the encoded bytes
     */
    public byte[] encode(String message){
            return (message+'\n').getBytes(StandardCharsets.UTF_8);
    }

}
