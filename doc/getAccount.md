**URL**: http://localhost:8080/api/account/{AccountUUID}

**Method**: `GET`

**Required Body**: None

## Success Response

**Condition**: Account is found

**Code**: `200`

```json
{
  "status": "OK",
  "message": "SUCCESS",
  "data": {
    "fullName": "Nguyen Van A",
    "accountNumber": "62dbc409-778d-40de-8e7f-f5ca0d512611",
    "balance": 2000.00,
    "events": [
      {
        "createdAt": "2024-03-22T06:11:25.278+00:00",
        "eventType": "ACCOUNT_CREATED_EVENT",
        "aggregateUUID": "62dbc409-778d-40de-8e7f-f5ca0d512611",
        "name": "Nguyen Van A"
      }
    ],
    "transactionTempReserve": {},
    "transactions": {},
    "createdAt": "2024-03-22T06:11:25.278+00:00",
    "lastUpdatedAt": null,
    "urlpath": [
      {
        "rel": "self",
        "href": "/api/account/62dbc409-778d-40de-8e7f-f5ca0d512611"
      },
      {
        "rel": "self",
        "href": "/api/account/62dbc409-778d-40de-8e7f-f5ca0d512611/changeName"
      }
    ]
  },
  "urlpaths": []
}
```

## Error Response
**Condition**: Invalid UUID

**Code**: `400`

```json
{
  "status": "ERROR",
  "message": "Validation errors :>",
  "data": {
    "uuid": [
      "Is not a valid UUID value"
    ]
  },
  "urlpaths": null
}
```

**Condition**: Account not found

**Code**: `404`

```json
{
  "status": "ERROR",
  "message": "Account not found",
  "data": null,
  "urlpaths": null
}
```