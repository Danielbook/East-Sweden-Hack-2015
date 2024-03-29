package eastswedenhack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *  Station Linköping: 85250
 *  Lufttemperatur - momentanvärde: 1
 *  Nederbördsmängd - summa 1 timme: 7
 *  Period: latest-hour
 * 
 */ 
public class jsonparser {
    
    public static void main(String[] args) {
            
            String station = "86340"; //Norrköping SMHI
            String luftTemp = "1";
            String nederBord = "7";
            String period = "latest-hour";
		try {
			jsonparser openDataMetobsReader = new jsonparser();
			/*
                        String parameterKey = openDataMetobsReader.getParameters();
			String stationKey = openDataMetobsReader.getStationNames(parameterKey);
			String periodName = openDataMetobsReader.getPeriodNames(parameterKey, stationKey);
                        String data = openDataMetobsReader.getData(parameterKey, stationKey, periodName);                            
			System.out.println(data);
                        */
                    
                        // ****NEDERBÖRD**** //
//                        String paramN = openDataMetobsReader.setParameter("Nederbördsmängd");
                        
                        // ****TEMPERATUR**** //
//                        String paramT = openDataMetobsReader.setParameter("Lufttemperatur");
                        
                        // ****STATION**** //
//                        String stationN = openDataMetobsReader.setStation(paramN, station);
//                        
//                        String stationT = openDataMetobsReader.setStation(paramT, station);
//                        
                        // ****PERIOD**** // -- ALWAYS USE latest-hour
                        //String periodN = openDataMetobsReader.setPeriodNames(paramN, stationN);
                        //String periodT = openDataMetobsReader.setPeriodNames(paramT, stationT);
                                                
                        // ****DATA**** //
                        String catDataN = openDataMetobsReader.getData(nederBord, station, period);
                        String catDataT = openDataMetobsReader.getData(luftTemp, station, period);
                        
                        String rain = openDataMetobsReader.getValue(nederBord, station, period);
                        String temperature = openDataMetobsReader.getValue(luftTemp, station, period);

                        System.out.println(catDataN);
                        
                        System.out.println("****************");
                        
                        System.out.println(catDataT);
                        
                        System.out.println("****************");
                        
                        System.out.println("Regn: " + rain);
                        System.out.println("Temperatur: " + temperature + " C");
                        
                        openDataMetobsReader.save("index.txt", rain, temperature);
                        
                        //System.out.println(catDataT);
                        // ******** //
                     
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}
  
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
                //System.out.println(metObsAPI + "/version/latest/parameter/" + parameterKey + "/station/" + stationKey + "/period/" + periodName + "/data.csv");
		return readStringFromUrl(metObsAPI + "/version/latest/parameter/" + parameterKey + "/station/" + stationKey + "/period/" + periodName + "/data.json");
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

	
    //Hämtar värde från Json filen, som hämtas från databasen
    private String getValue(String parameterKey, String stationKey, String periodName) throws IOException {
        
        String urlToRead = metObsAPI + "/version/latest/parameter/" + parameterKey + "/station/" + stationKey + "/period/" + periodName + "/data.json";
        
        JSONObject dataObject = readJsonFromUrl(urlToRead);
        JSONArray dataArray = dataObject.getJSONArray("value");   

        String dataKey = null;
        for (int i = 0; i < dataArray.length(); i++) {

                JSONObject parameter = dataArray.getJSONObject(i);
                dataKey = parameter.getString("value");
                //String dataName = parameter.getString("title");
                //System.out.println(dataKey);
        }

        return dataKey;
        
    }
    
    //Sparar till textfil
    public void save(String utFil, String rain, String temperature) throws IOException {
        FileWriter outfile = new FileWriter(utFil, true); 
        
        BufferedWriter bufferedWriter = new BufferedWriter(outfile);

        bufferedWriter.write("\n" + rain + "\n" + temperature);
        
        bufferedWriter.close();
    }
}




//    private String setParameter(String weather) throws IOException, JSONException {
//                
//        String weatherKey = null;
//        
//        JSONObject parameterObject = readJsonFromUrl(metObsAPI + "/version/latest.json");
//        JSONArray parametersArray = parameterObject.getJSONArray("resource");   
//
//        String parameterKey = null;
//        for (int i = 0; i < parametersArray.length(); i++) {
//
//                JSONObject parameter = parametersArray.getJSONObject(i);
//                parameterKey = parameter.getString("key");
//                String parameterName = parameter.getString("title");
//                //System.out.println(parameterKey + ": " + parameterName);
//                
//                if(parameterName.equalsIgnoreCase(weather))
//                {
//                    weatherKey = parameterKey;
//                    System.out.println("Väder: " + parameterName + " | Nyckel: " + weatherKey);
//                    break;
//                }
// 
//        }
//        return weatherKey;
//    }

//    private String setStation(String weatherKey, String station) throws IOException, JSONException {
//        
//        String stationKey = "";
//                
//        JSONObject stationsObject = readJsonFromUrl(metObsAPI + "/version/latest/parameter/" + weatherKey + ".json");
//        JSONArray stationsArray = stationsObject.getJSONArray("station");
//       
//        String stationId = null;
//        for (int i = 0; i < stationsArray.length(); i++) {
//                String stationName = stationsArray.getJSONObject(i).getString("name");
//                stationId = stationsArray.getJSONObject(i).getString("key");
//                //System.out.println(stationId + ": " + stationName);
//                
//                if(stationName.equalsIgnoreCase(station))
//                {
//                    stationKey = stationId;
//                    System.out.println("Station: " + stationName + " | Nyckel: " + stationKey);
//                    return stationKey;
//                }
//        }
//        return "notfound";
//    }