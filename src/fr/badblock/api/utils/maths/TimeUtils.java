package fr.badblock.api.utils.maths;


public class TimeUtils {
	public static Time convertTime(double time, TimeType type, TimeType end){
		if(type == end)
			return new Time(end, (long) time, time);
		else if(type.getId() < end.getId()){
			for(int i=type.getId();i<=end.getId();i++){
				time *= TimeType.match(i).getConvertInfo();
			}
			return new Time(end, (long) time, time);
		} else if(type.getId() < end.getId()){
			for(int i=type.getId();i>=end.getId();i++){
				time /= TimeType.match(i).getConvertInfo();
			}
			return new Time(end, (long) time, time);
		} else return null; // peut pas arriver ! x)
	}
	
	public static String parseTime(int time, TimeType type){
		int m = time / 60;
		int s = time % 60;
		String result = m + "m";
		if(s < 10)
			result += "0";
		result += s + "s";
		
		return result;
	}
	public static class Time{
		private TimeType type;
		private long value;
		private double floatValue;
		
		public Time(TimeType type, long value, double floatValue){
			this.type = type;
			this.value = value;
			if(floatValue > 1)
				floatValue = floatValue - ((int) floatValue);
			this.floatValue = floatValue;
		}
		@Override
		public String toString(){
			return value + type.getIdentifier() + floatValue;
		}
	}
	public static enum TimeType {
		NANOSECOND(0, "ns", "nanoseconde", 1000),
		MILLISECOND(1, "ms", "milliseconde", 1000),
		SECOND(2, "s", "seconde", 60),
		MINUTE(3, "m", "minute", 60),
		HOUR(4, "h", "heure", 0);
		
		private String identifier, displayName;
		private int id, convertInfo;
		
		private TimeType(int id, String identifier, String displayName, int convertInfo){
			this.identifier = identifier;
			this.displayName = displayName;
			this.id = id;
			this.convertInfo = convertInfo;
		}
		
		public int getId(){
			return id;
		}
		public int getConvertInfo(){
			return convertInfo;
		}
		public String getIdentifier(){
			return identifier;
		}
		public String getDisplayName(){
			return displayName;
		}
		@Override
		public String toString(){
			return identifier;
		}
		
		public static TimeType match(String identifier){
			for(TimeType type : values()){
				if(type.getDisplayName().equalsIgnoreCase(identifier) || type.getIdentifier().equalsIgnoreCase(identifier)){
					return type;
				}
			}
			return null;
		}
		public static TimeType match(int id){
			for(TimeType type : values()){
				if(type.getId() == id)
					return type;
			}
			return null;
		}
	}
}
