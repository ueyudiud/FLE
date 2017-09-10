package fargen.core.biome;

import java.util.Random;

import farcore.data.EnumTempCategory;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fargen.core.FarGen;
import fargen.core.biome.decorator.BiomeDecorator;
import fargen.core.biome.decorator.SimpleBiomeDecorator;
import fargen.core.biome.layer.BiomeLayerGenerator;
import fargen.core.biome.layer.surface.BLGSStandard;
import fargen.core.util.ClimaticZone;
import nebula.base.Register;
import nebula.common.util.IRegisteredNameable;
import nebula.common.util.L;
import nebula.common.world.IBiomeExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeBase extends Biome implements IRegisteredNameable, IBiomeExtended
{
	private static final net.minecraft.world.biome.BiomeDecorator EMPTY = new net.minecraft.world.biome.BiomeDecorator();
	public static final float minRainHumidity = 0.3F;
	public static final float minSnowTemperature = 0.5F;
	
	public static final BiomeBase DEBUG = new BiomeBase(-1, false, BiomePropertiesExtended.newProperties("debug"));
	
	private static final Register<BiomeBase> REGISTER = new Register(256);
	
	static
	{
		EMPTY.treesPerChunk = -999;
		EMPTY.flowersPerChunk = -999;
		EMPTY.grassPerChunk = -999;
		EMPTY.sandPerChunk = -999;
		EMPTY.clayPerChunk = -999;
		EMPTY.generateLakes = false;
	}
	
	public static BiomeBase getBiomeFromName(String name)
	{
		return REGISTER.get(name);
	}
	public static BiomeBase getBiomeFromID(int id)
	{
		BiomeBase ret = REGISTER.get(id, DEBUG);
		return ret;
	}
	
	public final ClimaticZone zone;
	public int biomeID;
	protected boolean canRain;
	protected boolean canSnow;
	public BiomeDecorator decorator;
	protected BiomeLayerGenerator layerGenerator;
	public BiomeBase baseBiome;
	public final int treePerChunkBase;
	public final int treePerChunkDiv;
	public final int cropPerChunkBase;
	public final int cropPerChunkRand;
	
	public BiomeBase(int id, BiomePropertiesExtended properties)
	{
		this(id, true, properties);
	}
	public BiomeBase(int id, boolean register, BiomePropertiesExtended properties)
	{
		super(properties);
		this.biomeID = id;
		this.zone = properties.zone;
		this.canRain = properties.canRain;
		this.canSnow = properties.canSnow;
		this.decorator = properties.decorator;
		this.layerGenerator = properties.layerGenerator;
		this.treePerChunkBase = this.theBiomeDecorator.treesPerChunk = properties.treePerChunk;
		this.treePerChunkDiv = properties.treeDivition;
		this.cropPerChunkBase = properties.cropPerChunkBase;
		this.cropPerChunkRand = properties.cropPerChunkRand;
		if(register)
		{
			setRegistryName(FarGen.ID, getBiomeName());
			REGISTER.register(id, getBiomeName(), this);
			GameRegistry.register(this);
		}
		if(isMutation())
		{
			this.baseBiome = (BiomeBase) REGISTRY.getObjectById(MUTATION_TO_BASE_ID_MAP.get(this));
		}
		else
		{
			this.baseBiome = this;
		}
	}
	
	@Override
	public final String getRegisteredName()
	{
		return getBiomeName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos)
	{
		World world = Minecraft.getMinecraft().world;
		IWorldPropProvider prop = WorldPropHandler.getWorldProperty(world);
		float temperature = L.range(0.0F, 1.0F, (prop.getTemperature(world, pos) - 263.15F) / (317.15F - 263.15F));
		float humidity = L.range(0.0F, 1.0F, prop.getHumidity(world, pos) * .3F);
		return ColorizerGrass.getGrassColor(temperature, humidity);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos)
	{
		World world = Minecraft.getMinecraft().world;
		IWorldPropProvider prop = WorldPropHandler.getWorldProperty(world);
		float temperature = L.range(0.0F, 1.0F, (prop.getTemperature(world, pos) - 263.15F) / (317.15F - 263.15F));
		float humidity = L.range(0.0F, 1.0F, prop.getHumidity(world, pos) * .3F);
		return ColorizerFoliage.getFoliageColor(temperature, humidity);
	}
	
	@Override
	public boolean getEnableSnow()
	{
		return true;
	}
	
	@Override
	public boolean canRainingAt(World world, BlockPos pos)
	{
		if(!this.canRain) return false;
		IWorldPropProvider prop = WorldPropHandler.getWorldProperty(world);
		return prop.getHumidity(world, pos) >= minRainHumidity;
	}
	
	public boolean isOcean()
	{
		return this.zone.category1 == EnumTempCategory.OCEAN;
	}
	
	@Override
	public TempCategory getTempCategory()
	{
		return this.zone.category;
	}
	
	@Override
	public boolean isHighHumidity()
	{
		return this.zone.rainAverage >= 1.6F;
	}
	
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal, int submeta)
	{
		this.layerGenerator.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal, this, submeta);
	}
	
	@Override
	public net.minecraft.world.biome.BiomeDecorator createBiomeDecorator()
	{
		return EMPTY;
	}
	
	@Override
	public void decorate(World worldIn, Random rand, BlockPos pos)
	{
		if(this.decorator != null)
		{
			this.decorator.decorate(worldIn, rand, pos, worldIn.getWorldType().getChunkGenerator(worldIn, null));
		}
	}
	
	public static class BiomePropertiesExtended extends BiomeProperties
	{
		public static BiomePropertiesExtended newProperties(String name)
		{
			return new BiomePropertiesExtended(name);
		}
		
		private float temperature = 2.0F;
		private int treePerChunk = -999;
		private int treeDivition = 1;
		private int cropPerChunkBase = 0;
		private int cropPerChunkRand = 0;
		private ClimaticZone zone = ClimaticZone.temperate_plain;
		private BiomeLayerGenerator layerGenerator = new BLGSStandard();
		private BiomeDecorator decorator = new SimpleBiomeDecorator();
		private boolean canRain = true;
		private boolean canSnow = false;
		
		protected BiomePropertiesExtended(String nameIn)
		{
			super(nameIn);
		}
		
		public BiomePropertiesExtended setDecorator(BiomeDecorator decorator)
		{
			this.decorator = decorator;
			return this;
		}
		
		public BiomePropertiesExtended setLayerGenerator(BiomeLayerGenerator layerGenerator)
		{
			this.layerGenerator = layerGenerator;
			return this;
		}
		
		public BiomePropertiesExtended setClimaticZone(ClimaticZone zone)
		{
			this.zone = zone;
			if(nebula.common.util.L.max(zone.rain) < minRainHumidity)
			{
				setRainDisabled();
				this.canRain = false;
			}
			if(nebula.common.util.L.min(zone.temperature) < minSnowTemperature)
			{
				setSnowEnabled();
				this.canSnow = true;
			}
			return this;
		}
		
		@Override
		public BiomePropertiesExtended setTemperature(float temperatureIn)
		{
			this.temperature = temperatureIn;
			return this;
		}
		
		@Override
		public BiomePropertiesExtended setRainfall(float rainfallIn)
		{
			return (BiomePropertiesExtended) super.setRainfall(rainfallIn);
		}
		
		@Override
		public BiomePropertiesExtended setBaseBiome(String nameIn)
		{
			return (BiomePropertiesExtended) super.setBaseBiome(nameIn);
		}
		
		public BiomePropertiesExtended setHeight(float min, float max)
		{
			super.setBaseHeight((min + max) / 2F);
			super.setHeightVariation((max - min) / 2F);
			return this;
		}
		
		public BiomePropertiesExtended setTreePerChunk(int a, int b)
		{
			this.treePerChunk = a;
			this.treeDivition = b;
			return this;
		}
		
		public BiomePropertiesExtended setCropPerChunk(int a, int b)
		{
			this.cropPerChunkBase = a;
			this.cropPerChunkRand = b;
			return this;
		}
	}
}