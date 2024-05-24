// ---------------------------------------------------------------------------
// Project:   Project 1 - Disk Usage Tool
// Purpose:   A simplified version of disk usage command tool (du) that calculates the disk usage of a directory in parallel or sequential mode.
// Author:    Aiden Green
// Date:      02/21/2024
// ---------------------------------------------------------------------------


using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;

namespace Project1
{
    /// <summary>
    /// A class to store the results of the disk usage analysis.
    /// Includes the number of folders, files, and bytes in the directory.
    /// </summary>
    struct Results
    {
        // Fields
        public int FolderCount;
        public int FileCount;
        public long ByteCount;
        public int ImageFileCount;
        public long ImageByteCount;
        public int Ignored;

        /// <summary>
        /// Default constructor setting all counts to 0
        /// </summary>
        public Results()
        {
            this.FolderCount = 0;
            this.FileCount = 0;
            this.ByteCount = 0;
            this.ImageFileCount = 0;
            this.ImageByteCount = 0;
            this.Ignored = 0;
        }

        /// <summary>
        /// Constructor setting all counts to the given values
        /// </summary>
        /// <param name="folderCount">The number of folders in the directory.</param>
        /// <param name="fileCount">The number of files in the directory.</param>
        /// <param name="byteCount">The number of bytes in the directory.</param>
        /// <param name="imageFileCount">The number of image files in the directory.</param>
        /// <param name="imageByteCount">The number of bytes in the image files in the directory.</param>
        public Results(int folderCount, int fileCount, long byteCount, int imageFileCount, long imageByteCount)
        {
            this.FolderCount = folderCount;
            this.FileCount = fileCount;
            this.ByteCount = byteCount;
            this.ImageFileCount = imageFileCount;
            this.ImageByteCount = imageByteCount;
            this.Ignored = 0;
        }

        // Overloaded Operators
        /// <summary>
        /// Overloads the + operator to combine two Results objects.
        /// </summary>
        /// <param name="a">First operand.</param>
        /// <param name="b">Second operand.</param>
        /// <returns>A new Results object containing summed values of both operands.</returns>
        public static Results operator +(Results a, Results b)
        {
            return new Results(
              a.FolderCount + b.FolderCount,
              a.FileCount + b.FileCount,
              a.ByteCount + b.ByteCount,
              a.ImageFileCount + b.ImageFileCount,
              a.ImageByteCount + b.ImageByteCount
            );
        }

        /// <summary>
        /// Overrides the ToString method to provide a formatted string representation of the Results.
        /// </summary>
        /// <returns>A formatted string describing the contents of the Results object.</returns>
        public override string ToString()
        {
            string bytes = String.Format("{0:N0}", ByteCount);
            string imageBytes = String.Format("{0:N0}", ImageByteCount);
            return
                $"{FolderCount} folders, {FileCount} files, {bytes} bytes\n" +
                (ImageFileCount == 0 ? "no image files found in directory\n" :
                 $"{ImageFileCount} image files: {imageBytes} bytes\n");
            /* + (Ignored == 0 ? "" : $"{Ignored} directories could not be accessed\n"); */
        }

    }

    /// <summary>
    /// Main class for the program
    /// </summary>
    class Program
    {


        static readonly HashSet<string> imageExtensions = new HashSet<string> { ".jpg", ".png", ".gif", ".jpeg", ".bmp", ".webp" };

        /// <summary>
        /// Represents an error when an unrecognized argument is provided.
        /// </summary>
        class WrongArgument : System.Exception
        {
            public string[] Args;
            public int WrongArgumentIndex;
            public WrongArgument(string[] args, int wrongArgumentIndex) : base($"Invalid option \"{args[wrongArgumentIndex]}\"")
            {
                this.Args = args;
                this.WrongArgumentIndex = wrongArgumentIndex;
            }
        }

        /// <summary>
        /// Represents an error when no argument is provided where one is expected.
        /// </summary>
        class NoArgument : System.Exception
        {
            public NoArgument() : base("No argument provided") { }
        }

