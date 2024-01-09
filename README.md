# Energy Efficiency App
## _The solution you needed for **decreasing** your electric bill as well as your contamination._
**MOVE TO [develop-release-1.0.0](https://github.com/DieGodMF4/ActiveMQ-WeatherSender-Subscriber/tree/event-store) branch to see the source code!!!**

**Repository including java code for:**
- Two API requests to the **_OpenWeatherMap page_** and to **_Red Eléctrica Española_**. It also implements an automatization by sending its data to an **ActiveMQ Broker**;
- Another module for subscribing to the mentioned Broker and storing the json strings into directories. 
- 
- Made by Diego Marrero Ferrera.

This code was originally made for the subject of DACD (*Applications Development for Data Science*); taught in the Degree
of Data Science and Engineering, at University of Las Palmas de Gran Canaria (ULPGC), Spain.

## Main purpose & functionality
Java program made for **extracting** weather information from a REST Api through the pages [**OpenWeatherMap**](https://openweathermap.org) and [**_Red Eléctrica Española_**](https://www.ree.es/es/apidatos); after that, the data is sent to an [Apache ActiveMQ broker](https://activemq.apache.org/). \
Code implemented for **subscribing** to the meant broker and **storing** the `json` texts into directories. 

<a name="openweather"></a>
The code is divided into two different modules:
- **prediction-provider**: Extracts the Weather Forecasts of a zone of each of the [Canary Islands](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjtn_qEvoODAxX60QIHHUJDAEMQFnoECGkQAQ&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FCanary_Islands&usg=AOvVaw3MsX_vfdWSCBDwprlEn4Tm&opi=89978449) and sends it to a broker.
- **event-store-builder**: Whose labour is to subscribe to the broker and to store the data into files depending on its date.


This code, specifically, makes an API call every 6 hours and fetches the already registered locations of the **weather forecast** for the next 5 days at 12:00 local time. These locations are punctual chosen ones for each of the 8 islands that constitue the Chinijo Archipielago (Canary Islands).


## Execution

### Module `event-store-builder`

In this module, you will first need to be in the **Main.java** class: (```event-store-builder/src/main/java/marrero_ferrera_gcid_ulpgc/test/control/Main.java```). \
Before running the code, if you are using IntelliJ IDEA, you can select **Modify Run Configuration...** and introduce up to two parameters, which are: `topicName` and the `basepath` where the files will be stored.
However, these parameters or arguments are **optional**, as a default value will be auto-assigned.

### Module `prediction-provider`

As in the other module, you will first need to be in the **Main.java** class of the respective module: (```prediction-provider/src/main/java/marrero_ferrera_gcid_ulpgc/test/control/Main.java```). \
Before running the code, if you are using IntelliJ IDEA, you can select **Modify Run Configuration...** and introduce both parameters needed beforehand.
When running the code into the CLI (console), it will ask for an **API Key** and the **topic name** as arguments (`args`).

In the following line, you will have an example and a suitable topic name: \
\
Example of the topic name: ```prediction.Weather```
For the **API Key**, you will have to log into [*OpenWeatherMap*](https://openweathermap.org) and create your own API key.
After that, you can freely start debugging and run the code.


**Disclaimers:**
- When introducing the **API Key** please stay tuned of the [terms of the free plan](https://home.openweathermap.org/subscriptions); by now, they admit up to 1,000 calls per day to the same API (which is difficult to exceed).
- **Important!!**: Before running the code, please visit the [ActiveMQ Installation and Start](https://activemq.apache.org/getting-started) to initialize the broker in your device, and [download it](https://activemq.apache.org/components/classic/download/) if you have not installed it yet.

# Resources
1. This project was fully made on **[IntelliJ IDEA](https://www.jetbrains.com/es-es/idea/)**, by **JetBrains**. Besides, the project structure is based on the same software's default
   settings, with a **Maven** file for the needed dependencies. **[More information below](#design-)**.


2. The version control tool used is **Git**, and published on a **GitHub Repository**. As you may see, there are just two existing branches in the building process of the code, due to its relatively low time of development and its linearity. This allows us to follow an easy and precise path on the _"making of"_ of each module.


3. Finally, this document has been made as a **Markdown** file, provided by the base **IntelliJ** software.   
   The information needed for the development of the project has been taken from many sources. The main ones are the resource ones like [SQLite](https://www.sqlite.org/index.html) and [Maven Repository](https://mvnrepository.com) for the database and the dependencies respectively. Additionally, subject uploaded support files, [Stack Overflow](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwia3r6cxsiCAxW5V6QEHd52AF0QFnoECA8QAQ&url=https%3A%2F%2Fstackoverflow.com%2F&usg=AOvVaw0C-i47dSU_h02E_IQoAztO&opi=89978449), [YouTube](https://www.youtube.com/) and [ChatGPT](https://chat.openai.com) have been of extreme help for syntax problems and questions.

# Design

## Event-store-builder
This module just consists on just a `control` package, as each class makes an interaction with the user and this module is a "simple" conversion-maker for functionality. The mentioned package contains the classes: `Main`, `MyReceiverException` (as the own package exception class), `TopicSubscriber` and `FileEventStoreBuilder` with their respectives **interfaces**: `Subscriber` and `EventStoreBuilder`.

![event-store-builder](https://github.com/DieGodMF4/ActiveMQ-WeatherSender-Subscriber/assets/145327666/89aa2769-e464-4b00-a3cf-ce90312e10a3)


## Prediction-provider
This project consists on the default settings of **IntelliJ IDEA**, with a **Maven** file for dependencies. The current root structure of the **source code** is the following: `src/main/java/marrero_ferrera_gcid_ulpgc/test`.  
After that, the program is divided into two modules:
1. `model`: with the base business and main ideas. Contains the **classes**: `Weather` and `Location`.
2. `control`: These classes are closer to the user view and interaction, and are not directly protected as the model ones. First, with the `Main` class and, additionally, the `Task` class which is just an extension of the first one used to automatize the procedural and scheduled call to the API. Besides, we have the other classes: `WeatherController` as a controller between classes, `MySenderException` (as the own package exception class) and **interfaces**: `WeatherStore` and `WeatherSupplier` with their own generalizations of classes: `SQLiteWeatherStore` and `OpenWeatherMapSupplier`.

\
Here we have a graphical explanation to define the relationship between classes and interfaces, made with the [StarUML software](https://staruml.io/download/):

![prediction-provider](https://github.com/DieGodMF4/ActiveMQ-WeatherSender-Subscriber/assets/145327666/70711a65-ea15-46e7-a644-65adae40bd63)

---

### License and state of the project
_This code is **FREE TO USE** and has no protection of License._   
This code is finished, but it can be freely modified and copied for particular usage; just follow [OpenWeatherMap](#main-purpose--functionality) policies.
