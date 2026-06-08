# Observer Design Pattern UML

```mermaid
classDiagram

class IChannel {
    <<interface>>
    +subscribe(ISubscriber s)
    +unsubscribe(ISubscriber s)
    +notifySubscribers()
}

class ISubscriber {
    <<interface>>
    +update()
}

class Channel {
    -String name
    -String latestVideo
    -List~ISubscriber~ subscribers

    +Channel(String name)
    +subscribe(ISubscriber s)
    +unsubscribe(ISubscriber s)
    +notifySubscribers()
    +uploadVideo(String video)
    +getVideo() String
}

class Subscriber {
    -String name
    -Channel channel

    +Subscriber(String name, Channel channel)
    +update()
}

IChannel <|.. Channel
ISubscriber <|.. Subscriber

IChannel --> "1..*" ISubscriber

Subscriber --> Channel

```

## Pattern Components

### Subject

* `IChannel`
* Defines methods to subscribe, unsubscribe and notify observers.

### Concrete Subject

* `Channel`
* Stores subscribers.
* Uploads videos.
* Notifies all subscribers when a new video is uploaded.

### Observer

* `ISubscriber`
* Defines the `update()` method.

### Concrete Observer

* `Subscriber`
* Receives notifications from the channel.
* Pulls the latest video information from the channel.

## Flow

1. Subscribers subscribe to a channel.
2. Channel uploads a new video.
3. Channel calls `notifySubscribers()`.
4. Every subscriber receives `update()`.
5. Subscriber fetches the latest video using `channel.getVideo()`.

```
```
