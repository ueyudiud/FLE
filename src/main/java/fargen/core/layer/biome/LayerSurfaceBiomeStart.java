/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.biome;

import fargen.core.layer.Layer;
import nebula.common.util.L;

/**
 * @author ueyudiud
 */
public class LayerSurfaceBiomeStart extends Layer
{
	static final int DETERMINE_OPERATOR = 0x100;
	static final int[] TEMP_ARRAY = {
			7,								//1
			7,								//2
			7 | 6 << 4 | DETERMINE_OPERATOR,//3
			6,								//4
			6,								//5
			6 | 5 << 4 | DETERMINE_OPERATOR,//6
			5,								//7
			5,								//8
			4,								//9
			4,								//10
			3,								//11
			3,								//12
			2,								//13
			1,								//14
			0,								//15
			0,								//16
			1,								//17
			2,								//18
			3,								//19
			3,								//20
			4,								//21
			4,								//22
			5,								//23
			5,								//24
			6,								//25
			6,								//26
			6 | 5 << 4 | DETERMINE_OPERATOR,//27
			6,								//28
			6,								//29
			7 | 6 << 4 | DETERMINE_OPERATOR,//30
			7,								//31
			7,								//32
	};
	
	public LayerSurfaceBiomeStart(long seed)
	{
		super(seed);
	}
	
	@Override
	public int[] getInts(int x, int y, int w, int h)
	{
		int[] result = array(w, h);
		for (int i = 0; i < h; ++i)
			for (int j = 0; j < w; ++j)
			{
				initChunkSeed(x + j, y + i);
				int temp = TEMP_ARRAY[(y + i +
						4//The generation offset.
						) & 0x1F];
				if ((temp & DETERMINE_OPERATOR) != 0)
				{
					temp = (nextBoolean() ? temp >> 4 : temp) & 0xF;
				}
				temp = L.range(0, 7, temp + next());
				result[i * w + j] = temp | nextInt(16) << 8;
			}
		return result;
	}
	
	protected int next()
	{
		switch (nextInt(9))
		{
		case 0 : return  1;
		case 1 : return -1;
		default: return  0;
		}
	}
}
