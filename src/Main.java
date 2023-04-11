import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        SLL AnimalSLL = new SLL(); // all animals in file


        // read animals from file and add to AnimalsSLL
        try {
            File file = new File("animals.txt");
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String data = input.nextLine();
                AnimalSLL.add(data);
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found...");
            e.printStackTrace();
        }

        // game

        Scanner input = new Scanner(System.in);
        Random rnd = new Random();


        System.out.print("Please enter n ");
        int n = input.nextInt();
        while (n> AnimalSLL.size()) {
            System.out.print("please enter a smaller value.");
            n = input.nextInt();
        }

        SLL SLL1 = selectAnimals(AnimalSLL, n); // sll1
        SLL SLL2 = createSLL2(SLL1); // sll2

        SLL SLL3 = new SLL(); // sll3 (names)
        SLL SLL4 = new SLL(); // sll4 (scores)

        System.out.println(String.format("SLL1: %-70s  \t Score: %d", printAnimals(SLL1), 0));
        System.out.println(String.format("SLL2: %-70s", printAnimals(SLL2)));

        int score = 0;

        int stepCount = 0; // tour count

        while (true) { // game loop
            stepCount++;

            int random1 = rnd.nextInt(SLL1.size()); // index of random animal in SLL1
            int random2 = rnd.nextInt(SLL2.size()); // index of random animal in SLL2

            // while printing, I am adding 1. Because index starts from 0.
            System.out.println(String.format("Randomly generated numbers %d  %d  %52s: %d ", (random1 + 1), (random2 + 1), "Step", stepCount));
            System.out.println();

            if (checkForIdenticalAnimals(SLL1, SLL2, random1, random2)) { // if animals are identical
                SLL1.remove(random1);
                SLL2.remove(random2);
                score += 20; // player gains 20 points
            } else {
                score -= 1; // player loses 1 point
            }

            System.out.println(String.format("SLL1: %-70s  \t Score: %d", printAnimals(SLL1), score));
            System.out.println(String.format("SLL2: %-70s", printAnimals(SLL2)));
            System.out.println();


            if (SLL1.size() == 0) { // when all tiles are deleted from the lists.
                System.out.println();
                System.out.println("The game is over.");
                break;
            }
        }


        String string = "You " + score; // string to write to file
        writeToFile(string);

        readHighScore(SLL3, SLL4); // read and sort high score table

        System.out.println("┌─────────────────┐");
        System.out.println("│High Score Table!│");
        System.out.println("│─────────────────│");
        highScoreTable(SLL3, SLL4);
        System.out.println("└─────────────────┘");

    }

    // random animal selection
    static SLL selectAnimals(SLL AnimalSLL, int n) {
        SLL SLL1 = new SLL();
        Random rnd = new Random();

        for (int i = 0; i < n; i++) {
            int index = rnd.nextInt(AnimalSLL.size()); // select random index from AnimalSLL
            String animal = (String) AnimalSLL.get(index);
            SLL1.add(animal); // add animal to SLL1
            AnimalSLL.remove(index); // remove selected animal from AnimalSLL
        }

        return SLL1;
    }


    static SLL createSLL2(SLL SLL1) {
        SLL tempSLL1 = new SLL();
        SLL SLL2 = new SLL();

        // copy SLL1 to tempSLL1
        for (int i = 0; i < SLL1.size(); i++) {
            tempSLL1.add(SLL1.get(i));
        }

        // shuffle tempSLL1 and add to SLL2
        Random rnd = new Random();
        int size = tempSLL1.size();
        while (size > 0) {
            int index = rnd.nextInt(tempSLL1.size());
            String animal = (String) tempSLL1.get(index);
            SLL2.add(animal);
            tempSLL1.remove(index);
            size--;
        }

        return SLL2;
    }

    // print lists
    static String printAnimals(SLL SLL) {
        String animals = "";
        for (int i = 0; i < SLL.size(); i++) {
            animals += SLL.get(i) + "   ";
        }
        return animals;
    }

    // check if selected animals are identical
    static boolean checkForIdenticalAnimals(SLL SLL1, SLL SLL2, int random1, int random2) {
        if (SLL1.get(random1).equals(SLL2.get(random2)))
            return true;
        else
            return false;
    }

    static void readHighScore(SLL SLL3, SLL SLL4) {

        try {
            File file = new File("highscoretable.txt");
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String name = input.next();
                int score = input.nextInt();
                SLL3.add(name);
                SLL4.add(score);
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found...");
            e.printStackTrace();
        }

        sortList(SLL3, SLL4);

    }

    static void sortList(SLL SLL3, SLL SLL4) {

        // sort SLL4 largest to smallest
        for (int i = 0; i < SLL4.size(); i++) {
            for (int j = 0; j < SLL4.size(); j++) {
                if ((int) SLL4.get(i) > (int)SLL4.get(j)) {
                    int temp = (int)SLL4.get(i); // swap
                    SLL4.set(i, SLL4.get(j)); // change positions
                    SLL4.set(j, temp);

                    String temp2 = (String) SLL3.get(i);
                    SLL3.set(i, SLL3.get(j));
                    SLL3.set(j, temp2);
                }
            }
        }
    }


    static void writeToFile(String line) {

        try {
            FileWriter myWriter = new FileWriter("highscoretable.txt", true); // "true" to print in the first empty place
            myWriter.write("\n" + line); // write to new line (\n)
            myWriter.close();
        } catch (IOException e) {
            System.out.println("While writing to file, an error occurred. :( ");
            e.printStackTrace();
        }

    }

    static void highScoreTable(SLL SLL3, SLL SLL4) {

        int count = 0;

        for (int i = 0; i < SLL3.size(); i++) {
            if (count < 12) {
                System.out.println(String.format("│   %-7s  %3d  │ ", SLL3.get(i), SLL4.get(i)));
                count++;
            }
        }


    }


}