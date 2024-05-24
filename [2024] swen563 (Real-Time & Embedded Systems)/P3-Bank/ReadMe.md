# P3-Bank

## Description
This project is a FREERTOS based bank real-time simulation. The project simualtes a bank with 3 tellers and infinitely queued customers that come in at random intervals. The customers are served by the tellers in a first come first serve basis. This project was made for multi-function shield attachment board and uses its LCD and buttons to display the customer queue and control the tellers.
- This project aims to use a mutlithreaded Real Time Operating System to simulate the workflow of a typical banking environment.
- I used FreeRtos and its Task, Queue, and Semaphore handler to run the project using mutiple threads. Queue for a thread safe system for customer generation, tasks for spawning the new thread, and semaphores to create mutexes allowing for safe modification operations to things like printing to USART or modifying the global statistics struct.

![Neovide-Neovide-Capture-010934.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/6dc985a3-ee71-4359-becb-2aa4a2af5847/a38c8579-0c43-458d-b0bf-445245c8fcb0/Neovide-Neovide-Capture-010934.png)