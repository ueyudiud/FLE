package fargen.core.worldgen.v;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class FarVoidTeleporter extends Teleporter
{
	public FarVoidTeleporter(WorldServer worldIn)
	{
		super(worldIn);
	}

	@Override
	public boolean makePortal(Entity entityIn)
	{
		return true;
	}
	
	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw)
	{
		placeInExistingPortal(entityIn, rotationYaw);
	}
	
	@Override
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
	{
		entityIn.setLocationAndAngles(0, 128, 0, rotationYaw, entityIn.rotationPitch);
		return true;
	}
	
	@Override
	public void removeStalePortalLocations(long worldTime)
	{
	}
}