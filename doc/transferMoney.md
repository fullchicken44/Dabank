**URL**: http://localhost:8080/api/account/transfer

**Method**: `POST`

**Required Body**:
```json
{
    "fromAccountUUID": "{SenderAccountUUID}",
    "toAccountUUID": "{ReceiverAccountUUID}",
    "amount": 1000.00
}
```

## Success Response
**Condition** : Transfer is successful

**Code** : `200`
```json
{
  "status": "OK",
  "message": "Money transferred",
  "data": null,
  "urlpaths": []
}
```

## Error Response
**Condition** : Either `fromAccountUUID` or `toAccountUUID` has Invalid UUID

**Code**: `404`

```json
{
  "status": "ERROR",
  "message": "Account with id: 4f484ed6-0a80-4c86-a1bb-bf775b737353 not exist",
  "data": null,
  "urlpaths": null
}
```

**Condition** : `fromAccountUUID` and `toAccountUUID` are the same

**Code**: `400`

```json
{
  "status": "ERROR",
  "message": "Validation errors :>",
  "data": {
    "toAccountNumber": [
      "Invalid transfer request, cannot transfer to the same account"
    ]
  },
  "urlpaths": null
}
```

**Condition** : Invalid `amount`

**Code**: `400`

```json
{
  "status": "ERROR",
  "message": "Validation errors :>",
  "data": {
    "value": [
      "Transfer amount must be greater than 0"
    ]
  },
  "urlpaths": null
}
```

