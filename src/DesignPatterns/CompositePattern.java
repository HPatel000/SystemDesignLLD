package DesignPatterns;

import java.util.ArrayList;
import java.util.List;

public class CompositePattern {
    public static void main(String[] args) {
        System.out.println("FILESYSTEM");
        new Terminal();
        System.out.println();
        System.out.println("CALCULATOR");
        new Calculator();
    }
}

class Terminal {
    public Terminal() {
        File inception = new File("Inception");
        File interstellar = new File("Interstellar");
        File strangerThings = new File("Stranger Things");
        Directory entertainment = new Directory("Entertainment");
        Directory movies = new Directory("Movies");
        Directory series = new Directory("Series");
        movies.add(inception);
        movies.add(interstellar);
        series.add(strangerThings);
        entertainment.add(movies);
        entertainment.add(series);
        entertainment.ls();
    }
}

interface FileSystem {
    void ls();
}

class File implements FileSystem {
    String fileName;

    public File(String fileName) {
        this.fileName = fileName;
    }

    public void ls() {
        System.out.println("File: "+fileName);
    }
}

class Directory implements FileSystem {
    String dictName;
    List<FileSystem> files;

    public Directory(String dictName) {
        this.dictName = dictName;
        files = new ArrayList<>();
    }

    public void add(FileSystem fileSystem) {
        files.add(fileSystem);
    }

    public void ls() {
        System.out.println("Directory: "+dictName);
        for(FileSystem fileSystem: files) fileSystem.ls();
    }
}


class Calculator {
    public Calculator() {
        ArithmeticExpression one = new Number(1);
        ArithmeticExpression two = new Number(2);
        ArithmeticExpression five = new Number(5);
        ArithmeticExpression onePlusTwo = new Expression(one, two, Operation.ADD);
        ArithmeticExpression total = new Expression(onePlusTwo, five, Operation.MULTIPLY);
        System.out.println(total.evaluate());
    }
}

interface ArithmeticExpression {
    int evaluate();
}

class Number implements ArithmeticExpression {
    int number;

    public Number(int number) {
        this.number = number;
    }

    @Override
    public int evaluate() {
        System.out.println("Number is: "+number);
        return number;
    }
}

class Expression implements ArithmeticExpression {
    ArithmeticExpression left;
    ArithmeticExpression right;
    Operation operation;

    public Expression(ArithmeticExpression left, ArithmeticExpression right, Operation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    @Override
    public int evaluate() {
        int ans = -1;
        switch (operation) {
            case ADD -> {
                ans = left.evaluate()+right.evaluate();
                System.out.println("ADD: "+ans);
            }
            case SUBTRACT -> {
                ans = left.evaluate() - right.evaluate();
                System.out.println("SUBTRACT: "+ans);
            }
            case MULTIPLY -> {
                ans = left.evaluate() * right.evaluate();
                System.out.println("MULTIPLY: "+ans);
            }
            default -> {
                ans = left.evaluate() / right.evaluate();
                System.out.println("DIVIDE: "+ans);
            }
        }
        return ans;
    }
}

enum Operation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVISION;
}
