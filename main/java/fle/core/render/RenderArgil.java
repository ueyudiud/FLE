package fle.core.render;

import net.minecraft.client.renderer.entity.RenderItem;

public class RenderArgil extends RenderBase
{
	double d1 = 1D / 16D,
			d2 = d1 * 2,
			d3 = d1 * 3,
			d4 = d1 * 4,
			d5 = d1 * 5,
			d6 = d1 * 6,
			d7 = d1 * 7,
			d9 = d1 * 9,
			d13 = d1 * 13,
			d14 = d1 * 14;
	
	@Override
	public void renderBlock() 
	{
		init();
		setTexture(block);
		
		switch(meta)
		{
		case 0 :
		{
			renderBlock(d5, 0D, d5, 1 - d5, d1, 1 - d5);
			
			renderBlock(d4,     d1, d5    , d5,     d3, 1 - d5);
			renderBlock(d5,     d1, d4    , 1 - d5, d3, d5    );
			renderBlock(1 - d5, d1, d5    , 1 - d4, d3, 1 - d5);
			renderBlock(d5,     d1, 1 - d5, 1 - d5, d3, 1 - d4);

			renderBlock(d3,     d3, d4    , d4,     d7, 1 - d4);
			renderBlock(d4,     d3, d3    , 1 - d4, d7, d4    );
			renderBlock(1 - d4, d3, d4    , 1 - d3, d7, 1 - d4);
			renderBlock(d4,     d3, 1 - d4, 1 - d4, d7, 1 - d3);

			renderBlock(d4,     d3, d4    , d5,     d7, d5    );
			renderBlock(1 - d5, d3, d4    , 1 - d4, d7, d5    );
			renderBlock(1 - d5, d3, 1 - d5, 1 - d4, d7, 1 - d4);
			renderBlock(d4,     d3, 1 - d5, d5    , d7, 1 - d4);

			renderBlock(d4,     d7, d5    , d5,     d9, 1 - d5);
			renderBlock(d5,     d7, d4    , 1 - d5, d9, d5    );
			renderBlock(1 - d5, d7, d5    , 1 - d4, d9, 1 - d5);
			renderBlock(d5,     d7, 1 - d5, 1 - d5, d9, 1 - d4);

			renderBlock(d5,     d9, d6    , d6,     d13, 1 - d6);
			renderBlock(d6,     d9, d5    , 1 - d6, d13, d6    );
			renderBlock(1 - d6, d9, d6    , 1 - d5, d13, 1 - d6);
			renderBlock(d6,     d9, 1 - d6, 1 - d6, d13, 1 - d5);

			renderBlock(d5,     d13, d5    , d6,     d14, 1 - d5);
			renderBlock(d5,     d13, d5    , 1 - d5, d14, d6    );
			renderBlock(1 - d6, d13, d5    , 1 - d5, d14, 1 - d5);
			renderBlock(d5,     d13, 1 - d6, 1 - d5, d14, 1 - d5);
		}
		}
	}
}