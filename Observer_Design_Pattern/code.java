
import java.util.ArrayList;
import java.util.List;

interface ISubscriber {
    void update();
}

interface IChannel {
    void subscribe(ISubscriber s);
    void unsubscribe(ISubscriber s);
    void notifySubscribers();
}

class Channel implements IChannel {

    private List<ISubscriber> subscribers;
    private String name;
    private String latestVideo;

    public Channel(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void subscribe(ISubscriber s) {
        if (!subscribers.contains(s)) {
            subscribers.add(s);
        }
    }

    @Override
    public void unsubscribe(ISubscriber s) {
        subscribers.remove(s);
    }

    @Override
    public void notifySubscribers() {
        for (ISubscriber sub : subscribers) {
            sub.update();
        }
    }

    public void uploadVideo(String video) {
        latestVideo = video;

        System.out.println(
                "\n[" + name + " uploaded \"" + video + "\"]");

        notifySubscribers();
    }

    public String getVideo() {
        return "\nCheckout our new video: "
                + latestVideo + "\n";
    }
}

class Subscriber implements ISubscriber {

    private String name;
    private Channel channel;

    public Subscriber(String name, Channel channel) {
        this.name = name;
        this.channel = channel;
    }

    @Override
    public void update() {
        System.out.println(
                "Hey " + name + ","
                        + channel.getVideo());
    }
}

public class ObserverDemo {

    public static void main(String[] args) {

        Channel channel =
                new Channel("CoderArmy");

        Subscriber subs1 =
                new Subscriber("Varun", channel);

        Subscriber subs2 =
                new Subscriber("Tarun", channel);

        channel.subscribe(subs1);
        channel.subscribe(subs2);

        channel.uploadVideo(
                "Observer Pattern Tutorial");

        channel.unsubscribe(subs1);

        channel.uploadVideo(
                "Decorator Pattern Tutorial");
    }
}

