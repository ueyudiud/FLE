import com.google.gson.Gson;

public class Debug
{
	public static void main(String[] args)
	{
		try
		{
			JSObj js = new JSObj("a");
			Gson gson = new Gson();
			String str = gson.toJson(js);
			System.out.print(str);
			System.out.print(gson.fromJson(str, JSObj.class).name);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
}