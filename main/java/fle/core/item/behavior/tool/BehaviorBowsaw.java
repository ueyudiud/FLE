package fle.core.item.behavior.tool;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import farcore.util.U.N;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import flapi.world.BlockPos;
import fle.core.init.Rs;

public class BehaviorBowsaw extends BehaviorTool
{
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos)
	{
        if (!player.canPlayerEdit(x, y, z, N.side(side), itemstack))
        {
            return false;
        }
        else
        {
        	player.setItemInUse(itemstack, 1000);
        	return true;
        }
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemFleMetaBase item, World aWorld,
			EntityPlayer aPlayer, int aUseTick, ItemStack aStack)
	{
		if(aUseTick > 100)
		{
			MovingObjectPosition mop = item.getMovingObjectPositionFromPlayer(aWorld, aPlayer, false);
			if(mop.typeOfHit == MovingObjectType.BLOCK)
			{
				BlockPos pos = new BlockPos(aWorld, mop.blockX, mop.blockY, mop.blockZ);
				Block block = pos.getBlock();
				int meta = block.getDamageValue(aWorld, pos.x, pos.y, pos.z);
				ItemStack output = Rs.getLogCraftOutput(new ItemStack(block, 1, meta));
				if(output != null)
				{
					item.damageItem(aStack, aPlayer, EnumDamageResource.UseTool, block.getBlockHardness(aWorld, mop.blockX, mop.blockY, mop.blockZ) / 4F);
					if(!aWorld.isRemote)
					{
						EntityItem entity = new EntityItem(aWorld, mop.blockX + 0.5F, mop.blockY + 0.5F, mop.blockZ + 0.5F, output.copy());
						entity.motionX = aWorld.rand.nextDouble() * 0.05;
						entity.motionY = aWorld.rand.nextDouble() * 0.035 + 0.1;
						entity.motionZ = aWorld.rand.nextDouble() * 0.05;
						aWorld.spawnEntityInWorld(entity);
					}
					aWorld.setBlockToAir(mop.blockX, mop.blockY, mop.blockZ);
					aWorld.removeTileEntity(mop.blockX, mop.blockY, mop.blockZ);
				}
				else
				{
					item.damageItem(aStack, aPlayer, EnumDamageResource.DropOnGround, 0.2F);
				}
			}
		}
	}
}