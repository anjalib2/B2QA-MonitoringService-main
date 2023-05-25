package com.monitoringService.constants;

public class EnvVariables {
	private static String env = "";
	
	public static void setEnv(String envValue)
	{
		env=envValue;
	}
	public static String getEnv ()
	{
		return env;
	}
}
