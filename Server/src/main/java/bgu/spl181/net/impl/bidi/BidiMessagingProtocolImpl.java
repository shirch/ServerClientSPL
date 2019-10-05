package bgu.spl181.net.impl.bidi;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.Commands.LoginCommand;
import bgu.spl181.net.impl.Commands.RegisterCommand;
import bgu.spl181.net.impl.Commands.RequestCommand;
import bgu.spl181.net.impl.Commands.SignoutCommand;
import bgu.spl181.net.impl.UserService;

import java.util.concurrent.ConcurrentHashMap;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    private  int connectionId;
    private  Connections<String> connections;
    private UserService userService;
    private String username;
    private boolean shouldTerminate = false;
    private ConcurrentHashMap<String,String> loggedIn;

    public BidiMessagingProtocolImpl(UserService _userService)
    {
        userService=_userService;
        loggedIn = new ConcurrentHashMap<String,String>();
    }
    @Override
    public void start(int connectionId, Connections connections)
    {
        this.connections=connections;
        this.connectionId=connectionId;

    }
    private String[] getString(String command,int maxLength) {
        String[] args = new String[maxLength];
        command = command.toString().substring(command.toString().indexOf(" ")+1);
        int i=0;
        while((command.indexOf(" ")!=-1)&&i<(maxLength-1)) {
            args[i]  = command.substring(0,command.indexOf(" "));
            command = command.substring(args[i].length()+1);
            i++;
        }
        args[i]=command;
        i++;
        while(i<maxLength) {
            args[i]=null;
            i++;
        }
        return args;
    }
    @Override
    public void process(T message)
    {
        String server_command="";
        if(message.toString().contains("REGISTER"))
        {
            String[] args = getString(message.toString(),3);
            if(args[2] != null) {
                server_command = (new RegisterCommand(args[0],args[1],args[2],userService)).handle();
            }
            else{
                server_command = "ERROR registration failed";
            }
            if(server_command.contains("ACK"))
            {
                userService.updateUsersJson();
            }
        }
        if(message.toString().contains("LOGIN"))
        {
            if((loggedIn.size()==0)||(loggedIn.size()!=0&&!loggedIn.containsKey(username))){
                String[] args = getString(message.toString(),2);
                if(!loggedIn.contains(args[0]))
                {
                    server_command = (new LoginCommand(args[0],args[1],userService)).handle();
                }
                else{
                    server_command="ERROR login failed";
                }

                if(server_command.contains("ACK"))
                {
                    loggedIn.put(args[0],connectionId+"");
                    username = args[0];
                }
            }
            else
            {
                server_command = "ERROR login failed";
            }
        }
        if(message.toString().contains("SIGNOUT"))
        {
            if(loggedIn.size()>0 && loggedIn.containsKey(username)){
                server_command = (new SignoutCommand(username,userService)).handle();
                if(server_command.contains("ACK"))
                {
                    loggedIn.remove(username);
                }
            }
            else{
                server_command = "ERROR signout failed";
            }
        }
        if(message.toString().contains("REQUEST"))
        {
            if(  loggedIn.size()>0 && loggedIn.containsKey(username)) {
                String[] args = getString(message.toString(), 2);
                server_command = (new RequestCommand(username, args[0], args[1], userService)).handle();
                if (server_command.contains("ACK") && ((server_command.contains("balance") || server_command.contains("added")) || server_command.contains("rent") || server_command.contains("return"))) {
                    userService.updateUsersJson();
                }
                if (server_command.contains("ACK") && (server_command.contains("changeprice") || server_command.contains("rent") || server_command.contains("return")|| server_command.contains("addmovie")|| server_command.contains("remmovie"))) {
                    userService.updateMoviesJson();
                }
            }
            else
            {
                server_command = "ERROR request balance failed";
            }
        }
        if(server_command!="")
        {
            if(server_command.contains("BROADCAST"))
            {
                connections.send(connectionId,server_command.substring(0,server_command.indexOf("BROADCAST")));
                broadcast(server_command.substring(server_command.indexOf("BROADCAST")));
            }
            else
            {
                connections.send(connectionId,server_command);
                if(server_command.contains("ACK signout succeeded")) {
                    connections.disconnect(connectionId);
                }
            }
        }


    }

    private void broadcast(String msg)
    {
        for(String conn:loggedIn.keySet())
        {
            connections.send(Integer.parseInt(loggedIn.get(conn)),msg);
        }
    }

    /**
     * @return true if the connection should be terminated
     */
    @Override
    public boolean shouldTerminate()
    {
        return  false;
    }
}