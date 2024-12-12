import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TimeNoteTaker {
    private List<String> notes;
    TimeNoteTaker(){
        notes = Collections.synchronizedList(new ArrayList<>());
    }
    public <T, R> Function<T, R> noteTimeTaken(String nameOfFunction, Function<T, R> function){
        return (T value) -> {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.startStopwatch();
            R result = function.apply(value);
            notes.add(String.format("%s за %s секунд\n", nameOfFunction, stopwatch.stopAndGetFormatedTime()));
            return result;
        };
    }
    public <T> Supplier<T> noteTimeTaken(String nameOfSupplier, Supplier<T> supplier){
        return () -> {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.startStopwatch();
            T result = supplier.get();
            notes.add(String.format("%s за %s секунд\n", nameOfSupplier, stopwatch.stopAndGetFormatedTime()));
            return result;
        };
    }
    public <T> Consumer<T> noteTimeTaken(String nameOfConsumer, Consumer<T> consumer){
        return (T value) -> {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.startStopwatch();
            consumer.accept(value);
            notes.add(String.format("%s за %s секунд\n", nameOfConsumer, stopwatch.stopAndGetFormatedTime()));
        };
    }
    public Runnable noteTimeTaken(String nameOfRunnable, Runnable runnable){
        return () -> {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.startStopwatch();
            runnable.run();
            notes.add(String.format("%s за %s секунд\n", nameOfRunnable, stopwatch.stopAndGetFormatedTime()));
        };
    }
    public List<String> getAllNotes(){
        List<String> notes = this.notes;
        this.notes = Collections.synchronizedList(new ArrayList<>());
        return notes;
    }
}
