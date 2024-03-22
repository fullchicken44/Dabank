Small simulation of a bank that allows to create accounts, change names and transfer money between two accounts by using event sourcing pattern.

Not yet added database or any other external dependencies so everything is stored in memory with event bus for now.

## Models
![Class diagram](/doc/class.png)

## Endpoints
| Endpoint                                  | Method | URL                                                            |
|-------------------------------------------|--------|----------------------------------------------------------------|
| [List Accounts](doc/listaccounts.md)      | `GET`  | http://localhost:8080/api/account/listAccounts                 |
| [Get Account](doc/getaccount.md)          | `GET`  | http://localhost:8080/api/account/{accountUUID}                |
| [Change Full Name](doc/changefullname.md) | `POST` | http://localhost:8080/api/account/{accountUUID}/changeFullName |
| [Create Account](doc/createaccount.md)    | `POST` | http://localhost:8080/api/account/createAccount                |
| [Transfer Money](doc/transfermoney.md)    | `POST` | http://localhost:8080/api/account/transfer                     |

Health check endpoint: http://localhost:8080/api/health

**Code**: `200`
```json
{
  "status": "OK",
  "message": "System's fine",
  "data": null,
  "urlpaths": null
}
```
