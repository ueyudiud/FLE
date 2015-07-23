package fla.core.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.recipe.Fuel;
import fla.api.recipe.IItemChecker.OreChecker;
import fla.api.util.FlaValue;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;
import fla.core.FlaItems;
import fla.core.tileentity.TileEntityOilLamp;
import fla.core.tileentity.base.TileEntityBaseWithFacing;

public class BlockOilLamp extends BlockBaseHasTile
{
	private IIcon wickIcon;
	private ForgeDirection lastDir = ForgeDirection.NORTH;
	
	public BlockOilLamp() 
	{
		super(Material.piston, SubTag.BLOCK_FLE_RENDER, SubTag.BLOCK_NOT_SOILD);
		setStepSound(soundTypeStone);
		setBlockBounds(0.3F, 0F, 0.3F, 0.7F, 0.25F, 0.7F);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		wickIcon = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":iconsets/wick");
		for(Fuel fuel : Fuel.fuelMap)
		{
			fuel.setIcon(register.registerIcon(fuel.getFuelTextureName()));
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,
			int y, int z, EntityPlayer player,
			int side, float xPos, float yPos,
			float zPos)
	{
		TileEntityOilLamp tile = (TileEntityOilLamp) world.getTileEntity(x, y, z);
		if(tile != null)
		{
			if(player.getCurrentEquippedItem() != null)
			{
				ItemStack itemstack = player.getCurrentEquippedItem().copy();
				if(new OreChecker("craftingToolFireStarter").match(itemstack))
				{
					itemstack.getItem().onItemUse(itemstack, player, world, x, y + 1, z, side, xPos, yPos, zPos);
					tile.setBurning();
				}
				if(new OreChecker("wick").match(itemstack))
				{
					tile.setWick();
				}
			}
		}
		return true;
	}
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return ((TileEntityOilLamp) world.getTileEntity(x, y, z)).isBurning() ? 13 : 0;
	}
	
	@Override
	public void setBlockFacing(BlockPos pos, ForgeDirection dir) 
	{
		lastDir = dir;
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ((TileEntityBaseWithFacing) pos.getBlockTile()).getBlockDirection(pos);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityOilLamp().setFacing(lastDir);
	}

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return Blocks.stone.getBlockTextureFromSide(side.ordinal());
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return Blocks.stone.getBlockTextureFromSide(side.ordinal());
	}

	public IIcon getWickIcon()
	{
		return wickIcon;
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}
	
	@Override
	public boolean hasSubs() 
	{
		return false;
	}

	@Override
	protected boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour) 
	{
		return false;
	}
	
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntityOilLamp tile = (TileEntityOilLamp)world.getTileEntity(x, y, z);

        if (tile != null)
        {
        	ItemStack itemstack = new ItemStack(FlaItems.stone_oil_lamp);

        	float f = world.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
            
            EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), itemstack);

            if(tile.fluid != null) FlaItems.stone_oil_lamp.setLiquid(itemstack, tile.fluid.getFuel(), tile.fluid.getContain());
            if(tile.hasWick) FlaItems.stone_oil_lamp.setWick(itemstack, true);
            if(tile.isBurning) FlaItems.stone_oil_lamp.setBurning(itemstack, true);
            float f3 = 0.05F;
            
            world.spawnEntityInWorld(entityitem);
            }

        world.func_147453_f(x, y, z, block);
    }
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune) 
	{
		return new ArrayList();
	}
	
	/**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
    	TileEntityOilLamp tile = (TileEntityOilLamp) world.getTileEntity(x, y, z);
    	if(tile == null) return;
    	if(tile.isBurning)
    	{
    		double f1 = (double)x + 0.49325D;
    		double f2 = (double)y + 0.4D + rand.nextFloat() / 32F;
    		double f3 = (double)z + 0.50625D;
    		world.spawnParticle("flame", f1, f2, f3, 0.0D, 0.0D, 0.0D);
            if(tile.hasSmoke())    
            	world.spawnParticle("smoke", f1, f2, f3, 0.0D, 0.2D, 0.0D);
    	}
    }
}