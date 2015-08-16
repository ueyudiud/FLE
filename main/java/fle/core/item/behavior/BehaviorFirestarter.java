package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;
import fle.api.item.ItemFleMetaBase;
import fle.api.world.BlockPos;

public class BehaviorFirestarter extends BehaviorTool
{
	private final float chance;
	
	public BehaviorFirestarter(float aChance) 
	{
		chance = aChance;
	}
	
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		BlockPos aPos = new BlockPos(world, x, y, z).toPos(side);
		int tX = aPos.x;
		int tY = aPos.y;
		int tZ = aPos.z;
		if (!player.canPlayerEdit(tX, tY, tZ, FleAPI.getIndexFromDirection(side), itemstack))
        {
            return false;
        }
        else
        {
            if (world.isAirBlock(tX, tY, tZ) && world.rand.nextFloat() < chance)
            {
                world.playSoundEffect((double)tX + 0.5D, (double)tY + 0.5D, (double)tZ + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                world.setBlock(tX, tY, tZ, Blocks.fire);
                item.damageItem(itemstack, player, EnumDamageResource.UseTool, 1);
            }
            else
            {
                item.damageItem(itemstack, player, EnumDamageResource.UseTool, 0.25F);
            }

            return true;
        }
	}
}