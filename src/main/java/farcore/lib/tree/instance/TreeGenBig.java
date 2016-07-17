package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeGenAbstract;
import farcore.lib.tree.TreeInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TreeGenBig extends TreeGenAbstract
{
    static final byte[] otherCoordPairs = {2, 0, 0, 1, 2, 1};
    int[] basePos = new int[] {0, 0, 0};
    float generateCoreLeavesChance;
    Random rand;
    World worldObj;
    TreeInfo info;
    int heightLimit;
    int height;
    double heightAttenuation = 0.618D;
    double branchDensity = 1.0D;
    double branchSlope = 0.381D;
    double scaleWidth = 1.0D;
    double leafDensity = 1.0D;
    /** Sets the limit of the random value used to initialize the height limit. */
    int heightLimitLimit = 12;
    /** Sets the distance limit for how far away the generator will populate leaves from the base leaf node. */
    int leafDistanceLimit = 4;
    /** Contains a list of a points at which to generate groups of leaves. */
    int[][] leafNodes;

	public TreeGenBig(TreeBase tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
     */
    void generateLeafNodeList()
    {
        height = (int)(heightLimit * heightAttenuation);

        if (height >= heightLimit)
			height = heightLimit - 1;

        int i = (int)(1.382D + Math.pow(leafDensity * heightLimit / 13.0D, 2.0D));

        if (i < 1)
			i = 1;

        int[][] aint = new int[i * heightLimit][4];
        int j = basePos[1] + heightLimit - leafDistanceLimit;
        int k = 1;
        int l = basePos[1] + height;
        int i1 = j - basePos[1];
        aint[0][0] = basePos[0];
        aint[0][1] = j;
        aint[0][2] = basePos[2];
        aint[0][3] = l;
        --j;

        while (i1 >= 0)
        {
            int j1 = 0;
            float f = layerSize(i1);

            if (f < 0.0F)
            {
                --j;
                --i1;
            }
            else
            {
                for (double d0 = 0.5D; j1 < i; ++j1)
                {
                    double d1 = scaleWidth * f * (rand.nextFloat() + 0.328D);
                    double d2 = rand.nextFloat() * 2.0D * Math.PI;
                    int k1 = MathHelper.floor_double(d1 * Math.sin(d2) + basePos[0] + d0);
                    int l1 = MathHelper.floor_double(d1 * Math.cos(d2) + basePos[2] + d0);
                    int[] aint1 = new int[] {k1, j, l1};
                    int[] aint2 = new int[] {k1, j + leafDistanceLimit, l1};

                    if (checkBlockLine(aint1, aint2) == -1)
                    {
                        int[] aint3 = basePos.clone();
                        int v;
                        double d3 = Math.sqrt((v = Math.abs(basePos[0] - aint1[0])) * v + (v = Math.abs(basePos[2] - aint1[2])) * v);
                        double d4 = d3 * branchSlope;

                        if (aint1[1] - d4 > l)
							aint3[1] = l;
						else
							aint3[1] = (int)(aint1[1] - d4);

                        if (checkBlockLine(aint3, aint1) == -1)
                        {
                            aint[k][0] = k1;
                            aint[k][1] = j;
                            aint[k][2] = l1;
                            aint[k][3] = aint3[1];
                            ++k;
                        }
                    }
                }

                --j;
                --i1;
            }
        }

        leafNodes = new int[k][4];
        System.arraycopy(aint, 0, leafNodes, 0, k);
    }

    void genLeaves(int x, int y, int z, float scale, byte side)
    {
        int l = (int)(scale + 0.618D);
        byte b1 = otherCoordPairs[side];
        byte b2 = otherCoordPairs[side + 3];
        int[] aint = new int[] {x, y, z};
        int[] aint1 = new int[] {0, 0, 0};
        int i1 = -l;
        int j1 = -l;

        for (aint1[side] = aint[side]; i1 <= l; ++i1)
        {
            aint1[b1] = aint[b1] + i1;
            j1 = -l;

            while (j1 <= l)
            {
                double d0 = Math.pow(Math.abs(i1) + 0.5D, 2.0D) + Math.pow(Math.abs(j1) + 0.5D, 2.0D);

                if (d0 > scale * scale)
					++j1;
				else
                {
                    aint1[b2] = aint[b2] + j1;
                    ++j1;
                    if (isReplaceable(worldObj, aint1[0], aint1[1], aint1[2]))
						generateTreeLeaves(worldObj, aint1[0], aint1[1], aint1[2], 0, rand, info);
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    float layerSize(int layer)
    {
        if (layer < (heightLimit) * 0.3D)
			return -1.618F;
		else
        {
            float f = heightLimit / 2.0F;
            float f1 = heightLimit / 2.0F - layer;
            float f2;

            if (f1 == 0.0F)
				f2 = f;
			else if (Math.abs(f1) >= f)
				f2 = 0.0F;
			else
            {
            	float v;
                f2 = (float)Math.sqrt((v = Math.abs(f)) * v - (v = Math.abs(f1)) * v);
            }

            f2 *= 0.5F;
            return f2;
        }
    }

    float leafSize(int height)
    {
        return height >= 0 && height < leafDistanceLimit ?
        		(height != 0 && height != leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
    }

    /**
     * Generates the leaves surrounding an individual entry in the leafNodes list.
     */
    void generateLeafNode(int x, int y, int z)
    {
        int l = y;

        for (int i1 = y + leafDistanceLimit; l < i1; ++l)
        {
            float f = leafSize(l - y);
            genLeaves(x, l, z, f, (byte)1);
        }
    }

    void genLog(int[] node1, int[] node2)
    {
        int[] aint2 = new int[] {0, 0, 0};
        byte b0 = 0;
        byte b1;

        for (b1 = 0; b0 < 3; ++b0)
        {
            aint2[b0] = node2[b0] - node1[b0];

            if (Math.abs(aint2[b0]) > Math.abs(aint2[b1]))
				b1 = b0;
        }

        if (aint2[b1] != 0)
        {
            byte b2 = otherCoordPairs[b1];
            byte b3 = otherCoordPairs[b1 + 3];
            byte b4;

            if (aint2[b1] > 0)
				b4 = 1;
			else
				b4 = -1;

            double d0 = (double)aint2[b2] / (double)aint2[b1];
            double d1 = (double)aint2[b3] / (double)aint2[b1];
            int[] aint3 = new int[] {0, 0, 0};
            int i = 0;

            for (int j = aint2[b1] + b4; i != j; i += b4)
            {
                aint3[b1] = MathHelper.floor_double(node1[b1] + i + 0.5D);
                aint3[b2] = MathHelper.floor_double(node1[b2] + i * d0 + 0.5D);
                aint3[b3] = MathHelper.floor_double(node1[b3] + i * d1 + 0.5D);
                byte b5 = 0;
                int k = Math.abs(aint3[0] - node1[0]);
                int l = Math.abs(aint3[2] - node1[2]);
                int i1 = Math.max(k, l);

                if (i1 > 0)
					if (k == i1)
						b5 = 4;
					else if (l == i1)
						b5 = 8;

                generateLog(worldObj, aint3[0], aint3[1], aint3[2], b5);
            }
        }
    }

    /**
     * Generates the leaf portion of the tree as specified by the leafNodes list.
     */
    void generateLeaves()
    {
        int i = 0;

        for (int j = leafNodes.length; i < j; ++i)
        {
            int k = leafNodes[i][0];
            int l = leafNodes[i][1];
            int i1 = leafNodes[i][2];
            generateLeafNode(k, l, i1);
        }
    }

    /**
     * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
     */
    boolean leafNodeNeedsBase(int p_76493_1_)
    {
        return p_76493_1_ >= heightLimit * 0.2D;
    }

    /**
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    void generateTrunk()
    {
        int i = basePos[0];
        int j = basePos[1];
        int k = basePos[1] + height;
        int l = basePos[2];
        int[] aint = new int[] {i, j, l};
        int[] aint1 = new int[] {i, k, l};
        genLog(aint, aint1);

//        if (this.trunkSize == 2)
//        {
//            ++aint[0];
//            ++aint1[0];
//            this.genLog(aint, aint1);
//            ++aint[2];
//            ++aint1[2];
//            this.genLog(aint, aint1);
//            aint[0] += -1;
//            aint1[0] += -1;
//            this.genLog(aint, aint1);
//        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    void generateLeafNodeBases()
    {
        int i = 0;
        int j = leafNodes.length;

        for (int[] aint = basePos.clone(); i < j; ++i)
        {
            int[] aint1 = leafNodes[i];
            int[] aint2 = aint1.clone();
            aint[1] = aint1[3];
            int k = aint[1] - basePos[1];

            if (leafNodeNeedsBase(k))
				genLog(aint, aint2);
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
     * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
     */
    int checkBlockLine(int[] node1, int[] node2)
    {
        int[] aint2 = new int[] {0, 0, 0};
        byte b0 = 0;
        byte b1;

        for (b1 = 0; b0 < 3; ++b0)
        {
            aint2[b0] = node2[b0] - node1[b0];

            if (Math.abs(aint2[b0]) > Math.abs(aint2[b1]))
				b1 = b0;
        }

        if (aint2[b1] == 0)
			return -1;
		else
        {
            byte b2 = otherCoordPairs[b1];
            byte b3 = otherCoordPairs[b1 + 3];
            byte b4;

            if (aint2[b1] > 0)
				b4 = 1;
			else
				b4 = -1;

            double d0 = (double)aint2[b2] / (double)aint2[b1];
            double d1 = (double)aint2[b3] / (double)aint2[b1];
            int[] aint3 = new int[] {0, 0, 0};
            int i = 0;
            int j;

            for (j = aint2[b1] + b4; i != j; i += b4)
            {
                aint3[b1] = node1[b1] + i;
                aint3[b2] = MathHelper.floor_double(node1[b2] + i * d0);
                aint3[b3] = MathHelper.floor_double(node1[b3] + i * d1);
                Block block = worldObj.getBlock(aint3[0], aint3[1], aint3[2]);

                if (!isReplaceable(worldObj, aint3[0], aint3[1], aint3[2]))
					break;
            }

            return i == j ? -1 : Math.abs(i);
        }
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     */
    boolean validTreeLocation()
    {
        int[] aint = new int[] {basePos[0], basePos[1], basePos[2]};
        int[] aint1 = new int[] {basePos[0], basePos[1] + heightLimit - 1, basePos[2]};
        Block block = worldObj.getBlock(basePos[0], basePos[1] - 1, basePos[2]);

        boolean isSoil = block.canSustainPlant(worldObj, basePos[0], basePos[1] - 1, basePos[2], ForgeDirection.UP, (BlockSapling)Blocks.sapling);
        if (!isSoil)
			return false;
		else
        {
            int i = checkBlockLine(aint, aint1);

            if (i == -1)
				return true;
			else if (i < 6)
				return false;
			else
            {
                heightLimit = i;
                return true;
            }
        }
    }

    /**
     * Rescales the generator settings, only used in WorldGenBigTree
     */
    public void setScale(double height, double width, double density)
    {
        heightLimitLimit = (int)(height * 12.0D);

        if (height > 0.5D)
			leafDistanceLimit = 5;

        scaleWidth = width;
        leafDensity = density;
    }

	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
        worldObj = world;
        rand = random;
        this.info = info;
        basePos[0] = x;
        basePos[1] = y;
        basePos[2] = z;

        if (heightLimit == 0)
			heightLimit = 5 + rand.nextInt(heightLimitLimit);

        if (!validTreeLocation())
        {
            worldObj = null;
            return false;
        }
        else
        {
            generateLeafNodeList();
            generateLeaves();
            generateTrunk();
            generateLeafNodeBases();
            worldObj = null;
            return true;
        }
	}
}