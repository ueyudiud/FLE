package farcore.alpha.interfaces.item;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.item.ItemSubTool;
import farcore.alpha.util.LangHook.UnlocalizedList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IToolProp
{
	Set<String> getTool();
	
	float getBaseDigSpeed(Block block, int meta);
	
	float getCraftDamage();
	
	float getAttackEntityDamage();
	
	float getDestoryBlockDamage();
	
	@SideOnly(Side.CLIENT)
	void getSubItem(ItemSubTool tool, List list);
	
	boolean isWeapon();
	
	boolean isBlockable();
	
	boolean hasToolHead();
	
	boolean hasHandle();

	void getAttributeModifiers(ItemSubTool item, Multimap multimap, ItemStack stack);
	
	@SideOnly(Side.CLIENT)
	void addInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean F3H);
}