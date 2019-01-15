int lasersensorPin = 2;
int threshold = 5;
int count = 0;

void setup() {
  Serial.begin(9600);
  pinMode(lasersensorPin, INPUT);
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
  int sensorState = digitalRead(lasersensorPin);
  if(sensorState == 0) {
    count = count + 1;
  } else {
    count = 0;
  }
  if(count >= threshold) {
    Serial.println(0);
  }
  delay(1); // delay in between reads for stability
}
