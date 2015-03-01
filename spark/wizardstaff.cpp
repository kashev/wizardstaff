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
int val = 0;
int flag = 0;
int flag2 = 0;

//send you time
unsigned long lastTime = 0UL;
unsigned long lastCheck = 0UL;
char publishString[64];
// This routine runs only once upon reset

//polls the status of the cup, return 1 if full, 0 if empty
int checkfull(String args)
{
  if(!val && flag)//if cup is empty, reset flag
  {
/*      unsigned long now = millis();
      if(lastCheck > 0) //not the first time we reach this state
      {
          if((now - lastCheck) > 2000UL) //cup has been empty for 2 seconds 
          {
            flag = 0; //reset flag, tell phone that drink is finished.
            lastCheck = 0;
            return 0;
          }
          else //cup has not been empty for at least 2 seconds
          {
            return 1;
          }
      }
      else //first time we reach this state
      {
          lastCheck = millis();
          return 1;
      }*/
      flag2 = 0;
      return state;
  }
  else if (!val && !flag)//if cup is empty and flag has already been reset
  {
      if (flag2 == 0)
     {   
         flag2 = 1;
         return 0;
     }
     else
     {
         
          return 2;
     }
       
  }
  else // cup is full and user has been asked to take a drink
  {
     flag2 = 0;
    return 1;
  }
    
}

//asks the user to take a drink, returns -1 if the cup is empty, returns 1 if success
int takedrink(String args)
{

  if(!val)
  {
      return -1;
  }   
  flag2 = 0;
  flag = 1;
  state = 1;
  
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
  
  val = digitalRead(water); //read from D1
  
  if(val && flag) //if the user has to take a drink and the cup is full, turn buzzer and led on
  {
      lastTime = 0;
      outputleds(); //led is on.
      digitalWrite(buzzer, HIGH); //buzzer is on.
  }
  else if (!val && flag) //if user has to take a drink but the cup is empty, output is off.
  {
      unsigned long now = millis();
      if(lastTime > 0) //not the first time we reach this state
      {
          if((now - lastTime) > 2000UL) //cup has been empty for 2 seconds 
          {
            flag = 0; //reset flag, tell phone that drink is finished.
            lastTime = 0;
            state = 0;
            stopleds();
            digitalWrite(buzzer, LOW);
          }
          else //cup has not been empty for at least 2 seconds
          {
            state = 1;
            outputleds(); //led is on.
            digitalWrite(buzzer, HIGH); //buzzer is on.
          }
      }
      else //first time we reach this state
      {
        lastTime = millis();
        state = 1;
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
