**URL**: http://localhost:8080/api/account/createAccount

**Method**: `POST`

**Required Body**:
```json
{
    "fullName": "Ronaldo"
}
```

## Success Response
**Code** : `200`
```json
{
  "status": "OK",
  "message": "Account created",
  "data": "09873ce6-0bb0-4db4-9ff2-0e35311e051f",
  "urlpaths": [
    {
      "rel": "self",
      "href": "/api/account/09873ce6-0bb0-4db4-9ff2-0e35311e051f"
    },
    {
      "rel": "self",
      "href": "/api/account/09873ce6-0bb0-4db4-9ff2-0e35311e051f/changeName"
    }
  ]
}
```

## Error Response
**Condition** : `fullName` is empty

**Code**: `400`
```json
{
  "status": "ERROR",
  "message": "Invalid request. Full name cannot be empty.",
  "data": null,
  "urlpaths": null
}
```