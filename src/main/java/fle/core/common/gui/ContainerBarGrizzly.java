/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.common.gui;

import farcore.data.EnumToolTypes;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.recipes.instance.RecipeMaps;
import nebula.common.gui.ContainerBlockPosition;
import nebula.common.gui.SlotBase;
import nebula.common.inventory.InventorySimple;
import nebula.common.inventory.InventoryWrapFactory;
import nebula.common.stack.AbstractStack;
import nebula.common.util.TileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class ContainerBarGrizzly extends ContainerBlockPosition implements ITickable
{
	private InventorySimple inventory = new InventorySimple(11);
	private TemplateRecipeCache<ItemStack> cache;
	
	public ContainerBarGrizzly(EntityPlayer player, World world, BlockPos pos)
	{
		super(player, world, pos);
		IInventory real = InventoryWrapFactory.wrap("inventory.bar.grizzly", this.inventory);
		addOpenerSlots();
		int size = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(real, 0, 40, 16));
		addSlotToContainer(new SlotBase(real, 1, 58, 16));
		addOutputSlotMatrix(real, 97, 16, 3, 3, 2, 18, 18);
		TL
		locationInput = new TL(size).addToList(),
		locationTool = new TL(size + 1) {
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return EnumToolTypes.BAR_GRIZZLY.toolMatch(stack);
			}
		}.addToList(),
		locationOutput = new TL(size + 2, size + 11).addToList();
		this.locationHand.appendTransferLocate(locationTool).appendTransferLocate(locationInput).appendTransferLocate(this.locationBag);
		this.locationBag.appendTransferLocate(locationTool).appendTransferLocate(locationInput).appendTransferLocate(this.locationHand);
		locationInput.appendTransferLocate(this.locationPlayer);
		locationOutput.appendTransferLocate(this.locationPlayer);
		locationTool.appendTransferLocate(this.locationPlayer);
		this.currentValue[2] = 1;
	}
	
	@Override
	protected boolean match(IBlockState oldState, IBlockState newstate)
	{
		return oldState.getBlock() == newstate.getBlock();
	}
	
	@Override
	protected int getFieldCount()
	{
		return 3;
	}
	
	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			if (this.cache == null)
			{
				this.cache = RecipeMaps.WASHING_BARGRIZZLY.findRecipe(this.inventory.getStack(0));
				if (this.cache != null)
				{
					this.inventory.decrStackSize(0, this.cache.<AbstractStack>get1(0));
					this.currentValue[2] = this.cache.<Integer>get(0);
					detectAndSendChanges();
				}
			}
			else if (this.cache != null && this.currentValue[1] > 100)
			{
				if (this.inventory.hasStackInSlot(1) && EnumToolTypes.BAR_GRIZZLY.toolMatch(this.inventory.getStack(1)))
				{
					if (this.currentValue[0] < this.currentValue[2])
					{
						TileEntities.damageTool(this.inventory, 1, 0.01F, this.opener, EnumToolTypes.BAR_GRIZZLY);
						this.currentValue[0] ++;
					}
					else
					{
						this.inventory.insertAllStacks(this.cache.<ItemStack[]>get(1), 2, 11, ()-> {
							this.currentValue[0] = 0;
							this.cache = null;
							this.currentValue[2] = 1;
							return null;
						});
					}
					detectAndSendChanges();
				}
			}
		}
		if (this.currentValue[1] > 0)
		{
			this.currentValue[1]--;
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		dropOrGivePlayerPlayerItems(this.inventory);
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		switch (type)
		{
		case 0 :
			if (value == 0)
			{
				this.currentValue[1] = Math.min(this.currentValue[1] + 40, 400);
			}
			break;
		default:
			break;
		}
	}
	
	public int getProgress()
	{
		return this.currentValue[0];
	}
	
	public int getPower()
	{
		return this.currentValue[1];
	}
	
	public int getMaxProgress()
	{
		return this.currentValue[2];
	}
}