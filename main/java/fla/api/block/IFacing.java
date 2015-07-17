package fla.api.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.world.BlockPos;

public interface IFacing 
{
	public ForgeDirection getBlockDirection(BlockPos pos);
	
	public boolean canSetDirection(World world, BlockPos pos);

	public boolean canSetDirectionWith(World world, BlockPos pos, double xPos, double yPos, double zPos, ItemStack itemstack);
	
	public ForgeDirection setDirectionWith(World world, BlockPos pos, double xPos, double yPos, double zPos, ItemStack itemstack);

	public boolean hasTopOrDownState(World world, BlockPos pos);
}
