package farcore.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.collection.Ety;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;

public class ModelFileCreator
{
	static final Gson GSON = new Gson();

	static void provideGroupItemInfo(String sourceLocate, MatCondition condition)
	{
		for(Mat material : Mat.materials())
		{
			if(condition.isBelongTo(material))
			{
				String locate = "group/" + condition.orePrefix.toLowerCase() + "/" + material.name;
				JsonObject object = new JsonObject();
				object.addProperty("parent", "item/generated");
				addTextures(object, "layer0", material.modid + ":items/" + locate);
				makeJson(sourceLocate, material.modid + "/models/item/"+ locate, object);
			}
		}
	}
	
	static void providePlankAndSlabInfo(String sourceLocate, Mat material)
	{
		JsonObject object = forgeState(),
				object2 = new JsonObject(),
				object3;
		object2.addProperty("transform", "forge:default-block");
		object2.addProperty("model", "farcore:base_simple");
		object.add("defaults", object2);
		object2 = new JsonObject();
		object3 = new JsonObject();
		addVariant(object3, "default", "all", material.modid + ":blocks/planks/" + material.name);
		addVariant(object3, "fire_resistance", "all", material.modid + ":blocks/planks/" + material.name);
		addVariant(object3, "anticorrosive", "all", material.modid + ":blocks/planks/" + material.name);
		addVariant(object3, "broke", "all", material.modid + ":blocks/planks/" + material.name + "_broke");
		object2.add("state", object3);
		object.add("variants", object2);
		makeJson(sourceLocate, material.modid + "/blockstates/plank/" + material.name, object);
		object = forgeState();
		JsonObject object1 = new JsonObject();
		object1.addProperty("transform", "forge:default-block");
		object.add("defaults", object1);
		object1 = new JsonObject();
		object2 = new JsonObject();
		addVarant(object2, "down", "farcore:slab", 0, 0);
		addVarant(object2, "up", "farcore:slab", 180, 0);
		addVarant(object2, "north", "farcore:slab", 90, 0);
		addVarant(object2, "south", "farcore:slab", 90, 180);
		addVarant(object2, "west", "farcore:slab", 90, 90);
		addVarant(object2, "east", "farcore:slab", 90, 270);
		addVarant(object2, "double_ud", "farcore:slab_double", 0, 0);
		addVarant(object2, "double_ns", "farcore:slab_double", 90, 0);
		addVarant(object2, "double_we", "farcore:slab_double", 90, 90);
		object1.add("facing", object2);
		object2 = new JsonObject();
		addVariant(object2, "default", "all", material.modid + ":blocks/planks/" + material.name);
		addVariant(object2, "fire_resistance", "all", material.modid + ":blocks/planks/" + material.name);
		addVariant(object2, "anticorrosive", "all", material.modid + ":blocks/planks/" + material.name);
		addVariant(object2, "broke", "all", material.modid + ":blocks/planks/" + material.name + "_broke");
		object1.add("state", object2);
		object.add("variants", object1);
		makeJson(sourceLocate, material.modid + ":blockstates/plank/slab/" + material.name, object);
	}
	
	static void provideLogAndLeavesInfo(String sourceLocate, Mat material)
	{
		JsonObject object = forgeState(),
				object2 = new JsonObject(),
				object3 = new JsonObject();
		object2.addProperty("transform", "forge:default-block");
		object2.addProperty("model", "farcore:base_oneaxis");
		object.add("defaults", object2);
		addTextures(object3,
				new Ety("side", material.modid + ":blocks/logs/" + material.name + "_side"),
				new Ety("top", material.modid + ":blocks/logs/" + material.name + "_top"));
		object2.add("textures", object3);
		object2 = new JsonObject();
		object3 = new JsonObject();
		object3.add("none", new JsonObject());
		addVarant(object3, "x", 90, 90);
		addVarant(object3, "y", 0, 0);
		addVarant(object3, "z", 90, 0);
		object2.add("axis", object3);
		object.add("variants", object2);
		makeJson(sourceLocate, material.modid + "/blockstates/log.natural." + material.name, object);
		makeJson(sourceLocate, material.modid + "/blockstates/log.artifical." + material.name, object);
		object = forgeState();
		object2 = new JsonObject();
		object2.addProperty("transform", "forge:default-block");
		object2.addProperty("model", "farcore:leaves");
		addTextures(object2, "all", material.modid + ":blocks/leaves/" + material.name);
		object.add("defaults", object2);
		makeJson(sourceLocate, material.modid + "/blockstates/leaves." + material.name, object);
		makeJson(sourceLocate, material.modid + "/blockstates/leaves.core." + material.name, object);
	}
	
