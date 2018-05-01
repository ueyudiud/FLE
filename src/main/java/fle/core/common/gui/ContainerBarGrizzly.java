/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.common.gui;

import farcore.data.EnumToolTypes;
import nebula.common.gui.Container03BlockPos;
import nebula.common.gui.InvDataWatcher;
import nebula.common.gui.ItemSlot;
import nebula.common.util.TileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class ContainerBarGrizzly extends Container03BlockPos implements ITickable
{
	private InventoryBarGrizzly inventory	= new InventoryBarGrizzly(this);
	
	public ContainerBarGrizzly(EntityPlayer player, World world, BlockPos pos)
	{
		super(player, world, pos);
		this.invDataWatcher = new InvDataWatcher(this.inventory);
		TransferLocation locationInput = createLocation(1);
		addSlotToContainer(new ItemSlot(this.inventory, 0, 40, 16));
		TransferLocation locationTool = createLocation(1);
		addSlotToContainer(new ItemSlot(this.inventory, 1, 58, 16).setPredicate(EnumToolTypes.BAR_GRIZZLY::toolMatch));
		TransferLocation locationOutput = createLocation(9);
		addOutputSlots(this.inventory.containers.getContainers(), this.inventory, 3, 3, 3, 97, 16, 18, 18);
		this.tsPlayerHand.addLocation(locationTool).addLocation(locationInput).addLocation(this.locationBag);
		this.tsPlayerBag.addLocation(locationTool).addLocation(locationInput).addLocation(this.locationHand);
		this.stragtegies.add(new TS(locationInput).addLocation(this.locationPlayer));
		this.stragtegies.add(new TS(locationOutput).addLocation(this.locationPlayer));
		this.stragtegies.add(new TS(locationTool).addLocation(this.locationPlayer));
	}
	
	@Override
	protected boolean match(IBlockState oldState, IBlockState newstate)
	{
		return oldState.getBlock() == newstate.getBlock();
	}
	
	@Override
	public void update()
	{
		this.inventory.update();
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		TileEntities.dropItemStacks(this.world, this.pos, this.inventory);
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		switch (type)
		{
		case 0:
			if (value == 0)
			{
				this.inventory.power = Math.min(this.inventory.power + 40, 400);
			}
			break;
		default:
			break;
		}
	}
	
	public int getProgress()
	{
		return this.inventory.progress;
	}
	
	public int getPower()
	{
		return this.inventory.power;
	}
	
	public int getMaxProgress()
	{
		return this.inventory.maxProgress;
	}
}
