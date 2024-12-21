# Recarga Balance


## Features

API documentation for create a new balance wallet and retrive an existing.

## **Create a new balance wallet**

**Endpoint**
```console 
POST localhost:8081/balance
```

**Request Body**
```json 
{
    "agency": 1,
    "account": 123
}
```

**Responses**:
```json
Status: 201
Body: 
{
    "accountNumber": 123,
    "accountAgency": 1,
    "amount": 0,
    "overdraftLimit": 0
}
```
```json
Status: 409
Body: 
{
    "errorCode": "Conflict",
    "errorMessage": "Account already exists"
}
```

## **Retrive a balance account**

**Endpoint**
```console 
POST localhost:8081/balance/agency/{agency}/account/{account}
```

**Request Body**
```json 
N/A
```

**Responses**:
```json
Status: 200
Body: 
{
    "accountNumber": 123,
    "accountAgency": 1,
    "amount": 0,
    "overdraftLimit": 0
}
```
```json
Status: 404
Body: 
{
    "errorCode": "NotFound",
    "errorMessage": "Account not found"
}
```