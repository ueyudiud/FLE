//package flapi.te.machine;
//
//import net.minecraft.nbt.NBTTagCompound;
//import flapi.gui.GuiError;
//import flapi.recipe.machine.RecipeHandler.RecipeInfo;
//import flapi.recipe.machine.RecipeHandlerI;
//import flapi.recipe.machine.RecipeHandlerI.IMachineInput;
//import flapi.recipe.machine.RecipeHandlerI.RecipeI;
//import flapi.te.InventoryTile;
//import flapi.te.TEInventory;
//
//public abstract class TEIMachine<R extends RecipeI<A>, A extends RecipeInfo> extends TEInventory implements IMachineInput
//{
//	public A recipe;
//	
//	public TEIMachine(InventoryTile inv)
//	{
//		super(inv);
//	}
//	
//	@Override
//	public void readFromNBT(NBTTagCompound nbt)
//	{
//		super.readFromNBT(nbt);
//		if(nbt.hasKey("Recipe"))
//		{
//			NBTTagCompound nbt1 = nbt.getCompoundTag("Recipe");
//			R r = getRecipeHandler().get(nbt1.getString("Name"));
//			if(recipe != null)
//				recipe = r.loadFromNBT(nbt1);
//		}
//	}
//	
//	@Override
//	public void writeToNBT(NBTTagCompound nbt)
//	{
//		super.writeToNBT(nbt);
//		writeToNBT2(nbt);
//	}
//	
//	public void writeToNBT2(NBTTagCompound nbt)
//	{
//		if(recipe != null)
//		{
//			NBTTagCompound nbt1 = new NBTTagCompound();
//			nbt1.setString("Name", recipe.getRecipeName());
//			nbt1.setTag("Recipe", recipe.writeToNBT(nbt1));
//		}
//	}
//
//	protected void matchAndInputRecipe()
//	{
//		if(recipe == null)
//		{
//			if((recipe = getRecipeHandler().matchAndInput(this)) != null)
//				onRecipeInput();
//		}
//	}
//	
//	protected void matchAndOutputRecipe()
//	{
//		if(recipe != null)
//		{
//			if(getRecipeHandler().match(recipe, this))
//				if(getRecipeHandler().output(recipe, this))
//				{
//					onRecipeOutput();
//					recipe = null;
//				}
//		}
//	}
//
//	protected void onRecipeInput()
//	{
//		
//	}
//	
//	protected void onRecipeOutput()
//	{
//		
//	}
//	
//	public boolean isWorking()
//	{
//		return recipe != null;
//	}
//	
//	protected abstract RecipeHandlerI<R, A> getRecipeHandler();
//	
//	public GuiError getGUICondition()
//	{
//		return GuiError.DEFAULT;
//	}
//}