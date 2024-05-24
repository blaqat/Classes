using System;
using System.IO;
using System.Numerics;
using System.Threading;
using System.Diagnostics;
using System.Collections.Concurrent;
using System.Security.Cryptography;
using Project2.ProgramHelp.Exceptions.Args;
using Project2.ProgramHelp.Exceptions.Bits;
using Project2.ProgramHelp.Exceptions.Option;
using Project2.ProgramHelp.Exceptions.Count;

namespace Project2
{
    namespace ProgramHelp
    {
        namespace Exceptions
        {
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
                    public InvalidOption(string opt) : base($"Invalid option (\"{opt}\"): Option must be 'odd' or 'prime'") { }
                }
            }

            namespace Count
            {
                /// <summary>
                /// Exception thrown when an invalid non positive count is provided.
                /// </summary>
                class InvalidCount : Exception
                {
                    public InvalidCount(int cnt) : base($"Invalid count ({cnt}): Must be a positive integer") { }
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
            public void Error(string errMessage)
            {
                this.PrintHelp();
                this.PrintExamples();

                Console.ForegroundColor = ConsoleColor.Red;

                errMessage =
                    "Error Details:\n" +
                    $"{errMessage}\n";

                Console.WriteLine(errMessage);
                Console.ResetColor();
            }
        }
    }

    /// <summary>
    /// Represents the result of a number generation operation.
    /// </summary>
    class Result
    {
        public required string type;
        public BigInteger number;
        public int factorsCount;

        /// <summary>
        /// Returns a string representation of the result.
        /// </summary>
        /// <returns>A string containing the generated number and additional information based on the type.</returns>
        public override string ToString()
        {
            string result = $"{number}";
            if (type == "odd") result += $"\n   Number of factors: {factorsCount}";
            return result;
        }
    }

    static class Program
    {
        /// <summary>
        /// Checks if the given BigInteger value is even.
        /// </summary>
        /// <param name="value">The BigInteger value to check.</param>
        /// <returns>True if the value is even, false otherwise.</returns>
        public static Boolean IsEven(this BigInteger value)
        {
            return (value & 1) == 0;
        }

        /// <summary>
        /// Checks if the given BigInteger value is odd.
        /// </summary>
        /// <param name="value">The BigInteger value to check.</param>
        /// <returns>True if the value is odd, false otherwise.</returns>
        public static Boolean IsOdd(this BigInteger value)
        {
            return (value & 1) == 1;
        }

