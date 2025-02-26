using System.Net;
using System.Numerics;
using System.Text;
using System.Text.Json;
using System.Security.Cryptography;
using Project3.Keys;
using Project3.Network;
using Project3.ProgramHelp.Exceptions;
using Project3.ProgramHelp.Exceptions.Key;
using Project3.ProgramHelp.Exceptions.Args;
using Project3.ProgramHelp.Exceptions.Bits;
using Project3.ProgramHelp.Exceptions.Option;
using Project3.ProgramHelp.Exceptions.Network;
using Project3.ProgramHelp.Exceptions.Encryption;

namespace Project3
{
    namespace ProgramHelp
    {
        namespace Exceptions
        {
            class Error : Exception
            {
                /// <summary>
                /// Initializes a new instance of the Error class.
                /// </summary>
                public Error(string message) : base(message) { }
            }

            namespace Key
            {
                /// <summary>
                /// Exception thrown when the key data fails to save.
                /// </summary>
                class KeyNotFound : Error
                {
                    public KeyNotFound(string email) : base($"Key does not exist for {email}") { }
                }

                /// <summary>
                /// Exception thrown when the key data fails to save.
                /// </summary>
                class FailedToSave : Error
                {
                    public FailedToSave(string message) : base($"Failed to save {message}") { }
                }

                /// <summary>
                ///  Exception thrown when the key data fails to deserialize.
                ///  </summary>
                class FailedToSerialize : Error
                {
                    public FailedToSerialize() : base($"Failed to serialize key data.") { }
                }

                /// <summary>
                ///  Exception thrown when the key data fails to deserialize.
                ///  </summary>
                class FailedToDeserialize : Error
                {
                    public FailedToDeserialize() : base($"Failed to deserialize key data.") { }
                }

                /// <summary>
                /// Exception thrown when the key is undecryptable.
                /// </summary>
                class PrivateKeyNotFound : Error
                {
                    public PrivateKeyNotFound(string email) : base($"Private key does not exist for {email}") { }
                }
            }

            namespace Network
            {
                /// <summary>
                /// Exception thrown when the client fails to retrieve a message.
                /// </summary>
                class FailedToRetrieve : Error
                {
                    public FailedToRetrieve(string endpoint) : base($"Client failed to retrieve {endpoint}.") { }
                }

                /// <summary>
                /// Exception thrown when the client fails to send a message.
                /// </summary>
                class FailedToSend : Error
                {
                    public FailedToSend(string message) : base($"Client failed to send {message}") { }
                }
            }

            namespace Encryption
            {
                /// <summary>
                /// Exception thrown when the message is too long.
                /// </summary>
                class MessageTooLong : Error
                {
                    public MessageTooLong() : base("Message is too long for key en/decrpytion.") { }
                }
            }

            namespace Args
            {
                /// <summary>
                /// Exception thrown when the number of command-line arguments is less than the required minimum.
                /// </summary>
                class TooLittleArgs : Exception
                {
                    public TooLittleArgs(string[] args, int min, int given) : base($"Invalid Args Count ({given}): Must provide at least {min} arguments. \nMissing {string.Join(", ", args.Skip(given))}") { }
                }

                /// <summary>
                /// Exception thrown when the number of command-line arguments exceeds the maximum allowed.
                /// </summary>
                class TooManyArgs : Exception
                {
                    public TooManyArgs(int max, int given) : base($"Invalid Args Count ({given}): Too many arguments, maximum is {max}") { }
                }
            }

            namespace Bits
            {
                /// <summary>
                /// Exception thrown when the number of bits is not a valid integer.
                /// </summary>
                class NotParseableToInt : Exception
                {
                    public NotParseableToInt(string arg) : base($"Invalid Bits ({arg}): Number of bits must be a valid integer") { }
                }

