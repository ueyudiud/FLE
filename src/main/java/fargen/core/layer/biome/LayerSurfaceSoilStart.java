/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.biome;

import fargen.core.layer.abstracts.LayerReplace;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceSoilStart extends LayerReplace
{
	private static final byte
	id_latosol = 0,
	id_latoaluminosol = 1,
	id_ruboloam = 2,
	id_ruboaluminoloam = 3,
	id_flavoloam = 4,
	id_peatsol = 5,
	id_aterosol = 6,
	id_podzol = 7,
	id_pheosol = 8,
	id_aterocalcosol = 9,
	id_rubosol = 10,
	id_unknown = Byte.MAX_VALUE;
	
	public LayerSurfaceSoilStart(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int id)
	{
		return getSoil(id & 0xF, id >> 4 & 0xF, id >> 8 & 0xF);
	}
	
	protected int getSoil(int temp, int rain, int rare)
	{
		switch (temp)
		{
		case 0 :
			return rain < 4 ? id_unknown :
				rain < 12 ? id_unknown :
					id_unknown;
		case 1 :
			return rain < 4 ? id_unknown :
				rain < 8 ? id_unknown :
					rain < 15 ? id_podzol :
						id_unknown;
		case 2 :
			return rain < 4 ? id_unknown :
				rain < 7 ? id_unknown :
					rain < 8 ? id_aterocalcosol :
						rain < 10 ? id_unknown :
							rain < 12 ? id_podzol :
								id_aterosol;
		case 3 :
			return rain < 1 ? id_unknown :
				rain < 3 ? id_unknown :
					rain < 4 ? id_unknown :
						rain < 6 ? id_unknown :
							rain < 7 ? id_aterocalcosol :
								rain < 10 ? id_unknown :
									rain < 11 ? id_unknown :
										id_aterosol;
		case 4 :
			return rain < 1 ? id_unknown :
				rain < 3 ? id_unknown :
					rain < 5 ? id_unknown :
						rain < 6 ? id_unknown :
							rain < 8 ? id_unknown :
								rain < 11 ? id_unknown :
									id_aterosol;
		case 5 :
			return rain < 1 ? id_unknown :
				rain < 3 ? id_unknown :
					rain < 5 ? id_unknown :
						rain < 6 ? id_unknown :
							rain < 9 ? id_flavoloam :
								rain < 12 ? id_ruboloam :
									rain < 14 ? id_peatsol :
										id_rubosol;
		case 6 :
			return rain < 2 ? id_unknown :
				rain < 6 ? id_ruboaluminoloam :
					rain < 9 ? id_flavoloam :
						rain < 12 ? id_ruboloam :
							rain < 14 ? id_peatsol :
								id_latosol;
		case 7 :
			return rain < 3 ? id_unknown :
				rain < 10 ? id_ruboaluminoloam :
					rain < 13 ? id_rubosol :
						id_latosol;
		default:
			return -1;
		}
	}
}