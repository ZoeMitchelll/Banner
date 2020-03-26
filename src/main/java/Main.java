import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    static final String FILENAME = "generatedXULA.dat";
    static final int LINES_IN_FILE = 3325;
    static Random rand = new Random();

    public static void main(String[] args) {

        System.out.println("\n\n=====  Hello, Banner!  =====\n");

        // generate simulated data
        int numRecords = createBannerData(FILENAME, LINES_IN_FILE);

        System.out.println("\n" + FILENAME + " has " + numRecords + " record(s)");
        // TODO 5 - ternary operators are concise when writing simple if-else statements
        // System.out.println(FILENAME + " has " + numRecords + (numRecords > 1 ? " records" : " record"));

        // TODO 6 - use an exit statement to check an invariant
        if(numRecords != LINES_IN_FILE) {
            System.out.println("\n\n*ERROR* some of data was not read from " + FILENAME + " *ERROR*\n");
            System.exit(5);  // in Terminal type            echo $?
        }

        // TODO 7 - learn to run Java code from the commandline (in the Terminal)
        // $ javac Main.java
        // $ java Main

        // TODO 8 - use an assertion to check an invariant
        // $ java -ea file.java
        assert numRecords == LINES_IN_FILE: "\n*ERROR* not all data read *ERROR*";

        // read names from file
        String[] names = getNamesFromFile(FILENAME);
        // TODO 10 - convert array to List
        System.out.println("\toriginal names:\t\t" + Arrays.asList(names));

        // sort the names
        // TODO 11 - SWEs don't write sorts, they use APIs (pre-written, optimized sorts)
        // Collections.sort(names);  // can't use Collections methods with primitive arrays, only with JCF
        Collections.sort(Arrays.asList(names));
        System.out.println("\tsorted names:\t\t" + Arrays.asList(names));

        // TODO 12 - how many unique names
        Set<String> uniqueNames = new HashSet<>();
        uniqueNames.addAll(Arrays.asList(names));
        System.out.println("\nIn Set, " + names.length + " names and " + uniqueNames.size() + " in the set so " +
                (names.length-uniqueNames.size()) + " duplicate names");

        // TODO 13 - reverse the names
        System.out.println("\nReversing the primitive array using a Stack:");
        System.out.println("\tnames before reversing:      " + Arrays.asList(names));
        Stack<String> reversedNames = new Stack<>();
        reversedNames.addAll(Arrays.asList(names));
        System.out.println("\tstack outputs like a vector: " + reversedNames);
        // must pop everything from stack to see that it's reversed
        for (int i = 0; i < names.length; i++) {
            names[i] = reversedNames.pop();
        }
        System.out.println("\tnames after reversing:       " + Arrays.asList(names));

        // TODO 14 - use a Deque to work like a Stack
        System.out.println("\nReversing the primitive array using a Deque:");
        // A Deque is an interface so look at the Javadocs to see implementing classes
        System.out.println("\tnames before reversing:\t\t\t" + Arrays.asList(names));

        // if dump using another data structures' constructor, don't assume order
        Deque<String> stk = new ArrayDeque<>(Arrays.asList(names));
        System.out.println("\tdeque outputs like a vector:\t" + stk);

        // maybe dumping doesn't work the same as pushing each element in Deque
        stk = new ArrayDeque<>();
        for (int i = 0; i < names.length; i++) {
            stk.push(names[i]);
        }

        System.out.println("\tdeque outputs like a stack:\t\t" + stk);
        // must pop everything from stack to see that it's reversed
        for (int i = 0; i < names.length; i++) {
            names[i] = stk.pop();
        }
        System.out.println("\tnames after reversing:\t\t\t" + Arrays.asList(names));


        // TODO 15 - frequency of each name
        System.out.println("\nUsing Map (Dict, Associative Array):");
        Map<String, Integer> countNames1 = new HashMap<>();
        for(String name : names) {
            Integer count = countNames1.get(name);

            // ternary operator
            countNames1.put(name, (count == null) ? 1 : count + 1);
            // can use ternary operator to output singular vs plural
        }
        System.out.println("\tHow many of each name (1)? " + countNames1);

        // using Map.putIfAbsent()
        Map<String, Integer> countNames2 = new HashMap<>();
        for(String name : names) {
            Integer count = countNames2.putIfAbsent(name, 1);
            if(count != null) {
                countNames2.put(name, countNames2.get(name)+1);
            }
        }
        System.out.println("\tHow many of each name (2)? " + countNames2);

        // TODO 15 - did I keep all the data?
        assert countNames1.size() == countNames2.size() : "*Error* Same maps should have the same size";

        // TODO 16 - Java 8 using method references
        int total1 = countNames1
                .values()
                .stream()
                .mapToInt(Integer::valueOf)
                .sum();

        // TODO 17 - Java 8 using map-reduce
        int total2 = countNames2.entrySet()
                .stream()
                .map(c -> c.getValue())
                .reduce(0, Integer::sum);
        assert total1 == total2 : "*Error* Maps have different sum of values";

    }


    private static String[] getNamesFromFile(String filename) {
        String[] names = new String[LINES_IN_FILE];

        // TODO 9 - how to read from an input file
        try(Scanner reader = new Scanner(new File(FILENAME))) {
            int i = 0;
            while(reader.hasNext()) {
                String line = reader.nextLine();
                String[] splitLine = line.split("\\|");  // read https://www.vogella.com/tutorials/JavaRegularExpressions/article.html

                assert splitLine.length == 3 : "*ERROR* line " + (i+1) + " has fewer than 3 columns";

                names[i] = splitLine[1]; // only the names
                i = i + 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return names;
    }


    private static int createBannerData(String filename, int numOfLines) {
        String[] firstNames = {"Jeffrey", "Joshua", "Phillips", "Vincent", "Zo\u00eb", "\u20A6"};  // Naira symbol
        String[] lastNames  = {"Boudreaux", "Lewis", "Olagunju", "Stevenson", "Mitchell", "\u7535\u8111\u4F60\u597D\uFF01"};
        Scanner sc = new Scanner(System.in);
        int count = 0;

        // throw new FileNotFoundException("I'm just joking.  " + filename + " is found.");


        rand.setSeed(numOfLines);

        // generate random names, random real numbers, check if file already exists
        File f = new File(filename);
        if(f.exists() || f.isDirectory()) {
            System.out.print("\n\n*Alert* File already exists.  Erase (y/n)? ");
            String response = sc.next();
            if (response.equals("y")) {
                try (PrintWriter writer = new PrintWriter(filename)) {
                    for (int i = 0; i < numOfLines; i++) {
                        writer.println(
                                rand.nextInt(10_000) + "|" +
                                        firstNames[rand.nextInt(firstNames.length)] + " " +
                                        lastNames[rand.nextInt(lastNames.length)] + "|" +
                                        rand.nextDouble() * (4 - 0) + 0);  // 0 is min value, 4 is max value
                        count++;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("pick a new filename");
                filename = sc.next();
                createBannerData(filename, numOfLines);
            }
        }

        // why is it helpful to return the count? TO KEEP TRACK OF THE AMOUNT OF RECORDS THAT HAVE BEEN CREATED
        return count;
    }

}
