package farcore.lib.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.Direction;
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

@SideOnly(Side.CLIENT)
public abstract class RenderBase 
{
    private static final double RENDER_OFFSET = 0.001D;
    protected static final double PIXEL_LENGTH = 0.0625D;
    static final float LIGHT_Y_POS = 1.0F;

    public RenderBlocks render;
    public ForgeDirection dir = ForgeDirection.UNKNOWN;
    public IBlockAccess world;
    public int x, y, z;
    public IIcon icon;
    public Block block;
    public int meta;
    public boolean isItem = true;
    public int brightness = -1;
    public float rgb_red = 1.0F;
    public float rgb_green = 1.0F;
    public float rgb_blue = 1.0F;

    public RenderBase() {}

//    public boolean render(TileEntity tile)
//    {
//    	isItem = false;
//    	this.render = RenderBlocks.getInstance();
//    	render.blockAccess = this.world = tile.getWorldObj();
//    	this.x = tile.xCoord;
//    	this.y = tile.yCoord;
//    	this.z = tile.zCoord;
//    	this.meta = tile.getBlockMetadata();
//    	block = tile.getBlockType();
//    	init();
//    	render();
//    	return true;
//    }

    //World Based Rendering
    public boolean render(RenderBlocks render, IBlockAccess world, int x, int y, int z) 
    {
        isItem = false;
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
        this.render = render;
        this.block = block;
        this.meta = meta;
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
        rgb_red = (color >> 16 & 255) / 255.0F;
        rgb_green = (color >> 8 & 255) / 255.0F;
        rgb_blue = (color & 255) / 255.0F;
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
    
    protected void renderFace(
    		double x1, double y1, double z1, 
    		double x2, double y2, double z2, 
    		double x3, double y3, double z3, 
    		double x4, double y4, double z4)
    {
    	renderFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, 0F, 0F, 16F, 16F);
    }
    
    /**
     * Render a face with icon.
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @param x3
     * @param y3
     * @param z3
     * @param x4
     * @param y4
     * @param z4
     * @param u1
     * @param v1
     * @param u2
     * @param v2
     */
    protected void renderFace(
    		double x1, double y1, double z1, 
    		double x2, double y2, double z2, 
    		double x3, double y3, double z3, 
    		double x4, double y4, double z4,
    		double u1, double v1,
    		double u2, double v2) 
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

        tessellator.setColorOpaque_F(rgb_red, rgb_green, rgb_blue);
        double d4 = PIXEL_LENGTH;
        double d0 = iicon.getInterpolatedU(u1);
        double d1 = iicon.getInterpolatedV(v1);
        double d2 = iicon.getInterpolatedU(u2);
        double d3 = iicon.getInterpolatedV(v2);
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

        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
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
        float f = rgb_red;
        float f1 = rgb_green;
        float f2 = rgb_blue;

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

//    protected void renderAngledBlock(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) 
//    {
//        if (icon == null) return;
//        renderFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
//        renderFace(x1, y1 + height, z1, x2, y2 + height, z2, x3, y3 + height, z3, x4, y4 + height, z4);
//        renderFace(x1, y1, z1, x2, y2, z2, x2 + 1, y2 + height, z2, x1 + 1, y1 + height, z1);
//        renderFace(x4 - 1, y4, z4, x3 - 1, y3, z3, x3, y3 + height, z3, x4, y4 + height, z4);
//        renderFace(x1, y1, z1, x1, y1 + height, z1 - 1, x4, y4 + height, z4 - 1, x4, y4, z4);
//        renderFace(x2, y2, z2 + 1, x2, y2 + height, z2, x3, y3 + height, z3, x3, y3, z3 + 1);
//    }
    /**
     * Raw type, see RenderBox.
     * @param minX
     * @param minY
     * @param minZ
     * @param maxX
     * @param maxY
     * @param maxZ
     */
//    protected void renderBoxing(
//    		double x1, double y1, double z1, 
//    		double x2, double y2, double z2, 
//    		double x3, double y3, double z3, 
//    		double x4, double y4, double z4) 
//    {
//        if (icon == null) return;
//        renderFace(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
//        renderFace(x1, y1 + height, z1, x2, y2 + height, z2, x3, y3 + height, z3, x4, y4 + height, z4);
//        renderFace(x1, y1, z1, x2, y2, z2, x2 + 1, y2 + height, z2, x1 + 1, y1 + height, z1);
//        renderFace(x4 - 1, y4, z4, x3 - 1, y3, z3, x3, y3 + height, z3, x4, y4 + height, z4);
//        renderFace(x1, y1, z1, x1, y1 + height, z1 - 1, x4, y4 + height, z4 - 1, x4, y4, z4);
//        renderFace(x2, y2, z2 + 1, x2, y2 + height, z2, x3, y3 + height, z3, x3, y3, z3 + 1);
//    }

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
		boolean customIconOffset;
		Vec3 a, b, c, d, e, f;
		
