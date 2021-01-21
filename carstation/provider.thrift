namespace java provider


service providerApi{

    void heartbeat(1: string name, 2: string serverIp, 3: i32 port);


    //falls primary aktiv ist 
    void sendCurrentToPrimary(1: map<string,string> sv, 2: string secondaryIp, 3: i32 secondaryPort);


    //falls secondary sich neu anmeldet soll Primary die komplette Datei an secondary schicken  - Server x und Server z
    void sendCompleteToSecondary(1: binary data);

    //danach soll primary die einzelnen Datensätze die er empfängt an seconardy schicken - Server x und Server z
    void sendCurrentToSecondary(1: map<string,string> sv);



    //void sendHistoryToPrimary(1: map<string,string> sensorHistory);
    //falls ein Server ausfällt soll Proxy eine History anlegen, falls beide server ausfallen sollten,
    //um die Konsistenz beizubehalten
		//oder beim start verhalten damit keine daten verloren gehen. 

}