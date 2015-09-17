package fle.api.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ModelPart extends ModelRenderer
{
	int size = -1;
	
	public ModelPart(ModelBase model, int textureX, int textureY)
	{
		super(model, textureX, textureY);
	}

	@Override
	public ModelPart addBox(String aName, float minX,
			float minY, float minZ, int maxX, int maxY,
			int maxZ)
	{
    	super.addBox(aName, minX, minY, minZ, maxX, maxY, maxZ);
    	float dis = (maxX - minX) * (maxX - minX) + (maxY - minY) * (maxY - minY) + (maxZ - minZ) * (maxZ - minZ);
    	if(dis > 1024.0F)
    	{
    		size = 16;
    	}
    	else if(dis > 256.0F)
    	{
    		size = 8;
    	}
    	else if(dis > 64.0F)
    	{
    		size = 4;
    	}
    	else if(dis > 16.0F)
    	{
    		size = 2;
    	}
    	else if(dis > 4.0F)
    	{
    		size = 1;
    	}
    	else
    	{
    		size = 0;
    	}
    	this.size = Math.max(this.size, size);
	    return this;
	}
	  
	public final boolean shouldRender(double aDis)
	{
		return aDis < 16.0D * size;
	}
	
	public final void render(TileEntity te, float pixelSize)
	{
		EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
	    double rx = ep.posX;
	    double ry = ep.posY;
	    double rz = ep.posZ;
	    double dx = rx - te.xCoord;
	    double dy = ry - te.yCoord;
	    double dz = rz - te.zCoord;
	    double d = dx * dx + dy * dy + dz * dz;
	    if ((!te.isInvalid()) || (shouldRender(d)))
	    {
	    	super.render(pixelSize);
	    }
	  }
}