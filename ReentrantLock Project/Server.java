
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread{
    private static ReentrantLock lock = Restaurant.lock;
    public static Condition ordersToBeServed = lock.newCondition();

    private String name;
    private Restaurant restaurant;
    private String order;

    private int fish=0, pizza=0, burger=0;

    Server(String name){
        this.name = name;

        if(restaurant == null)
            restaurant = Restaurant.restaurant;
    }

    public void run(){
        System.out.println("Waiter " + name + " is working");

        while (true) {
            lock.lock();
            try {
                while(restaurant.getServeOrders().isEmpty())
                    ordersToBeServed.await();

                order = restaurant.dequeueServeOrder();
                System.out.println("Waiter " + name + " is serving " + order);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            finally {
                lock.unlock();
            }

            if (order.contains("Burger"))
                burger++;
            else if (order.contains("Pizza"))
                pizza++;
            else if (order.contains("Fish"))
                fish++;

            System.out.println(name + " is serving " + order);
            try {
                sleep((int) (100 * Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(restaurant.getOrderList().isEmpty()) {
                printStats();
                return;
            }
        }
    }

    public void printStats(){
        int total = burger + pizza + fish;

        String str = "Waiter " + name;
        str += " finished serving " + total + " orders";
        str += " including " + burger + " burgers";
        str += ", " + pizza + " pizzas";
        str += " and " + fish + " fish n chips";

        System.out.println(str);
    }
}
