package model;

import java.util.*;

/**
 * Represents a Quadtree node in the tree for an image compressed using the
 * Rich Image Tool file format.
 *
 * A node contains a value which is either a grayscale color (0-255) for a
 * region, or QTree.QUAD_SPLIT meaning this node cannot hold a single color
 * and thus has split itself into 4 sub-regions.
 *
 * @author Sean Strout @ RIT
 */
public class RITQTNode {
    /** The node's value */
    private int val;
    private int size;

    /** quadrant II */
    private RITQTNode ul;

    /** quadrant I */
    private RITQTNode ur;

    /** quadrant III */
    private RITQTNode ll;

    /** quadrant IV */
    private RITQTNode lr;

    /**
     * Construct a leaf node with no children.
     * @param val node value
     */
    public RITQTNode(int val) {
        this(val, null, null, null, null);
    }

    /**
     * Construct a quad tree node.
     *
     * @param val the node's value
     * @param ul the upper left sub-node
     * @param ur the upper right sub-node
     * @param ll the lower left sub-node
     * @param lr the lower right sub-node
     */
    public RITQTNode(int val, RITQTNode ul, RITQTNode ur, RITQTNode ll, RITQTNode lr) {
        this.val = val;
        this.ul = ul;
        this.ur = ur;
        this.ll = ll;
        this.lr = lr;
    }

    /**
     * Get the node's value.
     *
     * @return node's value
     */
    public int getVal() { return this.val; }

    /**
     * Set the tree size
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Get the current tree size
     * @return node's size
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the upper left sub-node.
     *
     * @return upper left sub-node
     */
    public RITQTNode getUpperLeft() { return this.ul; }

    /**
     * Get the upper right sub-node.
     *
     * @return upper right sub-node
     */
    public RITQTNode getUpperRight() { return this.ur; }

    /**
     * Get the lower left sub-node.
     *
     * @return lower left sub-node
     */
    public RITQTNode getLowerLeft() { return this.ll; }

    /**
     * Get the lower right sub-node
     *
     * @return lower right sub-node
     */
    public RITQTNode getLowerRight() { return this.lr; }

    @Override
    public String toString() {
        String s = "" + String.valueOf(this.val);
        s += " ";
        s += ul;
        s += ur;
        s += ll;
        s += lr;
        s = s.replaceAll("null","");
        return s;
    }

    /**
     * Recursive function that converts an ArrayList of data from a compressed into a quadTree
     * @param size (the size of the image)
     * @param data (the list of numbers)
     * @return the created quadTree
     */
    public static RITQTNode fromCompressed(int size, ArrayList<Integer> data){
        int curr = data.get(0);
        data.remove(0);
        int hsize = size/4;

        if(size == 1 || curr != -1){
            RITQTNode n = new RITQTNode(curr);
            n.setSize(size);
            return n;
        }

        RITQTNode tl = fromCompressed(hsize, data);
        RITQTNode tr = fromCompressed(hsize, data);
        RITQTNode bl = fromCompressed(hsize, data);
        RITQTNode br = fromCompressed(hsize, data);
        return new RITQTNode(curr, tl, tr, bl, br);
    }

    /**
     * Recursive function that converts an ArrayList of data from an uncompressed into a quadTree
     * @param size (the size of the image)
     * @param data (the list of numbers)
     * @return the created quadTree
     */
    public static RITQTNode fromUncompressed(int size, List<Integer> data){
        RITQTNode n = fromUncompressed(data);
        n.setSize(size);
        return n;
    }
    public static RITQTNode fromUncompressed(List<Integer> data){
        int size = data.size();

        if(size == 1){
            RITQTNode n = new RITQTNode(data.get(0));
            n.setSize(1);
            return n;
        }
        else if(size < 1)
            return null;

        int isize = (int)Math.sqrt(size);
        int hsize = isize/2;

        int[][] matrix = new int[isize][isize];

        for(int i = 0; i < size; i++){
            matrix[i/isize][i%isize] = data.get(i);
        }

        ArrayList<Integer> tr = new ArrayList<>();
        ArrayList<Integer> tl = new ArrayList<>();
        ArrayList<Integer> br = new ArrayList<>();
        ArrayList<Integer> bl = new ArrayList<>();

        TreeSet<Integer> checker = new TreeSet<>();

        for(int i = 0; i < size; i++){
            int num = data.get(i);
            int x = i%isize;
            int y = i/isize;

            checker.add(num);
            if(x >= hsize){
                if(y >= hsize){
                    bl.add(num);
                }
                else {
                    tl.add(num);
                }
            }
            else{
                if(y >= hsize){
                    br.add(num);
                }
                else{
                    tr.add(num);
                }
            }
        }

        if(checker.size() == 1){
            return new RITQTNode(checker.first());
        }

        RITQTNode n = new RITQTNode(-1, fromUncompressed(isize, tr), fromUncompressed(isize, tl), fromUncompressed(isize, br), fromUncompressed(isize, bl));
        n.setSize(size);
        return n;
    }

    /**
     * Converts the quadTree into an ArrayList representing the uncompressed tree
     * @return ArrayList of the numbers
     */
    public ArrayList<Integer> toUncompressedList(){
        ArrayList<Integer> data = new ArrayList<>();
        if(this.val!=-1){
            //System.out.println(val + " " + size);
            for(int i = 0; i < getSize(); i++){
                data.add(this.val);
            }
        }

        ArrayList<Integer> tl = ul!=null?getUpperLeft().toUncompressedList():new ArrayList<>();
        ArrayList<Integer> tr = ur!=null?getUpperRight().toUncompressedList():new ArrayList<>();
        ArrayList<Integer> bl = ll!=null?getLowerLeft().toUncompressedList():new ArrayList<>();
        ArrayList<Integer> br = lr!=null?getLowerRight().toUncompressedList():new ArrayList<>();

        int sqsize = (int)Math.sqrt(tl.size());

        for(int i = 0; i < sqsize; i++){
            data.addAll(tl.subList(sqsize*i,sqsize*i+sqsize));
            data.addAll(tr.subList(sqsize*i,sqsize*i+sqsize));
        }
        for(int i = 0; i < sqsize; i++){
            data.addAll(bl.subList(sqsize*i,sqsize*i+sqsize));
            data.addAll(br.subList(sqsize*i,sqsize*i+sqsize));
        }

        return data;
    }
    /**
     * Converts the quadTree into an ArrayList representing the compressed tree
     * @return ArrayList of the numbers
     */
    public ArrayList<Integer> toCompressedList(){
        ArrayList<Integer> s = new ArrayList<>();
        s.add(val);

        if(ul!=null)
            s.addAll(ul.toCompressedList());
        if(ur!=null)
            s.addAll(ur.toCompressedList());
        if(ll!=null)
            s.addAll(ll.toCompressedList());
        if(lr!=null)
            s.addAll(lr.toCompressedList());

        return s;
    }
}


