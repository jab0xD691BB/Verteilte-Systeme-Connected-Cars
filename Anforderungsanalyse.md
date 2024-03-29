# Verteilte Systeme - Praktikum - Connected Cars

### Allgemein

In einem Auto(Subsystem) befinden sich vier Sensoren(Clients) und eine Zentrale die als Server ihre Aufgabe leistet. Die Sensoren versorgen im zwei Sekundentakt die Zentrale mit Daten, welche über UDP übertragen werden.
Dort werden die Nachrichten unter Angabe von IP, Port und Typ des Sensors auf der Standardausgabe ausgegeben.

Die Zentrale dient ebenfalls als ein HTTP-Server, damit man über ein Webbrowser, die Daten der Sensoren verfolgen kann. Die Kommunikation geschieht über TCP-HTTP (Socket).  
Später werden die Sensordaten vom Zentralserver, an einen Service Provider übermittelt, wo die Daten dann redundant ausgelegt werden sollen.

Abbildung 1 zeigt eine grobe Übersicht des Systems welches vorgegeben wurde.

![Alt-Text](res/Abbildung1.png)

_Abbildung 1 - Vorgegebenes Systemdesign_

___
<br/>

Programmiersprache: Java  
IDE: IntelliJ  
Build-Tool: Maven  
Test: Manuel (händisch)
Deployment: Docker

Zu jeder Aufgabenstellung (2-5) muss es jeweils einen Funktionalen, einen nicht Funktionalen und einen Performance Test geben. Diese werden weiter unten protokolliert. 

Beachten der nicht-Funktionalen Anforderungen:
1.  Hygiene des Git-Repositories

    Um unerwünschte Dateien aus den Git-Repositories fern zu halten, erhält das Repository eine vernünftige .gitignore Datei. Diese sorgt dafür, dass z.B. Bibliotheken und Kompilate nicht in das Repository eingecheckt werden können.

2.  Dokumentation

    Die Software wird in einer README.md Datei dokumentiert. Diese Datei beinhaltet eine detaillierte Anleitung wie die Software kompiliert und mittels Docker und Docker-Compose gestartet und getestet werden kann.

3.  Lizenzen

    Das Repository muss über ein Lizenz-File verfügen welches die Lizenz der Software ausweist. Für dieses Projekt wird die MIT License verwendet.

4.  Docker und Docker-Compose

    Die Software wird containerisiert und läuft in Docker Version 19.03 sowie mit Docker-Compose Version 1.24. Das Docker-Compse File ist in Version 3.7 geschrieben.

___
### __Anforderungsanalyse__  
<br/>

__Festlegung der Meilensteine:__  
__1. Meilenstein (15.12.2020): Aufgabe 1 und 2. Lauffähig, Protokolliert und getestet.__

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

- Definierte Tests implementiert und durch geführt.

<br/>

![Alt-Text](res/Abbildung2.png)

_Abbildung 2 - Systemdesign zum Meilenstein 1_

___
<br/>

__2. Meilenstein (15.01.2021): Aufgabe 3 und 4. Lauffähig, Protokolliert und getestet.__

- Zentrale sendet die aktuellen Werte der Sensoren, über einen RPC (Thrift) an die Cloud eines Dienstanbieters:

- Dienstanbierter erzeugen (Service Provider).

- Thrift-Datei beschriebene API am Server (Server des Anbieters) als auch am Client (Zentrale) implementiert.

- Anbieter speichert die übermittelten Daten persistent.

- Die Thrift aka IDL(Interface Description Language) beinhaltet ein Service
  sendCurrent(1: map<string, string> cv) die für den Client und Server als Schnittstelle dient.
  Der Client(Zentrale) speichert die aktuellen Messwerte in einer HashMap und verschickt sie über die Funktion sendCurrent(currentValues) und Port 9090 an den Server.
  Der Server(Cloud des Dienstanbieters) empfängt die Daten über die sendCurrent(1: map<string, string cv>) Funktion und persistiert die aktuellen Messwerte der Sensoren in einer Text datei ("currentSensornameValues.txt").

