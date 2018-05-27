/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.entity.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class PathNavigateClimberExt extends PathNavigateClimber
{
	public PathNavigateClimberExt(EntityLiving entityLivingIn, World worldIn)
	{
		super(entityLivingIn, worldIn);
	}
	
	@Override
	protected PathFinder getPathFinder()
	{
		this.nodeProcessor = new NodeProcessorBlockBreakable();
		// this.nodeProcessor.setCanEnterDoors(true);
		return new PathFinder(this.nodeProcessor);
		// return super.getPathFinder();
	}
}