        /// <summary>
        /// Computes the folder usage in sequential mode.
        /// </summary>
        /// <param name="path">The path of the folder to analyze.</param>
        /// <param name="results">A reference to the Results object to store the usage data.</param>
        /// <returns>A Results object containing the folder usage data.</returns>
        static Results GetFolderUsage(string path)
        {

            // Initalize local results object and mutex lock for thread safety
            Results results = new Results();
            int ignored = 0;

            // Process each entry in the given path
            foreach (string entry in Directory.EnumerateFileSystemEntries(path))
            {
                try
                {
                    // Check if entry is a file and increment file count and byte count
                    FileInfo file = new FileInfo(entry);
                    if (file.Exists)
                    {
                        long bytes = file.Length;
                        string ext = file.Extension;
                        bool isImage = imageExtensions.Contains(ext);

                        // Create a new results object for the file
                        results.FileCount++;
                        results.ByteCount += bytes;
                        if (isImage)
                        {
                            results.ImageFileCount++;
                            results.ImageByteCount += bytes;
                        }

                        continue;
                    }

                    // Recursively call GetFolderUsage to analyze subfolders contents and add results + 1 folder to the results
                    DirectoryInfo dir = new DirectoryInfo(entry);
                    if (dir.Exists)
                    {
                        Results r = GetFolderUsage(entry);
                        r.FolderCount++;
                        results += r;
                    }
                }
                catch (Exception)
                {
                    ignored++;
                }
            }

            // Add the ignored directors count to the results
            results.Ignored += ignored;
            return results;

        }

        static Results GetFolderUsageParallel2(string path)
        {
            var imageExtensions = new HashSet<string> { ".jpg", ".png", ".gif", ".jpeg", ".bmp", ".webp" };
            var finalResult = new Results();
            object lockObject = new object(); // Locking object for thread safety

            Parallel.ForEach(Directory.EnumerateFileSystemEntries(path), new ParallelOptions(),
                () => new Results(), // Local initial state
                (entry, loopState, localResult) => // Body
                {
                    try
                    {
                        if (File.Exists(entry)) // Check if entry is a file
                        {
                            var file = new FileInfo(entry);
                            localResult.FileCount++;
                            localResult.ByteCount += file.Length;
                            if (imageExtensions.Contains(file.Extension.ToLower()))
                            {
                                localResult.ImageFileCount++;
                                localResult.ImageByteCount += file.Length;
                            }
                        }
                        else if (Directory.Exists(entry)) // Check if entry is a directory
                        {
                            var subFolderResult = GetFolderUsageParallel2(entry);
                            subFolderResult.FolderCount++;
                            localResult += subFolderResult;
                        }
                    }
                    catch (UnauthorizedAccessException)
                    {
                        localResult.Ignored++;
                    }
                    return localResult;
                },
                localResult => // Final action
                {
                    lock (lockObject) // Lock on a separate object
                    {
                        finalResult += localResult;
                    }
                });

            return finalResult;
        }


        /// <summary>
        /// Computes the folder usage in parallel.
        /// </summary>
        /// <param name="path">The path of the folder to analyze.</param>
        /// <param name="results">A reference to the Results object to store the usage data.</param>
        /// <returns>A Results object containing the folder usage data.</returns>
        static Results GetFolderUsageParallel(string path)
        {

            // Initalize local results object and mutex lock for thread safety
            ConcurrentBag<Results> results = new ConcurrentBag<Results>();
            int ignored = 0;

            // Process each file in the given path in parallel
            Parallel.ForEach(Directory.EnumerateFileSystemEntries(path), (entry) =>
            {
                try
                {
                    // Check if entry is a file and increment file count and byte count
                    FileInfo file = new FileInfo(entry);
                    if (file.Exists)
                    {
                        long bytes = file.Length;
                        string ext = file.Extension;
                        bool isImage = imageExtensions.Contains(ext);

                        // Create a new results object for the file
                        Results fileResult = new Results(0, fileCount: 1, byteCount: bytes, 0, 0);

                        // Check if file is an image and increment image file count and byte count
                        if (isImage)
                        {
                            fileResult.ImageFileCount = 1;
                            fileResult.ImageByteCount = bytes;
                        }

                        // Add the file results to the results bag
                        results.Add(fileResult);
                        return;
                    }

                    // Recursively call GetFolderUsageParallel to analyze subfolders contents and add results + 1 folder to the result bag
                    DirectoryInfo dir = new DirectoryInfo(entry);
                    if (dir.Exists)
                    {
                        Results r = GetFolderUsageParallel(entry);
                        r.FolderCount++;
                        results.Add(r);
                    }
                }
                catch (Exception)
                {
                    ignored++;
                }
            });


            // Accumulate/Fold the results into a single results object
            Results finalResult = results.Aggregate(new Results(), (acc, r) => acc + r);

            // Add the ignored directories count to the results
            finalResult.Ignored += ignored;

            return finalResult;
        }


