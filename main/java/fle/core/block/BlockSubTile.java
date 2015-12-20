package fle.core.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.FleAPI;
import flapi.block.interfaces.IBlockBehaviour;
import flapi.block.interfaces.IBlockWithTileBehaviour;
import flapi.block.interfaces.IDebugableBlock;
import flapi.block.interfaces.IFacingBlock;
import flapi.block.old.BlockHasTile;
import flapi.collection.Register;
import flapi.enums.EnumWorldNBT;
import flapi.te.TEBase;
import flapi.util.BlockTextureHandler;
import flapi.util.FleValue;
import flapi.util.IDebugable;
import flapi.world.BlockPos;
import fle.FLE;
import fle.core.util.BlockTextureManager;

public abstract class BlockSubTile extends BlockHasTile implements IFacingBlock, IDebugable, IDebugableBlock
{
	public final Register<IBlockWithTileBehaviour<BlockSubTile>> blockBehaviors = new Register();
	protected Map<String, BlockTextureHandler> textureNameMap = new HashMap();

	protected BlockSubTile(String aName, Material aMaterial)
	{
		super(ItemSubTile.class, aName, aMaterial);
	}
	protected BlockSubTile(Class<? extends ItemSubTile> clazz, String aName, Material aMaterial)
	{
		super(clazz, aName, aMaterial);
	}
	
	public void registerSub(int index, String aName, String aLocalized, BlockTextureManager locate, IBlockWithTileBehaviour<BlockSubTile> blockBehavior)
	{
		blockBehaviors.register(index, blockBehavior, aName);
		textureNameMap.put(aName, new BlockTextureHandler(locate));
		FleAPI.langManager.registerLocal(new ItemStack(this, 1, index).getUnlocalizedName() + ".name", aLocalized);
	}
	
	public Register<IBlockWithTileBehaviour<BlockSubTile>> getRegister()
	{
		return blockBehaviors;
	}
	
	public BlockTextureHandler getTextureHandler(String meta)
	{
		return textureNameMap.get(meta);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
		for(String str : textureNameMap.keySet())
		{
			BlockTextureHandler handler = textureNameMap.get(str);
			if(handler != null) handler.registerIcon(register);
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		int tMeta = getDamageData(world, x, y, z);
		int tSide = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Facing);
		try
		{
			return textureNameMap.get(blockBehaviors.name(tMeta)).getIcon(this, world, x, y, z, FleValue.MACHINE_FACING[tSide][side]);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			return blockIcon;
		}
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		try
		{
			return textureNameMap.get(blockBehaviors.name(meta)).getIcon(this, meta, FleValue.MACHINE_FACING[3][side]);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			return blockIcon;
		}
	}
	
	@Override
	public int getDamageValue(World aWorld, int x, int y, int z)
	{
		return getDamageData(aWorld, x, y, z);
	}
	
	public int getDamageData(IBlockAccess aWorld, int x, int y, int z)
	{
		return aWorld.getTileEntity(x, y, z) != null ? aWorld.getTileEntity(x, y, z).getBlockMetadata() : aWorld.getBlockMetadata(x, y, z);
	}
	
	@Override
	public void addCollisionBoxesToList(World aWorld, int x,
			int y, int z, AxisAlignedBB aabb,
			List list, Entity entity)
	{
		super.addCollisionBoxesToList(aWorld, x, y, z, aabb, list, entity);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access,
			int x, int y, int z)
	{
		
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld,
			int x, int y, int z)
	{
		return noBoxList.contains(getDamageData(aWorld, x, y, z)) ? null : 
			super.getCollisionBoundingBoxFromPool(aWorld, x, y, z);
	}
	
	protected List<Integer> noBoxList = new ArrayList();
	
	@Override
	public void getSubBlocks(Item aItem, CreativeTabs aTab,	List aList) 
	{
		for(IBlockWithTileBehaviour<BlockSubTile> tB : blockBehaviors)
		{
			if(tB != null)
				aList.add(new ItemStack(aItem, 1, blockBehaviors.serial(tB)));
		}
	}
	
