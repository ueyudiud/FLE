package fle.core.block.plant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.block.BlockBase;
import farcore.block.ItemBlockBase;
import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.lib.collection.Ety;
import farcore.lib.recipe.DropHandler;
import farcore.lib.stack.BaseStack;
import farcore.util.LanguageManager;
import farcore.util.U;
import fle.api.block.BlockClassicalVine;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockVine1 extends BlockClassicalVine
{
	private static final String[] names = {
			"vine_ivy",
			"vine_rattan"
		};
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	public BlockVine1(String name)
	{
		super(name);
		EnumBlock.vine.setBlock(this);
		setHardness(0.3F);
		setResistance(0.05F);
		growOffset = 1;
	}
	
	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		manager.registerLocal(getUnlocalizedName() + "@" + names[0] + ".name", "Ivy");
		manager.registerLocal(getUnlocalizedName() + "@" + names[1] + ".name", "Rattan");
	}
	
	@Override
	public String getMetadataName(int meta)
	{
		return names[meta];
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		icons = new IIcon[names.length];
		for(int i = 0; i < icons.length; ++i)
			icons[i] = register.registerIcon(getTextureName() + "/" + names[i]);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return icons[meta % icons.length];
	}
	
	@Override
	protected DropHandler getDropHandler(int meta)
	{
		switch (meta)
		{
		default: return new DropHandler(1, 
				new Ety(new BaseStack(EnumItem.plant.instance(1, "vine")), 1));
		}
	}
}