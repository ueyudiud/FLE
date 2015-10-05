package fle.core.world.layer;

import net.minecraft.world.gen.layer.GenLayer;

@Deprecated
public class FLELayerZoom extends FLELayer
{
	public FLELayerZoom(long seed, GenLayer layer)
	{
		super(seed);
		parent = layer;
	}

	@Override
	public int[] getInts(int x, int z, int w, int h)
	{
		int x1 = x >> 1;
	    int z1 = z >> 1;
	    int w1 = (w >> 1) + 2;
	    int h1 = (h >> 1) + 2;
	    int[] parentCache = parent.getInts(x1, z1, w1, h1);
	    int i2 = w1 - 1 << 1;
	    int j2 = h1 - 1 << 1;
	    int[] out = new int[i2 * j2];
	    for (int loopZ = 0; loopZ < h1 - 1; loopZ++)
	    {
	    	int l2 = (loopZ << 1) * i2;
		    int loopX = 0;
		    int thisID = parentCache[(loopX + 0 + (loopZ + 0) * w1)];
		    for (int i3 = parentCache[(loopX + 0 + (loopZ + 1) * w1)]; loopX < w1 - 1; loopX++)
		    {
		        initChunkSeed(loopX + x1 << 1, loopZ + z1 << 1);
		        int rightID = parentCache[(loopX + 1 + (loopZ + 0) * w1)];
		        int upRightID = parentCache[(loopX + 1 + (loopZ + 1) * w1)];
		        out[l2] = thisID;
		        out[(l2++ + i2)] = choose(thisID, i3);
		        out[l2] = choose(thisID, rightID);
		        out[(l2++ + i2)] = selectModeOrRandom(thisID, rightID, i3, upRightID);
		        thisID = rightID;
		        i3 = upRightID;
		    }
		}
	    int[] outCache = new int[w * h];
	    for (int zoom = 0; zoom < h; zoom++)
	    {
	    	int srcPos = (zoom + (z & 0x1)) * i2 + (x & 0x1);
	    	System.arraycopy(out, srcPos, outCache, zoom * w, w);
	    }
	    return outCache;
	}

    /**
     * returns the most frequently occurring number of the set, or a random number from those provided
     */
    protected int selectModeOrRandom(int id0, int id1, int id2, int id3)
    {
    	int i = 0;
    	if(id0 == id1) ++i;
    	if(id0 == id2) ++i;
    	if(id0 == id3) ++i;
    	if(i > 2) return id0;
    	if(i == 2)
    	{
    		i = 0;
    		if(id1 != id2) ++i;
    		if(id2 != id3) ++i;
    		if(id3 != id1) ++i;
    		if(i > 2) return id0;
    	}
    	else
    	{
    		i = 0;
    		if(id1 == id2) ++i;
    		if(id1 == id3) ++i;
    		if(i > 1) return id1;
    		if(i == 1)
    		{
    			i = 0;
    			if(id2 != id3 && id3 != id0 && id0 != id2) return id1;
    		}
    		else
    		{
    			if(id2 == id3) return id2;
    		}
    	}
    	return choose(id0, id1, id2, id3);
    }

    /**
     * Magnify a layer. Parms are seed adjustment, layer, number of times to magnify
     */
    public static GenLayer magnify(long aSeed, GenLayer layer, int aTime)
    {
        GenLayer gl = layer;

        for (int k = 0; k < aTime; ++k)
        {
            layer = new FLELayerZoom(aSeed + (long)k, gl);
        }

        return gl;
    }
}