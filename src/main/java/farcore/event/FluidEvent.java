package farcore.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

public abstract class FluidEvent extends Event
{
	public final World world;
	public final int x, y, z;
	public final BlockFluidBase fluidBlock;
	
	public FluidEvent(World world, int x, int y, int z, BlockFluidBase block)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.fluidBlock = block;
	}
	
	@HasResult
	public static class FlowVerticallyEvent extends FluidEvent
	{
		public final int inputLevel;
		public int level;
		
		public FlowVerticallyEvent(World world, int x, int y, int z, BlockFluidBase block, int level)
		{
			super(world, x, y, z, block);
			this.inputLevel = this.level = level;
		}
	}
	
	public static class FluidTouchBlockEvent extends FluidEvent
	{
		public final int inputLevel;
		public int level;
		private boolean isBlockDestroyed = false;
		
		public FluidTouchBlockEvent(World world, int x, int y, int z, BlockFluidBase block, int level)
		{
			super(world, x, y, z, block);
			this.inputLevel = this.level = level;
		}
		
		public void setBlockDestroyed()
		{
			setBlockDestroyed(true);
		}
		
		public void setBlockDestroyed(boolean dropItem)
		{
			if(!isBlockDestroyed)
			{
				if(dropItem)
				{
					world.getBlock(x, y, z).dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				}
				world.setBlockToAir(x, y, z);
	            this.isBlockDestroyed = true;
			}
		}
		
		public boolean isBlockDestroyed()
		{
			return isBlockDestroyed;
		}
	}
}