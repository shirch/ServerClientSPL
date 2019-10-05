package bgu.spl181.net.impl.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Movie {
    private int id;
    private String name;
    private int price;
    private List<String> bannedCountries;
    private int availableAmount;
    private int totalAmount;

    public Movie(int _id, String _name, int _price, ArrayList<String> _banned_countries, int _available_amount, int _total_amount)
    {
        id=_id;
        name=_name;
        price=_price;
        bannedCountries=_banned_countries;
        availableAmount=_available_amount;
        totalAmount=_total_amount;
    }
    public void setPrice(int _price)
    {
        price =_price;
    }
    public int getPrice()
    {
        return  price;
    }
    public int getId()
    {
        return  id;
    }
    public String getName()
    {
        return  name;
    }
    public String getBannedCountries()
    {
        String result = "";
        for(String country : bannedCountries)
        {
            result=result+"\""+country + "\" ";
        }
        return result;
    }
    public int getAvailableAmount()
    {
        return availableAmount;
    }
    public int getTotalAmount()
    {
        return totalAmount;
    }
    public boolean isBanned(String country)
    {
        for (String bannedCountry:bannedCountries) {
            if(bannedCountry.equalsIgnoreCase(country))
                return true;
        }
        return false;
    }
    public void decAvailableAmount()
    {
        availableAmount--;
    }
    public void incAvailableAmount()
    {
        availableAmount++;
    }

}
