// Define the pins we're going to call pinMode on
int pwout = D1;  // You'll need to wire an LED to this one to see it blink.
int water = D2; // This one is the built-in tiny one to the right of the USB jack

int buzzer = D0;
int state = -1;

int led = D3;
int led2 = D4;
int led3 = D5;
int led4 = D6;
int led5 = D7;


//variables
volatile int isFull = 0;
volatile int wasEmpty = 0;

//send you time
unsigned long lastTime = 0UL;
unsigned long lastCheck = 0UL;
char publishString[64];
// This routine runs only once upon reset
int flag2 = 0;
// isFull == 0 means empty, isFull == 1 means full
int checkfull(String args)
{
  int retVal = -1;

  // If Empty and wasn't empty before
  if (!isFull && !wasEmpty)
  {
    retVal = 0;
    wasEmpty = 1;
  }  // If Empty and was empty before
  else if (!isFull && wasEmpty)
  {
    retVal = 2;
    wasEmpty = 1;
  }  // Else it must be full
  else
  {
    retVal = 1;
    wasEmpty = 0;
    flag2 = 0;
  }

  return retVal;
}

//asks the user to take a drink, returns -1 if the cup is empty, returns 1 if success
int doTakeDrink = 0;
int takedrink(String args)
{
   flag2 = 0;
  doTakeDrink = 1;
  return 1;

}

void outputleds() {
digitalWrite(led, HIGH);
digitalWrite(led2, HIGH);
digitalWrite(led3, HIGH);
digitalWrite(led4, HIGH);
digitalWrite(led5, HIGH);
}

void stopleds() {
digitalWrite(led, LOW);
digitalWrite(led2, LOW);
digitalWrite(led3, LOW);
digitalWrite(led4, LOW);
digitalWrite(led5, LOW);
}

void setup() {
  // Initialize D0 + D7 pin as output
  // It's important you do this here, inside the setup() function rather than outside it or in the loop function.
  pinMode(pwout, OUTPUT);
  pinMode(led, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);
  pinMode(led4, OUTPUT);
  pinMode(led5, OUTPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(water, INPUT_PULLDOWN );
  Spark.function("takedrink",takedrink);
  Spark.function("checkfull",checkfull);
  isFull = 0;
  wasEmpty = 0;
}


/*void sendEvent()
{
    unsigned long now = millis();
    now = lastTime - now; //how long it took for the user to take the drink
    unsigned nowSec = now/1000UL;
    unsigned sec = nowSec%60;
    unsigned min = (nowSec%3600)/60;
    sprintf(publishString,"{\"Minutes\": %u, \"Seconds\": %u}",min,sec);
    Spark.publish("Empty",publishString);
}*/


// This routine gets called repeatedly, like once every 5-15 milliseconds.
// Spark firmware interleaves background CPU activity associated with WiFi + Cloud activity with your code.
// Make sure none of your code delays or blocks for too long (like more than 5 seconds), or weird things can happen.
void loop() {
  digitalWrite(pwout, HIGH);   //turn on power in D0

  isFull = digitalRead(water); //read from D1

  if(isFull && doTakeDrink) //if the user has to take a drink and the cup is full, turn buzzer and led on
  {
      lastTime = 0;
      outputleds(); //led is on.
      digitalWrite(buzzer, HIGH); //buzzer is on.
  }
  else if (!isFull && doTakeDrink) //if user has to take a drink but the cup is empty, output is off.
  {
      unsigned long now = millis();
      if(lastTime > 0) //not the first time we reach this state
      {
          if((now - lastTime) > 2000UL) //cup has been empty for 2 seconds
          {
            doTakeDrink = 0; //reset flag, tell phone that drink is finished.
            lastTime = 0;
            flag2 = 0;
            stopleds();
            digitalWrite(buzzer, LOW);
          }
          else //cup has not been empty for at least 2 seconds
          {
            outputleds(); //led is on.
            digitalWrite(buzzer, HIGH); //buzzer is on.
          }
      }
      else //first time we reach this state
      {
        lastTime = millis();
        outputleds(); //led is on.
        digitalWrite(buzzer, HIGH); //buzzer is on.
      }


  }
  else //if user does not have to take a drink and the cup is empty, output is off
  {
    lastTime = 0;
    stopleds();
    digitalWrite(buzzer, LOW);
  }


}