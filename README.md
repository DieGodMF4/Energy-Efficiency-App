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
The code is divided into different modules:
- **weather-provider**: Extracts the Weather Forecasts of a zone of each of the [Canary Islands](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwjtn_qEvoODAxX60QIHHUJDAEMQFnoECGkQAQ&url=https%3A%2F%2Fen.wikipedia.org%2Fwiki%2FCanary_Islands&usg=AOvVaw3MsX_vfdWSCBDwprlEn4Tm&opi=89978449) and sends it to a broker.
- **energy-provider**: Extracts the prices out to the market of Spain from an API of the official **"Red Eléctrica Española"** organization.
- **datalake-builder**: Whose labour is to subscribe to the broker and to store the data into files depending on its date.
- **business-showcase**: The most important. This module gets the information, via _datalake_ or via subscription and transforms it into usable information.
Here, you can see the prices of the market energy sent via Grid organized by hour. Also, its main purpose is to inform the user about their solar and/or wind renewable production (based on the weather forecast) and optionally his battery increases, so you are informed to know when you should or should not use the energy via any source. 


This code, specifically, makes an API call every 6 hours (for the Weather provider part) and each day (for the Energy provider module) and fetches the already registered location. **This location should be from the customer's home.**

## Execution
### For avoiding bugs or possible mal-functioning, execute the modules in this order


### 1. Module `weather-provider`

First of all, place in the **Main.java** class of the respective module: (```weather-provider/src/main/java/marrero_ferrera_gcid_ulpgc/test/control/Main.java```). \
Before running the code, if you are using IntelliJ IDEA, you can select **Modify Run Configuration...** and introduce the parameters needed beforehand.
When running the code into the CLI (console), it will ask for an **API Key**, the **AvticeMQ broker url**, the **topic name** and your **latitude** and **longitude**  as arguments (`args`): `<API Key> <broker url> <topic Name> <latitude> <longitude>`

In the following line, you will have an example and a suitable topic name: \
\
Example of the topic name: ```prediction.Weather```
For the **API Key**, you will have to log into [*OpenWeatherMap*](https://openweathermap.org) and create your own API key.
After that, you can freely start debugging and run the code.


**Disclaimers:**
- When introducing the **API Key** please stay tuned of the [terms of the free plan](https://home.openweathermap.org/subscriptions); by now, they admit up to 1,000 calls per day to the same API (which is difficult to exceed).
- The _latitude_ and _longitude_ arguments **HAVE TO BE USED USING A POINT: "." AS DECIMAL**
- **Important!!**: Before running the code, please visit the [ActiveMQ Installation and Start](https://activemq.apache.org/getting-started) to initialize the broker in your device, and [download it](https://activemq.apache.org/components/classic/download/) if you have not installed it yet.

### 2. Module `energy-provider`
As in the other module, you will first need to be in the **Main.java** class: (`src/main/java/marrero_ferrera_gcid_ulpgc/control/Main.java`).
This module has just **TWO** optional parameters, which are `topicName` and the **AvticeMQ broker url** as `args`. 

After starting the execution, this module will **automatically get** the energy API prices and execute again at 20:30 h at the "Europe/Madrid" timezone; hour when the market closes and setter the prices for the next day.  

### 3. Module `datalake-builder`

In this module, you will first need to be in the **Main.java** class: (```datalake-builder/src/main/java/marrero_ferrera_gcid_ulpgc/test/control/Main.java```). \
Before running the code, if you are using IntelliJ IDEA, you can select **Modify Run Configuration...** and introduce up to 5 parameters, which are: `topicNameWeather`, `topicNameEnergy`, `brokerUrl` (ActiveMQ), `clientID1` (for Weather), `clientID2` (for Energy) and the `basepath` where the files will be stored.
However, these parameters or arguments are **optional**, as a default value will be auto-assigned.


### 4. Module `business-showcase`

This last one is the result one and the final purpose of the app.


# Resources
1. This project was fully made on **[IntelliJ IDEA](https://www.jetbrains.com/es-es/idea/)**, by **JetBrains**. Besides, the project structure is based on the same software's default
   settings, with a **Maven** file for the needed dependencies. **[More information below](#design-)**.


2. The version control tool used is **Git**, and published on a **GitHub Repository**. As you may see, there are just two existing branches in the building process of the code, due to its relatively low time of development and its linearity. This allows us to follow an easy and precise path on the _"making of"_ of each module.


3. Finally, this document has been made as a **Markdown** file, provided by the base **IntelliJ** software.   
   The information needed for the development of the project has been taken from many sources. The main ones are the resource ones like [SQLite](https://www.sqlite.org/index.html) and [Maven Repository](https://mvnrepository.com) for the database and the dependencies respectively. Additionally, subject uploaded support files, [Stack Overflow](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwia3r6cxsiCAxW5V6QEHd52AF0QFnoECA8QAQ&url=https%3A%2F%2Fstackoverflow.com%2F&usg=AOvVaw0C-i47dSU_h02E_IQoAztO&opi=89978449), [YouTube](https://www.youtube.com/) and [ChatGPT](https://chat.openai.com) have been of extreme help for syntax problems and questions.

# Design

## Datalake-builder
This module just consists on just a `control` package, as each class makes an interaction with the user and this module is a "simple" conversion-maker for functionality. The mentioned package contains the classes: `Main`, `MyReceiverException` (as the own package exception class), `TopicSubscriber` and `FileEventStoreBuilder` with their respectives **interfaces**: `Subscriber` and `EventStoreBuilder`.

![event-store-builder](https://github.com/DieGodMF4/ActiveMQ-WeatherSender-Subscriber/assets/145327666/89aa2769-e464-4b00-a3cf-ce90312e10a3)


## Prediction-provider
This project consists on the default settings of **IntelliJ IDEA**, with a **Maven** file for dependencies. The current root structure of the **source code** is the following: `src/main/java/marrero_ferrera_gcid_ulpgc/test`.  
After that, the program is divided into two modules:
1. `model`: with the base business and main ideas. Contains the **classes**: `Weather` and `Location`.
2. `control`: These classes are closer to the user view and interaction, and are not directly protected as the model ones. First, with the `Main` class and, additionally, the `Task` class which is just an extension of the first one used to automatize the procedural and scheduled call to the API. Besides, we have the other classes: `WeatherController` as a controller between classes, `MySenderException` (as the own package exception class) and **interfaces**: `WeatherStore` and `WeatherSupplier` with their own generalizations of classes: `SQLiteWeatherStore` and `OpenWeatherMapSupplier`.

\
Here we have a graphical explanation to define the relationship between classes and interfaces, made with the [StarUML software](https://staruml.io/download/):

![weather-provider](https://github.com/DieGodMF4/ActiveMQ-WeatherSender-Subscriber/assets/145327666/70711a65-ea15-46e7-a644-65adae40bd63)

---

### License and state of the project
_This code is **FREE TO USE** and has no protection of License._   
This code is finished, but it can be freely modified and copied for particular usage; just follow [OpenWeatherMap](#main-purpose--functionality) policies.
