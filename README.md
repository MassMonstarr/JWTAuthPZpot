# Spring Boot JWT Auth

Spring Boot JWT auth demo.  

## Getting Started  

Users with the roles, **ROLE_ADMIN** and **ROLE_USER** exist.   

**Username:** Admin  
**Password:** 123  

**Username:** User  
**Password:** 123  


To sign in, send post request to [localhost:8080/api/auth/login](localhost:8080/api/auth/login) with body  

{  
	"usernameOrEmail": "Admin",  
	"password": "123"  
}  

**or**
{  
	"usernameOrEmail": "User",  
	"password": "123"  
}  

A JWT will be returned from the server, sample:  

{  
    "accessToken": "eyJhbGciOiJIUzUxMiJ9",  
    "tokenType": "Bearer"  
}  

Use the token to access protected end points.  

### Register a new user  
Send a POST request to [localhost:8080/api/auth/register](localhost:8080/api/auth/register)   

With body:  
{  
	"username": "x",  
	"email": "y",  
	"password": "z"  
}  

## End Points
[localhost:8080/admin](localhost:8080/admin) only allows admins.  
[localhost:8080/user](localhost:8080/user) only allows users and admins.  

[localhost:8080/api/auth/register](localhost:8080/api/auth/register) register a new user.  
[localhost:8080/api/auth/login](localhost:8080/api/auth/login) login.  
