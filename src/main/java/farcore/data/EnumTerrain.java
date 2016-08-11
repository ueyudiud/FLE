package farcore.data;

public enum EnumTerrain
{
	tectogene(0.8F, 3.0F, -2.4F, 0.7F, true),
	deep_ocean(1.0F, 0.3F, -1.5F, 0.1F, true),
	ocean(-0.8F, 0.1F, true),
	channel(1.2F, 0.3F, -0.35F, 0.1F, true),
	ridge(-0.3F, 0.2F, true),
	ridge_high(1.0F, 0.3F, 0.5F, 0.3F, true),
	ocean_valley(1.0F, 0.8F, -0.5F, 0.2F, true),
	@Deprecated
	depression(-0.01F, 0.1F),
	plain(0.2F, 0.1F),
	basin(0.4F, 0.2F),
	hills(0.5F, 0.5F),
	mountain(0.6F, 0.7F),
	hi_mountain(0.8F, 0.9F),
	plateau(1.2F, 0.3F),
	ex_mountain(1.5F, 0.3F),
	river(2.5F, 0.6F, -0.3F, 0.0625F, true);

	static
	{
		plateau.canRiverGen = true;
		mountain.canRiverGen = true;
		hills.canRiverGen = true;
		basin.canRiverGen = true;
		plain.canRiverGen = true;
		river.isOcean = false;
	}
	
	public final float highWeight;
	public final float lowWeight;
	public final boolean isWater;
	public boolean isOcean;
	public final float root;
	public final float rand;
	public boolean canRiverGen = false;
	
	EnumTerrain(float root, float rand)
	{
		this(root, rand, false);
	}
	EnumTerrain(float root, float rand, boolean isOcean)
	{
		this(1.0F, 0.5F, root, rand, isOcean);
	}
	EnumTerrain(float lWeight, float hWeight, float root, float rand, boolean isOcean)
	{
		this.isOcean = isWater = isOcean;
		highWeight = hWeight;
		lowWeight = lWeight;
		this.root = root;
		this.rand = rand;
	}
}