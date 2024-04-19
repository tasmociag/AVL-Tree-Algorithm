import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Element {
    int value;
    Element(int newValue) {
        value = newValue;
    }
}

class Node {
    Node left;
    Node right;
    Element current;
    int numberUnder = 1;
    int height = 1;

    Node(Element newElement) {
        current = newElement;
    }
}

public class AVL {
    static int index = 0;
    static int index2 = 0;

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            int k = Integer.parseInt(br.readLine());

            Node root = new Node(null);

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String [] numbers = line.split(" ");
                for(int j=0;j<numbers.length;j++){
                    int value = Integer.parseInt(numbers[j]);
                    root = addNode(i, root, new Element(value));
                    i++;
                }
            }

            for (int j = 0; j < k; j++) {
                if (root == null)
                    break;
                int valueX = getNode(index, root).current.value;
                if (valueX % 2 == 1) {
                    root = addOperation(root, valueX);
                } else {
                    root = deleteOperation(root);
                }
            }

            if (root == null) {
                System.out.println("");
            } else {
                int size = getSize(root);
                int[] tab = new int[size];
                printTreeMake(root, tab);
                for (int m = 0; m < size; m++) {
                    if (index + m >= size) {
                        System.out.print(tab[index + m - size]);
                        if (m < size - 1)
                            System.out.print(" ");
                    } else {
                        System.out.print(tab[index + m]);
                        if (m < size - 1)
                            System.out.print(" ");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int max(int a, int b) {
        if(a>b)
            return a;
        else
            return b;
    }

    static int getHeight(Node node) {
        if(node != null)
            return node.height;
        else
            return 0;
    }

    static int getUnder(Node node) {
        if(node!=null)
            return node.numberUnder;
        else
            return 0;
    }

    static int getSize(Node root) {
        return 1 + getUnder(root.left) + getUnder(root.right);
    }

    static void printTreeMake(Node node, int[] tab) {
        if (node.left != null) {
            printTreeMake(node.left, tab);
        }

        tab[index2] = node.current.value;
        index2++;

        if (node.right != null) {
            printTreeMake(node.right, tab);
        }
    }

    static Node leftRotate(Node node) {
        Node child = node.right;
        Node grandchild = child.left;
        child.left = node;
        node.right = grandchild;

        node.height = 1 + max(getHeight(node.left), getHeight(node.right));
        node.numberUnder = 1 + getUnder(node.left) + getUnder(node.right);
        child.height = 1 + max(getHeight(child.left), getHeight(child.right));
        child.numberUnder = 1 + getUnder(child.left) + getUnder(child.right);

        return child;
    }

    static Node rightRotate(Node node) {
        Node child = node.left;
        Node grandchild = child.right;
        child.right = node;
        node.left = grandchild;

        node.height = 1 + max(getHeight(node.left), getHeight(node.right));
        node.numberUnder = 1 + getUnder(node.left) + getUnder(node.right);
        child.height = 1 + max(getHeight(child.left), getHeight(child.right));
        child.numberUnder = 1 + getUnder(child.left) + getUnder(child.right);

        return child;
    }

    static Node balance(Node node) {
        int balance = getHeight(node.left) - getHeight(node.right);

        if (balance > 1) {
            if (getHeight(node.left.left) > getHeight(node.left.right)) {
                return rightRotate(node);
            } else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        if (balance < -1) {
            if (getHeight(node.right.left) < getHeight(node.right.right)) {
                return leftRotate(node);

            } else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        return node;
    }

    static Node addNode(int index, Node currentNode, Element newElement) {
        if (currentNode.current == null) {
            currentNode.current = newElement;
            return currentNode;
        }

        if (getUnder(currentNode.left) == index) {
            Node newNode = new Node(newElement);
            newNode.right = currentNode;
            newNode.left = currentNode.left;
            currentNode.left = null;

            currentNode.numberUnder = 1 + getUnder(currentNode.left) + getUnder(currentNode.right);
            currentNode.height = 1 + max(getHeight(currentNode.left), getHeight(currentNode.right));
            newNode.numberUnder = 1 + getUnder(newNode.left) + getUnder(newNode.right);
            newNode.height = 1 + max(getHeight(newNode.left), getHeight(newNode.right));

            currentNode = balance(currentNode);
            newNode.right = currentNode;
            newNode = balance(newNode);

            return newNode;
        }

        if (index < getUnder(currentNode.left)) {
            if (currentNode.left == null)
                currentNode.left = new Node(null);
            currentNode.left = addNode(index, currentNode.left, newElement);
        }
        if (index > getUnder(currentNode.left)) {
            if (currentNode.right == null)
                currentNode.right = new Node(null);
            currentNode.right = addNode(index - 1 - getUnder(currentNode.left), currentNode.right, newElement);
        }

        currentNode.numberUnder = 1 + getUnder(currentNode.left) + getUnder(currentNode.right);
        currentNode.height = 1 + max(getHeight(currentNode.left), getHeight(currentNode.right));

        return balance(currentNode);
    }

    static Node minValueNode(Node node) {
        Node current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }

    static Node deleteNode(Node root, int index) {
        if (root == null)
            return root;

        if (index < getUnder(root.left)) {
            root.left = deleteNode(root.left, index);
        } else if (index > getUnder(root.left)) {
            root.right = deleteNode(root.right, index - 1 - getUnder(root.left));

        } else {
            if ((root.left == null) || (root.right == null)) {
                Node temp;
                if(root.left!=null)
                    temp = root.left;
                else 
                    temp = root.right;

                if (temp == null) {
                    temp = root;
                    root = null;
                } else
                    root = temp;
            } else {
                Node temp = minValueNode(root.right);
                root.current = temp.current;
                root.right = deleteNode(root.right, 0);
            }
        }

        if (root == null)
            return root;

        root.numberUnder = 1 + getUnder(root.left) + getUnder(root.right);
        root.height = 1 + max(getHeight(root.left), getHeight(root.right));

        return balance(root);
    }

    static Node getNode(int index, Node currentNode) {
        if (getUnder(currentNode.left) == index) {
            return currentNode;
        }

        if (index < getUnder(currentNode.left)) {
            return getNode(index, currentNode.left);
        }
        if (index > getUnder(currentNode.left)) {
            return getNode(index - 1 - getUnder(currentNode.left), currentNode.right);
        }

        return null;
    }

    static Node addOperation(Node root, int valueX) {
        Node newNode = addNode(index + 1, root, new Element(valueX - 1));
        int size = getSize(newNode);
        index = (index + valueX) - (((index + valueX) / size) * size);
        return newNode;
    }

    static Node deleteOperation(Node root) {
        int newIndex = index + 1;
        if (newIndex == getSize(root))
            newIndex = 0;

        int valueX = getNode(newIndex, root).current.value;
        Node newNode = deleteNode(root, newIndex);

        if (newNode == null)
            return null;

        int size = getSize(newNode);
        if (newIndex < index)
            index--;
        index = (index + valueX) - (((index + valueX) / size) * size);
        return newNode;
    }
}