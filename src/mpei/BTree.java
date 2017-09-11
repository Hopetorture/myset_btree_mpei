package mpei;

// TODO - Унести функционал из Node в Btree
public class BTree<T extends Comparable> {
    public  BTree(){
        this.root = null;
        this.size = 0;
        //this.depth = 0;
    }
    public int getSize(){return size;}
    //public int getDepth(){return depth;}

    public void add(T d){
        if(root == null)
            root = new Node<>(null, d);
        else
            rAdd(root, d);
        size++;
    }
    private void rAdd(Node<T> n, T newData){
        int compareResult = n.data.compareTo(newData);
        switch (compareResult){
            case  -1:           // data < newData //add to right
                if (n.right != null)
                    rAdd(n.right, newData);
                else
                    n.right = new Node(n, newData);
                break;
            case   0:  return;  //already exists!
            case   1:           // data > newData //add to left
                if (n.left != null)
                    rAdd(n.left, newData);
                else
                    n.left = new Node(n, newData);
                break;
            default : throw new RuntimeException();
        }
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
    }

    private void setRef(Node<T> node, Node <T> newNode){
        if (node != root)
            node.parent.setRef(node, newNode);
        else
            root = newNode;
    }

    private int getNullableDepth(Node<T> n){
        if (n != null)
            return n.depth;
        else
            return 0;
    }
    private int bFactor(Node<T> n){return getNullableDepth(n.left) - getNullableDepth(n.right);}
    private void fixDepth(Node<T> n){
        int rightDepth = n.right.depth;
        int leftDepth = n.left.depth;
        if (leftDepth > rightDepth)
            n.depth = leftDepth + 1;
        else
            n.depth = rightDepth +1;
    }

    private Node<T> rightRotate(Node<T> n){
        Node<T> tmp = n.left;
        n.left = tmp.right;
        tmp.right = n;
        fixDepth(n);
        fixDepth(tmp);
        return tmp;
    }
    private Node<T> leftRotate (Node<T> n){
        Node<T> tmp=n.right;
        n.right=tmp.left;
        tmp.left=n;
        fixDepth(n);
        fixDepth(tmp);
        return tmp;
    }

    private Node<T> balance(Node<T> p){
        fixDepth(p);
        if(bFactor(p)==2){
            if(bFactor(p.right)<0)
                p.right= rightRotate(p.right);
            return leftRotate(p);
        }
        if(bFactor(p)==-2){
            if(bFactor(p.left)>0)
                p.left= leftRotate(p.left);
            return rightRotate(p);
        }
        return p;
    }


    private Node<T> root;
    private int size;
    //private int depth;

    private class Node<D extends Comparable> {
        public Node(Node parent, D data){
            this.left = null;
            this.right = null;
            this.parent = parent;
            this.data = data;
            this.depth = 0;
        }

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

        public Node parent;
        public Node left;
        public Node right;
        public int depth;
        public D data;
    }

}
