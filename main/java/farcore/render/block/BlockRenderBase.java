package farcore.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class BlockRenderBase 
{
	protected static final double RENDER_OFFSET = 0.001D;
	static final float LIGHT_Y_POS = 1.0F;
	
	public RenderBlocks render;
	public ForgeDirection dir = ForgeDirection.UNKNOWN;
	public IBlockAccess world;
	public int x, y, z;
	public IIcon icon;
	public Block block;
	public int meta;
	public boolean isItem;
	public boolean isTESR;
	public int brightness = -1;
	public float R = 1.0F;
	public float G = 1.0F;
	public float B = 1.0F;
	
	public BlockRenderBase() {}
	
	//World Based Rendering
	public boolean render(RenderBlocks render, IBlockAccess world, int x, int y, int z) 
	{
		isItem = false;
		isTESR = false;
		this.render = render;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.meta = world.getBlockMetadata(x, y, z);
		block = world.getBlock(x, y, z);
		init();
		render();
		return true;
	}
	
	//Item Based Rendering
	public void render(RenderBlocks render, Block block, int meta) 
	{
		isItem = true;
		isTESR = false;
		this.render = render;
		this.block = block;
		this.meta = meta;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		render();
	}
	
	public boolean render() 
	{
		if (isItem()) 
		{
			GL11.glPushMatrix();
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.4F, -0.5F);
			GL11.glTranslatef(0F, -0.1F, 0F);
			renderBlock();
			GL11.glPopMatrix();
		}
		else
		{
			renderBlock();
			if (render.hasOverrideBlockTexture()) 
			{
				render.clearOverrideBlockTexture();
			}
		}
		
		return true;
	}
    
    public abstract void renderBlock();

    public void init() 
    {
        dir = ForgeDirection.NORTH;
    }

    public boolean isItem() 
    {
        return isItem;
    }
    
    public boolean isTESR()
    {
		return isTESR;
	}

    protected void setTexture(IIcon texture) 
    {
        icon = texture;
        if (!isItem()) 
        {
            render.setOverrideBlockTexture(texture);
        }
    }

    protected void setTexture(ItemStack stack) 
    {
        setTexture(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
    }

    protected void setTexture(Block block, int meta) 
    {
        setTexture(block.getIcon(0, meta));
    }

    protected void setTexture(Block block) 
    {
        setTexture(block, 0);
    }
    
    protected void setColor(int color)
    {
        R = (color >> 16 & 255) / 255.0F;
        G = (color >> 8 & 255) / 255.0F;
        B = (color & 255) / 255.0F;
    }

    //Can only be called by the in world renderer
    protected void renderColoredBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Block block) 
    {
        render.renderAllFaces = true;
        render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        render.renderStandardBlock(block, x, y, z);
        render.renderAllFaces = false;
    }

    protected void renderBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) 
    {
        if (isItem()) 
        {
            renderItemBlock(minX, minY, minZ, maxX, maxY, maxZ);
        } 
        else 
        {
            renderWorldBlock(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }
    
    protected void renderFace(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) 
    {
        //Diagonal Guessing
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = icon;
        if (!isItem()) 
        {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        }
        if (brightness > -1) 
        {
            tessellator.setBrightness(brightness);
        }

        tessellator.setColorOpaque_F(R, G, B);
        double d0 = iicon.getMinU();
        double d1 = iicon.getMinV();
        double d2 = iicon.getMaxU();
        double d3 = iicon.getMaxV();
        double d4 = 0.0625D;
        double d5 = x + 1 + x1;
        double d6 = x + 1 + x2;
        double d7 = x + 0 + x3;
        double d8 = x + 0 + x4;
        double d9 = z + 0 + z1;
        double d10 = z + 1 + z2;
        double d11 = z + 1 + z3;
        double d12 = z + 0 + z4;
        double d13 = y + d4 + y1;
        double d14 = y + d4 + y2;
        double d15 = y + d4 + y3;
        double d16 = y + d4 + y4;

        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
    }

    protected void renderSide(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4)
    {
        //Diagonal Guessing
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = icon;
        if (!isItem()) 
        {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        }
        else
        {
        	tessellator.startDrawingQuads();
        }
        if (brightness > -1) 
        {
            tessellator.setBrightness(brightness);
        }

        tessellator.setColorOpaque_F(R, G, B);
        double d0 = iicon.getMinU();
        double d1 = iicon.getMinV();
        double d2 = iicon.getMaxU();
        double d3 = iicon.getMaxV();
        double d5 = x + x1;
        double d6 = x + x2;
        double d7 = x + x3;
        double d8 = x + x4;
        double d9 = z + z1;
        double d10 = z + z2;
        double d11 = z + z3;
        double d12 = z + z4;
        double d13 = y + y1;
        double d14 = y + y2;
        double d15 = y + y3;
        double d16 = y + y4;

        tessellator.addVertexWithUV(d5, d13, d9 , d2, d1);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9 , d2, d1);
        if(isItem())
        {
        	tessellator.draw();
        }
    }

    protected void renderFluid(FluidStack fluid, int max, double scale, int xPlus, int yPlus, int zPlus) 
    {
    	renderFluid(fluid, max, scale, xPlus, yPlus, zPlus, 0.0D, 1.0D, 0.0D, 1.0D);
    }
    protected void renderFluid(FluidStack fluid, int max, double scale, int xPlus, int yPlus, int zPlus, double xPos1, double xPos2, double zPos1, double zPos2) 
    {
        if (fluid == null || fluid.getFluid() == null || fluid.getFluid().getIcon() == null) return;
        int x2 = x + xPlus;
        int y2 = y + yPlus;
        int z2 = z + zPlus;

        Tessellator tessellator = Tessellator.instance;
        int color = fluid.getFluid().getColor(fluid);
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, 1.0F);
        double extra = (double) fluid.amount / max * scale;
        double height = extra;
        IIcon iconStill = fluid.getFluid().getIcon();

        height += RENDER_OFFSET;

        double u1, u2, u3, u4, v1, v2, v3, v4;
        u2 = iconStill.getInterpolatedU(0.0D);
        v2 = iconStill.getInterpolatedV(0.0D);
        u1 = u2;
        v1 = iconStill.getInterpolatedV(16.0D);
        u4 = iconStill.getInterpolatedU(16.0D);
        v4 = v1;
        u3 = u4;
        v3 = v2;

        if (!isItem()) 
        {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        }
        if (brightness > -1) 
        {
            tessellator.setBrightness(brightness);
        }
        tessellator.setColorOpaque_F(LIGHT_Y_POS * red, LIGHT_Y_POS * green, LIGHT_Y_POS * blue);
        tessellator.addVertexWithUV(x2 + xPos1, y2 + height, z2 + zPos1, u2, v2);
        tessellator.addVertexWithUV(x2 + xPos1, y2 + height, z2 + zPos2, u1, v1);
        tessellator.addVertexWithUV(x2 + xPos2, y2 + height, z2 + zPos2, u4, v4);
        tessellator.addVertexWithUV(x2 + xPos2, y2 + height, z2 + zPos1, u3, v3);

        render.renderMinY = 0;
        render.renderMaxY = 1;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void renderFluidBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) 
    {
        render.renderAllFaces = true;
        render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        float f = R;
        float f1 = G;
        float f2 = B;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }
        if(Minecraft.isAmbientOcclusionEnabled() && Blocks.lava.getLightOpacity(world, x, y, z) == -1)
        	if(render.partialRenderBounds)
        		render.renderStandardBlockWithAmbientOcclusionPartial(Blocks.lava, x, y, z, f, f1, f2);
        	else
        		render.renderStandardBlockWithAmbientOcclusion(Blocks.lava, x, y, z, f, f1, f2);
        else
        	render.renderStandardBlockWithColorMultiplier(Blocks.lava, x, y, z, f, f1, f2);
        render.renderAllFaces = false;
    }

    private void renderWorldBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) 
    {
        render.renderAllFaces = true;
        render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        render.renderStandardBlock(block, x, y, z);
        render.renderAllFaces = false;
    }

    protected void renderAngledBlock(double x2, double y2, double z2, double x3, double y3, double z3, double x1, double y1, double z1, double x4, double y4, double z4) 
    {
        renderAngledBlock(x2, y2, z2, x3, y3, z3, x1, y1, z1, x4, y4, z4, 0D, 0D, 0D);
    }

    protected void renderAngledBlock(double x2, double y2, double z2, double x3, double y3, double z3, double x1, double y1, double z1, double x4, double y4, double z4, double xDim, double height, double zDim) 
    {
        if (icon == null) return;
        renderFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        renderFace(x1, y1 + height, z1, x2, y2 + height, z2, x3, y3 + height, z3, x4, y4 + height, z4);
        renderFace(x1, y1, z1, x2, y2, z2, x2 + 1, y2 + height, z2, x1 + 1, y1 + height, z1);
        renderFace(x4 - 1, y4, z4, x3 - 1, y3, z3, x3, y3 + height, z3, x4, y4 + height, z4);
        renderFace(x1, y1, z1, x1, y1 + height, z1 - 1, x4, y4 + height, z4 - 1, x4, y4, z4);
        renderFace(x2, y2, z2 + 1, x2, y2 + height, z2, x3, y3 + height, z3, x3, y3, z3 + 1);
    }

    private void renderItemBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) 
    {
        if (icon == null) return;
        render.renderMinX = minX;
        render.renderMinY = minY;
        render.renderMinZ = minZ;
        render.renderMaxX = maxX;
        render.renderMaxY = maxY;
        render.renderMaxZ = maxZ;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
    }

	protected void renderStandardBlock(Block block, int x, int y, int z) 
	{
		render.setOverrideBlockTexture(icon);
		render.renderStandardBlock(block, x, y, z);
		render.clearOverrideBlockTexture();
	}
	
	protected class RenderBox
	{
		IIcon[] icon = new IIcon[6];
		
		double minX, minY, minZ, maxX, maxY, maxZ;
		double offsetX, offsetY, offsetZ;
		double rotationU, rotationV, rotationW;
		double translationX, translationY, translationZ;
		Vec3 a, b, c, d, e, f;
		
		public RenderBox(double x, double y, double z) 
		{
			this(0D, 0D, 0D, x, y, z);
		}
		public RenderBox(double aMinX, double aMinY, double aMinZ, double aMaxX, double aMaxY, double aMaxZ)
		{
			minX = aMinX;
			minY = aMinY;
			minZ = aMinZ;
			maxX = aMaxX;
			maxY = aMaxY;
			maxZ = aMaxZ;
		}

		public void setIcon(IIcon...icons)
		{
			switch(icons.length)
			{
			case 1 : icon = new IIcon[]{icons[0], icons[0], icons[0], icons[0], icons[0], icons[0]};
			break;
			case 2 : icon = new IIcon[]{icons[0], icons[0], icons[1], icons[1], icons[1], icons[1]};
			break;
			case 3 : icon = new IIcon[]{icons[0], icons[0], icons[1], icons[2], icons[2], icons[2]};
			break;
			case 4 : icon = new IIcon[]{icons[0], icons[0], icons[1], icons[2], icons[3], icons[3]};
			break;
			case 5 : icon = new IIcon[]{icons[0], icons[1], icons[2], icons[3], icons[4], icons[4]};
			break;
			case 6 : icon = new IIcon[]{icons[0], icons[1], icons[2], icons[3], icons[4], icons[5]};
			break;
			default: throw new RuntimeException("Icon array out of bounds!");
			}
		}
		public void setIcon(Block aBlock)
		{
			if(isItem())
			{
				setIcon(aBlock, meta);
			}
			else
			{
				icon = new IIcon[]{aBlock.getIcon(world, x, y, z, 0), aBlock.getIcon(world, x, y, z, 1),
						aBlock.getIcon(world, x, y, z, 2), aBlock.getIcon(world, x, y, z, 3),
						aBlock.getIcon(world, x, y, z, 4), aBlock.getIcon(world, x, y, z, 5)};
			}
		}
		public void setIcon(Block aBlock, int meta)
		{
			icon = new IIcon[]{aBlock.getIcon(0, meta), aBlock.getIcon(1, meta),
					aBlock.getIcon(2, meta), aBlock.getIcon(3, meta),
					aBlock.getIcon(4, meta), aBlock.getIcon(5, meta)};
		}
		
		public void setOffset(double x, double y, double z)
		{
			offsetX = y;
			offsetY = z;
			offsetZ = x;
		}
		
		public void setTranslate(double x, double y, double z)
		{
			translationX = x;
			translationY = y;
			translationZ = z;
		}

		public void setRotation(double u)
		{
			rotationU = u;
			rotationV = 0D;
			rotationW = 0D;
		}
		public void setRotation(double u, double w)
		{
			rotationU = u;
			rotationV = u;
			rotationW = w;
		}
		public void setRotation(double u, double v, double w)
		{
			rotationU = u;
			rotationV = v;
			rotationW = w;
		}
		public void setRotation(double base, double u, double v, double w)
		{
			rotationU = u / base;
			rotationV = v / base;
			rotationW = w / base;
		}
		
		public void setColor(float r, float g, float b)
		{
			B = b;
			G = g;
			R = r;
		}
		
		public void setupVec3()
		{
			a = Vec3.createVectorHelper(minX + offsetX, 0, 0);
			b = Vec3.createVectorHelper(0, minY + offsetY, 0);
			c = Vec3.createVectorHelper(0, 0, minZ + offsetZ);
			d = Vec3.createVectorHelper(maxX + offsetX, 0, 0);
			e = Vec3.createVectorHelper(0, maxY + offsetY, 0);
			f = Vec3.createVectorHelper(0, 0, maxZ + offsetZ);
		}
		
		public void render()
		{
			Tessellator tessellator = Tessellator.instance;
			
			if (!isItem()) 
			{
				tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
			}
			if (brightness > -1) 
			{
				tessellator.setBrightness(brightness);
			}
			if(!isItem())
			{
				setupVec3();
				a.rotateAroundX((float) (2 * Math.PI * rotationV));
				b.rotateAroundX((float) (2 * Math.PI * rotationV));
				c.rotateAroundX((float) (2 * Math.PI * rotationV));
				d.rotateAroundX((float) (2 * Math.PI * rotationV));
				e.rotateAroundX((float) (2 * Math.PI * rotationV));
				f.rotateAroundX((float) (2 * Math.PI * rotationV));
				a.rotateAroundY((float) (2 * Math.PI * rotationU));
				b.rotateAroundY((float) (2 * Math.PI * rotationU));
				c.rotateAroundY((float) (2 * Math.PI * rotationU));
				d.rotateAroundY((float) (2 * Math.PI * rotationU));
				e.rotateAroundY((float) (2 * Math.PI * rotationU));
				f.rotateAroundY((float) (2 * Math.PI * rotationU));
				a.rotateAroundZ((float) (2 * Math.PI * rotationW));
				b.rotateAroundZ((float) (2 * Math.PI * rotationW));
				c.rotateAroundZ((float) (2 * Math.PI * rotationW));
				d.rotateAroundZ((float) (2 * Math.PI * rotationW));
				e.rotateAroundZ((float) (2 * Math.PI * rotationW));
				f.rotateAroundZ((float) (2 * Math.PI * rotationW));
				double x1_1 = translationX + a.xCoord + b.xCoord + c.xCoord;
				double y1_1 = translationY + a.yCoord + b.yCoord + c.yCoord;
				double z1_1 = translationZ + a.zCoord + b.zCoord + c.zCoord;
				double x2_1 = translationX + d.xCoord + b.xCoord + c.xCoord;
				double y2_1 = translationY + d.yCoord + b.yCoord + c.yCoord;
				double z2_1 = translationZ + d.zCoord + b.zCoord + c.zCoord;
				double x3_1 = translationX + d.xCoord + b.xCoord + f.xCoord;
				double y3_1 = translationY + d.yCoord + b.yCoord + f.yCoord;
				double z3_1 = translationZ + d.zCoord + b.zCoord + f.zCoord;
				double x4_1 = translationX + a.xCoord + b.xCoord + f.xCoord;
				double y4_1 = translationY + a.yCoord + b.yCoord + f.yCoord;
				double z4_1 = translationZ + a.zCoord + b.zCoord + f.zCoord;
				double x5_1 = translationX + a.xCoord + e.xCoord + c.xCoord;
				double y5_1 = translationY + a.yCoord + e.yCoord + c.yCoord;
				double z5_1 = translationZ + a.zCoord + e.zCoord + c.zCoord;
				double x6_1 = translationX + d.xCoord + e.xCoord + c.xCoord;
				double y6_1 = translationY + d.yCoord + e.yCoord + c.yCoord;
				double z6_1 = translationZ + d.zCoord + e.zCoord + c.zCoord;
				double x7_1 = translationX + d.xCoord + e.xCoord + f.xCoord;
				double y7_1 = translationY + d.yCoord + e.yCoord + f.yCoord;
				double z7_1 = translationZ + d.zCoord + e.zCoord + f.zCoord;
				double x8_1 = translationX + a.xCoord + e.xCoord + f.xCoord;
				double y8_1 = translationY + a.yCoord + e.yCoord + f.yCoord;
				double z8_1 = translationZ + a.zCoord + e.zCoord + f.zCoord;
				renderFace(tessellator, 0, 
						x1_1, y1_1, z1_1, 
						x2_1, y2_1, z2_1, 
						x3_1, y3_1, z3_1, 
						x4_1, y4_1, z4_1, false);
				renderFace(tessellator, 1, 
						x5_1, y5_1, z5_1, 
						x6_1, y6_1, z6_1, 
						x7_1, y7_1, z7_1, 
						x8_1, y8_1, z8_1, false);
				renderFace(tessellator, 2, 
						x1_1, y1_1, z1_1, 
						x2_1, y2_1, z2_1, 
						x6_1, y6_1, z6_1, 
						x5_1, y5_1, z5_1, false);
				renderFace(tessellator, 3, 
						x3_1, y3_1, z3_1, 
						x4_1, y4_1, z4_1, 
						x8_1, y8_1, z8_1, 
						x7_1, y7_1, z7_1, false);
				renderFace(tessellator, 4,
						x2_1, y2_1, z2_1,
						x3_1, y3_1, z3_1,
						x7_1, y7_1, z7_1,
						x6_1, y6_1, z6_1, false);
				renderFace(tessellator, 5,
						x4_1, y4_1, z4_1,
						x1_1, y1_1, z1_1,
						x5_1, y5_1, z5_1,
						x8_1, y8_1, z8_1, false);
			}
	        else
	        {
	        	GL11.glTranslated(translationX, translationY, translationZ);
	        	GL11.glRotated(1.0D, rotationV, rotationU, rotationW);
	        	GL11.glTranslated(offsetX, offsetY, offsetZ);
	        	render.renderMinX = minX;
	        	render.renderMinY = minY;
	        	render.renderMinZ = minZ;
	        	render.renderMaxX = maxX;
	        	render.renderMaxY = maxY;
	        	render.renderMaxZ = maxZ;
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, -1.0F, 0.0F);
		        render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon[0]);
		        tessellator.draw();
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, 1.0F, 0.0F);
		        render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon[1]);
		        tessellator.draw();
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, 0.0F, -1.0F);
		        render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon[2]);
		        tessellator.draw();
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(0.0F, 0.0F, 1.0F);
		        render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon[3]);
		        tessellator.draw();
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		        render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon[4]);
		        tessellator.draw();
		        tessellator.startDrawingQuads();
		        tessellator.setNormal(1.0F, 0.0F, 0.0F);
		        render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon[5]);
		        tessellator.draw();
	        	GL11.glTranslated(-offsetX, -offsetY, -offsetZ);
	        	GL11.glRotated(1.0D, -rotationV, -rotationU, -rotationW);
	        	GL11.glTranslated(-translationX, -translationY, -translationZ);
	        }
		}
		
		private void renderFace(Tessellator tessellator, int iconID, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, boolean useRenderHelper)
		{
	        //Diagonal Guessing
	        IIcon iicon = icon[iconID];
	        if(useRenderHelper)
	        {
		        if (!isItem()) 
		        {
		            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		        }
		        if (brightness > -1) 
		        {
		            tessellator.setBrightness(brightness);
		        }

		        tessellator.setColorOpaque_F(R, G, B);
	        }
	        double d0 = iicon.getMinU();
	        double d1 = iicon.getMinV();
	        double d2 = iicon.getMaxU();
	        double d3 = iicon.getMaxV();
	        double d5 = x + x1;
	        double d6 = x + x2 + RENDER_OFFSET;
	        double d7 = x + x3 + RENDER_OFFSET;
	        double d8 = x + x4;
	        double d13 = y + y1;
	        double d14 = y + y2 + RENDER_OFFSET;
	        double d15 = y + y3 + RENDER_OFFSET;
	        double d16 = y + y4;
	        double d9 = z + z1;
	        double d10 = z + z2 + RENDER_OFFSET;
	        double d11 = z + z3 + RENDER_OFFSET;
	        double d12 = z + z4;

	        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
	        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
	        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
	        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
	        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
	        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
	        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
	        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
	        render.renderMinX = minX;
	        render.renderMinY = minY;
	        render.renderMinZ = minZ;
	        render.renderMaxX = maxX;
	        render.renderMaxY = maxY;
	        render.renderMaxZ = maxZ;
		}
	}
}