                /// <summary>
                /// Exception thrown when the number of bits is not a multiple of 8.
                /// </summary>
                class NotMultipleOf8 : Exception
                {
                    public NotMultipleOf8(int num) : base($"Invalid Bits ({num}): Number of bits is not a multiple of 8") { }
                }

                /// <summary>
                /// Exception thrown when the number of bits is less than the required minimum.
                /// </summary>
                class TooLittleBits : Exception
                {
                    public TooLittleBits(int min, int num) : base($"Invalid Bits ({num}): Number of bits must be at least {min}") { }
                }

                /// <summary>
                /// Exception thrown when the number of bits is invalid (not a multiple of 8 or less than 32).
                /// </summary>
                class InvalidBits : Exception
                {
                    public InvalidBits(int min, int num) : base($"Invalid Bits ({num}): Number of bits must be a multiple of 8 and at least 32") { }
                }
            }

            namespace Option
            {
                /// <summary>
                /// Exception thrown when an invalid option is provided.
                /// </summary>
                class InvalidOption : Exception
                {
                    public InvalidOption(string opt, string[] options) : base($"Invalid option (\"{opt}\"): Option must be one of these: {String.Join(", ", options)}") { }
                }
            }
        }

        /// <summary>
        /// Provides helper methods for displaying help messages and error details.
        /// </summary>
        class Helper
        {
            public string helpMessage = "Welcome to the program.";
            public string helpExamples = "No examples provided.";

            /// <summary>
            /// Prints the help message to the console.
            /// </summary>
            public void PrintHelp()
            {
                Console.WriteLine(this.helpMessage);
            }

            /// <summary>
            /// Prints the help examples to the console.
            /// </summary>
            public void PrintExamples()
            {
                Console.WriteLine(this.helpExamples);
            }

            /// <summary>
            /// Displays an error message along with the help message and examples.
            /// </summary>
            /// <param name="errMessage">The error message to display.</param>
            public void ErrorHelp(string errMessage)
            {
                this.PrintHelp();
                this.PrintExamples();
                errMessage = "Error Details:\n" + errMessage;
                this.Error(errMessage);
            }

