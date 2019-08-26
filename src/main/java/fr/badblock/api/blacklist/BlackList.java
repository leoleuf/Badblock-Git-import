package fr.badblock.api.blacklist;

import java.util.ArrayList;

public class BlackList {
    ArrayList<String> address = new ArrayList<>();

    public ArrayList<String> getAddress() {
        return address;
    }
    public void test(String str){
        if(!getAddress().contains(str)) {
            getAddress().add(str);
        }
    }
}
