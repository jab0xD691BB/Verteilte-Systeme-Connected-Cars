# Verteilte Systeme - Praktikum - Connected Cars

### Allgemein

In einem Auto(Subsystem) befinden sich vier Sensoren(Clients) und eine Zentrale die als Server ihre Aufgabe leistet. Die Sensoren versorgen im zwei Sekundentakt die Zentrale mit Daten, welche über UDP übertragen werden.
Dort werden die Nachrichten unter Angabe von IP, Port und Typ des Sensors auf der Standardausgabe ausgegeben.

Die Zentrale dient ebenfalls als ein HTTP-Server, damit man über ein Webbrowser, die Daten der Sensoren verfolgen kann. Die Kommunikation geschieht über TCP-HTTP (Socket).  
Später werden die Sensordaten vom Zentralserver, an einen Service Provider übermittelt, wo die Daten dann redundant ausgelegt werden sollen.

Abbildung 1 zeigt eine grobe Übersicht des Systems welches vorgegeben wurde.

![Alt-Text](/Abbildung1.png)

_Abbildung 1 - Vorgegebenes Systemdesign_

___
### __Anforderungsanalyse__  

Festlegung der Meilensteine:
1. Meilenstein (15.12.2020): Aufgabe 1 und 2. Lauffähig, Protokolliert und getestet.

    System: 

            - Vier Sensoren(Clients/Prozesse) die mittels UPD Socket mit dem Zentralserver kommunizieren:

                - Tanksensor;Kilometerstand;Verkehrssituation;Durschnittsgeschwindigkeit

                - Sensorwerte ändern sich ständig

                - Übertragung alle zwei Sekunden
                
                - Nachrichtenformat: JSON


            - Eine Zentrale(Server/Prozess) dient als UDP-Server und HTTP-Server:

                - Empfängt von Sensoren Nachrichten (per UDP Socket) die mit Angabe von IP, Port und Typ des Sensors auf der Standardausgabe ausgegeben werden. Diese werden zusätzlich in einer einfachen Form abgespeichert.

                - Sensorwerte werden langfristig abgespeichert um Sensordaten jederzeit abrufen zu können.
                
                - Ebenfalls dient der Server als HTTP-Schnittstelle mit HTTP GET, der über REST-API zugriff auf einzelne Sensordaten bietet, die abgespeichert vorliegen. 

            - Sensordaten abrufbar über eine URI ( bsp. http://127.0.0.1:3124/values/historyTankvalue, http://127.0.0.1:3124/values/currentTankvalue):

                - HTTP Kommunikation mit der Zentrale(Server) vom Browser aus.

2. Meilenstein (15.01.2021): Aufgabe 3 und 4. Lauffähig, Protokolliert und getestet.

    System: 

            - Zentrale sendet die aktuellen Werte der Sensoren, über einen RPC (Thrift) an die Cloud eines Dienstanbieters:

                - Dienstanbierter erzeugen (Service Provider).

                - Thrift-Datei beschriebene API am Server (Server des Anbieters) als auch am Client (Zentrale) implementiert.

                - Anbieter speichert die übermittelten Daten persistent.

            - Designänderung im Bereich der Übertragung der Sensorwerte vom Clienten an den Server:

                - Übertragung der Sensorwerte per MQTT an die Zentrale (Server).

3. Meilenstein (12.02.2021): Aufgabe 5. Lauffähig, Protokolliert und getestet. Projektabschluss!

    System: 

            - Server des Service Providers Redundant auslegen:

                - Auslegen der Daten auf zwei Server parallel in einer Primary-Secondary Architektur im Hot-Standby Betrieb.

                - Die zwei Server tauschen unter Verwendung eines RPCs (Thrift oder Protobuf) untereinander die empfangenen Daten aus und speichern diese konsistent.

                - Um die Ausfallsicherheit zu testen fallen während des Betriebs immer wieder zu zufälligen einzeln die Server aus. (Simuliert).

Programmiersprache: Java  
IDE: IntelliJ  
Build-Tool: Maven  
Test: JUnit  
Deployment: Docker

Zu jeder Aufgabenstellung (2-5) muss es jeweils einen Funktionalen, einen nicht Funktionalen und einen Performance Test geben. Diese werden weiter unten protokolliert. 

Beachten der nicht-Funktionalen Anforderungen:
1.  Hygiene des Git-Repositories

    Um unerwünschte Dateien aus den Git-Repositories fern zu halten, erhält das Repository eine vernünftige .gitignore Datei. Diese sorgt dafür, dass z.B. Bibliotheken und Kompilate nicht in das Repository eingecheckt werden können.

2.  Dokumentation

    Die Software wird in einer README.md Datei dokumentiert. Diese Datei beinhaltet eine detaillierte Anleitung wie die Software kompiliert und mittels Docker und Docker-Compose gestartet und getestet werden kann.

3.  Lizenzen

    Das Repository muss über ein Lizenz-File verfügen welches die Lizenz der Software ausweist.

4.  Docker und Docker-Compose

    Die Software wird containerisiert und läuft in Docker Version 19.03 sowie mit Docker-Compose Version 1.24. Das Docker-Compse File ist in Version 3.7 geschrieben.


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

- [x] Aufgabe Projektplan
- [x] Aufgabe UDP TCP HTPP
- [ ] Aufgabe RPC
- [ ] Aufgabe MoM mittels MQTT
- [ ] Aufgabe Hochverfügbarkeit und Konsistenz
