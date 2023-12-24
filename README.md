# NutriPal Application


NutriPal is a health-focused app combining a step tracker, BMI and weight progress tools, and meal management features. It integrates Spoonacular API for recipe and nutritional information and is developed with Fast API, Firebase, and Java + XML. Aimed at promoting a healthier lifestyle, it plans to introduce AI for more personalized diet tracking.

<img src="https://github.com/Ramzi788/NutriPal_App/assets/99996354/40ca1d65-23d6-40a2-a4dc-ccf270a203f6" width="250" height="550">


# Requirements
- Android Studio
- Any Python IDE (PyCharm recommended)

# Instructions and Usage
Steps: 
- Download these following files for the FastAPI program:
  1. [main.py.zip](https://github.com/Ramzi788/NutriPal_App/files/13762965/main.py.zip)
  2. [nutripal-874d0-firebase-adminsdk-31tmi-e55646c9a8.json.zip](https://github.com/Ramzi788/NutriPal_App/files/13762969/nutripal-874d0-firebase-adminsdk-31tmi-e55646c9a8.json.zip)
- Create a new Python project and insert these files.
- To make this program activated, open the terminal.
- Run this command:  uvicorn main:app --reload --port 8000. Server should be up and running now.
- In android studio, create a new a project and clone this repository: https://github.com/Ramzi788/NutriPal_App.git
- Once loaded, you have to first change the localURL String variable in the strings.xml file, to connect to the fastAPI. If you are running on an emulator: http://10.0.2.2:8000. If running on real device: http://LOCAL_IP:8000

# Remarks
- Please note that this app is not 100% completed as there are some features that have been not completed as of yet. 
