package fr.badblock.api.blacklist;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BlackList {
    JSONObject object = new JSONObject();
    JSONArray objectList = new JSONArray();
    public boolean check(String str){
        if(object.containsKey(str)) return true;
        else return false;
    }
    public void addIp(String str){
        object.put("users", str);
        System.out.println("Object créé et data ajouté");
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
        object.remove(str);
        System.out.println("L'addresse ip " + str + " a été retirée de la blacklist.");
    }
}
