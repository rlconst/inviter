# Invite users to organization on Auth0

Unfortunately organization invite flow is [broken](https://community.auth0.com/t/organization-invitation-flow/85868). This is a dirty and ad-hoc workaround consist of following steps:

1. Create user with random password
2. Addign roles
3. Invite to organization
4. Reset password

# Build

`./gradlew build`

# Configure

* Copy application-cli.yml.example file and rename as application-cli.yml
* Go to auth0 management and fill CAPITALIZED params 

Application should
* have **client-credentials** flow enabled 
* be authorized in standard **Auth0 Management API**
* have following grants: `create:users`, `create:role_members`, `create:organization_members`,

# Usage

```
Usage: inviter [-hV] -e=<email> -n=<name> -o=<organization> -r=<roles>... [-r=<roles>...]...
...
  -e, --email=<email>      User email to send invite e.g. jdoe@example.com
  -h, --help               Show this help message and exit.
  -n, --name=<name>        User name e.g. John Doe
  -o, --organization=<organization>
                           Organization ids org_abcdefgh
  -r, --roles=<roles>...   Roles e.g. user
  -V, --version            Print version information and exit.
```
