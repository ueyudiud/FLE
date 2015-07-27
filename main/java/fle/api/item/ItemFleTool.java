package fle.api.item;

import fle.api.enums.EnumDamageResource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public abstract class ItemFleTool extends ItemFleMetaBase
{
	protected ItemFleTool(String aUnlocalized, String aUnlocalizedTooltip) 
	{
		this(aUnlocalized, aUnlocalizedTooltip, true);
	}
	protected ItemFleTool(String aUnlocalized, String aUnlocalizedTooltip, boolean aFull3D) 
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		setMaxStackSize(1);
	    setNoRepair();
	    if(aFull3D) setFull3D();
	}
}