package flapi.block;

import java.util.List;
import java.util.Set;

import farcore.block.interfaces.IHarvestCheck;
import farcore.world.BlockPos;
import flapi.block.item.ItemBlockSub;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSub extends BlockFle implements IHarvestCheck
{
	protected final InformationBlock[] informations = new InformationBlock[16];
	
	protected BlockSub(String unlocalized, Material Material)
	{
		super(ItemBlockSub.class, unlocalized, Material);
	}
	protected BlockSub(Class<? extends ItemBlockSub> clazz, String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
	}
	
	public BlockSub addInfomation(InformationBlock infomation)
	{
		informations[infomation.meta] = infomation;
		return this;
	}
	
	private InformationBlock getInfomation(int meta)
	{
		return meta >= informations.length || meta < 0 || informations[meta] == null ?
				InformationBlock.DEFAULT : informations[meta];
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		return getInfomation(world.getBlockMetadata(x, y, z)).getHardness();
	}
	
	@Override
	public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX,
			double explosionY, double explosionZ)
	{
		return getInfomation(world.getBlockMetadata(x, y, z)).getResistance();
	}
	
	@Override
	public boolean isToolEffective(String type, int metadata)
	{
		return getInfomation(metadata).getEffectiveTools().contains(type);
	}
	
	@Override
	public Set<String> getAccessTools(BlockPos pos)
	{
		return getInfomation(pos.meta()).getEffectiveTools();
	}
	
	@Override
	public int getToolLevel(BlockPos pos, String tool)
	{
		return getInfomation(pos.meta()).getHarvestLevel();
	}
	
	@Override
	protected void getDrops(List<ItemStack> list, World world, EntityPlayer player, 
			int x, int y, int z, int meta, int fortune, boolean silkHarvest, 
			TileEntity tile)
	{
		if(!silkHarvest)
		{
			list.addAll(getInfomation(meta).getDrops(fortune));
		}
		else
		{
			list.add(new ItemStack(this, 1, meta));
		}
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int i = 0; i < informations.length; ++i)
		{
			if(informations[i] != null)
			{
				list.add(new ItemStack(item, 1, i));
			}
		}
	}
}