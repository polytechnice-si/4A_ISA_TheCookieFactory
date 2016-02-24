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

  public static void Main ()
  {
    Console.Write("Starting a WCF self-hosted .Net server... ");
    // Decalring an HTTP Binding and instantiating an host
    string url = "http://localhost:9090/";
    WebHttpBinding b = new WebHttpBinding();
    var host = new WebServiceHost(typeof(PaymentService), new Uri (url));

    // Adding the service to the host
    host.AddServiceEndpoint(typeof(IPaymentService), b, "");

    // Staring the Host server
    host.Open();
    Console.WriteLine("done!\n");
    Console.WriteLine("Hit Return to shutdown the server.");
    Console.ReadLine();

    // Cleaning up and ending the hosting
    host.Close ();
    Console.WriteLine("Server shutdown complete!");
  }

}
