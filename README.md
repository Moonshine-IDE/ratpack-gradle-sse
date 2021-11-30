# ratpack-gradle-sse basic demo app
Basic HTTP reactive application that uses Ratpack and Server-sent events to send notifications to the front-end when a local CSV file is updated.

This application checks each minute if a CSV file named 'people.csv' (located at `ratpackpush_csv_path`) was modified, if that's the case a server-sent event is triggered and a notification is sent to the client browser. The application stops if the file is not found.

There is a JVM argument `ratpackpush_csv_path` in the `building.gradle` file that can be modified with the proper path to the CSV file in your file system. You can use the default CSV file included in the project files, in my case (Windows) I have to set that argument like this:

`'-Dratpackpush_csv_path=C:\\projects\\ratpack-gradle-sse\\src\\main\\resources'`

Once the application is running, you can open localhost:5050/ and you should see a page listing all the records from the CSV file.

You can then edit the CSV file records (the first line contains the headers and should not be changed) and the HTML table will be updated accordingly.

## Requirements
#### Required Dependencies:
- Java 8+  

#### Optional: 
- Moonshine-IDE

## Run the project
Once the mongodb server is running you can continue and run this project
### Using the Gradle wrapper
#### Ubuntu  
`./gradlew run`

#### Windows  
`gradlew.bat run`

### From Moonshine IDE

Open the project in Moonshine with File > Open/Import Project or by double-clicking on ratpack-push.javaproj.
Project > Run Gradle Command. This will run the default command gradle clean runApp.
