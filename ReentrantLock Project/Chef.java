
import java.util.concurrent.locks.ReentrantLock;

public class Chef extends Thread{

    private String name;
    private static Restaurant restaurant;
    private String order;

    private static ReentrantLock lock = Restaurant.lock;

    private int fish=0, pizza=0, burger=0;

    Chef(String name){
        this.name = name;

        if(restaurant == null)
            restaurant = Restaurant.restaurant;
    }

    @Override
    public void run(){
        System.out.println("Chef " + name + " is working");

        while (!restaurant.getOrderList().isEmpty()){
            lock.lock();
            try {
                if(!restaurant.getOrderList().isEmpty())
                    order = restaurant.dequeueOrder();

            } finally {


                if (order.contains("Burger"))
                    burger++;
                else if (order.contains("Pizza"))
                    pizza++;
                else if (order.contains("Fish"))
                    fish++;

                System.out.println(name + " is preparing " + order);
                try {
                    sleep((int) (100 * Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                restaurant.serveOrder(order);
                Server.ordersToBeServed.signalAll();

                lock.unlock();
            }
        }

        printStats();
    }

    public void printStats(){
        int total = burger + pizza + fish;

        String str = "Chef " + name;
        str += " finished preparing " + total + " orders";
        str += " including " + burger + " burgers";
        str += ", " + pizza + " pizzas";
        str += " and " + fish + " fish n chips";

        System.out.println(str);
    }
}