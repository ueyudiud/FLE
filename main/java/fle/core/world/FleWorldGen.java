package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.api.material.MaterialOre;
import fle.api.material.MaterialRock;
import fle.api.util.WeightHelper;
import fle.api.world.TreeInfo;
import fle.core.block.BlockLog;
import fle.core.init.Materials;
import fle.core.util.Arrays;

public class FleWorldGen implements IWorldGenerator
{
	public FleWorldGen() 
	{
		MinecraftForge.ORE_GEN_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onGenOre(GenerateMinable evt)
	{
		if(evt.type == EventType.COAL || evt.type == EventType.DIAMOND || evt.type == EventType.GOLD || evt.type == EventType.IRON || evt.type == EventType.QUARTZ || evt.type == EventType.LAPIS || evt.type == EventType.REDSTONE)
		{
			evt.setResult(Result.DENY);
		}
	}
	
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		switch(world.provider.dimensionId)
		{
		case 1 : this.generateEnd(random, chunkX * 16, chunkZ * 16, world);
		case 0 : this.generateSurface(random, chunkX * 16, chunkZ * 16, world);
		case -1 : this.generateNether(random, chunkX * 16, chunkZ * 16, world);
		}
	}
	
	public void generateEnd(Random random, int x, int z, World world)
	{
		
	}

	public void generateSurface(Random random, int x, int z, World world)
	{
		int a = random.nextInt(16);
		switch(a)
		{
		case 0 :
		{
			genOre(random, world, x, z, 180, 50, 0.5D, 120, Blocks.stone, g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1));
			genOre(random, world, x, z, 180, 50, 0.5D, 120, Blocks.glass, g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1));
		}
		break;
		case 1 :
		{
			genOre(random, world, x, z, 180, 10, 0.3D, 70, Blocks.stone, g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6));
			genOre(random, world, x, z, 180, 10, 0.3D, 70, Blocks.glass, g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6));
		}
		break;
		case 2 :;
		case 3 :;
		case 4 :;
		case 5 :
		{
			genRock(random, world, x, z, 180, 40, 0.8D, 180, Blocks.stone, Materials.Limestone);
		}
		break;
		default :;
		break;
		}
		for(TreeInfo tInfo : BlockLog.trees)
		{
			if(random.nextInt(5) == 0)
				generateTree(tInfo, world, random, x, z);
		}
	}
	
	public void generateNether(Random random, int x, int z, World world)
	{
		
	}
	
	public boolean generateTree(TreeInfo tInfo, World world, Random random, int x, int z)
	{
		for (int count = tInfo.getGenerateWeight(world, x + 8, z + 8); count > 0; count--)
		{
			int y;
			for (y = 128 - 1; world.isAirBlock(x, y - 1, z) && y > 0; y--);
			if (tInfo.generate(world, x, y, z, random))
				count -= 3;
			x += random.nextInt(15) - 7;
			z += random.nextInt(15) - 7;
		}

		return true;
	}

	public boolean genRock(Random rand, World world, int x, int z, int maxY, int minY, double chance, int size, Block base, MaterialRock material)
	{
		int i = 0;
		int j = 0;
		if(rand.nextGaussian() < chance)
		{
			int X = x + rand.nextInt(16);
			int Y = minY + rand.nextInt(maxY - minY);
			int Z = z + rand.nextInt(16);
			int s = (int) Math.round(Math.sqrt(size) * rand.nextGaussian() + size / 2);
			if(Y >= world.getTopSolidOrLiquidBlock(X, Z))
			{
				return false;
			}
			if(base.isReplaceableOreGen(world, X, Y, Z, base))
			{
				new FleRockGen(base, (short) MaterialRock.getOreID(material), s).generate(world, rand, X, Y, Z);
				return true;
			}
		}
		return false;
	}
	
	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, double chance, int size, Block base, MaterialOre[]...aOres)
	{
		return genOre(rand, world, x, z, maxY, minY, chance, size, base, new WeightHelper<MaterialOre>(aOres));
	}
	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, double chance, int size, Block base, WeightHelper<MaterialOre> aHelper)
	{
		int i = 0;
		int j = 0;
		if(rand.nextGaussian() < chance)
		{
			int X = x + rand.nextInt(16);
			int Y = minY + rand.nextInt(maxY - minY);
			int Z = z + rand.nextInt(16);
			int s = (int) Math.round(Math.sqrt(size) * rand.nextGaussian() + size / 2);
			if(Y >= world.getTopSolidOrLiquidBlock(X, Z))
			{
				return false;
			}
			if(base.isReplaceableOreGen(world, X, Y, Z, base))
			{
				new FleOreGen(aHelper, s, base).generate(world, rand, X, Y, Z);
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	private static MaterialOre[] g(MaterialOre ore, int size)
	{
		return Arrays.toArray(ore, new MaterialOre[size]);
	}
}