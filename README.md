# clipcomp

It's me, I'm the dummy.

I wrote this program because I like to follow tutorials without copying and pasting. I think typing everything helps me remember what I learn. However, I frequently make transcription errors, realize that copying the tutorial code fixes my problem, and then stare at my code for a really long time trying to figure out where I mistyped.

I thought I'd write a program to make it explicitly clear to myself exactly where my file deviates from the contents of my clipboard because it might save me some time. Then I thought I'd write it in Java because I hate myself.

## Requirements
Something to execute a Java archive with. I suggest [OpenJDK](https://openjdk.java.net/).

## Instructions

The Windows release contains a .jar file and a batch script you can call to execute it. To be able to call the batch script from anywhere, you need to add the path to the clipcomp_win directory (wherever you put it) to your PATH environment variable. Search the Start menu for "edit system environment variables" and hit enter. A window will pop up. Hit the button on the bottom that says "Environment Variables" and double click your PATH variable under System variables. Add a new one and paste the path to clipcomp_win into the text field that appears. Now you should be able to call it from the command line.

## Use
Run it with the file you want to test as the first argument, making sure you have the text to test the file against in your clipboard. Say you want to compare your clipboard contents to a file called test.txt in your Documents folder:
```
$ clipcomp.cmd C:\User\username\Documents\test.txt
```
Optionally, you can specify start and end line numbers (inclusive) to only compare a section of your file. So, if you want to check lines 2-14:
```
$ clipcomp.cmd C:\User\username\Documents\test.txt 2 14
```
It will print out a line from your file, a corresponding line from your clipboard, and a line below both with ```^``` symbols to indicate which characters don't match.

## Known issue I don't feel like fixing:

This program separates input by newlines. If you input a line that is too long, it will appear to not be reading your file/clipboard line by line, but it should still give you some idea of where you messed up. If you're using this to compare code, chances are you are not going to be having this problem, since you shouldn't be writing super long lines anyways.
