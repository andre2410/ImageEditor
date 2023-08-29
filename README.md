# ImageEditor
Simple image editor GUI which was made from a university project.

## About
Simple image editor aka ANDIE is an image editor that every Computer Science graduate from Otago University would have created from their group projects.

Although its not unique or a defining project, it demonstrates that I can do OOP and fiddle with the pain of java GUI.

![Application UI](https://i.imgur.com/AZHMhdO.jpg)

For my (group's) implementation, the image editor has the following functions:<br>
- Image filters (Emboss, Sobel, Greyscale, Gaussian etc)
- Image Resize and Rotation
- Image crop
- Image vertical and horizontal flipping
- Image brightness and contrast
- Inverted colours for image 
- Image zoom

The application also has hotkeys and macros which can be saved and applied to a different image which saves time by instantly repeating the exact steps saved in the macro.

## Instructions
To run the image editor, download the files from the repository and by using your favourite IDE, run Andie.java in the src/cosc202/andie. A GUI window will appear similar to the UI image above and you can access the features of the image editor.

If you enjoy command lines and refuse to use something easier like a GUI, you can download the files from the repository and follow the commands below:<br>
- Follow this [link](https://code.visualstudio.com/)
- download it and stop using command lines to compile code, this is not C

### WHY DIDN'T YOU MAKE AN EXECUTABLE JAR?
Funny you asked, I did but the jar executable could not be launched as the java versions used to compile the .java files are different to the current java version (outdated). I am a windows user and by compiling through command lines (the hypocrisy!), it produced an compiling error as windows cannot recognise the arrow characters that was used for the FilterActions.java menu. Since the purpose of the project is to show I can do OOP and you can still run the GUI through an IDE. It is good enough.



