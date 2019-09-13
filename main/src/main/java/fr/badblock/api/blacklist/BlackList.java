package fr.badblock.api.blacklist;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BlackList {
    ArrayList<String> address = new ArrayList<>();
    JSONObject object = new JSONObject();

    public ArrayList<String> getAddress() {
        return address;
    }
    public boolean check(String str){
        if(getAddress().contains(str)) return true;
        else return false;
    }
    public void addIp(String str){
        //if(getAddress().contains(str)) return;
        //getAddress().add(str);
        if(!(object.contains(str))){
        object.put("users", str);
        System.out.println("Object créé et data ajouté");
        }
        JSONArray objectList = new JSONArray();
        objectList.add(object);
        System.out.println("Object JSON ajouté à la liste d'objects.");
        try (FileWriter file = new FileWriter("blacklist.json")){
            file.write(objectList.toJSONString());
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
    public void removeIp(String str){
        if(getAddress().contains(str)){
            getAddress().remove(str);
        }
    }
}
