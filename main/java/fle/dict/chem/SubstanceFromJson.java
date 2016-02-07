//package fle.dict.chem;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.lang.reflect.Type;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.stream.JsonReader;
//import com.sun.org.apache.bcel.internal.generic.RETURN;
//
//import farcore.substance.Matter;
//import farcore.substance.Substance;
//import farcore.util.FleLog;
//
//public class SubstanceFromJson
//{
//	public static File substanceFile;
//	private static final JsonDeserializer<Matter> MATTER_DESERIALIZER = 
//			(JsonElement json, Type typeOfT, JsonDeserializationContext context) -> 
//	{
//		return Matter.matter(json.getAsString());
//	};
//	
//	public static void load()
//	{
//		JsonReader reader = null;
//		try
//		{
//			if(!substanceFile.exists())
//			{
//				substanceFile.createNewFile();
//			}
//			else
//			{
//				reader = new JsonReader(new FileReader(substanceFile));
//				Gson gson = 
//						new GsonBuilder()
//						.registerTypeAdapter(Matter.class, typeAdapter)
//						.create();
//				gson.fromJson(reader, Substance.class);
//			}
//		}
//		catch(IOException exception)
//		{
//			FleLog.warn("Fail to read substance from file.", exception);
//		}
//		finally
//		{
//			if(reader != null)
//			{
//				try
//				{
//					reader.close();
//				}
//				catch(IOException exception2)
//				{
//					;
//				}
//			}
//		}
//	}
//}