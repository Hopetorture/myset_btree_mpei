package mpei;

import javax.lang.model.type.NullType;

public class BTree<DataType extends Comparable> {
    public  BTree(){
        this.root = null;
    }

    public void add(DataType d){
        if(root == null)
            root = new Node<DataType>(null, d);
        else
            root.add(d);
    }
    public boolean find(DataType d){
        if (root == null) return false;
        return root.find(d);
    }



    private Node<DataType> root;
    private class Node<T extends Comparable> {
        public Node(Node parent, T data){
            this.left = null;
            this.right = null;
            this.parent = parent;
            this.data = data;
        }

        public void add(T newData){
            int compareResult = data.compareTo(newData);
            switch (compareResult){
                case  -1:           // data < newData //add to right
                    if (right != null)
                        right.add(newData);
                    else
                        right = new Node(this, newData);
                    break;
                case   0:  return;  //already exists!
                case   1:           // data > newData //add to left
                    if (left != null)
                        left.add(newData);
                    else
                        left = new Node(this, newData);
                    break;
                default : throw new RuntimeException();
            }
        }

        public boolean find(T fd){
            int compareResult = this.data.compareTo(fd);
            switch (compareResult){
                case -1:              // data < fd - find in right sub-tree
                    if (right != null)
                        return right.find(fd);
                    break;
                case 0: return true; //found
                case 1:              //data  > fd - find in left sub-tree
                    if (left != null)
                        return left.find(fd);
                    break;

                default: return false;
            }
            return false;
        }

        public boolean rm(T fd){
            int compareResult = this.data.compareTo(fd);
            switch (compareResult){
                case -1:
                    if (right != null)
                        return right.rm(fd);
                    else break;
                case 0: return
                case 1:
                    if (left != null)
                        return left.rm(fd);
                    else break;
                default: break;
            }
            return false;
        }

        private boolean rm_help(){
            if (this.left == null && this.right == null) ;
        }

        public Node parent;
        public Node left;
        public Node right;
        public T data;
    }

}
