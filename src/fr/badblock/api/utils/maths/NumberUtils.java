package fr.badblock.api.utils.maths;

public class NumberUtils {
	public static int random(int min, int max){
		return (int)(Math.random() * (max + 1 - min)) + min;		
	}
}
