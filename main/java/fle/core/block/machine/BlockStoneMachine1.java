package fle.core.block.machine;

import fle.api.FleValue;
import fle.core.block.behaviour.BehaviourCastingPool;
import fle.core.block.behaviour.BehaviourLavaHeatTransfer;
import fle.core.util.BlockTextureManager;

public class BlockStoneMachine1 extends BlockStoneMachine
{
	@Override
	public BlockStoneMachine1 init()
	{
		registerSub(0, "castingPool", "Casting Pool", new BlockTextureManager("void"), new BehaviourCastingPool());
		return this;
	}
	
	public BlockStoneMachine1(String aName)
	{
		super(aName);
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}