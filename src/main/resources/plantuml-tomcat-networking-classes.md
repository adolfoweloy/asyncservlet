@startuml
interface Runnable {
run()
}
class Acceptor {
}
class Poller {
register(socketWrapper)
}
class SocketChannel {
}
class NioSocketWrapper {
}
class NioEndpoint {
setSocketOptions
}
Acceptor .up.|> Runnable
Poller .up.|> Runnable

Acceptor -down-> NioEndpoint: endpoint

NioEndpoint -up-> Poller: poller
NioEndpoint .left.> SocketChannel: set options
NioEndpoint .right.> SocketProcessor: creates

NioSocketWrapper -down-> SocketChannel
Poller .left.> NioSocketWrapper

@enduml