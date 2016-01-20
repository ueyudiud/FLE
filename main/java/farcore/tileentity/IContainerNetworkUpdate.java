package farcore.tileentity;

public interface IContainerNetworkUpdate
{
	int getProgressSize();

	int getProgress(int id);

	void setProgress(int id, int value);	
}