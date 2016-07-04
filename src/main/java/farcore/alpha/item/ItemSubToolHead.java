package farcore.alpha.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.interfaces.item.IToolProp;
import farcore.alpha.util.LangHook.UnlocalizedList;
import farcore.lib.substance.SubstanceHandle;
import farcore.lib.substance.SubstanceTool;
import fle.load.Langs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemSubToolHead extends ItemSubBehaviorII<ItemSubBehavior>
{
	ItemSubTool tool;
	
	ItemSubToolHead(ItemSubTool parent, String unlocalized)
	{
		super(unlocalized);
		this.tool = parent;
	}
	ItemSubToolHead(ItemSubTool parent, String modid, String unlocalized)
	{
		super(modid, unlocalized);
		this.tool = parent;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return ItemSubTool.getCustomDamage(stack) != 0;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (double) ItemSubTool.getCustomDamage(stack) / (double) getMaxCustomDamgae(stack);
	}
	
	protected int getMaxCustomDamgae(ItemStack stack)
	{
		return ItemSubTool.getToolMaterial(stack).maxUses;
	}
	
	/**
	 * Forced render passes to be 1, the tool
	 * head mush on first pass.
	 */
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void addUnlocalInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean F3H)
	{
		if(tool.hasTool(stack))
		{
			IToolProp prop = (IToolProp) tool.toolMap.get(Short.valueOf((short) getDamage(stack)));
			
			int maxUse = getMaxCustomDamgae(stack);
			float lastUse = (float) maxUse - ItemSubTool.getCustomDamage(stack);
			list.add(Langs.infoToolHeadMaterial, ItemSubTool.getToolMaterial(stack).getLocalName());
			
			if(prop.hasHandle())
			{
				SubstanceHandle handle = ItemSubTool.getHandleMaterial(stack);
				list.add(Langs.infoToolHandleMaterial, handle.getLocalName());
			}
			list.add(Langs.infoToolDamage, (int) (lastUse * 100), (int) (maxUse * 100));
		}
	}
}