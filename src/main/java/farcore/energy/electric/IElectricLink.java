/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

import org.apache.commons.lang3.tuple.Pair;

/**
 * @author ueyudiud
 */
public interface IElectricLink
{
	Pair<IElectricNode, IElectricNode> owner();
}
