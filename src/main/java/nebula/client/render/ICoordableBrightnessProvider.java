/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The coordinate brightness data provider.
 * 
 * @author ueyudiud
 */
public interface ICoordableBrightnessProvider
{
	@SideOnly(Side.CLIENT)
	static ICoordableBrightnessProvider wrap(World world)
	{
		return new SimpleBrightnessProvider(world);
	}
	
	@SideOnly(Side.CLIENT)
	int getBrightness(int x, int y, int z);
	
	@SideOnly(Side.CLIENT)
	float getAmbientOcclusionLightValue(int x, int y, int z);
	
	@SideOnly(Side.CLIENT)
	float getOpaqueness(int x, int y, int z);
	
	@SideOnly(Side.CLIENT)
	default float getReflection(int x, int y, int z)
	{
		return 1.0F;
	}
	
	@SideOnly(Side.CLIENT)
	class SimpleBrightnessProvider implements ICoordableBrightnessProvider
	{
		private final World		world;
		private MutableBlockPos	pos	= new MutableBlockPos();
		
		private SimpleBrightnessProvider(World world)
		{
			this.world = world;
		}
		
		@Override
		public int getBrightness(int x, int y, int z)
		{
			return world.getBlockState(pos.setPos(x, y, z)).getPackedLightmapCoords(world, pos);
		}
		
		@Override
		public float getAmbientOcclusionLightValue(int x, int y, int z)
		{
			return world.getBlockState(pos.setPos(x, y, z)).getAmbientOcclusionLightValue();
		}
		
		@Override
		public float getOpaqueness(int x, int y, int z)
		{
			return world.getBlockLightOpacity(pos.setPos(x, y, z)) / 255F;
		}
	}
}
