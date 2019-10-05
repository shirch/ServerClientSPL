package bgu.spl181.net.impl.JSON;

        import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.concurrent.ConcurrentLinkedQueue;

        import com.google.gson.*;


public class ConvertJSON {

    private Gson gson;
    private JsonObject object_movies;
    private JsonObject object_users;
    private  String path;

    public ConvertJSON(String file_path)
    {
        gson = new Gson();
        path = file_path;
        try
        {
            object_movies = gson.fromJson(new FileReader(path+"//Movies.json"), JsonObject.class) ;
            object_users = gson.fromJson(new FileReader(path+"//Users.json"), JsonObject.class) ;
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Cannot find file path");
        }


    }

    public HashMap<Integer,Movie> getMovies()
    {
        HashMap<Integer,Movie> movies = new HashMap<Integer, Movie>();
        JsonArray movies_elements = object_movies.getAsJsonArray("movies");

        for(JsonElement movie_element:movies_elements) {
            int id = movie_element.getAsJsonObject().get("id").getAsInt();
            String name = movie_element.getAsJsonObject().get("name").getAsString();
            int price = movie_element.getAsJsonObject().get("price").getAsInt();
            ArrayList<String> banned_queue = new ArrayList<>();
            JsonArray banned_array = movie_element.getAsJsonObject().get("bannedCountries").getAsJsonArray();
            for (JsonElement banned_element : banned_array) {
                banned_queue.add(banned_element.getAsString());
            }
            int available_amount = movie_element.getAsJsonObject().get("availableAmount").getAsInt();
            int total_amount = movie_element.getAsJsonObject().get("totalAmount").getAsInt();
            movies.put(id, new Movie(id, name, price, banned_queue, available_amount, total_amount));
        }
        return movies;
    }

    public HashMap<String,User> getUsers()
    {
        HashMap<String,User> users = new HashMap<String, User>();
        JsonArray users_elements = object_users.getAsJsonArray("users");

        for(JsonElement user_element:users_elements) {
            String username = user_element.getAsJsonObject().get("username").getAsString();
            String type = user_element.getAsJsonObject().get("type").getAsString();
            String password = user_element.getAsJsonObject().get("password").getAsString();
            String country = user_element.getAsJsonObject().get("country").getAsString();
            int balance = user_element.getAsJsonObject().get("balance").getAsInt();
            HashMap<Integer, String> movies_map = new HashMap<Integer, String>();
            JsonArray movies_array = user_element.getAsJsonObject().get("movies").getAsJsonArray();
            for (JsonElement movie_element : movies_array) {
                String name = movie_element.getAsJsonObject().get("name").getAsString();
                int id = movie_element.getAsJsonObject().get("id").getAsInt();
                movies_map.put(id,name);
            }
            users.put(username,new User(username,type,password,country,movies_map,balance));
        }
        return users;
    }

    public void setUsers(HashMap<String,User> users){
        try(FileWriter write = new FileWriter(path+"//Users.json"))
        {
            Object[] arrayUsers = users.values().toArray();
            String jsonString = gson.toJson(arrayUsers);
            JsonParser parser = new JsonParser();
            jsonString = jsonString.replace("\\\"","");
            JsonObject objectJson = parser.parse("{\"users\":"+jsonString+"}").getAsJsonObject();
            gson.toJson(objectJson, write);
        }catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void setMovies(HashMap<Integer,Movie> movies){
        try(FileWriter write = new FileWriter(path+"//Movies.json"))
        {
            Object[] arrayUsers = movies.values().toArray();
            String jsonString = gson.toJson(arrayUsers);
            jsonString = jsonString.replace("\\u0026" , "&");
            JsonParser parser = new JsonParser();
            JsonObject objectJson = parser.parse("{\"movies\":"+jsonString +"}").getAsJsonObject();
            gson.toJson(objectJson, write);
        }catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}