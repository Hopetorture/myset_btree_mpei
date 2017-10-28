package mpei;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//import java.util.Iterator;
class autoTests{
    autoTests(){
        BTree<Integer> itest = new BTree<>();

        itest.add(new ArrayList<>(Arrays.asList(50,45,5,1,100,99,105,104,103,102)));

        itest.remove(1);
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

        System.out.println("Iterator print: ");

        for (int i : itest){
            System.out.println(i);
        }
        itest.printBalance(itest.getRoot());

        BTree<Integer> otherTree = new BTree<>();
        otherTree.add(5);
        otherTree.add(15);
        otherTree.add(55);
        otherTree.add(150);
        BTree<Integer> resultTree = itest.union(otherTree);
        System.out.println("Union:");
        for (int i : resultTree)
            System.out.println(i);
        //resultTree.printBalance(resultTree.getRoot());

        BTree<Integer> intersectTree = itest.intersect(otherTree);
        System.out.println("Intersect test:");
        for (int i : intersectTree)
            System.out.println(i);
    }
}

public class Main {

    public static void main(String[] args) {
        boolean execLoopFlag = true;
        BTree<Integer> mainTree = new BTree<>();
        while (execLoopFlag){
            System.out.println("Выберите пункт меню:");
            System.out.println("1. Создать новое дерево");
            System.out.println("2. Добавить узел");
            System.out.println("3. Поиск узла");
            System.out.println("4. Удаление узла");
            System.out.println("5. Отобразить элементы дерева");
            System.out.println("6. Отобразить баланс дерева");
            System.out.println("7. Запустить авто тесты");
            System.out.println("8. Выход");
            Scanner in = new Scanner(System.in);
            int menupoint = in.nextInt();
            //int menupoint = 7;
            switch (menupoint){
                case 1: //new tree
                    mainTree = new BTree<>();
                    System.out.println("Новое дерево создано.");
                    break;
                case 2: // add
                    System.out.println("Введите число для добавления");
                    Integer nVal = in.nextInt();
                    mainTree.add(nVal);
                    break;
                case 3: //find
                    System.out.println("Введите число для поиска");
                    Integer fVal = in.nextInt();
                    if(mainTree.find(fVal))
                         System.out.println("Значение найдено");
                    else System.out.println("Значение не найдено");
                    break;
                case 4: //delete
                    System.out.println("Введите число для удаления");
                    Integer rVal = in.nextInt();
                    if(mainTree.remove(rVal))
                         System.out.println("Успешно удалено");
                    else System.out.println("Удаление не удалось");
                    break;
                case 5: //print
                    for (int i : mainTree)
                        System.out.println(i);
                    break;
                case 6: //print balance
                    mainTree.printBalance(mainTree.getRoot());
                    break;
                case 7: //start auto-tests
                    autoTests tests = new autoTests();
                    break;
                case 8: //exit
                    execLoopFlag = false;
                    break;
                default:
                    System.out.println("Ошибка ввода");
                    break;
            }
        }
    }
}
