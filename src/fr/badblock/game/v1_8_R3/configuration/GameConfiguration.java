package fr.badblock.game.v1_8_R3.configuration;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.gameapi.configuration.BadConfiguration;
import fr.badblock.gameapi.configuration.values.MapList;
import fr.badblock.gameapi.configuration.values.MapValue;
import fr.badblock.gameapi.configuration.values.MapValuePrimitive;
import fr.badblock.gameapi.utils.general.JsonUtils;

public class GameConfiguration implements BadConfiguration {
	private JsonObject 					   handle;
	private Map<String, GameConfiguration> subConfigurations = Maps.newConcurrentMap();

	public GameConfiguration(JsonObject handle){
		this.handle = handle;
	}
	
	@Override
	public <T extends MapValue<?>> T getValue(String key, Class<T> clazzValue) {
		try {
			if(handle.has(key)){
				if(MapValuePrimitive.class.isAssignableFrom(clazzValue)){
					MapValuePrimitive<?> value = (MapValuePrimitive<?>) clazzValue.getConstructor().newInstance();
					value.from(handle.get(key));

					return clazzValue.cast(value);
				}

				return new Gson().fromJson(handle.get(key), clazzValue);
			}

			return clazzValue.getConstructor().newInstance();
		} catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T extends MapValue<?>> T getValue(String key, Class<T> clazzValue, T def) {
		if(!handle.has(key)) setValue(key, def);

		return getValue(key, clazzValue);
	}

	@Override
	public <T extends MapValue<K>, K> MapList<T, K> getValueList(String key, Class<T> clazzValue) {
		MapList<T, K> result = new MapList<>();

		if(handle.has(key)){
			JsonArray array = handle.get(key).getAsJsonArray();

			boolean primitive = MapValuePrimitive.class.isAssignableFrom(clazzValue);

			array.forEach(element -> {
				try {
					if(primitive){
						MapValuePrimitive<?> value = (MapValuePrimitive<?>) clazzValue.getConstructor().newInstance();
						value.from(element);

						result.add(clazzValue.cast(value));
					}

					result.add(new Gson().fromJson(element, clazzValue));
				} catch(Exception e){
					e.printStackTrace();
				}
			});
		}

		return result;
	}

	@Override
	public <T extends MapValue<K>, K> MapList<T, K> getValueList(String key, Class<T> clazzValue, List<T> def) {
		if(!handle.has(key)) setValueList(key, def);

		return getValueList(key, clazzValue);
	}

	@Override
	public <T extends MapValue<?>> void setValue(String key, T value) {
		JsonElement val = null;
		
		if(value instanceof MapValuePrimitive<?>){
			val = ((MapValuePrimitive<?>) value).to();
		} else {
			val = new Gson().toJsonTree(value);
		}
		
		handle.add(key, val);
	}

	@Override
	public <T extends MapValue<?>> void setValueList(String key, List<T> value) {
		JsonArray array = new JsonArray();
	
		for(T single : value){
			if(single instanceof MapValuePrimitive<?>){
				array.add(((MapValuePrimitive<?>) single).to());
			} else {
				array.add(new Gson().toJsonTree(single));
			}
		}
		
		handle.add(key, array);
	}

	@Override
	public BadConfiguration getSection(String key) {
		if(!subConfigurations.containsKey(key)){
			if(handle.has(key)){
				subConfigurations.put(key, new GameConfiguration(handle.get(key).getAsJsonObject()));
			} else {
				subConfigurations.put(key, new GameConfiguration(new JsonObject()));
			}
		}
		
		return subConfigurations.get(key);
	}

	@Override
	public void save(File file) {
		JsonUtils.save(file, save(), true);
	}

	@Override
	public JsonObject save() {
		subConfigurations.forEach((key, sub) -> {
			handle.add(key, sub.save());
		});
		
		return handle;
	}

}
