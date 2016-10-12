package farcore.lib.gui;

import farcore.lib.world.ICoord;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		state = world.getBlockState(pos);
		currentValue = new int[getFieldCount()];
		lastCurrentValue = new int[getFieldCount()];
	}

	protected int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		for(int i = 0; i < currentValue.length; ++i)
		{
			listener.sendProgressBarUpdate(this, i, currentValue[i]);
		}
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0; i < currentValue.length; ++i)
		{
			if(lastCurrentValue[i] != currentValue[i])
			{
				for(IContainerListener listener : listeners)
				{
					listener.sendProgressBarUpdate(this, i, currentValue[i]);
				}
				lastCurrentValue[i] = currentValue[i];
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return match(state, world.getBlockState(pos)) && playerIn.worldObj == world && playerIn.getDistanceSq(pos.getX() + .5F, pos.getY() + .5F, pos.getZ() + .5F) <= 16;
	}
	
	protected boolean match(IBlockState oldState, IBlockState newstate)
	{
		return oldState == newstate;
	}

	@Override
	public void onInventoryChanged(InventoryBasic inventory)
	{

	}
}