        /// <summary>
        /// Displays the help message for the program.
        /// </summary>
        /// <param name="error">The exception that caused the help message to be displayed.</param>
        static void Help(Exception error)
        {
            // Console.WriteLine($"Error: {error.Message}\n");

            // Default help message
            string helpMessage = "Usage: du [-s] [-d] [-b] <path>\n" +
              "Summarize disk usage of the set of FILES, recursively for directories.\n" +
              "You MUST specify one of the parameters, -s, -d, or -b\n" +
              "-s Run in single threaded mode\n" +
              "-d Run in parallel mode (uses all available processors) -b Run in both parallel and single threaded mode.\n" +
              "Runs parallel followed by sequential mode\n";

            Console.WriteLine(helpMessage);

            // Message that provides more information about the error
            string hintMessage = "Hint:\n" +
              "- You MUST specify one of the parameters, -s, -d, or -b\n" +
              "- <path> is a required argument representing the directory to analyze\n\n" +
              "Example:\n" +
              "dotnet run -- -d ~/Dropbox/Teaching\n\n" +
              "\u001b[31mError Details:\n";

            switch (error)
            {
                case NoArgument _:
                    hintMessage += "You must specify an option and a directory path\n";
                    break;

                // Display the invalid argument provided visually (Inspired by rust error messages)
                case WrongArgument e:
                    int index = e.WrongArgumentIndex;
                    if (index + 1 >= e.Args.Length) index -= 1;
                    string secondArg = e.Args[index + 1].Length > 3 ? e.Args[index + 1].Substring(0, 3) : e.Args[index + 1];
                    hintMessage +=
                      $"- {e.Args[index]}  ───┐\n" +
                      $"         ├─ {e.Message}\n" +
                      $"{secondArg}... ──┘\n";
                    break;

                default:
                    hintMessage += $"{error.Message}\n";
                    break;
            }

            Console.WriteLine(hintMessage);
            Console.Write("\u001b[0m"); // Reset terminal color
        }

        /// <summary>
        ///  Runs the program in either parallel or sequential mode.
        ///  </summary>
        ///  <param name="path">The path of the folder to analyze.</param>
        ///  <param name="parallel">A boolean value indicating whether to run in parallel mode.</param>
        static void Run(string path, bool parallel)
        {

            // Initialize results and timer
            Results results;
            Stopwatch timer = new Stopwatch();

            timer.Start();

            // Get disk usage
            if (parallel)
                results = GetFolderUsageParallel(path);
            else
                results = GetFolderUsage(path);


            timer.Stop();

            // Calculate time taken
            double elapsed = timer.Elapsed.TotalMicroseconds / 1000000.0;
            string elapsedString = String.Format("{0:0.0000000}", elapsed);
            string timerResults = $"{(parallel ? "Parallel" : "Sequential")} Calculated in: {elapsedString}s\n";

            // Display results
            Console.WriteLine(timerResults + results);
        }

        /// <summary>
        ///  Main entry point for the program.
        ///  </summary>
        ///  <param name="args">The command line arguments.</param>
        ///  <exception cref="NoArgument">Thrown when no argument is provided.</exception>
        ///  <exception cref="WrongArgument">Thrown when an invalid argument is provided.</exception>
        static void Main(string[] args)
        {
            /* Validate Command Arguments:
             * -s Run in single threaded mode
             * -d Run in parallel mode (use all avaiable processors)
             * -b Run in both parallel and single threaded mode
             *    Runs parallel followed by squential mode
             */
            string[] validArgs = new string[] { "-s", "-d", "-b" };
            try
            {
                // Check for valid amount of arguments
                if (args.Length < 2) throw new NoArgument();
                if (args.Length > 2) throw new WrongArgument(args, 2);


                string option = args[0];
                string path = args[1];

                // Validate argument options and execute the/ chosen option
                if (validArgs.Contains(option))
                {
                    Console.WriteLine($"Directory “{path}”\n");
                    if (option == "-b")
                    {
                        Run(path, true);
                        Run(path, false);
                    }
                    else
                    {
                        Run(path, option == "-d");
                    }
                }
                else
                {
                    throw new WrongArgument(args, 0);
                }
            }
            catch (Exception e) { Help(e); }
        }
    }
}

