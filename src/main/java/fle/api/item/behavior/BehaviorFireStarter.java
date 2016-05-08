package fle.api.item.behavior;

import farcore.enums.Direction;
import farcore.enums.EnumBlock;
import farcore.enums.EnumDamageResource;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorFireStarter extends BehaviorBase
{
	private final float chance;
	
	public BehaviorFireStarter(float aChance) 
	{
		chance = aChance;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		Direction direction = Direction.directions[side];
		x += direction.x;
		y += direction.y;
		z += direction.z;
		if (!player.canPlayerEdit(x, y, z, side, stack))
        {
            return false;
        }
        else
        {
        	if(world.isRemote)
        	{
                U.Inventorys.damage(stack, player, 3, EnumDamageResource.USE);
        	}
        	else
        	{
                if (world.isAirBlock(x, y, z) && world.rand.nextFloat() < chance)
                {
                    world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "fire.ignite", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                    EnumBlock.fire.spawn(world, x, y, z);
                    U.Inventorys.damage(stack, player, 3, EnumDamageResource.USE);
                }
                else
                {
                    U.Inventorys.damage(stack, player, 1, EnumDamageResource.USE);
                }
        	}

            return true;
        }
	}
}