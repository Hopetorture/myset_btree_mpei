package mpei;

import java.util.ArrayList;
import java.util.Iterator;

// TODO - Унести функционал из Node в Btree
public class BTree<T extends Comparable> implements Iterable<T> {
    public  BTree(){
        this.root = null;
        this.size = 0;
        //this.height = 0;
    }

    @Override
    public Iterator<T> iterator(){
        return new mIterator<>();
    }

    public int getSize(){return size;}
    //public int getDepth(){return height;}

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
        //balance?
        //int balance = bFactor(n);
        //if (balance > 1){
            //if (bFactor(n.left) < 0)
                //!//leftRotate() fix rotate
        //}
    }


    public boolean find(T d){
        if (root == null) return false;
        return rFind(root, d);
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
            size--;
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
                return true;
            case 1:
                if (n.left != null)
                    return rm(n.left, fd);
                else break;
            default: break;
        }
        return false;
    }
    //bug - if removes root
    private void rm_help(Node<T> n){ // fix deletion of elements
        System.out.println("removing object: ");
        System.out.println(n.data);
        if (n.left == null && n.right == null){
            if (n.parent.left == n)
                n.parent.left = null;
            else
                n.parent.right = null;
        }
        if (n.left != null && n.right != null){ //got both parents
            //! cycle-traversal
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
        }catch (Exception e){System.out.println(e.getLocalizedMessage().toString()); System.out.println("getNullable caught");}
        return 0;
    }
    private int bFactor(Node<T> n){
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
        if (n == null) return;
        Node<T> tmp = n.left;
        n.left = tmp.right;
        tmp.right = n;
        fixHeight(n);
        fixHeight(tmp);
    }
    private void leftRotate (Node<T> n){ // root rotation special case
        if (n == null) return;
        Node<T> tmp = n.right;
        n.right=tmp.left;
        tmp.left=n;
        // TODO: add parent-pointers fix
        // https://gist.github.com/danicat/7075125
        fixHeight(n);
        fixHeight(tmp);
    }
    private void balance(Node<T> p){
        if (p == null)return;

    try {
        fixHeight(p);
        if (bFactor(p) == 2) {
            if (bFactor(p.right) < 0)
                rightRotate(p.right);
            leftRotate(p);
        }
        if (bFactor(p) == -2) {
            if (bFactor(p.left) > 0)
                leftRotate(p.left);
            rightRotate(p);
        }
        fixParents(root);
    }
    catch (Exception e) {
        System.out.println("Caught in balance()");
        StackTraceElement[] strace = e.getStackTrace();
        for (int i = 0; i < strace.length; i++)
            System.out.println(strace[i]);

        System.out.println(p);
        System.out.println(p.data);
    }
    }

    public void printTree(){
        if (this.getSize() == 0){
            System.out.println("Empty tree");
        }
        else
            printTree(root);
    } // should I use it? or stick with iterators?
    private void printTree(Node<T> n){
        if (n != null){
            //System.out.println("Parent node: ");
            printTree(n.left);
            System.out.println(n.data);
            printTree(n.right);
        }
    }

    public void clear(){
        this.root = null;
        this.size = 0;
    }

