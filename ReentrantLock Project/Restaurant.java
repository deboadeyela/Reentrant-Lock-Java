
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {
    public static void main(String[] args){
        new Restaurant();
    }

    public static Restaurant restaurant;
    public static ReentrantLock lock = new ReentrantLock(true);

    private Queue<String> orderList = new LinkedList<String>();
    private Queue<String> serveOrders = new LinkedList<String>();

    ExecutorService threadPool = Executors.newFixedThreadPool(5);
    private Restaurant(){
        restaurant = this;

        orderList = loadOrderList("orders.txt");

        Chef chef1 = new Chef("John");
        Chef chef2 = new Chef("Mark");

        Server waiter1 = new Server("Katie");
        Server waiter2 = new Server("Andrew");
        Server waiter3 = new Server("Emily");

        threadPool.execute(chef1);
        threadPool.execute(chef2);
        threadPool.execute(waiter1);
        threadPool.execute(waiter2);
        threadPool.execute(waiter3);

        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        chef1.printStats();
        chef2.printStats();
        waiter1.printStats();
        waiter2.printStats();
        waiter3.printStats();
    }


    private Queue<String> loadOrderList(String path){
        Queue<String> orderList = new LinkedList<>();

        File file = new File(path);
        Scanner scanner = null;

        try{
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert scanner != null;
        while (scanner.hasNextLine()){
            orderList.add(scanner.nextLine());
        }

        return orderList;
    }

    public String dequeueOrder(){
        return orderList.remove();
    }

    public Queue<String> getOrderList(){
        return orderList;
    }

    public void serveOrder(String order){
        serveOrders.add(order);
    }

    public Queue<String> getServeOrders(){
        return serveOrders;
    }

    public String dequeueServeOrder(){
        return serveOrders.remove();
    }
}
