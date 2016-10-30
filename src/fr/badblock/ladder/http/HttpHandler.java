package fr.badblock.ladder.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

import fr.badblock.ladder.api.utils.StringUtils;
import fr.badblock.utils.Callback;
import lombok.Getter;

public class HttpHandler extends Thread {
	@Getter private String url;
	@Getter private Map<String, Object> values;
	@Getter private JsonElement object;
	@Getter private boolean get = false;
	@Getter private Callback<String> callback;

	public HttpHandler(Callback<String> callback, String url) {
		this.callback = callback;
		this.url = url;
	}

	public HttpHandler changeUrl(String url){
		this.url = url; return this;
	}

	public HttpHandler addValues(Map<String, Object> values){
		this.values = values; return this;
	}
	
	public HttpHandler addValues(String[] keys, Object[] values){
		this.values = new HashMap<String, Object>();
		
		for(int i=0;i<keys.length;i++){
			if(values.length<=i) break;
			this.values.put(keys[i], values[i]);
		}
		
		return this;
	}

	public HttpHandler addJson(JsonElement object){
		this.object = object; return this;
	}

	public HttpHandler setGet(boolean get){
		this.get = get;
		return this;
	}

	@Override
	public void run(){
		String result = "";
		Throwable throwable = null;

		try {
			HttpURLConnection con = createConnection();
			prepareConnection(con);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));;

			String inputLine = "";
			while((inputLine = in.readLine()) != null) {
				result += inputLine + "\n";
			}
			in.close();

			result = StringUtils.removeBOM(result);
		} catch(Throwable t){
			throwable = t;
		}

		callback.done(result, throwable);
	}

	private void prepareConnection(HttpURLConnection con) throws Throwable {
		con.setRequestMethod(get ? "GET" : "POST");

		if(!get){
			if(object != null) con.setRequestProperty("content-type", "application/json");

			String urlParameters = getPostParameters();
			con.setDoOutput(true);

			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush(); wr.close();
		}
	}

	private HttpURLConnection createConnection() throws Throwable {
		URL url = new URL(prepareURL());
		return (HttpURLConnection) url.openConnection();
	}

	private String getPostParameters(){
		if(object != null){
			return object.toString();
		} else if(values != null){
			return StringUtils.join(values, "&", "=");
		} else return "";
	}

	public String prepareURL(){
		if(get && values != null){
			if(!url.endsWith("?"))
				return url + "?" + StringUtils.join(values, "&", "=");
			else return url + StringUtils.join(values, "&", "=");
		} else return url;
	}
}
