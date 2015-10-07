package fle.core.world;

import java.util.HashMap;
import java.util.Map;
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
import fle.api.crop.CropCard;
import fle.api.material.MaterialOre;
import fle.api.material.MaterialRock;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;
import fle.api.world.TreeInfo;
import fle.core.block.BlockLog;
import fle.core.init.Crops;
import fle.core.init.IB;
import fle.core.init.Materials;

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
		TreeInfo.genFlag = false;
		switch(world.provider.dimensionId)
		{
		case 1 : this.generateEnd(random, chunkX * 16, chunkZ * 16, world);
		case 0 : this.generateSurface(random, chunkX * 16, chunkZ * 16, world);
		case -1 : this.generateNether(random, chunkX * 16, chunkZ * 16, world);
		}
		TreeInfo.genFlag = true;
	}
	
	public void generateEnd(Random random, int x, int z, World world)
	{
		
	}

	public void generateSurface(Random random, int x, int z, World world)
	{
		switch(random.nextInt(16))
		{
		case 0 :
		{
			genOre(random, world, x, z, 210, 100, 0.5D, 120, Blocks.stone, g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1));
			genOre(random, world, x, z, 210, 100, 0.5D, 120, Blocks.glass, g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1));
		}
		break;
		case 1 :
		{
			genOre(random, world, x, z, 180, 40, 0.3D, 70, Blocks.stone, g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6));
			genOre(random, world, x, z, 180, 40, 0.3D, 70, Blocks.glass, g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6));
		}
		break;
		case 2 :
		{
			genOre(random, world, x, z, 180, 100, 0.4D, 130, Blocks.stone, g(Materials.Orpiment, 8), g(Materials.Realgar, 12), g(Materials.Arsenolite, 2), g(Materials.Nickeline, 1), g(Materials.Arsenopyrite, 2), g(Materials.Schorodite, 1), g(Materials.Erythrite, 2));
			genOre(random, world, x, z, 180, 100, 0.4D, 130, Blocks.glass, g(Materials.Orpiment, 8), g(Materials.Realgar, 12), g(Materials.Arsenolite, 2), g(Materials.Nickeline, 1), g(Materials.Arsenopyrite, 2), g(Materials.Schorodite, 1), g(Materials.Erythrite, 2));
		}
		break;
		case 3 :;
		case 4 :;
		case 5 :
		{
			if(genRock(random, world, x, z, 240, 120, 0.9D, 480, Blocks.stone, Materials.Limestone))
			{
				switch(random.nextInt(16))
				{
				case 0 : ;
				genOre(random, world, x, z, 240, 120, 1.0D, 120, IB.rock, g(Materials.Gelenite, 5), g(Materials.Sphalerite, 7), g(Materials.Cassiterite, 10), g(Materials.Stannite, 7));
				genOre(random, world, x, z, 240, 120, 1.0D, 120, Blocks.glass, g(Materials.Gelenite, 5), g(Materials.Sphalerite, 7), g(Materials.Cassiterite, 10), g(Materials.Stannite, 7));
				break;
				}
			}
		}
		break;
		default :;
		break;
		}
		int s = world.getBiomeGenForCoords(x * 16 + 8, z * 16 + 8).theBiomeDecorator.treesPerChunk;
		Map<TreeInfo, Integer> info = new HashMap();
		for(TreeInfo tInfo : BlockLog.trees)
		{
			int weight = tInfo.getGenerateWeight(world, x, z);
			if(weight > 0) info.put(tInfo, weight);
		}
		WeightHelper<TreeInfo> wh = new WeightHelper<TreeInfo>(info);
		if(random.nextInt(4) == 0 && wh.allWeight() > 0)
		{
			generateTree(wh.randomGet(), world, random, x, z);
		}
		switch(random.nextInt(128))
		{
		case 33:;
		case 17:;
		case 1 : generateCrop(Crops.millet, world, random, x, z, 125, 8);
		break;
		case 34:;
		case 18:;
		case 2 : generateCrop(Crops.ramie, world, random, x, z, 125, 8);
		break;
		case 35:;
		case 19:;
		case 3 : generateCrop(Crops.soybean, world, random, x, z, 125, 8);
		break;
		case 36:;
		case 20:;
		case 4 : generateCrop(Crops.cotton, world, random, x, z, 125, 8);
		break;
		case 21:;
		case 5 : generateCrop(Crops.potato, world, random, x, z, 125, 8);
		break;
		case 22:;
		case 6 : generateCrop(Crops.sweet_potato, world, random, x, z, 125, 8);
		break;
		case 39:;
		case 23:;
		case 7 : generateCrop(Crops.wheat, world, random, x, z, 125, 8);
		break;
		case 8 :;
		case 9 :;
		case 10 :;
		case 11 :;
		case 12 :;
		break;
		case 13 :;
		case 14 :;
		case 15 :;
		case 16 : generateVine(world, random, x, z, 100, 20);
		break;
		default : break;
		}
	}
	
	public void generateNether(Random random, int x, int z, World world)
	{
		
	}
	
	public boolean generateCrop(CropCard card, World world, Random random, int x, int z, int size, int count)
	{
		int X = x + random.nextInt(15);
		int Z = z + random.nextInt(15);
		return new FleCropGen(size, count, card).generate(world, random, X, world.getTopSolidOrLiquidBlock(X, Z), Z);
	}
	
	public boolean generateVine(World world, Random random, int x, int z, int size, int count)
	{
		int X = x + random.nextInt(15);
		int Z = z + random.nextInt(15);
		return new FleVineGen(size, count).generate(world, random, X, world.getTopSolidOrLiquidBlock(X, Z), Z);
	}
	
	public boolean generateTree(TreeInfo tInfo, World world, Random random, int x, int z)
	{
		for (int count = tInfo.getGenerateWeight(world, x + 8, z + 8) * 3; count > 0; count--)
		{
			int y;
			for (y = 255; world.isAirBlock(x, y - 1, z) && y > 0; y--);
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
	
	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, double chance, int size, Block base, Stack<MaterialOre>...aOres)
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
	
	private static Stack<MaterialOre> g(MaterialOre ore, int size)
	{
		return new Stack(ore, size);
	}
}