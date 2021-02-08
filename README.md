# VS README
Vorraussetzung ist das Docker lauffähig vorinstalliert ist.   
### build and run
1. Shell in Linux oder Windows öffnen

2. Shellpfad: In Projektverzeichnis wechseln

2. Folgende befehle nacheinander eingeben:
- docker-compose -f start.yml build
- docker-compose -f start.yml up

3. Enjoy and chill


### Zugriff auf die Daten über die URI

Station A (für Werte von Auto A):

http://127.0.0.1:3124/ 

http://127.0.0.1:3124/values/currentavgSpeedvalue.txt

http://127.0.0.1:3124/values/historyavgSpeedvalue.txt

http://127.0.0.1:3124/values/currentTankvalue.txt

http://127.0.0.1:3124/values/historyTankvalue.txt

http://127.0.0.1:3124/values/currentKilometerstandvalue.txt

http://127.0.0.1:3124/values/historyKilometerstandvalue.txt

http://127.0.0.1:3124/values/currentavgSpeedvalue.txt

http://127.0.0.1:3124/values/historyavgSpeedvalue.txt

Station B (für Werte von Auto B):

http://127.0.0.1:3125/ 

http://127.0.0.1:3125/values/currentavgSpeedvalue.txt

http://127.0.0.1:3125/values/historyavgSpeedvalue.txt

http://127.0.0.1:3125/values/currentTankvalue.txt

http://127.0.0.1:3125/values/historyTankvalue.txt

http://127.0.0.1:3125/values/currentKilometerstandvalue.txt

http://127.0.0.1:3125/values/historyKilometerstandvalue.txt

http://127.0.0.1:3125/values/currentavgSpeedvalue.txt

http://127.0.0.1:3125/values/historyavgSpeedvalue.txt

-----------------------------------------------------------

Das Programm kompiliert automatisch und es ist nichts weiter zu beachten.
