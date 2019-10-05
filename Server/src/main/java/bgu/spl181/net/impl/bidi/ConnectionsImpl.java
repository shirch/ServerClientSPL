package bgu.spl181.net.impl.bidi;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.util.HashMap;

public class ConnectionsImpl<T> implements Connections<T> {
    private HashMap<Integer,ConnectionHandler> clients = new HashMap<Integer, ConnectionHandler>();
    private  int connectionId=0;
    @Override
    public boolean send(int connectionId, T msg){
        ConnectionHandler conHandler = clients.get(connectionId);
        if(conHandler!=null){
            conHandler.send(msg);
            return true;
        }
        return false;
    }
    @Override
    public void broadcast(T msg){
        for(Integer key:clients.keySet())
        {
            clients.get(key).send(msg);
        }
    }
    @Override
    public void disconnect(int connectionId){
        if (clients.containsKey(connectionId)) {
            clients.remove(connectionId);
        }
    }

    public  int connect(ConnectionHandler<T> conHandler)
    {
        clients.put(++connectionId,conHandler);
        return connectionId;
    }

}
