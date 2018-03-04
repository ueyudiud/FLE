/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tile.rocky;

import farcore.data.M;
import farcore.energy.thermal.IThermalHandler;
import farcore.energy.thermal.IThermalProvider;
import farcore.energy.thermal.ThermalNet;
import farcore.energy.thermal.instance.ThermalHandlerSimple;
import farcore.handler.FarCoreEnergyHandler;
import fle.api.recipes.instance.SimplyKilnRecipe;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import nebula.common.util.Players;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

/**
 * @author ueyudiud
 */
public class TESimplyKiln extends TESynchronization implements IThermalProvider, ITB_BlockActived
{
	public ItemStack stack;
	
	private long progress;
	private ThermalHandlerSimple handler = new ThermalHandlerSimple(this);
	
	{
		this.handler.material = M.argil;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.handler.readFromNBT(nbt);
		this.stack = NBTs.getItemStackOrDefault(nbt, "stack", null);
		this.progress = nbt.getLong("progress");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		this.handler.writeToNBT(nbt);
		nbt.setLong("progress", this.progress);
		NBTs.setItemStack(nbt, "stack", this.stack, false);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		NBTs.setItemStack(nbt, "s1", this.stack, true);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (this.stack != (this.stack = NBTs.getItemStackOrDefault(nbt, "s1", this.stack)))
		{
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (isServer())
		{
			if (hand == EnumHand.OFF_HAND)
				return EnumActionResult.PASS;
			if (this.stack == null)
			{
				if (stack != null)
				{
					int size = SimplyKilnRecipe.getMaxStacksizeInKiln(stack);
					if (size > 0)
					{
						if (stack.stackSize == 1)
						{
							this.stack = stack;
							player.setHeldItem(hand, null);
						}
						else
							this.stack = stack.splitStack(1);
					}
				}
			}
			else if (stack == null)
			{
				player.setHeldItem(hand, this.stack);
				this.stack = null;
				this.progress = 0;
			}
			else if (ItemStacks.areItemAndTagEqual(this.stack, stack))
			{
				int size = SimplyKilnRecipe.getMaxStacksizeInKiln(stack);
				if (this.stack.stackSize < size)
				{
					if (stack.stackSize == 1)
					{
						player.setHeldItem(hand, null);
					}
					else
					{
						stack.stackSize --;
					}
					this.stack.stackSize ++;
				}
			}
			else
			{
				Players.giveOrDrop(player, this.stack.splitStack(1));
				if (this.stack.stackSize == 0)
				{
					this.stack = null;
					this.progress = 0;
				}
			}
			syncToNearby();
			return EnumActionResult.SUCCESS;
		}
		return hand == EnumHand.MAIN_HAND ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		FarCoreEnergyHandler.onAddFromWorld(this);
	}
	
	@Override
	public void onRemoveFromLoadedWorld()
	{
		super.onRemoveFromLoadedWorld();
		FarCoreEnergyHandler.onRemoveFromWorld(this);
	}
	
	@Override
	protected void updateServer()
	{
		SimplyKilnRecipe recipe;
		super.updateServer();
		if (!isAirNearby(true) && (recipe = SimplyKilnRecipe.getRecipe(this.stack)) != null)
		{
			if (ThermalNet.getRealHandlerTemperature(this.handler, Direction.Q) >= recipe.minTemp)
			{
				this.handler.energy -= 300L * this.stack.stackSize;
				if (++ this.progress == recipe.duration)
				{
					this.stack = ItemStacks.setSizeOf(recipe.output.instance(), recipe.output.size(null) * this.stack.stackSize);
					this.progress = 0;
					syncToNearby();
				}
			}
		}
		else
		{
			this.progress = 0;
		}
	}
	
	@Override
	public IThermalHandler getThermalHandler()
	{
		return this.handler;
	}
}
