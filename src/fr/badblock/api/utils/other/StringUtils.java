package fr.badblock.api.utils.other;

import java.util.Collection;
import java.util.Map;

public class StringUtils {
	public static <T> String join(Collection<T> toJoin, String joiner){
		boolean first = true;;
		String result = "";
		
		for(T o : toJoin){
			if(!first)
				result += joiner;
			else first = false;
			
			result += o.toString();
		}
		
		return result;
	}
	public static <K, V> String join(Map<K, V> toJoin, String joiner, String subJoiner){
		boolean first = true;;
		String result = "";
		
		for(K key : toJoin.keySet()){
			if(!first)
				result += joiner;
			else first = false;
			
			result += key + subJoiner + toJoin.get(key);
		}
		
		return result;
	}
	public static <T> String join(T[] toJoin, String joiner){
		boolean first = true;;
		String result = "";
		
		for(T o : toJoin){
			if(!first)
				result += joiner;
			else first = false;
			
			result += o.toString();
		}
		
		return result;
	}
	
	public static String getUpperFirstLetter(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
	}
	
}
