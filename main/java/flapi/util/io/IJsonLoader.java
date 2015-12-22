package flapi.util.io;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

public interface IJsonLoader
{
	void readJson(Gson gson, List<String> list) throws IOException;
	
	void writeJson(Gson gson, List<String> list) throws IOException;

	Class<?> getSaveClass();
}