            /// <summary>
            /// Displays an error message.
            /// </summary>
            /// <param name="errMessage">The error message to display.</param>
            public void Error(string errMessage)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine(errMessage);
                Console.ResetColor();
            }
        }
    }

    namespace Network
    {
        /// <summary>
        /// Represents a generic object that can be serialized and sent over the network.
        /// </summary>
        class SendableObject
        {
            public string? email { get; set; }

            /// <summary>
            /// Serializes the current object to a JSON string.
            /// </summary>
            /// <typeparam name="T">The type of the object being serialized.</typeparam>
            /// <returns>The serialized JSON string.</returns>
            public string Serialize<T>() where T : SendableObject
            {
                try
                {
                    return JsonSerializer.Serialize(this as T, new JsonSerializerOptions { WriteIndented = true });
                }
                catch
                {
                    throw new FailedToSerialize();
                }
            }


            /// <summary>
            /// Deserializes a JSON string into an object of type T.
            /// </summary>
            /// <typeparam name="T">The type of the object to deserialize into.</typeparam>
            /// <param name="data">The JSON string to deserialize.</param>
            /// <returns>The deserialized object of type T.</returns>
            public static T Deserialize<T>(string data) where T : SendableObject
            {
                return JsonSerializer.Deserialize<T>(data) as T ?? throw new FailedToDeserialize();
            }
        }

        /// <summary>
        /// Represents a message that can be sent over the network.
        /// </summary>
        class Message : SendableObject
        {
            public required string content { get; set; }

            /// <summary>
            /// Gets the content of the message as a byte array.
            /// </summary>
            /// <returns>The content of the message as a byte array.</returns>
            public byte[] getContentAsBytes()
            {
                return Convert.FromBase64String(this.content);
            }
        }

        /// <summary>
        /// Represents a client that interacts with a server.
        /// </summary>
        class Client
        {
            public string Address;
            private HttpClient HttpClient;

            /// <summary>
            /// Initializes a new instance of the Client class.
            /// </summary>
            /// <param name="serverAddress">The address of the server to connect to.</param>
            public Client(string serverAddress)
            {
                this.Address = $"http://{serverAddress}:5050/";
                this.HttpClient = new HttpClient();
            }

            /// <summary>
            /// Creates a new message with the specified email and content.
            /// </summary>
            /// <param name="email">The email associated with the message.</param>
            /// <param name="content">The content of the message.</param>
            /// <returns>A new Message object.</returns>
            public Message CreateMessage(string email, string content)
            {
                return new Message
                {
                    email = email,
                    content = content,
                };
            }

            /// <summary>
            /// Sends a GET request to the specified endpoint with the provided email and retrieves the response as an object of type T.
            /// </summary>
            /// <typeparam name="T">The type of the object to retrieve.</typeparam>
            /// <param name="endpoint">The endpoint to send the request to.</param>
            /// <param name="email">The email to include in the request.</param>
            /// <returns>The retrieved object of type T.</returns>
            public async Task<T> Get<T>(string endpoint, string email) where T : SendableObject
            {
                try
                {
                    var response = await this.HttpClient.GetAsync($"{this.Address}{endpoint}/{email}");

                    if (!response.StatusCode.IsOkay())
                    {
                        throw new Exception();
                    }

                    string data = await response.Content.ReadAsStringAsync();
                    T obj = JsonSerializer.Deserialize<T>(data) ?? throw new FailedToDeserialize();

                    if (obj.email != email)
                    {
                        throw new Exception();
                    }

                    return obj;
                }
                catch
                {
                    throw new FailedToRetrieve(endpoint);
                }

            }

            /// <summary>
            /// Sends a PUT request to the specified endpoint with the provided object.
            /// </summary>
            /// <typeparam name="T">The type of the object to send.</typeparam>
            /// <param name="endpoint">The endpoint to send the request to.</param>
            /// <param name="objectSending">The object to send in the request.</param>
            public async Task Send<T>(string endpoint, T objectSending) where T : SendableObject
            {
                var content = new StringContent(objectSending.Serialize<T>(), Encoding.UTF8, "application/json");
                var response = await this.HttpClient.PutAsync($"{this.Address}{endpoint}/{objectSending.email}", content);
                if (!response.StatusCode.IsOkay())
                {
                    throw new FailedToSend(endpoint);
                }
            }
        }
    }

    public static class Extensions
    {
        /// <summary>
        /// Determines if the HTTP status code represents a successful response (2xx).
        /// </summary>
        /// <param name="code">The HTTP status code.</param>
        /// <returns>True if the status code represents a successful response, false otherwise.</returns>
        public static bool IsOkay(this HttpStatusCode code) => ((int)code).ToString().StartsWith("2");

        /// <summary>
        /// Serializes the current object to a JSON string.
        /// </summary>
        ///  <typeparam name="T">The type of the object being serialized.</typeparam>
        ///  <returns>The serialized JSON string.</returns>
        public static string Serialize(this Object obj)
        {
            try
            {

                return JsonSerializer.Serialize(obj, new JsonSerializerOptions { WriteIndented = true });
            }
            catch
            {
                throw new FailedToSerialize();
            }
        }

        /// <summary>
        /// Converts the length of a byte array to a byte array representation of the length in little-endian order.
        /// </summary>
        /// <param name="bytes">The byte array whose length will be converted to a byte array.</param>
        /// <returns>A byte array containing the length of the input byte array in little-endian order.</returns>
        public static byte[] getLengthBytes(this byte[] bytes)
        {
            int l = bytes.Length;
            return BitConverter.GetBytes(l).Reverse().ToArray();
        }

        /// <summary>
        /// Reads a subset of bytes from a byte array.
        /// </summary>
        /// <param name="bytes">The byte array to read from.</param>
        /// <param name="start">The starting index of the byte array where the length is stored in little-endian order.</param>
        /// <returns>A new byte array containing the subset of bytes read from the original byte array.</returns>
        public static byte[] readBytes(this byte[] bytes, int start)
        {
            var x = BitConverter.ToInt32(bytes.Skip(start).Take(4).Reverse().ToArray());
            return bytes.Skip(start + 4).Take(x).ToArray();
        }

        /// <summary>
        /// Determines if the BigInteger value is even.
        /// </summary>
        /// <param name="value">The BigInteger value.</param>
        /// <returns>True if the value is even, false otherwise.</returns>
        public static bool IsEven(this BigInteger value)
        {
            return (value & 1) == 0;
        }

        /// <summary>
        /// Calculates the modular multiplicative inverse of 'a' modulo 'b'.
        /// </summary>
        /// <param name="a">The value to find the modular multiplicative inverse of.</param>
        /// <param name="b">The modulus.</param>
        /// <returns>The modular multiplicative inverse of 'a' modulo 'b'.</returns>
        public static BigInteger ModInverse(this BigInteger a, BigInteger b)
        {
            BigInteger i = b, v = 0, d = 1;
            while (a > 0)
            {
                BigInteger z = i / a, x = a;
                a = i % x;
                i = x;
                x = d;
                d = v - z * x;
                v = x;
            }
            v %= b;
            if (v < 0) v = (v + b) % b;
            return v;
        }

        /// <summary>
        /// Determines if the given BigInteger value is probably prime using the Miller-Rabin primality test.
        /// </summary>
        /// <param name="value">The BigInteger value to test for primality.</param>
        /// <param name="k">The number of iterations to perform (default is 10).</param>
        /// <returns>True if the value is probably prime, false otherwise.</returns>
        public static Boolean IsProbablyPrime(this BigInteger value, int k = 10)
        {
            // Ensure input 1 is > 3 and odd
            if (value.IsEven() || value <= 1) return false;
            if (value <= 3) return true;

            // Write value as 2^r * d + 1
            BigInteger d = value - 1;
            int r = 0;
            while (d.IsEven)
            {
                d /= 2;
                r += 1;
            }

            RandomNumberGenerator rng = RandomNumberGenerator.Create();
            byte[] bytes = new byte[value.ToByteArray().LongLength];
            BigInteger a;

            // WitnessLoop: repeat k times
            for (int i = 0; i < k; i++)
            {
                // Pick a random number in the range [2, value - 2]
                rng.GetBytes(bytes);
                a = new BigInteger(bytes) % (value - 2) + 2;

                // x <- a^d mod value
                BigInteger x = BigInteger.ModPow(a, d, value);

                if (x == 1 || x == value - 1) continue;

                for (int j = 0; j < r - 1 && x != value - 1; j++)
                {
                    x = BigInteger.ModPow(x, 2, value);
                }

                if (x == value - 1) continue;

                return false;
            }

            return true;
        }

        /// <summary>
        /// Generates a BigInteger using the specified random number generator and byte array.
        /// </summary>
        /// <param name="rng">The random number generator.</param>
        /// <param name="bytes">The byte array to store the generated value.</param>
        /// <returns>The generated BigInteger.</returns>
        public static BigInteger GenerateBigInteger(this RandomNumberGenerator rng, byte[] bytes)
        {
            rng.GetBytes(bytes);
            return new BigInteger(bytes);
        }
    }

    /// <summary>
    /// Provides methods for encryption and decryption using public and private keys.
    /// </summary>
    static class Encryption
    {
        /// <summary>
        /// Encrypts the specified message using the provided public key.
        /// </summary>
        /// <param name="message">The message to encrypt.</param>
        /// <param name="key">The public key to use for encryption.</param>
        /// <returns>The encrypted message as a base64-encoded string.</returns>
        public static string Encrypt(string message, PublicKey key)
        {
            var (E, N) = key.Decode();
            var messageBytes = Encoding.UTF8.GetBytes(message);
            if (messageBytes.Length > N.ToByteArray().Length - 1)
                throw new MessageTooLong();
            var messageInt = new BigInteger(messageBytes);
            var encryptedInt = BigInteger.ModPow(messageInt, E, N);
            return Convert.ToBase64String(encryptedInt.ToByteArray());
        }

        /// <summary>
        /// Decrypts the specified encrypted message using the provided private key.
        /// </summary>
        /// <param name="encryptedMessage">The encrypted message as a base64-encoded string.</param>
        /// <param name="key">The private key to use for decryption.</param>
        /// <returns>The decrypted message.</returns>
        public static string Decrypt(string encryptedMessage, PrivateKey key)
        {
            var (D, N) = key.Decode();
            var encryptedMessageBytes = Convert.FromBase64String(encryptedMessage);
            if (encryptedMessageBytes.Length > N.ToByteArray().Length)
                throw new MessageTooLong();
            var encryptedInt = new BigInteger(encryptedMessageBytes);
            var decryptedInt = BigInteger.ModPow(encryptedInt, D, N);
            var decryptedBytes = decryptedInt.ToByteArray();
            return Encoding.UTF8.GetString(decryptedBytes);
        }
    }

    /// <summary>
    /// Provides methods for generating prime numbers and key pairs.
    /// </summary>
    static class Generator
    {
        static RandomNumberGenerator randomGenerator = RandomNumberGenerator.Create();

        /// <summary>
        /// Generates a prime number with the specified number of bits.
        /// </summary>
        /// <param name="bits">The number of bits for the prime number.</param>
        /// <returns>The generated prime number.</returns>
        public static BigInteger GeneratePrime(int bits)
        {
            BigInteger number;
            byte[] bytes = new byte[bits / 8];
            do
            {
                number = randomGenerator.GenerateBigInteger(bytes);
            } while (!number.IsProbablyPrime());

            return number;
        }

        /// <summary>
        /// Generates a prime number with the specified number of bits using parallel processing.
        /// </summary>
        /// <param name="bits">The number of bits for the prime number.</param>
        /// <param name="threads">The number of threads to use for parallel processing.</param>
        /// <returns>The generated prime number.</returns>
        public static BigInteger GeneratePrimeParallel(int bits, int threads = 15)
        {
            BigInteger number = -1;
            bool isPrimeFound = false;

            Parallel.For(0, threads, (i, state) =>
            {
                byte[] bytes = new byte[bits / 8];
                BigInteger num;

                while (!isPrimeFound)
                {
                    num = randomGenerator.GenerateBigInteger(bytes);

                    if (!isPrimeFound && num.IsProbablyPrime())
                    {
                        number = num;
                        isPrimeFound = true;
                    }
                }
            });

            if (number == -1)
                throw new Exception("Failed to generate prime number.");

            return number;
        }

        /// <summary>
        /// Generates a public-private key pair with the specified number of bits.
        /// </summary>
        /// <param name="bits">The number of bits for the key pair.</param>
        /// <returns>A tuple containing the generated public key and private key.</returns>
        public static (PublicKey, PrivateKey) GenerateKeyPairs(int bits)
        {
            int pLength = bits / 2 + bits / 5;
            int qLength = bits - pLength;
            BigInteger p = GeneratePrimeParallel(pLength);
            BigInteger q = GeneratePrimeParallel(qLength);

            BigInteger N = p * q;
            BigInteger T = (p - 1) * (q - 1);
            BigInteger E = 65537; // 2^16 + 1
            BigInteger D = E.ModInverse(T);

            var publickey = Key.Encode<PublicKey>(E, N);
            var privatekey = Key.Encode<PrivateKey>(D, N);

            return (publickey, privatekey);
        }
    }

    namespace Keys
    {

        /// <summary>
        /// Represents a private key.
        /// </summary>
        class PrivateKey : Key
        {
            public new List<string> email { get; set; }

            /// <summary>
            /// Creates a new PrivateKey instance from the specified Key.
            /// </summary>
            /// <param name="k">The Key to create the PrivateKey from.</param>
            /// <returns>A new PrivateKey instance.</returns>
            public static PrivateKey fromKey(Key k) => new PrivateKey(k.key);

            /// <summary>
            /// Adds an email to the private key.
            /// </summary>
            /// <param name="email">The email to add.</param>
            public void AddEmail(string email) => this.email.Add(email);

            /// <summary>
            /// Initializes a new instance of the PrivateKey class.
            /// </summary>
            public PrivateKey() : base() => this.email = new List<string>();

            /// <summary>
            /// Initializes a new instance of the PrivateKey class with the specified key.
            /// </summary>
            /// <param name="key">The private key.</param>
            public PrivateKey(string key) : base(key) => this.email = new List<string>();

            /// <summary>
            /// Initializes a new instance of the PrivateKey class with the specified email and key.
            /// </summary>
            /// <param name="email">The email associated with the private key.</param>
            /// <param name="key">The private key.</param>
            public PrivateKey(string email, string key) : base(key)
            {
                this.email = new List<string>();
                this.email.Add(email);
            }

            /// <summary>
            /// Saves the private key to a file.
            /// </summary>
            public void Save() => base.Save<PrivateKey>("private");
        }

        /// <summary>
        /// Represents a public key.
        /// </summary>
        class PublicKey : Key
        {

            /// <summary>
            /// Creates a new PublicKey instance from the specified Key and email.
            /// </summary>
            /// <param name="k">The Key to create the PublicKey from.</param>
            /// <param name="email">The email associated with the public key.</param>
            /// <returns>A new PublicKey instance.</returns>
            public static PublicKey fromKey(Key k, string email) => new PublicKey(email, k.key);

            /// <summary>
            /// Initializes a new instance of the PublicKey class.
            /// </summary>
            public PublicKey() : base() => this.email = "";

            /// <summary>
            /// Initializes a new instance of the PublicKey class with the specified email and data.
            /// </summary>
            /// <param name="email">The email associated with the public key.</param>
            /// <param name="data">The public key data.</param>
            public PublicKey(string email, string data) : base(data) => this.email = email;

            /// <summary>
            /// Saves the public key to a file.
            /// </summary>
            public void Save() => base.Save<PublicKey>(this.email ?? "public");
        }

        /// <summary>
        /// Represents a generic key.
        /// </summary>
        class Key : SendableObject
        {
            public string key { get; set; }

            /// <summary>
            /// Initializes a new instance of the Key class.
            /// </summary>
            public Key()
            {
                key = "";
            }

            /// <summary>
            /// Initializes a new instance of the Key class with the specified key.
            /// </summary>
            /// <param name="key">The key.</param>
            public Key(string key)
            {
                this.key = key;
            }

            /// <summary>
            /// Saves the key to a file.
            /// </summary>
            /// <typeparam name="T">The type of the key.</typeparam>
            /// <param name="email">The email associated with the key.</param>
            public void Save<T>(string email) where T : Key
            {
                string fileName = $"{email}.key";
                // Force serialize to proper order
                string data;

                // Serialize the key
                if (typeof(T) == typeof(PrivateKey))
                {
                    data = ((T)this).Serialize<T>();
                }
                else
                {

                    data = new { ((T)this).email, key }.Serialize();
                }

                // Save the key to a file
                try
                {
                    File.WriteAllText(fileName, data);
                }
                catch
                {
                    throw new FailedToSave(fileName);
                }
            }

            /// <summary>
            /// Loads a key from a file.
            /// </summary>
            /// <typeparam name="T">The type of the key.</typeparam>
            /// <param name="id">The identifier of the key.</param>
            /// <returns>The loaded key.</returns>
            public static T Load<T>(string id) where T : Key
            {
                string fileName = $"{id}.key";
                if (!File.Exists(fileName))
                    throw new KeyNotFound(id);
                string data = File.ReadAllText(fileName);
                return (T)Key.Deserialize<T>(data);
            }

            /// <summary>
            /// Decodes the key into its components.
            /// </summary>
            /// <returns>A tuple containing the decoded key components.</returns>
            public (BigInteger, BigInteger) Decode()
            {
                string deserializedBase64Data = this.key;

                byte[] decodedData = Convert.FromBase64String(this.key);
                byte[] NBytes, EBytes;

                EBytes = decodedData.readBytes(0);
                NBytes = decodedData.readBytes(EBytes.Length + 4);

                var E = new BigInteger(EBytes);
                var N = new BigInteger(NBytes);

                return (E, N);
            }

            /// <summary>
            /// Encodes the specified values into a key.
            /// </summary>
            /// <typeparam name="T">The type of the key.</typeparam>
            /// <param name="E">The first component of the key.</param>
            /// <param name="N">The second component of the key.</param>
            /// <returns>The encoded key.</returns>
            public static T Encode<T>(BigInteger E, BigInteger N) where T : Key, new()
            {
                byte[] EBytes = E.ToByteArray(), NBytes = N.ToByteArray();
                byte[] data = new byte[EBytes.Length + NBytes.Length + 8];

                byte[] eBytes = EBytes.getLengthBytes();
                byte[] nBytes = NBytes.getLengthBytes();

                Array.Copy(eBytes, 0, data, 0, 4);
                Array.Copy(EBytes, 0, data, 4, EBytes.Length);
                Array.Copy(nBytes, 0, data, 4 + EBytes.Length, 4);
                Array.Copy(NBytes, 0, data, 8 + EBytes.Length, NBytes.Length);

                return new T()
                {
                    key = Convert.ToBase64String(data),
                };
            }
        }
    }

    static class Program
    {
        static readonly string ServerAddress = "voyager.cs.rit.edu";
        private static Client Client = new Network.Client(ServerAddress);

        /// <summary>
        /// Dictionary containing the command definitions and their required arguments.
        /// </summary>
        static readonly Dictionary<string, string[]> CommandDefinitions = new Dictionary<string, string[]>
        {
            { "keyGen", new string[] { "keySize" } },
            { "sendKey", new string[] { "email" } },
            { "getKey", new string[] { "email" } },
            { "sendMsg", new string[] { "email", "content" } },
            { "getMsg", new string[] { "email" } },
        };

        /// <summary>
        /// Runs the specified command with the provided arguments.
        /// </summary>
        /// <param name="command">The command to run.</param>
        /// <param name="args">The arguments for the command.</param>
        static void RunCommand(string command, string[] args)
        {
            var def = CommandDefinitions[command];
            var argsLen = def.Length;

            // Validate the number of arguments
            if (args.Length < argsLen) throw new TooLittleArgs(def, argsLen, args.Length);
            if (args.Length > argsLen) throw new TooManyArgs(argsLen, args.Length);

            string email;
            PublicKey publicKey;
            PrivateKey privateKey;
            string encryptedMessage;

            // Run the command
            switch (command)
            {
                case "keyGen":
                    int size = 0;
                    try
                    {
                        // Validate parsing the key size
                        size = int.Parse(args[0]);
                    }
                    catch
                    {
                        throw new NotParseableToInt(args[0]);
                    }
                    finally
                    {
                        // Validate the key size
                        if (size < 32 && size % 8 != 0) throw new InvalidBits(32, size);
                        if (size < 32) throw new TooLittleBits(32, size);
                        if (size % 8 != 0) throw new NotMultipleOf8(size);

                        // Generate the Key Pairs
                        (publicKey, privateKey) = Generator.GenerateKeyPairs(size);

                        // Save the generated keys
                        publicKey.Save<PublicKey>("public");
                        privateKey.Save<PrivateKey>("private");
                    }
                    break;

                case "sendKey":
                    email = args[0];
                    publicKey = Key.Load<PublicKey>("public");
                    privateKey = Key.Load<PrivateKey>("private");

                    publicKey.email = email;
                    Client.Send<PublicKey>("Key", publicKey).Wait();

                    privateKey.AddEmail(email);
                    privateKey.Save();

                    Console.WriteLine("Key saved");
                    break;

                case "getKey":
                    email = args[0];

                    // Get the key from the server
                    Client.Get<PublicKey>("Key", email).Result
                      .Save() // Save the key
                      ;
                    break;

                case "sendMsg":
                    email = args[0];
                    string content = args[1];

                    // Load the saved public key
                    publicKey = Key.Load<PublicKey>(email);

                    // Encrypt the message
                    encryptedMessage = Encryption.Encrypt(content, publicKey);

                    // Send the encrypted message to the server
                    Message message = Client.CreateMessage(email, encryptedMessage);
                    Client.Send<Message>("Message", message).Wait();

                    Console.WriteLine("Message written"); break;

                case "getMsg":
                    email = args[0];

                    // Load the saved private key
                    privateKey = Key.Load<PrivateKey>("private");

                    // Validate the private key
                    if (privateKey.email.IndexOf(email) == -1)
                        throw new PrivateKeyNotFound(email);

                    // Retrieve the encrypted message from the server
                    encryptedMessage = Client.Get<Message>("Message", email).Result.content;

                    // Decrypt the message
                    string decryptedMessage = Encryption.Decrypt(encryptedMessage, privateKey);

                    Console.WriteLine(decryptedMessage); break;

                default:
                    throw new InvalidOption(command, CommandDefinitions.Keys.ToArray());
            }

        }

        /// <summary>
        /// The main entry point of the program.
        /// </summary>
        /// <param name="args">The command-line arguments.</param>
        static void Main(string[] args)
        {
            // Variable Declarations
            string[] validOptions = { "keyGen", "sendKey", "getKey", "sendMsg", "getMsg" };

            // Create a new Helper object
            ProgramHelp.Helper HelpWriter = new ProgramHelp.Helper();

            HelpWriter.helpMessage =
            "Usage: <command> <command args>\n" +
            "Commands:\n" +
            "  keyGen  <keySize>  Generate a new key pair with the specified size\n" +
            "  sendKey <email>  Send the specified key to the specified email\n" +
            "  getKey  <email>  Retrieve the key associated with the specified email\n" +
            "  sendMsg <email> <content>  Send the specified message to the specified email\n" +
            "  getMsg  <email>  Retrieve the message associated with the specified email\n";

            HelpWriter.helpExamples =
            "Examples:\n" +
            "  keyGen 1024\n" +
            "  sendKey test@cs.rit.edu\n" +
            "  sendMsg test@cs.rit.edu \"It worked!\"\t";

            try
            {
                // Validate the number of arguments
                if (args.Length == 0) throw new TooLittleArgs(validOptions, 1, 0);

                // Validate the option
                string command = args[0];
                if (Array.IndexOf(validOptions, command) == -1) throw new InvalidOption(command, validOptions);

                // Run the number generation process
                RunCommand(command, args.Skip(1).ToArray());
            }
            catch (Error e)
            {
                HelpWriter.Error(e.Message);
            }
            catch (TooLittleArgs e)
            {
                if (args.Length == 0)
                {
                    HelpWriter.PrintHelp();
                }
                else
                {
                    HelpWriter.ErrorHelp(e.Message);
                }
            }
            catch (Exception e)
            {
                HelpWriter.ErrorHelp(e.Message);
            }
        }
    }
}
