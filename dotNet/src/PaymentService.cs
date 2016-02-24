using System;
using System.Net;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Collections.Generic;
using System.Linq;
using Partner.Data;

namespace Partner.Service {

  // The service is stateful, as it is only a Proof of Concept.
  // Services should be stateless, this is for demonstration purpose only.
  [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single)]
  public class PaymentService : IPaymentService
  {
    private const string MagicKey = "896983"; // ASCII code for "YES"

    private Dictionary<int, Payment> accounts = new Dictionary<int, Payment>();
    private int counter;

    public int ReceiveRequest(PaymentRequest request)
    {
      Console.WriteLine("ReceiveRequest: " + request);
      var payment = BuildPayment(request);
      accounts.Add(counter, payment);
      return counter;
    }

    public Payment FindPaymentById(int identifier)
    {
      if(!accounts.ContainsKey(identifier)) {
        WebOperationContext.Current.OutgoingResponse.StatusCode = HttpStatusCode.NotFound;
        return null;
      }
      return accounts[identifier];
    }

    public List<int> GetAllPaymentIds()
    {
      return accounts.Keys.ToList();
    }

    private Payment BuildPayment(PaymentRequest request)
    {
      var payment = new Payment();
      payment.Identifier = counter++;
      payment.CreditCard = request.CreditCard;
      payment.Amount = request.Amount;
      if (request.CreditCard.Contains(MagicKey)) {
        payment.Status = PaymentStatus.Ok;
      } else {
        payment.Status = PaymentStatus.Ko;
      }
      payment.Date = DateTime.Now.ToString();
      return payment;
    }

  }
}
