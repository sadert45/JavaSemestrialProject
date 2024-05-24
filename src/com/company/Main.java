package com.company;


import manipulator.ObjectManipulator;
import manipulator.XPathOperations;
import modifier.Printer;
import opener.XMLReader;
import saver.Saver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args){
        boolean isFileOpen = false;
        boolean isFileSaved = false;
        String filePath = null;


        Pattern openPattern  = Pattern.compile("([o][p][e][n])\\s?([a-zA-Z0-9:/\\\\\\\\. ]+)");
        Pattern printPattern = Pattern.compile("([p][r][i][n][t]\\s?)");
        Pattern savePattern = Pattern.compile("(save)\\b");
        Pattern selectPattern = Pattern.compile("([s][e][l][e][c][t]+)\\s?([0-9_\\s?]+)\\s?([a-zA-Z]+)");
        Pattern nChildPattern = Pattern.compile("\\b([c][h][i][l][d]+)\\s?([0-9_\\s?]+)\\s?([0-9]+)");
        Pattern deletePattern = Pattern.compile("([d][e][l][e][t][e]+)\\s?([0-9_]+)\\s?([a-zA-Z]+)");
        Pattern saveAsPattern = Pattern.compile("\\b([s][a][v][e][a][s])\\s?([a-zA-Z0-9:/\\\\\\\\. ]+)");
        Pattern textPattern = Pattern.compile("([t][e][x][t]+)\\s?([0-9_]+)");
        Pattern newChildPattern = Pattern.compile("([n][e][w][c][h][i][l][d]+)\\s?([0-9_]+)");
        Pattern childrenPattern = Pattern.compile("([c][h][i][l][d][r][e][n]+)\\s?([0-9_]+)");
        Pattern setPattern = Pattern.compile("([s][e][t]+)\\s?([0-9_\\s?]+)\\s?([a-zA-Z]+)\\s?([a-zA-Z0-9\\s?]+)");
        Pattern closePattern = Pattern.compile("([c][l][o][s][e]\\s?)");
        Pattern exitPattern = Pattern.compile("([e][x][i][t]\\s?)");
        Pattern helpPattern = Pattern.compile("([h][e][l][p]\\s?)");

        Pattern XPathFindAllPattern = Pattern.compile("([a-zA-Z])\\s?([/\\\\])(?![0-9])([a-zA-Z0-9]+)(?!.*\\[[^\\]]*\\]).*$");
        Pattern XPathFindByIndexPattern = Pattern.compile("([a-zA-Z])\\s?([/\\\\])([a-zA-Z0-9]+)\\[([0-9])\\]");
        Pattern xPathFindByAttributePattern = Pattern.compile("([a-zA-Z])\\s?\\(@([a-zA-Z]+)\\)");
        Pattern xPathComparePattern = Pattern.compile("([p][e][r][s][o][n])\\s?\\(([a-zA-Z]+)\\s?\\=\\s?\\\"([a-zA-Z0-9\\s?]+)\\\"\\)\\s?\\/([a-zA-Z]+)");

        Pattern ancestorPattern = Pattern.compile("(ancestor)\\:\\:([a-zA-Z0-9\\*]+)");
        Pattern childPattern = Pattern.compile("([a-zA-Z]+)[\\/](child)");
        Pattern parentPattern = Pattern.compile("([a-zA-Z]+)[\\/](parent)");
        Pattern descendantPattern = Pattern.compile("([a-zA-Z]+)[\\/](descendant)");

        System.out.println("Enter your next command(you can write help" +
                " to see list of all commands)");
        while (true) {
            Scanner consoleScanner = new Scanner(System.in);
            String inputData = consoleScanner.nextLine();

            Matcher openMatcher = openPattern.matcher(inputData);
            if (openMatcher.find()) {
                try {
                    if (!isFileOpen) {
                        XMLReader opener = new XMLReader();
                        opener.OpenFile(openMatcher.group(2));
                        filePath = openMatcher.group(2);
                        isFileOpen = true;
                        System.out.println("Successfully opened file");
                    }
                } catch (Exception e) {
                    System.out.println("Cannot open this file or incorrect file name!");
                }
            }

            Matcher printMatcher = printPattern.matcher(inputData);
            if (printMatcher.find()) {
                if (isFileOpen) {
                    Printer printer = new Printer();
                    printer.Printer();
                } else {
                    System.out.println("File is not open");
                }
            }
            Matcher saveMatcher = savePattern.matcher(inputData);
            if(saveMatcher.find()){
                if(isFileOpen){
                    Saver saver = new Saver();
                    saver.saver(filePath);
                    System.out.println("Successfully saved file");
                    isFileSaved = true;
                }else{
                    System.out.println("Cannot save this file!");
                }
            }
            Matcher saveAsMatcher = saveAsPattern.matcher(inputData);
            if(saveAsMatcher.find()){
                if(isFileOpen){
                    Saver saver = new Saver();
                    saver.saver(saveAsMatcher.group(2));
                    System.out.println("Successfully saved file");
                    isFileSaved = true;
                }else{
                    System.out.println("Cannot save this file!");
                }
            }
            Matcher exitMatcher = exitPattern.matcher(inputData);
            if(exitMatcher.find()){
                System.exit(0);
            }

            Matcher closeMatcher = closePattern.matcher(inputData);
            if(closeMatcher.find()){
                if(isFileOpen){
                    XMLReader closer = new XMLReader();
                    closer.CloseFile();
                    isFileOpen = false;
                    System.out.println("Successfully closed file");
                }
            }

            Matcher newChildMather = newChildPattern.matcher(inputData);
            if(newChildMather.find()){
                if(isFileOpen){
                    ObjectManipulator objectManipulator = new ObjectManipulator(filePath);
                    objectManipulator.newChild(newChildMather.group(2));
                }
            }

            Matcher nChildMather = nChildPattern.matcher(inputData);
            if(nChildMather.find()){
                if(isFileOpen){
                    ObjectManipulator objectManipulator = new ObjectManipulator(filePath);
                    System.out.println(objectManipulator.childElement(nChildMather.group(2).trim(), nChildMather.group(3)));
                }
            }

            Matcher selectMatcher = selectPattern.matcher(inputData);
            if(selectMatcher.find()){
                if(isFileOpen) {
                    ObjectManipulator objectManipulator = new ObjectManipulator(filePath);
                    System.out.println(objectManipulator.selectElement(selectMatcher.group(2).trim(), selectMatcher.group(3).trim()));
                }
            }
            Matcher setMatcher = setPattern.matcher(inputData);
            if(setMatcher.find()){
                if(isFileOpen) {
                    ObjectManipulator objectManipulator = new ObjectManipulator(filePath);
                    objectManipulator.setElement(setMatcher.group(2).trim(), setMatcher.group(3).trim(), setMatcher.group(4).trim());
                }
            }

            Matcher deleteMatcher = deletePattern.matcher(inputData);
            if(deleteMatcher.find()){
                if(isFileOpen){
                    ObjectManipulator objectManipulator = new ObjectManipulator(filePath);
                    objectManipulator.deleteElement(deleteMatcher.group(2).trim(), deleteMatcher.group(3).trim());
                }
            }

            Matcher textMatcher = textPattern.matcher(inputData);
            if(textMatcher.find()){
                if(isFileOpen){
                    ObjectManipulator objectManipulator = new ObjectManipulator(filePath);
                    objectManipulator.textOfElement(textMatcher.group(2).trim());
                }else{
                    System.out.println("File is not opened!");
                }
            }

            Matcher childrenMather = childrenPattern.matcher(inputData);
            if(childrenMather.find()){
                if(isFileOpen){
                    ObjectManipulator objectManipulator = new ObjectManipulator(filePath);
                    objectManipulator.childrenElements(childrenMather.group(2).trim());
                }
            }

            Matcher xPathFindAllMatcher = XPathFindAllPattern.matcher(inputData);
            if(xPathFindAllMatcher.find()){
                if(isFileOpen){
                    List<String> keyArray = new ArrayList<String>();
                    XPathOperations xPathOperations = new XPathOperations(filePath);
                    keyArray = xPathOperations.findAllOperator(xPathFindAllMatcher.group(3));
                    for(int i = 0; i < keyArray.size();i++){
                        System.out.println(keyArray.get(i));
                    }
                }else
                    System.out.println("File is not open");
            }

            Matcher xPathFindByIndexMatcher = XPathFindByIndexPattern.matcher(inputData);
            if(xPathFindByIndexMatcher.find()){
                if(isFileOpen){
                    XPathOperations xPathOperations = new XPathOperations(filePath);
                    System.out.println(xPathOperations.findByIndexOperator(xPathFindByIndexMatcher.group(3).trim(), xPathFindByIndexMatcher.group(4)));
                }
            }

            Matcher xPathFindByAttributeMatcher = xPathFindByAttributePattern.matcher(inputData);
            if(xPathFindByAttributeMatcher.find()){
                if(isFileOpen){
                    List<String> keyArray = new ArrayList<String>();
                    XPathOperations xPathOperations = new XPathOperations(filePath);
                    keyArray = xPathOperations.findByAttributeOperator(xPathFindByAttributeMatcher.group(2));
                    for(int i = 0; i < keyArray.size();i++){
                        System.out.println(keyArray.get(i));
                    }
                }
            }

            Matcher xPathCompareMatcher = xPathComparePattern.matcher(inputData);
            if(xPathCompareMatcher.find()){
                if(isFileOpen){
                    List<String> keyArray = new ArrayList<String>();
                    XPathOperations xPathOperations = new XPathOperations(filePath);
                    keyArray = xPathOperations.compareOperation(xPathCompareMatcher.group(2),xPathCompareMatcher.group(3).trim(),xPathCompareMatcher.group(4));
                    if(keyArray!=null) {
                        for (int i = 0; i < keyArray.size(); i++) {
                            System.out.println(keyArray.get(i));
                        }
                    }else{
                        System.out.println("No matches found");
                    }
                }
            }

            Matcher ancestorMatcher = ancestorPattern.matcher(inputData);
            if(ancestorMatcher.find()){
                if(isFileOpen){
                    List<String> keyArray = new ArrayList<String>();
                    XPathOperations xPathOperations = new XPathOperations(filePath);
                    keyArray = xPathOperations.ancestor(ancestorMatcher.group(2));
                    for(int i = 0; i < keyArray.size();i++){
                        System.out.println(keyArray.get(i));
                    }
                }
            }

            Matcher descendantMatcher = descendantPattern.matcher(inputData);
            if(descendantMatcher.find()){
                if (isFileOpen){
                    XPathOperations xPathOperations = new XPathOperations(filePath);
                    System.out.println(xPathOperations.descendant(descendantMatcher.group(1)));
                }
            }

            Matcher parentMatcher = parentPattern.matcher(inputData);
            if(parentMatcher.find()){
                if(isFileOpen){
                    XPathOperations xPathOperations = new XPathOperations(filePath);
                    System.out.println(xPathOperations.parent(parentMatcher.group(1)));
                }
            }

            Matcher childMatcher = childPattern.matcher(inputData);
            if(childMatcher.find()){
                if(isFileOpen){
                    List<String> keyArray = new ArrayList<String>();
                    XPathOperations xPathOperations = new XPathOperations(filePath);
                    keyArray = xPathOperations.child(childMatcher.group(1));
                    for(int i = 0; i < keyArray.size();i++){
                        System.out.println(keyArray.get(i));
                    }
                }
            }

            Matcher helpMatcher = helpPattern.matcher(inputData);
            if(helpMatcher.find()){
                System.out.println("Following commands are supported:");
                System.out.println("\n\nBasic block:");
                System.out.println("open <file>                                            : opens <file>");
                System.out.println("close                                                  : close currently opened file");
                System.out.println("save                                                   : save currently opened file");
                System.out.println("saveas <file>                                          : save currently opened file in <file>");
                System.out.println("print                                                  : print information from currently opened file");
                System.out.println("select <id> <key>                                      : print <attribute> of <id> element");
                System.out.println("set <id> <key> <value>                                 : change or set <value> of <attribute> " +
                        "of <id> element");
                System.out.println("children <id>                                          : list of all children of element <id>");
                System.out.println("child <id> <n>                                         : access to <n> child of <id> element");
                System.out.println("text <id>                                              : XML text of <id> element");
                System.out.println("delete <id> <key>                                      : deleting of <key> of <id> element");
                System.out.println("newchild <id>                                          : creating of new child with <id> " +
                        "and without children");
                System.out.println("\n\nXPath block:");
                System.out.println("<parent tag>/<child tag>                               : output list of all <child tag> in file");
                System.out.println("<parent tag>/<child tag>[index]                        : output <child tag> of <n> <parent tag> element");
                System.out.println("<parent tag>(@<attribute>)                             : output all elements with <attribute>");
                System.out.println("<parent tag>(<child tag>=\"<value>\")/<output value>     : output list of <output value>" +
                        "where <child tag> equals to <value>");
                System.out.println("\n\nXpath axes block:");
                System.out.println("ancestor::<tag>or*                                     : output all tags till" +
                        "<tag> or if \"*\" output all file ");
                System.out.println("<tag>/child                                            : output children of all" +
                        "<tag> elements in file");
                System.out.println("<tag>/parent                                           : output parent of <tag>");
                System.out.println("<tag>/descendant                                       : output all text between <tag>" +
                        "and </tag>");
                System.out.println("exit                                                   : close program");
            }
        }
    }
}
