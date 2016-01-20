package fle.core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import farcore.util.IDebugable;

public class ItemDebug extends Item
{
	public ItemDebug()
	{
		setUnlocalizedName("fle.debug");
		GameRegistry.registerItem(this, "fle.debug");
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn,
			World worldIn, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ)
	{
		if(worldIn.isRemote) return true;
		IBlockState state = worldIn.getBlockState(pos);
		List<String> list = new ArrayList();
		list.add("=============================FWM===============================");
		for(Object obj : state.getProperties().keySet())
		{
			try
			{
				list.add(((IProperty) obj).getName() + " : " + ((IProperty) obj).getName(state.getValue((IProperty) obj)));
			}
			catch(Throwable e)
			{
				list.add("State has bug!");
				break;
			}
		}
		if(state.getBlock() instanceof IDebugable)
		{
			try
			{
				((IDebugable) state.getBlock()).addInfomationToList(worldIn, pos, list);
			}
			catch(Throwable e)
			{
				list.add("Information has bug!");
			}
		}
		for(String str : list)
		{
			playerIn.addChatMessage(new ChatComponentText(str));
		}
		return true;
	}
}
