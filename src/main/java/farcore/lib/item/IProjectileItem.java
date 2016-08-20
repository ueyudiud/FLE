package farcore.lib.item;

import farcore.lib.entity.EntityProjectileItem;
import farcore.lib.util.Direction;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IProjectileItem
{
	/**
	 * Called when projective item is initialized.
	 * @param entity
	 */
	void initEntity(EntityProjectileItem entity);
	
	/**
	 * Called when projective item is updating.
	 * @param entity
	 */
	void onEntityTick(EntityProjectileItem entity);
	
	/**
	 * Called when projective item hitting on ground.
	 * @param world
	 * @param pos
	 * @param entity
	 * @param direction
	 * @return Return true to prevent defaults drop action.
	 */
	boolean onHitGround(World world, BlockPos pos, EntityProjectileItem entity, Direction direction);
	
	/**
	 * Called when projective item hitting on another entity.
	 * Use to attack entity, give entity potion effect, etc.
	 * @param world
	 * @param target
	 * @param entity
	 * @return Return true to prevent defaults drop action.
	 */
	boolean onHitEntity(World world, Entity target, EntityProjectileItem entity);
}