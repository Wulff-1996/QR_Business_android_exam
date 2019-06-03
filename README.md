# QR-Business ©
This repository contains the source code for our exam project at KEA Computer Science, 
and is owned alone by the contributors of the project.

## App description
This program is made for generating and storing QR codes, using firebase as an authentication and no-SQL database.
The QR codes are generated java site on the device running the app.
The program currently has three available QR code types, WiFi, Website and Vcard, 
each using a different template but using the same generic methods for generating the images.
QR Business uses multiple activities, but tries to keep it to a minimum by using fragments whenever  makes sense.
It also uses sliding functionalities, navigation drawer for increased UX. 
Our app tries to keep the design simple, using only a couple of colours, however there is a lot of functionality,
if you choose to use the options in drawers and sliders.

## Using the app
To use the app you will need a user, if you do not already have a user, 
input your desired email and password, and press the Sign-Up button.
Use your created user to log in, from here you will be able to create new QR codes, 
by using the "New" button in the navigation drawer to your left.
When creating a new QR fill out the desired fields and create it. 
You will then be redirected to the details page, where you can see information regarding your QR code.
Press back to go to the library again, and you will see it has been added to the gridview, 
if you choose to press the image in the library, you will go to the details page once again.

## Installation
All dependencies are declared in the build.gradle files, and simply requires you to sync the file.
After this, the app should be runable.

Enjoy using our App,
Jakob Wulff & Mikkel S. Pieler
