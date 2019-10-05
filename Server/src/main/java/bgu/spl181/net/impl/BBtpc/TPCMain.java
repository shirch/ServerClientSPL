package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.impl.JSON.ConvertJSON;
import bgu.spl181.net.impl.JSON.Movie;
import bgu.spl181.net.impl.JSON.User;
import bgu.spl181.net.impl.UserService;
import bgu.spl181.net.impl.bidi.BidiMessagingProtocolImpl;
import bgu.spl181.net.impl.bidi.MessagingEncoderDecoderImpl;
import bgu.spl181.net.srv.ActorThreadPool;
import bgu.spl181.net.srv.Server;
import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TPCMain {

    public static void main(String[] args)
    {
        ActorThreadPool pool = new ActorThreadPool(7);

        ReadWriteLock lockUser = new ReentrantReadWriteLock();
        ReadWriteLock lockMovie = new ReentrantReadWriteLock();
        UserService service = new UserService(System.getProperty("user.dir")+"//Database",lockUser,lockMovie);




        Server.threadPerClient(Integer.parseInt(args[0]),()-> new BidiMessagingProtocolImpl(service),()-> new MessagingEncoderDecoderImpl()).serve();

    }
}
