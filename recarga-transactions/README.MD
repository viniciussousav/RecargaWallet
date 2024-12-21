# Recarga Transactions


## API Documentation

API documentation to create new transactions to customer wallet.

### **Request new credit**

**Endpoint**
```console 
POST http://localhost:8082/credit
```

**Request Body**
```json 
{
    "id": "{{#guid}}",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "{{#dateISO}}",
    "description": "test"
}
```

**Responses**:
```json
Status: 202
Body: N/A
```

### **Request new debit**

**Endpoint**
```console 
POST http://localhost:8082/debit
```

**Request Body**
```json 
{
    "id": "{{#guid}}",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "{{#dateISO}}",
    "description": "test"
}
```

**Responses**:
```json
Status: 202
Body: N/A
```

### **Request new transfer**

**Endpoint**
```console 
POST http://localhost:8082/transfer
```

**Request Body**
```json 
{
    "id": "{{#guid}}",
    "amount": 100.00,
    "payer": {
        "agency": 1,
        "account": 123,
        "description": "Enviado"
    },
    "receiver": {
        "agency": 1,
        "account": 321,
        "description": "Recebido"
    },
    "referenceDate": "{{#dateISO}}",
    "description": "test"
}
```

**Responses**:
```json
Status: 202
Body: N/A
```

## Public Events Documentation

All events are produced to "recarga-transactions" topics

### CreditRequestedEvent

```json
{
    "id": "8ca5b262-fb8e-40d5-abe6-1afa2916cc49",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "2024-12-21T05:27:11.687Z",
    "description": "test",
    "key": "1#123"
}
```

### DebitRequestedEvent

```json
{
    "id": "4bc08137-fc19-4f99-aec9-99ca37e5d94b",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "2024-12-21T07:03:34.153Z",
    "description": "test"
}
```

### TransferRequestedEvent

```json
{
    "id": "df01dfdd-f964-42de-81d0-0f9fba09e49c",
    "amount": 100.00,
    "receiver": {
        "agency": 1,
        "account": 321,
        "description": "Recebido"
    },
    "payer": {
        "agency": 1,
        "account": 123,
        "description": "Enviado"
    },
    "referenceDate": "2024-12-21T07:05:14.147Z"
}
```

### CreditSucceededEvent

```json
{
    "id": "90bcf7e7-c245-42ec-871c-fd78503f10b7",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "2024-12-21T06:51:34.313Z",
    "description": "test",
}
```

### DebitSucceededvent

```json
{
    "id": "4bc08137-fc19-4f99-aec9-99ca37e5d94b",
    "amount": 100.00,
    "accountNumber": 123,
    "accountAgency": 1,
    "referenceDate": "2024-12-21T07:03:34.153Z",
    "description": "test",
}
```

### TransferSucceededEvent

```json
{
    "id": "421a7809-03a9-45c7-baed-52cac51bf8c5",
    "amount": 100.00,
    "receiver": {
        "agency": 1,
        "account": 321,
        "description": "Recebido"
    },
    "payer": {
        "agency": 1,
        "account": 123,
        "description": "Enviado"
    },
    "referenceDate": "2024-12-21T07:06:34.382Z"
}
```

### CreditFailedEvent

```json
{
    "id": "fc9137ec-914f-488a-957d-c64e1283b79d",
    "agency": 1,
    "account": 42131,
    "amount": 100.00,
    "errorCode": "CreditAccount",
    "errorMessage": "Account does not exists"
}
```

### DebitFailedvent

```json
{
    "id": "9885062b-ab46-4025-bafd-546da430ad1c",
    "agency": 1,
    "account": 123,
    "amount": 100.00,
    "errorCode": "Amount",
    "errorMessage": "Insufficient amount for debit transaction"
}
```

### TransferFailedEvent

```json
{
    "id": "df01dfdd-f964-42de-81d0-0f9fba09e49c",
    "errorCode": "ReceiverAccount",
    "errorMessage": "Receiver account does not exist"
}
```