/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author ueyudiud
 *
 * @param <T> The tile entity class.
 */
public interface ITileEntityProperties<T>
{
	float getPlayerRelativeBlockHardness(T tile, IBlockState state, EntityPlayer player);
	
	float getBlockHardness(T tile, IBlockState state);
	
	float getExplosionResistance(T tile, Entity exploder, Explosion explosion);
	
	AxisAlignedBB getBoundBox(T tile, IBlockState state);
	
	AxisAlignedBB getCollisionBoundingBox(T tile, IBlockState state);
	
	void addCollisionBoxToList(T tile, IBlockState state, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity);
	
	@SideOnly(Side.CLIENT)
	AxisAlignedBB getSelectedBoundingBox(IBlockState state);
}
