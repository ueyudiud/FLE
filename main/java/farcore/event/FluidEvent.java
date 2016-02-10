package farcore.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

public abstract class FluidEvent extends Event
{
	public final World world;
	public final int x, y, z;
	public final BlockFluidBase fluid;
	
	FluidEvent(World world, int x, int y, int z, BlockFluidBase fluid)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.fluid = fluid;
	}
	
	@HasResult
	public static class FluidTouchBlockEvent extends FluidEvent
	{
		public final int preAmount;
		public int amount;
		public final Block target;

		public FluidTouchBlockEvent(World world, int x, int y, int z, BlockFluidBase fluid, int amt, Block target)
		{
			super(world, x, y, z, fluid);
			preAmount = (amount = amt);
			this.target = target;
		}
	}
	
	@HasResult
	public static class FluidTouchFluidEvent extends FluidEvent
	{
		public final int preAmount;
		public int amount;
		public final BlockFluidBase target;

		public FluidTouchFluidEvent(World world, int x, int y, int z, BlockFluidBase fluid, int amt, BlockFluidBase target)
		{
			super(world, x, y, z, fluid);
			preAmount = (amount = amt);
			this.target = target;
		}
	}
}