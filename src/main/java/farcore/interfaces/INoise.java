package farcore.interfaces;

public interface INoise
{	
	double[] generate(int x, int y, int z, int w, int h, int l);
		
	double[] generate(int x, int z, int w, int l);
	
	long seed();
}