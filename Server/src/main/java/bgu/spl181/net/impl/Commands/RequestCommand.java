package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.JSON.Movie;
import bgu.spl181.net.impl.JSON.User;
import bgu.spl181.net.impl.UserService;

import java.util.ArrayList;
import java.util.List;

public class RequestCommand extends CommandImpl {
    private String command_type;
    private String params;
    private UserService service;
    private String username;

    public RequestCommand(String _username,String _command_type, String _params, UserService _service)
    {
        command_type = _command_type;
        params = _params;
        service=_service;
        username=_username;
    }



    public String handle(){
        String returnString = "";
        Integer key=null;

        User user = service.getUser(username);

        switch(command_type)
        {
            case "balance":
                if(params.contains("add"))
                {
                    String s = params.substring(params.indexOf("add")+4);
                    int amount = Integer.parseInt(s.substring(0));
                    user.setBalance(user.getBalance()+amount);
                    returnString = "ACK balance "+user.getBalance()+" added "+amount;
                }
                else if(params.contains("info"))
                {
                    returnString= "ACK balance "+user.getBalance();
                }
                else {
                    returnString = "ERROR request balance failed";
                }
                break;
                case "info":
                    if(params == null)//no movie name was given
                    {
                        returnString = "ACK info"+ service.moviesToString();
                    }
                    else
                    {
                        String temp = params.substring(params.indexOf("\"")+1);
                        temp=temp.substring(0,temp.indexOf("\""));
                        key = service.movieExist(temp);
                        if(key!=null)
                        {
                            Movie movie = service.getMovie(key);
                            returnString = "ACK info \""+movie.getName()+"\" "+movie.getAvailableAmount()+" "+movie.getPrice()+" "+movie.getBannedCountries();
                        }
                        else
                        {
                            returnString = "ERROR request info failed";
                        }
                    }
                    break;
                case "rent":
                    key = service.movieExist(params.replace("\"",""));
                    if(key!=null) {
                        Movie movie = service.getMovie(service.movieExist(params.replace("\"","")));
                        int amount = movie.getAvailableAmount();
                        int balance = user.getBalance();
                        int price = movie.getPrice();
                        boolean banned = movie.isBanned(user.getCountry().replace("\"",""));
                        boolean rented = (user.rentedMovie(movie.getName()));
                        if ((movie.getAvailableAmount() > 0) && (user.getBalance() >= movie.getPrice()) && (!movie.isBanned(user.getCountry())) && (!user.rentedMovie(movie.getName()))) {
                            user.rentMovie(movie);
                            movie.decAvailableAmount();
                            user.setBalance(user.getBalance() - movie.getPrice());
                            returnString = "ACK rent \""+movie.getName()+"\" success"+"BROADCAST movie \""+movie.getName()+"\" "+movie.getAvailableAmount()+" "+movie.getPrice();
                        }
                        else
                        {
                            returnString = "ERROR request rent failed";
                        }
                    }
                    else
                    {
                        returnString = "ERROR request rent failed";
                    }
                    break;
                case "return":
                    key = service.movieExist(params.replace("\"",""));
                    if(key!=null) {
                        Movie movie = service.getMovie(key);
                        if (user.rentedMovie(movie.getName())) {
                            movie.incAvailableAmount();
                            user.returnMovie(movie);
                            returnString = "ACK return \""+movie.getName()+"\" success"+"BROADCAST movie \""+movie.getName()+"\" "+movie.getAvailableAmount()+" "+movie.getPrice();
                        } else {
                            returnString = "ERROR request return failed";
                        }
                    }
                    else
                    {
                        returnString = "ERROR request return failed";
                    }
                    break;
                case "addmovie":
                    if(user.getType().equalsIgnoreCase("admin"))
                    {
                        String s = params.substring(params.indexOf("\"")+1);
                        String movieName = s.substring(0,s.indexOf("\""));
                        String temp = s.substring(movieName.length()+2);
                        String amount =  temp.substring(0,temp.indexOf(" "));
                        temp = temp.substring(amount.length()+1);
                        String price;
                        ArrayList<String> bannedcountries =new ArrayList<>();
                        String temp1 = temp;
                        try {
                            price =  temp.substring(0,temp.indexOf(" "));
                            temp = temp.substring(price.length() + 1);
                            int i=0;
                            temp=temp.substring(temp.indexOf("\"")+1);
                            while(!temp.equals("\""))
                            {
                                String country = temp.substring(0,temp.indexOf("\""));
                                bannedcountries.add(country);
                                i++;
                                temp = temp.substring(temp.indexOf("\""));
                                if(!temp.equals("\""))
                                {
                                    temp =temp.substring(3);
                                }

                            }
                        }catch(Exception ex){
                            price = temp1;
                        }
                        key = service.movieExist(movieName);
                        if((key==null)&&(Integer.parseInt(price)>0)&&((Integer.parseInt(amount)>0))) {

                            service.addMovie(movieName,amount,price,bannedcountries);
                            returnString = "ACK addmovie \""+movieName+"\" success"+"BROADCAST movie \""+movieName+"\" "+amount+" "+price;
                        }
                        else
                        {
                            returnString = "ERROR request addmovie failed";
                        }
                    }
                    else
                    {
                        returnString = "ERROR request addmovie failed";
                    }
                    break;
                case "remmovie":
                    if(user.getType().equalsIgnoreCase("admin"))
                    {
                        String s = params.substring(params.indexOf("\"")+1);
                        String movieName = s.substring(0,s.indexOf("\""));
                        key = service.movieExist(movieName);
                        if(key!=null){
                            Movie movie = service.getMovie(key);
                            if (!service.rentedMovie(movieName))
                            {
                                service.removeMovie(movie);
                                returnString = "ACK remmovie \""+movieName+"\" success"+"BROADCAST movie \""+movieName+"\" removed";
                            }
                            else
                            {
                                returnString = "ERROR request remmovie failed";
                            }
                        }
                        else
                        {
                            returnString = "ERROR request remmovie failed";
                        }
                    }
                    else
                    {
                        returnString = "ERROR request remmovie failed";
                    }
                    break;
                case "changeprice":
                    if(user.getType().equalsIgnoreCase("admin"))
                    {
                        String movieName = params.substring(params.indexOf("\"")+1,(params.substring(params.indexOf("\"")+1)).indexOf("\"")+1);
                        String price = params.substring(movieName.length()+3);
                        key = service.movieExist(movieName);
                        if((key!=null)&(Integer.parseInt(price)>0)){
                            Movie movie = service.getMovie(key);
                            service.updateMoviePrice(movie,Integer.parseInt(price));
                            returnString = "ACK changeprice \""+movieName+"\" success"+"BROADCAST movie \""+movie.getName()+"\" "+movie.getAvailableAmount()+" "+movie.getPrice();
                        }
                        else
                        {
                            returnString = "ERROR request changeprice failed";
                        }
                    }
                    else
                    {
                        returnString = "ERROR request changeprice failed";
                    }
                    break;

            }

        return returnString;
    }
}
