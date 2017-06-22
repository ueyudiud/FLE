package farcore.lib.material.prop;

import java.util.Random;

import farcore.lib.material.Mat;
import farcore.lib.material.ore.IOreProperty;
import farcore.lib.tile.instance.TEOre;
import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PropertyOre extends PropertyBlockable implements IOreProperty
{
	public PropertyOre(Mat material, int harvestLevel, float hardness, float explosionResistance)
	{
		super(material, harvestLevel, hardness, explosionResistance);
	}
	
	@Override
	public void updateTick(TEOre ore, Random rand)
	{
		
	}
	
	@Override
	public boolean onBlockClicked(TEOre ore, EntityPlayer playerIn, Direction side)
	{
		return false;
	}
	
	@Override
	public void onEntityWalk(TEOre ore, Entity entityIn)
	{
		
	}
	
	@Override
	public EnumActionResult onBlockActivated(TEOre ore, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
			Direction side, float hitX, float hitY, float hitZ)
	{
		return EnumActionResult.PASS;
	}
	
	@Override
	public boolean onBurn(TEOre ore, float burnHardness, Direction direction)
	{
		return false;
	}
	
	@Override
	public boolean onBurningTick(TEOre ore, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		return false;
	}
	
	@Override
	public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, TEOre ore,
			Direction side, float hitX, float hitY, float hitZ)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, TEOre ore, Random rand)
	{
		
	}
	
	/**
	 * Only for out dated ore property wrapper, not
	 * suggested use in mod.
	 * @author ueyudiud
	 *
	 */
	@Deprecated
	public static class PropertyOreWrapper extends PropertyOre
	{
		IOreProperty oreProperty;
		
		public PropertyOreWrapper(Mat material, int harvestLevel, float hardness, float explosionResistance, IOreProperty oreProperty)
		{
			super(material, harvestLevel, hardness, explosionResistance);
			this.oreProperty = oreProperty;
		}
		
		@Override
		public EnumActionResult onBlockActivated(TEOre ore, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
				Direction side, float hitX, float hitY, float hitZ)
		{
			return this.oreProperty.onBlockActivated(ore, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}
		
		@Override
		public boolean onBlockClicked(TEOre ore, EntityPlayer playerIn, Direction side)
		{
			return this.oreProperty.onBlockClicked(ore, playerIn, side);
		}
		
		@Override
		public boolean onBurn(TEOre ore, float burnHardness, Direction direction)
		{
			return this.oreProperty.onBurn(ore, burnHardness, direction);
		}
		
		@Override
		public boolean onBurningTick(TEOre ore, Random rand, Direction fireSourceDir, IBlockState fireState)
		{
			return this.oreProperty.onBurningTick(ore, rand, fireSourceDir, fireState);
		}
		
		@Override
		public void onEntityWalk(TEOre ore, Entity entityIn)
		{
			this.oreProperty.onEntityWalk(ore, entityIn);
		}
		
		@Override
		public ActionResult<Float> onToolClick(EntityPlayer player, EnumToolType tool, ItemStack stack, TEOre ore,
				Direction side, float hitX, float hitY, float hitZ)
		{
			return this.oreProperty.onToolClick(player, tool, stack, ore, side, hitX, hitY, hitZ);
		}
	}
}