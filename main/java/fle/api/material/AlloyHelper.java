package fle.api.material;

import fle.api.enums.EnumAtoms;
import fle.api.util.WeightHelper;

public interface AlloyHelper
{
	public boolean matchProportion(WeightHelper<EnumAtoms> aHelper);
}