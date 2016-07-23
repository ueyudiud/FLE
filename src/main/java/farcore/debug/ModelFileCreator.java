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
	static final Gson gson = new Gson();

	static void provideRockSlabInfo(String sourceLocate, Mat material)
	{
		for(RockType type : RockType.values())
		{
			String locate = material.modid + ":rock/" + material.name + "/slab/" +
					(type == RockType.cobble_art ? RockType.cobble.name() : type.name());
			String locate1 = material.modid + "/models/block/rock/" + material.name + "/slab/" +
					(type == RockType.cobble_art ? RockType.cobble.name() : type.name());
			String locate2 = material.modid + ":block/rock/" + material.name + "/slab/" +
					(type == RockType.cobble_art ? RockType.cobble.name() : type.name());
			JsonObject object = new JsonObject();
			JsonArray array = new JsonArray();
			JsonObject object2 = new JsonObject();
			addCondition(object2, "facing", "down");
			addModel(object2, locate, 0, 0);
			array.add(object2);
			object2 = new JsonObject();
			addCondition(object2, "facing", "up");
			addModel(object2, locate, 2, 0);
			array.add(object2);
			object2 = new JsonObject();
			addCondition(object2, "facing", "south");
			addModel(object2, locate, 1, 2);
			array.add(object2);
			object2 = new JsonObject();
			addCondition(object2, "facing", "north");
			addModel(object2, locate, 1, 0);
			array.add(object2);
			object2 = new JsonObject();
			addCondition(object2, "facing", "east");
			addModel(object2, locate, 1, 3);
			array.add(object2);
			object2 = new JsonObject();
			addCondition(object2, "facing", "west");
			addModel(object2, locate, 1, 1);
			array.add(object2);
			object2 = new JsonObject();
			addCondition(object2, "facing", "double_ud");
			addModel(object2, locate + "_double", 0, 0);
			array.add(object2);
			object2 = new JsonObject();
			addCondition(object2, "facing", "double_ns");
			addModel(object2, locate + "_double", 1, 0);
			array.add(object2);
			object2 = new JsonObject();
			addCondition(object2, "facing", "double_we");
			addModel(object2, locate + "_double", 1, 1);
			array.add(object2);
			object.add("multipart", array);
			makeJson(sourceLocate, material.modid + "/blockstates/rock." + material.name + "." + type.name() + ".slab.json", object);
			if(type == RockType.cobble_art)
			{
				continue;
			}
			object = new JsonObject();
			object.addProperty("parent", "farcore:block/slab");
			object2 = new JsonObject();
			object2.addProperty("all", material.modid + ":blocks/rock/" + material.name + "/" + type);
			object.add("textures", object2);
			makeJson(sourceLocate, locate1 + ".json", object);
			object = new JsonObject();
			object.addProperty("parent", "farcore:block/slab_double");
			object2 = new JsonObject();
			object2.addProperty("all", material.modid + ":blocks/rock/" + material.name + "/" + type);
			object.add("textures", object2);
			makeJson(sourceLocate, locate1 + "_double.json", object);
			String locate3 = material.modid + "/models/item/rock/" + material.name + "/" + (type == RockType.cobble_art ? RockType.cobble : type) + "_slab";
			object = new JsonObject();
			object.addProperty("parent", locate2);
			makeJson(sourceLocate, locate3, object);
		}
	}

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

	static void addCondition(JsonObject object, String prop, String value)
	{
		JsonObject object1 = new JsonObject();
		object1.addProperty(prop, value);
		object.add("when", object1);
	}
	
	static void addModel(JsonObject object, String locate)
	{
		JsonObject object1 = new JsonObject();
		object1.addProperty("model", locate);
		object.add("apply", object1);
	}
	
	static void addModel(JsonObject object, String locate, int x, int y)
	{
		JsonObject object1 = new JsonObject();
		object1.addProperty("model", locate);
		object1.addProperty("x", x * 90);
		object1.addProperty("y", y * 90);
		object.add("apply", object1);
	}
	
	static void makeJson(String sourceLocate, String pathName, JsonObject object)
	{
		if(!pathName.endsWith(".json"))
		{
			pathName += ".json";
		}
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
			gson.toJson(object, writer);
			System.out.println("Generated json at ./" + pathName);
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