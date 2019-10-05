package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.UserService;

public class SignoutCommand extends CommandImpl {
    private String username;
    private UserService service;
    public SignoutCommand(String _username, UserService _service)
    {
        username=_username;
        service=_service;
    }


    public String handle() {
        if (service.getUser(username) != null) {
            return "ACK signout succeeded";
        }
        return "ERROR signout failed";
    }
}
