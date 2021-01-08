# io-comparer-for-dummies

I wrote this program because I like to follow tutorials without copying and pasting. I think typing everything helps me remember what I learn. However, I frequently make transcription errors, realize that copying the tutorial code fixes my problem, and then stare at my code for a really long time trying to figure out where I mistyped. I thought I'd write a program to make it explicitly clear to me exactly where my file deviates from the contents of my clipboard because it might save me some time. Then I thought I'd write it in Java because I hate myself.

All you need to download is Comparer.java. From the directory it's located in, it like so:
```
$ javac Comparer.java
```
Then run it with the file you want to test as the argument, making sure you have the text to test the file against in your clipboard. Say you want to compare your clipboard contents to a file called test.txt in your Documents folder:
```
$ java Comparer ~/Documents/test.txt
```
Should also work on Windows (no promises):
```
$ java Comparer C:\Users\YourName\Documents\test.txt
```
