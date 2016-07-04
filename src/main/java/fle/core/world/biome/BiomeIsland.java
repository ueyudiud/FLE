package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeIsland extends BiomeBase
{
    private static final WorldGenTrees shrubI = new WorldGenShrub(0, 0);
    
	private boolean genRainforestTree;
	
	public BiomeIsland(int id, Block top, boolean genRainforestTree)
	{
		super(id);
		this.topBlock = top;
		this.genRainforestTree = genRainforestTree;
//        this.biomeDecorator.treesPerChunk = 2;
	}

    public WorldGenAbstractTree func_150567_a(Random random)
    {
        return !genRainforestTree ? worldGeneratorTrees : (WorldGenAbstractTree)
        		(random.nextInt(10) == 0 ? this.worldGeneratorBigTree : (random.nextInt(2) == 0 ? shrubI : random.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, 3, 3) : new WorldGenTrees(false, 4 + random.nextInt(7), 3, 3, true)));
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random random)
    {
        return new WorldGenTallGrass(Blocks.tallgrass, random.nextInt(4) == 0 ? 2 : 1);
    }

    public void decorate(World world, Random random, int x, int z)
    {
        super.decorate(world, random, x, z);
        if(genRainforestTree)
        {
            int k = x + random.nextInt(16) + 8;
            int l = z + random.nextInt(16) + 8;
            int height = world.getHeightValue(k, l) * 2; //This was the original input for the nextInt below.  But it could == 0, which crashes nextInt
            if (height < 1) height = 1;
            int i1 = random.nextInt(height);
            (new WorldGenMelon()).generate(world, random, k, i1, l);
            WorldGenVines worldgenvines = new WorldGenVines();

            for (l = 0; l < 50; ++l)
            {
                i1 = x + random.nextInt(16) + 8;
                short short1 = 128;
                int j1 = z + random.nextInt(16) + 8;
                worldgenvines.generate(world, random, i1, short1, j1);
            }
        }
    }
}