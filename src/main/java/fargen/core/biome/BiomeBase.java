package fargen.core.biome;

import farcore.lib.collection.Register;
import farcore.lib.util.IRegisteredNameable;
import farcore.util.U.L;
import fargen.core.FarGen;
import fargen.core.util.IWorldPropProvider;
import fargen.core.world.WorldPropHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeBase extends Biome implements IRegisteredNameable
{
	public static final BiomeBase DEBUG = new BiomeBase(-1, false, new BiomeProperties("debug").setBaseHeight(0.0F));

	private static final Register<BiomeBase> register = new Register(256);
	
	public static BiomeBase getBiomeFromName(String name)
	{
		return register.get(name);
	}
	public static BiomeBase getBiomeFromID(int id)
	{
		return register.get(id);
	}

	public int biomeID;
	
	public BiomeBase(int id, BiomeProperties properties)
	{
		this(id, true, properties);
	}
	public BiomeBase(int id, boolean register, BiomeProperties properties)
	{
		super(properties);
		biomeID = id;
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
}