import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSObj
{
	@Expose
	@SerializedName("name")
	public String name;
	
	public JSObj(String aName)
	{
		name = aName;
	}
}
