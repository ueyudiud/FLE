package farcore.lib.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.util.INamedIconRegister;
import farcore.lib.util.UnlocalizedList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemGrouped extends ItemBase
{
	private ItemGrouped parent;
	public final MatCondition condition;
	private String prefix = "";
	protected boolean hasGeneralTooltip;
	protected boolean hasMetaTooltip;

	protected ItemGrouped(MatCondition condition)
	{
		super("grouped." + condition.orePrefix.toLowerCase());
		hasSubtypes = true;
		this.condition = condition;
		parent = null;
	}
	protected ItemGrouped(String modid, MatCondition condition)
	{
		super(modid, "grouped." + condition.orePrefix.toLowerCase());
		hasSubtypes = true;
		this.condition = condition;
		parent = null;
	}
	ItemGrouped(ItemGrouped parent, String prefix)
	{
		super(parent.modid, "grouped." + prefix + "." + parent.condition.orePrefix.toLowerCase());
		hasSubtypes = true;
		condition = parent.condition;
		this.prefix = prefix;
		this.parent = parent;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return condition.translateToLocal(Mat.register.get(getDamage(stack)));
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		return parent != null ? parent.onLeftClickEntity(stack, player, entity) :
			super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity)
	{
		return parent != null ? parent.itemInteractionForEntity(stack, player, entity) :
			super.itemInteractionForEntity(stack, player, entity);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x,
			int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return parent != null ? parent.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ) :
			super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x,
			int y, int z, EntityLivingBase entity)
	{
		return parent != null ? parent.onBlockDestroyed(stack, world, block, x, y, z, entity) :
			super.onBlockDestroyed(stack, world, block, x, y, z, entity);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		return parent != null ? parent.onItemRightClick(stack, world, player) :
			super.onItemRightClick(stack, world, player);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int tick,
			boolean flag)
	{
		if(parent != null)
			parent.onUpdate(stack, world, entity, tick, flag);
		else
			super.onUpdate(stack, world, entity, tick, flag);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entity)
	{
		return parent != null ? parent.onEntityItemUpdate(entity) :
			super.onEntityItemUpdate(entity);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		if(parent != null)
			parent.onUsingTick(stack, player, count);
		else
			super.onUsingTick(stack, player, count);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick)
	{
		if(parent != null)
			parent.onPlayerStoppedUsing(stack, world, player, tick);
		else
			super.onPlayerStoppedUsing(stack, world, player, tick);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player)
	{
		return parent != null ? parent.onBlockStartBreak(stack, x, y, z, player) :
			super.onBlockStartBreak(stack, x, y, z, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(Mat material : Mat.register)
			if(condition.isBelongTo(material))
				list.add(new ItemStack(item, 1, material.id));
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void registerIcons(INamedIconRegister register)
	{
		for(Mat material : Mat.register)
			if(condition.isBelongTo(material))
			{
				register.push(material);
				register.registerIcon(null, material.modid + ":grouped/"+ condition.orePrefix.toLowerCase() + (prefix != "" ? "_" + prefix : "")  + "/" + material.name);
				register.pop();
			}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(INamedIconRegister register, ItemStack stack, int pass)
	{
		register.push(Mat.register.get(getDamage(stack)));
		IIcon icon = register.getIconFromName(null);
		register.pop();
		return icon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addUnlocalInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean F3H)
	{
		if(hasGeneralTooltip)
			list.add(getUnlocalizedName() + ".general.tooltip");
		if(hasMetaTooltip)
			list.add(getUnlocalizedName(getDamage(stack)) + ".tooltip");
	}
}