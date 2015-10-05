package fle.api.soild;

public interface ISolidTanks
{
	int getSizeSolidTank();
	
	SolidTank getSolidTank(int index);
	
	SolidStack getSolidStackInTank(int index);
	
	void setSolidStackInTank(int index, SolidStack aStack);
	
	SolidStack drainSolidTank(int index, int maxDrain, boolean doDrain);
	
	int fillSolidTank(int index, SolidStack resource, boolean doFill);
}
