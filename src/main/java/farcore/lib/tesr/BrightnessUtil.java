package farcore.lib.tesr;

import static farcore.lib.util.Direction.E;
import static farcore.lib.util.Direction.N;
import static farcore.lib.util.Direction.S;
import static farcore.lib.util.Direction.U;
import static farcore.lib.util.Direction.W;

import java.util.Arrays;

import farcore.lib.util.Direction;
import farcore.util.Maths;
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
			directionUX = u.x;
			directionUY = u.y;
			directionUZ = u.z;
			directionVX = v.x;
			directionVY = v.y;
			directionVZ = v.z;
		}
	}

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
	private static int blend4Brightness(int c1, int c2, int c3, int c)
	{
		return (c1 + c2 + c3 + c) >> 2 & 0x00FF00FF;
	}

	@SideOnly(Side.CLIENT)
	private static int blend2Brightness(int c1, int c2)
	{
		return (c1 + c2) >> 1 & 0x00FF00FF;
	}

	public int[] brightness = new int[4];
	public float[] color = new float[4];
	
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
			Arrays.fill(brightness, provider.getBrightness(x + direction.x, y + direction.y, z + direction.z));
			Arrays.fill(color, provider.getAmbientOcclusionLightValue(x + direction.x, y + direction.y, z + direction.z));
			return;
		}
		BrightnessSideInformation i = BrightnessSideInformation.information(direction);
		x += direction.x;
		y += direction.y;
		z += direction.z;
		float o = provider.getOpaqueness(x, y, z);
		int b = provider.getBrightness(x, y, z);
		float ao = provider.getAmbientOcclusionLightValue(x, y, z);
		if(o == 1F)
		{
			Arrays.fill(brightness, b);
			Arrays.fill(color, ao);
			return;
		}
		int bPO = provider.getBrightness(x + i.directionUX, y + i.directionUY, z + i.directionUZ);
		int bNO = provider.getBrightness(x - i.directionUX, y - i.directionUY, z - i.directionUZ);
		int bOP = provider.getBrightness(x + i.directionVX, y + i.directionVY, z + i.directionVZ);
		int bON = provider.getBrightness(x - i.directionVX, y - i.directionVY, z - i.directionVZ);
		float oPO = provider.getOpaqueness(x + i.directionUX, y + i.directionUY, z + i.directionUZ);
		float oNO = provider.getOpaqueness(x - i.directionUX, y - i.directionUY, z - i.directionUZ);
		float oOP = provider.getOpaqueness(x + i.directionVX, y + i.directionVY, z + i.directionVZ);
		float oON = provider.getOpaqueness(x - i.directionVX, y - i.directionVY, z - i.directionVZ);
		float aoPO = provider.getAmbientOcclusionLightValue(x + i.directionUX, y + i.directionUY, z + i.directionUZ);
		float aoNO = provider.getAmbientOcclusionLightValue(x - i.directionUX, y - i.directionUY, z - i.directionUZ);
		float aoOP = provider.getAmbientOcclusionLightValue(x + i.directionVX, y + i.directionVY, z + i.directionVZ);
		float aoON = provider.getAmbientOcclusionLightValue(x - i.directionVX, y - i.directionVY, z - i.directionVZ);
		float aoPP, aoPN, aoNP, aoNN;
		int bPP, bPN, bNP, bNN;
		if(oPO == 1F && oOP == 1F)
		{
			aoPP = (aoNO + aoOP) / 2F;
			bPP = blend2Brightness(bPO, bOP);
		}
		else
		{
			aoPP = provider.getAmbientOcclusionLightValue(x + i.directionUX + i.directionVX, y + i.directionUY + i.directionVY, z + i.directionUZ + i.directionVZ);
			aoPP = (Maths.lerp(aoPP, aoPO, oPO) + Maths.lerp(aoPP, aoOP, oOP)) / 2F;
			bPP = provider.getBrightness(x + i.directionUX + i.directionVX, y + i.directionUY + i.directionVY, z + i.directionUZ + i.directionVZ);
		}
		if(oPO == 1F && oON == 1F)
		{
			aoPN = (aoPO + aoON) / 2F;
			bPN = blend2Brightness(bPO, bON);
		}
		else
		{
			aoPN = provider.getAmbientOcclusionLightValue(x + i.directionUX - i.directionVX, y + i.directionUY - i.directionVY, z + i.directionUZ - i.directionVZ);
			aoPN = (Maths.lerp(aoPN, aoPO, oPO) + Maths.lerp(aoPN, aoON, oON)) / 2F;
			bPN = provider.getBrightness(x + i.directionUX - i.directionVX, y + i.directionUY - i.directionVY, z + i.directionUZ - i.directionVZ);
		}
		if(oNO == 1F && oON == 1F)
		{
			aoNN = (aoNO + aoON) / 2F;
			bNN = blend2Brightness(bNO, bON);
		}
		else
		{
			aoNN = provider.getAmbientOcclusionLightValue(x - i.directionUX - i.directionVX, y - i.directionUY - i.directionVY, z - i.directionUZ - i.directionVZ);
			aoNN = (Maths.lerp(aoNN, aoNO, oNO) + Maths.lerp(aoNN, aoON, oON)) / 2F;
			bNN = provider.getBrightness(x - i.directionUX - i.directionVX, y - i.directionUY - i.directionVY, z - i.directionUZ - i.directionVZ);
		}
		if(oNO == 1F && oOP == 1F)
		{
			aoNP = (aoNO + aoOP) / 2F;
			bNP = blend2Brightness(bNO, bOP);
		}
		else
		{
			aoNP = provider.getAmbientOcclusionLightValue(x - i.directionUX + i.directionVX, y - i.directionUY + i.directionVY, z - i.directionUZ + i.directionVZ);
			aoNP = (Maths.lerp(aoNP, aoNO, oNO) + Maths.lerp(aoNP, aoOP, oOP)) / 2F;
			bNP = provider.getBrightness(x - i.directionUX + i.directionVX, y - i.directionUY + i.directionVY, z - i.directionUZ + i.directionVZ);
		}
		color[0] = (aoOP + aoPP + aoPO + ao) / 4F;
		color[1] = (aoPO + aoPN + aoON + ao) / 4F;
		color[2] = (aoON + aoNN + aoNO + ao) / 4F;
		color[3] = (aoNO + aoNP + aoOP + ao) / 4F;
		brightness[0] = blend4Brightness(bOP, bPP, bPO, b);
		brightness[1] = blend4Brightness(bPO, bPN, bON, b);
		brightness[2] = blend4Brightness(bON, bNN, bNO, b);
		brightness[3] = blend4Brightness(bNO, bNP, bOP, b);
	}
}