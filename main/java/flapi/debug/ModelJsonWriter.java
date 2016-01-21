package flapi.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.EnumMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import com.sun.javafx.geom.Vec3f;

import net.minecraft.util.EnumFacing;

public class ModelJsonWriter
{
	static final Vec3f DEFAULT_FROM = new Vec3f(0.0F, 0.0F, 0.0F);
	static final Vec3f DEFAULT_TO = new Vec3f(1.0F, 1.0F, 1.0F);
	
	private static JsonWriter $(JsonWriter writer)
	{
		writer.setIndent(" ");
		return writer;
	}
	
	private boolean build;
	
	private JsonArray elements = new JsonArray();
	private JsonObject object = new JsonObject();
	private JsonWriter writer;
	
	void reset()
	{
		build = false;
		elements = new JsonArray();
		object = new JsonObject();
		
	}
	
	public ModelJsonWriter(File file) throws IOException
	{
		this(new BufferedWriter(new FileWriter(file)));
		if (!file.exists())
		{
			file.mkdirs();
		}
	}
	
	public ModelJsonWriter(Writer writer)
	{
		this($(new JsonWriter(writer)));
	}
	
	public ModelJsonWriter(JsonWriter writer)
	{
		this.writer = writer;
	}
	
	public ModelJsonWriter setParent(String parent)
	{
		check();
		if (object.has("parent"))
			throw new IllegalArgumentException("Parent had already set.");
		object.addProperty("parent", parent);
		return this;
	}
	
	public ModelJsonWriter setAmbientOcclusion(boolean value)
	{
		check();
		if (object.has("ambientocclusion"))
			throw new IllegalArgumentException(
					"Ambient occlusion had already set.");
		object.addProperty("ambientocclusion", value);
		return this;
	}
	
	public ModelJsonWriter setTextures(String tag, String value)
	{
		check();
		if (object.has("textures"))
		{
			if (object.getAsJsonObject("textures").has(tag))
				throw new IllegalArgumentException(
						"Texture tag " + tag + " had already set.");
			object.getAsJsonObject("textures").addProperty(tag, value);
		}
		else
		{
			JsonObject object1 = new JsonObject();
			object1.addProperty(tag, value);
			object.add("textures", object1);
		}
		return this;
	}
	
	public ElementBuilder element()
	{
		check();
		return new ElementBuilder();
	}
	
	public JsonObject build()
	{
		check();
		object.add("elements", elements);
		build = true;
		return object;
	}
	
	public JsonObject getObject()
	{
		if (!build)
			build();
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
	}
	
	private class ElementBuilder
	{
		private class FaceBuilder
		{
			final EnumFacing facing;
			boolean build = false;
			float[] uv;
			String texture;
			EnumFacing cullface;
			int rotation;
			
			FaceBuilder(EnumFacing facing)
			{
				this.facing = facing;
				this.texture = "#" + facing.getName();
			}
			
			private void check()
			{
				if (build)
					throw new IllegalArgumentException(
							"This face is already build!");
			}
			
			public FaceBuilder uv(float x1, float y1, float x2, float y2)
			{
				check();
				uv = new float[]{x1, y1, x2, y2};
				return this;
			}
			
			public FaceBuilder texture(String texture)
			{
				check();
				this.texture = "#" + texture;
				return this;
			}
			
			public FaceBuilder cullface(EnumFacing facing)
			{
				check();
				this.cullface = facing;
				return this;
			}
			
			public FaceBuilder rotate(int i)
			{
				check();
				this.rotation = 90 * i;
				return this;
			}
			
			public ElementBuilder build()
			{
				check();
				build = true;
				map.put(facing, this);
				return ElementBuilder.this;
			}
		}
		
		EnumMap<EnumFacing, FaceBuilder> map = new EnumMap(EnumFacing.class);
		float[] from;
		float[] to;
		float[] origin;
		String axis;
		float angle;
		boolean rescale;
		boolean shade = true;
		
		public ElementBuilder from(float x, float y, float z)
		{
			from = new float[]{x, y, z};
			return this;
		}
		
		public ElementBuilder to(float x, float y, float z)
		{
			to = new float[]{x, y, z};
			return this;
		}
		
		public ElementBuilder rotationOrigin(float x, float y, float z)
		{
			origin = new float[]{x, y, z};
			return this;
		}
		
		public ElementBuilder rotation(char axis, float angle, boolean rescale)
		{
			if (axis != 'x' && axis != 'y' && axis != 'z')
				throw new IllegalArgumentException();
			this.axis = Character.toString(axis);
			this.angle = angle;
			this.rescale = rescale;
			return this;
		}
		
		public ElementBuilder rotation(char axis, float angle)
		{
			return rotation(axis, angle, false);
		}
		
		public ElementBuilder shade(boolean shade)
		{
			this.shade = shade;
			return this;
		}
		
		public FaceBuilder face(EnumFacing face)
		{
			if (map.containsKey(face))
				throw new IllegalArgumentException(
						"The facing " + face.name() + " has already build.");
			return new FaceBuilder(face);
		}
		
		public ModelJsonWriter build()
		{
			if (from != null ^ to != null)
				throw new IllegalArgumentException();
			JsonObject object = new JsonObject();
			if (from != null && to != null)
			{
				object.add("from", create(from));
				object.add("to", create(to));
			}
			if (axis != null)
			{
				JsonObject rotation = new JsonObject();
				if (origin != null)
				{
					rotation.add("origin", create(origin));
				}
				rotation.addProperty("axis", axis);
				rotation.addProperty("angle", angle);
				rotation.addProperty("rescale", rescale);
				object.add("rotation", rotation);
			}
			if (!map.isEmpty())
			{
				JsonObject faces = new JsonObject();
				for (FaceBuilder builder : map.values())
				{
					JsonObject face = new JsonObject();
					if (builder.uv != null)
					{
						face.add("uv", create(builder.uv));
					}
					face.addProperty("texture", builder.texture);
					if (builder.cullface != null)
						face.addProperty("cullface",
								builder.cullface.getName());
					if (builder.rotation != 0)
						face.addProperty("rotation", builder.rotation);
					faces.add(builder.facing.getName(), face);
				}
				object.add("faces", faces);
			}
			elements.add(object);
			return ModelJsonWriter.this;
		}
	}
	
	private void check()
	{
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