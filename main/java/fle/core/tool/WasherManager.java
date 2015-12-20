package fle.core.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import flapi.world.BlockPos;
import fle.FLE;

public class WasherManager
{
	public static boolean tryWashingItem(World aWorld, EntityPlayer aPlayer)
	{
		if(FLE.fle.getKeyboard().isSneakKeyDown(aPlayer) && FLE.fle.getKeyboard().isPlaceKeyDown(aPlayer) && !aWorld.isRemote)
		{
			MovingObjectPosition pos1 = getMovingObjectPositionFromPlayer(aWorld, aPlayer, true);
			if(pos1 == null) return false;
			if(pos1.typeOfHit == MovingObjectType.BLOCK)
			{
				int waterCount = 0;
				for(int x1 = -4; x1 < 5; ++x1)
					for(int y1 = 0; y1 > -5; --y1)
						for(int z1 = -4; z1 < 5; ++z1)
						{
							BlockPos pos = new BlockPos(aWorld, pos1.blockX + x1, pos1.blockY + y1, pos1.blockZ + z1);
							if(pos.getBlock() == Blocks.water) ++waterCount;
							if(pos.getBiome() == BiomeGenBase.ocean || pos.getBiome() == BiomeGenBase.river) ++waterCount;
						}
				if(waterCount > 80)
				{
					aPlayer.openGui(FLE.MODID, -1, aWorld, pos1.blockX, pos1.blockY, pos1.blockZ);
					return true;
				}
			}
		}
		return false;
	}
	
	public static MovingObjectPosition getMovingObjectPositionFromPlayer(World aWorld, EntityPlayer aPlayer, boolean checkWater)
    {
        float f = 1.0F;
        float f1 = aPlayer.prevRotationPitch + (aPlayer.rotationPitch - aPlayer.prevRotationPitch) * f;
        float f2 = aPlayer.prevRotationYaw + (aPlayer.rotationYaw - aPlayer.prevRotationYaw) * f;
        double d0 = aPlayer.prevPosX + (aPlayer.posX - aPlayer.prevPosX) * (double)f;
        double d1 = aPlayer.prevPosY + (aPlayer.posY - aPlayer.prevPosY) * (double)f + (double)(aWorld.isRemote ? aPlayer.getEyeHeight() - aPlayer.getDefaultEyeHeight() : aPlayer.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = aPlayer.prevPosZ + (aPlayer.posZ - aPlayer.prevPosZ) * (double)f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        if (aPlayer instanceof EntityPlayerMP)
        {
            d3 = ((EntityPlayerMP)aPlayer).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        return aWorld.func_147447_a(vec3, vec31, checkWater, !checkWater, false);
    }
}