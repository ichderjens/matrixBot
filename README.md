
# Matrix-ClientServer-API-java
A small and simple java API for the Matrix ClientServer Protocol (see [clientServer api](https://matrix.org/docs/spec/client_server/latest))
The API is still in Beta and known for bugs. If you found or missing a feature one you can create a new issue.


## Usage

### Login
With credentials
```java
//https not supported yet
Client c = new Client("http://matrix.your.server.xyz:8008");  
c.login("examplebot", "wordpass123", loginData -> {  
	if (loginData.isSuccess()) {    
		//Do sth with the bot
	} else {  
		System.err.println("error logging in");  
	}
});
```
With Usertoken
```java
//https not supported yet
Client c = new Client("http://matrix.your.server.xyz:8008");  
c.login("Y0ur70ken", loginData -> {  
	if (loginData.isSuccess()) {    
		//Do sth with the bot
	} else {  
		System.err.println("error logging in");  
	}
});
```

## Features

- Login
	-	[x] UserID/Password
	-	[x] Usertoken
	
- Events
	-	[x] Receive&Send roomevents (join, messages, typing, ....)
	-	[x] Get eventdata by EventID
	-	[x] Multiple eventlistener
	-	[x] Receive events happend when bot was offline
	-	[ ] Custom sync filter
- User
    -	[x] Presence
	-	[x] Typing
	-	[x] Receipts
	-	[x] Send text/messages (formatted and raw)
	-	[x] Login/Logout/Logout all
	-	[x] Join/leave room
	-	[x] Get roommembers
	-	[x] Kick
	-	[x] Ban
	-	[x] Unban
	-	[ ] Create new room