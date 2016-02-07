package farcore.fluid;

import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * More method from fluid block.<br>
 * You can choose how many fluid you will current in the type implements
 * this class.
 * @author ueyudiud
 *
 */
public interface ISmartFluidBlock extends IFluidBlock
{
    /**
     * Attempt to drain the block with a max drain order. 
     * This method should be called by container or devices such as pumps.<br>
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param maxDrain The max accept to drain amount.
     * @param doDrain If false, the drain will only be simulated.
     * @return
     */
    FluidStack drain(World world, int x, int y, int z, int maxDrain, boolean doDrain);
    
    /**
     * Fill fluid to fluid block.
     * This method should be called by container or devices such as fillers.<br>
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @param maxFill The max accept to fill amount.
     * @param doFill If false, the fill will only be simulated.
     * @return How many fill from container.
     */
    int fill(World world, int x, int y, int z, int maxFill, boolean doFill);
}