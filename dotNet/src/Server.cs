using System;
using System.ServiceModel;
using System.ServiceModel.Web;

using Partner.Service;

/**
 * References:
 * http://badger.developpez.com/tutoriels/dotnet/web-service-rest-avec-wcf-3-5/#LII-B
 **/
public class Server
{

  // mono server.exe               ==> localhost:9090
  // mono server.exe HOSTNAME      ==> HOSTNAME:9090
  // mono server.exe HOSTNAME PORT ==> HOSTNAME:PORT

  public static void Main(string[] args)
  {
    Console.Write("Starting a WCF self-hosted .Net server... ");
    string hostname = "localhost";
    string port = "9090";
    if (args.Length >= 1)  { hostname = args[0]; }
    if (args.Length == 2) { port = args[1]; }
    // Decalring an HTTP Binding and instantiating an host
    string url = "http://" + hostname + ":" + port;

    WebHttpBinding b = new WebHttpBinding();
    var host = new WebServiceHost(typeof(PaymentService), new Uri (url));

    // Adding the service to the host
    host.AddServiceEndpoint(typeof(IPaymentService), b, "");

    // Staring the Host server
    host.Open();
    Console.WriteLine("done!\n");
    Console.WriteLine("  Listening to "+hostname+":"+port+"\n\n");
    Console.WriteLine("Hit Return to shutdown the server.");
    Console.ReadLine();

    // Cleaning up and ending the hosting
    host.Close ();
    Console.WriteLine("Server shutdown complete!");
  }

}
