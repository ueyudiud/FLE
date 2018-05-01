/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.wooden;

import static fle.api.recipes.instance.RecipeMaps.DRYING;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import nebula.common.inventory.ItemContainerSingle;
import nebula.common.inventory.ItemContainers;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_Drops;
import nebula.common.tile.TE05InventorySimple;
import nebula.common.util.Direction;
import nebula.common.util.Players;
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
public class TEDryingTable extends TE05InventorySimple implements ITB_BlockActived, ITP_Drops
{
	protected int								dryingTick;
	protected TemplateRecipeCache<ItemStack>	cache;
	
	public TEDryingTable()
	{
		super();
		this.items = new ItemContainers<>(new ItemContainerSingle(1) {
			@Override
			protected void onContainerChanged()
			{
				super.onContainerChanged();
				syncToNearby();
				TEDryingTable.this.cache = null;
				TEDryingTable.this.dryingTick = 0;
			}
		});
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.cache = DRYING.readFrom(compound, "recipe");
		this.dryingTick = compound.getInteger("progress");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		DRYING.writeTo(compound, "recipe", this.cache);
		compound.setInteger("progress", this.dryingTick);
		return compound;
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.items.readFrom(nbt, "i");
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		this.items.writeTo(nbt, "i");
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if (this.cache == null && this.items.getContainer(0).hasStackInContainer())
		{
			this.cache = DRYING.findRecipe(this.items.getStackInContainer(0));
		}
		if (this.cache != null)
		{
			IWorldPropProvider provider = WorldPropHandler.getWorldProperty(this.world);
			float rain = Math.min(0.0F, provider.getHumidity(this));
			rain += 1.0F;
			if (isRaining())
			{
				rain *= 5.0F;
			}
			this.dryingTick += Math.ceil(this.cache.<Float> get(1) / rain);
			if (this.dryingTick >= this.cache.<Integer> get(0))
			{
				this.items.setStackInContainer(0, this.cache.<ItemStack> get(2));
			}
		}
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (!player.isSneaking())
		{
			if (isServer())
			{
				if (!this.items.getContainer(0).hasStackInContainer())
				{
					if (stack != null)
					{
						this.items.setStackInContainer(0, stack.splitStack(1));
					}
				}
				else
				{
					if (stack == null)
					{
						player.setHeldItem(hand, this.items.extractStack(0, 1));
					}
					else
					{
						Players.giveOrDrop(player, this.items.extractStack(0, 1));
					}
				}
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
