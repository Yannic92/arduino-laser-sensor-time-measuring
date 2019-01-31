#define IRpin_PIN PIND
#define IRpin 2
#define threshold 1500
#define RESOLUTION 20
int count = 0;
int currentState = -1;
unsigned long time;

void setup() {
  Serial.begin(9600);
  waitForHandshake();
}

void waitForHandshake() {
  Serial.println("Waiting for Handshake");
  while(Serial.available() == 0){}
  String handshake = Serial.readString();
  if(handshake.equals("Arduino\n")) {
    Serial.println("Uno");
  } else {
    waitForHandshake();
  }
  while(Serial.available() == 0){}
  String goSignal = Serial.readString();
  if(!goSignal.equals("Go\n")) {
    waitForHandshake();
  }
}

void loop() {
  unsigned long loopTime = millis();
  if(!(IRpin_PIN & (1 << IRpin))) {
    if(currentState == 2) {
      currentState = 1;
      time = loopTime;
    }
    count = count + 1;
  } else {
    count = 0;
    currentState = 2;
  }
  if( count >= threshold && currentState != 0 ) {
    Serial.println(time);
    currentState = 0;
    delay(5000);
  }
}
