# Observer Design Pattern

```mermaid
classDiagram

class ISubscriber {
    <<interface>>
    +update()
}

class IChannel {
    <<interface>>
    +subscribe(ISubscriber)
    +unsubscribe(ISubscriber)
    +notifySubscribers()
}

class Channel {
    -List~ISubscriber~ subscribers
    -String name
    -String latestVideo

    +subscribe(ISubscriber)
    +unsubscribe(ISubscriber)
    +notifySubscribers()
    +uploadVideo(String)
    +getVideo() String
}

class Subscriber {
    -String name
    -Channel channel
    +update()
}

IChannel <|.. Channel
ISubscriber <|.. Subscriber

Channel "1" o-- "*" ISubscriber : observers
Subscriber --> Channel : observes
```

## Pattern Components

### Subject

* `Channel`
* Maintains list of subscribers
* Notifies subscribers when a new video is uploaded

### Observer

* `ISubscriber`
* Defines `update()` method

### Concrete Observer

* `Subscriber`
* Receives notifications from channel

### Flow

1. Subscriber subscribes to Channel.
2. Channel uploads a video.
3. Channel calls `notifySubscribers()`.
4. Every subscriber receives `update()`.
5. Unsubscribed users stop receiving notifications.
