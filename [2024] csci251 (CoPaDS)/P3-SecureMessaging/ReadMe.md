# P3-SecureMessaging

# Description
This project takes the prime generation from previous project and uses it to generate RSA keys. It then uses these keys to encrypt and decrypt messages. It sends and receives public keys and messages over a RIT hosted server.

# Order of Execution:
1. Generate your public and private keys. `dotnet run keyGen`
2. Send your public key to the server. `dotnet run sendKey {email}`
3. Receive the public key of the person you want to send a message to. `dotnet run getKey {email}`
4. Send a message to the email of choice. `dotnet run sendMsg {email} {message}`
5. Receive messages from the server. `dotnet run getMsg {your email}`
- Note: You can only read messages of emails with private keys that you have generated.