- Designänderung im Bereich der Übertragung der Sensorwerte vom Clienten an den Server:

- Übertragung der Sensorwerte per MQTT an die Zentrale (Server).
                
- Die Sensoren publish die Messwerte, auf dem Topic "values/"Sensorname"", per MQTT an den Broker(mosquitto). 
  Die Zentrale wiederum subscribe alle Sensoren Topics, um vom Broker Nachrichten zu empfangen.

- Definierte Tests implementiert und durch geführt.

<br/>

![Alt-Text](res/Abbildung3.png)

_Abbildung 3 - Systemdesign zum Meilenstein 2_

___ 
<br/> 
  
__3. Meilenstein (12.02.2021): Aufgabe 5. Lauffähig, Protokolliert und getestet. Projektabschluss!__

- Server des Service Providers Redundant auslegen:

- Auslegen der Daten auf zwei Server parallel in einer Primary-Secondary Architektur im Hot-Standby Betrieb.

- Die zwei Server tauschen unter Verwendung eines RPCs (Thrift oder Protobuf) untereinander die empfangenen Daten aus und speichern diese konsistent.

- Um die Ausfallsicherheit zu testen fallen während des Betriebs immer wieder zu zufälligen einzeln die Server aus. (Simuliert).
- Ausfallsimulationszenario: Händischer hart stopp des Servers über Docker.

- Definierte Tests implementiert und durch geführt.

![Alt-Text](res/Abbildung4.png)     

_Abbildung 4 - Systemdesign zum Meilenstein 3_

<br/>
<br/>
Am 22.01.2020 kam eine neue Anforderung rein. So wurde unser Systemdesign angepasst nach Absprache im Praktikum.<br/>
<br/>
![Alt-Text](res/Abbildung5.png)     

_Abbildung 5 - Neues Systemdesign zur kurzfristigen Änderung zum Meilenstein 3_


___
<br/>

__Protokoll - Testverfahren zu Aufgabe 2__  


Funktionaler Test:  
Es wird die Formatierung von einem String in ein JSON Format getestet. Dazu wird ein JSONDummyObject erzeugt der die selben Daten wie der String enthält und der String wird in die Send Funktion übergeben, wo dann ein JSONObject aus dem String zurück gibt. Im anschlusss wird das DummyObject mit dem erzeugten Object verglichen.



nicht Funktionaler Test:
Es wird die Stabilität des Clients und des Server durch ein Stresstest überprüft.
Dazu erzeugt der Client 5000 Werte und sendet diese an den Server. Der Server muss diese empfangen und verarbeiten können. Client und Server dürfen nicht abstürzen.



Performance Test:
Es wird getestet, dass der Sensor die Werte schnell genug erzeugt und die auch in der vorgegenen Zeit vorbereitet und abschickt.
Gestoppt wird hierbei die Zeit vom begin der Erzeugung der Daten über die Konvertierung als JSON Objekt bishin zum abschicken über das Socket.
Diese gemessene Zeit sollte kleiner sein als die Zeit, die die Anforderung vorgibt immer Daten abzuschicken. In dem Fall zwei Sekunden. 

___
<br/>

__Protokoll - Testverfahren zu Aufgabe 3__  

Bitte Testflags beachten! Station - testflagmeilstein2 in der main ca 86

Funktionaler Test:  (Qualität von Thrift)

Vier Nachrichten verschicken und schauen ob diese korrekt Persistieren. 
Test ob die API funktioniert. (Thrift beschriebene Datei.)
Datei wird erzeugt und Testwerte werden mit Timestamp abgespeichert.

nicht Funktionaler Test:    (Persistens Test Serverseitig ausgehend von Thrift)

