/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.layer.biome;

import static farcore.data.M.aterobrunnoloam;
import static farcore.data.M.aterocalcosol;
import static farcore.data.M.aterosol;
import static farcore.data.M.brunnocalcosol;
import static farcore.data.M.brunnodesertosol;
import static farcore.data.M.brunnoloam;
import static farcore.data.M.cinerodesertosol;
import static farcore.data.M.flavobrunnoloam;
import static farcore.data.M.flavoloam;
import static farcore.data.M.latroaluminosol;
import static farcore.data.M.latrosol;
import static farcore.data.M.moraine;
import static farcore.data.M.palosol;
import static farcore.data.M.peatsol;
import static farcore.data.M.pheosol;
import static farcore.data.M.podzol;
import static farcore.data.M.redsand;
import static farcore.data.M.ruboaluminoloam;
import static farcore.data.M.ruboloam;
import static farcore.data.M.sand;
import static farcore.data.M.spaticocalcosol;
import static farcore.data.M.tundrosol;
import static fargen.core.worldgen.surface.FarSurfaceDataGenerator.getSoilLayerIDByName;

import fargen.core.layer.abstracts.LayerReplace;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceSoilStart extends LayerReplace
{
	private static final int
	id_latrosol = getSoilLayerIDByName(latrosol.name),
	id_latroaluminosol = getSoilLayerIDByName(latroaluminosol.name),
	id_ruboloam = getSoilLayerIDByName(ruboloam.name),
	id_ruboaluminoloam = getSoilLayerIDByName(ruboaluminoloam.name),
	id_flavoloam = getSoilLayerIDByName(flavoloam.name),
	id_peatsol = getSoilLayerIDByName(peatsol.name),
	id_aterosol = getSoilLayerIDByName(aterosol.name),
	id_podzol = getSoilLayerIDByName(podzol.name),
	id_pheosol = getSoilLayerIDByName(pheosol.name),
	id_aterocalcosol = getSoilLayerIDByName(aterocalcosol.name),
	id_brunnocalcosol = getSoilLayerIDByName(brunnocalcosol.name),
	id_brunnodesertosol = getSoilLayerIDByName(brunnodesertosol.name),
	id_cinerodesertosol = getSoilLayerIDByName(cinerodesertosol.name),
	id_flavobrunnoloam = getSoilLayerIDByName(flavobrunnoloam.name),
	id_brunnoloam = getSoilLayerIDByName(brunnoloam.name),
	id_tundrosol = getSoilLayerIDByName(tundrosol.name),
	id_moraine = getSoilLayerIDByName(moraine.name),
	id_aterobrunnoloam = getSoilLayerIDByName(aterobrunnoloam.name),
	id_spaticocalcosol = getSoilLayerIDByName(spaticocalcosol.name),
	id_palosol = getSoilLayerIDByName(palosol.name),
	id_sand = getSoilLayerIDByName(sand.name),
	id_redsand = getSoilLayerIDByName(redsand.name),
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
				rain < 12 ? id_tundrosol :
					id_moraine;
		case 1 :
			return rain < 4 ? id_unknown :
				rain < 8 ? id_palosol :
					rain < 15 ? id_podzol :
						id_moraine;
		case 2 :
			return rain < 4 ? id_unknown :
				rain < 7 ? id_unknown :
					rain < 8 ? id_aterocalcosol :
						rain < 10 ? id_aterobrunnoloam :
							rain < 12 ? id_podzol :
								id_aterosol;
		case 3 :
			return rain < 1 ? id_brunnodesertosol :
				rain < 3 ? id_cinerodesertosol :
					rain < 4 ? id_brunnocalcosol :
						rain < 6 ? id_spaticocalcosol :
							rain < 7 ? id_aterocalcosol :
								rain < 10 ? id_flavobrunnoloam :
									rain < 11 ? id_brunnoloam :
										id_aterosol;
		case 4 :
			return rain < 1 ? id_brunnodesertosol :
				rain < 3 ? id_cinerodesertosol :
					rain < 5 ? id_brunnocalcosol :
						rain < 6 ? id_spaticocalcosol :
							rain < 8 ? id_pheosol :
								rain < 11 ? id_flavobrunnoloam :
									id_aterosol;
		case 5 :
			return rain < 1 ? id_brunnodesertosol :
				rain < 3 ? id_cinerodesertosol :
					rain < 5 ? id_brunnocalcosol :
						rain < 6 ? id_spaticocalcosol :
							rain < 9 ? id_flavoloam :
								rain < 12 ? id_ruboloam :
									rain < 14 ? id_peatsol :
										id_latroaluminosol;
		case 6 :
			return rain < 2 ? id_sand :
				rain < 6 ? id_ruboaluminoloam :
					rain < 9 ? id_flavoloam :
						rain < 12 ? id_ruboloam :
							rain < 14 ? id_peatsol :
								id_latrosol;
		case 7 :
			return rain < 3 ? id_sand :
				rain < 10 ? id_ruboaluminoloam :
					rain < 13 ? id_latroaluminosol :
						id_latrosol;
		default:
			return -1;
		}
	}
}