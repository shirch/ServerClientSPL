package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.JSON.User;
import bgu.spl181.net.impl.UserService;

import java.util.Objects;

public class LoginCommand extends CommandImpl {
    private String username;
    private String password;
    private UserService service;
    public LoginCommand(String _username,String _password,UserService _service)
    {
        username = _username;
        password = _password;
        service=_service;
    }

    public String handle(){
        User user= service.getUser(username);
        if((user!=null)) {
            if(Objects.equals(user.getPassword(),this.password))
            {
                return "ACK login succeeded";
            }
        }
        return "ERROR login failed";
    }
}
