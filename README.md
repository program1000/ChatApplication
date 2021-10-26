Chatapplication
===============

## Requirements
jersey 2.33
jersey test framework 2.33
simpleserver 6.0.1
jackson 2.11.3


This is application is build as an alternative of using Spring. It is highly modularized and decoupled to increase maintainability.

It uses the Jersey framework and SimpleServer for implementation as webserver.
The way it is setup, it also could use other webserver implementation such as JDK server.
Because JDK server does not support SSE (JAX-RS 2.1), the default is SimpleServer.

Project hierarchy:
![](/images/project-hierarchy.png)

The structure consists of different modules, wich allows testing individually.
The modules are connected with connectors, which allows changing an implementation on either side.
Because the connectors use their own message objects, a change in the message definition is not propagated to the connectors interface.

Functional hierarchy:
![](/images/functional-hierarchy.png)

AccountServer keeps track of users, MessageServer keeps track of messages.
Session keeps a list of current users/messages online.
Security is used for identification.
WebServer contains the webapplication.

The webapplication uses only JavaScript with Bootstrap. It is a single page application. It futures movable chat dialogs. It supports multiple chat dialogs.
An user can login, request invite id, add others invite id, send and receive messages.
When messages can not be delivered, because user is offline, the messages are kept at the server. When is receiving user is online, it will receive undelivered messages.

## Images
![](/images/gui.png)
> Example of chat application in the browser

Notes:
- Maven is not used to keep the (unused) dependencies to a minimum.
- Error handling is incomplete
- Logging is not used

