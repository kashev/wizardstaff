# wizardstaff

    wizardstaff
    A project for HackIllinois 2015

    Dario Aranguiz :: aranguizdario@gmail.com
    Kashev Dalmia  :: kashev.dalmia@gmail.com
    Brady Salz     :: brady.salz@gmail.com
    Ahmed Suhyl    :: sulaimn2@illinois.edu

## About
In our submission for HackIllinois Hardware 2015, we decided to modernize a classic drinking game "Wisest Wizard" (also known as "Wizard Staffs"). For the unfamiliar, the game involves drinking from cans of beer, and upon finishing a can, opening a new one and stacking it atop the previous one, securing it down with duct tape. The fallback to this is that the game is only playable with cheaper beer, so beer that comes in a glass doesn't quite work with this set up. 

Our hack consists of adding DIY moisture sensors with Spark microcontrollers to pint glasses. When the glass is detected as empty, it pings the Android app and informs it that a drink has been completed. The app contains of chart printout of all contestants and their current "wizard level", which is stored in a database. This will also be duplicated to both Android Wear and Pebble products, thanks to Strap. 

## House Rules
- A wizard's level corresponds to their completed beer count (all players start at Level 0, reach Level 1 upon finishing first beer)
- Every 5th level is a boss level, and one must take a shot to proceed (Boss Jim, Boss Jose, Boss Burnette)
- If one wizard is two or more levels above another, they may command the other wizard to chug their beer (takes 30minutes to charge)
- After two wizards reach Level 5, they may duel each other. Both wizards upon a new can and race to finish it. The winner of the duel gets to claim two levels, and the loser gets nothing

## Thanks

Team Brady Rocks built WizardStaff in 36 hours.

- **Dario Aranguiz** - Android App, DB Interaction
- **Kashev Dalmia** - Pebble App, Website, Android Layout
- **Brady Salz** - Hardware, Hardware, Hardware, Embedded Software
- **Ahmed Suhyl** - Embedded Software, Test, Hardware


## TODO
- [ ] Microcontroller Code
- [ ] Android App
- [ ] Chromecast App
- [ ] Android Wear / Pebble / Strap App
- [ ] Enclosure 
- [x] Hardware
