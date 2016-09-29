# Kobold Fight Club for Android
[Kobold Fight Club](http://kobold.club) is an Encounter builder and manager for the popular tabletop RPG, Dungeons & Dragons. It is primarily used by Dungeon Masters as a tool to facilitate smoother and more balanced combat.

With the permission of its developer, [Ian Toltz](https://github.com/Asmor), this project is the Android app for Kobold Fight Club. The app supports devices running Android 4.4+, and is optimized for phones of all shapes and sizes.

## Features
With the app, you can:
* Create encounters by adding monsters from a list.
* Filter through the list of monsters by name or CR.
* Create your party by adding your players.
* Run encounters.

The above features will continue to be refined as the app undergoes thorough user testing (mainly by myself when I use it during my games). More features will be considered.

## How to Work with the Source
Currently the source code is a mess and is probably only comprehensible by me. I will endeavor to make it more develop-friendly once the basic functionality of the app has been fully established. Until then, do bear with me as I continue pushing uncommented code.

Since this is an open source project, do feel free to submit a pull request and assist in reporting and fixing bugs that you might see along the way.

## To-Do
Here's a list of things that are currently in the works:
- [ ] Allow user to sort and filter the list of monsters based on various options as seen on Kobold.club
- [ ] Design and implement the Encounter manager
- [ ] Design and implement the Run encounter
  - [ ] "Dice-roller" to roll for initiative
  - [ ] Save encounter to database
  - [ ] HP counter (remove monster when HP <= 0)
- [ ] Allow user to build multiple encounters and multiple parties
- [ ] Possibly allow creation of custom monsters (Sub-section of monster list for homebrewed creatures?)
- [ ] Include images for monsters? (Low chance of viability)

## Acknowledgments
This app was created with the assistance of the following amazing libraries (in no particular order):
* [Realm](https://realm.io/)
* [Butter Knife](http://jakewharton.github.io/butterknife/)
* [Gson](https://github.com/google/gson)

## License
'''
(c) 2016 Omar Khalid

This is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this app. If not, see https://www.gnu.org/licenses/.
'''
