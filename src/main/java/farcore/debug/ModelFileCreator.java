package farcore.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.material.Mat;

public class ModelFileCreator
{
	static void provideRockInfo(String sourceLocate, Mat material)
	{
		JsonObject object = new JsonObject();
		JsonArray array = new JsonArray();
		for(RockType type : RockType.values())
		{
			JsonObject object2 = new JsonObject();
			JsonObject object3 = new JsonObject();
			object3.addProperty("rock_type", type.name());
			object2.add("when", object3);
			object3 = new JsonObject();
			object3.addProperty("model", material.modid + ":rock/" + material.name + "/" +
					(type == RockType.cobble_art ? RockType.cobble.name() : type.name()));
			object2.add("apply", object3);
			array.add(object2);
		}
		object.add("multipart", array);
		makeJson(sourceLocate, material.modid + "/blockstates/rock." + material.name + ".json", object);
		for(RockType type : RockType.values())
		{
			object = new JsonObject();
			object.addProperty("parent", "farcore:block/base_simple");
			JsonObject object2 = new JsonObject();
			object2.addProperty("all", material.modid + ":blocks/rock/" + material.name + "/" + type.name());
			object.add("textures", object2);
			makeJson(sourceLocate, material.modid + "/models/block/rock/" + material.name+ "/" + type.name() + ".json", object);
			object = new JsonObject();
			object.addProperty("parent", material.modid + ":block/rock/" + material.name +"/" + type.name());
			makeJson(sourceLocate, material.modid + "/models/item/rock/" + material.name+ "/" + type.name() + ".json", object);
		}
	}
	
	static void makeJson(String sourceLocate, String pathName, JsonObject object)
	{
		JsonWriter writer = null;
		try
		{
			File file = new File(sourceLocate, pathName);
			File file2 = file.getParentFile();
			if (!file2.exists())
			{
				file2.mkdirs();
			}
			if (!file.exists())
			{
				file.createNewFile();
			}
			writer = new JsonWriter(new BufferedWriter(new FileWriter(file)));
			writer.setIndent("	");
			Gson gson = new Gson();
			gson.toJson(object, writer);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (Exception exception2)
				{
					exception2.printStackTrace();
				}
			}
		}
	}
}