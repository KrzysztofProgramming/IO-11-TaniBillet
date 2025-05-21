# Local development

1. Go to ./tools and run docker compose:
```shell
cd ./tools & docker compose up
```
2. Run server application from intellij: `io.tanibilet.server.MainServerApplication`, remember to setup env variable $SMTP_PASSWORD
4. To use API you need JWT token from keycloak, you can get it using OAuth2 flow:
```
Grant Type: Authorization Code
Callback URL: http://localhost:8080/login/oauth2/code/keycloak
Auth URL: http://localhost:8080/auth/realms/tani-bilet/protocol/openid-connect/auth
Access token URL: http://localhost:8080/auth/realms/tani-bilet/protocol/openid-connect/token
Client ID: tani-bilet-app
Client Secret: tani-bilet-app
```
5. There are two users available: `creator` and `zenek`. Both have password equal to username.
### Swagger
Swagger is accessible at the following url: http://localhost:8080/swagger-ui/index.html
It has already configured OAuth2 flow, so you only need to pass client id and client secret to authorize using it.
### Postman
In postman select OAuth2 authorization option and pass the OAuth2 parameters from above


## Modifying keycloak realm
If you needed to modify keycloak realm or add more users:
1. Go to `localhost:8080/auth`.
2. Login with username/email and password `admin`.
3. After modifying the realm you need to export the changes to make sure other developers will be able to use it,
simply run this command.
```shell
./tools/sync_realm.sh
```
4. Commit and push changes.