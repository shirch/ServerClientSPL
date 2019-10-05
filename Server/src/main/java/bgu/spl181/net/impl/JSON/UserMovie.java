package bgu.spl181.net.impl.JSON;

public class UserMovie {
    private  int id;
    private  String name;

    public UserMovie(int _id,String _name)
    {
        id=_id;
        name=_name;
    }
    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
}
