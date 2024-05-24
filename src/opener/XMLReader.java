package opener;

import modifier.PeopleListModifier;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLReader {

    private String buffId = null;
    private String buffName = null;
    private String buffAddress = null;


    private static Scanner scanner;
    private Pattern pattern  = Pattern.compile("<(\\w+):(\\w+)>([^<]*)</\\1:\\2>");
    private Pattern attributePattern = Pattern.compile("[<]([a-zA-Z]+):([a-zA-Z]+)\\s+([a-zA-Z]+)\\s*=\\s*\"([0-9_]+)\"[>]");
    private Pattern namespacePattern = Pattern.compile("xmlns:(\\w+)=\"([^\"]+)\"");

    private PeopleListModifier peopleListModifier = new PeopleListModifier();

    public static Map<String, String> namespaces = new HashMap<>();
    private boolean namespaceMatchFound;


    public void OpenFile(String fileName){
        try {
            scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                String text = scanner.nextLine().trim();
                Matcher matcher = pattern.matcher(text);
                Matcher attributeMatcher = attributePattern.matcher(text);
                Matcher namespaceMatcher = namespacePattern.matcher(text);
                boolean matchFound = matcher.find();
                boolean attributeMatchFound = attributeMatcher.find();


                while (namespaceMatcher.find()){
                    namespaces.put(namespaceMatcher.group(1), namespaceMatcher.group(2));
                    text = scanner.nextLine().trim();
                    namespaceMatcher = namespacePattern.matcher(text);

                }

                if (attributeMatchFound && attributeMatcher.group(3).equals("id")) {
                    if(attributeMatcher.group(4).trim() == "") {
                        buffId = null;
                    }
                    buffId = attributeMatcher.group(4).trim();
                }
                if (matchFound && matcher.group(2).equals("name")) {
                    buffName = matcher.group(3).trim();
                }
                if (matchFound && matcher.group(2).equals("address")) {
                    buffAddress = matcher.group(3).trim();
                }
                if(text.equals("</h:person>")){
                    peopleListModifier.getPeople().add(new People(buffId, buffName, buffAddress));
                    buffId = null;
                    buffName= null;
                    buffAddress = null;
                }
            }
        }catch (IOException e){
            System.out.println("Open file exception! Cannot open or read file");
        }
    }

    public void CloseFile(){
        scanner.close();
    }
}