	@Override
	public boolean onBlockActivated(World aWorld, int x, int y, int z, EntityPlayer aPlayer, ForgeDirection aSide, float xPos, float yPos, float zPos)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			if(tBehaviour.onBlockActivated(this, aWorld, x, y, z, aPlayer, aSide, xPos, yPos, zPos))
			{
				return true;
			}
			else return false;
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return false;
	}
	
	@Override
	public void onBlockClicked(World aWorld, int x, int y, int z, EntityPlayer aPlayer)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onBlockClicked(this, aWorld, x, y, z, aPlayer);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void onNeighborBlockChange(World aWorld, int x, int y, int z,
			Block block)
	{
		if(!canBlockStay(aWorld, x, y, z))
		{
			dropBlockAsItem(aWorld, x, y, z, getDamageValue(aWorld, x, y, z), 0);
		}
		else
		{
			super.onNeighborBlockChange(aWorld, x, y, z, block);
		}
	}
	
	@Override
	public boolean canBlockStay(World aWorld, int x,
			int y, int z)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			return tBehaviour.canBlockStay(this, aWorld, x, y, z);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return true;
	}
	
	@Override
	public boolean canPlaceBlockAt(World aWorld, int x,
			int y, int z)
	{
		return canBlockStay(aWorld, x, y, z);
	}
	
	@Override
	public void onBlockPlacedBy(World aWorld, int x,
			int y, int z, EntityLivingBase aEntity,
			ItemStack aStack)
	{
		ForgeDirection dir = getPointFacing(aWorld, x, y, z, aEntity).getOpposite();
		FLE.fle.getWorldManager().setData(new BlockPos(aWorld, x, y, z), EnumWorldNBT.Facing, FleAPI.getIndexFromDirection(dir));
		if(aWorld.getTileEntity(x, y, z) instanceof TEBase)
		{
			((TEBase) aWorld.getTileEntity(x, y, z)).setDirction(dir);
		}
		super.onBlockPlacedBy(aWorld, x, y, z, aEntity, aStack);
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onBlockPlacedBy(this, aWorld, x, y, z, aStack, aEntity);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public int onBlockPlaced(World aWorld, int x, int y, int z, int aSide, float xPos, float yPos, float zPos, int metadata)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) metadata));
		try
	    {
			tBehaviour.onBlockPlaced(this, aWorld, x, y, z, ForgeDirection.VALID_DIRECTIONS[aSide], xPos, yPos, zPos);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		return metadata;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World aWorld, int x, int y, int z, Entity aEntity) 
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onEntityCollidedWithBlock(this, aWorld, x, y, z, aEntity);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void onFallenUpon(World aWorld, int x, int y, int z, Entity aEntity, float aHeight)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onFallenUpon(this, aWorld, x, y, z, aEntity, aHeight);;
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock,
			int aMeta)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onBlockBreak(this, aWorld, x, y, z, aBlock, aMeta);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
	}
	
	@Override
	public void addInformation(ItemStack aStack, List<String> aList,
			EntityPlayer aPlayer) 
	{
		super.addInformation(aStack, aList, aPlayer);
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) aStack.getItemDamage()));
		try
	    {
			if(tBehaviour != null)
				tBehaviour.getAdditionalToolTips(this, aList, aStack);
			else 
				aList.add("Bug block!");
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}

	@Override
	public boolean hasTileEntity(int aMeta)
	{
		IBlockWithTileBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) aMeta));
		try
		{
			return tBehaviour == null ? false : tBehaviour.createNewTileEntity(this, null, aMeta) != null;
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta) 
	{
		IBlockWithTileBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) aMeta));
		try
		{
			return tBehaviour.createNewTileEntity(this, aWorld, aMeta);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		int meta = metaThread.get();
		IBlockWithTileBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) meta));
		try
		{
			return tBehaviour.getHarvestDrop(this, world, meta, tile, fortune);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		ArrayList<ItemStack> drops = new ArrayList();
		drops.add(new ItemStack(this, 1, meta));
		return drops;
	}

	@Override
	public ForgeDirection getDirction(BlockPos pos) 
	{
		return ForgeDirection.VALID_DIRECTIONS[FLE.fle.getWorldManager().getData(pos, EnumWorldNBT.Facing)];
	}

	@Override
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos,
			float yPos, float zPos) 
	{
		return false;
	}

	@Override
	public void setDirection(World world, BlockPos pos, ItemStack tool,
			float xPos, float yPos, float zPos)
	{
		
	}
	
	@Override
	public void dropBlockAsItem(World aWorld, int x,
			int y, int z, ItemStack aStack)
	{
		if(aStack != null)
			super.dropBlockAsItem(aWorld, x, y, z, aStack.copy());
	}
	
	@Override
	public void randomDisplayTick(World aWorld, int x,
			int y, int z, Random rand)
	{
		IBlockBehaviour<BlockSubTile> tBehaviour = blockBehaviors.get(Short.valueOf((short) getDamageValue(aWorld, x, y, z)));
		try
	    {
			tBehaviour.onRenderUpdate(this, aWorld, x, y, z, rand);
	    }
	    catch (Throwable e)
	    {
	    	e.printStackTrace();
	    }
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		IBlockWithTileBehaviour<BlockSubTile> behaviour = blockBehaviors.get(getDamageValue(aWorld, x, y, z));
		if(behaviour instanceof IDebugableBlock)
		{
			((IDebugableBlock) behaviour).addInfomationToList(aWorld, x, y, z, aList);//Old API.
		}
		else if(behaviour instanceof IDebugable)
		{
			((IDebugable) behaviour).addInfomationToList(aWorld, x, y, z, aList);
		}
	}
}