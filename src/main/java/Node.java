public class Node {
    private String value;
    private Node leftChild;
    private Node rightChild;

    public String getValue() {
        return value;
    }

    public Node setValue(String value) {
        this.value = value;
        return this;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
        return this;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public Node setRightChild(Node rightChild) {
        this.rightChild = rightChild;
        return this;
    }
}
