package nebula.common.gui;

import nebula.common.inventory.IBasicInventory;
import nebula.common.util.Worlds;
import nebula.common.world.ICoord;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The container with block position, active with block has GUI but no
 * TileEntity.
 * @author ueyudiud
 */
public class ContainerBlockPosition extends ContainerBase implements IInventoryChangedListener
{
	protected World world;
	protected BlockPos pos;
	private IBlockState state;
	private int[] lastCurrentValue;
	protected int[] currentValue;
	
	public ContainerBlockPosition(EntityPlayer player, ICoord coord)
	{
		this(player, coord.world(), coord.pos());
	}
	public ContainerBlockPosition(EntityPlayer player, World world, BlockPos pos)
	{
		super(player);
		this.world = world;
		this.pos = pos;
		//Get block state, to check if block changed to close GUI.
		this.state = world.getBlockState(pos);
		this.currentValue = new int[getFieldCount()];
		this.lastCurrentValue = new int[getFieldCount()];
	}
	
	protected int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		for(int i = 0; i < this.currentValue.length; ++i)
		{
			listener.sendProgressBarUpdate(this, i, this.currentValue[i]);
		}
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0; i < this.currentValue.length; ++i)
		{
			if(this.lastCurrentValue[i] != this.currentValue[i])
			{
				for(IContainerListener listener : this.listeners)
				{
					listener.sendProgressBarUpdate(this, i, this.currentValue[i]);
				}
				this.lastCurrentValue[i] = this.currentValue[i];
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		if (id < this.currentValue.length)
		{
			this.currentValue[id] = data;
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return match(this.state, this.world.getBlockState(this.pos)) && playerIn.world == this.world && playerIn.getDistanceSq(this.pos.getX() + .5F, this.pos.getY() + .5F, this.pos.getZ() + .5F) <= 16;
	}
	
	protected boolean match(IBlockState oldState, IBlockState newstate)
	{
		return oldState == newstate;
	}
	
	@Override
	public void onInventoryChanged(InventoryBasic inventory)
	{
		
	}
	
	protected void dropInventoryItems(IBasicInventory inventory)
	{
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = inventory.removeStackFromSlot(i);
			Worlds.spawnDropInWorld(this.world, this.pos, stack);
		}
	}
	
	protected void dropPlayerItems(IBasicInventory inventory)
	{
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = inventory.removeStackFromSlot(i);
			Worlds.spawnDropInWorld(this.opener, stack);
		}
	}
}