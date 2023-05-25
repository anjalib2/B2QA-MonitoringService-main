package com.montioringService.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Reader {
	public static JSONObject getJsonObject(String fileName)
	{
		JSONObject returnValue = null;
		String filePath = System.getProperty("user.dir")+File.separator+"src\\main\\resources\\configs"
                + "\\" + fileName + ".json";
        try {
            returnValue = (JSONObject) new org.json.simple.parser.JSONParser().parse(
                    new FileReader(filePath));
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} catch (ParseException e) {
			
		}
		return returnValue;
	}
	public static String getData(String fileName,String fieldName)  {
		String returnValue = (String) getJsonObject(fileName).get(fieldName);
		return returnValue;
	}
}