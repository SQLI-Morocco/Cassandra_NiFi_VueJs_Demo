# Yacovida - Yet Another Covid 19 Dashboard - Part 1

Cassandra is a leader-less distributed NoSQL database with a partitioned wide column storage model. This blog is the first part of a **two part demo** around Cassandra Database. The goal of part 1 is to showcase the usage of Cassandra in a data ingestion/data pipeline and quering context.

This repo contains a fully working example of the demo, in order to build the solution, you will find the following artefacts:

- Nifi Data flow supporting daily and hourly data pull into Cassandra.
- SpringBoot REST APIs using SpringData for exposing the data through API calls.
- VueJS Dashboard with ChartJS and Bulma CSS Framework display various data and charts.
- CQL, Python, Groovy and Shell scripts supporting the demo.
- Excalidraw sketch

Prerequisites for running the demo:

- [Apache NiFi 1.11.3](https://nifi.apache.org/)
- [Apache Cassandra 3.11.6](https://cassandra.apache.org/)
- [SpringBoot 2.2.6](https://spring.io/projects/spring-boot)
- [SpringData for Apache Cassandra](https://docs.spring.io/spring-data/cassandra/docs/current/reference/html/#)
- [VueJS 2.6.11](https://vuejs.org/)
- [ChartJS 2.9.3](https://www.chartjs.org/)
- [Bulma 0.9.0](https://bulma.io/)
- [ArchUnit 0.13.1](https://www.archunit.org/)

We also used the excellent [Excalidraw](https://excalidraw.com/) tool for mockups.

## Covid19 Dashboard Scenario.

The scenario used is a COVID-19 Data Dashboard, thus to stay in the time context in wich this demo was built. The data used is provided by the [COVID-19 Data Repository by the Center for Systems Science and Engineering (CSSE) at Johns Hopkins University](https://github.com/CSSEGISandData/COVID-19).

This demo is for education/training purpose, for official data and information on COVID19 please refer to [WHO](https://www.who.int/) and [JHU Dashboard](https://coronavirus.jhu.edu/map.html).

In the following section, you will find each components technology used in this demo and a description of the steps followed to build the solution.

You can check the final result on [this page](https://nabil.github.io/yacovida).

---

**Cassandra**

The main advantage of Cassandra over the other NoSQL database is its linear scalability to support [high throughput](https://www.datastax.com/blog/2019/03/apache-cassandratm-four-interesting-facts) with [no single point of failure](https://stackoverflow.com/a/21667085).

This section is a tentative compressed introduction to Cassandra with a focus on a list of key facts that are a must-know in order to handle Cassandra in an optimal way.

For an indepth training about Cassandra, a good place to start would be the excellent [Datastax Academy](https://academy.datastax.com/#/) free training/courses and of course the [Apache Cassandra Architecture](https://cassandra.apache.org/doc/latest/architecture/overview.html) documentation.

Another excellent stop would be the [YouTube Datastax Channel](https://www.youtube.com/c/DataStax/videos).

For more fun facts on Cassandra, expand the list below:

<details>
  <Summary>Cassandra Must Know Facts!</Summary>

1. [x] It can work with a single node, but obviously you will benefit from its capabilities if you run with a minimum of 3 nodes.
1. [x] The nodes can be grouped logically in a ring like architecture.
1. [x] All nodes communicates between each others thourgh peer-to-peer communication called Gossiping.
1. [x] Cassandra architecture allows an effort-less horizontal scalling by adding more nodes to the ring.
1. [x] Multiples rings can exists in a Cluster, each ring can be in a different datacenter.
1. [x] Deciding the composition or your table partition key is critical for performance as you want to reduce the number of communication required between the nodes in order to complete a query.
1. [x] Even for the smallest size table, all data is distributed in the cluster based on a partition key value. To avoid datasque, a hash of the partition key is produced and the resulting token is used to distribute the data accross the nodes evenely.
1. [x] Each node have a maximum of 128 [virtual nodes](https://docs.datastax.com/en/cassandra-oss/3.0/cassandra/architecture/archDataDistributeDistribute.html).
1. [x] Each node is responsible for a token range.
1. [x] A full scan over a table means a full scan of all the nodes in the ring, i.e increased network calls and resources usage.
1. [x] All rows with the same partition key are stored on the same partition.
1. [x] A query goal should always be to read from a single partition, the where clause should filter by the partition key.
1. [x] Its recommended that you store up to [1TB](https://wikitech.wikimedia.org/wiki/Cassandra/Hardware) of data in each node, beyond this value the nodes start to slow down.
1. [x] You can run up to 3K TPS per core.
1. [x] A ring should always have an odd number of nodes in order to
1. [x] Leader-less architecture means that each node can act as a leader (coordinator) node during a specific request. A request can be sent to any node and it will be managed to ensure the request is fullfilled by the required node.
1. [x] Data is distributed but also replicated, i.e copied on all replica nodes. The number of replication is based on the replication factor RF.
1. [x] Its recommended that your replication factor is an odd number, thus allowing a faster conscensus.
1. [x] If a node is down in a cluster and the other replica nodes, a [hinted handoff](https://cassandra.apache.org/doc/latest/operating/hints.html) is put in place to buffer the request until the node is back.
1. [x] By default the hinted handoff duration is 3 hours. The value can be changed but the long the duration, the longer the time required to bring the node back once is up.

</details>

**NiFi**

NiFi is a powerfull Flow Based Programming software designed for [massive](https://blog.cloudera.com/benchmarking-nifi-performance-and-scalability/) data flow from any source to any destination. Based on Java, it support scripting and extension through adding new Processors on top of its already rich 286 Processors, 84 Controller Services 15 and reporting tasks!

**SpringBoot**/**SpringData**

We are using SpringBoot to speedup exposing our APIs and start building the UI. This is done by levering the [containerless](https://github.com/spring-projects/spring-framework/issues/14521) and starter dependencies to simplify our build configuration. We will use also SpringData for Apache Cassandra to benefit from the [Repository Support](https://docs.spring.io/spring-data/cassandra/docs/current/reference/html/#repositories) to simplfy our queries.

For more reading on Spring, a good start is to have an overview of the [history](https://www.quickprogrammingtips.com/spring-boot/history-of-spring-framework-and-spring-boot.html) of the spring framework.

To get started quickly, wer are using the [Spring Initialzr](https://start.spring.io/) tool to generate your project structure and starter POM.

We are also using ArchUnit to validate our architecture inside our SpringBoot application, have a look at [C19ArchitectureTest](https://raw.githubusercontent.com/nabil/yacovida/master/content/images/dashboard-sketch.png)

**Learning**

You can learn from this demo by doing some of the suggested exercies, you will find an exercies suggested in each section and at the end in the exercices section.

---

**_Lets get started!_**

https://github.com/apache/cassandra#getting-started

To follow first Cassandra Database principle: design your data model based on your queries, lets first start by sketching how we want our UI to look like and extract the main queries.

Using [Excalidraw](https://excalidraw.com/) we obtain the following sketch:

![Dashboard Sketch](https://raw.githubusercontent.com/SQLI-Morocco/Cassandra_NiFi_VueJs_Demo/master/content/images/dashboard-sketch.png)

The following queries can be extracted from this sketch:

- Latest aggregated global data for Confirmed, Deaths, Active and Recovered cases
- List of all top level regions (countries)
- Latest aggregated data for Confirmed, Death, Active and Recovered cases for each top level region (country)
- List of historic data point for each region

In order to populate our tables correclty we have to understand the structure of the JHU CSSE COVID-19 [dataset](https://github.com/CSSEGISandData/COVID-19/tree/master/csse_covid_19_data). We will be using the daily reports files:
[csse_covid_19_daily_reports](https://github.com/CSSEGISandData/COVID-19/tree/master/csse_covid_19_data/csse_covid_19_daily_reports).

The documentation explains that there are multiples refresh frequencies:

- Since June 15, refresh occurs between 04:45 and 05:15 GMT.
- Files on and after April 23, once per day between 03:30 and 04:00 UTC.
- Files from February 2 to April 22: once per day around 23:59 UTC.
- Files on and before February 1: the last updated files before 23:59 UTC.

When you analyze the files from the different refresh group you can notice that **not only the files are on diffrent refresh schedule, but they also have different "schemas"**. For this demo we will be using the dataset from April 23 onward, see learning section to implement loading the data for the other date ranges.

We will only focus on the data we need for our dashboard, thus we will use only the daily data points.

Once your instance of Cassandra is [intalled](https://cassandra.apache.org/doc/latest/getting_started/installing.html), start your [cql shell](https://cassandra.apache.org/doc/latest/getting_started/querying.html) and run the commande below.

First let's create a keyspace with a [SimpleStrategy](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/architecture/archDataDistributeReplication.html) with a replication factor of 1 as we will be using one node only, this can be modified at later stage. For the Strategy, its recommended to go with a [NetworkTopologyStrategy](https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/operations/opsChangeKSStrategy.html) for production usage.

```CQL
CREATE KEYSPACE coronavirus WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};
```

Then lets create our daily data points table.

```CQL
CREATE TABLE coronavirus.region_daily_data_points (
    region text,
    year int,
    month int,
    day int,
    last_update timestamp,
    province_state text,
    admin2 text,
    active bigint,
    combined_key text,
    confirmed bigint,
    deaths bigint,
    fips text,
    lat float,
    long_ float,
    recovered bigint,
    PRIMARY KEY (region, year, month, day, last_update, province_state, admin2))
    WITH CLUSTERING ORDER BY (year DESC, month DESC, day DESC, last_update DESC, province_state ASC, admin2 ASC);
```

Fore more advanced time series configuration have a look at the [Time Window CompactionStrategy](https://cassandra.apache.org/doc/latest/operating/compaction/twcs.html#twcs) strategy.

Now, lets create our latest (hourly) data table. We are only interested in the latest data, thus our table is partitionned by the main country_region thus allowing faster aggregation at the country level as each country_region rows will be on the same node.

```CQL
CREATE TABLE coronavirus.region_data_global_latest (
    country_region text,
    combined_key text,
    active bigint,
    admin2 text,
    confirmed bigint,
    deaths bigint,
    fips text,
    last_update timestamp,
    lat float,
    long_ float,
    province_state text,
    recovered bigint,
    PRIMARY KEY (country_region, combined_key)
) WITH CLUSTERING ORDER BY (combined_key ASC);
```

As an exercies, you can add a new table for querying the available region and sub region with their dependencies.

_Now let's populate our table with the data using NiFi._

First, start by [installing and running your instance of NiFi](http://nifi.apache.org/docs/nifi-docs/html/getting-started.html#downloading-and-installing-nifi). Once started, open your browser and navigate to your configured url.

For the demo, we need to create two separate groups: Hourly and Daily

![Nifi Groups](https://raw.githubusercontent.com/SQLI-Morocco/Cassandra_NiFi_VueJs_Demo/master/content/images/nifi-groups.png)

![Nifi Hourly Group](https://raw.githubusercontent.com/SQLI-Morocco/Cassandra_NiFi_VueJs_Demo/master/content/images/nifi-hourly-group.jpg)

![Nifi Daily Group](https://raw.githubusercontent.com/SQLI-Morocco/Cassandra_NiFi_VueJs_Demo/master/content/images/nifi-daily-group3.png)

The flow described is available in the nifi folder and can be uploaded throught the mouse context menu.

![Context Menu](https://raw.githubusercontent.com/SQLI-Morocco/Cassandra_NiFi_VueJs_Demo/master/content/images/nifi-mouse-menu.png)

The demo show case the usage of Groovy to generate the required date to be loaded as separate flows:

```groovy
class Const {
  static Date lastUpdate = null;
}

static onStart(ProcessContext context) {
  Const.lastUpdate = new Date('04/23/2020')
  println "onStart $context ${Const.lastUpdate}"
}

static onStop(ProcessContext context) {
  println "onStop $context ${Const.lastUpdate}"
}

d1 = Const.lastUpdate
d2 = new Date()
(d1..d2).each {
    def flowFile = session.create()
    flowFile = session.putAttribute(flowFile, 'day', it.format('dd'))
    flowFile = session.putAttribute(flowFile, 'month', it.format('MM'))
    flowFile = session.putAttribute(flowFile, 'year', it.format('yyyy'))
    flowFile = session.putAttribute(flowFile, 'targetDate', it.format('MM-dd-yyyy'))
    REL_SUCCESS << flowFile
}
Const.lastUpdate = d2
```

This ensure that only the initial call is loading all the data.

The demo also shows a simple methode to cleanup the received date based on different date format:

```groovy
import java.text.ParseException
import java.text.SimpleDateFormat

class DateConst {

  static SimpleDateFormat DATE_FORMAT_GMT = null;
  static def datePatterns = [
      'yyyy-MM-dd HH:mm:ss',
      'yyyy-MM-dd HH:mm',
      'yyyy-MM-dd',
      'MM/dd/yy HH:mm:ss',
      'MM/dd/yy HH:mm',
      'MM/dd/yy'
    ];

}

static onStart(ProcessContext context) {
  DateConst.DATE_FORMAT_GMT = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
  DateConst.DATE_FORMAT_GMT.setTimeZone(TimeZone.getTimeZone('UTC'))
}

def flowFile = session.get()
if (!flowFile) return

String last_update = flowFile.last_update
String parsedDate = last_update

for (f in DateConst.datePatterns) {
  try {
    SimpleDateFormat dateFormat = new SimpleDateFormat(f)
    dateFormat.setTimeZone(TimeZone.getTimeZone('UTC'))
    Date date = dateFormat.parse(last_update)
    parsedDate = DateConst.DATE_FORMAT_GMT.format(date)
    break
    } catch (ParseException pe) {
  //silent
  }
}
flowFile = session.putAttribute(flowFile, 'last_update', parsedDate)
session.transfer(flowFile, REL_SUCCESS)
```

Once you have imported your Flow you can run the groups to start populating your database.

As an exercies, you can add processors to populate the regions table with available regions and sub regions with their dependencies.

_Now Lets expose our data through API using SpringBoot_

The project starter pom was generated using [Spring Initialzr](https://start.spring.io/). If you are new to spring boot a good start is to follow this excellent [guide](https://spring.io/guides/gs/spring-boot/).

In order for the front-end to retreive the necessary data to build UI we are exposing three resources:

- SummaryResource
- DataPointResource
- HealthCheckResource

The application architecture is based on the [Ports And Adapters Architecture](http://wiki.c2.com/?HexagonalArchitecture), more reading [here](https://alistair.cockburn.us/hexagonal-architecture/). We are also providing an ArchUnit test to verify some of the compliance.

As an exercices modify the HealthCheckResource to verify database status
As an exercices you can add the region resources to expose regions and sub regions information

Visit the application.properties files and ensure the setting are aligned with your configuration:

```properties
spring.data.cassandra.keyspace-name=coronavirus
spring.data.cassandra.contact-points=localhost
spring.data.cassandra.port=9042
spring.data.cassandra.username=cassandra
spring.data.cassandra.password=cassandra
spring.data.cassandra.schema-action=create_if_not_exists
```

Then run the project in the springboot folder:

```SH
mvn spring-boot:run
```

## Dashboard

This demo uses VueJS. A good start to lean VueJS is these two crash courses:

- [Vue JS Crash Course](https://www.youtube.com/watch?v=Wy9q22isx3U)
- [Intro to Vue.js](https://www.vuemastery.com/courses/intro-to-vue-js/vue-instance)

_Let's run the dashboard!_

Once you open the projet at the vuejs folder level, you can setup the projet by running the following command:

```sh
yarn install
```

Once that is done, you can start development by compiling and using hot-reloads for development

```sh
yarn serve --port 4242
```

![Dashboard 1](https://raw.githubusercontent.com/SQLI-Morocco/Cassandra_NiFi_VueJs_Demo/master/content/images/dashboard1.jpg)

A live version is available for demo [here](https://nabil.github.io/yacovida/).

Once you are ready to publish your site, you can compiles and minifies for production

```sh
yarn build
```

_Caching instead of API calls!_

In order to reduce impact on our APIs without using any tool (Varnish etc), we can use a python script to geneate a json version of the APIs response.

```SH
python3 buildStaticData.py /path/to/data/folder/
```

Once the data is generated, you need to modify the VueJS application main.js file to activate the caching and the URLs prefix and suffix are correctly defined:

```js
Vue.config.cached = false;
Vue.prototype.$apiurl = Vue.config.cached ? '' : 'http://localhost:8082/';
Vue.prototype.$apiurlsuffix = Vue.config.cached ? '.json' : '';
```

As an exercices you can add the option to push the resulting file in an [S3](https://docs.aws.amazon.com/AmazonS3/latest/dev/UsingBucket.html) bucket or [another](https://firebase.google.com/docs/hosting) cloud service provider.

**Exercices**

- Add data points for the period from February 2 to April 22
- Pre-populate country information from [RestCountries API](https://restcountries.eu) service, load the country code and nativeName value in the Dashboard. See: https://restcountries.eu/rest/v2/all
- Use the [Country Flag](https://www.countryflags.io/) service to enrich the Dashboard with the flag information

**References**

- https://www.datastax.com/blog/2015/02/basic-rules-cassandra-data-modeling
- https://www.datastax.com/blog/2019/03/apache-cassandratm-four-interesting-facts
