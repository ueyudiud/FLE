/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.render;

import static nebula.common.util.Maths.lerp;

import java.util.Arrays;

import nebula.common.util.Direction;
import nebula.common.world.IBlockCoordQuarterProperties;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class BrightnessProviderQuarterCoord extends BrightnessUtil
{
	private float[][][] os  = new float[6][6][6];
	private float[][][] aos = new float[6][6][6];
	private int  [][][] bs  = new int  [6][6][6];
	
	public void initalizeValue(final IBlockCoordQuarterProperties properties)
	{
		for (int i = 0; i < 6; ++i)
			for (int j = 0; j < 6; ++j)
				for (int k = 0; k < 6; ++k)
				{
					this.os[i][j][k] = properties.getOpaqueness(i - 1, j - 1, k - 1);
					this.bs[i][j][k] = properties.getBrightness(i - 1, j - 1, k - 1);
					this.aos[i][j][k] = properties.getAmbientOcclusionLightValue(i - 1, j - 1, k - 1);
				}
	}
	
	@Override
	public void caculateBrightness(ICoordableBrightnessProvider unsed,
			int x, int y, int z, Direction direction)
	{
		x += direction.x + 1;
		y += direction.y + 1;
		z += direction.z + 1;
		if (!Minecraft.isAmbientOcclusionEnabled() || this.os[x][y][z] == 1.0F)
		{
			Arrays.fill(this.brightness, this.bs[x][y][z]);
			Arrays.fill(this.color, this.aos[x][y][z]);
		}
		else
		{
			BrightnessSideInformation i = BrightnessSideInformation.information(direction);
			caculateBrightness(i, x, y, z);
			caculateColorByEdge(i, x, y, z);
		}
		super.caculateBrightness(unsed, x, y, z, direction);
	}
	
	private void caculateColorByEdge(BrightnessSideInformation i, int x, int y, int z)
	{
		
	}
	
	private void caculateBrightness(BrightnessSideInformation i, int x, int y, int z)
	{
		float[] os = new float[9];
		float[] aos = new float[9];
		int[] bs = new int[9];
		
		os[4] = this.os[x][y][z];
		bs[4] = this.bs[x][y][z];
		
		for (int[] is : INDEXS1)
		{
			os[is[0]] = this.os[x + i.directionUX * is[1] + i.directionVX * is[2]][y + i.directionUY * is[1] + i.directionVY * is[2]][z + i.directionUZ * is[1] + i.directionVZ * is[2]];
			bs[is[0]] = this.bs[x + i.directionUX * is[1] + i.directionVX * is[2]][y + i.directionUY * is[1] + i.directionVY * is[2]][z + i.directionUZ * is[1] + i.directionVZ * is[2]];
			aos[is[0]] = this.aos[x + i.directionUX * is[1] + i.directionVX * is[2]][y + i.directionUY * is[1] + i.directionVY * is[2]][z + i.directionUZ * is[1] + i.directionVZ * is[2]];
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
				aos[is[3]] = this.aos[x + i.directionUX * is[4] + i.directionVX * is[5]][y + i.directionUY * is[4] + i.directionVY * is[5]][z + i.directionUZ * is[4] + i.directionVZ * is[5]];
				aos[is[3]] = (lerp(aos[is[3]], aos[is[1]], os[is[1]]) + lerp(aos[is[3]], aos[is[2]], os[is[2]])) / 2F;
				bs[is[3]] = this.bs[x + i.directionUX * is[4] + i.directionVX * is[5]][y + i.directionUY * is[4] + i.directionVY * is[5]][z + i.directionUZ * is[4] + i.directionVZ * is[5]];
			}
		}
		caculateBrightness(aos, bs);
	}
}
