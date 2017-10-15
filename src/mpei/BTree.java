package mpei;

import java.util.Iterator;


public class BTree<T extends Comparable> implements Iterable<T> {
    public  BTree(){
        this.root = null;
        this.size = 0;
    }

    @Override
    public Iterator<T> iterator(){
        return new mIterator<>();
    }

    public int getSize(){return size;}

    public void add(T d){
        if(root == null)
            root = new Node<>(null, d);
        else
            rAdd(root, d);
    }
    private void rAdd(Node<T> n, T newData){
        int compareResult = n.data.compareTo(newData);
        switch (compareResult){
            case  -1:           // data < newData //add to right
                if (n.right != null)
                    rAdd(n.right, newData);
                else{
                    n.right = new Node(n, newData);
                    size++;
                }
                break;
            case   0:  return;  //already exists!
            case   1:           // data > newData //add to left
                if (n.left != null)
                    rAdd(n.left, newData);
                else{
                    n.left = new Node(n, newData);
                    size++;
                }
                break;
            default : throw new RuntimeException();
        }
        balance(n);
    }


    public boolean find(T d) {
        return root != null && rFind(root, d);
    }

    private boolean rFind(Node<T> n, T fd){
        int compareResult = n.data.compareTo(fd);
        switch (compareResult){
            case -1:              // data < fd - find in right sub-tree
                if (n.right != null)
                    return rFind(n.right, fd);
                break;
            case 0: return true; //found
            case 1:              //data  > fd - find in left sub-tree
                if (n.left != null)
                    return rFind(n.left, fd);
                break;

            default: return false;
        }
        return false;
    }

    public boolean remove(T fd){ //should try-catch here;
        if (root != null){
            return this.rm(root, fd);
        }
        else return false;
    }
    private boolean rm(Node<T> n, T fd){
        int compareResult = n.data.compareTo(fd);
        switch (compareResult){
            case -1:
                if (n.right != null)
                    return rm(n.right, fd);
                else break;
            case 0:
                rm_help(n);
                size --;
                return true;
            case 1:
                if (n.left != null)
                    return rm(n.left, fd);
                else break;
            default: break;
        }
        return false;
    }

    private void rm_help(Node<T> n){
        System.out.println("removing object: ");
        System.out.println(n.data);
        if (n.left == null && n.right == null){
            if (n.parent.left == n)
                n.parent.left = null;
            else
                n.parent.right = null;
        }
        if (n.left != null && n.right != null){ //got both parents
            Node<T> iter;
            iter = n.right;
            while (iter.left != null){
                iter = iter.left;
            }
            setRef(n, iter);
            if (iter.right != null)
                iter.parent.swap(iter, iter.right);
            else
                setRef(iter, null);
        }
        else{ // got left OR right node
            if (n.left != null){ //
                n.parent.swap(n, n.left);
            }
            else
                n.parent.swap(n,n.right);
        }
        balance(n);
        fixParents(root);
    }

    public <C extends Iterable> BTree<T> union(C other){
        Iterator<T> thisIter = this.iterator();
        Iterator<T> otherIter = other.iterator();
        BTree<T> result = new BTree<>();
        while (thisIter.hasNext())
            result.add(thisIter.next());
        while (otherIter.hasNext())
            result.add(otherIter.next());
        return result;
    }

    private void setRef(Node<T> node, Node <T> newNode){
        if (node != root)
            node.parent.setRef(node, newNode);
        else
            root = newNode;
    }

    //AVL-functions
    private int getNullableHeight(Node<T> n){
        try {
            if (n != null)
                return n.height;
            else
                return 0;
        }catch (Exception e){System.out.println("getNullable caught");}
        return 0;
    }
    private int bFactor(Node<T> n){
        if (n == null) return 0;
        try{
        return getNullableHeight(n.left) - getNullableHeight(n.right);
        }catch (Exception e){
            System.out.println("Caught in bFactor");
        }
        return 0;

    }
    private void fixHeight(Node<T> n){
        if (n.left == null || n.right == null) return;
        int rightHeight = n.right.height;
        int leftHeight = n.left.height;
        if (leftHeight > rightHeight)
            n.height = leftHeight + 1;
        else
            n.height = rightHeight +1;
    }
    private void fixParents(Node<T> node){
        if (node.left != null){
            node.left.parent = node;
            fixParents(node.left);
        }
        if (node.right != null) {
            node.right.parent = node;
            fixParents(node.right);
        }
    }

