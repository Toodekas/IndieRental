package indiegame_store.backend.database;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import indiegame_store.backend.domain.Customer;
import indiegame_store.backend.domain.Game;
import indiegame_store.backend.domain.RentOrder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class DatabaseFactory {

    public static String convertYamlToJson(String yaml) {
        try {
            ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
            Object obj = yamlReader.readValue(yaml, Object.class);
            ObjectMapper jsonWriter = new ObjectMapper();
            return jsonWriter.writeValueAsString(obj);
        } catch (JsonParseException e) {
            return null;
        } catch (JsonMappingException e) {
            return null;
        } catch (JsonProcessingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
    public static String convertJsonToYaml(String json) {
        // parse JSON
        JsonNode jsonNodeTree = null;
        try {
            jsonNodeTree = new ObjectMapper().readTree(json);
            // save it as YAML
            return new YAMLMapper().writeValueAsString(jsonNodeTree);
        } catch (IOException e) {
            return null;
        }
    }


    public static Database from(String filePath) {

        String json = "";
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.serializeNulls().setPrettyPrinting().create();
        FileObject object;
        //Read in the database file
        try{
            File file = new File(filePath);
            Scanner scanner = new Scanner(file,"UTF-8");
            json = scanner.useDelimiter("\\A").next();
        } catch (FileNotFoundException e) {
            System.out.println("Error404");
            return null;
        }

        //Check what kind of database are we dealing with
        if (filePath.length() >5){ //has to be bigger than just the extension
            if(filePath.substring(filePath.length()-4).equals("yaml")){
                json = convertYamlToJson(json);
            } //since we are only working with yaml and json, we don't need to check if the file ends with .json
        }
        else{
            return null;
        }

        //Turning the String into a DBObject
        try{
            object = gson.fromJson(json, FileObject.class);
        } catch (JsonSyntaxException e) {
            return null;
        }

        //Validation for games and customers
        for(Game m: object.getGames()){
            m.fromJson();
            if(m.isValid() == false){
                return null;
            }
        }
        for(Customer c:object.getCustomers()){
            if(c.isValid() == false){
                return null;
            }
        }
        DBTableRepository<Game> gameTable = new DBTableRepository<>(new ArrayList<>(Arrays.asList(object.getGames())));
        DBTableRepository<Customer> customerTable = new DBTableRepository<>(new ArrayList<>(Arrays.asList(object.getCustomers())));

        for(RentOrder rentOrder: object.getRentOrders()){
            rentOrder.fromJson(customerTable);
            for(RentOrder.Item i: rentOrder.getItems())
                i.fromJson(gameTable);
            if(rentOrder.isValid() == false){
                return null;
            }
        }
        DBTableRepository<RentOrder> orderTable = new DBTableRepository<>(new ArrayList<>(Arrays.asList(object.getRentOrders())));


        return new Database() {

            @Override
            public DBTableRepository<Game> getGameTable() {
                return gameTable;
            }

            @Override
            public DBTableRepository<Customer> getCustomerTable() {
                return customerTable;
            }

            @Override
            public DBTableRepository<RentOrder> getOrderTable() {
                return orderTable;
            }

            @Override
            public void updateDB() {
                object.setGames(getGameTable().getAll().toArray(new Game[0]));
                object.setCustomers(getCustomerTable().getAll().toArray(new Customer[0]));
                object.setRentOrders(getOrderTable().getAll().toArray(new RentOrder[0]));
                try {
                    if (filePath.substring(filePath.length()-4).equals("yaml")){
                        Files.writeString(Path.of(filePath), convertJsonToYaml(gson.toJson(object)));
                    } else{
                        Files.writeString(Path.of(filePath), gson.toJson(object));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
