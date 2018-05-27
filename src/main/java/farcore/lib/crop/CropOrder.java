/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import farcore.lib.bio.TemplateOrder;

/**
 * @author ueyudiud
 */
public final class CropOrder extends TemplateOrder<ICropFamily<?>, ICropSpecie>
{
	public static final CropOrder ORDER = new CropOrder();
	
	private CropOrder() { }
}
