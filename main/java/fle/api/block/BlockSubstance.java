package fle.api.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farcore.block.BlockHasTile;
import farcore.block.ItemBlockBase;
import farcore.enums.EnumBlock.IInfoSpawnable;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.substance.ISubstance;
import farcore.lib.substance.SubstanceBlockAbstract;
import farcore.lib.substance.SubstanceRock;
import farcore.util.U;
import fle.api.tile.TileEntitySubstance;
import fle.api.util.V;
import fle.core.tile.statics.TileEntityRock;
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

public abstract class BlockSubstance<T extends SubstanceBlockAbstract> extends BlockHasTile
implements IInfoSpawnable
{
	public final ItemSubstance item;
	protected Map<SubstanceBlockAbstract, IIcon> icons = new HashMap();
	protected final IRegister<T> register = createNewTileEntity(null, 0).getRegister();
	
	protected BlockSubstance(String name, Material material)
	{
		super(name, ItemSubstance.class, material);
		this.item = (ItemSubstance) Item.getItemFromBlock(this);
		item.initRegister();
	}

	protected BlockSubstance(String name, Class<? extends ItemBlockBase> clazz, Material material, Object[] objects)
	{
		super(name, clazz, material, objects);
		this.item = (ItemSubstance) Item.getItemFromBlock(this);
		item.initRegister();
	}
	
	@Override
	public String getMetadataName(int meta)
	{
		return U.Lang.validate(register.name(meta));
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		for(T t : this.register)
		{
			icons.put(t, register.registerIcon(getTextureName() + "/" + t.getName()));
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return icons.getOrDefault(((TileEntitySubstance<T>) world.getTileEntity(x, y, z)).substance, V.voidBlockIcon);
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return icons.getOrDefault(register.get(meta), V.voidBlockIcon);
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(T t : register)
		{
			list.add(this.item.provide(t));
		}
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntitySubstance)
		{
			return register.id(((TileEntitySubstance<T>) world.getTileEntity(x, y, z)).substance);
		}
		return super.getDamageValue(world, x, y, z);
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntitySubstance)
		{
			return ((TileEntitySubstance) world.getTileEntity(x, y, z)).substance.hardness;
		}
		return 1.0F;
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
		if(world.getTileEntity(x, y, z) instanceof TileEntitySubstance)
		{
			TileEntitySubstance tile = (TileEntitySubstance) world.getTileEntity(x, y, z);
			tile.substance = item.substance(stack);
			addInfomationToTile((TileEntitySubstance) tile, stack);
		}
	}
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		if(threadTile.get() != null)
		{
			SubstanceBlockAbstract substance = ((TileEntitySubstance) threadTile.get()).substance;
			return super.canHarvestBlock(player, register.id((T) substance));			
		}
		return super.canHarvestBlock(player, 0);
	}
	
	@Override
	public String getHarvestTool(int metadata)
	{
		return register.get(metadata).harvestTool;
	}
	
	@Override
	public int getHarvestLevel(int metadata)
	{
		return register.get(metadata).harvestLevel;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune, boolean silktouch,
			TileEntity tile)
	{
		ArrayList<ItemStack> ret = new ArrayList();
		if(tile == null)
		{
			return ret;
		}
		((TileEntitySubstance) tile).getDrops(this, item, ret, silktouch);
		return ret;
	}
	
	protected void addInfomationToTile(TileEntitySubstance tile, ItemStack stack)
	{
		
	}

	public abstract TileEntitySubstance<T> createNewTileEntity(World world, int meta);
}