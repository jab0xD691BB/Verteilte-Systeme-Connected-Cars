window.setInterval (requestData, 2000);

var request = new XMLHttpRequest();
var requestHistory = new XMLHttpRequest();

function process(_bestellung) {
    let jsonobj = JSON.parse(_bestellung);
    let sensorsWithSemi = jsonobj.activeSensor.split(" ", 3);
    let sensorsClean = sensorsWithSemi[2].split(";");
    
    let tanks = document.getElementById("Tank");
    tanks.style.color = "black";
    let kms = document.getElementById("Kilometerstand");
    kms.style.color = "black";
    let verks = document.getElementById("Verkehrssituation");
    verks.style.color = "black";
    let avgks = document.getElementById("avgSpeed");
    avgks.style.color = "black";


    for(let i = 0; i < sensorsClean.length; i++){
        if(sensorsClean[i] != ""){
        let ele = document.getElementById(sensorsClean[i]);
        
        if(ele != null){
            ele.style.color = "green";
            ele.disabled = false;
        }
    }
    }


}

function requestData() {
    request.open("GET", "/values/activeSensor.txt");
    request.onreadystatechange = processData;
    request.send(null);
}

function processData() {
    if (request.readyState == 4) {
        if (request.status == 200) {
            if (request.responseText != null){
                process(request.responseText)
            }else{
                console.error ("Dokument ist leer");
            }
            
        }else{
            console.error ("Uebertragung fehlgeschlagen");
        } 
    } else{

    }
}

