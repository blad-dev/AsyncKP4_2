import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class Main {
    public static Consumer<int[]> curryPrintArray(String note) {
        return (int[] array) -> printArray(note, array);
    }

    private static void printArray(String note, int[] array) {
        System.out.print(note);
        for (int element : array) {
            System.out.printf("%d ", element);
        }
        System.out.println();
    }

    private static void asyncProgram() {
        Joiner joiner = new Joiner();
        TimeNoteTaker timeNoteTaker = new TimeNoteTaker();

        CompletableFuture<int[]> numbers =
                CompletableFuture.supplyAsync(
                        timeNoteTaker.noteTimeTaken("Згенеровано початковий масив", () -> {
                            Util.sleepALittle();
                            return Util.genRandomArray(20, 0, 1000);
                        }
                        )
                );
        joiner.addToJoinList(
                numbers.thenAcceptAsync(
                        timeNoteTaker.noteTimeTaken(
                                "Виведено початковий масив",
                                curryPrintArray("Початковий масив: ")
                        )
                )
        );
        joiner.addToJoinList(numbers.thenApplyAsync(timeNoteTaker.noteTimeTaken("Обраховано масив попарних сум",
                (int[] array) -> {
                    int halfSize = (array.length + 1) / 2;
                    int[] newArray = new int[halfSize];

                    for(int i = 0; i < array.length; ++i){
                        newArray[i / 2] += array[i];
                    }
                    return newArray;
                }
        )).thenApplyAsync(timeNoteTaker.noteTimeTaken("Знайдено мінімальне значення серед масиву попарних сум",
                (int[] array) -> {
                    int min = array[0];
                    for(int i = 1; i < array.length; ++i){
                        if(min > array[i])
                            min = array[i];
                    }
                    return min;
                }
        )).thenAcceptAsync(timeNoteTaker.noteTimeTaken("Виведено мінімальне значення попарної суми",
                (Integer min) -> System.out.println("Мінімальне значення попарної суми: " + min)
        )).thenRunAsync(timeNoteTaker.noteTimeTaken("Виведено повідомлення про закінчення усіх обрахунків",
                () -> System.out.println("Закінчено усі обрахунки")
        )));

        joiner.joinAll();
        System.out.println();
        for(String note : timeNoteTaker.getAllNotes()){
            System.out.print(note);
        }
    }

    public static void main(String[] args) {
        CompletableFuture<Void> program = CompletableFuture.runAsync(
                Util.noteTimeTaken("Виконано усю програму", Main::asyncProgram)
        );
        program.join();
    }
}