# API Coding Exercise (Fetch Rewards)
### by Cameron Alberg

Welcome! This web service was developed as part of a coding exercise for Fetch Rewards. This service was developed using the Spring Boot framework.

The repository for this project is available here: https://github.com/cameronalberg/points_demo

A live demo of this API is available at https://points-demo.calberg.me

## Prompt
The service requirements defined for the exercise are listed below:

```
Our users have points in their accounts. Users only see a single balance in their accounts. But for reporting purposes we actually track their points per payer/partner. In our system, each transaction record contains: payer (string), points (integer), timestamp (date). For earning points it is easy to assign a payer, we know which actions earned the points. And thus which partner should be paying for the points. When a user spends points, they don't know or care which payer the points come from. But, our accounting team does care how the points are spent. There are two rules for determining what points to "spend" first:

- We want the oldest points to be spent first (oldest based on transaction timestamp, not the order they’re received)
- We want no payer's points to go negative.
Provide routes that:

- Add transactions for a specific payer and date.
- Spend points using the rules above and return a list of { "payer": <string>, "points": <integer> } for each call.
- Return all payer point balances.
```

## Overview

This service provides the three requested API endpoints (add transaction, spend points, get balances) and operates with the conditions outlined in the requirements. In the absence of an exhaustive set of allowable conditions (and the stated request to use best judgement), several additional assumptions were made on the desired system requirements:
Assumptions:

- All parseable transactions posted to the transaction route are considered to have valid point values and will not result in a payer's balance becoming negative.
- Spend calls are considered separate from the transactions added via the transaction route, and are therefore not stored as transactions.
- Transactions that contain the same name and timestamp are considered duplicates and are not re-added.
- If a spend call requests to spend more points than are possible, the maximum amount of possible points will be spent.
- A user of the service may want to retrieve the details of a previously added transaction, and so the response to the add transaction route should return a reference to the added transaction.

One additional endpoint was added to be able to retrieve an added transaction.

# Installation

This service can be deployed via Docker or using an executable JAR file.

## Docker
The Docker Engine is required to deploy this web service with Docker.

The latest docker image can be retrieved using the following command:

```
docker pull calberg/XXX
```
The image can be run as a container with the following command:
```
 docker run -p {host_port}:8080 calberg/XXX 
```
```host_port``` can be 8080 or another port number if 8080 is already taken.
## JAR
Java 11 is required to run this application via a JAR File.

The latest release of this API can be downloaded as a .jar package here

If Java is installed, the application can be started with the following command: 
```
java -jar ./{latestReleaseName}.jar
```

# API Usage

This API follows the REST architecture style and accepts/returns JSON. Request bodies are assumed to be properly formatted JSON. 

## Add Transaction (/add)
HTTP Method: **POST**

Request Body Schema: ``` application/json ```

| key | type | description |
| --- | --- | --- |
| payer | string | Name of Payer. Should contain at least one alphanumeric character. Not case-sensitive. |
| points | integer | Number of points gained by transaction. Can be negative. |
| timestamp | string | Time of transaction captured as a string using the following format: "YYYY-MM-DDThh:mm:ssTZD"|

### Example Usage:
#### Request URL:

```  https://{service_url}/add ```

#### Request Body Example (required):
```json
{
    "payer": "DANNON",
    "points": 100,
    "timestamp":"2020-11-01T14:00:00Z"
}
```
### Responses:
**201 CREATED**

The transaction was successfully added to the system (or already existed). A transactionID will be provided in the response body, which can be used to retrieve the transaction details via the transaction lookup endpoint. 
#### Example Response:
```json
{
    "transactionID": 3
}
```
<hr>

**400 BAD REQUEST**

An error was encountered while trying to add the transaction. If possible, error details will be provided in the response body.
<hr>

## Spend Points (/spend)
HTTP Method: **PUT**

Request Body Schema: ``` application/json ```

type | description |
| --- | --- |
| integer ``` >= 0 ``` | Number of points to be spent. Cannot be negative. |


### Example Usage:
#### Request URL:

```  https://{service_url}/spend ```

#### Request Body Example (required):
```json
{
5000
}
```
### Responses:
**200 OK**

All possible points were spent. The response body indicates which payer's points were spent. **Note:** The sum of the returned points may be less than the number of points specified to be spent, in the event that not enough points were available.

|  | type | description |
| --- | --- | --- |
| type | string |integer ``` >= 0 ``` |
| description | Name of payer. | Number of payer points spent. |
| example | ``` "DANNON" ``` | ``` -100 ```|

#### Example Response:
```json
{
	"DANNON": -100,
	"UNILEVER": -200,
	"MILLER COORS": -4700
}
```
<hr>

**400 BAD REQUEST**

An error was encountered while trying to spend the points requested. If possible, error details will be provided in the response body.
<hr>

## Get Payer Balances (/balance)
HTTP Method: **GET**

Query Parameters: **None**

### Example Usage:
#### Request URL:

```  https://{service_url}/balance ```

### Responses:
**201 CREATED**

#### Response Schema ``` application/json ```

|  | key | value |
| --- | --- | --- |
| type | string |integer ``` >= 0 ``` |
| description | Name of payer. | Number of points remaining for payer. |
| example | ``` "DANNON" ``` | ``` -100 ```|


#### Example Response:
```json
{
	"DANNON": 1000,
	"UNILEVER": 0,
	"MILLER COORS": 5300
}
```
<hr>

## Lookup Transaction (/transactions)
HTTP Method: **POST**

### Query Parameters (required):

| parameter | type | description |
| --- | --- | --- |
| transactionID | integer | ID of previously added transaction. |



### Example Usage:
#### Request URL:

```  https://{service_url}/add ```

#### Request Body Example (required):
```json
{
    "payer": "DANNON",
    "points": 100,
    "timestamp":"2020-11-01T14:00:00Z"
}
```
### Responses:
**200 OK**
#### Response Body Schema ``` application/json ```
| key | type | description |
| --- | --- | --- |
| payer | string | Name of Payer.|
| points | integer | Number of points gained by transaction.|
| timestamp | string | Timestamp of transaction.|
| transactionID | integer | ID of transaction. |
#### Example Response:
```json
{
	"payer": "DANNON",
	"points": 100,
	"timestamp":"2020-11-01T14:00:00Z",
	"transactionID": 2
}
```
<hr>

**404 NOT FOUND**

The specified transaction ID could not be found.
<hr>
