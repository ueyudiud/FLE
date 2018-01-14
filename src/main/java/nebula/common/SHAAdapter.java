/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * @author ueyudiud
 */
class SHAAdapter extends TypeAdapter<String>
{
	static final SHAAdapter INSTANCE = new SHAAdapter();
	
	String name;
	
	@Override
	public void write(JsonWriter out, String value) throws IOException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String read(JsonReader in) throws IOException
	{
		in.beginArray();
		while (true)
		{
			if (in.peek() == JsonToken.END_ARRAY)
			{
				break;
			}
			in.beginObject();
			in.nextName();//name:
			if (this.name.equals(in.nextString()))
			{
				in.skipValue();//path:
				in.skipValue();
				in.skipValue();//sha:
				return in.nextString();
			}
			else
			{
				while (in.peek() != JsonToken.END_OBJECT)
				{
					in.skipValue();
					in.skipValue();
				}
				in.endObject();
			}
		}
		throw new IOException(this.name + " not found");
	}
	
}