    private void rightRotate(Node<T> n){
        try {
            if (n == null) return;
            Node<T> newroot = n.left;
            n.left = newroot.right;
            newroot.right = n;

            if (n.parent == null) {
                root = newroot;
                newroot.parent = null;
            } else {
                if (n.parent.left == n) {
                    n.parent.left = newroot;
                } else {
                    n.parent.right = newroot;
                }
                newroot.parent = n.parent;
            }
            n.parent = newroot;
            fixHeight(n);
            fixHeight(newroot);
        }//try
        catch(Exception e){
            System.out.println("caught in right rotate");
        }
    }
    private void leftRotate (Node<T> n){
        if (n == null) return;
        Node<T> newroot = n.right;
        n.right = newroot.left;
        newroot.left = n;

        if (n.parent == null){
            root = newroot;
            newroot.parent = null;
        }
        else{
            if( n.parent.left == n ){
                n.parent.left = newroot;
            }
            else {
                n.parent.right = newroot;
             }
            newroot.parent = n.parent;
        }
        n.parent = newroot;
        fixHeight(n);
        fixHeight(newroot);
    }


    private void balance(Node<T> p){
        if (p == null)return;
        try {
            fixHeight(p);
            if (bFactor(p) > 1){ // левое поддерево несбалансировано
                if(bFactor(p.left) < 0)// из-за правого потомка левого дерева
                    leftRotate(p.left); // двойной поворот
                rightRotate(p);
            }//if
            else if (bFactor(p) < -1){// правое поддерево
                if (bFactor(p.right) > 0) //из-за левого потомка правого дерева
                    rightRotate(p.right);
                leftRotate(p);
            }//elif
            fixParents(root);
        }//try
        catch (Exception e) {
        System.out.println("Caught in balance()");
        StackTraceElement[] strace = e.getStackTrace();
        for (StackTraceElement aStrace : strace)
            System.out.println(aStrace);
        }//catch
    }//balance

    public void clear(){
        this.root = null;
        this.size = 0;
    }


    public void printBalance(Node<T> node){
        if (node != null){
            System.out.print("Node balance: ");
            System.out.println(bFactor(node));
            int bf = bFactor(node);
            System.out.print("node:");
            System.out.println(node.data);
            if (bf > 1 || bf < -1){
                System.out.print("erroneus data:");
                System.out.println(node.data);
            }
            printBalance(node.left);
            printBalance(node.right);
        }
    }
    public Node<T> getRoot(){return root;}// for debug
    public T getRootData(){return root.data;}// for debug

    private Node<T> root; //root ptr in BTree
    private int size;

    private class Node<D extends Comparable> {
         Node(Node parent, D data){
            this.left = null;
            this.right = null;
            this.parent = parent;
            this.data = data;
            this.height = 1;
            this.visitCount = -1;
         }

        void swap(Node<D> kid, Node<D> kidskid){
            if (this.left == kid)
                this.left = kidskid;
            else
                this.right = kidskid;
        }

        void setRef(Node<D> target, Node<D> newValue){
            if (this.left == target)
                this.left = newValue;
            else if (this.right == target)//elif
                this.right = newValue;
            else{
                System.out.println("illegal delete");
                throw new RuntimeException();
            }//else
        }//setRef


        Node parent;
        Node left;
        Node right;
        int height;
        int visitCount;
        final D data;
    }

    //****DATA-Iterator
    private class mIterator<Type extends Comparable> implements Iterator<Type>{
     mIterator(){
         int rvc = root.visitCount;
         if (rvc == -1 || rvc > 100){
             root.visitCount = 1;
         }
         currentVisited = root.visitCount;
         root.visitCount++;
     }//ctor

     public boolean hasNext(){
         return hasNextCounter != getSize();
     }//hasNext

     public Type next(){ // do with pop root
         hasNextCounter++;
         Type returnData = (Type)recNext(root.left);
         if (returnData != null)
             return returnData;

         returnData  = returnRoot();
         if (returnData != null)
             return returnData;

         returnData = (Type)recNext(root.right);
         if (returnData != null)
             return returnData;

         throw new IndexOutOfBoundsException("AVL Tree out of bound exception! Use .hasNext()!!!");
     }//next

     private Type recNext(Node<Type> node){
         if (node == null) return null;
         if(node.visitCount != currentVisited){
             node.visitCount = currentVisited;
             return node.data;
         }
         Type data = (Type) recNext(node.left); //return?
         if (data != null)
             return data;

         data = (Type)recNext(node.right);
         if (data != null)
             return data;

         return null;
     }//recNext

     private Type returnRoot(){
         if(!rootReturned){
            rootReturned = true;
            return (Type)root.data;
         }
         return null;
     }//returnRoot

     public void remove(){
         throw new RuntimeException("unsupported operation");
     }
     private int currentVisited;
     private int hasNextCounter = 0;
     private boolean rootReturned = false;
    }//mIterator class

}//BTree class
