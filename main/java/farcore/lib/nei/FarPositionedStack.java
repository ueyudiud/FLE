package farcore.lib.nei;

import codechicken.nei.PositionedStack;
import farcore.lib.stack.AbstractStack;
import net.minecraft.item.ItemStack;

public class FarPositionedStack extends PositionedStack
{
	private AbstractStack stack;
	
	public FarPositionedStack(AbstractStack stack, int x, int y)
	{
		super(stack.display(), x, y);
		this.stack = stack;
	}
	public FarPositionedStack(AbstractStack stack, int x, int y, boolean genPerms) 
	{
		super(stack.display(), x, y, genPerms);
		this.stack = stack;
	}
	
	@Override
	public boolean contains(ItemStack arg0)
	{
		return stack.similar(arg0);
	}
}