		double x1, y1, z1,
		x2, y2, z2,
		x3, y3, z3,
		x4, y4, z4,
		x5, y5, z5,
		x6, y6, z6,
		x7, y7, z7,
		x8, y8, z8,
		ux1, vx1,
		ux2, vx2,
		ux3, vx3,
		ux4, vx4,
		uy1, vy1,
		uy2, vy2,
		uy3, vy3,
		uy4, vy4,
		uz1, vz1,
		uz2, vz2,
		uz3, vz3,
		uz4, vz4;
		
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
		public RenderBox(double aMinX, double aMinY, double aMinZ, double aMaxX, double aMaxY, double aMaxZ, double scale)
		{
			this(aMinX * scale, aMinY * scale, aMinZ * scale, aMaxX * scale, aMaxY * scale, aMaxZ * scale);
		}

		public RenderBox setIcon(IIcon...icons)
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
			return this;
		}
		public RenderBox setIcon(Block aBlock)
		{
			if(isItem())
			{
				return setIcon(aBlock, meta);
			}
			else
			{
				icon = new IIcon[]{aBlock.getIcon(world, x, y, z, 0), aBlock.getIcon(world, x, y, z, 1),
						aBlock.getIcon(world, x, y, z, 2), aBlock.getIcon(world, x, y, z, 3),
						aBlock.getIcon(world, x, y, z, 4), aBlock.getIcon(world, x, y, z, 5)};
				return this;
			}
		}
		public RenderBox setIcon(Block aBlock, int meta)
		{
			icon = new IIcon[]{aBlock.getIcon(0, meta), aBlock.getIcon(1, meta),
					aBlock.getIcon(2, meta), aBlock.getIcon(3, meta),
					aBlock.getIcon(4, meta), aBlock.getIcon(5, meta)};
			return this;
		}
		
		public RenderBox setOffset(double x, double y, double z)
		{
			offsetX = y;
			offsetY = z;
			offsetZ = x;
			return this;
		}
		
		public RenderBox setTranslate(double x, double y, double z)
		{
			translationX = x;
			translationY = y;
			translationZ = z;
			return this;
		}

		public RenderBox setRotation(double u)
		{
			return setRotation(u, 0, 0);
		}
		public RenderBox setRotation(double u, double w)
		{
			return setRotation(u, 0, w);
		}
		public RenderBox setRotation(double u, double v, double w)
		{
			rotationU = u;
			rotationV = v;
			rotationW = w;
			return this;
		}
		public RenderBox setRotation(double base, double u, double v, double w)
		{
			rotationU = u / base;
			rotationV = v / base;
			rotationW = w / base;
			return this;
		}
		
		public RenderBox setColor(float r, float g, float b)
		{
			rgb_blue = b;
			rgb_green = g;
			rgb_red = r;
			return this;
		}
		
		public RenderBox setCustomIconOffset(double uX, double vX, double uY, double vY, double uZ, double vZ)
		{
			return setCustomIconOffset(uX, vX, uX, vX, uY, vY, uY, vY, uZ, vZ, uZ, vZ);
		}
		
		public RenderBox setCustomIconOffset(double uX1, double vX1, double uX2, double vX2, double uY1, double vY1, double uY2, double vY2, double uZ1, double vZ1, double uZ2, double vZ2)
		{
			return setCustomIconOffset(uX1, vX1, uX2, vX2, uY1, vY1, uY2, vY2, uZ1, vZ1, uZ2, vZ2, (maxX - minX) * 16, (maxY - minY) * 16, (maxZ - minZ) * 16);
		}
		
		public RenderBox setCustomIconOffset(double uX1, double vX1, double uX2, double vX2, double uY1, double vY1, double uY2, double vY2, double uZ1, double vZ1, double uZ2, double vZ2, double xL, double yL, double zL)
		{
			return setCustomIconOffset(
					uX1, vX1, uX1 + xL, vX1 + yL, uX2, vX2, uX2 + xL, vX2 + yL, 
					uY1, vY1, uY1 + zL, vY1 + xL, uY2, vY2, uY2 + zL, vY2 + xL, 
					uZ1, vZ1, uZ1 + zL, vZ1 + yL, uZ2, vZ2, uZ2 + zL, vZ2 + yL);
		}

		public RenderBox setCustomIconOffset(
				double uX1, double vX1, double uX2, double vX2, //X Neg
				double uX3, double vX3, double uX4, double vX4, //X Pos
				double uY1, double vY1, double uY2, double vY2, //Y Neg
				double uY3, double vY3, double uY4, double vY4, //Y Pos
				double uZ1, double vZ1, double uZ2, double vZ2, //Z Neg
				double uZ3, double vZ3, double uZ4, double vZ4) //Z Pos
		{
			customIconOffset = true;
			this.ux1 = uX1;
			this.ux2 = uX2;
			this.ux3 = uX3;
			this.ux4 = uX4;
			this.uy1 = uY1;
			this.uy2 = uY2;
			this.uy3 = uY3;
			this.uy4 = uY4;
			this.uz1 = uZ1;
			this.uz2 = uZ2;
			this.uz3 = uZ3;
			this.uz4 = uZ4;
			this.vx1 = vX1;
			this.vx2 = vX2;
			this.vx3 = vX3;
			this.vx4 = vX4;
			this.vy1 = vY1;
			this.vy2 = vY2;
			this.vy3 = vY3;
			this.vy4 = vY4;
			this.vz1 = vZ1;
			this.vz2 = vZ2;
			this.vz3 = vZ3;
			this.vz4 = vZ4;
			return this;
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
		
		public RenderBox initRender()
		{
			setupVec3();
	    	a.rotateAroundX((float) (2 * Math.PI * rotationV));
	    	b.rotateAroundX((float) (2 * Math.PI * rotationV));
	    	c.rotateAroundX((float) (2 * Math.PI * rotationV));
	    	d.rotateAroundX((float) (2 * Math.PI * rotationV));
	    	e.rotateAroundX((float) (2 * Math.PI * rotationV));
	    	f.rotateAroundX((float) (2 * Math.PI * rotationV));
	    	a.rotateAroundZ((float) (2 * Math.PI * rotationW));
	    	b.rotateAroundZ((float) (2 * Math.PI * rotationW));
	    	c.rotateAroundZ((float) (2 * Math.PI * rotationW));
	    	d.rotateAroundZ((float) (2 * Math.PI * rotationW));
	    	e.rotateAroundZ((float) (2 * Math.PI * rotationW));
	    	f.rotateAroundZ((float) (2 * Math.PI * rotationW));
	    	a.rotateAroundY((float) (2 * Math.PI * rotationU));
	    	b.rotateAroundY((float) (2 * Math.PI * rotationU));
	    	c.rotateAroundY((float) (2 * Math.PI * rotationU));
	    	d.rotateAroundY((float) (2 * Math.PI * rotationU));
	    	e.rotateAroundY((float) (2 * Math.PI * rotationU));
	    	f.rotateAroundY((float) (2 * Math.PI * rotationU));
			x1 = translationX + a.xCoord + b.xCoord + c.xCoord;
			y1 = translationY + a.yCoord + b.yCoord + c.yCoord;
			z1 = translationZ + a.zCoord + b.zCoord + c.zCoord;
			x2 = translationX + d.xCoord + b.xCoord + c.xCoord;
			y2 = translationY + d.yCoord + b.yCoord + c.yCoord;
			z2 = translationZ + d.zCoord + b.zCoord + c.zCoord;
			x3 = translationX + d.xCoord + b.xCoord + f.xCoord;
			y3 = translationY + d.yCoord + b.yCoord + f.yCoord;
			z3 = translationZ + d.zCoord + b.zCoord + f.zCoord;
			x4 = translationX + a.xCoord + b.xCoord + f.xCoord;
			y4 = translationY + a.yCoord + b.yCoord + f.yCoord;
			z4 = translationZ + a.zCoord + b.zCoord + f.zCoord;
			x5 = translationX + a.xCoord + e.xCoord + c.xCoord;
			y5 = translationY + a.yCoord + e.yCoord + c.yCoord;
			z5 = translationZ + a.zCoord + e.zCoord + c.zCoord;
			x6 = translationX + d.xCoord + e.xCoord + c.xCoord;
			y6 = translationY + d.yCoord + e.yCoord + c.yCoord;
			z6 = translationZ + d.zCoord + e.zCoord + c.zCoord;
			x7 = translationX + d.xCoord + e.xCoord + f.xCoord;
			y7 = translationY + d.yCoord + e.yCoord + f.yCoord;
			z7 = translationZ + d.zCoord + e.zCoord + f.zCoord;
			x8 = translationX + a.xCoord + e.xCoord + f.xCoord;
			y8 = translationY + a.yCoord + e.yCoord + f.yCoord;
			z8 = translationZ + a.zCoord + e.zCoord + f.zCoord;
			if(!customIconOffset)
			{
				ux1 = minX;
				ux2 = maxX;
				uy1 = maxY;
				uy2 = minY;
				uz1 = minZ;
				uz2 = maxZ;
				vx1 = minX;
				vx2 = maxX;
				vy1 = maxY;
				vy2 = minY;
				vz1 = minZ;
				vz2 = maxZ;
			}
			return this;
		}
		
		public void render()
		{
			Tessellator tessellator = Tessellator.instance;

	        if(!isItem())
	        {
		        render.renderMinX = minX;
		        render.renderMinY = minY;
		        render.renderMinZ = minZ;
		        render.renderMaxX = maxX;
		        render.renderMaxY = maxY;
		        render.renderMaxZ = maxZ;
				renderFace(tessellator, 0, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, true, uy1, vy1, uy2, vy2);
				renderFace(tessellator, 1, x5, y5, z5, x6, y6, z6, x7, y7, z7, x8, y8, z8, true, uy3, vy3, uy4, vy4);
				renderFace(tessellator, 2, x5, y5, z5, x1, y1, z1, x2, y2, z2, x6, y6, z6, true, uz1, vz1, uz2, vz2);
				renderFace(tessellator, 3, x7, y7, z7, x3, y3, z3, x4, y4, z4, x8, y8, z8, true, uz3, vz3, uz4, vz4);
				renderFace(tessellator, 4, x6, y6, z6, x2, y2, z2, x3, y3, z3, x7, y7, z7, true, ux1, vx1, ux2, vx2);
				renderFace(tessellator, 5, x1, y1, z1, x5, y5, z5, x8, y8, z8, x4, y4, z4, true, ux1, vx2, ux2, vx1);
	        }
	        else
	        {
		        if (!isItem()) 
		        {
		            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		        }
		        if (brightness > -1) 
		        {
		            tessellator.setBrightness(brightness);
		        }
	        	GL11.glPushMatrix();
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
		        GL11.glPopMatrix();
	        }
		}
		
		private void renderFace(Tessellator tessellator, int iconID, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, boolean useRenderHelper, double u1, double v1, double u2, double v2) 
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

		        tessellator.setColorOpaque_F(rgb_red, rgb_green, rgb_blue);
	        }
	        double d0 = iicon.getInterpolatedU(u1);
	        double d1 = iicon.getInterpolatedV(v1);
	        double d2 = iicon.getInterpolatedU(u2);
	        double d3 = iicon.getInterpolatedV(v2);
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

	        tessellator.addVertexWithUV(d5, d13, d9 , d2, d1);
	        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
	        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
	        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
	        tessellator.addVertexWithUV(d8, d16, d12, d0, d1);
	        tessellator.addVertexWithUV(d7, d15, d11, d0, d3);
	        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
	        tessellator.addVertexWithUV(d5, d13, d9 , d2, d1);
		}
	}
}