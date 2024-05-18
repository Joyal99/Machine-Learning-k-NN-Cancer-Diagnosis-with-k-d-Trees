import java.util.Scanner;

public class A1_Q2 {
    static class Node {
        char data;
        Node next;

        Node(char data){
            this.data = data;
            this.next = null;
        }
    }

    //Class for stack

    static class Stack {
        Node top;

        Stack() {
            this.top = null;
        }

        //Push
        public void push(char data){
            Node newNode = new Node(data);
            newNode.next = top;
            top = newNode;
        }

        //Pop
        public char pop(){
            if (isEmpty()) {
                return '\0'; //returns null character if stack is empty.
            }
            char data = top.data;
            top = top.next;
            return data;
        }

        //Checking if stack is empty or not
        public boolean isEmpty() {
            return top == null;
        }
    }

    public static boolean isBalanced(String expression) {
        Stack stack = new Stack();
        for(int i=0; i< expression.length(); i++){
            char ch = expression.charAt(i);

            //Pushing opening brackets in the stack
            if(ch == '(' || ch == '{' || ch == '['){
                stack.push(ch);
            }

            //Popping and checking closing brackets in the stack
            if(ch == ')' || ch == '}' || ch == ']'){
                if(stack.isEmpty()){
                    return false;
                }
                char top = stack.pop();
                if(!PairMatched(top,ch)) {
                    return false;
                }
            }
        }

        //If it shows that it is an empty stack, then all brackets in the pair are matched
        return stack.isEmpty();
    }

    private static boolean PairMatched(char opened, char closed) {
        return  (opened == '(' && closed == ')') ||
                (opened == '{' && closed == '}') ||
                (opened == '[' && closed == ']');
    }

    public static boolean ContainsBrackets(String expression){
        for(int i=0; i<expression.length(); i++){
            char ch = expression.charAt(i);
            if(ch != '(' && ch != ')' && ch != '{' && ch != '}' && ch!= '[' && ch!= ']') {
                return false;
            }
        }
        return true;
    }

    //Main Method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your string: ");
        String input = scanner.nextLine();

        if(!ContainsBrackets(input)){
            System.out.println("You have inputted an invalid input! The string contains non-bracket characters.");
        }
        else if(isBalanced(input)){
            System.out.println("Balanced");
        }
        else{
            System.out.println("Not Balanced");
        }

        scanner.close();
    }
}
