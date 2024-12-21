# Recarga Statements


## Features

API documentation to retrive bank statements to check balance at each transaction.

## **Retrive statements**

**Endpoint**
```console 
POST localhost:8083/statements/agency/{agency}/account/{account}
```

**Request Body**
```json 
N/A
```

**Responses**:
```json
Status: 200
Body: 
[
  {
    "transactionId": "69b59c9f-80fb-49d2-b49f-87637954d890",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "2024-12-21T05:27:14.215Z",
    "description": "test",
    "operationType": "CREDIT",
    "balance": 300.00
  },
  {
    "transactionId": "95218674-da3e-4368-8c6b-6460197edb3d",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "2024-12-21T05:27:13.615Z",
    "description": "test",
    "operationType": "CREDIT",
    "balance": 200.00
  },
  {
    "transactionId": "8ca5b262-fb8e-40d5-abe6-1afa2916cc49",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "2024-12-21T05:27:11.687Z",
    "description": "test",
    "operationType": "CREDIT",
    "balance": 100.00
  }
]
```