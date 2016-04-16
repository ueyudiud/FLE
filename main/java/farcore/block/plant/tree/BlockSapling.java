package farcore.block.plant.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.block.BlockHasTile;
import farcore.lib.substance.SubstanceWood;
import farcore.util.FleLog;
import farcore.util.V;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSapling extends BlockHasTile
{
	@SideOnly(Side.CLIENT)
	private Map<String, IIcon> icons;
	
	public BlockSapling()
	{
		super("sapling", ItemSapling.class, Material.plants);
		setHardness(1.0F);
		GameRegistry.registerTileEntity(TileEntitySpaling.class, "treeSpaling");		
	}
	
	@Override
	public int getRenderType()
	{
		return FarCoreSetup.handlerA.getRenderId();
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		icons = new HashMap();
		for(SubstanceWood wood : SubstanceWood.getWoods())
		{
			icons.put(wood.getName(), register.registerIcon(getTextureName() + "/" + wood.getName()));
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		IIcon icon;
		try
		{
			icon = icons.get(((TileEntitySpaling) world.getTileEntity(x, y, z)).wood.getName());
		}
		catch(Exception exception)
		{
			return V.voidBlockIcon;
		}
		if(icon == null) icon = V.voidBlockIcon;
		return icon;
	}
	
	@Override
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
	{
		return true;
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		IIcon icon = icons.get(SubstanceWood.getWoods().name(meta));
		if(icon == null) icon = V.voidBlockIcon;
		return icon;		
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		for(SubstanceWood wood : SubstanceWood.getWoods())
		{
			list.add(ItemSapling.setWood(new ItemStack(item), wood));
		}
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y,
			int z)
	{
		return null;
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta)
	{
		return 0;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof TileEntitySpaling)
		{
			((TileEntitySpaling) tile).setWood(ItemSapling.getWood(stack));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntitySpaling();
	}
}