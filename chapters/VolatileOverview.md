# Volatile Implementation Overview [TCF case study]

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016



__Note__: To checkout this version, be sure that you are browsing the code stored in the [`releases/1.0`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/tree/release/v1.0) branch.

Interfaces are defined in the main package, as classical Java interfaces. Components are implemented in the `components` sub-package, in classes with `Bean` postfixed names. When a component refers to another one according to a _provides/requires_ association, the component implementation refers to the associated Interface instead of the concrete implementation to ensure decoupling.

![Architecture Implementation](https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/master/docs/archi_impl.png)

The `Cart` component is implemented twice, first as a `Stateful` bean, and then as a `Stateless` one (in the `cart` sub-package). 

![Architecture Implementation](https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/master/docs/archi_cart.png) 

As there is no persistent backend, we mocked the persistence layer using a `Singleton` bean named `Database`. It stores all the necessary data in static maps. We'll se in the next section how to remove this mock and use a real persistence layer thanks to EJB Entities.