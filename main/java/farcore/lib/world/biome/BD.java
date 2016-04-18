package farcore.lib.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenClay;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenWaterlily;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BD
{
    /** The world the BiomeDecorator is currently decorating */
    public World currentWorld;
    /** The Biome Decorator's random number generator. */
    public Random randomGenerator;
    /** The X-coordinate of the chunk currently being decorated */
    public int chunkX;
    /** The Z-coordinate of the chunk currently being decorated */
    public int chunkZ;
    /** The clay generator. */
    public WorldGenerator clayGen;
    /** The sand generator. */
    public WorldGenerator sandGen;
    /** The gravel as sand generator. */
    public WorldGenerator gravelAsSandGen;
    /** The dirt generator. */
    public WorldGenerator dirtGen;
    /** The gravel generator. */
    public WorldGenerator gravelGen;
    public WorldGenFlowers yellowFlowerGen;
    /** Field that holds mushroomBrown WorldGenFlowers */
    public WorldGenerator mushroomBrownGen;
    /** Field that holds mushroomRed WorldGenFlowers */
    public WorldGenerator mushroomRedGen;
    /** Field that holds big mushroom generator */
    public WorldGenerator bigMushroomGen;
    /** Field that holds WorldGenReed */
    public WorldGenerator reedGen;
    /** Field that holds WorldGenCactus */
    public WorldGenerator cactusGen;
    /** The water lily generation! */
    public WorldGenerator waterlilyGen;
    /** The dead bush generation */
    public WorldGenerator deadBushGen;
    /** Amount of waterlilys per chunk. */
    public int waterlilyPerChunk;
    /** The number of trees to attempt to generate per chunk. Up to 10 in forests, none in deserts. */
    public int treesPerChunk;
    /**
     * The number of yellow flower patches to generate per chunk. The game generates much less than this number, since
     * it attempts to generate them at a random altitude.
     */
    public int flowersPerChunk;
    /** The amount of tall grass to generate per chunk. */
    public int grassPerChunk;
    /** The number of dead bushes to generate per chunk. Used in deserts and swamps. */
    public int deadBushPerChunk;
    public int redMushroomsPerChunk;
    public int brownMushroomsPerChunk;
    /** The number of reeds to generate per chunk. Reeds won't generate if the randomly selected placement is unsuitable. */
    public int reedsPerChunk;
    /** The number of cactus plants to generate per chunk. Cacti only work on sand. */
    public int cactiPerChunk;
    /** The number of sand patches to generate per chunk. Sand patches only generate when part of it is underwater. */
    public int sandPerChunk;
    /**
     * The number of sand patches to generate per chunk. Sand patches only generate when part of it is underwater. There
     * appear to be two separate fields for this.
     */
    public int sandPerChunk2;
    /** The number of clay patches to generate per chunk. Only generates when part of it is underwater. */
    public int clayPerChunk;
    /** Amount of big mushrooms per chunk */
    public int bigMushroomsPerChunk;
    /** True if decorator should generate surface lava & water */
    public boolean generateLakes;

    public BD()
    {
    	this.clayGen = new WorldGenClay(4);
        this.sandGen = new WorldGenSand(Blocks.sand, 7);
        this.gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
        this.dirtGen = new WorldGenMinable(Blocks.dirt, 32);
        this.gravelGen = new WorldGenMinable(Blocks.gravel, 32);
        
        this.yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower);
        this.mushroomBrownGen = new WorldGenFlowers(Blocks.brown_mushroom);
        this.mushroomRedGen = new WorldGenFlowers(Blocks.red_mushroom);
        this.bigMushroomGen = new WorldGenBigMushroom();
        this.reedGen = new WorldGenReed();
        this.cactusGen = new WorldGenCactus();
        this.waterlilyGen = new WorldGenWaterlily();
        this.deadBushGen = new WorldGenDeadBush(Blocks.deadbush);
        this.flowersPerChunk = 2;
        this.grassPerChunk = 1;
        this.sandPerChunk = 1;
        this.sandPerChunk2 = 3;
        this.clayPerChunk = 1;
        this.generateLakes = true;
    }

    public void decorateChunk(World world, Random random, BiomeBase biome, int x, int z)
    {
        if (this.currentWorld != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            this.currentWorld = world;
            this.randomGenerator = random;
            this.chunkX = x;
            this.chunkZ = z;
            this.genDecorations(biome);
            this.currentWorld = null;
            this.randomGenerator = null;
        }
    }

	public void genDecorations(BiomeBase biome)
	{
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(currentWorld, randomGenerator, chunkX, chunkZ));
        
        int i, j, k;
        boolean gen;
        
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.SAND))
        {
        	generateAtTop(sandGen, sandPerChunk);
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.CLAY))
        {
        	generateAtTop(clayGen, clayPerChunk);
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.SAND_PASS2))
        {
        	generateAtTop(gravelAsSandGen, sandPerChunk2);
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.BIG_SHROOM))
        {
        	generateFromMaxHeight(bigMushroomGen, bigMushroomsPerChunk);
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.DEAD_BUSH))
        {
        	generateWithDoubleHeightRand(deadBushGen, deadBushPerChunk);
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.LILYPAD))
        {
        	generateWithDoubleHeightRandFloor(waterlilyGen, waterlilyPerChunk);
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.TREE))
        {
    		for(int loop = 0; loop < treesPerChunk; ++loop)
    		{
    			i = chunkX + randomGenerator.nextInt(16) + 8;
    			k = chunkZ + randomGenerator.nextInt(16) + 8;
    			ITreeGenerator generator = biome.getTreeGenerator(currentWorld, randomGenerator, i, k);
    			if(generator != null)
    			{
    				generator.generate(currentWorld, randomGenerator, i, currentWorld.getTopSolidOrLiquidBlock(i, k), k, true);
    			}
    		}
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.GRASS))
        {
    		for(int loop = 0; loop < grassPerChunk; ++loop)
    		{
    			i = chunkX + randomGenerator.nextInt(16) + 8;
    			k = chunkZ + randomGenerator.nextInt(16) + 8;
    			WorldGenerator generator = biome.getRandomWorldGenForGrass(randomGenerator);
    			if(generator != null)
    			{
    				generator.generate(currentWorld, randomGenerator, i, currentWorld.getTopSolidOrLiquidBlock(i, k), k);
    			}
    		}
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.SHROOM))
        {
        	generateAtTop(mushroomBrownGen, brownMushroomsPerChunk);
        	generateAtTop(mushroomRedGen, redMushroomsPerChunk);
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.REED))
        {
        	generateWithDoubleHeightRand(reedGen, reedsPerChunk);
        }
        if(TerrainGen.decorate(currentWorld, randomGenerator, chunkX, chunkZ, EventType.CACTUS))
        {
        	generateWithDoubleHeightRand(cactusGen, cactiPerChunk);
        }
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(currentWorld, randomGenerator, chunkX, chunkZ));
	}
	
	protected void generateAtTop(WorldGenerator generator, int count)
	{
		int i1, j1;
		for(int i = 0; i < count; ++i)
		{
			i1 = chunkX + randomGenerator.nextInt(16) + 8;
			j1 = chunkZ + randomGenerator.nextInt(16) + 8;
			generator.generate(currentWorld, randomGenerator, i1, currentWorld.getTopSolidOrLiquidBlock(i1, j1), j1);
		}
	}
	
	protected void generateFromMaxHeight(WorldGenerator generator, int count)
	{
		int i1, j1;
		for(int i = 0; i < count; ++i)
		{
			i1 = chunkX + randomGenerator.nextInt(16) + 8;
			j1 = chunkZ + randomGenerator.nextInt(16) + 8;
			generator.generate(currentWorld, randomGenerator, i1, currentWorld.getHeightValue(i1, j1), j1);
		}
	}
	
	protected void generateWithDoubleHeightRand(WorldGenerator generator, int count)
	{
		int i1, j1, k1;
		for(int i = 0; i < count; ++i)
		{
			i1 = chunkX + randomGenerator.nextInt(16) + 8;
			k1 = chunkZ + randomGenerator.nextInt(16) + 8;
			j1 = nextInt(currentWorld.getHeightValue(i1, k1) * 2);
			generator.generate(currentWorld, randomGenerator, i1, j1, k1);
		}
	}
	
	protected void generateWithDoubleHeightRandFloor(WorldGenerator generator, int count)
	{
		int i1, j1, k1;
		for(int i = 0; i < count; ++i)
		{
			i1 = chunkX + randomGenerator.nextInt(16) + 8;
			k1 = chunkZ + randomGenerator.nextInt(16) + 8;
            for (j1 = nextInt(currentWorld.getHeightValue(i1, k1) * 2); 
            		j1 > 0 && this.currentWorld.isAirBlock(i1, j1 - 1, k1); 
            		--j1);
			generator.generate(currentWorld, randomGenerator, i1, j1, k1);
		}
	}

    private int nextInt(int i)
    {
        if (i <= 1)
        {
        	return 0;
        }
        return this.randomGenerator.nextInt(i);
	}
}