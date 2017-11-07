package nebula.client.model;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Point3f;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import nebula.client.util.IModelModifier;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemTextureHelper
{
	private static float	zPos	= 8.5F;
	private static float	zNeg	= 7.5F;
	private static float	red		= 1F;
	private static float	green	= 1F;
	private static float	blue	= 1F;
	private static float	alpha	= 1F;
	private static int		tint	= -1;
	
	private static void setZLevel(float zLevel)
	{
		zPos = (8.0F + zLevel) / 16F;
		zNeg = (8.0F - zLevel) / 16F;
	}
	
	public static void setColor(int color)
	{
		red = ((color >> 24) & 0xFF) / 255F;
		green = ((color >> 16) & 0xFF) / 255F;
		blue = ((color >> 8) & 0xFF) / 255F;
		alpha = (color & 0xFF) / 255F;
	}
	
	public static void setColor(float r, float g, float b, float a)
	{
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
	
	public static ImmutableList<BakedQuad> getQuadsForSprite(int tint, TextureAtlasSprite sprite, VertexFormat format, IModelModifier transform)
	{
		return getQuadsForSprite(tint, sprite, format, transform, 0.5F);
	}
	
	public static ImmutableList<BakedQuad> getQuadsForSprite(int tint, TextureAtlasSprite sprite, VertexFormat format, IModelModifier transform, float zLevel)
	{
		setZLevel(zLevel);
		
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		
		int uMax = sprite.getIconWidth();
		int vMax = sprite.getIconHeight();
		
		BitSet faces = new BitSet((uMax + 1) * (vMax + 1) * 4);
		for (int f = 0; f < sprite.getFrameCount(); f++)
		{
			int[] pixels = sprite.getFrameTextureData(f)[0];
			boolean ptu;
			boolean[] ptv = new boolean[uMax];
			Arrays.fill(ptv, true);
			for (int v = 0; v < vMax; v++)
			{
				ptu = true;
				for (int u = 0; u < uMax; u++)
				{
					boolean t = isTransparent(pixels, uMax, vMax, u, v);
					if (ptu && !t) // left - transparent, right - opaque
					{
						addSideQuad(builder, faces, format, transform, EnumFacing.WEST, tint, sprite, uMax, vMax, u, v);
					}
					if (!ptu && t) // left - opaque, right - transparent
					{
						addSideQuad(builder, faces, format, transform, EnumFacing.EAST, tint, sprite, uMax, vMax, u, v);
					}
					if (ptv[u] && !t) // up - transparent, down - opaque
					{
						addSideQuad(builder, faces, format, transform, EnumFacing.UP, tint, sprite, uMax, vMax, u, v);
					}
					if (!ptv[u] && t) // up - opaque, down - transparent
					{
						addSideQuad(builder, faces, format, transform, EnumFacing.DOWN, tint, sprite, uMax, vMax, u, v);
					}
					ptu = t;
					ptv[u] = t;
				}
				if (!ptu) // last - opaque
				{
					addSideQuad(builder, faces, format, transform, EnumFacing.EAST, tint, sprite, uMax, vMax, uMax, v);
				}
			}
			// last line
			for (int u = 0; u < uMax; u++)
			{
				if (!ptv[u])
				{
					addSideQuad(builder, faces, format, transform, EnumFacing.DOWN, tint, sprite, uMax, vMax, u, vMax);
				}
			}
		}
		// front
		builder.add(buildQuad(format, transform, EnumFacing.NORTH, sprite, tint, 0, 0, zNeg, sprite.getMinU(), sprite.getMaxV(), 0, 1, zNeg, sprite.getMinU(), sprite.getMinV(), 1, 1, zNeg, sprite.getMaxU(), sprite.getMinV(), 1, 0, zNeg, sprite.getMaxU(), sprite.getMaxV()));
		// back
		builder.add(buildQuad(format, transform, EnumFacing.SOUTH, sprite, tint, 0, 0, zPos, sprite.getMinU(), sprite.getMaxV(), 1, 0, zPos, sprite.getMaxU(), sprite.getMaxV(), 1, 1, zPos, sprite.getMaxU(), sprite.getMinV(), 0, 1, zPos, sprite.getMinU(), sprite.getMinV()));
		return builder.build();
	}
	
	private static boolean isTransparent(int[] pixels, int uMax, int vMax, int u, int v)
	{
		return (pixels[u + (vMax - 1 - v) * uMax] >> 24 & 0xFF) == 0;
	}
	
	private static void addSideQuad(ImmutableList.Builder<BakedQuad> builder, BitSet faces, VertexFormat format, IModelModifier transform, EnumFacing side, int tint, TextureAtlasSprite sprite, int uMax, int vMax, int u, int v)
	{
		int si = side.ordinal();
		if (si > 4)
		{
			si -= 2;
		}
		int index = (vMax + 1) * ((uMax + 1) * si + u) + v;
		if (!faces.get(index))
		{
			faces.set(index);
			builder.add(buildSideQuad(format, transform, side, tint, sprite, u, v));
		}
	}
	
	private static BakedQuad buildSideQuad(VertexFormat format, IModelModifier transform, EnumFacing side, int tint, TextureAtlasSprite sprite, int u, int v)
	{
		final float eps0 = 30e-5f;
		final float eps1 = 45e-5f;
		final float eps2 = .5f;
		final float eps3 = .5f;
		float x0 = (float) u / sprite.getIconWidth();
		float y0 = (float) v / sprite.getIconHeight();
		float x1 = x0, y1 = y0;
		float z1 = zNeg - eps1, z2 = zPos + eps1;
		switch (side)
		{
		case WEST:
			z1 = zPos + eps1;
			z2 = zNeg - eps1;
		case EAST:
			y1 = (v + 1f) / sprite.getIconHeight();
			break;
		case DOWN:
			z1 = zPos + eps1;
			z2 = zNeg - eps1;
		case UP:
			x1 = (u + 1f) / sprite.getIconWidth();
			break;
		default:
			throw new IllegalArgumentException("can't handle z-oriented side");
		}
		float u0 = 16f * (x0 - side.getDirectionVec().getX() * eps3 / sprite.getIconWidth());
		float u1 = 16f * (x1 - side.getDirectionVec().getX() * eps3 / sprite.getIconWidth());
		float v0 = 16f * (1f - y0 - side.getDirectionVec().getY() * eps3 / sprite.getIconHeight());
		float v1 = 16f * (1f - y1 - side.getDirectionVec().getY() * eps3 / sprite.getIconHeight());
		switch (side)
		{
		case WEST:
		case EAST:
			y0 -= eps1;
			y1 += eps1;
			v0 -= eps2 / sprite.getIconHeight();
			v1 += eps2 / sprite.getIconHeight();
			break;
		case DOWN:
		case UP:
			x0 -= eps1;
			x1 += eps1;
			u0 += eps2 / sprite.getIconWidth();
			u1 -= eps2 / sprite.getIconWidth();
			break;
		default:
			throw new IllegalArgumentException("can't handle z-oriented side");
		}
		switch (side)
		{
		case WEST:
			x0 += eps0;
			x1 += eps0;
			break;
		case EAST:
			x0 -= eps0;
			x1 -= eps0;
			break;
		case DOWN:
			y0 -= eps0;
			y1 -= eps0;
			break;
		case UP:
			y0 += eps0;
			y1 += eps0;
			break;
		default:
			throw new IllegalArgumentException("can't handle z-oriented side");
		}
		return buildQuad(format, transform, side.getOpposite(), sprite, tint, // getOpposite
																				 // is
																				 // related
																				 // either
																				 // to
																				 // the
																				 // swapping
																				 // of
																				 // V
																				 // direction,
																				 // or
																				 // something
																				 // else
				x0, y0, z1, sprite.getInterpolatedU(u0), sprite.getInterpolatedV(v0), x1, y1, z1, sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v1), x1, y1, z2, sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v1), x0, y0, z2, sprite.getInterpolatedU(u0), sprite.getInterpolatedV(v0));
	}
	
	public static final BakedQuad buildQuad(VertexFormat format, IModelModifier transform, EnumFacing side, TextureAtlasSprite sprite, int tint, float x0, float y0, float z0, float u0, float v0, float x1, float y1, float z1, float u1, float v1, float x2, float y2, float z2, float u2, float v2,
			float x3, float y3, float z3, float u3, float v3)
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		builder.setQuadTint(tint);
		builder.setQuadOrientation(side);
		builder.setTexture(sprite);
		putVertex(builder, format, transform, side, x0, y0, z0, u0, v0);
		putVertex(builder, format, transform, side, x1, y1, z1, u1, v1);
		putVertex(builder, format, transform, side, x2, y2, z2, u2, v2);
		putVertex(builder, format, transform, side, x3, y3, z3, u3, v3);
		return builder.build();
	}
	
	public static final BakedQuad buildBlockQuad(VertexFormat format, IModelModifier transform, @Nullable EnumFacing side, EnumFacing normal, TextureAtlasSprite sprite, int tint, float x0, float y0, float z0, float u0, float v0, float x1, float y1, float z1, float u1, float v1, float x2, float y2,
			float z2, float u2, float v2, float x3, float y3, float z3, float u3, float v3)
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		builder.setQuadTint(tint);
		builder.setQuadOrientation(side);
		builder.setTexture(sprite);
		putVertex(builder, format, transform, normal, x0, y0, z0, u0, v0);
		putVertex(builder, format, transform, normal, x1, y1, z1, u1, v1);
		putVertex(builder, format, transform, normal, x2, y2, z2, u2, v2);
		putVertex(builder, format, transform, normal, x3, y3, z3, u3, v3);
		return builder.build();
	}
	
	private static void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, IModelModifier transform, EnumFacing side, float x, float y, float z, float u, float v)
	{
		Point3f point = new Point3f();
		point.x = x;
		point.y = y;
		point.z = z;
		transform.transform(point);
		for (int e = 0; e < format.getElementCount(); e++)
		{
			switch (format.getElement(e).getUsage())
			{
			case POSITION:
				builder.put(e, point.x, point.y, point.z, 1);
				break;
			case COLOR:
				builder.put(e, red, green, blue, alpha);
				break;
			case UV:
				if (format.getElement(e).getIndex() == 0)
				{
					builder.put(e, u, v, 0, 1);
					break;
				}
			case NORMAL:
				builder.put(e, side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ(), 0f);
				break;
			default:
				builder.put(e);
				break;
			}
		}
	}
	
	/**
	 * Takes a texture and converts it into BakedQuads. The conversion is done
	 * by scanning the texture horizontally and vertically and creating "strips"
	 * of the texture. Strips that are of the same size and follow each other
	 * are converted into one bigger quad. </br>
	 * The resulting list of quads is the texture represented as a list of
	 * horizontal OR vertical quads, depending on which creates less quads. If
	 * the amount of quads is equal, horizontal is preferred.
	 *
	 * @param format
	 * @param template The input texture to convert
	 * @param sprite The texture whose UVs shall be used @return The generated
	 *            quads.
	 */
	public static List<UnpackedBakedQuad> convertTexture(VertexFormat format, IModelModifier transform, TextureAtlasSprite template, TextureAtlasSprite sprite, float z, EnumFacing facing, int tint)
	{
		ItemTextureHelper.tint = tint;
		
		List<UnpackedBakedQuad> horizontal = convertTextureHorizontal(format, transform, template, sprite, z, facing);
		List<UnpackedBakedQuad> vertical = convertTextureVertical(format, transform, template, sprite, z, facing);
		
		return horizontal.size() >= vertical.size() ? horizontal : vertical;
	}
	
	/**
	 * Scans a texture and converts it into a list of horizontal strips stacked
	 * on top of each other. The height of the strips is as big as possible.
	 */
	public static List<UnpackedBakedQuad> convertTextureHorizontal(VertexFormat format, IModelModifier transform, TextureAtlasSprite template, TextureAtlasSprite sprite, float z, EnumFacing facing)
	{
		int w = template.getIconWidth();
		int h = template.getIconHeight();
		float wScale = 16f / w;
		float hScale = 16f / h;
		int[] data = template.getFrameTextureData(0)[0];
		List<UnpackedBakedQuad> quads = Lists.newArrayList();
		
		// the upper left x-position of the current quad
		int start = -1;
		for (int y = 0; y < h; y++)
		{
			for (int x = 0; x < w; x++)
			{
				// current pixel
				int pixel = data[y * w + x];
				
				// no current quad but found a new one
				if (start < 0 && isVisible(pixel))
				{
					start = x;
				}
				// got a current quad, but it ends here
				if (start >= 0 && !isVisible(pixel))
				{
					// we now check if the visibility of the next row matches
					// the one fo the current row
					// if they are, we can extend the quad downwards
					int endY = y + 1;
					boolean sameRow = true;
					while (sameRow)
					{
						for (int i = 0; i < w; i++)
						{
							int px1 = data[y * w + i];
							int px2 = data[endY * w + i];
							if (isVisible(px1) != isVisible(px2))
							{
								sameRow = false;
								break;
							}
						}
						if (sameRow)
						{
							endY++;
						}
					}
					
					// create the quad
					quads.add(genQuad(format, transform, start * wScale, y * hScale, x * wScale, endY * hScale, z, sprite, facing, -1));
					
					// update Y if all the rows match. no need to rescan
					if (endY - y > 1)
					{
						y = endY - 1;
					}
					// clear current quad
					start = -1;
				}
			}
		}
		
		return quads;
	}
	
	/**
	 * Scans a texture and converts it into a list of vertical strips stacked
	 * next to each other from left to right. The width of the strips is as big
	 * as possible.
	 */
	public static List<UnpackedBakedQuad> convertTextureVertical(VertexFormat format, IModelModifier transform, TextureAtlasSprite template, TextureAtlasSprite sprite, float z, EnumFacing facing)
	{
		int w = template.getIconWidth();
		int h = template.getIconHeight();
		float wScale = 16f / w;
		float hScale = 16f / h;
		int[] data = template.getFrameTextureData(0)[0];
		List<UnpackedBakedQuad> quads = Lists.newArrayList();
		
		// the upper left y-position of the current quad
		int start = -1;
		for (int x = 0; x < w; x++)
		{
			for (int y = 0; y < h; y++)
			{
				// current pixel
				int pixel = data[y * w + x];
				
				// no current quad but found a new one
				if (start < 0 && isVisible(pixel))
				{
					start = y;
				}
				// got a current quad, but it ends here
				if (start >= 0 && !isVisible(pixel))
				{
					// we now check if the visibility of the next column matches
					// the one fo the current row
					// if they are, we can extend the quad downwards
					int endX = x + 1;
					boolean sameColumn = true;
					while (sameColumn)
					{
						for (int i = 0; i < h; i++)
						{
							int px1 = data[i * w + x];
							int px2 = data[i * w + endX];
							if (isVisible(px1) != isVisible(px2))
							{
								sameColumn = false;
								break;
							}
						}
						if (sameColumn)
						{
							endX++;
						}
					}
					
					// create the quad
					quads.add(genQuad(format, transform, x * wScale, start * hScale, endX * wScale, y * hScale, z, sprite, facing, pixel));// Blind
																																			// convert
																																			// color.
					
					// update X if all the columns match. no need to rescan
					if (endX - x > 1)
					{
						x = endX - 1;
					}
					// clear current quad
					start = -1;
				}
			}
		}
		
		return quads;
	}
	
	// true if alpha != 0
	private static boolean isVisible(int color)
	{
		return (color >> 24 & 255) > 0;
	}
	
	public static List<BakedQuad> getSurfaceQuadsForSprite(int tint, VertexFormat format, IModelModifier transform, float z, TextureAtlasSprite sprite)
	{
		setZLevel(z);
		return ImmutableList.of(genQuad(format, transform, 0, 0, 16, 16, zNeg, sprite, EnumFacing.NORTH, tint), genQuad(format, transform, 0, 0, 16, 16, zPos, sprite, EnumFacing.SOUTH, tint));
	}
	
	/**
	 * Generates a Front/Back quad for an itemmodel. Therefore only supports
	 * facing NORTH and SOUTH. Coordinates are [0,16] to match the usual
	 * coordinates used in TextureAtlasSprites
	 */
	public static UnpackedBakedQuad genQuad(VertexFormat format, IModelModifier transform, float x1, float y1, float x2, float y2, float z, TextureAtlasSprite sprite, EnumFacing facing, int color)
	{
		float u1 = sprite.getInterpolatedU(x1);
		float v1 = sprite.getInterpolatedV(y1);
		float u2 = sprite.getInterpolatedU(x2);
		float v2 = sprite.getInterpolatedV(y2);
		
		x1 /= 16f;
		y1 /= 16f;
		x2 /= 16f;
		y2 /= 16f;
		
		float tmp = y1;
		y1 = 1f - y2;
		y2 = 1f - tmp;
		
		return putQuad(format, transform, facing, sprite, x1, y1, x2, y2, z, u1, v1, u2, v2, color);
	}
	
	private static UnpackedBakedQuad putQuad(VertexFormat format, IModelModifier transform, EnumFacing side, TextureAtlasSprite sprite, float x1, float y1, float x2, float y2, float z, float u1, float v1, float u2, float v2, int color)
	{
		side = side.getOpposite();
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		builder.setQuadTint(tint);
		builder.setQuadOrientation(side);
		builder.setTexture(sprite);
		
		if (side == EnumFacing.NORTH)
		{
			putVertex(builder, format, transform, side, x1, y1, z, u1, v2, color);
			putVertex(builder, format, transform, side, x2, y1, z, u2, v2, color);
			putVertex(builder, format, transform, side, x2, y2, z, u2, v1, color);
			putVertex(builder, format, transform, side, x1, y2, z, u1, v1, color);
		}
		else
		{
			putVertex(builder, format, transform, side, x1, y1, z, u1, v2, color);
			putVertex(builder, format, transform, side, x1, y2, z, u1, v1, color);
			putVertex(builder, format, transform, side, x2, y2, z, u2, v1, color);
			putVertex(builder, format, transform, side, x2, y1, z, u2, v2, color);
		}
		return builder.build();
	}
	
	private static void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, IModelModifier transform, EnumFacing side, float x, float y, float z, float u, float v, int color)
	{
		Point3f point = new Point3f();
		point.x = x;
		point.y = y;
		point.z = z;
		transform.transform(point);
		for (int e = 0; e < format.getElementCount(); e++)
		{
			switch (format.getElement(e).getUsage())
			{
			case POSITION:
				builder.put(e, point.x, point.y, point.z, 1);
				break;
			case COLOR:
				float a = alpha * ((color >> 24) & 0xFF) / 255f; // alpha
				float r = red * ((color >> 16) & 0xFF) / 255f; // red
				float g = green * ((color >> 8) & 0xFF) / 255f; // green
				float b = blue * ((color >> 0) & 0xFF) / 255f; // blue
				builder.put(e, r, g, b, a);
				break;
			case UV:
				if (format.getElement(e).getIndex() == 0)
				{
					builder.put(e, u, v, 0f, 1f);
					break;
				}
			case NORMAL:
				builder.put(e, side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ(), 0f);
				break;
			default:
				builder.put(e);
				break;
			}
		}
	}
}
