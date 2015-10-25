package fle.core.block.tank;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.BlockHasTile;
import fle.api.block.IDebugableBlock;
import fle.core.te.tank.TileEntityMultiTank;
import fle.core.te.tank.TileEntityMultiTankIO;
import fle.core.util.TankBlockInfo;

public class BlockMultiTank extends BlockHasTile implements IDebugableBlock
{
	private IIcon[] icons;
	
	public BlockMultiTank(String aName)
	{
		super(ItemTankBlock.class, aName, Material.rock);
		setResistance(1.0F);
		GameRegistry.registerTileEntity(TileEntityMultiTank.class, "tankMulti");
		GameRegistry.registerTileEntity(TileEntityMultiTankIO.class, "tankMultiIO");
		FleAPI.lm.registerLocal(getUnlocalizedName() + ":0.name", "Tank Block");
		FleAPI.lm.registerLocal(getUnlocalizedName() + ":1.name", "Tank Valve");
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}
	
	@Override
	public float getBlockHardness(World aWorld, int x,
			int y, int z)
	{
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof TileEntityMultiTank)
		{
			return ((TileEntityMultiTank) tile).getTankMaterial().getBlockHardness(aWorld, x, y, z);
		}
		return 1.0F;
	}

	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta)
	{
		switch(aMeta & 15)
		{
		case 0 : return new TileEntityMultiTank();
		case 1 : return new TileEntityMultiTankIO();
		}
		return null;
	}
	
	@Override
	public void onNeighborBlockChange(World aWorld, int x, int y, int z,
			Block block)
	{
		super.onNeighborBlockChange(aWorld, x, y, z, block);
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof TileEntityMultiTank)
		{
			((TileEntityMultiTank) tile).onNeibourChange(true);
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		if(tile instanceof TileEntityMultiTank)
			ret.add(new ItemStack(this, 1, metadata + (((TileEntityMultiTank) tile).getInfoData() << 4)));
		return ret;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		icons = new IIcon[2];
		icons[0] = register.registerIcon(getTextureName() + "_side");
		icons[1] = register.registerIcon(getTextureName() + "_valve");
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return icons[(meta % 16) % icons.length];
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab,
			List list)
	{
		for(TankBlockInfo info : ItemTankBlock.register)
		{
			list.add(ItemTankBlock.a(ItemTankBlock.register.name(info), 0));
			list.add(ItemTankBlock.a(ItemTankBlock.register.name(info), 1));
		}
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if (tile instanceof TileEntityMultiTank)
		{
			TileEntityMultiTank tank = (TileEntityMultiTank) tile;
			aList.add("Material is " + new ItemStack(tank.getTankMaterial(), 1, tank.getTankMaterialMeta()).getDisplayName());
		}
	}
}