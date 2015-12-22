package flapi.solid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SolidJsonStack
{
	@SerializedName("solidName")
	@Expose
	public String solid;
	@SerializedName("amount")
	@Expose
	public int amount;
	
	public SolidJsonStack()
	{
		
	}
	
	public SolidJsonStack(SolidStack input)
	{
		this.solid = input.getSolid().getSolidName();
		this.amount = input.size;
	}

	public SolidStack getSolid()
	{
		return new SolidStack(SolidRegistry.getSolidFromName(solid), amount);
	}
	
	public boolean hasStack()
	{
		return solid != null ? SolidRegistry.hasSolid(solid) : false;
	}
}