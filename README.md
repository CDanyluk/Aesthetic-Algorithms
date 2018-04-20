# Aesthetic-Algorithms
Independent Research Project 2017 - 2018: The Aesthetics of Computational Art

This project contains a year long research project by Taylor Baer and Chantal Danyluk 
that explored the aesthetic and artistic potential of two discrete mathematic models: 
L-systems and cellular automata.

## Purpose

The application is primarily designed to let a user run a genetic algorithm to create 
and store images created from mathematical concepts. Images can be generated 
randomly with some control features, including the ability to export a certain number of
images, at certain iterations, and control some database features.
Underneath the surface, all generated images are given a score between 0.0 - 1.0 determined
by an aesthetic metric. 

## Get Started - Cellular Automata

Users may want to visit the following link to view how to use cellular automata: https://youtu.be/MFrWTETF-lw

## Get Started - L-Systems

Users may want to visit the following link to view how to use L-systems: https://youtu.be/5kE5qKw4RWw

## Cellular Automata

To create cellular automata, users can select the cellular automata radio
button, which changes the mode of the program. Then, users can "fetch" a new canvas, and 
"randomize the grid values" on the current grid. Alternatively, by clicking the canvas users
can place their own cells. By continually clicking "fetch" users can watch the cellular automata
grow. If users like the image, they can "export" the current image to a place on their computer.

## Genetic Cellular Automata

Users can automate this process of finding a likable image through a genetic algorithm. To start,
users should hit "delete all" under the cells database controls to clear the database of any 
pre-existing data. Select the cellular automata radio button, enter an amount of iterations, 
and an original export number greater than ten.
**Wait!**
When a new image appears on the screen, it is time to run the genetic algorithm. In the empty text
field labelled "Gen" enter the desired number of generations. Over ten generations and the program
will slow considerably.
**To find the images:**
In windows machines, the images will be stored in Windows(C:)\AutomataImages filepath, under a 
randomly generated file name.

## L-Systems 

To create L-Systems, users can select the L-system radio button, which changes the mode of the program. 
Then, users can hit "go" to generate a random L-system that will be saved somewhere in the computer. 
Alternatively, users can click "export" after creating this image to choose the file path the image
will be saved to.

## Genetic L-Systems

Users can automata the process of finding a likable image through mutation. To start, users should hit 
"delete all" under the L-systems database controls to clear the database of any pre-existing data.
Select the L-system radio button, enter the export number greater than ten, and press "go".
**Wait!**
When a new image appears on screen, it is time to run the mutation algorithm. To do so, simply press
"mutate" in the L-system database controls. It is important to wait for the program to stop running
before pressing mutate again. Each time mutate is pressed, a new generatin will be formed.
**To find the images:**
In the code the fielpath is hardcoded, users will have to individually go through and change this
to be custom to their needs.

## Authors

This program was developed by Taylor Baer and Chantal Danyluk, with Taylor Baer handling L-system
controls, and Chantal Danyluk developing cellular automata features. 
