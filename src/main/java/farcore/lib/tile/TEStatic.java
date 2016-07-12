package farcore.lib.tile;

import net.minecraft.entity.player.EntityPlayer;

public class TEStatic extends TESynchronization
{
	private boolean shouldUpdate = false;
	
	public TEStatic()
	{
		
	}
	
	@Override
	public void syncToAll()
	{
		super.syncToAll();
		shouldUpdate = true;
	}
	
	@Override
	public void syncToDim()
	{
		super.syncToDim();
		shouldUpdate = true;
	}
	
	@Override
	public void syncToNearby()
	{
		super.syncToNearby();
		shouldUpdate = true;
	}
	
	@Override
	public void syncToPlayer(EntityPlayer player)
	{
		super.syncToPlayer(player);
		shouldUpdate = true;
	}
	
	@Override
	protected void updateServer()
	{
		shouldUpdate = false;
	}
	
	@Override
	protected void updateClient()
	{
		shouldUpdate = false;
	}
	
	@Override
	public void markDirty()
	{
		shouldUpdate = true;
		super.markDirty();
	}
	
	@Override
	public boolean canUpdate()
	{
		return !isInitialized() || shouldUpdate;
	}
}