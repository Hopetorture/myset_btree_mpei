package mpei;
//import mpei.BTree;

public class BTreeSet<T extends Comparable> {
    BTreeSet(){
        init();
    }
    public void test(T x, T y){
        System.out.println(x.compareTo(y));
    }

    public boolean add(){
        return true;
    }
    public boolean add(String s){ //initilizer-list init?
        return  true;
    }
    public boolean rm(){
        return true;
    }

    public boolean contains(){
        return true;
    }

    public boolean isEmpty(){
        if (size > 0){
            return false;
        }
        return true;
    }

    public void clear(){}
    public int size(){return size;}

    public void intersect(){} // сделать версию метода, возвращающую новый сет
    public void union(){}
    public boolean subset(){return false;}
    public void removeAll(){}
    // set difference?

    // add iterator?

    private void init(){
        System.out.println("test");
    }

    private int size;
    private BTree<T> tree;
}
