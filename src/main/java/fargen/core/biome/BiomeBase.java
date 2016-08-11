package fargen.core.biome;

import java.util.Random;

import farcore.lib.collection.Register;
import farcore.lib.util.IRegisteredNameable;
import farcore.lib.world.IBiomeExtended;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import farcore.util.U.L;
import fargen.core.FarGen;
import fargen.core.biome.decorator.BiomeDecorator;
import fargen.core.biome.layer.BiomeLayerGenerator;
import fargen.core.biome.layer.surface.BLGSStandard;
import fargen.core.util.ClimaticZone;
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
	
	private static final Register<BiomeBase> register = new Register(256);

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
		return register.get(name);
	}
	public static BiomeBase getBiomeFromID(int id)
	{
		BiomeBase ret = register.get(id, DEBUG);
		return ret;
	}
	
	public final ClimaticZone zone;
	public int biomeID;
	protected boolean canRain;
	protected boolean canSnow;
	protected BiomeDecorator decorator;
	protected BiomeLayerGenerator layerGenerator;

	public BiomeBase(int id, BiomePropertiesExtended properties)
	{
		this(id, true, properties);
	}
	public BiomeBase(int id, boolean register, BiomePropertiesExtended properties)
	{
		super(properties);
		biomeID = id;
		zone = properties.zone;
		canRain = properties.canRain;
		canSnow = properties.canSnow;
		decorator = properties.decorator;
		layerGenerator = properties.layerGenerator;
		setRegistryName(FarGen.ID, getBiomeName());
		if(register)
		{
			this.register.register(id, getBiomeName(), this);
		}
		GameRegistry.register(this);
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
		World world = Minecraft.getMinecraft().theWorld;
		IWorldPropProvider prop = WorldPropHandler.getWorldProperty(world);
		float temperature = L.range(0.0F, 1.0F, prop.getTemperature(world, pos) * .3F);
		float humidity = L.range(0.0F, 1.0F, prop.getHumidity(world, pos) * .3F);
		return ColorizerGrass.getGrassColor(temperature, humidity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos)
	{
		World world = Minecraft.getMinecraft().theWorld;
		IWorldPropProvider prop = WorldPropHandler.getWorldProperty(world);
		float temperature = L.range(0.0F, 1.0F, prop.getTemperature(world, pos) * .3F);
		float humidity = L.range(0.0F, 1.0F, prop.getHumidity(world, pos) * .3F);
		return ColorizerFoliage.getFoliageColor(temperature, humidity);
	}

	@Override
	public boolean getEnableSnow()
	{
		return super.getEnableSnow();
	}
	
	@Override
	public boolean canRainingAt(World world, BlockPos pos)
	{
		if(!canRain) return false;
		IWorldPropProvider prop = WorldPropHandler.getWorldProperty(world);
		return prop.getHumidity(world, pos) >= minRainHumidity;
	}

	@Override
	public TempCategory getTempCategory()
	{
		return zone.category;
	}
	
	@Override
	public boolean isHighHumidity()
	{
		return zone.rainAverage >= 1.6F;
	}

	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal, int submeta)
	{
		layerGenerator.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal, this, submeta);
	}

	@Override
	public net.minecraft.world.biome.BiomeDecorator createBiomeDecorator()
	{
		return EMPTY;
	}
	
	@Override
	public void decorate(World worldIn, Random rand, BlockPos pos)
	{
		decorator.decorate(worldIn, rand, pos);
	}
	
	public static class BiomePropertiesExtended extends BiomeProperties
	{
		public static BiomePropertiesExtended newProperties(String name)
		{
			return new BiomePropertiesExtended(name);
		}

		private float temperature = 2.0F;
		private ClimaticZone zone = ClimaticZone.temperate_plain;
		private BiomeLayerGenerator layerGenerator = new BLGSStandard();
		private BiomeDecorator decorator;
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
			if(L.max(zone.rain) < minRainHumidity)
			{
				setRainDisabled();
				canRain = false;
			}
			if(L.min(zone.temperature) < minSnowTemperature)
			{
				setSnowEnabled();
				canSnow = true;
			}
			return this;
		}
		
		@Override
		public BiomePropertiesExtended setTemperature(float temperatureIn)
		{
			temperature = temperatureIn;
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
	}
}