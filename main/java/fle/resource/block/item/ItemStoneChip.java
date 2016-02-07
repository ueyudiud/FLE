package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import farcore.block.item.ItemBlockBase;
import farcore.item.enums.EnumItemSize;
import farcore.item.enums.EnumParticleSize;
import farcore.item.interfaces.IItemSize;
import flapi.FleResource;
import flapi.block.item.ItemFleMultipassRender;
import fle.core.enums.EnumRockSize;
import fle.resource.block.auto.BlockUniversalStoneChip;
import fle.resource.entity.EntityThrowStone;

public class ItemStoneChip extends ItemFleMultipassRender<BlockUniversalStoneChip>
implements IItemSize
{
	public ItemStoneChip(Block block)
	{
		super(block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "."
				+ this.block.getRockName(stack).replace(' ', '.') + "."
				+ this.block.getRockSize(stack).name();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return translateToLocal(
				this.block.getRockSize(stack).getUnlocalized() + ".name",
				translateToLocal(this.block.getRock(stack).getTranslateName(),
						new Object[0]));
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		return block.getRockSize(stack).ordinal() * FleResource.rock.size()
				+ FleResource.rock.serial(block.getRockName(stack));
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		player.setItemInUse(stack, getMaxItemUseDuration(stack));
		return stack;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick)
	{
		boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

		float f = (float)tick / 2000.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if ((double)f < 0.1D)
        {
            return;
        }

        if (f > 1.0F)
        {
            f = 1.0F;
        }
        
        if(!flag)
        {
        	--stack.stackSize;
        	if(stack.stackSize <= 0)
        		player.destroyCurrentEquippedItem();
        }

        EntityThrowStone stone = new EntityThrowStone(world, stack, player, f * 2.0F);
        
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
        {
            stone.setFire(100);
        }
        
        if (!world.isRemote)
        {
            world.spawnEntityInWorld(stone);
        }
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 7200;
	}

	@Override
	public EnumItemSize getItemSize(ItemStack stack)
	{
		return block.getRockSize(stack) == EnumRockSize.small ?
				EnumItemSize.tiny : EnumItemSize.small;
	}

	@Override
	public EnumParticleSize getParticleSize(ItemStack stack)
	{
		return block.getRockSize(stack) == EnumRockSize.small ?
				EnumParticleSize.nuggetic : EnumParticleSize.ingotic;
	}
}