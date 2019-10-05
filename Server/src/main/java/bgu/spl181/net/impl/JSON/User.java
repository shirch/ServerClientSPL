package bgu.spl181.net.impl.JSON;

import bgu.spl181.net.impl.JSON.Movie;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String username;
    private String type;
    private String password;
    private String country;
    private ArrayList<UserMovie> movies;
    private int balance;

    public User(String _user_name,String _type,String _password,String _country,HashMap<Integer, String> _movies , int _balance)
    {
        username=_user_name;
        type=_type;
        password=_password;
        country=_country;
        balance = _balance;
        movies = new ArrayList<UserMovie>();
        for (Integer key:_movies.keySet()) {
            movies.add(new UserMovie(key,_movies.get(key)));
        }
    }

    public void setBalance(int _balance)
    {
        balance=_balance;
    }
    public int getBalance()
    {
        return balance;
    }
    public void rentMovie(Movie movie)
    {
        movies.add(new UserMovie(movie.getId(),movie.getName()));
    }
    public void returnMovie(Movie movie)
    {
        for (UserMovie userMovie: movies) {
            if(userMovie.getName().equalsIgnoreCase(movie.getName()))
            {
                movies.remove(userMovie);
                return;
            }

        }
    }
    public String getPassword()
    {
        return password;
    }
    public boolean rentedMovie(String movieName)
    {
        for (UserMovie userMovie: movies) {
            if(userMovie.getName().equalsIgnoreCase(movieName))
            {
                return  true;
            }

        }
        return  false;
    }
    public  String getCountry(){
        return country;
    }
    public String getType()
    {
        return  type;
    }


}
