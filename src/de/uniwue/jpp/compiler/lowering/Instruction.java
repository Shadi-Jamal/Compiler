package de.uniwue.jpp.compiler.lowering;

public class Instruction {

    Operation operation; int result; Operand left; Operand right;
    Operand value;
    public Instruction(Operation operation, int result, Operand left, Operand right) {
        this.operation = operation;
        this.result = result;
        this.left = left;
        this.right = right;
    }

    public Instruction(Operation operation, int result, Operand value) {
        this.operation = operation;
        this.result = result;
        this.value=value;
    }

    public Operation getOperation() {
        return this.operation;
    }

    public int getResult() {
        return this.result;
    }

    public Operand getLeft() {
        return this.left;
    }

    public Operand getRight() {
        return this.right;
    }

    public Operand getValue() {
        return value;
    }
}