        /// <summary>
        /// Counts the number of factors for the given BigInteger value.
        /// </summary>
        /// <param name="value">The BigInteger value to count factors for.</param>
        /// <returns>The number of factors of the given value.</returns>
        public static int CountFactors(this BigInteger value)
        {
            // Handle edge case where value is 1
            if (value == 1) return 1;

            int factors = 1;
            int power = 0;
            BigInteger remainder = value;

            // Iterate through odd numbers for further factorization
            for (BigInteger i = 3; i * i <= remainder; i += 2)
            {
                power = 0;
                while (remainder % i == 0)
                {
                    remainder /= i;
                    power++;
                }
                factors *= (power + 1);

                // Check if the new remainder is prime
                if (remainder > 1 && remainder.IsProbablyPrime(1))
                {
                    return factors * 2;
                }
            }

            // If there's any remainder left, it's a prime factor
            if (remainder > 1) factors *= 2;

            return factors;
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
            while (d.IsEven())
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
        /// Generates a prime number with the specified number of bits and stores it in the given Result object.
        /// </summary>
        /// <param name="result">The Result object to store the generated prime number.</param>
        /// <param name="bits">The number of bits for the prime number.</param>
        public static void GeneratePrime(Result result, int bits)
        {
            BigInteger number;
            byte[] bytes = new byte[bits / 8];
            RandomNumberGenerator rng = RandomNumberGenerator.Create();

            // Generate random number until it is prime
            do
            {
                rng.GetBytes(bytes);
                number = new BigInteger(bytes);
            } while (!number.IsProbablyPrime());

            result.number = number;
        }

        /// <summary>
        /// Generates an odd number with the specified number of bits and stores it in the given Result object.
        /// </summary>
        /// <param name="result">The Result object to store the generated odd number and its factors count.</param>
        /// <param name="bits">The number of bits for the odd number.</param>
        public static void GenerateOdd(Result result, int bits)
        {
            BigInteger number;
            byte[] bytes = new byte[bits / 8];
            RandomNumberGenerator rng = RandomNumberGenerator.Create();

            rng.GetBytes(bytes);
            number = new BigInteger(bytes);
            if (number <= 0) number *= -1;
            if (number.IsEven()) number += 1;

            /* do */
            /* { */
            /*     rng.GetBytes(bytes); */
            /*     number = new BigInteger(bytes); */
            /* } while (number <= 0 || number.IsEven()); */

            result.number = number;
            result.factorsCount = number.CountFactors();
        }

        /// <summary>
        /// Runs the number generation process with the specified parameters.
        /// </summary>
        /// <param name="bits">The number of bits for the generated numbers.</param>
        /// <param name="option">The type of numbers to generate ("odd" or "prime").</param>
        /// <param name="numGenerations">The number of numbers to generate.</param>
        static void Run(int bits, string option, int numGenerations)
        {
            Stopwatch timer = new Stopwatch();
            timer.Start();

            // Generate random numbers
            object countHolder = new object();
            int count = 0;

            // Multiply by 10 to give more options for the generated numbers to finish quickly
            Parallel.For(0, numGenerations * 10, i =>
            {
                Result result = new Result { type = option };

                if (option == "odd") GenerateOdd(result, bits);
                else if (option == "prime") GeneratePrime(result, bits);

                lock (countHolder)
                {
                    count++;
                    Console.WriteLine($"{count}: {result}");
                    if (count == numGenerations)
                    {

                        timer.Stop();

                        // Calculate time taken
                        double elapsed = timer.Elapsed.TotalMicroseconds / 1000000.0;
                        string elapsedString = String.Format("{00:00:00.0000000}", elapsed);
                        string timerResults = $"Time to Generate: {elapsedString}\n";


                        // Display time taken
                        Console.WriteLine(timerResults);

                        // Exit the program
                        Environment.Exit(0);
                    }
                }
            });
        }

        static void Main(string[] args)
        {
            // Variable Declarations
            const int minArgsNum = 2;
            const int maxArgsNum = 3;
            const int minBits = 32;
            string[] validOptions = { "odd", "prime" };
            string[] validArgs = { "bits", "option", "count" };

            // Create a new Helper object
            ProgramHelp.Helper HelpWriter = new ProgramHelp.Helper();

            HelpWriter.helpMessage =
            "Usage: numgen <bits> <option> <count>\n" +
            "Generate random numbers of the specified bits and option\n" +
            "Arguments:\n" +
            $"  <bits>   Number of bits in the random number, this must be a multiple of 8, and at least {minBits} bits\n" +
            "  <option> The type of numbers to be generated. 'odd' or 'prime'\n" +
            "  <count>  Positive count of numbers to generate, defaults to 1\n";

            HelpWriter.helpExamples =
            "Examples:\n" +
            $"  numgen {minBits} odd 5\n" +
            "  numgen 64 prime 3\n";

            try
            {
                // Validate the number of arguments
                if (args.Length < minArgsNum) throw new TooLittleArgs(validArgs, minArgsNum, args.Length);
                if (args.Length > maxArgsNum) throw new TooManyArgs(maxArgsNum, args.Length);

                // Validate the number of bits
                int bits = int.Parse(args[0]);
                if (bits < minBits && bits % 8 != 0) throw new InvalidBits(minBits, bits);
                if (bits < minBits) throw new TooLittleBits(minBits, bits);
                if (bits % 8 != 0) throw new NotMultipleOf8(bits);

                // Validate the option
                string option = args[1];
                if (Array.IndexOf(validOptions, option) == -1) throw new InvalidOption(option);

                // Validate the count
                int count = 1;
                if (args.Length > 2)
                {
                    count = int.Parse(args[2]);
                    if (count <= 0) throw new InvalidCount(count);
                }

                // Run the number generation process
                Run(bits, option, count);
            }
            catch (TooLittleArgs e)
            {
                if (args.Length == 0)
                {
                    HelpWriter.PrintHelp();
                }
                else
                {
                    HelpWriter.Error(e.Message);
                }
            }
            catch (Exception e)
            {
                HelpWriter.Error(e.Message);
            }
        }
    }

}
