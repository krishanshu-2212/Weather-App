package com.example.weatherapp;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemperature,micon,mcity,mWeatherType;
    private int mCondition;

    private String minTemperature; //Addition code
    private String maxTemperature; //Addtional Code
    private static final String TAG = "weatherData";

    public static weatherData fromJson(JSONObject jsonObject)
    {

        try
        {

            weatherData weatherD=new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            Log.d(TAG, "fromJson: Main" + jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            //From here addition code works
            double minTemp = jsonObject.getJSONObject("main").getDouble("temp_min")-273.15;
            int roundedMinTemp=(int)Math.rint(minTemp);
            weatherD.minTemperature=Integer.toString(roundedMinTemp);

            Log.e(TAG, "fromJson: MinTemp"+jsonObject.getJSONObject("main").getDouble("temp_min") );
            double maxTemp = jsonObject.getJSONObject("main").getDouble("temp_max")-273.15;
            int roundedMaxTemp = (int)Math.rint(maxTemp);
            weatherD.maxTemperature = Integer.toString(roundedMaxTemp);
            Log.e(TAG, "fromJson: MaxTemp"+jsonObject.getJSONObject("main").getDouble("temp_max") );
            //Upto here

            return weatherD;
        }


        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }


    private static String updateWeatherIcon(int condition)
    {
        if(condition>=0 && condition<=300)
        {
            return "thunderstrom1";
        }
        else if(condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<=600)
        {
            return "shower";
        }
        else  if(condition>=600 && condition<=700)
        {
            return "snow2";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }

        else if(condition>=772 && condition<=800)
        {
            return "overcast";
        }
        else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        else  if(condition>=900 && condition<=902)
        {
            return "thunderstrom1";
        }
        if(condition==903)
        {
            return "snow1";
        }
        if(condition==904)
        {
            return "sunny";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunderstrom2";
        }

        return "dunno";


    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }


    public String getMinTemperature() { return minTemperature+"°"; }

    public String getMaxTemperature() { return maxTemperature+"°";}

}

