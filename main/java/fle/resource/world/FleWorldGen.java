package fle.resource.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import flapi.collection.ArrayStandardStackList;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.material.MaterialOre;
import flapi.plant.CropCard;
import flapi.plant.TreeInfo;
import fle.core.block.plant.PlantBase;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.init.Plants;
import fle.core.world.FWM;
import fle.resource.block.BlockFleLog;
import fle.resource.block.BlockFleRock;

public class FleWorldGen implements IWorldGenerator
{
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> plantGeneratorsTempCold;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> plantGeneratorsTempWarm;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> plantGeneratorsTempHot;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> superficialSmallMineableGenerators;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> middleMidMineableGenerators;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> middleSmallMineableGenerators;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> deepLargeMineableGenerators;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> deepMidMineableGenerators;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> deepSmallMineableGenerators;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> superficialRockGenerators;
	private static IStackList<Stack<WorldGenerator>,WorldGenerator> deepRockGenerators;
	
	private static boolean flag = false;
	
	private static void init()
	{
		if(flag) return;
		FleSurfaceGen gen1 = new FleSurfaceGen(8, 6) 
		{
		    @Override
			public boolean generateAt(World aWorld, Random aRand, int x, int y, int z)
		    {
				if(aWorld.getBlock(x, y, z).isAir(aWorld, x, y, z) && (aWorld.getBlock(x, y - 1, z).getMaterial() == Material.ground || aWorld.getBlock(x, y - 1, z) == Blocks.grass))
				{
					aWorld.setBlock(x, y, z, IB.plant_rattan, 0, 3);
					return true;
				}
				return false;
			}
		};
		Stack[] wgs1 = {new Stack(new FlePlantGen(8, 2, Plants.bristlegrass), 24),
				new Stack(new FleCropGen(8, 3, Plants.ramie)),
				new Stack(new FleCropGen(8, 3, Plants.soybean))};
		Stack[] wgs2 = {new Stack(new FlePlantGen(8, 12, Plants.bristlegrass), 24), 
				new Stack(new FlePlantGen(8, 4, Plants.dandelion), 8),
				new Stack(new FleCropGen(8, 2, Plants.cotton)),
				new Stack(new FleCropGen(8, 3, Plants.potato), 2),
				new Stack(new FleCropGen(8, 3, Plants.ramie), 2),
				new Stack(new FleCropGen(8, 3, Plants.soybean), 3),
				new Stack(new FleCropGen(8, 2, Plants.sweet_potato), 2),
				new Stack(new FleCropGen(8, 2, Plants.flax)),
				new Stack(new FleCropGen(8, 3, Plants.wheat), 3),
				new Stack(gen1, 6)};
		Stack[] wgs3 = {new Stack(new FlePlantGen(8, 32, Plants.bristlegrass), 24), 
				new Stack(new FlePlantGen(8, 5, Plants.dandelion), 6),
				new Stack(new FleCropGen(8, 4, Plants.cotton)),
				new Stack(new FleCropGen(8, 4, Plants.potato)),
				new Stack(new FleCropGen(8, 4, Plants.ramie)),
				new Stack(new FleCropGen(8, 4, Plants.soybean)),
				new Stack(new FleCropGen(8, 4, Plants.sweet_potato)),
				new Stack(new FleCropGen(8, 4, Plants.flax)),
				new Stack(new FleCropGen(8, 4, Plants.wheat)),
				new Stack(gen1, 14)};
		Stack[] wgssm = {
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1)), 
						Blocks.stone, 0.5F, 1.2F, 96), 32),//Cu mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6)), 
						Blocks.stone, 0.5F, 1.2F, 96), 24),//Cu & S mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Orpiment, 8), g(Materials.Realgar, 12), g(Materials.Arsenolite, 2), g(Materials.Nickeline, 1), g(Materials.Arsenopyrite, 2), g(Materials.Scorodite, 1), g(Materials.Erythrite, 2)), 
						Blocks.stone, 0.5F, 1.2F, 96), 18)//As				
		};
		Stack[] wgmsm = {
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1)), 
						Blocks.stone, 0.2F, 1.2F, 96), 18),//Cu mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6)), 
						Blocks.stone, 0.2F, 1.2F, 96), 32),//Cu & S mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Orpiment, 8), g(Materials.Realgar, 12), g(Materials.Arsenolite, 2), g(Materials.Nickeline, 1), g(Materials.Arsenopyrite, 2), g(Materials.Scorodite, 1), g(Materials.Erythrite, 2)), 
						Blocks.stone, 0.2F, 1.2F, 96), 18)//As				
		};
		Stack[] wgdsm = {
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1)), 
						Blocks.stone, 0.2F, 1.1F, 96), 10),//Cu mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6)), 
						Blocks.stone, 0.2F, 1.1F, 96), 40),//Cu & S mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Orpiment, 8), g(Materials.Realgar, 12), g(Materials.Arsenolite, 2), g(Materials.Nickeline, 1), g(Materials.Arsenopyrite, 2), g(Materials.Scorodite, 1), g(Materials.Erythrite, 2)), 
						Blocks.stone, 0.2F, 1.1F, 96), 18)//As				
		};
		Stack[] wgmmm = {
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1)), 
						Blocks.stone, 0.8F, 2.0F, 640), 24),//Cu mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6)), 
						Blocks.stone, 0.8F, 2.0F, 640), 32),//Cu & S mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Orpiment, 8), g(Materials.Realgar, 12), g(Materials.Arsenolite, 2), g(Materials.Nickeline, 1), g(Materials.Arsenopyrite, 2), g(Materials.Scorodite, 1), g(Materials.Erythrite, 2)), 
						Blocks.stone, 0.8F, 2.0F, 640), 18)//As				
		};
		Stack[] wgdmm = {
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1)), 
						Blocks.stone, 0.8F, 2.0F, 640), 20),//Cu mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6)), 
						Blocks.stone, 0.8F, 2.0F, 640), 32),//Cu & S mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Orpiment, 8), g(Materials.Realgar, 12), g(Materials.Arsenolite, 2), g(Materials.Nickeline, 1), g(Materials.Arsenopyrite, 2), g(Materials.Scorodite, 1), g(Materials.Erythrite, 2)), 
						Blocks.stone, 0.8F, 2.0F, 640), 18)//As				
		};
		Stack[] wgdlm = {
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.NativeCopper, 12), g(Materials.Malachite, 9), g(Materials.Azurite, 5), g(Materials.Cuprite, 4), g(Materials.Chalcocite, 5), g(Materials.Tenorite, 1)), 
						Blocks.stone, 1.0F, 2.5F, 5120), 32),//Cu mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Chalcopyrite, 14), g(Materials.Bornite, 8), g(Materials.Chalcocite, 7), g(Materials.Covellite, 2), g(Materials.Tetrahedrite, 4), g(Materials.Enargite, 6)), 
						Blocks.stone, 1.0F, 2.5F, 5120), 15),//Cu & S mine
				new Stack(new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Orpiment, 8), g(Materials.Realgar, 12), g(Materials.Arsenolite, 2), g(Materials.Nickeline, 1), g(Materials.Arsenopyrite, 2), g(Materials.Scorodite, 1), g(Materials.Erythrite, 2)), 
						Blocks.stone, 1.0F, 2.5F, 5120), 18)//As				
		};
		Stack[] wgsrg = {
				new Stack(new FleDivergeRockGen(Blocks.stone, Materials.Limestone, 1.2F, 1.0F, 1280, 0.2F,
						new FleDivergeOreGen(new ArrayStandardStackList(g(Materials.Gelenite, 5), g(Materials.Sphalerite, 7), g(Materials.Cassiterite, 10), g(Materials.Stannite, 7)), 
								BlockFleRock.a(Materials.Limestone), 8.0F, 2.0F, 1280)), 32),//lime		
		};
		Stack[] wgdrg = {
				new Stack(new FleDivergeRockGen(Blocks.stone, Materials.Limestone, 1.2F, 1.0F, 1280), 32),//lime		
		};
		plantGeneratorsTempCold = new ArrayStandardStackList(wgs1);
		plantGeneratorsTempWarm = new ArrayStandardStackList(wgs2);
		plantGeneratorsTempHot = new ArrayStandardStackList(wgs3);
		superficialRockGenerators = new ArrayStandardStackList(wgsrg);
		deepRockGenerators = new ArrayStandardStackList(wgdrg);
		deepLargeMineableGenerators = new ArrayStandardStackList(wgdlm);
		deepMidMineableGenerators = new ArrayStandardStackList(wgdmm);
		deepSmallMineableGenerators = new ArrayStandardStackList(wgdsm);
		middleMidMineableGenerators = new ArrayStandardStackList(wgmmm);
		middleSmallMineableGenerators = new ArrayStandardStackList(wgmsm);
		superficialSmallMineableGenerators = new ArrayStandardStackList(wgssm);
		flag = true;
	}
	
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
	
	private int cX;
	private int cZ;
	
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		init();
		TreeInfo.genFlag = false;
		FWM.generateFlag = false;
		cX = chunkX;
		cZ = chunkZ;
		switch(world.provider.dimensionId)
		{
		case 1 : this.generateEnd(random, chunkX * 16, chunkZ * 16, world);
		case 0 : this.generateSurface(random, chunkX * 16, chunkZ * 16, world);
		case -1 : this.generateNether(random, chunkX * 16, chunkZ * 16, world);
		}
		FWM.generateFlag = true;
		TreeInfo.genFlag = true;
	}
	
	public void generateEnd(Random random, int x, int z, World world)
	{
		
	}

	public void generateSurface(Random random, int x, int z, World world)
	{
		if(x % 7 == 0 && z % 7 == 0)
			switch(random.nextInt(3))
			{
			case 0 : genOre(random, world, x, z, 250, 100, superficialRockGenerators);
			break;
			case 1 : genOre(random, world, x, z, 130, 0, deepRockGenerators);
			break;
			}
		if(cX % 83 == 0 && cZ % 83 == 0)
		{
			if(random.nextBoolean())
				genOre(random, world, x, z, 60, 10, deepLargeMineableGenerators);
		}
		else if(cX % 23 == 0 && cZ % 23 == 0)
		{
			switch(random.nextInt(5))
			{
			case 0 : genOre(random, world, x, z, 60, 10, deepMidMineableGenerators);
			break;
			case 1 : genOre(random, world, x, z, 110, 60, middleMidMineableGenerators);
			break;
			default:;
			break;
			}
		}
		else if(cX % 5 == 0 && cZ % 5 == 0)
		{
			switch(random.nextInt(8))
			{
			case 0 : genOre(random, world, x, z, 250, 0, superficialSmallMineableGenerators);
			break;
			case 1 : genOre(random, world, x, z, 250, 0, middleSmallMineableGenerators);
			break;
			case 2 : genOre(random, world, x, z, 250, 0, deepSmallMineableGenerators);
			break;
			default:;
			break;
			}
		}
		//Surface generate.
		BiomeGenBase biome = world.getBiomeGenForCoords(x * 16 + 8, z * 16 + 8);
		Map<TreeInfo, Integer> info = new HashMap();
		for(TreeInfo tInfo : BlockFleLog.trees)
		{
			int weight = tInfo.getGenerateWeight(world, x, z);
			if(weight > 0) info.put(tInfo, weight);
		}
		if(info.size() > 0 && random.nextInt(4) == 0)
		{
			ArrayStandardStackList<TreeInfo> wh = new ArrayStandardStackList<TreeInfo>(info);
			generateTree(wh.randomGet(), world, random, x, z);
		}
		if(biome.getFloatTemperature(x, 0, z) >= 0.04F)
		for(int i = 0; i < 4; ++i)
			if(random.nextInt(6) == 0)
			{
				switch(biome.getTempCategory())
				{
				case COLD:
					plantGeneratorsTempCold.randomGet().generate(world, random, x, 255, z);
					break;
				case MEDIUM:
					plantGeneratorsTempWarm.randomGet().generate(world, random, x, 255, z);
					break;
				case WARM:
					plantGeneratorsTempHot.randomGet().generate(world, random, x, 255, z);
					break;
				default :;
				}
			}
	}

	public void generateNether(Random random, int x, int z, World world)
	{
		
	}
	
	public boolean generatePlant(PlantBase card, World world,
			Random random, int x, int z, int size, int count)
	{
		int X = x + random.nextInt(15);
		int Z = z + random.nextInt(15);
		return new FlePlantGen(size, count, card).generate(world, random, X, 200, Z);
	}
	
	public boolean generateCrop(CropCard card, World world, Random random, int x, int z, int size, int count)
	{
		int X = x + random.nextInt(15);
		int Z = z + random.nextInt(15);
		return new FleCropGen(size, count, card).generate(world, random, X, world.getTopSolidOrLiquidBlock(X, Z), Z);
	}
	
	public boolean generateTree(TreeInfo tInfo, World world, Random random, int x, int z)
	{
		for (int count = world.getBiomeGenForCoords(x, z).theBiomeDecorator.treesPerChunk * 3; count > 0; count--)
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
	
	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, double chance, int size, Block base, Stack<MaterialOre>...aOres)
	{
		return genOre(rand, world, x, z, maxY, minY, chance, size, base, new ArrayStandardStackList<MaterialOre>(aOres));
	}
	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, double chance, int size, Block base, ArrayStandardStackList<MaterialOre> aHelper)
	{
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
	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, IStackList<Stack<WorldGenerator>, WorldGenerator> list)
	{
		int X = x + rand.nextInt(16);
		int Y = minY + rand.nextInt(maxY - minY);
		int Z = z + rand.nextInt(16);
		if(Y >= world.getTopSolidOrLiquidBlock(X, Z))
		{
			return false;
		}
		list.randomGet().generate(world, rand, X, Y, Z);
		return true;
	}
	
	private static Stack<MaterialOre> g(MaterialOre ore, int size)
	{
		return new Stack(ore, size);
	}
}