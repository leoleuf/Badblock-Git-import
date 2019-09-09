package fr.badblock.api.blacklist;

import java.util.ArrayList;

public class BlackList {
    ArrayList<String> address = new ArrayList<>();

    //WONT WORK, NEED TO USE IT AS A DATABASE OR BLACKLIST.JSON FILE WHICH I NEED TO LOG IP INSIDE.
    public ArrayList<String> getAddress() {
        return address;
    }
    public boolean check(String str){
        if(getAddress().contains(str)) return true;
        else return false;
    }
    public void addIp(String str){
        if(getAddress().contains(str)) return;
        getAddress().add(str);
    }
    public void removeIp(String str){
        if(getAddress().contains(str)){
            getAddress().remove(str);
        }
    }
}
