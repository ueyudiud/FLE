/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import static nebula.common.util.Direction.E;
import static nebula.common.util.Direction.N;
import static nebula.common.util.Direction.S;
import static nebula.common.util.Direction.U;
import static nebula.common.util.Direction.W;
import static nebula.common.util.Maths.lerp;

import java.util.Arrays;

import nebula.common.util.Direction;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BrightnessUtil
{
	public static enum BrightnessSideInformation
	{
		DOWN(W, N),
		UP(E, S),
		NORTH(E, U),
		SOUTH(W, U),
		WEST(N, U),
		EAST(S, U);
		
		public static final BrightnessSideInformation[] INFORMATIONS = values();
		
		public static BrightnessSideInformation information(Direction direction)
		{
			return INFORMATIONS[direction.ordinal()];
		}
		
		int directionUX;
		int directionUY;
		int directionUZ;
		int directionVX;
		int directionVY;
		int directionVZ;
		
		BrightnessSideInformation(Direction u, Direction v)
		{
			this.directionUX = u.x;
			this.directionUY = u.y;
			this.directionUZ = u.z;
			this.directionVX = v.x;
			this.directionVY = v.y;
			this.directionVZ = v.z;
		}
	}
	
	private static final int[][] INDEXS1 = {{1, 0, -1}, {3, -1, 0}, {5, 1, 0}, {7, 0, 1}};
	private static final int[][] INDEXS = {{4, 5, 7, 8, 1, 1}, {4, 5, 1, 2, 1, -1}, {4, 3, 1, 0, -1, -1}, {4, 3, 7, 6, -1, 1}};
	
	private static BrightnessUtil util;
	
	public static BrightnessUtil instance()
	{
		if(util == null)
		{
			util = new BrightnessUtil();
		}
		return util;
	}
	
	@SideOnly(Side.CLIENT)
	private static int blend4Brightness(int c, int c1, int c2, int c3)
	{
		if(c  == 0) c  = Math.max(c1, c2) & 0xFF0000 | Math.max(c1 & 0xFF, c2 & 0xFF);
		if(c1 == 0) c1 = Math.max(0, c - 0x10000) & 0xFF0000 | Math.max(0, (c & 0xFF) - 1);
		if(c2 == 0) c2 = Math.max(0, c - 0x10000) & 0xFF0000 | Math.max(0, (c & 0xFF) - 1);
		return (c1 + c2 + c3 + c) >> 2 & 0x00FF00FF;
	}
	
	@SideOnly(Side.CLIENT)
	private static int blend2Brightness(int c1, int c2)
	{
		return (c1 + c2) >> 1 & 0x00FF00FF;
	}
	
	public int[] brightness = new int[4];
	public float[] color = new float[4];
	
	public void caculateBrightness(
			float aoNN, float aoON, float aoPN,
			float aoNO, float aoOO, float aoPO,
			float aoNP, float aoOP, float aoPP,
			
			float oNN, float oON, float oPN,
			float oNO, float oOO, float oPO,
			float oNP, float oOP, float oPP,
			
			int bNN, int bON, int bPN,
			int bNO, int bOO, int bPO,
			int bNP, int bOP, int bPP)
	{
		if(!Minecraft.isAmbientOcclusionEnabled())
		{
			Arrays.fill(this.brightness, bOO);
			Arrays.fill(this.color, aoOO);
			return;
		}
		else
		{
			if(oOO == 1F)
			{
				Arrays.fill(this.brightness, bOO);
				Arrays.fill(this.color, aoOO);
				return;
			}
			float[] os = {
					oNN, oON, oPN,
					oNO, oOO, oPO,
					oNP, oOP, oPP
			};
			int[] bs = {
					bNN, bON, bPN,
					bNO, bOO, bPO,
					bNP, bOP, bPP
			};
			float[] aos = {
					aoNN, aoON, aoPN,
					aoNO, aoOO, aoPO,
					aoNP, aoOP, aoPP
			};
			
			for (int[] is : INDEXS)
			{
				if (os[is[1]] == 1F && os[is[2]] == 1F)
				{
					aos[is[3]] = (aos[is[1]] + aos[is[2]]) / 2F;
					bs[is[3]] = blend2Brightness(bs[is[1]], bs[is[2]]);
				}
				else
				{
					aos[is[3]] = (lerp(aos[is[3]], aos[is[1]], os[is[1]]) + lerp(aos[is[3]], aos[is[2]], os[is[2]])) * .5F;
				}
			}
			caculateBrightness(aos, bs);
		}
	}
	
	/**
	 * Calculate brightness of target coord.<br>
	 * Result format : Direction for UV
	 * [(-, +), (+, +), (+, -), (-, -)]
	 * <br>
	 * @param provider The brightness provider.
	 * @param x
	 * @param y
	 * @param z
	 * @param direction
	 */
	public void caculateBrightness(ICoordableBrightnessProvider provider, int x, int y, int z, Direction direction)
	{
		if(!Minecraft.isAmbientOcclusionEnabled())
		{
			Arrays.fill(this.brightness, provider.getBrightness(x + direction.x, y + direction.y, z + direction.z));
			Arrays.fill(this.color, provider.getAmbientOcclusionLightValue(x + direction.x, y + direction.y, z + direction.z));
			return;
		}
		else
		{
			BrightnessSideInformation i = BrightnessSideInformation.information(direction);
			x += direction.x;
			y += direction.y;
			z += direction.z;
			float o = provider.getOpaqueness(x, y, z);
			int b = provider.getBrightness(x, y, z);
			float ao = provider.getAmbientOcclusionLightValue(x, y, z);
			if(o == 1F)
			{
				Arrays.fill(this.brightness, b);
				Arrays.fill(this.color, ao);
				return;
			}
			float[] os = new float[9];
			int[] bs = new int[9];
			float[] aos = new float[9];
			
			os [4] = o;
			bs [4] = b;
			aos[4] = ao;
			
			for (int[] is : INDEXS1)
			{
				os[is[0]] = provider.getOpaqueness(
						x + i.directionUX * is[1] + i.directionVX * is[2],
						y + i.directionUY * is[1] + i.directionVY * is[2],
						z + i.directionUZ * is[1] + i.directionVZ * is[2]);
				bs[is[0]] = provider.getBrightness(
						x + i.directionUX * is[1] + i.directionVX * is[2],
						y + i.directionUY * is[1] + i.directionVY * is[2],
						z + i.directionUZ * is[1] + i.directionVZ * is[2]);
				aos[is[0]] = provider.getAmbientOcclusionLightValue(
						x + i.directionUX * is[1] + i.directionVX * is[2],
						y + i.directionUY * is[1] + i.directionVY * is[2],
						z + i.directionUZ * is[1] + i.directionVZ * is[2]);
			}
			for (int[] is : INDEXS)
			{
				if (os[is[1]] == 1F && os[is[2]] == 1F)
				{
					aos[is[3]] = (aos[is[1]] + aos[is[2]]) / 2F;
					bs[is[3]] = blend2Brightness(bs[is[1]], bs[is[2]]);
				}
				else
				{
					aos[is[3]] = provider.getAmbientOcclusionLightValue(
							x + i.directionUX * is[4] + i.directionVX * is[5],
							y + i.directionUY * is[4] + i.directionVY * is[5],
							z + i.directionUZ * is[4] + i.directionVZ * is[5]);
					aos[is[3]] = (lerp(aos[is[3]], aos[is[1]], os[is[1]]) + lerp(aos[is[3]], aos[is[2]], os[is[2]])) / 2F;
					bs[is[3]] = provider.getBrightness(
							x + i.directionUX * is[4] + i.directionVX * is[5],
							y + i.directionUY * is[4] + i.directionVY * is[5],
							z + i.directionUZ * is[4] + i.directionVZ * is[5]);
				}
			}
			caculateBrightness(aos, bs);
		}
	}
	
	private void caculateBrightness(float[] aos, int[] bs)
	{
		for (int I = 0; I < 4; ++I)
		{
			this.color[I] = (aos[INDEXS[I][0]] + aos[INDEXS[I][1]] + aos[INDEXS[I][2]] + aos[INDEXS[I][3]]) / 4F;
			this.brightness[I] = blend4Brightness(bs[INDEXS[I][0]], bs[INDEXS[I][1]], bs[INDEXS[I][2]], bs[INDEXS[I][3]]);
		}
	}
}