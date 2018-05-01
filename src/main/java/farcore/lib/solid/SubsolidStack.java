/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.solid;

import javax.annotation.Nullable;

/**
 * @author ueyudiud
 */
public class SubsolidStack
{
	private Subsolid solid;
	public int amount;
	
	public SubsolidStack(Subsolid solid)
	{
		this(solid, 1);
	}
	public SubsolidStack(Subsolid solid, int amount)
	{
		this.solid = solid;
		this.amount = amount;
	}
	
	public Solid getSolid()
	{
		return this.solid.getFluid();
	}
	
	public Subsolid getSubsolid()
	{
		return this.solid;
	}
	
	public SolidStack toSolidStack()
	{
		return this.solid.stack(this.amount);
	}
	
	/**
	 * Return a copy of Subsolid stack with specific size,
	 * <code>null</code> if amount is non-positive number.
	 * @param amount the stack amount.
	 * @return the new stack.
	 */
	@Nullable
	public SubsolidStack of(int amount)
	{
		return amount > 0 ? new SubsolidStack(this.solid, amount) : null;
	}
}
