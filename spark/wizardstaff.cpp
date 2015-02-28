/****** MUSIC STUFF ************/
#define NOTE_BF4	1073
#define NOTE_AF4	1205
#define NOTE_F4		1433
#define NOTE_EF5	804
#define NOTE_D5		852
#define PAUSE		50000
#define MAX_NOTES		11

int funkyTown[12] = {NOTE_BF4, NOTE_BF4, NOTE_AF4, NOTE_BF4, 
					PAUSE, NOTE_F4, PAUSE, NOTE_F4,
					NOTE_BF4, NOTE_EF5, NOTE_D5, NOTE_BF4};

int16_t audioPin = D6;
int16_t x,y = 0;
/****** END MUSIC STUFF ************/


// Define the pins we're going to call pinMode on
int pwout = D0;  // You'll need to wire an LED to this one to see it blink.
int led2 = D1; // This one is the built-in tiny one to the right of the USB jack
int led = D3;
int buzzer = D4;

//variables
int val = 0;
int flag = 0;

//send you time
unsigned long lastTime = 0UL;
char publishString[64];
// This routine runs only once upon reset
void setup() {
  // Initialize D0 + D7 pin as output
  // It's important you do this here, inside the setup() function rather than outside it or in the loop function.
  pinMode(pwout, OUTPUT);
  pinMode(led, OUTPUT);
  pinMode(buzzer, OUTPUT);
  pinMode(led2, INPUT_PULLDOWN );
}

//polls the status of the cup, return 1 if full, 0 if empty
int checkfull(String args)
{
  if(!val)//if cup is empty, reset flag
  {
      flag = 0;
  }
  return val;

}

//asks the user to take a drink, returns -1 if the cup is empty, returns 1 if success
int takedrink(String args)
{

  if(!val)
  {
      return -1;
  }
  flag = 1;
  lastTime = millis();
  return 1;

}

void sendEvent()
{
    unsigned long now = millis();
    now = lastTime - now; //how long it took for the user to take the drink
    unsigned nowSec = now/1000UL;
    unsigned sec = nowSec%60;
    unsigned min = (nowSec%3600)/60;
    sprintf(publishString,"{\"Minutes\": %u, \"Seconds\": %u}",min,sec);
    Spark.publish("Empty",publishString);
}


void takeMeTo(int pin, int note){
	for(x = 0; x < (DURATION/(note*2)); x++) {
	    PIN_MAP[pin].gpio_peripheral->BSRR = PIN_MAP[pin].gpio_pin; // HIGH
	    delayMicroseconds(note);
	    PIN_MAP[pin].gpio_peripheral->BRR = PIN_MAP[pin].gpio_pin;  // LOW
	    delayMicroseconds(note);
	  }
	  y++;
	  if(y >= MAX_NOTES) y = 0;
	  delay(250);
}

void outputmusic()
{
    int i = 0;
    for (i = 0; i < 12; i++) {
    takeMeTo(audioPin, funkyTown[i]);
    }
}


// This routine gets called repeatedly, like once every 5-15 milliseconds.
// Spark firmware interleaves background CPU activity associated with WiFi + Cloud activity with your code. 
// Make sure none of your code delays or blocks for too long (like more than 5 seconds), or weird things can happen.
void loop() {
  digitalWrite(pwout, HIGH);   //turn on power in D0
  
  val = digitalRead(led2); //read from D1
  
  if(val && flag) //if the user has to take a drink and the cup is full, turn buzzer and led on
  {
      digitalWrite(led, HIGH); //led is on.
      digitalWrite(buzzer, HIGH); //led is on.
  }
  else if (!val && flag) //if user has to take a drink but the cup is empty, output is off. //SEND event
  {
      digitalWrite(led, LOW);
      digitalWrite(buzzer, LOW);
      sendEvent(); //Send event to server, "Event Name is empty!"
      flag = 0;
      
  }
  else //if user does not have to take a drink and the cup is empty, output is off
  {
    digitalWrite(led, LOW);
    digitalWrite(buzzer, LOW);
  }
  
  
}
