package farcore.lib.item;

import farcore.lib.entity.EntityProjectileItem;
import farcore.lib.util.Direction;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IProjectileItem
{
	/**
	 * Called when projectile item is initialized.
	 * @param entity
	 */
	void initEntity(EntityProjectileItem entity);

	void onEntityTick(EntityProjectileItem entity);

	boolean onHitGround(World world, BlockPos pos, EntityProjectileItem entity, Direction direction);

	boolean onHitEntity(World world, Entity target, EntityProjectileItem entity);
}