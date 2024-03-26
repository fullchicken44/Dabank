**URL**: http://localhost:8080/api/account/{AccountUUID}/changeName

**Method**: `PUT`

**Required Body**:
```json
{
    "fullName": "Siuuu"
}
```

## Success Response
**Condition** : Account name is changed

**Code** : `200`
```json
{
  "status": "OK",
  "message": "Name changed",
  "data": "62dbc409-778d-40de-8e7f-f5ca0d512611",
  "urlpaths": [
    {
      "rel": "self",
      "href": "/api/account/62dbc409-778d-40de-8e7f-f5ca0d512611"
    },
    {
      "rel": "self",
      "href": "/api/account/62dbc409-778d-40de-8e7f-f5ca0d512611/changeName"
    }
  ]
}
```

## Error Response
**Condition** : Empty `fullName`

Same as [createAccount](createAccount.md) error response.

**Condition** : UUID in path is not valid

Same as [getAccount](getAccount.md) error response.

**Condition** : Account not found

same as [getAccount](getAccount.md) error response.

## Condition after name changed
Change name event should be added to the account events list.
```json
{
  "status": "OK",
  "message": "SUCCESS",
  "data": [
    {
      "fullName": "Siuuu",
      "accountNumber": "62dbc409-778d-40de-8e7f-f5ca0d512611",
      "balance": 2000.00,
      "events": [
        {
          "createdAt": "2024-03-22T06:11:25.278+00:00",
          "eventType": "ACCOUNT_CREATED_EVENT",
          "aggregateUUID": "62dbc409-778d-40de-8e7f-f5ca0d512611",
          "name": "Nguyen Van A"
        },
        {
          "createdAt": "2024-03-22T06:21:55.976+00:00",
          "eventType": "NAME_CHANGED_EVENT",
          "aggregateUUID": "62dbc409-778d-40de-8e7f-f5ca0d512611",
          "fullName": "Siuuu"
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
    }
  ],
  "urlpaths": []
}
```

