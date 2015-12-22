package fle.core.world.dim;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CACTUS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CLAY;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CUSTOM;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.DEAD_BUSH;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LILYPAD;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.PUMPKIN;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.REED;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SAND_PASS2;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;

import java.util.Random;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import fle.core.init.IB;
import fle.core.world.biome.FLEBiome;
import fle.resource.world.FleDirtDefaultGen;
import fle.resource.world.FleDirtGen;

public class FLEBiomeDecoratorBase extends BiomeDecorator
{
	public FleDirtGen peatGen = new FleDirtDefaultGen(IB.peat, 4);
	public int peatPerChunk = 0;
	public int waterLakePerChunk = 50;
	public int lavaLakePerChunk = 20;
	
	public void decorateChunk(World aWorld, Random aRand, BiomeGenBase aBiome, int x, int z)
    {
        if (currentWorld != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            currentWorld = aWorld;
            randomGenerator = aRand;
            chunk_X = x;
            chunk_Z = z;
            genDecorations(aBiome);
            currentWorld = null;
            randomGenerator = null;
        }
    }

    protected void genDecorations(BiomeGenBase biome)
    {
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, chunk_X, chunk_Z));
        generateOres();
        int i;
        int j;
        int k;

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SAND);
        for (i = 0; doGen && i < sandPerChunk2; ++i)
        {
        	generate(sandGen);
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, CLAY);
        for (i = 0; doGen && i < clayPerChunk; ++i)
        {
        	generate(clayGen);
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, CUSTOM);
        for (i = 0; doGen && i < peatPerChunk; ++i)
        {
        	generate(peatGen);
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SAND_PASS2);
        for (i = 0; doGen && i < sandPerChunk; ++i)
        {
        	generate(gravelAsSandGen);
        }

        //Generate lake before tree and grass to prevent that plants generate on the top of lakes.
        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, LAKE);
        if (doGen && generateLakes)
        {
            for (j = 0; j < waterLakePerChunk; ++j)
            {
            	generate((byte) 3, new WorldGenLiquids(Blocks.flowing_water));
            }

            for (j = 0; j < lavaLakePerChunk; ++j)
            {
            	generate((byte) 4, new WorldGenLiquids(Blocks.flowing_lava));
            }
        }

        i = treesPerChunk;

        if (randomGenerator.nextInt(16) == 0)
        {
            ++i;
        }

        int l;
        int i1;

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, TREE);
        for (j = 0; doGen && j < i; ++j)
        {
            k = chunk_X + randomGenerator.nextInt(16) + 8;
            l = chunk_Z + randomGenerator.nextInt(16) + 8;
            i1 = currentWorld.getHeightValue(k, l);
            WorldGenAbstractTree worldgenabstracttree = biome instanceof FLEBiome ? ((FLEBiome) biome).getTreeGenerator(chunk_X, chunk_Z, randomGenerator) : biome.func_150567_a(randomGenerator);
            worldgenabstracttree.setScale(1.0D, 1.0D, 1.0D);

            if (worldgenabstracttree.generate(currentWorld, randomGenerator, k, i1, l))
            {
                worldgenabstracttree.func_150524_b(currentWorld, randomGenerator, k, i1, l);
            }
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, BIG_SHROOM);
        for (j = 0; doGen && j < bigMushroomsPerChunk; ++j)
        {
        	generate((byte) 1, bigMushroomGen);
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, FLOWERS);
        for (j = 0; doGen && j < flowersPerChunk; ++j)
        {
            k = chunk_X + randomGenerator.nextInt(16) + 8;
            l = chunk_Z + randomGenerator.nextInt(16) + 8;
            i1 = nextInt(currentWorld.getHeightValue(k, l) + 32);
            String s = biome.func_150572_a(randomGenerator, k, i1, l);
            BlockFlower blockflower = BlockFlower.func_149857_e(s);

            if (blockflower.getMaterial() != Material.air)
            {
                yellowFlowerGen.func_150550_a(blockflower, BlockFlower.func_149856_f(s));
                yellowFlowerGen.generate(currentWorld, randomGenerator, k, i1, l);
            }
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, GRASS);
        for (j = 0; doGen && j < grassPerChunk; ++j)
        {
            generate((byte) 2, biome.getRandomWorldGenForGrass(randomGenerator));
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, DEAD_BUSH);
        for (j = 0; doGen && j < deadBushPerChunk; ++j)
        {
        	generate((byte) 2, new WorldGenDeadBush(Blocks.deadbush));
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, LILYPAD);
        for (j = 0; doGen && j < waterlilyPerChunk; ++j)
        {
            k = chunk_X + randomGenerator.nextInt(16) + 8;
            l = chunk_Z + randomGenerator.nextInt(16) + 8;

            for (i1 = nextInt(currentWorld.getHeightValue(k, l) * 2); i1 > 0 && currentWorld.isAirBlock(k, i1 - 1, l); --i1)
            {
                ;
            }

            waterlilyGen.generate(currentWorld, randomGenerator, k, i1, l);
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SHROOM);
        for (j = 0; doGen && j < mushroomsPerChunk; ++j)
        {
            if (randomGenerator.nextInt(4) == 0)
            {
            	generate((byte) 1, mushroomBrownGen);
            }

            if (randomGenerator.nextInt(8) == 0)
            {
            	generate((byte) 1, mushroomRedGen);
            }
        }

        if (doGen && randomGenerator.nextInt(4) == 0)
        {
        	generate((byte) 2, mushroomBrownGen);
        }

        if (doGen && randomGenerator.nextInt(8) == 0)
        {
        	generate((byte) 2, mushroomRedGen);
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, REED);
        for (j = 0; doGen && j < reedsPerChunk; ++j)
        {
        	generate((byte) 2, reedGen);
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, PUMPKIN);
        if (doGen && randomGenerator.nextInt(32) == 0)
        {
        	generate((byte) 2, new WorldGenPumpkin());
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, CACTUS);
        for (j = 0; doGen && j < cactiPerChunk; ++j)
        {
        	generate((byte) 2, cactusGen);
        }

        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, chunk_X, chunk_Z));
    }
    
    protected boolean generate(int minY, int maxY, WorldGenerator aWg)
    {
    	int x = chunk_X + randomGenerator.nextInt(16) + 8;
    	int y = minY + nextInt(maxY - minY);
    	int z = chunk_Z + randomGenerator.nextInt(16) + 8;
    	return aWg.generate(currentWorld, randomGenerator, x, y, z);
    }
    
    protected boolean generate(WorldGenerator aWg)
    {
    	return generate((byte) 0, aWg);
    }
    
    protected final boolean generate(byte flag, WorldGenerator aWg)
    {
    	int x = chunk_X + randomGenerator.nextInt(16) + 8;
    	int z = chunk_Z + randomGenerator.nextInt(16) + 8;
    	switch(flag)
    	{
    	case 0 : return aWg.generate(currentWorld, randomGenerator, x, currentWorld.getTopSolidOrLiquidBlock(x, z), z);
    	case 1 : return aWg.generate(currentWorld, randomGenerator, x, currentWorld.getHeightValue(x, z), z);
    	case 2 : return aWg.generate(currentWorld, randomGenerator, x, nextInt(currentWorld.getHeightValue(x, z) * 3 / 2), z);
    	case 3 : return aWg.generate(currentWorld, randomGenerator, x, randomGenerator.nextInt(randomGenerator.nextInt(height()) + 8), z);
    	case 4 : return aWg.generate(currentWorld, randomGenerator, x, randomGenerator.nextInt(randomGenerator.nextInt(randomGenerator.nextInt(height()) + 8) + 8), z);
    	}
    	return false;
    }
    
    protected int height()
    {
    	return 240;
    }
	
	@Override
	protected void generateOres()
	{
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Pre(currentWorld, randomGenerator, chunk_X, chunk_Z));
		MinecraftForge.ORE_GEN_BUS.post(new OreGenEvent.Post(currentWorld, randomGenerator, chunk_X, chunk_Z));
	}

    protected int nextInt(int i)
    {
        if (i <= 1)
            return 0;
        return randomGenerator.nextInt(i);
	}
}