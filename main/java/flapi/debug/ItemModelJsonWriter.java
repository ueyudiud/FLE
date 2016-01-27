package flapi.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import farcore.util.FleLog;

public class ItemModelJsonWriter
{
	private static File $(File file) throws IOException
	{
		if (!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return file;
	}
	
	private static JsonWriter $(JsonWriter writer)
	{
		writer.setIndent("	");
		return writer;
	}
	
	public class Display
	{
		final String name;
		float[] rotation;
		float[] translation;
		float[] scale;
		
		Display(String name)
		{
			this.name = name;
		}
		
		public Display rotation(float x, float y, float z)
		{
			rotation = new float[]{x, y, z};
			return this;
		}
		
		public Display translation(float x, float y, float z)
		{
			translation = new float[]{x, y, z};
			return this;
		}
		
		public Display scale(float x, float y, float z)
		{
			scale = new float[]{x, y, z};
			return this;
		}
		
		public ItemModelJsonWriter build()
		{
			JsonObject obj = new JsonObject();
			if (rotation != null)
				obj.add("rotation", create(rotation));
			if (scale != null)
				obj.add("scale", create(scale));
			if (translation != null)
				obj.add("translation", create(translation));
			display.add(name, obj);
			return ItemModelJsonWriter.this;
		}
	}
	
	private boolean build;
	
	private JsonObject display = new JsonObject();
	private JsonObject object = new JsonObject();
	private JsonWriter writer;
	
	void reset()
	{
		display = new JsonObject();
		object = new JsonObject();
		writer = null;
		build = false;
	}
	
	public ItemModelJsonWriter writer(File file) throws IOException
	{
		return writer(new BufferedWriter(new FileWriter($(file))));
	}
	
	public ItemModelJsonWriter writer(Writer writer)
	{
		return writer($(new JsonWriter(writer)));
	}
	
	public ItemModelJsonWriter writer(JsonWriter writer)
	{
		this.writer = writer;
		return this;
	}
	
	public ItemModelJsonWriter parent(String parent)
	{
		check();
		if (object.has("parent"))
			throw new IllegalArgumentException();
		object.addProperty("parent", parent);
		return this;
	}
	
	public ItemModelJsonWriter texture(int layer, String texture)
	{
		return texture("layer" + layer, texture);
	}
	
	public ItemModelJsonWriter texture(String texture)
	{
		return texture("layer", texture);
	}
	
	public ItemModelJsonWriter texture(String tag, String texture)
	{
		check();
		if (!object.has("textures"))
		{
			JsonObject object = new JsonObject();
			object.addProperty(tag, texture);
			this.object.add("textures", object);
		}
		else
		{
			object.get("texutures").getAsJsonObject().addProperty(tag, texture);
		}
		return this;
	}
	
	public ItemModelJsonWriter textures(String... textures)
	{
		check();
		if (!object.has("textures"))
		{
			JsonObject object = new JsonObject();
			for (int i = 0; i < textures.length; ++i)
				object.addProperty("layer" + i, textures[i]);
			this.object.add("textures", object);
		}
		else
		{
			throw new IllegalArgumentException("The texture are already set.");
		}
		return this;
	}
	
	public ItemModelJsonWriter particle(String name)
	{
		check();
		if (!object.has("textures"))
		{
			JsonObject object = new JsonObject();
			object.addProperty("particle", name);
			this.object.add("textures", object);
		}
		else
		{
			object.get("texutures").getAsJsonObject().addProperty("particle",
					name);
		}
		return this;
	}
	
	public Display display(String name)
	{
		check();
		if (object.has(name))
			throw new IllegalArgumentException();
		return new Display(name);
	}
	
	/**
	 * A default display for block.
	 * 
	 * @return
	 */
	public ItemModelJsonWriter displayB()
	{
		return display("thirdperson").rotation(10, -45, 170)
				.translation(0, 1.5F, -2.75F).scale(0.375F, 0.375F, 0.375F)
				.build();
	}
	
	public JsonObject build()
	{
		check();
		if (!display.entrySet().isEmpty())
			object.add("display", display);
		build = true;
		return object;
	}
	
	public void write() throws IOException
	{
		if (!build)
			build();
		try
		{
			new Gson().toJson(object, writer);
		}
		catch (JsonIOException exception)
		{
			throw new IOException(exception);
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (IOException exception)
			{
				FleLog.error("Can not close item model file.", exception);
			}
		}
	}
	
	private void check()
	{
		if (writer == null)
			throw new IllegalArgumentException(
					"Do not current a writer to write!");
		if (build)
			throw new IllegalArgumentException("Can not init two times!");
	}
	
	private JsonArray create(float[] list)
	{
		JsonArray array = new JsonArray();
		for (int i = 0; i < list.length; ++i)
			array.add(new JsonPrimitive(list[i]));
		return array;
	}
}