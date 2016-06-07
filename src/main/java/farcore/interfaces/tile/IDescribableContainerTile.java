package farcore.interfaces.tile;

public interface IDescribableContainerTile extends IDescribableTile
{
	int[] getAllContainDescriptions();
	
	int getContainerDescriptionsLength();
	
	int getContainerDescriptions(int id);
	
	void setContainerDescriptions(int id, int value);
	
	void setContainerAllDescriptions(int[] value);
}
