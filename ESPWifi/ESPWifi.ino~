/**
* Author Shazin Sadakath
*/

#include "ESP8266.h"
#include <SoftwareSerial.h>

#define SSID        "<SSID>"
#define PASSWORD    "<Password>"
#define HOST_NAME   "192.168.0.100"
#define HOST_PORT   (8090)
#define GREEN_PIN    11
#define RED_PIN      12

SoftwareSerial mySerial(3, 2);
ESP8266 wifi(mySerial);

void setup(void)
{
    Serial.begin(9600);
    Serial.print("setup begin\r\n");
    
    Serial.print("FW Version:");
    Serial.println(wifi.getVersion().c_str());
      
    if (wifi.setOprToStation()) {
        Serial.print("to station ok\r\n");
    } else {
        Serial.print("to station err\r\n");
    }
 
    if (wifi.joinAP(SSID, PASSWORD)) {
        Serial.print("Join AP success\r\n");
        Serial.print("IP:");
        Serial.println( wifi.getLocalIP().c_str());       
    } else {
        Serial.print("Join AP failure\r\n");
    }
    
    if (wifi.disableMUX()) {
        Serial.print("single ok\r\n");
    } else {
        Serial.print("single err\r\n");
    }
    
    Serial.print("setup end\r\n");
    
    pinMode(GREEN_PIN, OUTPUT);
    pinMode(RED_PIN, OUTPUT);
}
 
void loop(void)
{
    uint8_t buffer[128] = {0};
    
    if (wifi.createTCP(HOST_NAME, HOST_PORT)) {
        Serial.print("create tcp ok\r\n");
    } else {
        Serial.print("create tcp err\r\n");
    }
    
    char *hello = "GET\n";
    wifi.send((const uint8_t*)hello, strlen(hello));
    
    uint32_t len = wifi.recv(buffer, sizeof(buffer), 10000);
    if (len > 0) {
        Serial.print("Received:[");
        for(uint32_t i = 0; i < len; i++) {
            Serial.print((char)buffer[i]);
            if(i == 0 && ((char)buffer[i]) != '1') {
              digitalWrite(RED_PIN, HIGH);
              delay(500);
              digitalWrite(RED_PIN, LOW);
            } else {
              digitalWrite(GREEN_PIN, HIGH);
              delay(500);
              digitalWrite(GREEN_PIN, LOW);
            }
        }
        Serial.print("]\r\n");
    }
    
    if (wifi.releaseTCP()) {
        Serial.print("release tcp ok\r\n");
    } else {
        Serial.print("release tcp err\r\n");
    }
    delay(5000);
}
     
