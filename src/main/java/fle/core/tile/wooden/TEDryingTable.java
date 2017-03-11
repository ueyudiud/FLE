/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.tile.wooden;

import static fle.api.recipes.instance.RecipeMaps.DRYING;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.TEInventorySingleSlot;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import nebula.common.util.Worlds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

/**
 * @author ueyudiud
 */
public class TEDryingTable extends TEInventorySingleSlot
implements ITB_BlockActived, ITP_Drops
{
	protected int dryingTick;
	protected TemplateRecipeCache<ItemStack> cache;
	
	public TEDryingTable()
	{
		super();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.cache = DRYING.readFromNBT(compound, "recipe");
		this.dryingTick = compound.getInteger("progress");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		DRYING.writeToNBT(this.cache, compound, "recipe");
		compound.setInteger("progress", this.dryingTick);
		return compound;
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.stack = NBTs.getItemStackOrDefault(nbt, "i", this.stack);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		NBTs.setItemStack(nbt, "i", this.stack, true);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	protected void onInventoryChanged()
	{
		super.onInventoryChanged();
		syncToNearby();
		this.cache = null;
		this.dryingTick = 0;
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if (this.cache == null && this.stack != null && this.stack.stackSize == 1)
		{
			this.cache = DRYING.findRecipe(this.stack);
		}
		if (this.cache != null)
		{
			IWorldPropProvider provider = WorldPropHandler.getWorldProperty(this.world);
			float rain = provider.getHumidity(this);
			if (isRaining())
			{
				rain *= 5.0F;
			}
			this.dryingTick += Math.ceil(this.cache.<Float>get(1) / rain);
			if (this.dryingTick >= this.cache.<Integer>get(0))
			{
				this.stack = this.cache.<ItemStack>get(2);
				onInventoryChanged();
			}
		}
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side,
			float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
		{
			if (isServer())
			{
				if (this.stack == null)
				{
					if (stack != null)
					{
						this.stack = stack.splitStack(1);
					}
				}
				else
				{
					if (stack == null)
					{
						player.setHeldItem(hand, this.stack);
						this.stack = null;
					}
					else
					{
						if (!player.inventory.addItemStackToInventory(this.stack))
						{
							Worlds.spawnDropInWorld(this, this.stack);
						}
						this.stack = null;
					}
				}
				onInventoryChanged();
			}
			return EnumActionResult.SUCCESS;
		}
		return super.onBlockActivated(player, hand, stack, side, hitX, hitY, hitZ);
	}
	
	@Override
	public String getName()
	{
		return "inventory.drying.table";
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockState state, int fortune, boolean silkTouch)
	{
		return ImmutableList.of(new ItemStack(Items.STICK, 4));
	}
}