package farcore.lib.bio;

public interface IDNADecoder<T extends IBiology>
{
	void decodeDNA(T biology, String dna);
	
	String makeNativeDNA();
	
	String makeChildDNA(int generation, String par);
	
	String makeOffspringDNA(String par1, String par2);
}