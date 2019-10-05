#include <stdlib.h>
#include <connectionHandler.h>
#include <iostream>
#include <boost/thread.hpp>
#include <boost/chrono.hpp>
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
class SocketThread{
private:
    ConnectionHandler &connection;
public:
    SocketThread(ConnectionHandler &connection): connection(connection) {}

    void operator()(){
        while (1) {
            std::string answer;
            if (!connection.getLine(answer)) {
                break;
            }
            int len = answer.length();
            answer.resize(len - 1);
            std::cout << answer << std::endl;
            if (answer == "ACK signout succeeded") {
                std::cout << "Ready to exit. Press enter" << std::endl;
                break;
            }
        }
    }
};

class KeyboardThread{
private:
    ConnectionHandler &connection;
public:
    KeyboardThread(ConnectionHandler &connection): connection(connection) {}
    void operator()(){
        while(!std::cin.eof()){
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            if(line == ""){
                break;
            }
            if (!connection.sendLine(line)){
                break;
            }
        }
    }
};

int main (int argc, char *argv[]) {
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connection(host, port);
    if (!connection.connect()) {
        return 1;
    }

    KeyboardThread keyboard(connection);
    SocketThread socket(connection);
    boost::thread threadForKeyboard(keyboard);
    boost::thread threadForSocket(socket);
    threadForKeyboard.join();
    threadForSocket.join();
    return 0;
}
