package mpei;


//import java.util.Iterator;

//TODO intersect, union,
public class Main {

    public static void main(String[] args) {
	// write your code here
        //Integer x = 5;
        //System.out.println(x.compareTo(10)); // compareTo=spaceship operator

        BTree<Integer> itest = new BTree<>();
        itest.add(50);
        itest.add(45);
        itest.add(5);
        itest.add(1);
        itest.add(100);

        itest.add(99);
        itest.add(105);
        itest.add(104);
        itest.add(103);
        itest.add(102);

        System.out.println(itest.getRootData());
        System.out.println("root data^");
//        itest.remove(100);
//        itest.remove(1);
//        itest.remove(104);
//        itest.remove(103);
//        itest.remove(102);
//        itest.remove(99);
//        itest.remove(45);

//
//        System.out.print(" 100 ");
//        System.out.println(itest.find(100));
//
//        System.out.print(" 101 ");
//        System.out.println(itest.find(101));
//
//        System.out.print(" 1 ");
//        System.out.println(itest.find(1));
//
//        System.out.print(" 45 ");
//        System.out.println(itest.find(45));
//
//        System.out.print(" 46 ");
//        System.out.println(itest.find(46));
//
//        System.out.print(" 45 ");
//        System.out.println(itest.find(45));
//
//        System.out.print(" 99 ");
//        System.out.println(itest.find(99));

        //itest.prettyPrint();

        //System.out.println("Array print: ");
        //itest.printArray();

        System.out.println("Iterator print: ");
//        Iterator<Integer> iter = itest.iterator();
//        while (iter.hasNext()){
//            System.out.println(iter.next());
//        }
///*        for (int i : itest){ //bug with remove and iterators
///*            System.out.println(i);
///*        }
        itest.printBalance(itest.getRoot());
        //try {
            BTree<Integer> otherTree = new BTree<>();
            otherTree.add(5);
            otherTree.add(15);
            otherTree.add(55);
            otherTree.add(150);
            BTree<Integer> resultTree = itest.union(otherTree);
            //resultTree.printArray();
            for (int i : resultTree)
                System.out.println(i);
            resultTree.printBalance(resultTree.getRoot());

        //resultTree.fixLevels(resultTree.getRoot());
        //resultTree.printLevels(resultTree.getRoot());

        //itest.fixLevels(itest.getRoot());
        //itest.printLevels(itest.getRoot());
        //}
        //catch (Exception e){System.out.println(e.getStackTrace());}
    }
}
