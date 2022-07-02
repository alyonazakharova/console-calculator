import lombok.Getter;

import java.io.PrintStream;
import java.util.*;

public class ExpressionProcessor {
    @Getter
    private static final Map<String, Integer> variables = new HashMap<>();
    private static final List<String> tokens = new ArrayList<>();
    private static final List<String> postfixNotation = new ArrayList<>();
    private static String previousToken;
    private static final ArrayDeque<String> operatorsDeque = new ArrayDeque<>();
    private static final ArrayDeque<String> resultDeque = new ArrayDeque<>();
    private static final ArrayDeque<Node> nodesDeque = new ArrayDeque<>();
    private static int bracketCounter = 0;

    public static void parseExpression(String expression) {
        previousToken = null;
        variables.clear();
        tokens.clear();
        operatorsDeque.clear();
        postfixNotation.clear();
        resultDeque.clear();

        int i = 0;
        char ch;
        while (i < expression.length()) {
            StringBuilder variableName = new StringBuilder();
            StringBuilder number = new StringBuilder();

            ch = expression.charAt(i);

            String currentToken;
            if ('+' == ch) {
                currentToken = "+";
                checkPreviousTokenForOperator();
                i++;
            } else if ('-' == ch) {
                currentToken = "-";
                checkPreviousTokenForOperator();
                i++;
            } else if ('*' == ch) {
                currentToken = "*";
                checkPreviousTokenForOperator();
                i++;
            } else if ('/' == ch) {
                currentToken = "/";
                checkPreviousTokenForOperator();
                i++;
            } else if ('(' == ch) {
                currentToken = "(";
                bracketCounter++;
                i++;
            } else if (')' == ch) {
                currentToken = ")";
                if (bracketCounter == 0) {
                    throw new RuntimeException("Invalid number of brackets");
                }
                bracketCounter--;
                i++;
            } else {
                if (isLetter(ch)) {
                    do {
                        variableName.append(ch);
                        i++;
                        if (i == expression.length()) {
                            break;
                        }
                        ch = expression.charAt(i);
                    } while (isLetter(ch) || isDigit(ch));
                    currentToken = variableName.toString();
                    checkPreviousTokenForOperand();
                    variables.put(variableName.toString(), null);
                } else if (isDigit(ch)) {
                    do {
                        number.append(ch);
                        i++;
                        if (i == expression.length()) {
                            break;
                        }
                        ch = expression.charAt(i);
                    } while (isDigit(ch));
                    currentToken = number.toString();
                    checkPreviousTokenForOperand();
                } else {
                    throw new IllegalArgumentException("What the hell id you enter???");
                }
            }
            tokens.add(currentToken);
            previousToken = currentToken;
        }
        if (bracketCounter != 0) {
            throw new RuntimeException("Invalid number of brackets");
        }

        createPostfixNotation();
    }

    private static void createPostfixNotation() {
        for (String token : tokens) {
            if ("(".equals(token)) {
                operatorsDeque.push(token);
            } else if (")".equals(token)) {
                String operator = operatorsDeque.pop();
                while (!Objects.equals(operator, "(")) {
                    postfixNotation.add(operator);
                    operator = operatorsDeque.pop();
                }
            } else if (Arrays.asList("*", "/", "-", "+").contains(token)) {
                String previousOperator = operatorsDeque.peek();
                if (previousOperator == null || "(".equals(previousOperator)) {
                    operatorsDeque.push(token);
                } else if ("*".equals(token) || "/".equals(token)) {
                    if ("+".equals(previousOperator) || "-".equals(previousOperator)) {
                        operatorsDeque.push(token);
                    } else {
                        previousOperator = operatorsDeque.pop();
                        postfixNotation.add(previousOperator);
                        operatorsDeque.push(token);
                    }
                } else {
                    previousOperator = operatorsDeque.pop();
                    postfixNotation.add(previousOperator);
                    operatorsDeque.push(token);
                }
            } else {
                postfixNotation.add(token);
            }
        }
        while (operatorsDeque.peek() != null) {
            postfixNotation.add(operatorsDeque.pop());
        }
    }

    public static void buildTree() {
        Node rightNode, leftNode, resultNode;
        for (String token : postfixNotation) {
            if (Arrays.asList("*", "/", "+", "-").contains(token)) {
                rightNode = nodesDeque.pop();
                leftNode = nodesDeque.pop();
                resultNode = new Node()
                        .setValue(token)
                        .setLeftChild(leftNode)
                        .setRightChild(rightNode);
                nodesDeque.push(resultNode);
            } else {
                nodesDeque.push(new Node().setValue(token));
            }
        }

        StringBuilder treeRepresentation = new StringBuilder();
        traversePreOrder(treeRepresentation, "", "", nodesDeque.pop());
        PrintStream stream = new PrintStream(System.out);
        stream.print(treeRepresentation);
    }

    private static void traversePreOrder(StringBuilder sb, String padding, String pointer, Node node) {
        if (node != null) {
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.getValue());
            sb.append("\n");

            String paddingForBoth = padding + "│  ";
            String pointerForRight = "└──";
            String pointerForLeft = (node.getRightChild() != null) ? "├──" : "└──";

            traversePreOrder(sb, paddingForBoth, pointerForLeft, node.getLeftChild());
            traversePreOrder(sb, paddingForBoth, pointerForRight, node.getRightChild());
        }
    }

    public static int calculate() {
        for (String variable : variables.keySet()) {
            while (postfixNotation.contains(variable)) {
                postfixNotation.set(postfixNotation.indexOf(variable), variables.get(variable).toString());
            }
        }

        int secondOperand, firstOperand, result;
        for (String token : postfixNotation) {
            if (Arrays.asList("*", "/", "+", "-").contains(token)) {
                secondOperand = Integer.parseInt(resultDeque.pop());
                firstOperand = Integer.parseInt(resultDeque.pop());
                if ("*".equals(token)) {
                    result = firstOperand * secondOperand;
                } else if ("/".equals(token)) {
                    result = firstOperand / secondOperand;
                } else if ("+".equals(token)) {
                    result = firstOperand + secondOperand;
                } else {
                    result = firstOperand - secondOperand;
                }
                resultDeque.push(String.valueOf(result));
            } else {
                resultDeque.push(token);
            }
        }
        return Integer.parseInt(resultDeque.pop());
    }

    private static void checkPreviousTokenForOperator() {
        // оператор не может идти в начале строки, после открывающей скобки и после оператора
        if (previousToken == null || Arrays.asList("(", "+", "-", "*", "/").contains(previousToken)) {
            throw new RuntimeException("Invalid token order");
        }
    }

    private static void checkPreviousTokenForOperand() {
        // операнд на может идти после операнда и после закрывающей скобки
        if (previousToken != null && !Arrays.asList("(", "+", "-", "*", "/").contains(previousToken)) {
            throw new RuntimeException("Invalid token order");
        }
    }

    private static boolean isLetter(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    private static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }
}
