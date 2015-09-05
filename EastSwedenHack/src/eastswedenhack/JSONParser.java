package eastswedenhack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Scanner;

/**
 *  Station Linköping: 85250
 *  Lufttemperatur: 19, 20, 2, 1 
 *  Nederbördsmängd: 14, 7, 5
 *  Period: latest-hour
 *  
 * 
 * 
 */
public class JSONParser {

	// Url for the metobs API
	private String metObsAPI = "http://opendata-download-metobs.smhi.se/api";


	/**
	 * Print all available parameters.
	 *
	 * @return The key for the last parameter.
	 * @throws IOException
	 * @throws JSONException
	 */
	private String getParameters() throws IOException, JSONException {

		JSONObject parameterObject = readJsonFromUrl(metObsAPI + "/version/latest.json");
		JSONArray parametersArray = parameterObject.getJSONArray("resource");   

		String parameterKey = null;
		for (int i = 0; i < parametersArray.length(); i++) {

			JSONObject parameter = parametersArray.getJSONObject(i);
			parameterKey = parameter.getString("key");
			String parameterName = parameter.getString("title");
			System.out.println(parameterKey + ": " + parameterName);
		}

		return parameterKey;
	}
        
	/**
	 * Print all available stations for the given parameter. Return the id for the last station.
	 *
	 * @param parameterKey The key for the wanted parameter
	 * @return The id for the last station
	 * @throws IOException
	 * @throws JSONException
	 */
	private String getStationNames(String parameterKey) throws IOException, JSONException {

		JSONObject stationsObject = readJsonFromUrl(metObsAPI + "/version/latest/parameter/" + parameterKey + ".json");
		JSONArray stationsArray = stationsObject.getJSONArray("station");
		String stationId = null;
		for (int i = 0; i < stationsArray.length(); i++) {
			String stationName = stationsArray.getJSONObject(i).getString("name");
			stationId = stationsArray.getJSONObject(i).getString("key");
			System.out.println(stationId + ": " + stationName);
		}

		return stationId;
	}


	/**
	 * Print all available periods for the given parameter and station. Return the key for the last period.
	 *
	 * @param parameterKey The key for the wanted parameter
	 * @param stationKey The key for the wanted station
	 * @return The name for the last period
	 * @throws IOException
	 * @throws JSONException
	 */
	private String getPeriodNames(String parameterKey, String stationKey) throws IOException, JSONException {

		JSONObject periodsObject = readJsonFromUrl(metObsAPI + "/version/latest/parameter/" + parameterKey + "/station/" + stationKey + ".json");
		JSONArray periodsArray = periodsObject.getJSONArray("period");

		String periodName = null;
		for (int i = 0; i < periodsArray.length(); i++) {
			periodName = periodsArray.getJSONObject(i).getString("key");
			System.out.println(periodName);
		}

		return periodName;
	}


	/**
	 * Get the data for the given parameter, station and period.
	 *
	 * @param parameterKey The key for the wanted parameter
	 * @param stationKey The key for the wanted station
	 * @param periodName The name for the wanted period
	 * @return The data
	 * @throws IOException
	 * @throws JSONException
	 */
	private String getData(String parameterKey, String stationKey, String periodName) throws IOException {
                System.out.println(metObsAPI + "/version/latest/parameter/" + parameterKey + "/station/" + stationKey + "/period/" + periodName + "/data.xml");
		return readStringFromUrl(metObsAPI + "/version/latest/parameter/" + parameterKey + "/station/" + stationKey + "/period/" + periodName + "/data.xml");
	}


	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		String text = readStringFromUrl(url);
		return new JSONObject(text);
	}


	private String readStringFromUrl(String url) throws IOException {

		InputStream inputStream = new URL(url).openStream();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			StringBuilder stringBuilder = new StringBuilder();
			int cp;
			while ((cp = reader.read()) != -1) {
				stringBuilder.append((char) cp);
			}
			return stringBuilder.toString();
		} finally {
			inputStream.close();
		}
	}

	public static void main(String... args) {
            
            String station = "Linköping";
            String period = "latest-hour";
		try {
			JSONParser openDataMetobsReader = new JSONParser();
			/*
                        String parameterKey = openDataMetobsReader.getParameters();
			String stationKey = openDataMetobsReader.getStationNames(parameterKey);
			String periodName = openDataMetobsReader.getPeriodNames(parameterKey, stationKey);
                        String data = openDataMetobsReader.getData(parameterKey, stationKey, periodName);                            
			System.out.println(data);
                        */
                    
                        // ****NEDERBÖRD**** //
                        String paramN = openDataMetobsReader.setParameter("Nederbördsmängd");
                        
                        // ****TEMPERATUR**** //
                        String paramT = openDataMetobsReader.setParameter("Lufttemperatur");
                        
                        // ****STATION**** //
                        String stationN = openDataMetobsReader.setStation(paramN, station);
                        String stationT = openDataMetobsReader.setStation(paramT, station);
                        
                        // ****PERIOD**** // -- ALWAYS USE latest-hour
                        //String periodN = openDataMetobsReader.setPeriodNames(paramN, stationN);
                        //String periodT = openDataMetobsReader.setPeriodNames(paramT, stationT);
                        
                        // ****DATA**** //
                        String catDataN = openDataMetobsReader.getData(paramN, stationN, period);
                        //String catDataT = openDataMetobsReader.getData(paramT, stationT, period);
                        
                        System.out.println(catDataN);
                        //System.out.println(catDataT);
                        // ******** //
                     
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

    private String setParameter(String weather) throws IOException, JSONException {
                
        String weatherKey = null;
        
        JSONObject parameterObject = readJsonFromUrl(metObsAPI + "/version/latest.json");
        JSONArray parametersArray = parameterObject.getJSONArray("resource");   

        String parameterKey = null;
        for (int i = 0; i < parametersArray.length(); i++) {

                JSONObject parameter = parametersArray.getJSONObject(i);
                parameterKey = parameter.getString("key");
                String parameterName = parameter.getString("title");
                //System.out.println(parameterKey + ": " + parameterName);
                
                if(parameterName.equalsIgnoreCase(weather))
                {
                    weatherKey = parameterKey;
                    System.out.println("Väder: " + parameterName + " | Nyckel: " + weatherKey);
                    break;
                }
 
        }
        return weatherKey;
    }

    private String setStation(String weatherKey, String station) throws IOException, JSONException {
        
        String stationKey = null;
                
        JSONObject stationsObject = readJsonFromUrl(metObsAPI + "/version/latest/parameter/" + weatherKey + ".json");
        JSONArray stationsArray = stationsObject.getJSONArray("station");
       
        String stationId = null;
        for (int i = 0; i < stationsArray.length(); i++) {
                String stationName = stationsArray.getJSONObject(i).getString("name");
                stationId = stationsArray.getJSONObject(i).getString("key");
                //System.out.println(stationId + ": " + stationName);
                
                if(stationName.equalsIgnoreCase(station))
                {
                    stationKey = stationId;
                    System.out.println("Väder: " + stationName + " | Nyckel: " + stationId);
                    break;
                }
        }

        return stationId;
    }
    
    

}