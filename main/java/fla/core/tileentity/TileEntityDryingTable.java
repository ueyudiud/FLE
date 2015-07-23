package fla.core.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.recipe.ErrorType;
import fla.api.world.BlockPos;
import fla.core.gui.InventoryDryingTable;
import fla.core.tileentity.base.TileEntityInventory;
import fla.core.world.FWM;

public class TileEntityDryingTable extends TileEntityInventory<InventoryDryingTable>
{
	private int levelCheckBuffer;
	private int colour = 0xFFFFFF;
	private double tem;
	private double water;
	public ErrorType type;
	
	public TileEntityDryingTable() 
	{
		super(new InventoryDryingTable());
	}
	
	public double getTempretureLevel()
	{
		return tem == 0D ? FWM.getTempretureLevel(worldObj, xCoord, yCoord, zCoord) : tem;
	}
	
	public double getWaterLevel() 
	{
		return water == 0D ? FWM.getWaterLevel(worldObj, xCoord, yCoord, zCoord) : water;
	}
	
	@Override
	public boolean canSetDirection(World world, BlockPos pos)
	{
		return false;
	}

	@Override
	public boolean canSetDirectionWith(World world, BlockPos pos, double xPos,
			double yPos, double zPos, ItemStack itemstack) 
	{
		return false;
	}

	@Override
	public ForgeDirection setDirectionWith(World world, BlockPos pos,
			double xPos, double yPos, double zPos, ItemStack itemstack) 
	{
		return null;
	}
	
	@Override
	public void updateEntity() 
	{	
		inv.updateEntity(this);
		++levelCheckBuffer;
		if(levelCheckBuffer >= 200)
		{
			levelCheckBuffer = 0;
			tem = FWM.getTempretureLevel(worldObj, xCoord, yCoord, zCoord);
			water = FWM.getWaterLevel(worldObj, xCoord, yCoord, zCoord);
		}
	}

	@SideOnly(Side.CLIENT)
	public int getRecipeProgressBar(int i) 
	{
		return inv.getProgressBar(i);
	}

	public void setBlockColour(int colour) 
	{
		this.colour = colour;
	}
	
	public int getBlockColour()
	{
		return colour;
	}
}