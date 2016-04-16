# TCF Third-part system (.Net)

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016

  
Third-part systems are implemented in this case study as a .Net service using the REST paradigm (_i.e._, exposing resources instead of procedures), implemented using the C# language.

To simplify the implementation and deployment, considering that this course is dedicated to software architecture and not to C# programming, we rely on a self-hosted server and the `Mono` implementation of the .Net framework. 

It gives us portability and a light-weighted way to deploy a third-part system. This is clearly not intended as is for production.

The provided service defines a _Payment_ service, one can use it to process credit card requests.

## Code architecture

The code is kept as simple as possible, and consists in only four files:

  * `BusinessObjects.cs`: The different data structure to be used to support the payment service (_i.e._, `PaymentRequest`, `Payment` and `PaymentStatus`);
  * `IPaymentService.cs`: the interface that models the resources exposed by the service:
    * a `mailbox` to receive `PaymentRequest`s;
    * a list of all `payments` performed on the system;
    * a way to access to a dedicated payment thanks to its identifier: `payments/{id}`. 
  * `PaymentService.cs`: the concrete class that implement the previously described interface;
  * `Server.cs`: this main class starts an HTTP server and binds the implemented service to it.
    
## Running the client

To compile the client, you need to use a version of Mono that bundles the _Windows Communication Foundations_ (WCF) framework. Recent versions of mono include it natively. To compile all the C# source code with the WCF package available and create a `server.exe` binary, simply run the following command:

    azrael:dotNet mosser$ mcs src/*.cs -pkg:wcf -out:server.exe  
     
     
Then, one can start the `server.exe` using the mono runtime environment:

    azrael:dotNet mosser$ mono server.exe
    Starting a WCF self-hosted .Net server... done!
    
    Listening to localhost:9090
    
    Hit Return to shutdown the server.  
    
    
    