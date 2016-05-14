package fle.core.block.machine.alpha;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.BlockHasTile;
import farcore.block.ItemBlockBase;
import fle.api.FleAPI;
import fle.core.tile.TileEntityCampfire;
import fle.load.Icons;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCampfire extends BlockHasTile
{
	public BlockCampfire()
	{
		super("campfire", ItemBlockBase.class, Material.circuits);
		blockHardness = 0.5F;
		blockResistance = 0.2F;
		setTickRandomly(true);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = Icons.firewood_side = register.registerIcon(getTextureName() + "/firewood_oak_side");
		Icons.firewood_top = register.registerIcon(getTextureName() + "/firewood_oak_top");
	}
	
	@Override
	public int getRenderType()
	{
		return FarCore.handlerA.getRenderId();
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 1;
	}
	
	@Override
	public boolean canBlockStay(World world, int x, int y, int z)
	{
		return world.isSideSolid(x, y - 1, z, ForgeDirection.UP);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		if(!canBlockStay(world, x, y, z))
		{
			breakBlock(world, x, y, z, this, world.getBlockMetadata(x, y, z));
			threadTile.set(null);
			world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(!canBlockStay(world, x, y, z))
		{
			breakBlock(world, x, y, z, this, world.getBlockMetadata(x, y, z));
			threadTile.set(null);
			world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y,
			int z)
	{
		return null;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof TileEntityCampfire)
		{
			if(((TileEntityCampfire) tile).isBurning())
			{
				entity.setFire(40);
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xPos,
			float yPos, float zPos)
	{
		if(!world.isRemote)
		{
			FleAPI.openGui(0, player, world, x, y, z);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityCampfire();
	}
}