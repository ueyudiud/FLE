package farcore.interfaces;

public interface INoise
{	
	double[] noise(int x, int y, int z, int w, int h, int l);
		
	double[] noise(int x, int z, int w, int l);
	
	long seed();
}