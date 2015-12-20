package flapi.te.interfaces;

/**
 * Send progress from player network, different from fle network.
 * @see net.minecraft.inventory.Container
 * @see flapi.net.FleNetworkHandler
 * @see flapi.te.TEBase
 * @author ueyudiud
 *
 */
public interface IContainerNetworkUpdate
{
	int getProgressSize();
	
	int getProgress(int id);
	
	void setProgress(int id, int value);
}