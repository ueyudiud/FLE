package fle.api.config;

import java.io.IOException;

import com.google.gson.JsonElement;

public interface IJsonLoader
{
	void readJson(JsonElement e) throws IOException;
}