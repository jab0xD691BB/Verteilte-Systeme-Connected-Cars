# Verteilte Systeme - Praktikum - Connected Cars

### Allgemein

Mindestens drei Sensoren sollen die Zentrale(Server) mit Daten versorgen.  
Die Daten zur Zentrale werden per udp übertragen.
Die Zentrale hat auch ein HTTP-Server, damit man über ein Webbrowser, die Daten der Sensoren verfolgen kann. Die Übertragung geschieht über TCP-HTTP (Socket).  
Später werden die Daten der Sensoren vom Zentralserver, an den Server des Anbieters übermittelt.  
___
### Systeme
- min. 3 Sensoren
- Zentrale (UDP-Server, HTTP-Server?)
- Server des Anbieters
- externe Geräte (Webbrowser)
___
### __Anforderungsanalyse__  

Programmiersprache: Java  
IDE: IntelliJ  
Build-Tool: Maven  
Test: JUnit  
Deployment: Docker

___
__Zu Aufgabe 2 UDP Sockets__  
Die Sensoren erfassen jeweils folgende Werte: Füllstand des Tanks, Kilometerstand, Verkehrssituation und Durschnittsgeschwindigkeit. Diese werden zufällig in einem bestimmten Bereich z.B Verkehrssituation (frei, mäßiger Verrkehr, starker Verkehr, Stau), generiert. Jeder Sensor wird als eigenständiger Prozess bzw. als Container gestartet. Die Werte werden periodisch an den Zentralserver gesendet. Desweiteren werden noch die IP, Port sowie Typ des Sensors and die Zentrale verschickt und dort in der Konsole ausgeben. Die Kommunikation läuft über UDP - Socket und ist unidirektional.  

Der String, der Übertragen wird, sieht wie folgt aus: < IP-Adresse : Port : Sensorname : XY Wert >.

Funktionale Tests:  
Werden die Werte richtig übertragen?  
Werden die Daten richtig decodiert?

Nicht-Funktionale Tests:  
Wartbarkeit:   
Programmier Konventionen z.B Google Style Guide  
Testbarkeit:  


___
__Zu Aufgabe 2 HTTP TCP__  
Implementierung eines HTTP-Server, der den HTTP-GET Befehl korrekt ausführt. Der Zentralserver liefert dem HTTP-Server die Daten. Dabei bleibt die Zentrale mit den Sensoren und den HTTP Clients in kontakt. Alle Sensordaten werden gespeichert. HTTP greift über ein REST-API auf einzelne oder alle Sensordaten zu. Der HTTP-Client kann durch das Aufrufen eines URLs, sich die Daten ansehen.

Funktionale Tests:  
Werden die angeforderten Sensordaten richtig übertragen?


___
__Zu Aufgabe 3 RPC__  
Die aktuellen Werte der Sensoren wird über Thrift an die Server des Anbieters übertragen. Die Programmierschnittstelle wird an beiden Seiten implementiert (Zentrale und Client(Anbieter)). Der Anbieter speichert die übermittelten Daten persistent.  

Remote Procedure Call ermöglicht einen entfernteren Aufruf einer Prozedur. Mit Remote Procedure Call können Clients auf einem entfernten Server Prozeduren aufrufen.



___
__Aufgabe MoM mittels MQTT__  
UDP Verbindung zwischen UDP-Client & UDP-Server aus Aufgabe 1a wird ersetzt durch MQTT. 



___

__Status__

- [ ] Aufgabe Projektplan
- [ ] Aufgabe UDP TCP HTPP
- [ ] Aufgabe RPC
- [ ] Aufgabe MoM mittels MQTT
- [ ] Aufgabe Hochverfügbarkeit und Konsistenz
