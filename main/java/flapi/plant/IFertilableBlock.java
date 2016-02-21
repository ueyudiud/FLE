package flapi.plant;

import net.minecraft.world.World;
import flapi.world.BlockPos;

public interface IFertilableBlock
{
	boolean needFertilize(BlockPos pos, FertitleLevel lv);
	
	boolean isFertile(World world, int x, int y, int z);
	
	void doFertilize(World world, int x, int y, int z, FertitleLevel lv);
	
	boolean useFertilizer(World world, int x, int y, int z, FertitleLevel lv);
	
	FertitleLevel getFertileLevel(World world, int x, int y, int z);
	
	int getWaterLevel(World world, int x, int y, int z);
	
	public static class FertitleLevel
	{
		public int N;
		public int P;
		public int K;
		public int Ca;

		public FertitleLevel(short aNPKCa)
		{
			this((aNPKCa & 0xF000) >> 12, (aNPKCa & 0x0F00) >> 8, (aNPKCa & 0x00F0) >> 4, aNPKCa & 0x000F);
		}
		public FertitleLevel(int aN, int aP, int aK)
		{
			this(aN, aP, aK, 8);
		}
		public FertitleLevel(int aN, int aP, int aK, int aCa)
		{
			N = aN;
			P = aP;
			K = aK;
			Ca = aCa;
		}
		
		public boolean contain(FertitleLevel fLevel)
		{
			return contain(fLevel.N, fLevel.P, fLevel.K, fLevel.Ca);
		}

		public boolean contain(int aN, int aP, int aK, int aCa)
		{
			return N >= aN && P >= aP && K >= aK && Ca >= aCa;
		}

		public void add(FertitleLevel fLevel)
		{
			add(fLevel.N, fLevel.P, fLevel.K, fLevel.Ca);
		}
		
		public void add(int aN, int aP, int aK, int aCa)
		{
			N = Math.max(0, Math.min(15, N + aN));
			P = Math.max(0, Math.min(15, P + aP));
			K = Math.max(0, Math.min(15, K + aK));
			Ca= Math.max(0, Math.min(15, Ca + aCa));
		}
		
		public boolean need(FertitleLevel fLevel)
		{
			if(N < 15 && fLevel.N > 0) return true;
			else if(K < 15 && fLevel.K > 0) return true;
			else if(P < 15 && fLevel.P > 0) return true;
			else if(Ca < 15 && fLevel.Ca > 0) return true;
			return false;
		}
		
		@Override
		public String toString()
		{
			return String.format("Fertitle Level$%d, %d, %d, %d", N, P, K, Ca);
		}
		
		@Override
		public int hashCode()
		{
			return (N << 12) + (P << 8) + (K << 4) + Ca;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof FertitleLevel ? ((FertitleLevel)obj).N == N &&
					((FertitleLevel)obj).P == P && ((FertitleLevel)obj).K == K && ((FertitleLevel)obj).Ca == Ca : false;
		}
	}
}