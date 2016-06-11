package fle.core.block;

import java.util.List;

import farcore.FarCore;
import farcore.block.ItemBlockBase;
import farcore.enums.EnumItem.IInfomationable;
import farcore.lib.substance.SubstanceWood;
import fle.load.Langs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemTorch extends ItemBlockBase implements IInfomationable
{
	public ItemTorch(Block block)
	{
		super(block);
	}
	
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		return ((BlockTorch) block).iconExtinguished;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
//		list.add(FarCore.translateToLocal(Langs.infoTorchMaterial, BlockTorch.getSubstance(stack).getLocalName()));
		list.add(FarCore.translateToLocal(Langs.infoTorchBurnTime, BlockTorch.getBurnTick(stack)));
		if(BlockTorch.getIsWet(stack))
		{
			list.add(FarCore.translateToLocal(Langs.infoTorchWet1));
			list.add(FarCore.translateToLocal(Langs.infoTorchWet2));
		}
	}

	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 2)
		{
//			SubstanceWood wood = (SubstanceWood) objects[0];
			int tick = ((Integer) objects[1]).intValue();
			ItemStack ret = new ItemStack(this, size);
//			BlockTorch.setSubstance(ret, SubstanceWood.WOOD_VOID);
			BlockTorch.setBurnTick(ret, tick);
			return ret;
		}
		if(objects.length == 1)
		{
//			SubstanceWood wood = (SubstanceWood) objects[0];
			ItemStack ret = new ItemStack(this, size);
//			BlockTorch.setSubstance(ret, wood);
			return ret;
		}
		return provide(size, SubstanceWood.WOOD_VOID);
	}
}