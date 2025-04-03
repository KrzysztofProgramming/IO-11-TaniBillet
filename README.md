# Local development

1. Go to ./tools and run docker compose:
```shell
cd ./tools & docker compose up
```
2. Run server application from intellij: `io.tanibilet.server.MainServerApplication`
3. Go to `localhost:8080`, you should be redirected to login page and after logging in redirect back and see
the restricted page. Use `zenek` as login and password


### Modifying keycloak realm
If you needed to modify keycloak realm or add more users:
1. Go to `localhost:8080/auth`.
2. Login with username/email and password `admin`.
3. After modifying the realm you need to export the changes to make sure other developers will be able to use it,
simply run this command.
```shell
./tools/sync_realm.sh
```
4. Commit and push changes.