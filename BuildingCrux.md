# _**Crux**_ Artifacts and Projects #

_**Crux**_ distribution includes a set of jar files:
  1. crux-runtime.jar - The runtime jar of _**Crux**_ framework.
  1. crux-dev.jar - The _**Crux**_ tools for development with _**Crux**_.
  1. crux-themes.jar - The standard css to all _**Crux**_ widgets.
  1. crux-gadgets.jar - Support for Gadgets creation with _**Crux**_
  1. crux-widgets.jar - The _**Crux**_ widget library.

These files can be generated using ant. All you need is download the _**Crux**_ project from its repository [repository](http://code.google.com/p/crux-framework/source/checkout):
  1. /Crux

In addition of _**Crux**_ project, we have two projects that you need to download to build the _**Crux**_ distribution:
  1. /CruxRelease - This project contains the ant build file needed to create the distribution file.
  1. /CruxQuickstart - This project contains the quickstart application.
  1. /HelloWorld - This project contains a sample helloWorld application included in distribution file.

# Building _**Crux**_ Projects #

To build _**Crux**_ you need:
  1. A JDK 1.6+ (Tested with sun JDK 1.6 and openJDK 1.6)
  1. Eclipse IDE (Tested with 3.6 on Linux and Windows)

After meet the previous requirements, you must:

  1. Checkout the projects from _**Crux**_ [repository](http://code.google.com/p/crux-framework/source/checkout);
  1. Run the ant build file /Crux/build/build.xml, calling the task `<dist>`;

To generate the final distribution file, just run the ant build file /CruxRelease/build/build.xml, calling the task `<dist>`;