	static void provideRockSlabInfo(String sourceLocate, Mat material)
	{
		JsonObject object = forgeState();
		JsonObject object1 = new JsonObject();
		object1.addProperty("transform", "forge:default-block");
		object.add("defaults", object1);
		object1 = new JsonObject();
		JsonObject object2 = new JsonObject();
		for(RockType type : RockType.values())
		{
			JsonObject object3 = new JsonObject();
			addTextures(object3, material.modid + ":blocks/rock/" + material.name + "/" +
					(type == RockType.cobble_art ? RockType.cobble.name() : type.name()));
			object2.add(type.getName(), object3);
		}
		object1.add("type", object2);
		object2 = new JsonObject();
		addVarant(object2, "down", "farcore:slab", 0, 0);
		addVarant(object2, "up", "farcore:slab", 180, 0);
		addVarant(object2, "north", "farcore:slab", 90, 0);
		addVarant(object2, "south", "farcore:slab", 90, 180);
		addVarant(object2, "west", "farcore:slab", 90, 90);
		addVarant(object2, "east", "farcore:slab", 90, 270);
		addVarant(object2, "double_ud", "farcore:slab_double", 0, 0);
		addVarant(object2, "double_ns", "farcore:slab_double", 90, 0);
		addVarant(object2, "double_we", "farcore:slab_double", 90, 90);
		object1.add("facing", object2);
		object.add("variants", object1);
		makeJson(sourceLocate, material.modid + "/blockstates/rock/slab/" + material.name + ".json", object);
	}

	static void provideRockInfo(String sourceLocate, Mat material)
	{
		JsonObject object = new JsonObject();
		object.addProperty("forge_marker", 1);
		JsonObject object1 = new JsonObject();
		object1.addProperty("transform", "forge:default-block");
		object1.addProperty("model", "farcore:base_simple");
		object.add("defaults", object1);
		object1 = new JsonObject();
		JsonObject object2 = new JsonObject();
		for(RockType type : RockType.values())
		{
			JsonObject object3 = new JsonObject();
			object3.addProperty("all", material.modid + ":blocks/rock/" + material.name + "/" +
					(type == RockType.cobble_art ? RockType.cobble.name() : type.name()));
			JsonObject object4 = new JsonObject();
			object4.add("textures", object3);
			object2.add(type.getName(), object4);
		}
		object1.add("type", object2);
		object.add("variants", object1);
		makeJson(sourceLocate, material.modid + "/blockstates/rock/" + material.name + ".json", object);
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
	
	static void addTextures(JsonObject object, String locate)
	{
		addTextures(object, "all", locate);
	}

	static void addTextures(JsonObject object, String name, String locate)
	{
		JsonObject object1 = new JsonObject();
		object1.addProperty(name, locate);
		object.add("textures", object1);
	}

	static void addTextures(JsonObject object, Entry<String, String>...locates)
	{
		JsonObject object1 = new JsonObject();
		for(Entry<String, String> entry : locates)
		{
			object1.addProperty(entry.getKey(), entry.getValue());
		}
		object.add("textures", object1);
	}
	
	static JsonObject forgeState()
	{
		JsonObject object = new JsonObject();
		object.addProperty("forge_marker", 1);
		return object;
	}
	
	static void addVariant(JsonObject apply, String key, String replacement, String texture)
	{
		JsonObject object = new JsonObject();
		addTextures(object, replacement, texture);
		apply.add(key, object);
	}
	
	static void addVarant(JsonObject apply, String key, int x, int y)
	{
		JsonObject object = new JsonObject();
		object.addProperty("x", x);
		object.addProperty("y", y);
		apply.add(key, object);
	}
	
	static void addVarant(JsonObject apply, String key, String model, int x, int y)
	{
		JsonObject object = new JsonObject();
		object.addProperty("model", model);
		object.addProperty("x", x);
		object.addProperty("y", y);
		apply.add(key, object);
	}
	
	static void makeJson(String sourceLocate, String pathName, JsonObject object)
	{
		pathName = pathName.replace(':', '/');
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
			GSON.toJson(object, writer);
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