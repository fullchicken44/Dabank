Small simulation of a bank that allows to create accounts, change names and transfer money between two accounts by using event sourcing pattern.

Not yet added database or any other external dependencies so everything is stored in memory with event bus for now.

## Models
![Class diagram](/doc/class.png)

## Endpoints
| Endpoint                                     | Method | URL                                                            |
|----------------------------------------------|--------|----------------------------------------------------------------|
| [List Accounts](doc/listAccount.md)          | `GET`  | http://localhost:8080/api/account/listAccounts                 |
| [Get Account](doc/getAccount.md)             | `GET`  | http://localhost:8080/api/account/{accountUUID}                |
| [Change Full Name](doc/changeAccountName.md) | `POST` | http://localhost:8080/api/account/{accountUUID}/changeFullName |
| [Create Account](doc/createAccount.md)       | `POST` | http://localhost:8080/api/account/createAccount                |
| [Transfer Money](doc/transferMoney.md)       | `POST` | http://localhost:8080/api/account/transfer                     |

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
