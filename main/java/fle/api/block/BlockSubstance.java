package fle.api.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farcore.block.BlockBase;
import farcore.block.BlockHasTile;
import farcore.block.ItemBlockBase;
import farcore.enums.EnumBlock.IInfoSpawnable;
import farcore.interfaces.ISmartHarvestBlock;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.substance.ISubstance;
import farcore.lib.substance.SubstanceBlockAbstract;
import farcore.lib.substance.SubstanceRock;
import farcore.util.U;
import farcore.util.V;
import fle.api.tile.TileEntitySubstance;
import fle.core.tile.statics.TileEntityRock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public abstract class BlockSubstance<T extends SubstanceBlockAbstract> extends BlockBase implements IInfoSpawnable, ISmartHarvestBlock
{
	public final ItemSubstance item;
	protected final IRegister<Block> register;
	
	protected final T substance;
	
	protected BlockSubstance(String name, Material material, T substance, IRegister<Block> register)
	{
		super(name + "." + substance.getName(), ItemSubstance.class, material);
		this.item = (ItemSubstance) Item.getItemFromBlock(this);
		this.register = register;
		this.substance = substance;
		item.initRegister();
	}

	protected BlockSubstance(String name, Class<? extends ItemBlockBase> clazz, Material material, T substance, IRegister<Block> register, Object[] objects)
	{
		super(name + ":" + substance.getName(), clazz, material, objects);
		this.item = (ItemSubstance) Item.getItemFromBlock(this);
		this.register = register;
		this.substance = substance;
		item.initRegister();
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(getTextureName() + "/" + substance.getName());
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return blockIcon;
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return blockIcon;
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(item));
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
//		if(world.getTileEntity(x, y, z) instanceof TileEntitySubstance)
//		{
//			return register.id(((TileEntitySubstance<T>) world.getTileEntity(x, y, z)).substance);
//		}
		return super.getDamageValue(world, x, y, z);
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
//		if(world.getTileEntity(x, y, z) instanceof TileEntitySubstance)
//		{
//			return ((TileEntitySubstance) world.getTileEntity(x, y, z)).substance.hardness;
//		}
		return substance.hardness;
	}
	
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX,
			double explosionY, double explosionZ)
	{
		return getBlockHardness(world, x, y, z) * 1.2F;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack)
	{
//		if(world.getTileEntity(x, y, z) instanceof TileEntitySubstance)
//		{
//			TileEntitySubstance tile = (TileEntitySubstance) world.getTileEntity(x, y, z);
//			tile.substance = item.substance(stack);
//			addInfomationToTile((TileEntitySubstance) tile, stack);
//		}
	}
	
	private boolean harvestFlag = false;
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		if(!harvestFlag) return false;
		return super.canHarvestBlock(player, 0);
	}
	
	@Override
	public String getHarvestTool(int metadata)
	{
		return substance.harvestTool;
	}
	
	@Override
	public int getHarvestLevel(int metadata)
	{
		return substance.harvestLevel;
	}
	
	@Override
	public boolean canHarvestBlock(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		harvestFlag = true;
//		threadTile.set(world.getTileEntity(x, y, z));
		boolean ret = canHarvestBlock(player, meta);
//		threadTile.set(null);
		harvestFlag = false;
		return ret;
	}
	
	@Override
	public void harvestAndDropBlock(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		harvestBlock(world, player, x, y, z, meta);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
			boolean silkTouching)
	{
		ArrayList<ItemStack> ret = new ArrayList();
		ret.add(new ItemStack(this));
		return ret;
	}
		
	protected void addInfomationToTile(TileEntitySubstance tile, ItemStack stack)
	{
		
	}
}