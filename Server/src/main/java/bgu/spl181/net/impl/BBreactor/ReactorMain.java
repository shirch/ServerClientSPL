package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.impl.UserService;
import bgu.spl181.net.impl.bidi.BidiMessagingProtocolImpl;
import bgu.spl181.net.impl.bidi.MessagingEncoderDecoderImpl;
import bgu.spl181.net.srv.ActorThreadPool;
import bgu.spl181.net.srv.Server;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReactorMain {

    public static void main(String[] args) {
        ActorThreadPool pool = new ActorThreadPool(7);
        ReadWriteLock lockUser = new ReentrantReadWriteLock();
        ReadWriteLock lockMovie = new ReentrantReadWriteLock();
        UserService service = new UserService(System.getProperty("user.dir")+"//Database",lockUser,lockMovie);

        Server.reactor(Runtime.getRuntime().availableProcessors(),Integer.parseInt(args[0]),() -> new BidiMessagingProtocolImpl(service),() -> new MessagingEncoderDecoderImpl()).serve();

    }


}
