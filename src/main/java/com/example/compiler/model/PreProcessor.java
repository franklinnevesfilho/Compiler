package com.example.compiler.model;

import java.io.*;
import java.util.*;

public class PreProcessor {
    // Reader
    BufferedReader reader;
    // File being read
    String file;
    // Stack
    Stack<String> stack = new Stack<>();

    // Delimiters
    List<String> openDelimiters = Arrays.asList("(","{","[","/","*");
    List<String> closeDelimiters = Arrays.asList(")","}","]");
    public boolean passed = false;

    public PreProcessor(){}

    public PreProcessor(String file) {
        this.file = file;
        stack = new Stack<>();
    }

    public void setFile(String file){
        this.file = file;
    }

    public void process(){
        // This variable ensures that each line is approved
        boolean approved = false;

        // This try catch block handles the buffer Reader
        try{
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                for(int i = 0; i < line.length(); i++) {
                    //This will first check if any of the openDelimiters are available
                    String curLetter = String.valueOf(line.charAt(i));
                    if(openDelimiters.contains(curLetter)) {
                        switch(checkCommentBlock(i,line)){
                            case -1: ;
                            case 0: stack.push(curLetter);
                            case 1:{
                                stack.push(curLetter);
                                stack.push(String.valueOf(line.charAt(i+1)));
                                i++;
                            }
                            case 2: {
                                stack.pop();
                                stack.pop();
                            }
                        }
                        // if the closeDelimiters are available then it will get the equivalent opener and compare to top of stack
                    }else if(!stack.isEmpty() && closeDelimiters.contains(String.valueOf(line.charAt(i)))){
                        if(openDelimiters.get(closeDelimiters.indexOf(String.valueOf(line.charAt(i)))).equals(stack.peek())){
                            // if they are the same then remove
                            stack.pop();
                            approved = true;
                        }else{
                            break;
                        }
                    }
                }
                if(!approved){
                    break;
                }
            }
            // if the stack is empty then the code returns true
            if(stack.isEmpty() && approved){
                passed = true;
            }
            reader.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(passed ? "passed" : "failed");
    }

    // This method is used as an exception handler
    private String getNextVal(int currentVal, String text){
        String returnVal;
        try{
            returnVal = String.valueOf(text.charAt(currentVal+1));
        }catch(StringIndexOutOfBoundsException e){
            returnVal = " ";
        }
        return returnVal;
    }

    int checkCommentBlock(int i,String line){
        // result = -1 is neither
        // result = 0 it is not a comment
        // result = 1 is an open comment block
        // result = 2 is a close comment block
        int result = 0;
        String nextVal = getNextVal(i, line);
        boolean forSlash = String.valueOf(line.charAt(i)).equals("/");
        boolean asterisk = String.valueOf(line.charAt(i)).equals("*");
        if(forSlash && (nextVal).equals("*")){
            result = 1;
        }else if(asterisk && (nextVal).equals("/")){
            result = 2;
        }else if(asterisk && !(nextVal).equals("/") || forSlash && !(nextVal).equals("*")){
            result = -1;
        }
        return result;
    }
}
