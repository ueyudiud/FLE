package fle.cwg.world;

/**
 * The FCWG world information.
 * @author ueyudiud
 *
 */
public class FCWGWorldInfo
{
	public static FCWGWorldInfo createNewInfo(String options)
	{
		FCWGWorldInfo info = new FCWGWorldInfo();
		if(options == null)
		{
			return info;
		}
		String[] keys;
		if(options.indexOf(',') != -1)
		{
			keys = options.split(",");
		}
		else
		{
			keys = new String[]{options};
		}
		for(String key : keys)
		{
			if(key.indexOf('=') == -1)
			{
				continue;
			}
			String[] entry;
			String key1 = key.trim();
			entry = key1.split("=");
			if(entry.length != 2)
			{
				continue;
			}
			switch (entry[0])
			{
			case "coreEfficiency":
				info.coreEfficiency = Float.valueOf(entry[1]);
				break;
			case "heightCheckRange":
				info.heightCheckRange = Integer.valueOf(entry[1]);
				break;
			case "baseHeight":
				info.baseHeight = Float.valueOf(entry[1]);
				break;
			case "rangeHeight":
				info.rangeHeight = Float.valueOf(entry[1]);
				break;
			case "rootHeightEfficiency":
				info.rootHeightEfficiency = Float.valueOf(entry[1]);
				break;
			case "randHeightEfficiency":
				info.randHeightEfficiency = Float.valueOf(entry[1]);
				break;
			case "mainNoiseSize":
				info.mainNoiseSize = Float.valueOf(entry[1]);
				break;
			case "rangeNoiseXZSize":
				info.rangeNoiseXZSize = Float.valueOf(entry[1]);
				break;
			case "slopeNoiseXZSize":
				info.slopeNoiseXZSize = Float.valueOf(entry[1]);
				break;
			case "decorateNoiseSize":
				info.decorateNoiseSize = Float.valueOf(entry[1]);
				break;
			case "seaLevel":
				info.seaLevel = Integer.valueOf(entry[1]);
				break;
			case "tempNoiseOctave":
				info.tempNoiseOctave = Integer.valueOf(entry[1]);
				break;
			case "tempNoiseSize":
				info.tempNoiseSize = Float.valueOf(entry[1]);
				break;
			case "rainfallNoiseOctave":
				info.rainfallNoiseOctave = Integer.valueOf(entry[1]);
				break;
			case "rainfallNoiseSize":
				info.rainfallNoiseSize = Float.valueOf(entry[1]);
				break;
			case "startLandChance":
				info.startLandChance = Integer.valueOf(entry[1]);
				break;
			case "secondLandChance":
				info.secondLandChance = Integer.valueOf(entry[1]);
				break;
			case "terrainNoiseSize":
				info.terrainNoiseSize = Float.valueOf(entry[1]);
				break;
			case "terrainNoiseIteration":
				info.terrainNoiseIteration = Integer.valueOf(entry[1]);
				break;
			case "enableRiver":
				info.enableRiver = Boolean.getBoolean(entry[1]);
				break;
			default:
				break;
			}
		}
		return info;
	}
	
	//Generations.
	//Height related.
	/**
	 * The height efficiency of core biomes.<br>
	 * Lower value will give higher weight for main height efficiency.
	 */
	public float coreEfficiency = 0.4F;
	/**
	 * The height check range, higher value will smooth the
	 * surface.<br>
	 * BUT USE MORE MEMORY TO GENERATE WORLD!<br>
	 * Suggested use 2~4 for generate.
	 */
	public int heightCheckRange = 4;
	/**
	 * The base height, about sea level.
	 */
	public float baseHeight = 16.5F;
	/**
	 * The height range.
	 */
	public float rangeHeight = 4.0F;
	/**
	 * Rooted height efficiency.
	 */
	public float rootHeightEfficiency = 9.5F;
	/**
	 * Random height efficiency.
	 */
	public float randHeightEfficiency = 0.25F;
	//Noise related
	/**
	 * The basic height noise size.
	 */
	public float mainNoiseSize = 100F;
	/**
	 * The range height noise size.
	 */
	public float rangeNoiseXZSize = 200F;
	public float rangeNoiseYSize = 128F;

	public float slopeNoiseXZSize = 8F;
	public float slopeNoiseYSize = 4F;
	
	public float decorateNoiseSize = 0.03125F;
	//Surface related
	public int seaLevel = 127;
	//Biome related
	public int tempNoiseOctave = 7;
	public float tempNoiseSize = 1.8F;
	
	public int rainfallNoiseOctave = 6;
	public float rainfallNoiseSize = 2.3F;
	//Terrain related
	public int startLandChance = 11;
	public int secondLandChance = 9;
	
	public float terrainNoiseSize = .48F;

	public int terrainNoiseIteration = 6;
	
	public boolean enableRiver = true;
	
	@Override
	public String toString()
	{
		return "baseHeight=" + baseHeight + ","	+
				"coreEfficiency=" + coreEfficiency + "," +
				"decorateNoiseSize=" + decorateNoiseSize + "," +
				"enableRiver=" + enableRiver + "," +
				"heightCheckRange=" + heightCheckRange + "," +
				"mainNoiseSize=" + mainNoiseSize + "," +
				"rainfallNoiseOctave=" + rainfallNoiseOctave + "," +
				"rainfallNoiseSize=" + rainfallNoiseSize + "," +
				"randHeightEfficiency=" + randHeightEfficiency + "," +
				"rangeHeight=" + rangeHeight + "," +
				"rangeNoiseXZSize=" + rangeNoiseXZSize + "," +
				"rangeNoiseYSize=" + rangeNoiseYSize + "," +
				"rootHeightEfficiency=" + rootHeightEfficiency + "," +
				"seaLevel=" + seaLevel + "," +
				"secondLandChance=" + secondLandChance + "," +
				"slopeNoiseXZSize=" + slopeNoiseXZSize + "," +
				"slopeNoiseYSize=" + slopeNoiseYSize + "," +
				"startLandChance=" + startLandChance + "," +
				"tempNoiseOctave=" + tempNoiseOctave + "," +
				"tempNoiseSize=" + tempNoiseSize + "," +
				"terrainNoiseIteration=" + terrainNoiseIteration + "," +
				"terrainNoiseSize=" + terrainNoiseSize;
	}
}