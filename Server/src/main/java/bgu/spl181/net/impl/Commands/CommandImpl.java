package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.rci.Command;
import bgu.spl181.net.srv.ActorThreadPool;

import java.io.Serializable;

public abstract class CommandImpl<T> implements Command<T> {

    public Serializable execute(T arg){
        return "";
    }

    protected abstract String handle();
}
