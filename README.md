# Client Server Application to Maintain Address Book

This is a client-server address book application  made in Java. It lets users manage an address book using command-line interface in the terminal. A comma seperated text file is maintained to store the records of the book.

## Authors

- Tejas Vaity

## Components

1. `Server.java`: The Server-side application that manages the address book in this part the address.txt file manages the records of the Address with comma seperated values.
2. `Client.java`: The client-side application that connects to the server and sends user commands like ADD, LIST, DELETE, SHUTDOWN, QUIT .

## Features

- Add new records to the address book
- Delete existing records
- List all the records in the address book
- Shutdown the Server
- Quit the Client

## Server (Server.java)

The server listens on port 3811 and handles incoming Client connection. It supports the following commands:
Note: Commands are case insensitive.

- `ADD <firstName> <lastName> <phoneNumber>`: Adds a new record to the address book
- `DELETE <recordID>`: Deletes a record from the address book
- `LIST`: Lists all records in the address book
- `SHUTDOWN`: Shuts down the server and also the Client
- `QUIT`: Closes the client connection

## Client (Client.java)

The client connects to the server and allows users to send commands through a Command-Line Interface.

## How to Run

### Compile the Java files
2 ways to compile 

1st Method is
```
javac Server.java
javac Client.java

```  
2nd Method is

make clean
make all

### Start the Server
```
java Server

```
### Start the Client
Start this client when the Server is Up after 2 seconds.

java Client <server_host>
```
Replace `<server_host>` with the hostname or IP address of the Server.
Example : java Client localhost
```
### Usage

Once Client is connected to the Server, Enter Commands at the Client prompt. The Server will respond with the results of each command.
A client-server interaction with the commands looks like

Example :
```
For ADD command since We made the case insensitive we can use both

c : ADD Tejas Vaity 313-266-1654
s : 200 OK
The new Record ID is 1001


c : add Tejas Vaity 313-266-1654
s : 200 OK
The new Record ID is 1001
```
For LIST command 

c : LIST
s : 200 OK
The list of records in the book:
1001    Tejas Vaity     313-266-1654
1002    Jinhua Guo      313-583-6439
1003    Sayojya Patil   313-431-2341

c : list
s : 200 OK
The list of records in the book:
1001    Tejas Vaity     313-266-1654
1002    Jinhua Guo      313-583-6439
1003    Sayojya Patil   313-431-2341

```
For DELETE command

c : DELETE 1003
s : 200 OK

c : delete 1003
s : 200 OK

```

For QUIT command

c : QUIT
s : 200 OK

c : quit
s : 200 OK

```
For SHUTDOWN command

c : SHUTDOWN
s : 200 OK

c : shutdown
s : 200 OK

```
