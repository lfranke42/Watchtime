# Watchtime
An Android App for tracking and comparing the time you've spent watching TV Shows.

# Project Description
This project is a simple Android App that allows you to track the time you've spent watching TV Shows. 
It uses the TVDB API to get information about TV Shows and Episodes. 
The App is written in Kotlin and uses the MVVM architecture pattern. 
It also uses the Room library to store user data locally.

The home screen provides both an overview of some series to watch and a list of series that the user has started, but not yet finished.

Search functionality is provided on the search screen.
It queries the TVDB API for TV Shows and displays the results in a list.

A simple leaderboard allows users to compare their total watchtime to others.
This is implemented using Azure Functions which provide endpoints for updating and querying the leaderboard.

# Installing and running the project
I'm planning on changing a few things and releasing the app on the play store, so you don't have to go through the hassle of building it yourself.
For now, you have to follow these steps to build and run the app:
 - Make sure you have the latest version of Android Studio installed
 - Clone the project via Android Studio or the Git CLI and open it
 - Now you have to provide the required API key:
   - Create a file called `secrets.properties` in the root directory of the project (alternatively you can just change the values inside the `secrets.defaults.properties`)
   - Go to the [TVDB Website](https://thetvdb.com/api-information) and request an API key if you haven't already got one
   - in the secrets.properties file enter your key like this: `API_KEY=your_key_here`
   - to get leaderboard functionality you would need to provide your own Azure Function key, but this isn't possible as they are not public (probably subject to change, for now leaderboards won't work)
 - (optional but recommended) In the build options, change the build variant from `debug` to `release`
 - connect your physical device or start and emulator and hit the play button on the top right of Android Studio 

# How to use the App
The app is pretty self-explanatory, but here are some tips:
 - The home screen shows you some series to watch and a list of series you've started but not yet finished
 - The search screen allows you to search for TV Shows and add them to your list
 - When tapping on a series entry you can:
   - Select a season by tapping on the season chip
   - Mark an episode as watched by checking the checkbox to the right
   - Mark a whole season as watched by tapping the "season watched" checkbox
   - Mark the whole series as watched by tapping on the three dotted "more options" button on the top right and checking the "series completed" checkbox
 - The stats screen shows you how much time you've spent watching TV Shows compared to others and a rundown of your total watch time

# Screenshots
<img src="https://github.com/lfranke42/Watchtime/assets/84925848/c2cddd06-a0d1-478a-9cc3-77959f606267" width="150"> 
<img src="https://github.com/lfranke42/Watchtime/assets/84925848/5e070f95-5a79-471b-9615-f5cb55058c7e" width="150"> 
<img src="https://github.com/lfranke42/Watchtime/assets/84925848/13235183-ceed-4189-b34b-d45277595e48" width="150"> 
<img src="https://github.com/lfranke42/Watchtime/assets/84925848/283b17e6-a6d1-4f1b-8295-c4ece489a292" width="150"> 
<img src="https://github.com/lfranke42/Watchtime/assets/84925848/90cf5cdb-0bc7-4ad4-b4a5-07875f365b46" width="150"> 

# Credits
Metadata is provided by TheTVDB. 
Please consider adding missing information or subscribing.

<picture>
  <source media="(prefers-color-scheme: light)" srcset="https://github.com/lfranke42/Watchtime/assets/84925848/a8e9d673-bec7-4d35-b650-096aa02a2907">
  <source media="(prefers-color-scheme: dark)" srcset="https://github.com/lfranke42/Watchtime/assets/84925848/21c71ec5-19e2-4a83-80f1-c68287902df1">
  <img alt="TVDB LOGO" src="https://github.com/lfranke42/Watchtime/assets/84925848/21c71ec5-19e2-4a83-80f1-c68287902df1" width="150px">
</picture>
