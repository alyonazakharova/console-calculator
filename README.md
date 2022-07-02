# test-task
## HOW-TO BUILD & RUN

To build and run this project you need to have maven and java installed. 

First of all, you need to clone this repository. Then open the project folder from the command line and run the following command:
```
mvn clean install
```
Executable jar file with be created in *target* folder. To run the jar file, all you have to do is execute:
```
java -jar target/test-task-1.0-SNAPSHOT.jar
```


## HOW-TO USE
After you run the program, first, you will be asked to enter an arithmetic expression. Enter an expression and press Enter button.
If your expression is valid, you will get "OK" message.
Then the program will be waiting for you to enter values for the variables, if needed, and enter *print* or *calculate* command.

*Print* command prints abstract syntax tree for your expression.

*Calculate* command returns the result of your expression. All the variables need to be set before calling *calculate*.

If something is wrong with your input data, you'll get an error message.

Below is the output of the program:

<img width="639" alt="Снимок экрана 2022-07-02 в 15 43 41" src="https://user-images.githubusercontent.com/25694552/177001249-c4736467-fd92-4127-ba43-65d9e309a7e0.png">
