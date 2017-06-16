/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import nebula.base.ArrayListAddWithCheck;
import nebula.common.inventory.IBasicInventory;
import nebula.common.item.ITool;
import nebula.common.stack.AbstractStack;
import nebula.common.tile.IItemHandlerIO;
import nebula.common.tile.IToolableTile;
import nebula.common.tile.TEBase;
import nebula.common.tool.EnumToolType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * @author ueyudiud
 */
public final class TileEntities
{
	private TileEntities() {}
	
	public static boolean onTileActivatedGeneral(EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
			Direction facing, float hitX, float hitY, float hitZ, TileEntity tile)
	{
		if(tile == null) return false;
		if(tile instanceof TEBase && !((TEBase) tile).isInitialized())
			return false;
		EnumFacing facing2 = facing.of();
		if(heldItem != null && heldItem.getItem() instanceof ITool &&
				tile instanceof IToolableTile)
		{
			ITool tool = (ITool) heldItem.getItem();
			ActionResult<Float> result;
			for(EnumToolType toolType : tool.getToolTypes(heldItem))
			{
				if((result = ((IToolableTile) tile).onToolClick(playerIn, toolType, heldItem, facing, hitX, hitY, hitZ)).getType() != EnumActionResult.PASS)
				{
					tool.onToolUse(playerIn, heldItem, toolType, result.getResult());
					return true;
				}
			}
		}
		if(tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing2))
		{
			if(heldItem != null && heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
			{
				IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing2);
				IFluidHandler handler2 = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				FluidStack input;
				FluidStack output;
				int amt;
				if((output = handler2.drain(Integer.MAX_VALUE, false)) != null)
				{
					if((amt = handler.fill(output, true)) != 0)
					{
						input = output.copy();
						input.amount = amt;
						handler2.drain(input, true);
						return true;
					}
				}
				else if((output = handler.drain(Integer.MAX_VALUE, false)) != null)
				{
					if((amt = handler2.fill(output, true)) != 0)
					{
						input = output.copy();
						input.amount = amt;
						handler.drain(input, true);
						return true;
					}
				}
			}
		}
		if(tile instanceof IItemHandlerIO)
		{
			IItemHandlerIO handler = (IItemHandlerIO) tile;
			ActionResult<ItemStack> result = handler.onPlayerTryUseIO(heldItem, playerIn, facing, hitX, hitY, hitZ, true);
			if(result.getType() == EnumActionResult.SUCCESS)
			{
				if(heldItem != result.getResult())
				{
					playerIn.setHeldItem(hand, heldItem = ItemStack.copyItemStack(result.getResult()));
					for(int i = 0; i < playerIn.inventory.getSizeInventory(); ++i)
					{
						ItemStack stack = playerIn.inventory.getStackInSlot(i);
						if(stack != null && stack != heldItem)
						{
							result = handler.onPlayerTryUseIO(stack, playerIn, facing, hitX, hitY, hitZ, false);
							switch(result.getType())
							{
							case FAIL : return true;
							case SUCCESS :
								if(result.getResult() != stack)
								{
									playerIn.inventory.setInventorySlotContents(i, stack);
								}
							case PASS : break;
							}
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean matchOutput(AbstractStack output, ItemStack stackInSlot)
	{
		return matchOutput(output, stackInSlot, 64);
	}
	
	public static boolean matchOutput(AbstractStack output, ItemStack stackInSlot, int stackLimit)
	{
		return output == null || stackInSlot == null ? true :
			output.similar(stackInSlot) && stackInSlot.stackSize + output.size(stackInSlot) <= Math.min(stackLimit, stackInSlot.getMaxStackSize());
	}
	
	public static void insertStack(AbstractStack stack, IBasicInventory inventory, int idx)
	{
		if(inventory.getStack(idx) == null)
		{
			inventory.setInventorySlotContents(idx, stack.instance());
		}
		else
		{
			ItemStack stack2 = inventory.getStack(idx);
			stack2.stackSize += stack.size(stack2);
		}
	}
	
	public static void insertStack(AbstractStack stack, IInventory inventory, int idx)
	{
		if(inventory.getStackInSlot(idx) == null)
		{
			inventory.setInventorySlotContents(idx, stack.instance());
		}
		else
		{
			ItemStack stack2 = inventory.getStackInSlot(idx);
			stack2.stackSize += stack.size(stack2);
		}
	}
	
	public static int tryFlowFluidInto(IFluidTank tank, @Nullable TileEntity tile, Direction source, int amount, boolean process)
	{
		if(tile == null) return -1;
		FluidStack fill = tank.drain(amount, false);
		if(fill == null) return -1;
		if(tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, source.of()))
		{
			IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, source.of());
			
			if(handler.fill(fill, false) != 0)
			{
				amount = handler.fill(fill, process);
				if(process && amount > 0)
				{
					tank.drain(amount, true);
				}
				return amount;
			}
		}
		return -1;
	}
	
	public static <T extends TileEntity & IInventory> void dropItemStacks(T tile)
	{
		dropItemStacks(tile.getWorld(), tile.getPos(), tile);
	}
	
	public static void dropItemStacks(World world, BlockPos pos, IInventory inventory)
	{
		List<ItemStack> list = ArrayListAddWithCheck.requireNonnull();
		for (int i = 0; i < inventory.getSizeInventory(); list.add(inventory.removeStackFromSlot(i)), ++i);
		Worlds.spawnDropsInWorld(world, pos, list);
	}
	
	public static void dropItemStacks(World world, BlockPos pos, IBasicInventory inventory)
	{
		List<ItemStack> list = new ArrayList<>();
		for (int i = 0; i < inventory.getSizeInventory(); list.add(inventory.removeStackFromSlot(i)), ++i);
		Worlds.spawnDropsInWorld(world, pos, list);
	}
	
	public static void damageTool(IBasicInventory inventory, int index, float amount,
			EntityPlayer user, EnumToolType type)
	{
		ItemStack stack = inventory.getStack(index);
		if (stack == null) return;
		ItemStacks.damageTool(stack, amount, user, type);
		if (stack.stackSize <= 0)
		{
			inventory.setInventorySlotContents(index, null);
			if (user != null)
			{
				user.renderBrokenItemStack(stack);
			}
		}
	}
}