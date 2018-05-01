/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.Subsolid;
import nebula.common.inventory.IContainer;
import nebula.common.inventory.task.Task;
import nebula.common.nbt.INBTSelfCompoundReaderAndWriter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;

/**
 * @author ueyudiud
 */
public interface ISolidContainer extends IContainer<SolidStack>, INBTSelfCompoundReaderAndWriter
{
	/**
	 * Return <code>true</code> if solid is available to insert into this container.
	 * @param solid the checked sub solid.
	 * @return
	 */
	boolean isAvailable(Subsolid solid);
	
	/**
	 * Return stack amount contained in this container.
	 * @return the stack amount.
	 */
	int getStackAmountInContainer();
	
	/**
	 * Return max stack amount sill can be insert to this container.
	 * @return the max amount.
	 */
	int getRemainAmountInContainer();
	
	/**
	 * Create a simulated container, used for multiple insert check.
	 * @return the simulated container.
	 * @throws UnsupportedOperationException when simulated item container is not allowed.
	 */
	ISolidContainer simulated();
	
	/**
	 * Return all stacks in the container.<p>
	 * This collection is immutable that if you want take
	 * operation on any stack in the container, use method
	 * in this interface instead.
	 * @return the collection.
	 */
	@Nonnull Collection<SolidStack> stacks();
	
	/**
	 * Take <tt>click</tt> operation.<p>
	 * Called as player operation simulated.<p>
	 * The available modifiers are {@link #PROCESS}.
	 * @param stack the click item.
	 * @param modifier the operation request.
	 */
	/* Should I take this method in solid slot? */
	default ActionResult<ItemStack> clickContainer(@Nullable ItemStack stack, int modifier) { return new ActionResult<>(EnumActionResult.PASS, stack); }
	
	/**
	 * Take <tt>increase</tt> operation.<p>
	 * Increase amount of SolidStack contained specific solid from container.<p>
	 * The available modifiers are {@link #PROCESS}, {@link #FULLY}, {@link #SKIP_AC}.
	 * @param solid the specific solid.
	 * @param amount the increase amount.
	 * @param modifier the operation request.
	 * @return the increased amount.
	 */
	int incrStack(Subsolid solid, int amount, int modifier);
	
	/**
	 * Take <tt>decrease</tt> operation.<p>
	 * Decrease amount of SolidStack contained specific solid of from container.<p>
	 * The available modifiers are {@link #PROCESS}, {@link #FULLY}.
	 * @param solid the specific solid.
	 * @param size the decrease amount.
	 * @param modifier the operation request.
	 * @return the decreased amount.
	 */
	int decrStack(Subsolid solid, int amount, int modifier);
	
	/**
	 * Create <tt>increase</tt> operation task.<p>
	 * Increase SolidStack {@link #FULLY} into container.<p>
	 * The available modifiers are {@link #PROCESS}, {@link #SKIP_REFRESH}.
	 * @param solid the solid to take increasing match.
	 * @param amount the amount to increase.
	 * @param modifier the operation request.
	 * @return the task.
	 * @see #incrStack(Subsolid, int, int)
	 */
	Task.TaskBTB taskIncr(Subsolid solid, int amount, int modifier);
	
	/**
	 * Create <tt>decrease</tt> operation task.<p>
	 * Decrease SolidStack {@link #FULLY} from container.<p>
	 * The available modifiers are {@link #PROCESS}, {@link #SKIP_REFRESH}.
	 * @param solid the solid to take decreasing match.
	 * @param amount the amount to decrease
	 * @param modifier the operation request.
	 * @return the task.
	 * @see #decrStack(Subsolid, int, int)
	 */
	Task.TaskBTB taskDecr(Subsolid solid, int amount, int modifier);
}
