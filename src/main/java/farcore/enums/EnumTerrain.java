package farcore.enums;

public enum EnumTerrain
{
	channel(-0.3F),
	depression(0F),
	plain(0.15F),
	basin(0.5F),
	hills(0.6F),
	mountain(0.75F),
	plateau(0.9F),
	ex_mountain(0.96F);
	
	public static EnumTerrain get(float height)
	{
		for(EnumTerrain terrain : basic)
		{
			if(height < terrain.height)
			{
				return terrain;
			}
		}
		return EnumTerrain.ex_mountain;
	}
	
	private static final EnumTerrain[] basic = {channel, depression, plain, hills, mountain, plateau, ex_mountain};
	
	public final float height;
	
	EnumTerrain(float h)
	{
		this.height = h;
	}
}