Korrektheit der IDL prüfen.
Speicherung einer Test File die vom Thrift kommt (eine Map mit zwei Strings) mit einem testwort. Öffnen und Speicherung des Wortes in der Datei. Anschließend soll des gespeicherten Wort ausgelesen werden. Am ende wird verglichen ob das ausgelesene Wort aus der Datei, dass ausgangswort unser erwartetes Ergebniss enthält. 0 1 2 3 in der Reihenfolge.

Performance Test:

Thriftserver mit Nachrichrten (1000 stück in einer kurzen Zeit) Bombadieren und schauen ob diese min. zu 90 % Persistieren. Das heißt (900 Stück kommen an und werden gespeichert). 
Die Nachrichten kommen an und werden zu 100% Persisiert.

___
<br/>

__Protokoll - Testverfahren zu Aufgabe 4__  


Bitte Testflags beachten! Station - testflag in der main ca. zeile 24

Funktionaler Test:  

Client gibt abgesendete Nachtichten auf der Console aus.
Server gibt Empfangene Nachrichten aus, somit wissen wir, dass die Nachrichten abgesendet und angekommen sind.
Das tun die auch.

nicht Funktionaler Test:    

Korrektheit der Nachrichten vergleichen. Wir verschicken Nachrichten an den Broker und schauen ob die am Subscriber korrekt ankommen. 
Korrektheit der Nachrichten bleibt bestehen, schon wie bei Aufgabenstellung 2.

Performance Test:

Server mit Nachrichten pentrieren und schauen ob diese ankommen. Somit prüfen die Schnittstellen von MQTT (Broker etc.).
Performance Test 100 %. Es werdem pro Sensor 1000 Testnachrichten abgeschickt und der Server empfängt von Mqtt Broker 4x1000 Nachrichten.


___
<br/>

__Protokoll - Testverfahren zu Aufgabe 5__  

Funktionaler Test:<br/>
Zum Debugen wird ausgegeben in welchen Zustand sich unser Proxies(Thrift-Service)befindet und was passieren muss. Außerdem 
steht drin wann zuletzt sich der Server mit einem Heartbeat gemeldet hat und welcher Server als Primary auserwählt wurde. Alive bedeutet das der Server innerhalb eines Zeitintervalls das Heartbeat geschickt hat und als Lebend angesehen wird.<br/>

thriftServer                 | Name: A  Time: 2021-02-09 11:43:15.3     Primary: true   Alive: true<br/>
thriftServer                 | Name: B  Time: 2021-02-09 11:43:14.891   Primary: false  Alive: true<br/>
thriftServer                 | Zustand: Daten an Primay schicken und Primary schickt an Secondary<br/>
<br/>
Falls unser Proxie Daten bekommt wird das ausgegeben:<br/>
<br/>
thriftServer                 | 1612870994726 SensorTyp: TankautoA - Sensorvalue: 76<br/>
thriftServer                 | 1612870994727 SensorTyp: KilometerstandautoA - Sensorvalue: 61<br/>
thriftServer                 | 1612870994727 SensorTyp: avgSpeedautoA - Sensorvalue: 55<br/>
thriftServer                 | 1612870994727 SensorTyp: VerkehrssituationautoA - Sensorvalue: m¦¦iger Verkehr<br/>
thriftServer                 | send to provider server<br/>
<br/>
Falls unsere Server die Daten erhalten kommt diese Nachricht:<br/>
<br/>
serverA                      | Ankommende Daten:<br/>
serverA                      | TankautoA        76       Port:9092<br/>
serverA                      | KilometerstandautoA      61       Port:9092<br/>
serverA                      | avgSpeedautoA    55       Port:9092<br/>
serverA                      | VerkehrssituationautoA   m¦¦iger Verkehr  Port:9092<br/>
<br/>
serverA                      | Einzelne Weiterleitung zum Secondary wird vorbereitet<br/>
<br/>
serverA                      | send to secondary server<br/>
<br/>

