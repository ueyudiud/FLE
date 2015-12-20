package fle.core.util.noise;

@SuppressWarnings("unused")
public class NoiseCell extends NoiseBase
{
	public NoiseCell(long aSeed)
	{
		super(aSeed);
	}

	@Override
	public double noise(long x, long z)
	{
		return noise(x, 10, z);
	}

	@Override
	public double noise(long x, long y, long z)
	{
		return noise((int) x, (int) y, (int) z, 4.0D);
	}
	
	public float border2(double x, double z, double width, float depth)
	{
		x *= 1D;
		z *= 1D;

		int xInt = (x > .0? (int)x: (int)x - 1);
		int zInt = (z > .0? (int)z: (int)z - 1);
		
		double dCandidate = 32000000.0;
		double xCandidate = 0;
		double zCandidate = 0;
		
		double dNeighbour = 32000000.0;
		double xNeighbour = 0;
		double zNeighbour = 0;
		
		double xPos, zPos, xDist, zDist, dist;
		for(int zCur = zInt - 2; zCur <= zInt + 2; zCur++) 
		{
			for(int xCur = xInt - 2; xCur <= xInt + 2; xCur++) 
			{
				xPos = xCur + valueNoise2D(xCur, zCur, seed);
				zPos = zCur + valueNoise2D(xCur, zCur, next(xCur, 0, zCur));
				xDist = xPos - x;
				zDist = zPos - z;
				dist = distance(xPos - x, zPos - z);
				
				if(dist < dCandidate) 
				{
					dNeighbour = dCandidate;
					xNeighbour = xCandidate;
					zNeighbour = zCandidate;
					
					dCandidate = dist;
					xCandidate = xPos;
					zCandidate = zPos;
				}
				else if(dist > dCandidate && dist < dNeighbour)
				{
					dNeighbour = dist;
					xNeighbour = xPos;
					zNeighbour = zPos;
				}
			}
		}
		
		double diff = distance(xCandidate - xNeighbour, zCandidate - zNeighbour);
		double total = (dCandidate + dNeighbour) / diff;
		
		dCandidate = dCandidate / total;
		dNeighbour = dNeighbour / total;
		
		double c = (diff / 2D) - dCandidate;
		if(c < width)
		{
			return (((float)(c / width)) - 1f) * depth;
		}
		else
		{
			return 0f;
		}
	}
	
	public float border(double x, double z, double width, float depth) 
	{
		x *= 1D;
		z *= 1D;

		int xInt = (x > .0? (int)x: (int)x - 1);
		int zInt = (z > .0? (int)z: (int)z - 1);

		double dCandidate = 32000000.0;
		double xCandidate = 0;
		double zCandidate = 0;
		
		double dNeighbour = 32000000.0;
		double xNeighbour = 0;
		double zNeighbour = 0;

		for(int zCur = zInt - 2; zCur <= zInt + 2; zCur++) 
		{
			for(int xCur = xInt - 2; xCur <= xInt + 2; xCur++) 
			{
				double xPos = xCur + valueNoise2D(xCur, zCur, seed);
				double zPos = zCur + valueNoise2D(xCur, zCur, next(xCur, 0, zCur));
				double xDist = xPos - x;
				double zDist = zPos - z;
				//double dist = xDist * xDist + zDist * zDist;
				double dist = distance(xPos - x, zPos - z);
				
				if(dist < dCandidate) 
				{
					dNeighbour = dCandidate;
					dCandidate = dist;
					
					/*dNeighbour = dCandidate;
					xNeighbour = xCandidate;
					zNeighbour = zCandidate;
					
					dCandidate = dist;
					xCandidate = xPos;
					zCandidate = zPos;*/
				}
				else if(dist < dNeighbour)
				{
					dNeighbour = dist;
				}
			}
		}
		
		//double c = getDistance2D(xNeighbour - x, zNeighbour - z) - getDistance2D(xCandidate - x, zCandidate - z);
		double c = dNeighbour - dCandidate;
		if(c < width)
		{
			return (((float)(c / width)) - 1f) * depth;
		}
		else
		{
			return 0f;
		}
	}
	
	public double noise(double x, double y, double z, double frequency) 
	{
		// Inside each unit cube, there is a seed point at a random position.  Go
		// through each of the nearby cubes until we find a cube with a seed point
		// that is closest to the specified position.
		x *= frequency;
		y *= frequency;
		z *= frequency;

		int xInt = (x > .0 ? (int)x : (int)x - 1);
		int yInt = (y > .0 ? (int)y : (int)y - 1);
		int zInt = (z > .0 ? (int)z : (int)z - 1);

		double minDist = 32000000.0;

		double xCandidate = 0;
		double yCandidate = 0;
		double zCandidate = 0;

		for(int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
			for(int yCur = yInt - 2; yCur <= yInt + 2; yCur++) {
				for(int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {
					// Calculate the position and distance to the seed point inside of
					// this unit cube.

					double xPos = xCur + valueNoise3D (xCur, yCur, zCur, seed);
					double yPos = yCur + valueNoise3D (xCur, yCur, zCur, next(xCur, yCur, zCur));
					double zPos = zCur + valueNoise3D (xCur, yCur, zCur, next(xCur, yCur + 5, zCur));
					double xDist = xPos - x;
					double yDist = yPos - y;
					double zDist = zPos - z;
					double dist = xDist * xDist + yDist * yDist + zDist * zDist;

					if(dist < minDist) {
						// This seed point is closer to any others found so far, so record
						// this seed point.
						minDist = dist;
						xCandidate = xPos;
						yCandidate = yPos;
						zCandidate = zPos;
					}
				}
			}
		}

		double xDist = xCandidate - x;
		double yDist = yCandidate - y;
		double zDist = zCandidate - z;
		
		return distance(xDist, yDist, zDist);
	}
	
	/**
	 * To avoid having to store the feature points, we use a hash function 
	 * of the coordinates and the seed instead. Those big scary numbers are
	 * arbitrary primes.
	 */
	private static double valueNoise2D (int x, int z, long seed) 
	{
		long n = (1619 * x + 6971 * z + 1013 * seed) & 0x7fffffff;
		n = (n >> 13) ^ n;
		return 1.0 - ((double)((n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0);
	}

	private static double valueNoise3D (int x, int y, int z, long seed) 
	{
		long n = (1619 * x + 31337 * y + 6971 * z + 1013 * seed) & 0x7fffffff;
		n = (n >> 13) ^ n;
		return 1.0 - ((double)((n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0);
	}

	private long next(long x, long y, long z)
	{
		long ret = (x * 38591L + y) ^ (y * 37501L + z) ^ (z * 17419L + x) ^ seed;
	    return (ret * (ret * (ret * 15731 + seed) + 782949221L) + 76312589L) & 0x7FFFFFFF;   
	}
}