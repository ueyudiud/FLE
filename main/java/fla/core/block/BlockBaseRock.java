package fla.core.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;
import fla.core.FlaCreativeTab;

public abstract class BlockBaseRock extends BlockBaseSub
{
	protected BlockBaseRock(Material material, SubTag...tags) 
	{
		super(material, tags);
		setCreativeTab(FlaCreativeTab.fla_other_tab);
	}
	
	protected abstract int getMaxDamage();

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.UNKNOWN;
	}

	@Override
	public abstract int getRenderType();
	
	@Override
	public boolean isOpaqueCube() 
	{
		return true;
	}

	@Override
	protected abstract boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour);

	@Override
	public abstract void onBlockDestroyedByExplosion(World world, int x,
			int y, int z, Explosion explosion);
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) 
	{
		for(int i = 0; i < getMaxDamage(); ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y,
			int z, int meta) 
	{
        player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player))
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = this.createStackedBlock(meta);

            if (itemstack != null)
            {
                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
            for (ItemStack is : items)
            {
                this.dropBlockAsItem(world, x, y, z, is);
            }
        }
        else
        {
            harvesters.set(player);
            int i1 = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(world, x, y, z, meta, i1);
            harvesters.set(null);
        }
	}
}
