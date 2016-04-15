# Volatile Implementation Overview 

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Unit testing _versus_ Integration testing](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/IntegrationTesting.md)


__Note__: To checkout this version, be sure that you are browsing the code stored in the [`releases/1.0`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/tree/release/v1.0) branch.

Interfaces are defined in the main package, as classical Java interfaces. Components are implemented in the `components` sub-package, in classes with `Bean` postfixed names. When a component refers to another one according to a _provides/requires_ association, the component implementation refers to the associated Interface instead of the concrete implementation to ensure decoupling.

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/archi_impl.png"/>
</p>

The `Cart` component is implemented twice, first as a `Stateful` bean, and then as a `Stateless` one (in the `cart` sub-package). 

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/archi_cart.png"/>
</p>

As there is no persistent backend, we mocked the persistence layer using a `Singleton` bean named `Database`. It stores all the necessary data in static maps. We'll se in the next section how to remove this mock and use a real persistence layer thanks to EJB Entities.

  * Next: [Message interceptors to support the NTUI (_Never Trust User Input_) golden rule](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Interceptors.md)