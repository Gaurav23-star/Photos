

https://user-images.githubusercontent.com/59610677/210891004-212b3a4a-70a2-4738-b609-94ab0d06e713.mov

A single-user photo application that allows storage and management of photos in one or more albums. All user interaction is implemented in Java FX, and all UIs--except for standard Java FX dialogs such as Alert and TextInputDialog--must be designed in FXML. Used multiple stages to show complex secondary windows, and switch multiple scenes within a stage.

Features:

Date of photo
Tags
Location of Photos - Stock photos and User photos
Login
Admin Subsystem
Create a new user
Delete an existing user
Non-admin User Subsystem
The user can then do the following:
Create albums
Delete albums
Rename albums
Open an album. Opening an album displays all photos, with their thumbnail images and captions, inside that album. Once an album is open the user can do the following:
Add a photo
Remove a photo
Caption/recaption a photo
Display a photo in a separate display area. The photo display should also show its caption, its date-time of capture (see Date of photo below), and all its tags (see Tags below).
Add a tag to a photo
Delete a tag from a photo
Copy a photo from one album to another (multiple albums may have copies of the same photo)

Move a photo from one album (source) to another (the photo will be removed from the source album)
Go through photos in an album in sequence forward or backward, one at a time, with user interaction (manual slideshow)
Search for photos (Photos that match the search criteria should be displayed in a similar way to how photos in an album are displayed). 
Search for photos by a date range.
Search for photos by tag type-value pairs. The following types of tag-based searches should be implemented:
A single tag-value pair, e.g person=john
Conjunctive combination of two tag-value pairs, e.g. person=john AND location=prague
Disjunctive combination of two tag-value pairs, e.g. person=john OR location=prague
For conjunctions and disjunctions, if a tag can have multiple values for a photo, it can appear on both arms of the conjunction/disjunction, e.g. person=andre OR person=maya, person=andre AND person=maya

functionality to create an album containing the search results.
Logout

The user (whether admin or non-admin) logs out at the end of the session. All updates made by the user are saved to disk.
After a user logs out, the application is still running, allowing another user to log in.
Quit Application
user to quit the application safely at any time, bypassing the logout step (e.g. by killing the main window). Safely means that all updates that were made in the application in the user's session are saved on disk.
Unlike logout, the application stops running. The next user that wants to use the application will need to restart it.


You need to think about what objects you want to have in your design, with what attributes and operations. It is important to plan this out and come up with a good object-oriented design that clearly separates roles and functions between objects, and can be cleanly extended to add more features for future versions of the application.