Port:9092 bedeutet dabei die weiterleitung an den Secondary Server. Diese wird vom Proxie an den Server mit gegeben.<br/>
Der Secondary Server gibts dann diese Nachricht aus:<br/>
<br/>
serverB                      | TankautoA        76       Port:0<br/>
serverB                      | KilometerstandautoA      61       Port:0<br/>
serverB                      | avgSpeedautoA    55       Port:0<br/>
serverB                      | VerkehrssituationautoA   m¦¦iger Verkehr  Port:0<br/>
<br/>
Dabei bedeutet Port:0 keine Weiterleitung.<br/>
<br/>
<br/>
Zudem wird ein Serverausfall simuliert und überprüft ob die Daten beim hochfahren des ausgefallenen Servers mit dem laufenden Server korrespondiert und diese zugleich Persistent bleibt die Files werden bei Docker händisch geöffnet und verglichen.
<br/>
<br/>
Wenn ein Server zum Secondary gewählt wird..(z.B. neu anschließt nach einem Ausfall)<br/>
<br/>
thriftServer                 | Name: B  Time: 2021-02-09 12:12:36.738   Primary: true   Alive: true<br/>
thriftServer                 | Zustand: Ankommende Daten an Primary schicken<br/>
serverA                      | Starting the simple providerApi server...<br/>
thriftServer                 | Name: B  Time: 2021-02-09 12:12:37.239   Primary: true   Alive: true<br/>
thriftServer                 | Name: A  Time: 2021-02-09 12:12:36.923   Primary: false  Alive: true<br/>
thriftServer                 | Zustand: Daten an Primay schicken und Primary schickt an Secondary<br/>
thriftServer                 | Name: B  Time: 2021-02-09 12:12:37.74    Primary: true   Alive: true<br/>
thriftServer                 | Name: A  Time: 2021-02-09 12:12:37.428   Primary: false  Alive: true<br/>
thriftServer                 | Zustand: Daten an Primay schicken und Primary schickt an Secondary<br/>
serverB                      | Komplette Weiterleitung zum Secondary wird vorbereitet<br/>
serverB                      | Sende currentTankautoB.txt<br/>
serverA                      | currentTankautoB.txt<br/>
serverA                      | Mon Feb 08 19:53:56 GMT 2021     TankautoB       9<br/>
serverA                      | Mon Feb 08 19:53:59 GMT 2021     TankautoB       78<br/>
serverA                      | Mon Feb 08 19:54:02 GMT 2021     TankautoB       91<br/>
...<br/>
und das für jede File..<br/>
<br/>
<br/>
nicht Funktionaler Test:<br/>
Serverausfall simulieren und prüfen ob Service weiterläuft, indem Station weiterhin Daten an den Service schickt.
Wenn kein Server im Provider Online ist, gibt unser Proxie No service Available aus.
<br/>
thriftServer                 | Zustand: No Server Available - nichts tun<br/>
<br/>
Wenn dann ein Server Online kommt läuft alles wie oben beschrieben im Funktionalen Teil weiter.
Hier wurde getestet wie gut und schnell der Proxie auf Veränderung reagieren kann. Ohne große Datenverluste zu haben.
Ergebnis: Unser Proxy reagiert und verarbeitet schnell Veränderungen, und geht kann sich ebenfalls um die Kommunikation kümmern.
<br/>
<br/>
Performance Test:<br/> 
Ein Server für circa 1 minute ausfallen und Daten synchroniseren lassen.
Damit wird die Performanz getestet bei einem Serverausfall wo sich bereits viele Daten angesammelt haben zu übertragen. Bei uns sind es acht Files die von den Sensoren kommen die wie beim Funktionalen Test bereits beschrieben auch auf der Konsole ausgegeben werden bei der Übertragung.
Am ende wurden händisch die Files verglichen.<br/>
Ergebnis: Das System ist nicht abgestürzt und hat alle Daten Persistent übertragen, zu 100%. 
___
<br/>

__Status__

- [x] Aufgabe Projektplan
- [x] Aufgabe UDP TCP HTPP
- [x] Aufgabe RPC
- [x] Aufgabe MoM mittels MQTT
- [x] Aufgabe Hochverfügbarkeit und Konsistenz
