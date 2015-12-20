package fle.tool.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.FleAPI;
import flapi.block.old.BlockHasTile;
import flapi.util.FleValue;
import fle.core.init.Materials;
import fle.core.te.TileEntityDitch;
import fle.tool.DitchInfo;

public class BlockDitch extends BlockHasTile
{
	public BlockDitch()
	{
		super(ItemDitch.class, "ditch", Material.piston);
		setHardness(0.5F);
		setResistance(0.3F);
		FleAPI.langManager.registerLocal(new ItemStack(this).getUnlocalizedName() + ".name", "%s Ditch");
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world,
			int x, int y, int z)
	{
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
    public void addCollisionBoxesToList(World aWorld, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
    {
		if(aWorld.getTileEntity(x, y, z) instanceof TileEntityDitch)
		{
			TileEntityDitch tile = (TileEntityDitch) aWorld.getTileEntity(x, y, z);
			setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 0.5F, 0.625F);
			super.addCollisionBoxesToList(aWorld, x, y, z, aabb, list, entity);
			if(tile.canConnectWith(ForgeDirection.NORTH))
			{
				setBlockBounds(0.375F, 0.25F, 0.0F, 0.625F, 0.5F, 0.375F);
				super.addCollisionBoxesToList(aWorld, x, y, z, aabb, list, entity);
			}
			if(tile.canConnectWith(ForgeDirection.EAST))
			{
				setBlockBounds(0.625F, 0.25F, 0.375F, 1.0F, 0.5F, 0.625F);
				super.addCollisionBoxesToList(aWorld, x, y, z, aabb, list, entity);
			}
			if(tile.canConnectWith(ForgeDirection.SOUTH))
			{
				setBlockBounds(0.375F, 0.25F, 0.625F, 0.625F, 0.5F, 1.0F);
				super.addCollisionBoxesToList(aWorld, x, y, z, aabb, list, entity);
			}
			if(tile.canConnectWith(ForgeDirection.WEST))
			{
				setBlockBounds(0.0F, 0.25F, 0.375F, 0.375F, 0.5F, 0.625F);
				super.addCollisionBoxesToList(aWorld, x, y, z, aabb, list, entity);
			}
		}
	}

	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		;
	}

	@Override
	public boolean hasSubs() 
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta)
	{
		return new TileEntityDitch();
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		if(tile instanceof TileEntityDitch)
		{
			return ((TileEntityDitch) tile).getDrop();
		}
		return new ArrayList();
	}
	
	private DitchInfo infos[] = {Materials.ditch_stone, Materials.ditch_wood0, Materials.ditch_wood1, Materials.ditch_wood2, Materials.ditch_wood3, Materials.ditch_wood4, Materials.ditch_wood5};
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab,
			List list)
	{
		for(DitchInfo info : infos)
		{
			list.add(ItemDitch.a(info, 100));
		}
	}
}