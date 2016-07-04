package fle.core.world.climate;

import java.util.Arrays;
import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.world.biome.BiomeBase;
import fle.core.util.BlockInfo;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public abstract class Climate
{
	public static final IRegister<Climate> climates = new Register();
	
	public final int climateID;
	
	public boolean enableRain = true;
	public boolean enableSnow = false;
	
	public Climate(int id, String name)
	{
		climates.register(id, name, this);
		this.climateID = id;
	}
	
	public Climate setDisableRain()
	{
		enableRain = false;
		return this;
	}
	
	public Climate setEnableSnow()
	{
		enableSnow = true;
		return this;
	}
	
	public boolean enableRain()
	{
		return enableRain;
	}
	
	public boolean enableSnow()
	{
		return enableSnow;
	}
	
	public void decorate(World world, Random rand, int x, int z, BiomeBase biome)
	{
		
	}
	
	public ITreeGenerator getTreeGenerator(int x, int z, Random random)
	{
		return null;
	}
		
	public BlockInfo[] genTopBlock(int x, int z, Random random, int height, boolean underWater, boolean isFirstTop, double layer)
	{
		BlockInfo[] infos = new BlockInfo[height];
		Arrays.fill(infos, new BlockInfo(Blocks.dirt));
		if(!underWater && height > 0)
		{
			infos[0] = new BlockInfo(Blocks.grass);
		}
		if(height > 1)
		{
			infos[height - 1] = new BlockInfo(Blocks.gravel);
		}
		return infos;
	}
}