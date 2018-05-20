/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import farcore.lib.bio.IFamily;
import farcore.lib.bio.TemplateOrder;

/**
 * @author ueyudiud
 */
public final class TreeOrder extends TemplateOrder<IFamily<? extends ITree>, ITree>
{
	public static final TreeOrder ORDER = new TreeOrder();
	
	TreeOrder() {}
}
