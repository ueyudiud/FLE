/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.util;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class Lights
{
	public static int get256Light(World world, BlockPos pos, EnumSkyBlock type)
	{
		return world.getLightFor(type, pos) << 4;
	}
	
	public static int blend4Light(int light1, int light2, int light3, int light4)
	{
		return (light1 + light2 + light3 + light4) >> 2 & 0xFF;
	}
	
	public static int blend2Light(int light1, int light2)
	{
		return (light1 + light2) >> 1 & 0xFF;
	}
	
	public static int blend4MixLight(int light1, int light2, int light3, int light4)
	{
		return (light1 + light2 + light3 + light4) >> 2 & 0xFF00FF;
	}
	
	public static int blend2MixLight(int light1, int light2)
	{
		return (light1 + light2) >> 1 & 0xFF00FF;
	}
	
	public static int mixSkyBlockLight(int sky, int block)
	{
		return sky << 16 | block;
	}
	
	@SideOnly(Side.CLIENT)
	public static VertexBuffer lightmap(VertexBuffer buffer, int light)
	{
		return buffer.lightmap(light >> 16 & 0xFF, light & 0xFF);
	}
}
