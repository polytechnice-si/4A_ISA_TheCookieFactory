# Integration Testing

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Consuming external Web Services (REST)](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Consuming_REST.md)

## Unit testing using Mocks

We are now facing an important issue: the J2E kernel is strongly coupled to the .Net system. One need to start the .Net server to make the J2E system available for tests purpose. To isolate the two systems for tests purpose, we need to _mock_ the BankAPI instead of using the real one.

```java
@Before
public void setUpContext() {
	memory.flush();
	items = new HashSet<>();
	items.add(new Item(Cookies.CHOCOLALALA, 3));
	items.add(new Item(Cookies.DARK_TEMPTATION, 2));
	// Customers
	john = new Customer("john", "1234-896983");  // ends with the secret YES Card number
	pat  = new Customer("pat", "1234-567890");   // should be rejected by the payment service
	// Mocking the external partner
	BankAPI mocked = mock(BankAPI.class);
	cashier.useBankReference(mocked);
	when(mocked.performPayment(eq(john), anyDouble())).thenReturn(true);
	when(mocked.performPayment(eq(pat),  anyDouble())).thenReturn(false);
}
```

## Integration testing between Java and .Net

But we also need to implement _Integration Tests_, that will ensure the end to end connection between our two systems. We rely on Maven to implement such a behavior: 

  - classical Unit tests are always run (_e.g._, when invoking `mvn package`)
  - Integration tests will be run during the integration testing phase (_e.g._, when invoking `mvn integration-test`).

We will differentiate classical tests and integration ones using a file name prefix: integration tests will match `*IntegrationTest`. In the `pom.xml` file we rely on the following configuration to implement these specifications:

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-surefire-plugin</artifactId>
	<version>2.17</version>
	<configuration>
		<reuseForks>false</reuseForks>
		<excludes>
			<exclude>**/*IntegrationTest.java</exclude>
		</excludes>
	</configuration>
	<executions>
		<execution>
			<id>integration-test</id>
			<goals>
				<goal>test</goal>
			</goals>
			<phase>integration-test</phase>
			<configuration>
				<excludes>
					<exclude>**/*Test.java</exclude>
				</excludes>
				<includes>
					<include>**/*IntegrationTest.java</include>
				</includes>
			</configuration>
		</execution>
	</executions>
</plugin>
``` 


  * Next: [Complete architecture overview](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/VolatileOverview.md)
