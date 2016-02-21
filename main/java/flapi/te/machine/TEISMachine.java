//package flapi.te.machine;
//
//import net.minecraft.nbt.NBTTagCompound;
//import flapi.gui.GuiError;
//import flapi.recipe.machine.RecipeHandler.RecipeInfo;
//import flapi.recipe.machine.RecipeHandlerIS;
//import flapi.recipe.machine.RecipeHandlerIS.ISolidMachineInput;
//import flapi.recipe.machine.RecipeHandlerIS.RecipeIS;
//import flapi.te.InventoryTile;
//import flapi.te.TEISolidTank;
//
//public abstract class TEISMachine<R extends RecipeIS<A>, A extends RecipeInfo> extends TEISolidTank implements ISolidMachineInput
//{
//	public A recipe;
//	
//	public TEISMachine(InventoryTile inv, int capcity)
//	{
//		super(inv, capcity);
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
//					onRecipeOutput();
//			recipe = null;
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
//	protected abstract RecipeHandlerIS<R, A> getRecipeHandler();
//	
//	protected abstract boolean isInputSlot(int slotID);
//	protected abstract boolean isOutputSlot(int slotID);
//	
//	public GuiError getGUICondition()
//	{
//		return GuiError.DEFAULT;
//	}
//}