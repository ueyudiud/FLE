package flapi.chem.base;

public interface IMatterInputHatch
{
	int getMatterHatchSize();
	
	MatterStack getMatter(int idx);
	
	MatterStack decrMatter(int idx, int size);
	
	void setMatter(int idx, MatterStack stack);
}