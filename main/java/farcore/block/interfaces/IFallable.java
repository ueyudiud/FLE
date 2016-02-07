package farcore.block.interfaces;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.BlockFactory;
import farcore.entity.EntityFleFallingBlock;
import farcore.world.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Implements on block which can fall down (eg: sand).<br>
 * Use block factory to make block fall down.
 * Here is a example:<br>
 * 
 * <p><pre>
 * private void checkFallable(World world, int x, int y, int z)
 * {
 *     if (canFallInto(new BlockPos(world, x, y - 1, z)) && y >= 0)
 *     {
 *         BlockFactory.makeFallingBlock(world, new BlockPos(world, x, y, z), this);
 *     }
 * }
 * 	
 * public boolean canFallInto(BlockPos pos)
 * {
 *    if (pos.isAir())
 *       return true;
 *    Block block = pos.block();
 *    Material material = block.getMaterial();
 *    return block == Blocks.fire || material == Material.air
 *        || material == Material.water || material == Material.lava;
 *  }
 *  </pre></p>
 * @author ueyudiud
 * @see farcore.block.BlockFactory
 * @see farcore.entity.EntityFleFallingBlock
 */
public interface IFallable
{
    void onStartFalling(EntityFleFallingBlock entity);

	void onHitEntity(EntityFleFallingBlock entity);

	@SideOnly(Side.CLIENT)
    IIcon getFallingIcon(EntityFleFallingBlock entity, int side);
    
	List<ItemStack> onBlockDropAsItem(EntityFleFallingBlock entity);

    boolean canFallInto(BlockPos pos);
    
    void onEndFalling(BlockPos pos);
}