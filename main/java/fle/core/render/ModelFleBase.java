package fle.core.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;

public class ModelFleBase extends ModelBase
{
	protected int pass;
	
	public void setRenderPass()
	{
		pass = MinecraftForgeClient.getRenderPass();
	}
	
	public void renderTileEntityAt(TileEntity tile, float size)
	{
		
	}
	  
	protected final void setRotation(ModelRenderer model, float x, float y, float z)
	{
	    model.rotateAngleX = x;
	    model.rotateAngleY = y;
	    model.rotateAngleZ = z;
	}
}