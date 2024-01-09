# Energy Efficiency App
## _The solution you needed for **decreasing** your electric bill as well as your contamination._
**MOVE TO [re-adjustment branch](https://github.com/DieGodMF4/Energy-Efficiency-App/tree/re-adjustment) branch to see the source code!!!**

**SUITABLE FOR SOLAR PANELS AND WIND-POWERED GENERATOR PRODUCERS.**

**Repository including java code for:**
- Two API requests to the **_OpenWeatherMap page_** and to **_Red Eléctrica Española_**. It also implements an automatization by sending its data to an **ActiveMQ Broker**;
- Another module for subscribing to the mentioned Broker and storing the json strings into directories. 
- An app (represented via Swing) with the information you need to maximize your savings in electricity.
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
- **app-showcase**: The most important. This module gets the information, via _datalake_ or via subscription and transforms it into usable information.
Here, you can see the prices of the market energy sent via Grid organized by hour. Also, its main purpose is to inform the user about their solar and/or wind renewable production (based on the weather forecast) and optionally his battery increases, so you are informed to know when you should or should not use the energy via any source. 


This code, specifically, makes an API call every 6 hours (for the Weather provider part) and each day (for the Energy provider module) and fetches the already registered location. **This location should be from the customer's home.**

The app shows, at least, the available hours of the remaining day hour by hour. But:
**Disclaimer of execution**: The Weather API provides information only **every 3 hours**. Meanwhile, the Energy one does it every hour.
For this reason, **THE SOLAR AND WIND PRODUCTION, AS WELL AS THE BATTERY GAINED ARE JUST AN ESTIMATION**, by multiplying its value by 3 (deducing that the weather is the same as in the previous hours).

## Execution
##### **Disclaimer:** _For avoiding bugs or possible mal-functioning, execute the modules in this order_


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


### 4. Module `app-showcase`

This last one is the result one and the final purpose of the app.
As always, you will need to place in the same `Main` class, where you will have many arguments (8), but **they all are optional**.
1. **Renewable arguments:** In order, you will have: 
   - **Solar total power** (your maximum solar panels power production, if you have); represented in **Kw**.
   - **Wind total power** (your maximum wind-powered windmill production, if you have); represented in **Kw**.
   - **Maximum Battery Capacity** (your maximum battery storage, if you have); represented in **KwH**.
   - **Recommended Half Battery**, as a boolean, the gain of the Battery will be doubled, because it will mark that **you will not trespass below 50% of your battery capacity**, as it has been shown that this drastically reduces its useful life.
2. **ActiveMQ arguments**: Needed for receiving the data from the ActiveMQ broker.
   - **Topic name Weather**: the topic you will use for consuming `Weather` objects previously sent.
   - **Topic name Energy**: the topic you will use for consuming `EnergyPrice` objects previously sent.
   - **Broker url** : Already initialized as default `tcp://localhost:61616`, but you could maybe want to change it.
3. **Datalake argument**: For receiving the Objects via your Storage:
   - **Additional path**: A string where you can put the path to the Datalake where the `json` strings are stored. Initially put to look into directories at module-memory-level. 


# Resources
1. This project was fully made on **[IntelliJ IDEA](https://www.jetbrains.com/es-es/idea/)**, by **JetBrains**. Besides, the project structure is based on the same software's default
   settings, with a **Maven** file for the needed dependencies. **[More information below](#design-)**.


2. The version control tool used is **Git**, and published on a **GitHub Repository**. As you may see, there are just two existing branches in the building process of the code, due to its relatively low time of development and its linearity. This allows us to follow an easy and precise path on the _"making of"_ of each module.


3. Finally, this document has been made as a **Markdown** file, provided by the base **IntelliJ** software.   
   The information needed for the development of the project has been taken from many sources. The main ones are the resource ones like [SQLite](https://www.sqlite.org/index.html) and [Maven Repository](https://mvnrepository.com) for the database and the dependencies respectively. Additionally, subject uploaded support files, [Stack Overflow](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&cad=rja&uact=8&ved=2ahUKEwia3r6cxsiCAxW5V6QEHd52AF0QFnoECA8QAQ&url=https%3A%2F%2Fstackoverflow.com%2F&usg=AOvVaw0C-i47dSU_h02E_IQoAztO&opi=89978449), [YouTube](https://www.youtube.com/) and [ChatGPT](https://chat.openai.com) have been of extreme help for syntax problems and questions.

# Design


This app itself is made following the **_MVC_**(_Model-View-Controller_) software architecture, and the `app-showcase` module as a **Kappa System Architecture**.
This project consists on the default settings of **IntelliJ IDEA**, with a **Maven** file for dependencies in each module.
We have visual representations via **Star UML** software, as classes Diagrams.

## 1. Weather-provider
 The current root structure of the **source code** is the following: `src/main/java/marrero_ferrera_gcid_ulpgc/test`.  
After that, the program is divided into two modules:
1. `model`: with the base business and main ideas. Contains the **class**: `Weather`, which includes `Location`.
2. `control`: These classes are closer to the user view and interaction, and are not directly protected as the model ones. First, with the `Main` class and, additionally, the `Task` class which is just an extension of the first one used to automatize the procedural and scheduled call to the API. Besides, we have the other classes: `WeatherController` as a controller between classes, `MySenderException` (as the own package exception class) and **interfaces**: `WeatherStore` and `WeatherSupplier` with their own generalizations of classes: `SQLiteWeatherStore` and `OpenWeatherMapSupplier`.

\
Here we have a graphical explanation to define the relationship between classes and interfaces, made with the [StarUML software](https://staruml.io/download/):

![weather-provider](https://github.com/DieGodMF4/Energy-Efficiency-App/assets/145327666/00023eef-38bf-436c-8a11-6d38af1b2b19)

## 2. Energy-provider

Equivalent to the previous mentioned module, this one is also divided into a control and a model package.
1. `model`: With its base representation for the data: `EnergyPrice`, which includes `Slot` as an enumeration frequently used to **measure high or low demand energy on the market**.
2. `control`: Basically the same structure as the previous module and equivalent: **Weather-Provider**.

![energy-provider_diagram](https://github.com/DieGodMF4/Energy-Efficiency-App/assets/145327666/70758b70-8c9a-4f5d-b054-38fc3bf37e59)

## 3. Datalake-builder
This module just consists on just a `control` package, as each class makes an interaction with the user and this module is a "simple" conversion-maker for functionality. The mentioned package contains the classes: `Main`, `MyReceiverException` (as the own package exception class), `TopicSubscriber` and `FileEventStoreBuilder` with their respectives **interfaces**: `Subscriber` and `EventStoreBuilder`.

![dataLake-Builder](https://github.com/DieGodMF4/Energy-Efficiency-App/assets/145327666/85050943-6789-4410-b67a-42759d0e6112)

## 4. App-showcase

This final module differs from the others, as it has a **`view`** package, used for representing the results via **Java Swing**.
1. `model`: Even if it sounds redundant, we have the `Model` class, which includes the encapsulation of the `Items` and the different that will be shown.
2. `view`: With `ViewSwing` as its only member, this module is launch through the `Main` class and shows the final table of recommendations.
3. `control`: All the remaining classes store here. 

![app-showcase](https://github.com/DieGodMF4/Energy-Efficiency-App/assets/145327666/3cd00b5c-24da-4bee-9ba6-44124dde797d)


---

### License and state of the project

_This code is **FREE TO USE** and has no protection of License._   
This code is finished, but it can be freely modified and copied for particular usage; just follow [OpenWeatherMap](#main-purpose--functionality) policies.
_Shoutout to my professors: **Octavio Roncal** and **José Juan Hernández**, specially this last one (as he was not the in charge of it), as their help has been crucial for the successful develop of the project._
