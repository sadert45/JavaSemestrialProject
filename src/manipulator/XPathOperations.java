package manipulator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XPathOperations {

    String filePath = null;
    private Scanner scanner;
    Pattern pattern  = Pattern.compile("<(\\w+):(\\w+)>([^<]*)</\\1:\\2>");
    Pattern attributePattern = Pattern.compile("[<]([a-zA-Z]+):([a-zA-Z]+)\\s+([a-zA-Z]+)\\s*=\\s*\"([0-9_]+)\"[>]");
    Pattern tagPattern = Pattern.compile("[<]([a-zA-Z]+):([a-zA-Z\\/]+)[>]");


    public XPathOperations(String filePath) {
        this.filePath = filePath;
        try {
            scanner = new Scanner(new File(filePath));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private Matcher fileScanner(){
        while (scanner.hasNext()) {
            String text = scanner.nextLine();
            Matcher matcher = pattern.matcher(text);
            Matcher tagMatcher = tagPattern.matcher(text);
            Matcher attributeMatcher = attributePattern.matcher(text);
            boolean attributeMatchFound = attributeMatcher.find();
            boolean tagMatchFound = tagMatcher.find();
            boolean matchFound = matcher.find();
            if(attributeMatchFound)
                return attributeMatcher;
            if (matchFound)
                return matcher;
            if(tagMatchFound)
                return tagMatcher;
        }
        return null;
    }

    public String descendant(String parent){
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        int counter = 0;
        while (scanner.hasNext()){
            Matcher matcher = fileScanner();
            if (matcher != null && matcher.group(2).equals(parent)) {
                while (scanner.hasNext()) {
                    line = scanner.nextLine().trim();
                    if (line.contains(parent) && line.startsWith("</")) {
                        break;
                    }
                    stringBuilder.append(line).append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }

    public String parent(String child){
        String currentTag =  null;
        while (scanner.hasNext()){
            String line = scanner.nextLine().trim();
            Matcher matcher = pattern.matcher(line);
            if(!matcher.find()){
                if(line.contains(child) && !line.startsWith("</")){
                    return currentTag;
                }
                currentTag = line;
            }
            if(line.contains(child) && !line.startsWith("</")){
                return currentTag;
            }
        }
        return null;
    }

    public List<String> child(String key){
        List<String> keys = new ArrayList<>();
        String childLine = null;
        String line = null;
        int counter = 0;
        while (scanner.hasNext()){
            line = scanner.nextLine().trim();
            if (line.contains(key) && !line.startsWith("</")) {
                line = scanner.nextLine().trim();
                Matcher matcher = attributePattern.matcher(line);
                if (matcher.find())
                    childLine = matcher.group(2);
                else {
                    matcher = tagPattern.matcher(line);
                    if (matcher.find())
                        childLine = matcher.group(2);
                }
                while (scanner.hasNext()) {
                    if (line.contains(childLine) && !line.startsWith("</")) {
                        keys.add(line.trim());
                        while (scanner.hasNext()) {
                            matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                line = scanner.nextLine().trim();
                                keys.add(line.trim());
                                line = scanner.nextLine().trim();
                            } else {
                                line = scanner.nextLine().trim();
                                break;
                            }
                        }

                    } else
                        line = scanner.nextLine().trim();
                }
            }
        }
        return keys;
    }

    public List<String> ancestor(String key){
        List<String> keys = new ArrayList<String>();
        String line = null;
        while (scanner.hasNext()){
            line = scanner.nextLine().trim();
            Matcher matcher = pattern.matcher(line);
            if(!matcher.find()){
                if(line.contains(key) && !line.startsWith("</")){
                    return keys;
                }
                keys.add(line);
            }
            if(line.contains(key) && !line.startsWith("</")){
                return keys;
            }
        }
        return keys;
    }

    public List<String> findByAttributeOperator(String key){
        scanner.reset();
        List<String> keyArray = new ArrayList<String>();
        while (scanner.hasNext()){
            Matcher matcher = fileScanner();
            if(matcher!=null && matcher.groupCount() > 2 && matcher.group(3).equals(key)){
                keyArray.add(matcher.group(4));
            }
        }
        return keyArray;
    }

    public List<String> findAllOperator(String key){
        scanner.reset();
        List<String> keyArray = new ArrayList<String>();
        while (scanner.hasNext()){
            Matcher matcher = fileScanner();
            if(matcher != null && matcher.group(2).equals(key)){
                keyArray.add(matcher.group(matcher.groupCount()));
            }
        }
        return keyArray;
    }

    public String findByIndexOperator(String key, String n){
        scanner.reset();
        int counter = 0;
        while (scanner.hasNext()){
            Matcher matcher = fileScanner();
            if(matcher!=null && matcher.group(2).equals(key)){
                if (counter == Integer.parseInt(n))
                    return matcher.group(matcher.groupCount());
                else
                    counter++;
            }
        }
        return null;
    }

    public List<String> compareOperation(String key, String value, String outputKey){
        scanner.reset();
        List<String> keyArray = new ArrayList<String>();
        while (scanner.hasNext()){
            Matcher matcher = fileScanner();
            String saveKey = null;
            if(matcher != null && matcher.groupCount() > 2 && matcher.group(3).equals("id")){
                while (scanner.hasNext()){
                    if(matcher.group(2).equals(outputKey)){
                        saveKey = matcher.group(3);
                    }
                    if(matcher.group(2).equals(key) && matcher.group(3).trim().equals(value)){
                        if(saveKey!=null) {
                            keyArray.add(saveKey);
                            break;
                        }
                        matcher = fileScanner();
                        if(matcher.group(2).equals(outputKey)){
                            keyArray.add(matcher.group(3));
                            break;
                        }
                    }else{
                        matcher = fileScanner();
                    }
                }
            }
        }
        return keyArray;
    }
}
