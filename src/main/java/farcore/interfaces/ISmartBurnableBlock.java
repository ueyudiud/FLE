package farcore.interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface ISmartBurnableBlock
{
	public static final ISmartBurnableBlock instance = new SmartBurnableBlock();
	
	/**
	 * Called when update fire, if return false,
	 * fire will not update other things.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	boolean onBurning(World world, int x, int y, int z, int level);
	
	int canBeBurned(World world, int x, int y, int z, int level);

	boolean isFireSource(World world, int x, int y, int z, ForgeDirection face);
	
	boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face, int level);

	/**
	 * Called when block is flame.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param face
	 * @param level
	 * @return Do set a fire block on this block.
	 */
	boolean onFlame(World world, int x, int y, int z, ForgeDirection face, int level);
	
	boolean onBurned(World world, int x, int y, int z);
	
	int getFlammability(World world, int x, int y, int z, ForgeDirection face, int hardness);
	
	/**
	 * Get icon of fire, return null for use default icon.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	IIcon getFireIcon(IBlockAccess world,  int x, int y, int z);
	
	/**
	 * Get temperature when block burning, return an negative number
	 * for use default temperature settings.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param level
	 * @return
	 */
	int getBurningTemperature(World world, int x, int y, int z, int level);
	
	public static class SmartBurnableBlock implements ISmartBurnableBlock
	{
		private SmartBurnableBlock(){}
		
		@Override
		public boolean onBurning(World world, int x, int y, int z, int level)
		{
//			Block block = world.getBlock(x, y, z);
			return true;
		}
		
		@Override
		public int canBeBurned(World world, int x, int y, int z, int level)
		{
			return isFlammable(world, x, y, z, ForgeDirection.UP, level) ?
					8 : 0;
		}
		
		@Override
		public boolean isFireSource(World world, int x, int y, int z, ForgeDirection face)
		{
			Block block = world.getBlock(x, y, z);
			return block.isFireSource(world, x, y, z, face);
		}

		@Override
		public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face, int level)
		{
			Block block = world.getBlock(x, y, z);
			return block.isFlammable(world, x, y, z, face);
		}

		@Override
		public int getFlammability(World world, int x, int y, int z, ForgeDirection face, int hardness)
		{
			Block block = world.getBlock(x, y, z);
			return block.getFlammability(world, x, y, z, face);
		}
		
		@Override
		public boolean onFlame(World world, int x, int y, int z, ForgeDirection face, int level)
		{
			return true;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public IIcon getFireIcon(IBlockAccess world, int x, int y, int z)
		{
			return null;
		}

		@Override
		public int getBurningTemperature(World world, int x, int y, int z, int level)
		{
			return -1;
		}
		
		@Override
		public boolean onBurned(World world, int x, int y, int z)
		{
			return false;
		}
	}
}