//    public void prettyPrint(){
////        if (root == null) return;
////        System.out.println(root.data);
////        recPrettyPrint(root);
//    }
//    private void recPrettyPrint(Node<T> node){ //meh-result, should do it non-recursivly
////        System.out.println();
////        if (node.left != null)
////            System.out.print(node.left.data);
////        System.out.print("    ");
////        if (node.right != null)
////        System.out.print(node.right.data);
////
////        if (node.left != null)
////            recPrettyPrint(node.left);
////        if (node.right != null)
////            recPrettyPrint(node.right);
//    }
    //*experimental
    public void pp_calc(){ // abandon idea?
        root.line = 0;
        calculateDepthLines(root);

    }
    public void calculateDepthLines(Node<T> n){
        n.line = n.parent.line + 1;
        calculateDepthLines(n.left);
        calculateDepthLines(n.right);
    }
    //*experimental
    public ArrayList<Node<T>> toList(Node<T> node, ArrayList<Node<T>> result){
        if (node != null){
            result.add(node);
            toList(node.left, result);
            toList(node.right,result);
        }
        return result;
    }
    public void printArray(){
        ArrayList<Node<T>> res = new ArrayList<>();
        toList(root, res);
        for (int i = 0; i < res.size(); i++){
            System.out.println(res.get(i).data);
        }
    }
    // should I remove toList or change it?

    public void printBalance(Node<T> node){
        if (node != null){
            System.out.print("Node balance: ");
            System.out.println(bFactor(node));
            int bf = bFactor(node);
            System.out.print("root:");
            System.out.println(root.data);
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

    private Node<T> root;
    private int size;

    private class Node<D extends Comparable> {
        public Node(Node parent, D data){
            this.left = null;
            this.right = null;
            this.parent = parent;
            this.data = data;
            this.height = 1;
            this.line = -1;
            this.visitCount = -1;
        }
//        public Node(Node other){
//            // do I even need this ?
//        }

        public void swap(Node<D> kid, Node<D> kidskid){
            if (this.left == kid)
                this.left = kidskid;
            else
                this.right = kidskid;
        }

        public void setRef(Node<D> target, Node<D> newValue){
            if (this.left == target)
                this.left = newValue;
            else
            if (this.right == target)
                this.right = newValue;
            else{
                System.out.println("illegal delete");
                throw new RuntimeException();
            }
        }
//        public void copy (Node<D> oldNode, Node<D> newNode){
//            if (oldNode.left != null){
//                newNode.left = new Node<>(oldNode.left.parent, oldNode.left.data);
//                copy();
//
//            }
//
//        }

        public Node parent;
        public Node left;
        public Node right;
        public int height;
        public int line;
        public int visitCount;
        public D data;
    }

//    private class nodeIterator<Type extends Comparable> implements Iterator<Type>{
//        public nodeIterator(){
//            int rvc = root.visitCount;
//            if (rvc == -1 || rvc > 100){
//                root.visitCount = 1;
//            }
//            currentVisited = root.visitCount;
//            root.visitCount++;
//        }
//        public boolean hasNext(){
//            if (hasNextCounter != getSize())
//                return true;
//            return false;
//        }
//        public Type next(){return null;}
//        private Type recNext(Type node){
//            if (node == null) return null;
//            if(()node.visitCount != currentVisited){
//                node.visitCount = currentVisited;
//                return node.data;
//            }
//            Type data = (Type) recNext(node.left); //return?
//            if (data != null)
//                return data;
//
//            data = (Type)recNext(node.right);
//            if (data != null)
//                return data;
//
//            return null;
//        }
//
//        private Type returnRoot(){
//            if(!rootReturned){
//                rootReturned = true;
//                return (Type)root;
//            }
//            return null;
//        }
//        public void remove(){throw new RuntimeException("unsupported operation");}
//
//        private int currentVisited;
//        private int hasNextCounter = 0;
//        private boolean rootReturned = false;
//    }
    //^^Node Iterator
    //****DATA-Iterator
    private class mIterator<Type extends Comparable> implements Iterator<Type>{
     public mIterator(){
         int rvc = root.visitCount;
         if (rvc == -1 || rvc > 100){
             root.visitCount = 1;
         }
         currentVisited = root.visitCount;
         root.visitCount++;
     }
     public boolean hasNext(){
         if (hasNextCounter != getSize())
             return true;
         return false;
     }
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

     }
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
     }
     private Type returnRoot(){
         if(!rootReturned){
            rootReturned = true;
            return (Type)root.data;
         }
         return null;
     }
//     public Node<Type> nextNode(){
//        return null;
//     }
     public void remove(){
         throw new RuntimeException("unsupported operation");
     }
        private int currentVisited;
        private int hasNextCounter = 0;
        private boolean rootReturned = false;
    }

}
