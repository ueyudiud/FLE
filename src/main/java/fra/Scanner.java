/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.io.BufferedReader;
import java.io.Reader;

import javax.script.ScriptException;

/**
 * @author ueyudiud
 */
class Scanner
{
	TokenType type;
	Reader reader;
	IFSObject literal;
	String identifier;
	
	Scanner(Reader reader)
	{
		if (!reader.markSupported())
			reader = new BufferedReader(reader);
		this.reader = reader;
	}
	
	void scan() throws ScriptException
	{
		
	}
	
	void scan(TokenType expected) throws ScriptException
	{
		
	}
	
	void error(String cause) throws ScriptException
	{
		
	}
}
