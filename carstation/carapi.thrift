namespace java simpleServer


service sendReceive{
    void ping();
    void sendCurrent(1: map<string,string> sv);    
}