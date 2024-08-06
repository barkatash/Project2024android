# project2024
Ishay, Sagi, and Bar project
An Android app for our youtube-like application.
Developed as part of the Advanced Programming course, Bar-Ilan Universtiy, 2024.

part1 will be on the main branch.
part3 with server will be on the main-server branch.
project status part1: finish.
project status part3: finish.

link to server github: https://github.com/barkatash/project2024-server

part1 - client - main branch
To run the app via Android Studio, open the project in Android Studio and click on the Run button.

For your usage signed user: 
username: bar 
password: 12345678

for part3:
part3 - with server - main-server branch
First you need to run the mongoDB and the server here: https://github.com/barkatash/project2024-server.
To run the app via Android Studio, open the project in Android Studio and click on the Run button.
*to access to user video page and see some user videos u need to go to watch video page and click on the uploader name.
*to access to edit and delete your user u need to be logged in and click on the user image in the bottom "You" right of the main activity. 

Dependencies:
All dependencies are included in the gradle build file:
       // Room components
    implementation("androidx.room:room-runtime:2.4.2")
    annotationProcessor("androidx.room:room-compiler:2.4.2")

    // SwipeRefreshLayout dependency
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")


ishay in miluam:
![image](https://github.com/ishay970/project2024/assets/115925876/f0db9ae5-5a44-4501-afa5-4b9656b726bc)

