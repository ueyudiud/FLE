/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

/**
 * Block state register, use to register network data.<p>
 * The <tt>id</tt> of block state will allocate autonomously, you only
 * need assigning which state can be ignored or merge to other
 * state to store.
 * @author ueyudiud
 */
public interface IBlockStateRegister
{
	/**
	 * Unused, use {@link #registerStates(IProperty...)} instead.
	 */
	@Deprecated
	default void registerStates(Block block, IProperty<?>...properties)
	{
		registerStates(null, properties);
	}
	
	/**
	 * Register allowed state with all these properties cycled.<p>
	 * Example:<p>
	 * <tt>block[facing={n, s}, type={1, 2}];</tt><p>
	 * <code>registerStates(PROPERTY_FACING);</code> will put:<p>
	 * [block{facing=n, type=1}, block{facing=n, type=2}]=>id1<br>
	 * [block{facing=s, type=1}, block{facing=s, type=2}]=>id2<p>
	 * to map.
	 * @param properties the properties use to split block state, and unlisted
	 *                   property will not use to split block state.
	 */
	void registerStates(IProperty<?>...properties);
	
	/**
	 * Register state that should be transformed to itself in <tt>state=>id</tt>
	 * then <tt>id=>state</tt> logic.
	 * @param state the block state.
	 */
	void registerState(IBlockState state);
	
	/**
	 * Register <tt>id=>source</tt> and <tt>targets=>id</tt> logic.<p>
	 * @param source the id to state target.
	 * @param castable the state to id targets.
	 */
	void registerStateMap(IBlockState source, Collection<IBlockState> targets);
	
	/**
	 * @see #registerStateMap(IBlockState, Collection)
	 */
	void registerStateMap(IBlockState source, IBlockState...castable);
}