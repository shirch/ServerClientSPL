package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.UserService;

public class RegisterCommand extends CommandImpl {
    private String username;
    private String password;
    private String datablock;
    private UserService service;


    public RegisterCommand(String _username,String _password,String _datablock,UserService _service)
    {
        username = _username;
        password = _password;
        datablock = _datablock;
        service=_service;
    }

    public String handle(){
        if((service.getUser(username)==null)&&(username!=null)&&(password!=null)) {
            if (datablock.contains("country=")) {
                String country = datablock.substring(datablock.indexOf("=") + 1);
                service.addUser(username, password, country);
                return "ACK registration succeeded";
            }
        }
        return "ERROR registration failed";
    }
}
