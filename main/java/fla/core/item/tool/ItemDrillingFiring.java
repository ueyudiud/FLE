package fla.core.item.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fla.api.item.tool.ItemDamageResource;
import fla.api.util.FlaValue;

public class ItemDrillingFiring extends ItemFlaTool
{
	private float chance;
	
	public ItemDrillingFiring(int damage, float chance) 
	{
		setMaxDamage(damage);
		this.chance = chance;
	}

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xP, float yP, float zP)
    {
        if (side == 0)
        {
            --y;
        }

        if (side == 1)
        {
            ++y;
        }

        if (side == 2)
        {
            --z;
        }

        if (side == 3)
        {
            ++z;
        }

        if (side == 4)
        {
            --x;
        }

        if (side == 5)
        {
            ++x;
        }

        if (!player.canPlayerEdit(x, y, z, side, stack))
        {
            return false;
        }
        else
        {
            if (world.isAirBlock(x, y, z) && world.rand.nextFloat() < chance)
            {
                world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                world.setBlock(x, y, z, Blocks.fire);
            }

            damageItem(player, stack, ItemDamageResource.UseItem);
            return true;
        }
    }

	@Override
	public void damageItem(EntityLivingBase entity, ItemStack stack,
			ItemDamageResource resource) 
	{
		if(resource == ItemDamageResource.UseItem) stack.damageItem(1, entity);
	}

	@Override
	public int getToolMaxDamage(ItemStack stack) 
	{
		return stack.getMaxDamage();
	}

	@Override
	public int getToolDamage(ItemStack stack) 
	{
		return stack.getItemDamage();
	}

	@Override
	public String getToolType(ItemStack stack) 
	{
		return FlaValue.drilling_firing;
	}
}