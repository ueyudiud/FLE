/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items;

import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.util.Localization;
import fle.core.FLE;
import fle.loader.FLEConfig;
import nebula.client.CreativeTabBase;
import nebula.client.model.NebulaItemModelLoader;
import nebula.client.util.UnlocalizedList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemToolHead extends ItemMulti
{
	public ItemToolHead(MatCondition mc)
	{
		super(FLE.MODID, mc);
		if (FLEConfig.createAllToolCreativeTabs)
		{
			setCreativeTab(new CreativeTabBase(mc.name, mc.localName, () -> new ItemStack(this, 1, 0)));
		}
		this.maxStackSize = 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		NebulaItemModelLoader.registerModel(this, new ResourceLocation(this.modid, "tool_head/" + this.condition.name.replaceAll("_", ".")));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformation(ItemStack stack, EntityPlayer playerIn, UnlocalizedList unlocalizedList,
			boolean advanced)
	{
		super.addInformation(stack, playerIn, unlocalizedList, advanced);
		Mat material = getMaterial(stack);
		Localization.addToolMaterialInformation(material, null, unlocalizedList);
	}
}