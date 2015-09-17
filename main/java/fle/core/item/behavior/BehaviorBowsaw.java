package fle.core.item.behavior;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;
import fle.api.item.IItemBehaviour;
import fle.api.item.ItemFleMetaBase;
import fle.api.world.BlockPos;
import fle.core.init.Rs;

public class BehaviorBowsaw extends BehaviorTool
{
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos)
	{
        if (!player.canPlayerEdit(x, y, z, FleAPI.getIndexFromDirection(side), itemstack))
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
					item.damageItem(aStack, aPlayer, EnumDamageResource.UseTool, block.getBlockHardness(aWorld, mop.blockX, mop.blockY, mop.blockZ) / 2F);
					aPlayer.dropPlayerItemWithRandomChoice(output.copy(), false);
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