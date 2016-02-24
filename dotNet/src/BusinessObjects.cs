using System.Runtime.Serialization;
using System;

namespace Partner.Data {

  [DataContract(Namespace = "http://partner/external/payment/data/",
                Name = "PaymentRequest")]
  public class PaymentRequest
  {
    [DataMember]
    public string CreditCard { get; set; }

    [DataMember]
    public double Amount { get; set; }

    override public string ToString()
    {
      return "PaymentRequest[" + CreditCard + ", " + Amount + "]";
    }
  }

  [DataContract(Namespace = "http://partner/external/payment/data/",
                Name = "Payment")]
  public class Payment
  {
    [DataMember]
    public int Identifier { get; set; }

    [DataMember]
    public string CreditCard { get; set; }

    [DataMember]
    public double Amount { get; set; }

    [DataMember]
    public PaymentStatus Status { get; set; }

    [DataMember]
    public string Date { get; set; }

  }

  public enum PaymentStatus { Ok, Ko }

}
