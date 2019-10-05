package bgu.spl181.net.impl;

import bgu.spl181.net.impl.JSON.ConvertJSON;
import bgu.spl181.net.impl.JSON.Movie;
import bgu.spl181.net.impl.JSON.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;

public class UserService {
    private HashMap<Integer,Movie> movies;
    private HashMap<String,User> users;
    private ReadWriteLock lockUser;
    private  ReadWriteLock lockMovie;
    private ConvertJSON json;
    private  String path;

    public UserService(String _path, ReadWriteLock lockUser,ReadWriteLock lockMovie)
    {
        path=_path;
        ConvertJSON json = new ConvertJSON(path);
        movies = json.getMovies();
        users = json.getUsers();
        this.lockMovie=lockMovie;
        this.lockUser=lockUser;
    }

    public User getUser(String username)
    {
        lockUser.readLock().lock();
        if( users.containsKey(username))
        {
            lockUser.readLock().unlock();
            return users.get(username);
        }
        else
        {
            lockUser.readLock().unlock();
            return null;
        }

    }
    public void addUser(String username,String password,String country)
    {
        lockUser.readLock().lock();
        if(!users.containsKey(username))
        {
            User new_user = new User(username,"normal",password,country,new  HashMap<Integer,String>(),0);
            users.put(username,new_user);
            updateUsersJson();
        }
        lockUser.readLock().unlock();

    }
    public void addMovie(String name,String amount,String price, ArrayList<String> banned)
    {
        lockMovie.writeLock().lock();
        ArrayList<String> countries = banned;
        Movie new_movie = new Movie(movies.size()+1,name,Integer.parseInt(price),countries,Integer.parseInt(amount),Integer.parseInt(amount));
        movies.put(new_movie.getId(),new_movie);
        //updateMoviesJson();
        lockMovie.writeLock().unlock();

    }
    public void removeMovie(Movie movie)
    {
        lockMovie.writeLock().lock();
        movies.remove(movie.getId());
        //updateMoviesJson();
        lockMovie.writeLock().unlock();

    }
    public void updateMoviePrice(Movie movie,int price)
    {
        lockMovie.writeLock().lock();
        movies.get(movie.getId()).setPrice(price);
        //updateMoviesJson();
        lockMovie.writeLock().unlock();

    }

    public Movie getMovie(Integer id)
    {
        lockMovie.readLock().lock();
        if( movies.containsKey(id))
        {
            lockMovie.readLock().unlock();
            return movies.get(id);
        }
        else
        {
            lockMovie.readLock().unlock();
            return null;
        }

    }



    public String moviesToString()
    {
        lockMovie.readLock().lock();
        String result="";
        for(Integer movie: movies.keySet())
        {
            result = result+" \""+movies.get(movie).getName()+"\"";
        }
        lockMovie.readLock().unlock();
        return result;
    }

    public Integer movieExist(String movieName)
    {
        lockMovie.readLock().lock();
        for(Integer movie: movies.keySet())
        {
            if(movies.get(movie).getName().equalsIgnoreCase(movieName))
            {
                lockMovie.readLock().unlock();
                return movie;
            }
        }
        lockMovie.readLock().unlock();
        return null;
    }
    public void updateUsersJson()
    {
        ConvertJSON json = new ConvertJSON(path);
        json.setUsers(users);
    }
    public void updateMoviesJson()
    {
        ConvertJSON json = new ConvertJSON(path);
        json.setMovies(movies);
    }
    public boolean rentedMovie(String movieName)
    {
        for (String key: users.keySet()) {
            if(users.get(key).rentedMovie(movieName))
            {
                return  true;
            }

        }
        return  false;
    }
}
