package fle.core.world.dim;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class FLEBiomeDecoratorHell extends FLEBiomeDecoratorBase
{    
    public FLEBiomeDecoratorHell()
    {
    	bigMushroomsPerChunk = 0;
    	mushroomsPerChunk = 0;
    	treesPerChunk = 0;
	}

    @Override
	protected void genDecorations(BiomeGenBase biome)
    {
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, chunk_X, chunk_Z));
        //generateOres();
        int i;
        int j;
        int k;

        boolean doGen;

        //Generate lake before tree and grass to prevent that plants generate on the top of lakes.
        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, LAKE);
        if (doGen && generateLakes)
        {
            for (j = 0; j < 50; ++j)
            {
            	generate((byte) 4, new WorldGenLiquids(Blocks.flowing_lava));
            }
        }

        i = treesPerChunk;

        if (randomGenerator.nextInt(10) == 0)
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
            WorldGenAbstractTree worldgenabstracttree = biome.func_150567_a(randomGenerator);
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

        /**
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
         */

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, GRASS);
        for (j = 0; doGen && j < grassPerChunk; ++j)
        {
            generate((byte) 2, biome.getRandomWorldGenForGrass(randomGenerator));
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, chunk_X, chunk_Z, SHROOM);
        for (j = 0; doGen && j < mushroomsPerChunk; ++j)
        {
            if (randomGenerator.nextInt(4) == 0)
            {
            	generate((byte) 2, mushroomBrownGen);
            }

            if (randomGenerator.nextInt(8) == 0)
            {
            	generate((byte) 2, mushroomRedGen);
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

        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, chunk_X, chunk_Z));
    }
    
    @Override
    protected int height()
    {
    	return 120;
    }
}