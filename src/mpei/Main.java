package mpei;


import java.util.Arrays;
import java.util.List;

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

        itest.remove(100);
        itest.remove(1);
        itest.remove(104);
        itest.remove(103);
        itest.remove(102);
        itest.remove(99);
        itest.remove(45);

        System.out.print(" 100 ");
        System.out.println(itest.find(100));

        System.out.print(" 101 ");
        System.out.println(itest.find(101));

        System.out.print(" 1 ");
        System.out.println(itest.find(1));

        System.out.print(" 45 ");
        System.out.println(itest.find(45));

        System.out.print(" 46 ");
        System.out.println(itest.find(46));

        System.out.print(" 45 ");
        System.out.println(itest.find(45));

        System.out.print(" 99 ");
        System.out.println(itest.find(99));

        /*Integer a,b;
        a = 50;
        b = 10;
        int res = a.compareTo(b);
        System.out.println(res); // a < b  => -1;  a > b => 1; a=b => 0
        */
        /*
        BTreeSet<Integer> itest = new BTreeSet<Integer>();
        itest.test(10,20);
        BTreeSet<String> stest = new BTreeSet<String>();
        stest.test("abc", "abc");
        List<Integer> lint = Arrays.asList(5, 6, 7, 8, 9, 10);
        */



    }
}
